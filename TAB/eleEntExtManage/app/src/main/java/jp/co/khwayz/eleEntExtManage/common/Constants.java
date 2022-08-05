package jp.co.khwayz.eleEntExtManage.common;

import okhttp3.MediaType;

public class Constants {

    // region [ Constants String ]
    /**
     * サーバー通信
     */
    public static final int HTTP_OTHER_ERROR = -1;
    public static final int HTTP_RESPONSE_STATUS_CODE_OK = 200;
    public static final int HTTP_RESPONSE_STATUS_CODE_BAD_REQUEST = 400;
    public static final int HTTP_RESPONSE_STATUS_CODE_NOT_FOUND = 404;
    public static final int HTTP_RESPONSE_STATUS_CODE_REQUEST_TIMEOUT = 408;
    public static final int HTTP_RESPONSE_STATUS_CODE_INTERNAL_SERVER_ERROR = 500;
    public static final String HTTP_PROTOCOL_PREFIX = "https://";
    public static final String HTTP_SERVICE_NAME = "/api";
    public static final String API_ADDRESS_SYUKKOINVOICE_SEARCH = "/syukko/syukkoinvoice/search/";
    public static final String API_ADDRESS_SYUKKOINVOICEDETAIL_GET = "/syukko/syukkoinvoicedetail/get/";
    public static final String API_ADDRESS_PICKING_REGIST = "/syukko/pickingdata/entry/";
    public static final String API_ADDRESS_PICKEDINVOICE_SEARCH = "/syukko/pickinginvoice/search/";
    public static final String API_ADDRESS_CASEMARN_PRINT_INVOICE_SEARCH = "/syukko/csmarkjyoho/get/";
    public static final String API_ADDRESS_OUTER_INFO_GET = "/syukko/outerjoho/get/";
    public static final String API_ADDRESS_INNER_INFO_GET = "/syukko/innerjoho/get/";
    public static final String API_ADDRESS_OVER_PACK_KONPOSHIZAI_GET = "/syukko/overpackkonposhizai/get/";
    public static final String API_ADDRESS_NIFUDA_INFO_GET = "/syukko/nifudajouhou/get/";
    public static final String API_ADDRESS_PACKING_INFO_REGIST = "/syukko/konpojoho/entry/";
    public static final String API_ADDRESS_CASEMARK_PRINTED_REGIST = "/syukko/csmarkjyoho/put/";
    public static final String API_ADDRESS_CASEMARK_DETAIL_GET = "/syukko/csdetail/get/";
    public static final String API_ADDRESS_CASEMARK_NIFUDA_INFO_GET = "/syukko/csmarknifuda/get/";
    public static final String API_ADDRESS_CASEMARK_PASTE_UPD = "/syukko/csmarkjyoho/cspasteupd";

    /** HTTPタイムアウト(秒) */
    public static final int TIMEOUT_SEC = 10;
    /** Content-type */
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=UTF-8");
    /** WebAPIのレスポンス */
    public static final String API_RESPONSE_STATUS_CODE_OK = "0";
    public static final String API_RESPONSE_STATUS_OPE_ERR = "1";
    public static final String API_RESPONSE_STATUS_SYS_ERR = "9";
    public static final String API_RESPONSE_ERROR_NOT_AVAILABLE = "0001";
    /** リセットパスワード */
    public static final  String RESET_PASSWORD = "elema123";

    /**
     * フラグ
     */
    public static final String FLAG_FALSE = "0";
    public static final String FLAG_TRUE = "1";

    /**
     * 設定
     */
    public static final String PREFS_TERMINAL_ID = "PREFS_TERMINAL_ID";
    public static final String PREFS_AFFILIATION_BASE = "PREFS_AFFILIATION_BASE";
    public static final String PREFS_API_URL = "PREFS_API_URL";
    public static final String PREFS_SESSION_FLAG = "PREFS_SESSION_FLAG";
    public static final String PREFS_POWER_LEVEL = "PREFS_POWER_LEVEL";
    public static final String PREFS_CHANNEL = "PREFS_CHANNEL";
    public static final String PREFS_BUZZER_ENABLE = "PREFS_BUZZER_ENABLE";

    // テスト用初期URL定義
    public static final String PREFS_DEFAULT_API_URL = "http://172.16.20.109:18080";
    public static final int PREFS_DEFAULT_SESSION_FLAG = 0;
    public static final int PREFS_DEFAULT_POWER_LEVEL = 30;
    public static final ScannerChanel PREFS_DEFAULT_PREFS_CHANNEL = ScannerChanel.Default;

    /* メッセージパラメータ */
    public static final String MSG_OPTION_READER = "リーダー";
    public static final String MSG_OPTION_ORDER_NO = "発注番号";
    public static final String MSG_OPTION_BRANCH_NO = "枝番";

    /* RFID/QR切替メッセージ */
    public static final String TOAST_CHANGE_RFID = "RFIDモードに切替えました";
    public static final String TOAST_CHANGE_QR = "QRモードに切替えました";

    /* ケースマーク番号区切り文字 */
    public static final String CASEMARK_SPLIT = "-";

    /* 印刷状態 */
    public static final String CASEMARK_NOT_PRINTED = "未";
    public static final String CASEMARK_PRINTED = "済";

