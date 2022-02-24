using Elematec.EntExtManage_RFIDSystem_FileWatcher.Common;
using System;
using System.ServiceProcess;
using System.Xml.Schema;

namespace Elematec.EntExtManage_RFIDSystem_FileWatcher
{
    static class Program
    {
        /// <summary>
        /// アプリケーションのメイン エントリ ポイントです。
        /// </summary>
        static void Main()
        {
            ServiceBase[] ServicesToRun;
            try
            {
                if (!AppSettings.ReadXmlFile()) throw new Exception("AppSettings.ReadXmlFile() return false");
            }
            catch (XmlSchemaValidationException ex)
            {
                /* 設定ファイルの内容が定義情報と異なるエラー
                 */
                Log.Logger.Error(ex.Message);
                Log.Logger.Error(ex.ToString());
                return;
            }
            catch (Exception ex)
            {
                /* 設定ファイルの読み取りエラー
                 */
                Log.Logger.Error(ex.Message);
                Log.Logger.Error(ex.ToString());
                return;
            }
            ServicesToRun = new ServiceBase[]
            {
                new FileWatcher()
            };
            ServiceBase.Run(ServicesToRun);
        }
    }
}
