package jp.co.khwayz.eleEntExtManage.http.task.get;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.PackingCancelInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoInnerDao;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoOuterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.OverpackKonpoShizaiDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.http.response.InnerInfoGetResponse;
import jp.co.khwayz.eleEntExtManage.http.response.OuterInfoGetResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * 梱包解除 Task
 */
public class PackingCancelTask extends HttpTaskBase<Boolean> {

    private final String mInvoiceNo;
    private final String mDisplayCsNo;
    private ArrayList<PackingCancelInfo> mCancelInfoList;

    // region [ constructor ]
    public PackingCancelTask(@NonNull Callback<Boolean> callback, String invoiceNo, String csNo) {
        super(callback);
        mInvoiceNo = invoiceNo;
        mDisplayCsNo = csNo;
    }
    // endregion

    /**
     * 梱包解除情報取得
     * 　ローカルDBから指定Invoice番号、ケースマーク番号に紐づく梱包情報を削除する。
     *
     * @return 正常終了 = true
     */
    private boolean getPackingCancelInfo(){
        try {
            // 梱包解除情報取得
            mCancelInfoList = new KonpoOuterDao().getPackingCancelInfoList(Application.dbHelper.getWritableDatabase(),
                    mInvoiceNo, mDisplayCsNo);
            // 処理終了
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, "getOuterInfo : " + e.getMessage());
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return false;
        }
    }

    /**
     * 梱包解除
     * 　ローカルDBから指定Invoice番号、ケースマーク番号に紐づく梱包情報を削除する。
     *
     * @return 正常終了 = true
     */
    private boolean packingCancel(){
        InnerInfoGetResponse msgResult;
        try {
            // Invoice番号、オーバーパック番号をキーにオーバーパック梱包資材テーブルの該当レコードを削除する。
            new OverpackKonpoShizaiDao().delete(Application.dbHelper.getWritableDatabase(), mCancelInfoList);

            // Invoice番号、連番、行番号をキーに梱包インナーテーブルの該当レコードを削除する。
            new KonpoInnerDao().delete(Application.dbHelper.getWritableDatabase(), mCancelInfoList);

            // Invoice番号、連番、行番号をキーに出庫指示明細テーブル該当レコードを以下の通り更新する。
            //      梱包フラグ　←　"0"
            //      オーバーパック番号　←　NULL
            //      ケースマーク番号　←　NULL
            new SyukkoShijiDetailDao().cancelPackingState(Application.dbHelper.getWritableDatabase(), mCancelInfoList);

            //　梱包アウターテーブルからInvoice番号、表記用ケースマーク番号が一致するレコードを削除する。
            new KonpoOuterDao().deleteByInvoiceDisplayCsNo(Application.dbHelper.getWritableDatabase(), mInvoiceNo, mDisplayCsNo);

            // 処理終了
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, "getInnerInfo : " + e.getMessage());
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return false;
        }
    }

    @Override
    protected Boolean doInBackground() {
        OuterInfoGetResponse result;
        try {
            // 梱包解除情報取得
            if (!getPackingCancelInfo()) { return false; }
            // 梱包解除
            return packingCancel();
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        // エラー終了の場合
        if (!result) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(true);
    }
}
