using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Xml.Schema;
using Elematec.EntExtManage_RFIDSystem_Common.Common;

namespace Elematec.EntExtManage_RFIDSystem_Importer
{
    public abstract class ImportBase
    {
        protected Log Log { get; set; }

        #region ■プロテクトメソッド■

        protected ImportBase()
        {
            Log = new Log();
        }
        #endregion

        #region ■プロテクトメソッド■

        /// <summary>
        /// 設定ファイルを取得します
        /// </summary>
        protected void GetAppsettings()
        {
            #region [ 設定ファイルの読み取り ]
            try
            {
                if (!AppSettings.AppSettings.ReadXmlFile()) throw new Exception("AppSettings.ReadXmlFile() return false");
            }
            catch (XmlSchemaValidationException)
            {
                /* 設定ファイルの内容が定義情報と異なるエラー
                 */
                Log.Logger.Info("設定ファイルの内容が定義情報と異なるエラー");
                return;
            }
            catch (Exception)
            {
                /* 設定ファイルの読み取りエラー
                 */
                Log.Logger.Info("設定ファイルの読み取りエラー");
                return;
            }
            finally
            {
            }

            #endregion

        }

        /// <summary>
        /// 取込成功フォルダに取込ファイルを移動します
        /// </summary>
        /// <param name="file">移動元ファイル（フルパス）</param>
        protected void MoveSuccessFile(string file)
        {
            //  チェックファイル名の分割
            string fileName = Path.GetFileName(file);

            string successFolder = AppSettings.AppSettings.Import.SuccessedFolderPath;

            //  ファイルの移動
            string dsetFilePath = Path.Combine(successFolder, fileName);
            File.Move(file, dsetFilePath);
        }

        /// <summary>
        /// 取込失敗フォルダに取込ファイルを移動します
        /// </summary>
        /// <param name="file">移動元ファイル（フルパス）</param>
        protected void MoveFaildFile(string file)
        {
            //  チェックファイル名の分割
            string fileName = Path.GetFileName(file);

            string faildFolder = AppSettings.AppSettings.Import.FailedFolderPath;

            //  ファイルの移動
            string dsetFilePath = Path.Combine(faildFolder, fileName);
            File.Move(file, dsetFilePath);
        }

        /// <summary>
        /// エラーファイル名（フルパス）作成します
        /// </summary>
        /// <param name="importFilePath">取込ファイル名（フルパス）</param>
        /// <returns></returns>
        protected string CreateErrorFilePath(string importFilePath)
        {
            string baseName = Path.GetFileNameWithoutExtension(importFilePath);
            string errorFilePath = Path.Combine(AppSettings.AppSettings.Import.ErrorFolderPath, string.Format("{0}{1}.{2}", baseName, "_err", AppSettings.AppSettings.Import.ImportFileExtension));

            return errorFilePath;
        }

        /// <summary>
        /// 取り込むファイルを作業フォルダに移動します
        /// </summary>
        /// <param name="checkFile">チェック用ファイル名</param>
        /// <returns></returns>
        protected string MoveFile(string checkFile)
        {
            //  チェックファイル名の分割
            string baseName = Path.GetFileNameWithoutExtension(checkFile);

            //  移動先でのファイル名を生成
            //  同名のファイルが既に取り込まれていた場合BulkInsertでエラーとなるため、ファイル名の後ろに日時（yyyyMMddHHmmss形式）付与し移動する
            //  ※Bulk Ineert時指定したエラーファイル名が既に存在する為の対応
            var dsetFileBaseName = string.Format("{0}_{1}", baseName, DateTime.Now.ToString("yyyyMMddHHmmss"));

            //  ファイルの移動
            string sourceFilePath = Path.Combine(AppSettings.AppSettings.Import.ReceiveFolderPath, string.Format("{0}.{1}", baseName, AppSettings.AppSettings.Import.ImportFileExtension));
            string dsetFilePath = Path.Combine(AppSettings.AppSettings.Import.WorkFolderPath, string.Format("{0}.{1}", dsetFileBaseName, AppSettings.AppSettings.Import.ImportFileExtension));
            File.Move(sourceFilePath, dsetFilePath);

            //  チェックファイルの削除
            File.Delete(checkFile);

            return dsetFilePath;
        }

        /// <summary>
        /// 作業関連フォルダチェック
        /// </summary>
        protected void CheckFolder()
        {
            IList<string> targetFolders = new List<string>();
            targetFolders.Add(AppSettings.AppSettings.Import.SuccessedFolderPath);
            targetFolders.Add(AppSettings.AppSettings.Import.FailedFolderPath);
            targetFolders.Add(AppSettings.AppSettings.Import.WorkFolderPath);
            targetFolders.Add(AppSettings.AppSettings.Import.ErrorFolderPath);

            FileUtility futl = new FileUtility();
            futl.PrepairFolder(targetFolders);
        }

        #endregion
    }
}
