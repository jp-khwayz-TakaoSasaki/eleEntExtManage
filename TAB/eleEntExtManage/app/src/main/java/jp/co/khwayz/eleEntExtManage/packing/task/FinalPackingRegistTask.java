package jp.co.khwayz.eleEntExtManage.packing.task;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.models.OuterInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoShijiCsKeyInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoOuterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import timber.log.Timber;

public class FinalPackingRegistTask {
    // region [ private variable ]
    public static final String TAG = FinalPackingRegistTask.class.getSimpleName();
    private final FinalPackingRegistTask.Callback mCallback;
    private final int mStartCaseMarkNum;
    private final int mEndCaseMarkNum;
    private final String mInitHyokiCaseMarkNum;
    private final OuterInfo mRegistOuterInfo;
    private final List<SyukkoShijiCsKeyInfo> mKeyList;
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
    public FinalPackingRegistTask(@NonNull FinalPackingRegistTask.Callback callback,
                                  int startNum, int endNum, String initCsNum, OuterInfo registInfo, List<SyukkoShijiCsKeyInfo> keyList) {
        mCallback = callback;
        mStartCaseMarkNum = startNum;
        mEndCaseMarkNum = endNum;
        mInitHyokiCaseMarkNum = initCsNum;
        mRegistOuterInfo = registInfo;
        mKeyList = keyList;
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
            // 初期表記ケースマーク番号あり（修正）時は梱包アウターを削除
            if(!mInitHyokiCaseMarkNum.isEmpty()){
                new KonpoOuterDao().deleteByInvoiceDisplayCsNo(Application.dbHelper.getWritableDatabase()
                        ,mRegistOuterInfo.getInvoiceNo(), mInitHyokiCaseMarkNum);
            }

            // 梱包アウター登録
            for(int i=mStartCaseMarkNum; i<=mEndCaseMarkNum; i++){
                mRegistOuterInfo.setCsNumber(i);
                new KonpoOuterDao().insertRecord(Application.dbHelper.getWritableDatabase(), mRegistOuterInfo);
            }

            // 出庫指示明細ケースマーク番号更新
            for(SyukkoShijiCsKeyInfo info : mKeyList){
                new SyukkoShijiDetailDao().updateCsNo(Application.dbHelper.getWritableDatabase(), info);
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
