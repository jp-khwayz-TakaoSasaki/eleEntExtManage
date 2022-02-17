using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Schema;
using Elematec.EntExtManage_RFIDSystem_Common.Common;
using Elematec.EntExtManage_RFIDSystem_Importer.AppSettings;

namespace Elematec.EntExtManage_RFIDSystem
{
    static class Program
    {
        /// <summary>
        /// アプリケーションのメイン エントリ ポイントです。
        /// </summary>
        static void Main()
        {
            ServiceBase[] ServicesToRun;
            Log log = new Log() ;
            try
            {
                //Log.Logger.Info("ReadXmlFile start");
                if (!AppSettings.ReadXmlFile()) throw new Exception("AppSettings.ReadXmlFile() return false");
            }
            catch (XmlSchemaValidationException ex)
            {
                /* 設定ファイルの内容が定義情報と異なるエラー
                 */
                log.Logger.Error(ex.Message);
                log.Logger.Error(ex.ToString());
                return;
            }
            catch (Exception ex)
            {
                /* 設定ファイルの読み取りエラー
                 */
                log.Logger.Error(ex.Message);
                log.Logger.Error(ex.ToString());
                return;
            }
            finally
            {
                //Log.Logger.Info("ReadXmlFile end");
            }

            ServicesToRun = new ServiceBase[]
            {
                new Import()
            };

            ServiceBase.Run(ServicesToRun);
        }
    }
}
