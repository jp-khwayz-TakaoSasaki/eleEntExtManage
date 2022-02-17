using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Xml.Schema;

using Elematec.EntExtManage_RFIDSystem_Common.Common;
using Elematec.EntExtManage_RFIDSystem_Common.Database;
using Elematec.EntExtManage_RFIDSystem_Importer.AppSettings;
using Elematec.EntExtManage_RFIDSystem_Importer.Data;

namespace Elematec.EntExtManage_RFIDSystem_Importer.Services
{
    /// <summary>
    /// 入庫予定インポート関連サービス
    /// </summary>
    public class NyukoyoteiImporter : ImportBase
    {

        #region ■コンストラクタ■

        /// <summary>
        /// コンストラクタ
        /// </summary>
        public NyukoyoteiImporter()
        {
        }

        #endregion

        #region ■プロパティ■

        #endregion

        #region ■パブリックメソッド■

        /// <summary>
        /// 入庫予定データ（CSV）を取込ます
        /// </summary>
        /// <param name="targetFileList">取込対象ファイルリスト</param>
        /// <returns>取込結果リスト</returns>
        public IList<ImportArgument> Import(IList<string> targetFileList)
        {
            Log.Logger.Info("入庫予定インポート Start");

            //  設定ファイル取得
            GetAppsettings();

            //  作業関連フォルダチェック
            CheckFolder();

            string importFilePath = "";
            ExecuteArgument executeArgument = new ExecuteArgument();
            IList<ImportArgument> importArgumentList = new List<ImportArgument>();
            ImportArgument importArgument = new ImportArgument();

            Log.Logger.Info(string.Format("取込ファイル:{0}件", targetFileList.Count.ToString()));

            //  1ファイル毎に取込を行う
            foreach (var file in targetFileList)
            {
                Log.Logger.Info(string.Format("{0}{1}", "取込ファイル：", file));

                //  作業フォルダへの移動
                importFilePath = MoveFile(file);
                //  エラーファイル名生成
                string errorFilePath = CreateErrorFilePath(importFilePath);

                executeArgument = new ExecuteArgument();

                //  取込（BulkInsert）
                executeArgument = this.ExecuteImport(importFilePath, errorFilePath);

                //  異常があった場合は更新処理は行わず次のファイルを取り込む
                if (executeArgument.RtnCode == 9)
                {
                    importArgument = new ImportArgument
                    {
                        TorikomiKubn = TorikomiKubun.Nyukoyotei,
                        TargetFileFile = file,
                        ImportFilePath = importFilePath,
                        ErrorFilePath = errorFilePath,
                        RtnCode = executeArgument.RtnCode,
                        ErrorNumber = executeArgument.ErrorNumber,
                        ErrorMessage = executeArgument.ErrorMessage
                    };
                    importArgumentList.Add(importArgument);

                    //  ログ出力
                    Log.Logger.Error("処理異常");
                    Log.Logger.Error(string.Format("{0}{1}", "取込ファイル：", file));
                    Log.Logger.Error(string.Format("{0}{1}", "エラー：", executeArgument.ErrorNumber.ToString()));
                    Log.Logger.Error(string.Format("{0}{1}", "エラーメッセージ：", executeArgument.ErrorMessage));
                    MoveFaildFile(importFilePath);
                    break;
                }
                else
                {
                    //  更新処理実行
                    executeArgument = this.RegistNyukoYotei();

                    importArgument = new ImportArgument
                    {
                        TorikomiKubn = TorikomiKubun.Nyukoyotei,
                        TargetFileFile = file,
                        ImportFilePath = importFilePath,
                        ErrorFilePath = errorFilePath,
                        RtnCode = executeArgument.RtnCode,
                        ErrorNumber = executeArgument.ErrorNumber,
                        ErrorMessage = executeArgument.ErrorMessage
                    };
                    importArgumentList.Add(importArgument);

                    if (executeArgument.RtnCode == 9)
                    {
                        //  ログ出力
                        Log.Logger.Error("処理異常");
                        Log.Logger.Error(string.Format("{0}{1}", "取込ファイル", file));
                        Log.Logger.Error(string.Format("{0}{1}", "エラー：", executeArgument.ErrorNumber.ToString()));
                        Log.Logger.Error(string.Format("{0}{1}", "エラーメッセージ：", executeArgument.ErrorMessage));
                        MoveFaildFile(importFilePath);
                        break;
                    }
                    else
                    {
                        MoveSuccessFile(importFilePath);
                    }
                }
            }

            Log.Logger.Info("入庫予定インポート End");
            return importArgumentList;

        }
        #endregion

        #region ■プライベートメソッド■

        /// <summary>
        /// 取込処理を実行します
        /// </summary>
        /// <param name="importFilePath">取込ファイル名（フルパス）</param>
        /// <param name="errorFilePath">エラーファイル名（フルパス）</param>
        /// <returns>実行結果</returns>
        private ExecuteArgument ExecuteImport(string importFilePath, string errorFilePath)
        {
            //  設定ファイルが読み込めたらDB接続文字列を生成
            var dbConnectionString = AppSettings.AppSettings.SQLServer.ConnectionString;
            ExecuteArgument executeArgument = new ExecuteArgument();

            using (var db = new Database(dbConnectionString))
            {
                T_NYUKOYOTEI nyukoYotei = new T_NYUKOYOTEI();
                executeArgument = nyukoYotei.ImportByNyukoyotei(db, importFilePath, errorFilePath);
            }

            return executeArgument;
        }

        /// <summary>
        /// 更新処理を実行します
        /// </summary>
        /// <returns>実行結果</returns>
        private ExecuteArgument RegistNyukoYotei()
        {
            //  設定ファイルが読み込めたらDB接続文字列を生成
            var dbConnectionString = AppSettings.AppSettings.SQLServer.ConnectionString;
            ExecuteArgument executeArgument = new ExecuteArgument();
            int recCount = 0;

            //  ワークファイル件数取得
            try
            {
                using (var db = new Database(dbConnectionString))
                {
                    W_NYUKOYOTEI_TEMP w_Nyukoyotei_Temo = new W_NYUKOYOTEI_TEMP();
                    
                    w_Nyukoyotei_Temo.GetRecordCount(db);

                    while (db.DataReader.Read())
                    {
                        recCount = int.Parse(db.DataReader["REC_COUNT"].ToString());
                    }
                }
            }
            catch (Exception)
            {
                Log.Logger.Error("処理異常");
                Log.Logger.Error(string.Format("{0}{1}", "エラー：", executeArgument.ErrorNumber.ToString()));
                Log.Logger.Error(string.Format("{0}{1}", "エラーメッセージ：", executeArgument.ErrorMessage));
                throw;
            }

            //  入庫予定ワークが０件の場合は更新処理は行い
            if (recCount == 0)
            {
                return executeArgument;
            }

            //  更新処理実行
            try
            {
                using (var db = new Database(dbConnectionString))
                {
                    T_NYUKOYOTEI nyukoYotei = new T_NYUKOYOTEI();
                    executeArgument = nyukoYotei.UpdateByNyukoyotei(db);
                }

            }
            catch (Exception)
            {
                Log.Logger.Error("処理異常");
                Log.Logger.Error(string.Format("{0}{1}", "エラー：", executeArgument.ErrorNumber.ToString()));
                Log.Logger.Error(string.Format("{0}{1}", "エラーメッセージ：", executeArgument.ErrorMessage));
                throw;
            }
            return executeArgument;
        }
        #endregion

    }
}
