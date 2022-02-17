using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Elematec.EntExtManage_RFIDSystem_Importer
{
    /// <summary>
    /// 取引区分（列挙型）
    /// </summary>
    public enum TorikomiKubun
    {
        /// <summary>
        /// 入庫事前連絡情報
        /// </summary>
        Nyukoyotei = 1,

        /// <summary>
        /// 無償品情報
        /// </summary>
        Mushouhin = 2,
    }
}