    /* Database */
    public static final String LOCAL_DATABASE = "IssueRFID.db";
    /* 区分マスタ関連 */
    public static final String KBN_EIDO = "EIDO";
    public static final String KBN_EEPCSHIKIBETUSHI = "EEPCSHIKIBETUSHI";
    public static final String KBN_ESHIMUKECHI = "ESHIMUKECHI";
    public static final String KBN_EYUSOMEANS = "EYUSOMEANS";
    public static final String KBN_EKONPOSHIZAI = "EKONPOSHIZAI";
    public static final String KBN_EKONPOMEISAI = "EKONPOMEISAI";
    public static final String KBN_EKIKENTANI = "EKIKENTANI";
    public static final String KBN_EKIKENNAISO = "EKIKENNAISO";
    public static final String KBN_EKIKENGAISO = "EKIKENGAISO";
    public static final String KBN_ESAISHUKONPO = "ESAISHUKONPO";
    public static final String KBN_EMANAGEPASSWD = "EMANAGEPASSWD";

    // 企業コードの既定値
    public static final String DEFAULT_COM_CD = "985406002";
    // ケースマーク用管理者パスワード
    public static final String DEFAULT_CM_MANEGEPASSWORD = "12345";

    // QRコードサイズ
    public static final int QR_IMAGE_SIZE = 75;

    // Canvas指定用
    // 幅:595point、センター：297.5point
    // 高さ：842point、センター：421
    public static final int CANVAS_TEXT_SIZE = 15;
    // Invoice番号
    public static final float CANVAS_INVOICE_START = 575.0f;
    public static final float CANVAS_INVOICE_TOP = 30.0f;
    // ケースマーク行テキスト
    public static final float CANVAS_LEFT_START = 30.0f;
    public static final float CANVAS_RIGHT_START = 327.0f;
    public static final float CANVAS_UP_TOP = 70.0f;
    public static final float CANVAS_UNDER_TOP = 491.0f;
    public static final float CANVAS_TEXT_PADDING = 25.0f;
    // QRコード　75point角　右上から40point、右端から20point離し
    public static final float CANVAS_QR_LEFT_START = 207.5f;
    public static final float CANVAS_QR_RIGHT_START = 500.0f;
    public static final float CANVAS_QR_UP_TOP = 40.0f;
    public static final float CANVAS_QR_UNDER_TOP = 461.0f;

    // ケースマーク行文字列検査用
    public static final String CHK_CASEMARK_STR = "C/NO";
    public static final String SP_STR = "  ";

    // endregion

    // region [ Enum ]
    /**
     * スキャンモード(QR/RFID)
     */
    public enum ScanMode {
        RFID(0),
        QR(1)
        ;
        private final int scanMode;
        ScanMode(int scanMode) { this.scanMode = scanMode; }
        public int getScanMode() {
            return this.scanMode;
        }
    }

    public enum IssueMode {
        Normal(0),  // 通常
        Reissue(1), // 再発行
        Split(2)    // 梱包分割
        ;
        private final int issueMode;
        IssueMode(int issueMode) { this.issueMode = issueMode; }
        public int getIssueMode() { return issueMode; }
    }

    /**
     * 倉庫内ロケ変更・返品・転送区分
     */
    public enum CargoMovingType {
        Store("00"),
        LocationChange("01"),
        ReturnItem("02"),
        Forward("03"),
        Acceptance("04")
        ;
        private final String cargoMovingType;
        CargoMovingType(String cargoMovingType) { this.cargoMovingType = cargoMovingType; }
        public String getCargoMovingType() {
            return cargoMovingType;
        }
    }
    // endregion

    /**
     * SP1のチャンネル
     */
    public enum ScannerChanel {
        Default(0x0007FFFF),
        Ch5(0x00000001),
        Ch11(0x00000002),
        Ch17(0x00000004),
        Ch23(0x00000008),
        Ch24(0x00000010),
        Ch25(0x00000020)
        ;
        private final long scannerChannel;
        ScannerChanel(long scannerChannel) { this.scannerChannel = scannerChannel; }
        public long getScannerChannel() {
            return scannerChannel;
        }
    }

    /**
     * 保冷選択肢
     */
    public enum CoolChoices {
        None(""),
        Cool("保冷"),
        Normal("常温")
        ;
        private final String coolChoices;

        CoolChoices(String coolChoices) { this.coolChoices = coolChoices; }

        public String getCoolChoices() {
            return this.coolChoices;
        }
    }

    /**
     * 危険品選択肢
     */
    public enum DengerousChoices {
        None(""),
        Yes("あり"),
        No("なし")
        ;
        private final String dengerousChoices;
        DengerousChoices(String dengerousChoices) { this.dengerousChoices = dengerousChoices; }
        public String getDengerousChoices() {
            return dengerousChoices;
        }
    }

    /**
     * ケースマーク印刷状態選択肢
     */
    public enum CaseMarkPintStatusChoices {
        NotPrinted("印刷未"),
        Printed("印刷済")
        ;
        private final String caseMarkPrintStatusChoices;

        CaseMarkPintStatusChoices(String caseMarkPrintStatusChoices) { this.caseMarkPrintStatusChoices = caseMarkPrintStatusChoices; }

        public String getCaseMarkPrintStatusChoices() {
            return this.caseMarkPrintStatusChoices;
        }
    }

    /**
     * ソート画面選択肢
     */
    public enum SortChoices {
        INVOICE_NO("Invoice番号"),
        SHIMUKECHI("仕向地"),
        LIST_HENSIN_KIBOBI("リスト返信希望日"),
        SYUKKA_DATE("出荷日"),
        GYOSU("行数"),
        HOREI("保冷"),
        KIKENHIN("危険品"),
        BIKOU("備考"),
        KONPO_HOLD_FLG("保留")
        ;
        private final String sortChoices;
        SortChoices(String sortChoices) { this.sortChoices = sortChoices; }
        public String getSortChoices() {
            return sortChoices;
        }

        public static SortChoices getSortKey(String sortChoicesName) {
            for (SortChoices sortChoices : SortChoices.values()) {
                if (sortChoices.getSortChoices().equals(sortChoicesName)) {
                    return sortChoices;
                }
            }
            return null;
        }
    }
}
