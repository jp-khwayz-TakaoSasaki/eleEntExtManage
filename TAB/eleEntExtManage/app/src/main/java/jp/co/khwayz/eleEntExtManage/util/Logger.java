package jp.co.khwayz.eleEntExtManage.util;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import jp.co.khwayz.eleEntExtManage.BuildConfig;
import jp.co.khwayz.eleEntExtManage.application.Application;
import timber.log.Timber;

public class Logger {

    public enum LogLevel {
        debug(Level.DEBUG),
        info(Level.INFO);
        final Level log4jLevel;
        LogLevel(Level logLevel) {
            this.log4jLevel = logLevel;
        }
    }

    /* 内部ストレージ Download */
    //private String DEFAULT_LOG_OUTPUT_DIR = Environment.getExternalStorageDirectory().getPath() + "/Download/log";
    private final String DEFAULT_LOG_OUTPUT_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/log";
    private final String TAG = getClass().getSimpleName();
    private org.apache.log4j.Logger mLog;

    private String mLogDirPath = DEFAULT_LOG_OUTPUT_DIR;
    // file名 "mFileName"+.logとなる
    private String mFileName = "Info_";
    // 1ファイルの最大容量 (byte) 1024 * 1024 * 10 = 10MB
    private long mMaxFileSize = 1024 * 1024 * 10;
    // 出力フォーマット
    private String mConversionPattern = "%d %-5p %m%n";
    // MAX_FILE_SIZE によって分割されたファイルの最大保存数
    private int mMaxBackupCount = 10;
    // file 末尾に付ける日付フォーマット
    private String mSdfPatternEndFile = "yyyy-MM-dd";
    // 削除する期間(日)
    private int mDeleteInterval = 30;
    /** ログレベル */
    private LogLevel logLevel = LogLevel.info;

    // MediaScannerConnection#scanFile(context, ..)用
    private Context mContextForMediaScannerConnection;

    private boolean isLogInitialized;
    public boolean getIsLogInitialized() {
        return isLogInitialized;
    }

    /* ログファイルの出力場所 */

    private Logger() {}

    public static class LoggerBuilder {
        final Logger logger;
        public LoggerBuilder(){
            this.logger = new Logger();
        }

        public LoggerBuilder setLogDirPath(String logDirPath) {
            logger.mLogDirPath = logDirPath;
            return this;
        }

        public LoggerBuilder setFileName(String fileName) {
            logger.mFileName = fileName;
            return this;
        }

        public LoggerBuilder setMaxFileSize(int maxFileSize) {
            logger.mMaxFileSize = maxFileSize;
            return this;
        }

        public LoggerBuilder setMaxBackupSize(int maxBackupCount) {
            logger.mMaxBackupCount = maxBackupCount;
            return this;
        }

        public LoggerBuilder setConversionPattern(String conversionPattern) {
            logger.mConversionPattern = conversionPattern;
            return this;
        }

        public LoggerBuilder setSdfPatternEndFile(String sdfPatternEndFile) {
            logger.mSdfPatternEndFile = sdfPatternEndFile;
            return this;
        }

        public LoggerBuilder setDeleteInterval(int deleteInterval) {
            logger.mDeleteInterval = deleteInterval;
            return this;
        }

        public LoggerBuilder setLogLevel(LogLevel logLevel) {
            logger.logLevel = logLevel;
            return this;
        }

        public synchronized Logger build(Context context) {
            logger.initLog(context);
            return logger;
        }

    }

    /* ログファイル出力の設定 */
    private void initLog(Context context) {
        if (!isLogInitialized) {
            Date d = new Date();
            SimpleDateFormat d1 = new SimpleDateFormat("yyyyMMdd", Locale.JAPANESE);
            String c1 = d1.format(d);

            this.mContextForMediaScannerConnection = context;

            this.mFileName = this.mFileName + c1;

            isLogInitialized = initialize();
            i(TAG, "Log initialized: " + logLevel);
        }
    }

    /**
     * 初期化処理.
     *
     * @return true:OK false:NG
     */
    private  boolean initialize() {
        if (mLogDirPath.equals("")) {
            return false;
        }
        if (!initLog4J()) {
            return false;
        }
        initTimber();
        notifyFileToAndroid();
        return true;
    }

    private boolean initLog4J() {
        try {
            if (mLogDirPath.equals("")) {
                return false;
            }

            final File dir = new File(mLogDirPath);
            if (dir.exists()) {
                // ファイル名がfileNameであるファイルが対象
                File[] files = dir.listFiles((file, str) -> str.contains(mFileName + ".log"));
                final SimpleDateFormat sdf = new SimpleDateFormat(mSdfPatternEndFile, Locale.JAPANESE);
                for (final File f : files) {
                    if (f.isFile()) {
                        final String fileName = f.getName();
                        final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
                        if (!extension.equals("log")) {
                            try {
                                Integer.parseInt(extension);
                            } catch (NumberFormatException e) {
                                continue;
                            }
                            // 日付以外の数字の場合はrenameする ex.(app.log.3 → app.log.3.2020-06-12)
                            final String now = sdf.format(new Date());
                            final String newName = fileName.concat('.' + now);
                            boolean ret = f.renameTo(new File(mLogDirPath + System.getProperty("file.separator") + newName));
                        }
                    }
                }
                // rename 後のため再取得
                // ファイル名がfileNameであるファイルが対象
                files = dir.listFiles((file, str) -> str.contains(mFileName + ".log"));
                // 設定期間により、log ファイルを削除する
                for (final File f : files) {
                    if (f.isFile()) {
                        final String fileName = f.getName();
                        final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
                        if (extension.equals("log")) {
                            continue;
                        }
                        final Date fDate;
                        try {
                            fDate = sdf.parse(extension);
                        } catch (ParseException e) {
                            Timber.e(e, "日時パースエラー:%s", extension);
                            continue;
                        }
                        final Calendar cl = Calendar.getInstance();
                        final Calendar clNow = Calendar.getInstance();
                        cl.setTime(fDate);
                        clNow.setTime(new Date());

                        final long diffTime = clNow.getTimeInMillis() - cl.getTimeInMillis();
                        final int diffDayMillis = 1000 * 60 * 60 * 24;
                        if ((int) (diffTime / diffDayMillis) > mDeleteInterval) {
                            if (f.delete()) {
                                Timber.i("%s is deleted.", fileName);
                            } else {
                                Timber.d("%s can not delete.", fileName);
                            }
                        }
                    }
                }
            } else {
                if (dir.mkdirs()) {
                    Timber.d("log's directory is created.");
                }
            }

            final String logfilePath = mLogDirPath + System.getProperty("file.separator") + mFileName + ".log";
            final File file = new File(logfilePath);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    Timber.d("new file is created.");
                }
            }

