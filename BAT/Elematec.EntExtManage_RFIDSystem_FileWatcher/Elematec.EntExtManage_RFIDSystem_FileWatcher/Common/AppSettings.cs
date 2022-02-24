using System;
using System.IO;
using System.Reflection;
using System.Xml;
using System.Xml.Schema;

namespace Elematec.EntExtManage_RFIDSystem_FileWatcher.Common
{
    public static class AppSettings
    {

        #region [ Class ]

        #region [ General ]
        public class General {
            /// <summary>
            /// ログファイル保存日数
            /// </summary>
            public static int LogPreserveDay { get; set; }
            /// <summary>
            /// 完了判断ミリ秒
            /// </summary>
            public static double CompJudgMillisecond { get; set; }
            /// <summary>
            /// ファイルの書き込みの監視発生間隔
            /// </summary>
            public static double MonitoringInterval { get; set; }
            /// <summary>
            /// 監視フォルダのパス
            /// </summary>
            public static string MonitoringFolder { get; set; }
            /// <summary>
            /// ファイルを出力するフォルダのパス
            /// </summary>
            public static string OutputFolder { get; set; }
            /// <summary>
            /// 監視ファイルの拡張子
            /// </summary>
            public static string MonitoringFileExtension { get; set; }
            /// <summary>
            /// 出力ファイルの拡張子
            /// </summary>
            public static string OutputFileExtension { get; set; }

            public static void ReadElement(XmlReader xr)
            {
                switch (xr.Name)
                {
                    case "LogPreserveDay":
                        LogPreserveDay = Convert.ToInt32(xr.ReadString().Trim());
                        break;
                    case "CompJudgMillisecond":
                        CompJudgMillisecond = Convert.ToDouble(xr.ReadString().Trim());
                        break;
                    case "MonitoringInterval":
                        MonitoringInterval = Convert.ToDouble(xr.ReadString().Trim());
                        break;
                    case "MonitoringFolder":
                        MonitoringFolder = xr.ReadString().Trim();
                        break;
                    case "OutputFolder":
                        OutputFolder = xr.ReadString().Trim();
                        break;
                    case "MonitoringFileExtension":
                        MonitoringFileExtension = xr.ReadString().Trim();
                        break;
                    case "OutputFileExtension":
                        OutputFileExtension = xr.ReadString().Trim();
                        break;
                    default:
                        break;
                }
            }

