using System;
using System.IO;
using System.Xml;
using System.Xml.Schema;
using Elematec.EntExtManage_RFIDSystem_Common.Common;

namespace Elematec.EntExtManage_RFIDSystem_Common.AppSettings
{
    public class AppSettings
    {
        public class General
        {
            /// <summary>
            /// ログ保管日数
            /// </summary>
            public static int LogFilePreserveDay { get; set; }

            ///// <summary>
            ///// ピック指示データ取込み画面に表示する、出荷件数一覧表示日
            ///// </summary>
            //public static int DisplayShipDataDays { get; set; }

            ///// <summary>
            ///// 成功／失敗フォルダに移動した連携ファイル保管日数
            ///// </summary>
            //public static int WMSFilePreserveDay { get; set; }

            ///// <summary>
            ///// WMSファイル連携フォルダパス
            ///// </summary>
            //public static string WMSReceiveFolderPath { get; set; }

            ///// <summary>
            ///// 取込成功フォルダパス
            ///// </summary>
            //public static string SuccessedFolderPath { get; set; }

            ///// <summary>
            ///// 取込失敗フォルダパス
            ///// </summary>
            //public static string FailedFolderPath { get; set; }

            ///// <summary>
            ///// DB（ピッキング指示データ）の取込データ保管日数
            ///// </summary>
            //public static int PickingPreserveDay { get; set; }

            ///// <summary>
            ///// DB（生産性一覧データ）の取込データ保管日数
            ///// </summary>
            //public static int ProductivityPreserveDay { get; set; }

            ///// <summary>
            ///// 実行するバッチ
            ///// </summary>
            //public static string BatchName { get; set; }

            ///// <summary>
            ///// 特別導線設定画面での検索結果の最大表示件数
            ///// </summary>
            //public static int SpecialConductorSettingMaxRow { get; set; }

            ///// <summary>
            ///// 帳票用プリンタ プリンタドライバ名
            ///// </summary>
            //public static string PrinterDriverName { get; set; }
            ///// <summary>
            ///// バックアップファイル連携フォルダパス
            ///// </summary>
            //public static string BackupFolderPath { get; set; }

            ///// <summary>
            ///// 「ピッキング指示データ取込み」の有効設定
            ///// </summary>
            //public static int PickingDataImportValid { get; set; }

            ///// <summary>
            ///// 「残設定一覧」の有効設定
            ///// </summary>
            //public static int RemainingSettingValid { get; set; }

            ///// <summary>
            ///// 「ペア組確定処理」の有効設定
            ///// </summary>
            //public static int PairConfirmedValid { get; set; }

            ///// <summary>
            ///// 「特別導線設定」の有効設定
            ///// </summary>
            //public static int SpecialConductorSettingValid { get; set; }

            ///// <summary>
            ///// 「ペア組解消」の有効設定
            ///// </summary>
            //public static int PairCancellationValid { get; set; }

            ///// <summary>
            ///// 「欠品確定処理」の有効設定
            ///// </summary>
            //public static int OutOfStockConfirmedValid { get; set; }

            ///// <summary>
            ///// 「再ペア組処理」の有効設定
            ///// </summary>
            //public static int RepairConfirmedValid { get; set; }

            ///// <summary>
            ///// 「生産性一覧」の有効設定
            ///// </summary>
            //public static int ProductivityListValid { get; set; }

            ///// <summary>
            ///// 「アイテムマスタ」の有効設定
            ///// </summary>
            //public static int ItemMasterMaintenanceValid { get; set; }

            ///// <summary>
            ///// 「エリアマスタ」の有効設定
            ///// </summary>
            //public static int AreaMasterMaintenanceValid { get; set; }

            ///// <summary>
            ///// 「作業者マスタ」の有効設定
            ///// </summary>
            //public static int WorkerMasterMaintenanceValid { get; set; }

            ///// <summary>
            ///// 「リカバリ作業」の有効設定
            ///// </summary>
            //public static int PickWorkReleaseValid { get; set; }

            ///// <summary>
            ///// 環境設定のパスワード
            ///// </summary>
            //public static string SettingPassword { get; set; }


