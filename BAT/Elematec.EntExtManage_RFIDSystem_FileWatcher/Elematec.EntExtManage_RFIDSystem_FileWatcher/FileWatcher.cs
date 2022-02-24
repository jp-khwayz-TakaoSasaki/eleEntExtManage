using Elematec.EntExtManage_RFIDSystem_FileWatcher.Common;
using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Timers;

namespace Elematec.EntExtManage_RFIDSystem_FileWatcher
{
    public partial class FileWatcher : ServiceBase
    {
        #region [ private class ]
        /// <summary>
        /// 監視ファイル情報クラス
        /// </summary>
        private class MonitoringFileInfo
        {
            /// <summary>
            /// ファイルのフルパス
            /// </summary>
            public string FullPath { get; set; } = string.Empty;
            /// <summary>
            /// 最終書込時間
            /// </summary>
            public DateTime LastWrite { get; set; } = DateTime.MinValue;
            /// <summary>
            /// ファイルサイズ
            /// </summary>
            public long FileSize { get; set; } = 0;
        }
        #endregion

        #region [ private variable ]
        private readonly List<MonitoringFileInfo> _WatchFiles = new List<MonitoringFileInfo>();
        private readonly FileSystemWatcher _FileSystemWatcher = null;
        private readonly Timer _WatchTimer = new Timer();
        private bool _IsProcessing = false;
        private DateTime _LogDeletedDate = DateTime.Now.Date;
        #endregion

        #region [ コンストラクタ ]
        public FileWatcher()
        {
            InitializeComponent();

            /* Timerの設定 */
            _WatchTimer.Elapsed += new ElapsedEventHandler(OnTimer);
            _WatchTimer.Enabled = false;
            _WatchTimer.AutoReset = true;
            _WatchTimer.Interval = AppSettings.General.MonitoringInterval;
            _WatchTimer.Enabled = true;
            _WatchTimer.Stop();

            /* FileSystemWatcherの設定 */
            _FileSystemWatcher = new FileSystemWatcher
            {
                // 監視するディレクトリを指定
                Path = AppSettings.General.MonitoringFolder,
                // 最終アクセス日時、最終更新日時の変更を監視する
                NotifyFilter = (NotifyFilters.LastAccess | NotifyFilters.LastWrite | NotifyFilters.FileName | NotifyFilters.DirectoryName | NotifyFilters.Size),
                // 指定の拡張子のファイルを監視
                Filter = AppSettings.General.MonitoringFileExtension
            };

            //イベントハンドラの追加
            _FileSystemWatcher.Created += new FileSystemEventHandler(Watcher_Created);
            _FileSystemWatcher.Changed += new FileSystemEventHandler(Watcher_Changed);
            _FileSystemWatcher.EnableRaisingEvents = true;
        }
        #endregion

        #region ■Protectd メソッド■

        protected override void OnContinue()
        {
            Log.Logger.Info("OnContinue Called.");
            base.OnContinue();
        }

        protected override void OnPause()
        {
            Log.Logger.Info("OnPause Called.");
            base.OnPause();
        }

        protected override bool OnPowerEvent(PowerBroadcastStatus powerStatus)
        {
            Log.Logger.Info("OnPowerEvent Called.");
            return base.OnPowerEvent(powerStatus);
        }

        protected override void OnShutdown()
        {
            Log.Logger.Info("OnShutdown Called.");
            base.OnShutdown();
        }

        protected override void OnStart(string[] args)
        {
            Log.Logger.Info("OnStart Called.");
            // 監視終了
            _WatchTimer.Stop();

            // 開始前に作成されているファイル情報を取込
            string[] files = Directory.GetFiles(AppSettings.General.MonitoringFolder,
                                                AppSettings.General.MonitoringFileExtension,
                                                SearchOption.TopDirectoryOnly);
            foreach (string file in files)
            {
                // Textファイルが存在する場合はスキップする
                string textFileName = Path.ChangeExtension(file, AppSettings.General.OutputFileExtension);
                if (File.Exists(textFileName)) { continue; }
                // 監視情報にセットする
                UpdateFileInfo(file);
            }

            // 監視開始
            _WatchTimer.Start();
        }

        protected override void OnStop()
        {
            Log.Logger.Info("OnStop Called.");
            // 監視終了
            _WatchTimer.Stop();
        }

