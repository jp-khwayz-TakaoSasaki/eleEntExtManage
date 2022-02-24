using System;
using System.IO;
using System.Text.RegularExpressions;
using log4net;

namespace Elematec.EntExtManage_RFIDSystem_FileWatcher.Common
{
    public class Log
    {
        private const string DirLog = "Log";

        public static ILog Logger;
        public static bool IsDebug = false;
        static Log()
        {
            //設定ファイルは、AssembleInfo.csへ記述すると自動的に読み込まる
            //インスタンスの取得
            Logger = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        }

        /// <summary>
        /// 指定日数を経過した古いログファイル削除
        /// </summary>
        /// Log4netの場合、設定ファイルにて、日毎の出力に設定した場合、
        /// 古いログファイルを消す機能がない。そのため、本機能を実装した。
        public static void FileDelete(int LogPreserveDay)
        {
            if (LogPreserveDay == 0) return;

            var now = DateTime.Now;
            var ts = new TimeSpan(LogPreserveDay, 0, 0, 0);
            var DeleteTime = now - ts;

            var LogDirName = Path.Combine(Environment.CurrentDirectory, DirLog);
            var files = Directory.GetFiles(LogDirName, "*", SearchOption.TopDirectoryOnly);
            foreach (var file in files)
            {
                var filename = Path.GetFileName(file);
                if (Regex.IsMatch(filename, @"^Info_[0-9]{8}\.log$") == false)
                {
                    continue;
                }

                var year = int.Parse(filename.Substring(5, 4));
                var month = int.Parse(filename.Substring(9, 2));
                var day = int.Parse(filename.Substring(11, 2));
                var filedate = new DateTime(year, month, day, 0, 0, 0);
                if (filedate <= DeleteTime)
                {
                    if (File.Exists(file))
                    {
                        File.Delete(file);
                        Log.Logger.Debug("ログファイルを削除しました:" + filename);
                    }
                }
            }
        }
    }
}