            public static void ReadElement(XmlReader xr)
            {
                if (xr.Name == "LogFilePreserveDay") { LogFilePreserveDay = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "DisplayShipDataDays") { DisplayShipDataDays = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "WMSFilePreserveDay") { WMSFilePreserveDay = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "WMSReceiveFolderPath") { WMSReceiveFolderPath = xr.ReadString().Trim(); }
                //else if (xr.Name == "SuccessedFolderPath") { SuccessedFolderPath = xr.ReadString().Trim(); }
                //else if (xr.Name == "FailedFolderPath") { FailedFolderPath = xr.ReadString().Trim(); }
                //else if (xr.Name == "PickingPreserveDay") { PickingPreserveDay = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "ProductivityPreserveDay") { ProductivityPreserveDay = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "BatchName") { BatchName = xr.ReadString().Trim(); }
                //else if (xr.Name == "BackupFolderPath") { BackupFolderPath = xr.ReadString().Trim(); }
                //else if (xr.Name == "SpecialConductorSettingMaxRow") { SpecialConductorSettingMaxRow = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "PrinterDriverName") { PrinterDriverName = xr.ReadString().Trim(); }
                //else if (xr.Name == "PickingDataImportValid") { PickingDataImportValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "RemainingSettingValid") { RemainingSettingValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "PairConfirmedValid") { PairConfirmedValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "SpecialConductorSettingValid") { SpecialConductorSettingValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "PairCancellationValid") { PairCancellationValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "OutOfStockConfirmedValid") { OutOfStockConfirmedValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "RepairConfirmedValid") { RepairConfirmedValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "ProductivityListValid") { ProductivityListValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "ItemMasterMaintenanceValid") { ItemMasterMaintenanceValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "AreaMasterMaintenanceValid") { AreaMasterMaintenanceValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "WorkerMasterMaintenanceValid") { WorkerMasterMaintenanceValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "PickWorkReleaseValid") { PickWorkReleaseValid = Convert.ToInt32(xr.ReadString().Trim()); }
                //else if (xr.Name == "SettingPassword") { SettingPassword = Convert.ToString(xr.ReadString().Trim()); }
                else { }
            }

            public static void Save(XmlDocument xmlDoc)
            {
                const string targetNode = "configuration/General/";

                XmlNode newNode;
                XmlNodeList nodelist;

                nodelist = xmlDoc.SelectNodes(targetNode + "LogFilePreserveDay");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = LogFilePreserveDay.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                //nodelist = xmlDoc.SelectNodes(targetNode + "DisplayShipDataDays");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = DisplayShipDataDays.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "WMSFilePreserveDay");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = WMSFilePreserveDay.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "WMSReceiveFolderPath");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = WMSReceiveFolderPath.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "SuccessedFolderPath");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = SuccessedFolderPath.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "FailedFolderPath");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = FailedFolderPath.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "PickingPreserveDay");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = PickingPreserveDay.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "ProductivityPreserveDay");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = ProductivityPreserveDay.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "BatchName");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = BatchName.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "BackupFolderPath");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = BackupFolderPath.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "SpecialConductorSettingMaxRow");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = SpecialConductorSettingMaxRow.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "PrinterDriverName");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = PrinterDriverName.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "PickingDataImportValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = PickingDataImportValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "RemainingSettingValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = RemainingSettingValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "PairConfirmedValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = PairConfirmedValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "SpecialConductorSettingValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = SpecialConductorSettingValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "PairCancellationValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = PairCancellationValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "OutOfStockConfirmedValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = OutOfStockConfirmedValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "RepairConfirmedValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = RepairConfirmedValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "ProductivityListValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = ProductivityListValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "ItemMasterMaintenanceValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = ItemMasterMaintenanceValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "AreaMasterMaintenanceValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = AreaMasterMaintenanceValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "WorkerMasterMaintenanceValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = WorkerMasterMaintenanceValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "PickWorkReleaseValid");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = PickWorkReleaseValid.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}

                //nodelist = xmlDoc.SelectNodes(targetNode + "SettingPassword");
                //if (nodelist.Count > 0)
                //{
                //    newNode = nodelist.Item(0);
                //    newNode.InnerText = SettingPassword.ToString();
                //    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                //}
            }

        }

        public class SQLServer
        {
            public static string ConnectionString
            {
                get
                {
                    string connection_string_format = "Persist Security Info=false;User ID={0};Password={1};Initial Catalog={2};Server={3};";
                    string connection_string = string.Format(connection_string_format
                                                             , UserName
                                                             , Password
                                                             , DatabaseName
                                                             , ServerName);
                    return connection_string;
                }
            }

            public static string ServerName { get; set; }
            public static string DatabaseName { get; set; }
            public static string UserName { get; set; }
            public static string Password { get; set; }

