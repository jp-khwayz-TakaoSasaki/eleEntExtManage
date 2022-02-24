using System;
using System.IO;
using System.Net.Mail;
using System.Net.Mime;
using System.Net.Sockets;
using System.Text;

namespace Elematec.EntExtManage_RFIDSystem_FileWatcher.Common
{
    /// <summary>
    /// メール送信用サービス
    /// </summary>
    public class SendMail
    {
        #region ■コンストラクタ■

        /// <summary>
        /// コンストラクタ
        /// </summary>
        public SendMail()
        {
            this.AdjustToAddressByDoubleQuote = true;
            this.SubjectDoubleEncode = true;
        }

        #endregion

        #region ■プロパティ■

        /// <summary>
        /// 送信元メールアドレス
        /// </summary>
        public string FromAddress { get; set; }

        /// <summary>
        /// 送信先メールアドレス
        /// </summary>
        public string ToAddress { get; set; }

        /// <summary>
        /// 送信アドレスダブルクォート補正有無
        /// </summary>
        public bool AdjustToAddressByDoubleQuote { get; set; }

        /// <summary>
        /// CCメールアドレス
        /// </summary>
        public string CCAddress { get; set; }

        /// <summary>
        /// BCCメールアドレス
        /// /// </summary>
        public string BCCAddress { get; set; }

        /// <summary>
        /// 件名二重エンコード有無。.NET Framework 4.5の場合、trueとする。
        /// </summary>
        /// <remarks>
        /// http://blogs.technet.com/b/exchangeteamjp/archive/2012/10/05/3524293.aspx
        /// http://nobukoshi802.blogspot.jp/2013/07/csmtpclient-subject.html
        /// </remarks>
        public bool SubjectDoubleEncode { get; set; }

        /// <summary>
        /// 件名
        /// </summary>
        public string Subject { get; set; }

        /// <summary>
        /// メール本文
        /// </summary>
        public string MailMessage { get; set; }

        /// <summary>
        /// POP before SMTP
        /// </summary>
        public bool PopBeforeSmtp { get; set; }

        /// <summary>
        /// POP サーバ
        /// </summary>
        public string PopServer { get; set; }

        /// <summary>
        /// POP ポート番号
        /// </summary>
        public int PopPort { get; set; }

        /// <summary>
        /// POP ユーザー
        /// </summary>
        public string PopUser { get; set; }

        /// <summary>
        /// POP パスワード
        /// </summary>
        public string PopPassword { get; set; }

        /// <summary>
        /// SMTP認証
        /// </summary>
        public bool SmtpAuth { get; set; }

        /// <summary>
        /// SMTP サーバ
        /// </summary>
        public string SmtpServer { get; set; }

        /// <summary>
        /// SMTP ポート番号
        /// </summary>
        public int SmtpPort { get; set; }

        /// <summary>
        /// SMTP ユーザー
        /// </summary>
        public string SmtpUser { get; set; }

        /// <summary>
        /// SMTP パスワード
        /// </summary>
        public string SmtpPassword { get; set; }

        /// <summary>
        /// 送信にＳＳＬ使用の有無
        /// </summary>
        public bool EnableSSL { get; set; }

        #endregion

        #region ■パブリックメソッド■

        /// <summary>
        /// メールを送信します
        /// </summary>
        public void Send()
        {
            MailMessage msg = null;

            try
            {
                //Encoding.RegisterProvider(CodePagesEncodingProvider.Instance);
                // メールメッセージ作成
                Encoding enc = Encoding.GetEncoding(50220);   // iso-2022-jp

                msg = new MailMessage();
                msg.From = new MailAddress(this.FromAddress);
                if (this.AdjustToAddressByDoubleQuote)
                {
                    msg.To.Add(new MailAddress(EditToAddress(this.ToAddress)));
                }
                else
                {
                    msg.To.Add(new MailAddress(this.ToAddress));
                }

                if (!String.IsNullOrEmpty(this.CCAddress))
                {
                    if (this.AdjustToAddressByDoubleQuote)
                    {
                        msg.CC.Add(new MailAddress(EditToAddress(this.CCAddress)));
                    }
                    else
                    {
                        msg.CC.Add(new MailAddress(this.CCAddress));
                    }
                }

                if (!String.IsNullOrEmpty(this.BCCAddress))
                {
                    if (this.AdjustToAddressByDoubleQuote)
                    {
                        msg.Bcc.Add(new MailAddress(EditToAddress(this.BCCAddress)));
                    }
                    else
                    {
                        msg.Bcc.Add(new MailAddress(this.BCCAddress));
                    }
                }

                if (this.SubjectDoubleEncode)
                {
                    msg.Subject = EncodeSubject(EncodeSubject(this.Subject, enc), enc);
                }
                else
                {
                    msg.Subject = EncodeSubject(this.Subject, enc);
                }
                msg.BodyEncoding = enc;
                msg.IsBodyHtml = false;

                // Encoding: 7bit としてメッセージを格納(SoftBank対応)
                AlternateView plain7bit = AlternateView.CreateAlternateViewFromString(this.MailMessage, enc, MediaTypeNames.Text.Plain);
                plain7bit.TransferEncoding = System.Net.Mime.TransferEncoding.SevenBit;
                msg.AlternateViews.Add(plain7bit);


                // POP before SMTP
                if (this.PopBeforeSmtp)
                {
                    // ＰＯＰ認証
                    PopAuth(this.PopServer, this.PopPort, this.PopUser, this.PopPassword);
                }

                // メール送信
                SmtpClient smtp = new SmtpClient();
                smtp.EnableSsl = this.EnableSSL;
                smtp.Host = this.SmtpServer;
                smtp.Port = this.SmtpPort;
                // SMTP認証
                if (this.SmtpAuth)
                {
                    smtp.Credentials = new System.Net.NetworkCredential(this.SmtpUser, this.SmtpPassword);
                }

                smtp.Timeout = 3 * 60000;               //  Set the timeout to 3 minutes
                smtp.ServicePoint.MaxIdleTime = 1000;   //  ウィルスチェックソフトによっては送信がすぐにされない問題回避。
                smtp.Send(msg);
            }
            finally
            {
                if (msg != null)
                {
                    msg.Dispose();
                }
            }
        }

