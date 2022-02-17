using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using System.Timers;
using System.IO;
using Elematec.EntExtManage_RFIDSystem_Common.Common;
using Elematec.EntExtManage_RFIDSystem_Importer;
using Elematec.EntExtManage_RFIDSystem_Importer.AppSettings;
using Elematec.EntExtManage_RFIDSystem_Importer.Services;

namespace Elematec.EntExtManage_RFIDSystem
{
    public partial class Import : ServiceBase
    {
        Timer _timer;
        Log _log;
        private IList<string> _warehousingCheckList;
        private IList<string> _mushouhinCheckList;
        private IList<ImportArgument> _argumentList = new List<ImportArgument>();

        public Import()
        {
            InitializeComponent();

            ServiceEventLog = new System.Diagnostics.EventLog();
            if (!System.Diagnostics.EventLog.SourceExists("ServiceTest"))
            {
                System.Diagnostics.EventLog.CreateEventSource(
                    "ServiceTest", "Application");
            }
            ServiceEventLog.Source = "Import";
            ServiceEventLog.Log = "Application";

            _timer = new Timer();
            _timer.Interval = AppSettings.Import.ReceiveInterval;
            _timer.Elapsed += new ElapsedEventHandler(this.OnTimer);
            _timer.Start();
            _log = new Log();
        }

        #region ■Protectd メソッド■

        protected override void OnContinue()
        {
            ServiceEventLog.WriteEntry("OnContinue Called.");
            base.OnContinue();
        }

        protected override void OnPause()
        {
            ServiceEventLog.WriteEntry("OnPause Called.");
            base.OnPause();
        }

        protected override bool OnPowerEvent(PowerBroadcastStatus powerStatus)
        {
            ServiceEventLog.WriteEntry("OnPowerEvent Called.");
            return base.OnPowerEvent(powerStatus);
        }

        protected override void OnShutdown()
        {
            ServiceEventLog.WriteEntry("OnShutdown Called.");
            base.OnShutdown();
        }

        protected override void OnStart(string[] args)
        {
            //  サービス開始時に取込を行う場合は有効にする
            //  取込処理
            //this.Execute();
        }

        protected override void OnStop()
        {
        }

        protected void OnTimer(object sender, EventArgs e)
        {
            //ServiceEventLog.WriteEntry("タイマー停止");
            //_timer.Stop();

            //try
            //{
            //    ServiceEventLog.WriteEntry("取込開始");

            //    this.ExecuteImport();
            //    ServiceEventLog.WriteEntry("取込終了");
            //}
            //catch (Exception ex)
            //{
            //    this.SendExceptionMessage(ex, "");
            //    ServiceEventLog.WriteEntry("取込異常終了");
            //    //throw;
            //}

            //ServiceEventLog.WriteEntry("タイマー開始");
            //_timer.Start();

            //  取込処理
            this.Execute();
        }
        #endregion

        #region ■プライベートメソッド■

        private void Execute()
        {
            ServiceEventLog.WriteEntry("タイマー停止");
            _timer.Stop();

            try
            {
                ServiceEventLog.WriteEntry("取込開始");

                this.ExecuteImport();
                ServiceEventLog.WriteEntry("取込終了");
            }
            catch (Exception ex)
            {
                this.SendExceptionMessage(ex, "");
                ServiceEventLog.WriteEntry("取込異常終了");
                //throw;
            }

            ServiceEventLog.WriteEntry("タイマー開始");
            _timer.Start();
        }

        /// <summary>
        /// 取込処理を実行します
        /// </summary>
        private void ExecuteImport()
        {
            //  監視
            //  受信フォルダチェック
            if (!this.CheckReceiveFolder())
            {
                this.SendErrorMessage("受信フォルダが存在しません。");
                return;
            }
            //  作業関連フォルダチェック
            this.CheckFolder();

            //  過去ファイル削除
            this.RemoveFile();

            //  ログファイル削除
            this.RemoveLogFile();

            //  フォルダ監視
            this.ExistsRecieveFile();

            //  取込
            //  取込開始
            NyukoyoteiImporter nyuko = new NyukoyoteiImporter();
            IList<ImportArgument> importList = new List<ImportArgument>();
            importList = nyuko.Import(this._warehousingCheckList);

            foreach (var item in importList)
            {
                switch (item.RtnCode)
                {
                    case 1:
                        this.CheckErrorFile(item);
                        break;
                    case 9:
                        this.SendExceptionMessage(item.ImportFilePath, item.ErrorMessage, Utility.GetTorikomiKubunName(item.TorikomiKubn));
                        break;
                    default:
                        break;
                }
            }

            //  無償品取込
            MushouhinImporter mushouhin = new MushouhinImporter();
            importList = new List<ImportArgument>();
            importList = mushouhin.Import(this._mushouhinCheckList);

            foreach (var item in importList)
            {
                switch (item.RtnCode)
                {
                    case 1:
                        this.CheckErrorFile(item);
                        break;
                    case 9:
                        this.SendExceptionMessage(item.ImportFilePath, item.ErrorMessage, Utility.GetTorikomiKubunName(item.TorikomiKubn));
                        break;
                    default:
                        break;
                }
            }
        }

