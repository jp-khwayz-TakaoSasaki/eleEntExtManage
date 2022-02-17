using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using Elematec.EntExtManage_RFIDSystem_Common.Common;
using Elematec.EntExtManage_RFIDSystem_Common.Database;

namespace Elematec.EntExtManage_RFIDSystem_Importer.Data
{
    /// <summary>
    /// 入庫予定関連サービス
    /// </summary>
    public class T_NYUKOYOTEI
    {
        Log _log = new Log();
        
        public T_NYUKOYOTEI()
        {
            _log = new Log();
        }

        /// <summary>
        /// 入庫予定データを取込ます
        /// </summary>
        /// <param name="db">Databaseオブジェクト</param>
        /// <param name="importFilePath">取込ファイルパス</param>
        /// <param name="errorFilePath">エラーファイルパス</param>
        /// <returns></returns>
        public ExecuteArgument ImportByNyukoyotei(Database db, string importFilePath, string errorFilePath)
        {
            ExecuteArgument executeArgument = new ExecuteArgument();

            try
            {
                db.SetSql("ELE10PROCESS");

                db.SetSqlCommandType(CommandType.StoredProcedure);
                db.SetCommandtimeout(0);    //  キステム規定値：180

                db.AddParameter("@ImportFilePath", importFilePath);
                db.AddParameter("@ErrorFilePath", errorFilePath);

                // OUTPUT                
                db.AddOutParameter("@ErrorNumber", System.Data.SqlDbType.Int);
                db.AddDirection("@ErrorNumber", System.Data.ParameterDirection.Output);

                db.AddOutParameter("@ErrorMessage", System.Data.SqlDbType.NVarChar, 4000);
                db.AddDirection("@ErrorMessage", System.Data.ParameterDirection.Output);

                // RETURN
                db.AddParameter("ReturnValue", System.Data.SqlDbType.Int);
                db.AddDirection("ReturnValue", System.Data.ParameterDirection.ReturnValue);

                db.ExecuteNonQuery();

                executeArgument.RtnCode = (int)db.GetSqlCommnd().Parameters["ReturnValue"].Value;
                executeArgument.ErrorNumber = (int)db.GetSqlCommnd().Parameters["@ErrorNumber"].Value;
                executeArgument.ErrorMessage = db.GetSqlCommnd().Parameters["@ErrorMessage"].Value.ToString();
            }
            catch (Exception ex)
            {
                _log.Logger.Error(ex.Message);
                _log.Logger.Error(ex.ToString());
                throw;
            }

            return executeArgument;
        }

        /// <summary>
        /// 入庫予定を更新します
        /// </summary>
        /// <param name="db">Databaseオブジェクト</param>
        /// <returns></returns>
        public ExecuteArgument UpdateByNyukoyotei(Database db)
        {
            ExecuteArgument executeArgument = new ExecuteArgument();

            try
            {
                db.SetSql("ELE11PROCESS");

                db.SetSqlCommandType(CommandType.StoredProcedure);
                db.SetCommandtimeout(0);    //  キステム規定値：180

                // OUTPUT                
                db.AddOutParameter("@ErrorNumber", System.Data.SqlDbType.Int);
                db.AddDirection("@ErrorNumber", System.Data.ParameterDirection.Output);

                db.AddOutParameter("@ErrorMessage", System.Data.SqlDbType.NVarChar, 4000);
                db.AddDirection("@ErrorMessage", System.Data.ParameterDirection.Output);

                // RETURN
                db.AddParameter("ReturnValue", System.Data.SqlDbType.Int);
                db.AddDirection("ReturnValue", System.Data.ParameterDirection.ReturnValue);

                db.ExecuteNonQuery();

                executeArgument.RtnCode = (int)db.GetSqlCommnd().Parameters["ReturnValue"].Value;
                executeArgument.ErrorNumber = (int)db.GetSqlCommnd().Parameters["@ErrorNumber"].Value;
                executeArgument.ErrorMessage = db.GetSqlCommnd().Parameters["@ErrorMessage"].Value.ToString();
            }
            catch (Exception ex)
            {
                _log.Logger.Error(ex.Message);
                _log.Logger.Error(ex.ToString());
                throw;
            }

            return executeArgument;

        }

