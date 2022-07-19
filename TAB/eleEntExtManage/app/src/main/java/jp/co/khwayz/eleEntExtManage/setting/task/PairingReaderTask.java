package jp.co.khwayz.eleEntExtManage.setting.task;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.densowave.scannersdk.Common.CommException;
import com.densowave.scannersdk.Common.CommScanner;
import com.densowave.scannersdk.Dto.CommScannerBtSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.co.khwayz.eleEntExtManage.application.Application;

/**
 * リーダー接続タスク
 */
public class PairingReaderTask {
    private static final String TAG = PairingReaderTask.class.getSimpleName();
    private final Callback mCallback;
    private final CommScanner mCommScanner;

    private class AsyncRunnable implements Runnable {
        final Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void run() {
            // メイン処理
            boolean result = doInBackground();
            // 後処理
            handler.post(() -> onPostExecute(result));
        }
    }

    public PairingReaderTask(@NonNull Callback callback, CommScanner commScanner) {
        mCommScanner = commScanner;
        mCallback = callback;
    }

    public void execute() {
        // 事前処理
        mCallback.onPreExecute();
        // thread開始
        ExecutorService executors = Executors.newSingleThreadExecutor();
        executors.submit(new AsyncRunnable());
    }

    // メイン処理
    private boolean doInBackground() {
        try {
            // nullチェック
            if (mCommScanner == null) { return false; }
            // スキャナー接続
            mCommScanner.claim();
            Application.commScanner = mCommScanner;
            CommScannerBtSettings btSet = Application.commScanner.getBtSettings();
            btSet.mode = CommScannerBtSettings.Mode.SLAVE;
            Application.commScanner.setBtSettings(btSet);
            return true;
        } catch(CommException e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            return false;
        }
    }

    // 事後処理
    private void onPostExecute(boolean isSuccess) {
        if (isSuccess) {
            // 正常終了
            mCallback.onTaskFinished();
        } else {
            // エラー終了
            mCallback.onError();
        }
    }

    public interface Callback {
        void onPreExecute();
        void onTaskFinished();
        void onError();
    }
}