        /// <summary>
        /// 取込関連フォルダチェック
        /// </summary>
        /// <remarks>
        /// フォルダが存在しない場合は作成します
        /// </remarks>
        private void CheckFolder()
        {
            IList<string> targetFolders = new List<string>();
            targetFolders.Add(AppSettings.Import.SuccessedFolderPath);
            targetFolders.Add(AppSettings.Import.FailedFolderPath);
            targetFolders.Add(AppSettings.Import.WorkFolderPath);
            targetFolders.Add(AppSettings.Import.ErrorFolderPath);

            FileUtility futl = new FileUtility();
            futl.PrepairFolder(targetFolders);

        }

        /// <summary>
        /// 過去ファイルの削除
        /// </summary>
        private void RemoveFile()
        {
            FileUtility futl = new FileUtility();

            //  過去ファイル削除：取込成功フォルダ
            int preserveDay = AppSettings.Import.FilePreserveDay;
            string targetFolder = AppSettings.Import.SuccessedFolderPath;
            string targetFileName = AppSettings.Import.WarehousingBaseName;
            string fileExtension = AppSettings.Import.ImportFileExtension;

            futl.RemoveFileByLastWriteTime(preserveDay, targetFolder, targetFileName, fileExtension);

            //  過去ファイル削除：取込失敗フォルダパス
            targetFolder = AppSettings.Import.FailedFolderPath;

            futl.RemoveFileByLastWriteTime(preserveDay, targetFolder, targetFileName, fileExtension);

            //  エラーファイル削除：エラーフォルダ
            targetFolder = AppSettings.Import.ErrorFolderPath;

            futl.RemoveFileByLastWriteTime(preserveDay, targetFolder, targetFileName, fileExtension);
            futl.RemoveFileByLastWriteTime(preserveDay, targetFolder, targetFileName, "txt");
        }

        /// <summary>
        /// ログファイルの削除
        /// </summary>
        private void RemoveLogFile()
        {
            Log log = new Log();
            log.Logger.Info("Log.FileDelete start");
            log.FileDelete(AppSettings.General.LogFilePreserveDay);
        }

        /// <summary>
        /// 受信フォルダ存在チェック
        /// </summary>
        /// <returns></returns>
        private bool CheckReceiveFolder()
        {
            bool isExists = false;

            if (Directory.Exists(AppSettings.Import.ReceiveFolderPath))
            {
                isExists = true;
            }


            return isExists;
        }

        /// <summary>
        /// 受信ファイル存在チェック
        /// </summary>
        private void ExistsRecieveFile()
        {
            _warehousingCheckList = new List<string>();
            _mushouhinCheckList = new List<string>();

            string targetFolder = AppSettings.Import.ReceiveFolderPath;
            string warehaouseBaseName = AppSettings.Import.WarehousingBaseName;
            string mushouhinBaseName = AppSettings.Import.FreeProductBaseName;
            string checkfileExtension = AppSettings.Import.CheckFileExtension;

            //  入庫予定データをチェックします
            var files = new DirectoryInfo(targetFolder)
                                    .GetFiles(warehaouseBaseName + "*." + checkfileExtension, SearchOption.TopDirectoryOnly)
                                    .OrderBy(x => x.LastWriteTime);
            foreach (var file in files)
            {
                _warehousingCheckList.Add(file.FullName);
            }

            //  無償品データをチェックします
            files = new DirectoryInfo(targetFolder)
                                    .GetFiles(mushouhinBaseName + "*." + checkfileExtension, SearchOption.TopDirectoryOnly)
                                    .OrderBy(x => x.LastAccessTime);
            foreach (var file in files)
            {
                _mushouhinCheckList.Add(file.FullName);
            }
        }

