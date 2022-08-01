package jp.co.khwayz.eleEntExtManage.packing.task;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.models.InnerInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoShijiKeyInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoInnerDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import timber.log.Timber;

public class SinglePackingRegistTask {
    // region [ private variable ]
    public static final String TAG = SinglePackingRegistTask.class.getSimpleName();
    private final SinglePackingRegistTask.Callback mCallback;
    private final String mInvoiceNo;
    private final ArrayList<SyukkoShijiKeyInfo> mUpdateKeyInfo;
    private final InnerInfo mInnerInfoItem;
    // endregion

    public interface Callback {
        void onPreExecute();
        void onTaskFinished(boolean result);
        void onError();
    }

    private class AsyncRunnable implements Runnable {
        final Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void run() {
            int result = doInBackground();
            // 後処理
            handler.post(() -> onPostExecute(result));
        }
    }

    // region [ constructor ]
    public SinglePackingRegistTask(@NonNull SinglePackingRegistTask.Callback callback,
                                   String invoiceNo, ArrayList<SyukkoShijiKeyInfo> keyInfo, InnerInfo innerInfoItem) {
        mCallback = callback;
        mInvoiceNo = invoiceNo;
        mUpdateKeyInfo = keyInfo;
        mInnerInfoItem = innerInfoItem;
    }
    // endregion

    public void execute() {
        // 事前処理
        mCallback.onPreExecute();
        // thread開始
        ExecutorService executors = Executors.newSingleThreadExecutor();
        executors.submit(new AsyncRunnable());
    }

    private int doInBackground() {
        try {
            // ******************************************************
            // インナー情報replace
            // ******************************************************
            new KonpoInnerDao().replace(Application.dbHelper.getWritableDatabase()
                    , mInvoiceNo, mUpdateKeyInfo, mInnerInfoItem);

            // ******************************************************
            // 出庫指示明細テーブル単独梱包フラグ更新
            // ******************************************************
            new SyukkoShijiDetailDao().updateSinglePackingFlag(Application.dbHelper.getWritableDatabase()
                    , mInvoiceNo, mUpdateKeyInfo);

            // 処理終了
            return 0;
        } catch (Exception e) {
            Timber.e(e);
            return -1;
        }
    }

    private void onPostExecute(int response) {
        if (response == -1) {
            mCallback.onError();
        } else {
            mCallback.onTaskFinished(response == 0);
        }
    }
    // endregion
}