            // appender に渡す layout
            final PatternLayout layout = new PatternLayout();
            layout.setConversionPattern(mConversionPattern);

            // creates daily rolling file appender
            final DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
            rollingAppender.setFile(logfilePath);
            //rollingAppender.setDatePattern('.' + mSdfPatternEndFile);    // ローリングする単位...はログファイルに出力時に名前に日付を持たせているので未使用
            rollingAppender.setLayout(layout);
            rollingAppender.activateOptions();

            // 出力設定
            final LogConfigurator logConfigurator = new LogConfigurator();
            logConfigurator.setFileName(logfilePath);
            logConfigurator.setRootLevel(logLevel.log4jLevel);
            logConfigurator.setLevel("org.apache", Level.INFO);
            logConfigurator.setFilePattern(mConversionPattern);
            logConfigurator.setMaxBackupSize(mMaxBackupCount);
            logConfigurator.setMaxFileSize(mMaxFileSize);
            logConfigurator.setImmediateFlush(false);   // 2重出力防止subの出力は行わない
            logConfigurator.setUseLogCatAppender(true);    // logcat出力
            logConfigurator.configure();

            mLog = org.apache.log4j.Logger.getLogger(Application.class);
            mLog.addAppender(rollingAppender);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTreeLog4J());
        } else {
            Timber.plant(new TreeLog4J());
        }
    }

    private void notifyFileToAndroid() {
        MediaScannerConnection.scanFile(mContextForMediaScannerConnection,
                new String[]{mLogDirPath + System.getProperty("file.separator") + mFileName + ".log"},
                new String[]{"text/plain"},
                (path, uri) -> Timber.d("file " + path + " was scanned successfully: " + uri)
        );
    }

    private void writeLog(int priority, String tag, @NotNull String message, Throwable t) {
        try {
            switch (priority) {
                case Log.INFO:
                    mLog.info(tag + ":" + message);
                    mLog.info(message, t);
                    break;
                case Log.WARN:
                    mLog.warn(tag + ":" + message);
                    mLog.warn(message, t);
                    break;
                case Log.ERROR:
                    mLog.error(tag + ":" + message);
                    mLog.error(message, t);
                    break;
                case Log.DEBUG:
                    mLog.debug(tag + ":" + message);
                    mLog.debug(message, t);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DebugTreeLog4J extends Timber.DebugTree {
        @Override
        protected void log(int priority, String tag, @NotNull String message, Throwable t) {
            if (priority == Log.DEBUG) {
                mLog.debug(tag + ":" + message);
                return;
            }
            writeLog(priority, tag, message, t);
        }
    }

    private class TreeLog4J extends Timber.DebugTree {
        @Override
        protected void log(int priority, String tag, @NotNull String message, Throwable t) {
            writeLog(priority, tag, message, t);
        }
    }

    public void v(String TAG, String message) {
        if(isLogInitialized) {
            Timber.tag(TAG).v(message);
        }
        else {
            Application.log.v(TAG, message);
        }
    }

    public void v(String TAG, Throwable e) {
        if(isLogInitialized) {
            Timber.tag(TAG).v(e);
        }
        else {
            e.printStackTrace();
            Application.log.v(TAG, e.getMessage());
        }
    }

    public void d(String TAG, String message) {
        if(isLogInitialized) {
            Timber.tag(TAG).d(message);
        }
        else {
            Log.d(TAG, message);
        }
    }

    public void d(String TAG, Throwable e) {
        if(isLogInitialized) {
            Timber.tag(TAG).d(e);
        }
        else {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    public void i(String TAG, String message) {
        if(isLogInitialized) {
            Timber.tag(TAG).i(message);
        }
        else {
            Log.i(TAG, message);
        }
    }

    public void i(String TAG, Throwable e) {
        if(isLogInitialized) {
            Timber.tag(TAG).i(e);
        }
        else {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
    }

    public void w(String TAG, String message) {
        if(isLogInitialized) {
            Timber.tag(TAG).w(message);
        }
        else {
            Log.w(TAG, message);
        }
    }

    public void w(String TAG, Throwable e) {
        if(isLogInitialized) {
            Timber.tag(TAG).w(e);
        }
        else {
            e.printStackTrace();
            Log.w(TAG, e.getMessage());
        }
    }

    public void e(String TAG, String message) {
        if(isLogInitialized) {
            Timber.tag(TAG).e(message);
        }
        else {
            Log.e(TAG, message);
        }
    }

    public void e(String TAG, Throwable e) {
        if(isLogInitialized) {
            Timber.tag(TAG).e(e);
        }
        else {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

}
