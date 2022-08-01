package jp.co.khwayz.eleEntExtManage.application;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.densowave.scannersdk.Common.CommException;
import com.densowave.scannersdk.Common.CommScanner;
import com.densowave.scannersdk.Dto.CommScannerParams;
import com.densowave.scannersdk.Dto.RFIDScannerSettings;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.BuildConfig;
import jp.co.khwayz.eleEntExtManage.MainActivity;
import jp.co.khwayz.eleEntExtManage.common.BaseFragmentListener;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.InvoiceSearchInfo;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;
import jp.co.khwayz.eleEntExtManage.common.models.UserInfo;
import jp.co.khwayz.eleEntExtManage.database.RfidDataBaseOpenHelper;
import jp.co.khwayz.eleEntExtManage.util.Logger;

public class Application extends android.app.Application {
    private final String TAG = this.getClass().getSimpleName();
    /** インスタンス */
    private static Application instance = null;
    /** メインアクティビティ */
    public static MainActivity mainActivity;
    /** 表示中フラグメント */
    public static BaseFragmentListener currentFragment;
    /** ログインユーザー情報 */
    public static UserInfo userInfo;
    /** 企業コード */
    public static String corporateCode;

    // SQLiteOpenHelper
    public static RfidDataBaseOpenHelper dbHelper;

    /** 端末ID */
    public static String terminalID;
    /** 所属拠点 */
    public static String affiliationBase;
    /** APIのURL */
    public static String apiUrl;

    /** スキャナー : セッション */
    public static RFIDScannerSettings.Scan.SessionFlag scannerSessionFlag = RFIDScannerSettings.Scan.SessionFlag.S0;
    /** スキャナー : パワーレベル */
    public static int scannerPowerLevel = 30;
    /** スキャナー : チャンネル */
    public static long scannerChannel = Constants.ScannerChanel.Default.getScannerChannel();
    /** スキャナー : Beep音 */
    public static CommScannerParams.Notification.Sound.Buzzer scannerBuzzerEnable = CommScannerParams.Notification.Sound.Buzzer.ENABLE;

    /** SP1 */
    public static CommScanner commScanner = null;

    /** 出庫Invoice検索情報
     * 　　Invoice検索条件を保持する
     */
    public static InvoiceSearchInfo invoiceSearchInfo = null;

    /** 簿外リスト */
    public static List<TagInfo> scanOffBooklList;

    /* 初期化フラグ */
    public static boolean initFlag;

    /** Log出力ユーティリティ */
    @SuppressLint("StaticFieldLeak")
    public static Logger log;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // 現在設定されているUncaughtExceptionHandlerを退避する
        final Thread.UncaughtExceptionHandler saveUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        // キャッチされなかった例外発生時の処理をセットする
        Thread.setDefaultUncaughtExceptionHandler((new Thread.UncaughtExceptionHandler() {
            private volatile boolean mCrashing = false;
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                try {
                    if (!mCrashing) {
                        mCrashing = true;
                        // クラッシュログ出力
                        log.e(TAG, throwable);
                    }
                } finally {
                    saveUncaughtExceptionHandler.uncaughtException(thread, throwable);
                }
            }
        }));

        /* 端末設定取得 */
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(instance);
        terminalID = pref.getString(Constants.PREFS_TERMINAL_ID, "");
        affiliationBase = pref.getString(Constants.PREFS_AFFILIATION_BASE, "");
        // TODO テスト用
        apiUrl = pref.getString(Constants.PREFS_API_URL, Constants.PREFS_DEFAULT_API_URL);
//        apiUrl = Constants.PREFS_DEFAULT_API_URL;

        /* Scanner設定取得 */
        int sessionFlag = pref.getInt(Constants.PREFS_SESSION_FLAG, Constants.PREFS_DEFAULT_SESSION_FLAG);
        scannerSessionFlag = RFIDScannerSettings.Scan.SessionFlag.values()[sessionFlag];
        scannerPowerLevel = pref.getInt(Constants.PREFS_POWER_LEVEL, Constants.PREFS_DEFAULT_POWER_LEVEL);
        scannerChannel = pref.getLong(Constants.PREFS_CHANNEL, Constants.PREFS_DEFAULT_PREFS_CHANNEL.getScannerChannel());
        if (pref.getBoolean(Constants.PREFS_BUZZER_ENABLE, true)) {
            Application.scannerBuzzerEnable = CommScannerParams.Notification.Sound.Buzzer.ENABLE;
        } else {
            Application.scannerBuzzerEnable = CommScannerParams.Notification.Sound.Buzzer.DISABLE;
        }
    }

    /**
     * Applicationインスタンス取得
     * @return Applicationインスタンス
     */
    public static Application getInstance() {
        return instance;
    }

    /**
     * SP1との接続解除
     */
    public static void disConnectReader() {
        if (commScanner == null) { return; }
        try {
            commScanner.close();
        } catch (CommException e) {
            e.printStackTrace();
        }
        commScanner = null;
    }

    /**
     * Logger初期化
     */
    public synchronized static void initLog() {
        if(log == null || !log.getIsLogInitialized()) {
//            Application.log = new Logger.LoggerBuilder()
//                    .setLogLevel(BuildConfig.BUILD_TYPE.equals("debug") ? Logger.LogLevel.debug : Logger.LogLevel.info)
//                    .build(Application.getInstance());
            Application.log = new Logger.LoggerBuilder()
                    .setLogLevel(BuildConfig.DEBUG ? Logger.LogLevel.debug : Logger.LogLevel.info)
                    .build(Application.getInstance());
        }
    }
}
