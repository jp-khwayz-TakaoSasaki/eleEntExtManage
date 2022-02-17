using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Elematec.EntExtManage_RFIDSystem_Common.Database;

namespace Elematec.EntExtManage_RFIDSystem_Importer.Data
{
    /// <summary>
    /// 無償品ワークTemp関連
    /// </summary>
    public class W_MUSHOUHIN_TEMP
    {
        /// <summary>
        /// 無償品ワークの件数を取得します
        /// </summary>
        /// <param name="db">Databaseオブジェクト</param>
        public void GetRecordCount(Database db)
        {
            var now = DateTime.Today.AddDays(1);

            try
            {
                var sql = "SELECT COUNT(1) AS REC_COUNT FROM W_MUSHOUHIN_TEMP ";

                db.SetSql(sql);

                db.ClearParameter();

                db.ExecuteQuery();
            }
            catch (Exception)
            {
                throw;
            }
        }
    }
}