            public static void ReadElement(XmlReader xr)
            {
                if (xr.Name == "ServerName") { ServerName = xr.ReadString().Trim(); }
                else if (xr.Name == "DatabaseName") { DatabaseName = xr.ReadString().Trim(); }
                else if (xr.Name == "UserName") { UserName = xr.ReadString().Trim(); }
                else if (xr.Name == "Password") { Password = xr.ReadString().Trim(); }
                else { }
            }

            public static void Save(XmlDocument xmlDoc)
            {
                const string targetNode = "configuration/SQLServer/";

                XmlNode newNode;
                XmlNodeList nodelist;

                nodelist = xmlDoc.SelectNodes(targetNode + "ServerName");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = ServerName.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "DatabaseName");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = DatabaseName.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "UserName");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = UserName.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "Password");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = Password.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }
            }
        }
        public class Import
        {
            /// <summary>
            /// 成功／失敗フォルダに移動した連携ファイル保管日数
            /// </summary>
            public static int FilePreserveDay { get; set; }

            /// <summary>
            /// 取込成功フォルダパス
            /// </summary>
            public static string SuccessedFolderPath { get; set; }

            /// <summary>
            /// 取込失敗フォルダパス
            /// </summary>
            public static string FailedFolderPath { get; set; }

            /// <summary>
            /// 連携作業フォルダパス
            /// </summary>
            public static string WorkFolderPath { get;set;}

            /// <summary>
            /// エラーファイルフォルダパス
            /// </summary>
            public static string ErrorFolderPath { get; set; }

            /// <summary>
            /// ファイル連携フォルダパス
            /// </summary>
            public static string ReceiveFolderPath { get; set; }

            /// <summary>
            /// 入庫予定ファイル名（システム用）※拡張子なし
            /// </summary>
            public static string WarehaousingCSV { get; set; }

            /// <summary>
            /// 連携ファイルチェック用ファイル拡張子
            /// </summary>
            public static string CheckFileExtension { get; set; }

            /// <summary>
            /// 連携ファイル拡張子
            /// </summary>
            public static string ImportFileExtension { get; set; }

            public static void ReadElement(XmlReader xr)
            {
                if (xr.Name == "FilePreserveDay") { FilePreserveDay = Convert.ToInt32(xr.ReadString().Trim()); }
                else if (xr.Name == "SuccessedFolderPath") { SuccessedFolderPath = xr.ReadString().Trim(); }
                else if (xr.Name == "WarehaousingCSV") { WarehaousingCSV = xr.ReadString().Trim(); }
                else if (xr.Name == "FailedFolderPath") { FailedFolderPath = xr.ReadString().Trim(); }
                else if (xr.Name == "WorkFolderPath") { WorkFolderPath = xr.ReadString().Trim(); }
                else if (xr.Name == "ErrorFolderPath") { ErrorFolderPath = xr.ReadString().Trim(); }
                else if (xr.Name == "ReceiveFolderPath") { ReceiveFolderPath = xr.ReadString().Trim(); }
                else if (xr.Name == "CheckFileExtension") { CheckFileExtension = xr.ReadString().Trim(); }
                else if (xr.Name == "ImportFileExtension") { ImportFileExtension = xr.ReadString().Trim(); }
                else { }
            }

            public static void Save(XmlDocument xmlDoc)
            {
                const string targetNode = "configuration/Import/";

                XmlNode newNode;
                XmlNodeList nodelist;

                nodelist = xmlDoc.SelectNodes(targetNode + "FilePreserveDay");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = FilePreserveDay.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "SuccessedFolderPath");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = SuccessedFolderPath.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "FailedFolderPath");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = FailedFolderPath.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "WarehaousingCSV");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = WarehaousingCSV.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "WorkFolderPath");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = WorkFolderPath.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "ErrorFolderPath");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = ErrorFolderPath.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "ReceiveFolderPath");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = ErrorFolderPath.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "CheckFileExtension");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = ErrorFolderPath.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }

                nodelist = xmlDoc.SelectNodes(targetNode + "ImportFileExtension");
                if (nodelist.Count > 0)
                {
                    newNode = nodelist.Item(0);
                    newNode.InnerText = ErrorFolderPath.ToString();
                    nodelist.Item(0).ParentNode.ReplaceChild(newNode, nodelist.Item(0));
                }
            }
        }

        private enum MyXmlNode : int
        {
            None = 0,
            General = 1,
            SQLServer = 2,
            Import = 3,
        }

        #region [ ReadXmlFile ]
        public static bool ReadXmlFile()
        {
            /* 初期化 */
            return ReadXmlFile(Util.GetCurrentDirectory() + "\\AppSettings\\" + "AppSettings.xml");
        }

        public static bool ReadXmlFile(string filepath)
        {
            try
            {
                MyXmlNode node = MyXmlNode.None;
                if (!File.Exists(filepath))
                {
                    // 設定ファイルが存在しない場合は、何もせずに終了
                    return false;
                }
                XmlSchemaSet sc = new XmlSchemaSet();
                sc.Add(null, Util.GetCurrentDirectory() + "\\AppSettings\\AppSettings.xsd");

                var st = new XmlReaderSettings();
                st.IgnoreWhitespace = true; // 空白を無視する
                st.IgnoreComments = true;   // コメントを無視する
                st.ValidationType = ValidationType.Schema;
                st.Schemas = sc;
                using (XmlReader xr = XmlReader.Create(filepath, st))
                {
                    while (xr.Read())
                    {
                        if (xr.NodeType == XmlNodeType.Whitespace)
                        {
                            continue;
                        }
                        if (xr.NodeType == XmlNodeType.Element)
                        {
                            if (xr.Name == "General")
                            {
                                node = MyXmlNode.General;
                            }
                            else if (xr.Name == "SQLServer")
                            {
                                node = MyXmlNode.SQLServer;
                            }
                            else if (xr.Name == "Import")
                            {
                                node = MyXmlNode.Import;
                            }
                            else
                            {
                            }

                            switch (node)
                            {
                                case MyXmlNode.General:
                                    General.ReadElement(xr);
                                    break;
                                case MyXmlNode.SQLServer:
                                    SQLServer.ReadElement(xr);
                                    break;
                                case MyXmlNode.Import:
                                    Import.ReadElement(xr);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    xr.Close();
                }
                return true;
            }
            catch (XmlSchemaValidationException validex)
            {
#if false
                /* update t.tutagawa 2010.02.17
                 * ReadXmlFile内の再帰処理が無限ループに陥る可能性を排除するフラグを準備
                 * （xsdファイルが編集されていたら無限ループの可能性がある）
                 */
                if (this.m_ExecReflexive != true)
                {
                    if (validex != null) { validexp = null; }
                    this.m_ExecReflexive = true;        /* 2回目以降は、再帰処理を行わない */

                    if (this.CreateXmlFile(filepath) != true)
                    {
                        return false;
                    }
                    return this.ReadXmlFile(filepath);  //再帰
                }
                else
                {
                    msg = "XMLファイルのフォーマットが不正です。";
                    msg += "\r\n";
                    msg += filepath;
                    msg += "\r\n";
                    msg += "\r\n";
                    msg += "[エラー詳細]";
                    msg += "\r\n";
                    msg += validex.ToString();
                    System.Windows.Forms.MessageBox.Show(msg,
                                                         "Error",
                                                         System.Windows.Forms.MessageBoxButtons.OK,
                                                         System.Windows.Forms.MessageBoxIcon.Error);
                }
#else
                throw validex;
#endif
            }
            catch (Exception ex)
            {
#if false
                msg = "XMLファイルの読み込み中にエラーが発生しました。";
                msg += "\r\n";
                msg += "\r\n";
                msg += "[エラー詳細]";
                msg += "\r\n";
                msg += exp.ToString();
                System.Windows.Forms.MessageBox.Show(msg,
                                                     "Error",
                                                     System.Windows.Forms.MessageBoxButtons.OK,
                                                     System.Windows.Forms.MessageBoxIcon.Error);
#else
                throw ex;
#endif
            }
        }
        #endregion

        #region [ SaveXmlFile ]
        public static void SaveXmlFile()
        {
            //            SaveXmlFile(Util.GetCurrentDirectory() + "\\xml\\" + "AppSettings.xml");
            SaveXmlFile(Util.GetCurrentDirectory() + "\\AppSettings\\" + "AppSettings.xml");
        }
        public static void SaveXmlFile(string filepath)
        {
            if (!File.Exists(filepath))
            {
                // 設定ファイルが存在しない場合は、何もしない
                return;
            }
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(filepath);
            General.Save(xmlDoc);
            SQLServer.Save(xmlDoc);
            Import.Save(xmlDoc);
            xmlDoc.Save(filepath);
        }
        #endregion

    }
}
