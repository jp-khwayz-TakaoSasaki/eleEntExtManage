using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Elematec.EntExtManage_RFIDSystem_Importer
{
    /// <summary>
    /// ユーティリティ
    /// </summary>
    public static class Utility
    {
        /// <summary>
        /// 取込区分取得（日本語）
        /// </summary>
        /// <param name="torikomikubun"></param>
        /// <returns></returns>
        public static string GetTorikomiKubunName(TorikomiKubun torikomikubun)
        {
            switch (torikomikubun)
            {
                case TorikomiKubun.Nyukoyotei:
                    return "入庫事前連絡情報";
                case TorikomiKubun.Mushouhin:
                    return "無償品情報";
                default:
                    return "";
            }
        }
    }
}