        #region [ Timer Event ]
        protected void OnTimer(object sender, EventArgs e)
        {
            try
            {
                // Timer処理中は何もしない
                if (_IsProcessing) { return; }

                // Timer処理中 ⇒ Timer停止
                _IsProcessing = true;
                _WatchTimer.Stop();

                // ログファイルの削除の確認
                try
                {
                    // 設定ファイルの削除日数を超えたログファイルを削除する
                    DateTime nowDate = DateTime.Now.Date;
                    TimeSpan delLogDiff = nowDate.Subtract(_LogDeletedDate);
                    if (AppSettings.General.LogPreserveDay < delLogDiff.Days)
                    {
                        // ログファイル削除
                        Log.FileDelete(AppSettings.General.LogPreserveDay);
                        // ログ削除日を更新
                        _LogDeletedDate = DateTime.Now.Date;
                    }
                }
                catch (Exception ex)
                {
                    Log.Logger.Error("アプリケーションログの削除に失敗しました。");
                    Log.Logger.Error(ex.Message);
                    Log.Logger.Error(ex.ToString());
                }

                foreach (var fileInfo in _WatchFiles.ToArray())
                {
                    // Lockされている場合は次のファイルへ
                    if (IsFileLocked(fileInfo.FullPath))
                    {
                        Log.Logger.Info("Lockされています。：" + fileInfo.FullPath);
                        continue;
                    }

                    // 最終書き込みから指定ミリ秒経過したファイルは書き込み完了と判断する
                    TimeSpan diff = DateTime.Now.Subtract(fileInfo.LastWrite);
                    if (diff.TotalMilliseconds < AppSettings.General.CompJudgMillisecond) { continue; }

                    // 10秒経過したファイルと同名のtxtファイルを作成する
                    string csvFileName = Path.Combine(AppSettings.General.OutputFolder, Path.GetFileName(fileInfo.FullPath));
                    string textFileName = Path.ChangeExtension(csvFileName, AppSettings.General.OutputFileExtension);
                    Log.Logger.Info("最終書込完了判定：" + fileInfo.FullPath);
                    Log.Logger.Info("txtファイル作成：" + textFileName);
                    // 0byteのファイルを作成する
                    using (StreamWriter streamWriter = new StreamWriter(textFileName, false))
                    {
                        streamWriter.Write("");
                        _WatchFiles.Remove(fileInfo);
                    }
                }

                // Timer処理終了 ⇒ Timer再開
                _IsProcessing = false;
                _WatchTimer.Start();
            }
            catch (Exception ex)
            {
                Log.Logger.Error(ex.Message);
                Log.Logger.Error(ex.ToString());
                // エラーメールを送信する
                StringBuilder text = new StringBuilder();
                text.AppendLine("取込処理が異常終了しました。")
                    .AppendLine("システム管理者に連絡してください")
                    .AppendLine("")
                    .AppendLine("エラー詳細")
                    .AppendLine(ex.Message);
                SendExceptionMessage("【入出庫管理システム】ファイル監視 処理異常", text.ToString());
            }
        }
        #endregion

        #region [ FileSystemWatcher Event ]
        /// <summary>
        /// ファイル作成イベント
        /// </summary>
        /// <param name="source"></param>
        /// <param name="e"></param>
        protected void Watcher_Created(object source, FileSystemEventArgs e)
        {
            // 新規作成以外は何もしない
            if (e.ChangeType != WatcherChangeTypes.Created) { return; }
            Log.Logger.Info("【ファイル作成】" + e.FullPath);
            // ファイル情報をチェックする
            UpdateFileInfo(e.FullPath);
        }
        /// <summary>
        /// ファイル変更イベント
        /// </summary>
        /// <param name="source"></param>
        /// <param name="e"></param>
        private void Watcher_Changed(object source, FileSystemEventArgs e)
        {
            // 更新以外何もしない
            if (e.ChangeType != WatcherChangeTypes.Changed) { return; }
            Log.Logger.Info("【ファイル更新】" + e.FullPath);
            // ファイル情報をチェックする
            UpdateFileInfo(e.FullPath);
        }
        #endregion

        #endregion

        #region [ private method ]
        /// <summary>
        /// ファイル情報更新処理
        /// </summary>
        /// <param name="fullPath">更新するファイルのフルパス</param>
        private void UpdateFileInfo(string fullPath)
        {
            FileInfo f = new FileInfo(fullPath);
            var fileInfo = _WatchFiles.Where(x => x.FullPath == fullPath).FirstOrDefault();
            if (fileInfo == null)
            {
                fileInfo = new MonitoringFileInfo
                {
                    FullPath = fullPath,
                    LastWrite = DateTime.Now,
                    FileSize = f.Length
                };
                _WatchFiles.Add(fileInfo);
            }
            else
            {
                fileInfo.LastWrite = DateTime.Now;
                fileInfo.FileSize = f.Length;
            }
            Log.Logger.Info("ファイルサイズ：" + f.Length.ToString() + "byte");
        }
        /// <summary>
        /// Lock判定処理
        /// </summary>
        /// <param name="path">Lock状態を取得するファイルのフルパス</param>
        /// <returns>true = Lockされている、false = Lockされていない</returns>
        private static bool IsFileLocked(string path)
        {
            try
            {
                using (FileStream stream = new FileStream(path, FileMode.Open, FileAccess.ReadWrite, FileShare.None))
                {
                    return false;
                }
            }
            catch (Exception)
            {
                return true;
            }
        }

        /// <summary>
        /// メールを送信します
        /// </summary>
        /// <param name="SubJect">件名</param>
        /// <param name="MessageText">メール本文</param>
        private void SendExceptionMessage(string SubJect, string MessageText)
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
        #endregion

    }
}
