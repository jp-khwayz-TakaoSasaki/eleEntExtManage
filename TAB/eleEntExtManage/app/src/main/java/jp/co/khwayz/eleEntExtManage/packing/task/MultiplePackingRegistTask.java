package jp.co.khwayz.eleEntExtManage.packing.task;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.models.OverPackKonpoShizaiInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.OverpackKonpoShizaiDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.packing.OverPackListInfo;
import timber.log.Timber;

public class MultiplePackingRegistTask {
    // region [ private variable ]
    public static final String TAG = MultiplePackingRegistTask.class.getSimpleName();
    private final MultiplePackingRegistTask.Callback mCallback;

    private final String mInvoiceNo;
    private int mNewOverPackNo;
    private final List<OverPackListInfo> mOverPackInfoList;
    private final ArrayList<OverPackKonpoShizaiInfo> mKonpoShizaiList;

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
    public MultiplePackingRegistTask(@NonNull MultiplePackingRegistTask.Callback callback
            , String invoiceNo, int newOverPackNo, List<OverPackListInfo> overPackListInfo
            ,ArrayList<OverPackKonpoShizaiInfo> konpoShizaiInfo ) {
        mCallback = callback;
        mInvoiceNo = invoiceNo;
        mNewOverPackNo = newOverPackNo;
        mOverPackInfoList = overPackListInfo;
        mKonpoShizaiList = konpoShizaiInfo;
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
            // ***************************
            // オーバーパック番号設定
            // ***************************
            ArrayList<OverPackListInfo> updateList = new ArrayList<>(mOverPackInfoList);
            new SyukkoShijiDetailDao().registOverPackNo(Application.dbHelper.getWritableDatabase()
                    ,mNewOverPackNo
                    ,mInvoiceNo
                    ,updateList
            );

            // ***************************
            // 梱包資材テーブル登録
            // ***************************
            if(mKonpoShizaiList.size() > 0){
                new OverpackKonpoShizaiDao().insert(Application.dbHelper.getWritableDatabase(), mKonpoShizaiList);
            }

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
