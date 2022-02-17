using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Elematec.EntExtManage_RFIDSystem_Importer
{
    /// <summary>
    /// 取込用引数クラス
    /// </summary>
    public class ImportArgument
    {
        public ImportArgument()
        {
            TargetFileFile = "";
            ImportFilePath = "";
            ErrorFilePath = "";
            RtnCode = 0;
            ErrorNumber = 0;
            ErrorMessage = "";
        }
        
        /// <summary>
        /// 取込区分
        /// </summary>
        public TorikomiKubun TorikomiKubn { get; set; }

        /// <summary>
        /// 取込ファイル名（元ファイル名）
        /// </summary>
        public string TargetFileFile { get; set; }

        /// <summary>
        /// 取込ファイル名（日時を付与した移動先のパスを含むファイル名）
        /// </summary>
        public string ImportFilePath { get; set; }

        /// <summary>
        /// エラーファイル名
        /// </summary>
        public string ErrorFilePath { get; set; }

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
