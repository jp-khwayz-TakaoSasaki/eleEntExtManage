using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Elematec.EntExtManage_RFIDSystem_Importer.Data
{
    /// <summary>
    /// 実行用引数クラス
    /// </summary>
    public class ExecuteArgument
    {
        /// <summary>
        /// コンストラクタ
        /// </summary>
        public ExecuteArgument()
        {
            RtnCode = 0;
            ErrorNumber = 0;
            ErrorMessage = "";
        }

        /// <summary>
        /// リターンコード
        /// 0:未処理
        /// 1:正常終了
        /// 9：異常終了
        /// </summary>
        public int RtnCode { get; set; }

        /// <summary>
        /// エラー番号　※ERROR_NUMBERを保持する
        /// </summary>
        public int ErrorNumber { get; set; }

        /// <summary>
        /// エラーメッセージ　※ERROR_MESSAGEを保持する
        /// </summary>
        public string ErrorMessage { get; set; }
    }
}