        /// <summary>
        /// メールを送信します
        /// </summary>
        /// <param name="SubJect">件名</param>
        /// <param name="MessageText">メール本文</param>
        private void Send(string SubJect, string MessageText)
        {
            SendMail mail = new SendMail();

            mail.SmtpServer = AppSettings.Mail.SmtpServer;
            mail.SmtpAuth = AppSettings.Mail.SmtpAuth == 1 ? true : false;
            mail.SmtpUser = AppSettings.Mail.SmtpUser;
            mail.SmtpPassword = AppSettings.Mail.SmtpPassword;
            mail.SmtpPort = AppSettings.Mail.SmtpPort;
            mail.EnableSSL = AppSettings.Mail.EnableSSL == 1 ? true : false;

            mail.ToAddress = AppSettings.Mail.ToAddress;
            mail.FromAddress = AppSettings.Mail.FromAddress;
            mail.Subject = SubJect;
            mail.MailMessage = MessageText;

            mail.Send();
        }

        /// <summary>
        /// エラーファイル存在チェック
        /// </summary>
        /// <param name="argument">取込引数モデル</param>
        private void CheckErrorFile(ImportArgument argument)
        {
            //  エラーフファイル存在チェック
            if (!File.Exists(argument.ErrorFilePath))
            {
                return;
            }

            //  エラーファイルが存在した場合はメールを送信する
            this.SendWarningMessage(argument);
        }

        /// <summary>
        /// メール送信（例外用）
        /// </summary>
        /// <param name="ex">例外</param>
        private void SendExceptionMessage(Exception ex, string TorikomiKubunName)
        {
            this.SendExceptionMessage(null, ex.Message, TorikomiKubunName);
        }

        /// <summary>
        /// メール送信
        /// </summary>
        /// <param name="TargetFilePath">対象ファイル名</param>
        /// <param name="ErrorMessage">エラーメッセージ</param>
        private void SendExceptionMessage(string TargetFilePath, string ErrorMessage, string TorikomiKubnName)
        {
            string msg = "";
            string subject = string.Format("【入出庫管理システム】{0}データ取込　処理異常", TorikomiKubnName);
            StringBuilder text = new StringBuilder();

            text.AppendLine("取込処理が異常終了しました。")
                .AppendLine("システム管理者に連絡してください")
                .AppendLine("")
                .AppendLine("エラー詳細");
            if (!string.IsNullOrEmpty(TargetFilePath))
            {
                text.AppendLine(string.Format("対象ファイル：{0}", TargetFilePath))
                    .AppendLine("");
            }
            text.AppendLine(ErrorMessage);

            msg = text.ToString();

            this.Send(subject, msg);
        }

        /// <summary>
        /// メール送信（警告）
        /// </summary>
        /// <param name="argument">取込引数モデル</param>
        private void SendWarningMessage(ImportArgument argument)
        {
            string msg = "";
            string subject = string.Format("【入出庫管理システム】{0}データ取込　エラーデータあり", Utility.GetTorikomiKubunName(argument.TorikomiKubn));
            StringBuilder text = new StringBuilder();

            text.AppendLine("取込処理で取り込めなったデータがあります。")
                .AppendLine("内容を確認してください。")
                .AppendLine("")
                .AppendLine(string.Format("{0}:{1}", "対象ファイル", argument.TargetFileFile))
                .AppendLine(string.Format("{0}:{1}", "除外データ", argument.ErrorFilePath));

            msg = text.ToString();

            this.Send(subject, msg);

        }

        /// <summary>
        /// メール送信（Exception以外）
        /// </summary>
        /// <param name="message">メール本文</param>
        private void SendErrorMessage(string message)
        {
            string msg = "";
            string subject = "【入出庫管理システム】データ取込　処理エラー終了";
            StringBuilder text = new StringBuilder();

            text.AppendLine("取込処理を終了しました。")
                .AppendLine("システム管理者に連絡してください")
                .AppendLine("")
                .AppendLine("エラー詳細")
                .AppendLine(message);

            msg = text.ToString();

            this.Send(subject, msg);

        }

        #endregion
    }
}