        /// <summary>
        /// ＰＯＰ認証
        /// </summary>
        /// <param name="server">POPサーバ</param>
        /// <param name="port">ポート番号</param>
        /// <param name="user">ユーザー</param>
        /// <param name="password">パスワード</param>
        public static void PopAuth(string server, int port, string user, string password)
        {
            string msg = string.Empty;
            NetworkStream stream;

            // TcpClientの作成
            TcpClient client = new TcpClient();
            // タイムアウトの設定
            //client.ReceiveTimeout = 10000;
            client.SendTimeout = 10000;
            client.ReceiveTimeout = 3 * 60000;  // Set the timeout to 3 minutes
            client.SendTimeout = 3 * 60000;     // Set the timeout to 3 minutes

            try
            {
                // POPサーバーに接続
                client.Connect(server, port);
                // ストリームの取得
                stream = client.GetStream();
                // 受信
                msg = ReceiveData(stream);

                // USERの送信
                SendData(stream, String.Format("USER {0}\r\n", user));
                // 受信
                msg = ReceiveData(stream);

                // PASSの送信
                SendData(stream, String.Format("PASS {0}\r\n", password));
                // 受信
                msg = ReceiveData(stream);

                // QUITの送信
                SendData(stream, "QUIT\r\n");
                // 受信
                msg = ReceiveData(stream);
            }
            catch (Exception ex)
            {
                throw new ArgumentException("POP認証に失敗しました。サーバ名、ユーザ名、パスワードに誤りがある可能性があります。", ex);
            }
            finally
            {
                // 切断
                client.Close();
            }
        }

        #endregion

        #region ■プライベートメソッド■

        /// <summary>
        /// TOアドレス編集
        /// ※RFC 2821(2822)に反しているアドレスを回避する為、@より前を『"』で囲む
        /// </summary>
        /// <param name="address">メールアドレス</param>
        /// <returns></returns>
        static string EditToAddress(string address)
        {
            string[] s = address.Split('@');
            if (s.Length < 2)
            {
                return address;
            }

            if (s[1] == "docomo.ne.jp" || s[1] == "ezweb.ne.jp" || s[1] == "dion.ne.jp" || s[1] == "auone-net.jp")
            {
                return String.Format("{0}{1}{0}@{2}", @"""", s[0], s[1]);
            }
            else
            {
                return address;
            }
        }

        /// <summary>
        /// 件名を「=?iso-2022-jp?B?<エンコード文字列>?=」形式(RFC2047形式)に変換
        /// </summary>
        /// <param name="subject">件名</param>
        /// <param name="enc">エンコード</param>
        /// <returns>Base64でエンコードされた件名</returns>
        string EncodeSubject(string subject, Encoding enc)
        {
            // Base64でエンコードする
            string ret = System.Convert.ToBase64String(enc.GetBytes(subject));
            // RFC2047形式に
            return String.Format("=?{0}?B?{1}?=", enc.BodyName, ret);
        }

        /// <summary>
        /// データ受信
        /// </summary>
        /// <param name="stream">NetworkStream オブジェクト</param>
        /// <returns>受信メッセージ</returns>
        static string ReceiveData(NetworkStream stream)
        {
            byte[] data = new byte[255];
            int len;
            string msg;
            MemoryStream ms = new MemoryStream();
            Encoding enc = Encoding.GetEncoding(50220);  // iso-2022-jp

            // すべて受信する(無限ループに陥る恐れあり)
            try
            {
                do
                {
                    // 受信
                    len = stream.Read(data, 0, data.Length);
                    ms.Write(data, 0, len);
                    // 文字列に変換する
                    msg = enc.GetString(ms.ToArray());

                } while (stream.DataAvailable || (msg.StartsWith("-ERR") && !msg.EndsWith("\r\n")));
            }
            finally
            {
                ms.Close();
                ms.Dispose();
            }

            // 表示
            Console.Write(("S: " + msg));

            // 『-ERR』を受け取った時は例外をスロー
            if (msg.StartsWith("-ERR"))
            {
                throw new ApplicationException("-ERR: Received Error");
            }

            return msg;
        }

        /// <summary>
        /// データ送信
        /// </summary>
        /// <param name="stream">NetworkStream オブジェクト</param>
        /// <param name="message">メッセージ</param>
        static void SendData(NetworkStream stream, string message)
        {
            Encoding enc = Encoding.ASCII;

            // byte型配列に変換
            byte[] data = enc.GetBytes(message);
            // 送信
            stream.Write(data, 0, data.Length);

            // 表示
            Console.Write(("C: " + message));
        }

        #endregion
    }
}