        /// <summary>
        /// 無償品データを取込ます
        /// </summary>
        /// <param name="db">Databaseオブジェクト</param>
        /// <param name="importFilePath">取込ファイルパス</param>
        /// <param name="errorFilePath">エラーファイルパス</param>
        /// <returns></returns>
        public ExecuteArgument ImportByMushouhin(Database db, string importFilePath, string errorFilePath)
        {
            ExecuteArgument executeArgument = new ExecuteArgument();

            try
            {
                db.SetSql("ELE20PROCESS");

                db.SetSqlCommandType(CommandType.StoredProcedure);
                db.SetCommandtimeout(0);    //  キステム規定値：180

                db.AddParameter("@ImportFilePath", importFilePath);
                db.AddParameter("@ErrorFilePath", errorFilePath);

                // OUTPUT                
                db.AddOutParameter("@ErrorNumber", System.Data.SqlDbType.Int);
                db.AddDirection("@ErrorNumber", System.Data.ParameterDirection.Output);

                db.AddOutParameter("@ErrorMessage", System.Data.SqlDbType.NVarChar, 4000);
                db.AddDirection("@ErrorMessage", System.Data.ParameterDirection.Output);

                // RETURN
                db.AddParameter("ReturnValue", System.Data.SqlDbType.Int);
                db.AddDirection("ReturnValue", System.Data.ParameterDirection.ReturnValue);

                db.ExecuteNonQuery();

                executeArgument.RtnCode = (int)db.GetSqlCommnd().Parameters["ReturnValue"].Value;
                executeArgument.ErrorNumber = (int)db.GetSqlCommnd().Parameters["@ErrorNumber"].Value;
                executeArgument.ErrorMessage = db.GetSqlCommnd().Parameters["@ErrorMessage"].Value.ToString();
            }
            catch (Exception ex)
            {
                _log.Logger.Error(ex.Message);
                _log.Logger.Error(ex.ToString());
                throw;
            }

            return executeArgument;
        }

        /// <summary>
        /// 入庫予定を更新します（無償品）
        /// </summary>
        /// <param name="db">Databaseオブジェクト</param>
        /// <returns></returns>
        public ExecuteArgument UpdateByMushouhin(Database db)
        {
            ExecuteArgument executeArgument = new ExecuteArgument();

            try
            {
                db.SetSql("ELE21PROCESS");

                db.SetSqlCommandType(CommandType.StoredProcedure);
                db.SetCommandtimeout(0);    //  キステム規定値：180

                // OUTPUT                
                db.AddOutParameter("@ErrorNumber", System.Data.SqlDbType.Int);
                db.AddDirection("@ErrorNumber", System.Data.ParameterDirection.Output);

                db.AddOutParameter("@ErrorMessage", System.Data.SqlDbType.NVarChar, 4000);
                db.AddDirection("@ErrorMessage", System.Data.ParameterDirection.Output);

                // RETURN
                db.AddParameter("ReturnValue", System.Data.SqlDbType.Int);
                db.AddDirection("ReturnValue", System.Data.ParameterDirection.ReturnValue);

                db.ExecuteNonQuery();

                executeArgument.RtnCode = (int)db.GetSqlCommnd().Parameters["ReturnValue"].Value;
                executeArgument.ErrorNumber = (int)db.GetSqlCommnd().Parameters["@ErrorNumber"].Value;
                executeArgument.ErrorMessage = db.GetSqlCommnd().Parameters["@ErrorMessage"].Value.ToString();
            }
            catch (Exception ex)
            {
                _log.Logger.Error(ex.Message);
                _log.Logger.Error(ex.ToString());
                throw;
            }

            return executeArgument;

        }
    }
}
