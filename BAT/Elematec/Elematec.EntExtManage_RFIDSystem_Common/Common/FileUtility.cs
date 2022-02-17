using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;

namespace Elematec.EntExtManage_RFIDSystem_Common.Common
{
    /// <summary>
    /// ファイル関連ユーティリティ
    /// </summary>
    public class FileUtility
    {
        #region ■コンストラクタ■

        #endregion

        #region ■プロパティ■

        #endregion

        #region ■パブリックメソッド■

        /// <summary>
        /// ファイルを削除します
        /// </summary>
        /// <param name="PreserveDay">保持期間</param>
        /// <param name="TargetFolder">対象フォルダ名</param>
        /// <param name="TargetFileName">対象ファイル名</param>
        /// <param name="FileExtension">ファイル拡張子</param>
        /// <remarks>
        /// システム日付から保持期間（PreserveDay）を経過したファイルをファイルの最終更新日と比較して削除します
        /// </remarks>
        public void RemoveFileByLastWriteTime(int PreserveDay, string TargetFolder, string TargetFileName, string FileExtension)
        {
            if (PreserveDay == 0) return;

            var now = DateTime.Today;
            var ts = new TimeSpan(PreserveDay, 0, 0, 0);

            var deleteTime = now - ts;
            deleteTime = deleteTime.AddDays(1);

            var files = new DirectoryInfo(TargetFolder)
                                    .GetFiles(TargetFileName + "*." + FileExtension, SearchOption.TopDirectoryOnly);
            foreach (var file in files)
            {
                if (deleteTime >= file.LastWriteTime)
                {
                    file.Delete();
                }
            }

        }

        public void PrepairFolder(IList<string> forderNames)
        {
            foreach(var forderName in forderNames)
            {
                PrepairFolder(forderName);
            }
        }

        public void PrepairFolder(string folderName)
        {
            if (!Directory.Exists(folderName))
            {
                Directory.CreateDirectory(folderName);
            }
        }
        #endregion

        #region ■プライベートメソッド■

        #endregion

    }
}
