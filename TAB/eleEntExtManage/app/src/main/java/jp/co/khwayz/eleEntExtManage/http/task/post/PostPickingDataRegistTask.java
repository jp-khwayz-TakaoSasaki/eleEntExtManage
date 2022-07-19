package jp.co.khwayz.eleEntExtManage.http.task.post;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoInvoiceDetailInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.http.request.PickingDataRegistRequest;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * ピッキングデータ登録Task
 */
public class PostPickingDataRegistTask extends HttpTaskBase<SimpleResponse> {
    // region [ private variable ]
    private final String mURL;
    private final String mInvoiceNo;
    private final String mPickingHoldFrag;

    // endregion

    // region [ constructor ]
    public PostPickingDataRegistTask(@NonNull Callback<SimpleResponse> callback, String url,
                                     String invoiceNo, String pickingHoldFlag) {
        super(callback);
        mURL = url;
        mInvoiceNo = invoiceNo;
        mPickingHoldFrag = pickingHoldFlag;
    }
    // endregion

    @Override
    protected SimpleResponse doInBackground() {
        SimpleResponse result;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().create();

        try {
            // リクエストパラメータ作成
            PickingDataRegistRequest requestParam = new PickingDataRegistRequest();
            // Invoice番号
            requestParam.setInvoiceNo(mInvoiceNo);
            // ピッキング保留フラグ
            requestParam.setPickingHoldFlag(mPickingHoldFrag);
            // ピッキング済明細
            if(mPickingHoldFrag.equals(Constants.FLAG_TRUE)) {
                SyukkoShijiDetailDao dao = new SyukkoShijiDetailDao();
                ArrayList<SyukkoInvoiceDetailInfo> detailInfo = dao.getSyukkoShijiListPicked(Application.dbHelper.getWritableDatabase());
                // 明細
                for (SyukkoInvoiceDetailInfo detail : detailInfo) {
                    PickingDataRegistRequest.RequestDetail req = new PickingDataRegistRequest.RequestDetail();
                    // 連番
                    req.setRenban(detail.getRenban());
                    // 行番号
                    req.setLineNo(detail.getLineNo());
                    // リストに追加
                    requestParam.getList().add(req);
                }
            }
            // jsonに変換
            String json = gson.toJson(requestParam);

            // Http Post
            ExecuteResponse executeResponse = httpPostExecute(mURL, json, true);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return null;
            }
            // JSONを変換
            result = gson.fromJson(executeResponse.getResponseJson(), SimpleResponse.class);
            return result;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return null;
        }
    }

    @Override
    protected void onPostExecute(SimpleResponse response) {
        // エラー終了の場合
        if (response == null) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(response);
    }
}