            public static void Save(XmlDocument xmlDoc)
            {
                XmlNode newNode;
                XmlNodeList nodelist;
                /* LogPreserveDay */
                nodelist = xmlDoc.SelectNodes("configuration/General/LogPreserveDay");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = LogPreserveDay.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }
                /* CompJudgMillisecond */
                nodelist = xmlDoc.SelectNodes("configuration/General/CompJudgMillisecond");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = LogPreserveDay.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }
                /* MonitoringInterval */
                nodelist = xmlDoc.SelectNodes("configuration/General/MonitoringInterval");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = LogPreserveDay.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }
                /* MonitoringFolder */
                nodelist = xmlDoc.SelectNodes("configuration/General/MonitoringFolder");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = MonitoringFolder.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }
                /* OutputFolder */
                nodelist = xmlDoc.SelectNodes("configuration/General/OutputFolder");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = OutputFolder.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }
                /* MonitoringFileExtension */
                nodelist = xmlDoc.SelectNodes("configuration/General/MonitoringFileExtension");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = MonitoringFileExtension.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }
                /* OutputFileExtension */
                nodelist = xmlDoc.SelectNodes("configuration/General/OutputFileExtension");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = OutputFileExtension.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

            }

        }
        #endregion

        #region [ Mail ]
        public class Mail
        {
            /// <summary>
            /// SMTPサーバアドレス
            /// </summary>
            public static string SmtpServer { get; set; }

            /// <summary>
            /// SMTP認証有無
            /// </summary>
            public static int SmtpAuth { get; set; }

            /// <summary>
            /// SMTPユーザー
            /// </summary>
            public static string SmtpUser { get; set; }

            /// <summary>
            /// SMTPパスワード
            /// </summary>
            public static string SmtpPassword { get; set; }

            /// <summary>
            /// SMTPポート
            /// </summary>
            public static int SmtpPort { get; set; }

            /// <summary>
            /// SSL送信有無
            /// </summary>
            public static int EnableSSL { get; set; }

            /// <summary>
            /// 送信先メールアドレス
            /// </summary>
            public static string ToAddress { get; set; }

            /// <summary>
            /// 送信元メールアドレス
            /// </summary>
            public static string FromAddress { get; set; }

            public static void ReadElement(XmlReader xr)
            {
                if (xr.Name == "SmtpServer") { SmtpServer = xr.ReadString().Trim(); }
                else if (xr.Name == "SmtpAuth") { SmtpAuth = Convert.ToInt32(xr.ReadString().Trim()); }
                else if (xr.Name == "SmtpUser") { SmtpUser = xr.ReadString().Trim(); }
                else if (xr.Name == "SmtpPassword") { SmtpPassword = xr.ReadString().Trim(); }
                else if (xr.Name == "SmtpPort") { SmtpPort = Convert.ToInt32(xr.ReadString().Trim()); }
                else if (xr.Name == "EnableSSL") { EnableSSL = Convert.ToInt32(xr.ReadString().Trim()); }
                else if (xr.Name == "ToAddress") { ToAddress = xr.ReadString().Trim(); }
                else if (xr.Name == "FromAddress") { FromAddress = xr.ReadString().Trim(); }
                else { }
            }

            public static void Save(XmlDocument xmlDoc)
            {
                const string targetNode = "configuration/Mail/";

                XmlNode newNode;
                XmlNodeList nodelist;

                nodelist = xmlDoc.SelectNodes(targetNode + "SmtpServer");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = SmtpServer.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "SmtpAuth");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = Convert.ToInt32(SmtpAuth).ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "SmtpUser");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = SmtpUser.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "SmtpPassword");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = SmtpPassword.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "SmtpPort");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = SmtpPort.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "EnableSSL");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = Convert.ToInt32(EnableSSL).ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "ToAddress");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = ToAddress.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "FromAddress");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = FromAddress.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }
            }

        }
        #endregion

        #endregion

        private enum MyXmlNode : int
        {
            None = 0,
            General = 1,
            Mail = 2
        }

        #region [ ReadXmlFile ]
        public static bool ReadXmlFile()
        {
            /* 初期化 */
            return ReadXmlFile(GetCurrentDirectory() + "\\AppSettings\\AppSettings.xml");
        }

        public static bool ReadXmlFile(string filePath)
        {
            try
            {
                MyXmlNode node = MyXmlNode.None;
                if (!File.Exists(filePath))
                {
                    // 設定ファイルが存在しない場合は、何もせずに終了
                    return false;
                }
                XmlSchemaSet sc = new XmlSchemaSet();
                sc.Add(null, GetCurrentDirectory() + "\\AppSettings\\AppSettings.xsd");

                XmlReaderSettings st = new XmlReaderSettings
                {
                    IgnoreWhitespace = true, // 空白を無視する
                    IgnoreComments = true,   // コメントを無視する
                    ValidationType = ValidationType.Schema,
                    Schemas = sc
                };
                using (XmlReader xr = XmlReader.Create(filePath, st))
                {
                    while (xr.Read())
                    {
                        if (xr.NodeType == XmlNodeType.Whitespace) { continue; }
                        if (xr.NodeType == XmlNodeType.Element)
                        {
                            switch (xr.Name)
                            {
                                case "General":
                                    node = MyXmlNode.General;
                                    break;
                                case "SendMail":
                                    node = MyXmlNode.Mail;
                                    break;
                                default:
                                    break;
                            }
                            switch (node)
                            {
                                case MyXmlNode.General:
                                    General.ReadElement(xr);
                                    break;
                                case MyXmlNode.Mail:
                                    Mail.ReadElement(xr);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    xr.Close();
                }
                return true;
            }
            catch (XmlSchemaValidationException validex)
            {
#if false
                /* update t.tutagawa 2010.02.17
                 * ReadXmlFile内の再帰処理が無限ループに陥る可能性を排除するフラグを準備
                 * （xsdファイルが編集されていたら無限ループの可能性がある）
                 */
                if (this.m_ExecReflexive != true)
                {
                    if (validex != null) { validexp = null; }
                    this.m_ExecReflexive = true;        /* 2回目以降は、再帰処理を行わない */

                    if (this.CreateXmlFile(filepath) != true)
                    {
                        return false;
                    }
                    return this.ReadXmlFile(filepath);  //再帰
                }
                else
                {
                    msg = "XMLファイルのフォーマットが不正です。";
                    msg += "\r\n";
                    msg += filepath;
                    msg += "\r\n";
                    msg += "\r\n";
                    msg += "[エラー詳細]";
                    msg += "\r\n";
                    msg += validex.ToString();
                    System.Windows.Forms.MessageBox.Show(msg,
                                                         "Error",
                                                         System.Windows.Forms.MessageBoxButtons.OK,
                                                         System.Windows.Forms.MessageBoxIcon.Error);
                }
#else
                throw validex;
#endif
            }
            catch (Exception ex)
            {
#if false
                msg = "XMLファイルの読み込み中にエラーが発生しました。";
                msg += "\r\n";
                msg += "\r\n";
                msg += "[エラー詳細]";
                msg += "\r\n";
                msg += exp.ToString();
                System.Windows.Forms.MessageBox.Show(msg,
                                                     "Error",
                                                     System.Windows.Forms.MessageBoxButtons.OK,
                                                     System.Windows.Forms.MessageBoxIcon.Error);
#else
                throw ex;
#endif
            }
        }
        #endregion

        #region [ SaveXmlFile ]
        public static void SaveXmlFile()
        {
            SaveXmlFile(GetCurrentDirectory() + "\\xml\\" + "AppSettings.xml");
        }
        public static void SaveXmlFile(string filepath)
        {
            // 設定ファイルが存在しない場合は、何もしない
            if (!File.Exists(filepath)) { return; }
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(filepath);
            General.Save(xmlDoc);
            Mail.Save(xmlDoc);
            xmlDoc.Save(filepath);
        }
        #endregion

        #region [ GetCurrentDirectory ]
        private static string GetCurrentDirectory()
        {
            string fqn = Assembly.GetExecutingAssembly().ManifestModule.FullyQualifiedName;
            FileInfo finfo = new FileInfo(fqn);
            return finfo.DirectoryName;
        }
        #endregion

    }
}
