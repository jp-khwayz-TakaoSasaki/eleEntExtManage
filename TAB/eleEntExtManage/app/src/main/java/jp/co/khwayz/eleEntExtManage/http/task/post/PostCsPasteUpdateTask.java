package jp.co.khwayz.eleEntExtManage.http.task.post;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.casemark_paste.CaseMarkPasteScanInfo;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.request.CaseMarkPasteUpdateRequest;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * ケースマーク貼付けデータ更新Task
 */
public class PostCsPasteUpdateTask extends HttpTaskBase<SimpleResponse> {
    // region [ private variable ]
    private final String mURL;
    private final String mInvoiceNo;
    private final List<CaseMarkPasteScanInfo> mUpdateList;

    // endregion

    // region [ constructor ]
    public PostCsPasteUpdateTask(@NonNull Callback<SimpleResponse> callback, String url,
                                 String invoiceNo, List<CaseMarkPasteScanInfo> updateList) {
        super(callback);
        this.mURL = url;
        this.mInvoiceNo = invoiceNo;
        this.mUpdateList = updateList;
    }
    // endregion

    @Override
    protected SimpleResponse doInBackground() {
        SimpleResponse result;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().create();

        try {
            // リクエストパラメータ作成
            CaseMarkPasteUpdateRequest requestParam = new CaseMarkPasteUpdateRequest();
            // 明細構築
            for (CaseMarkPasteScanInfo detail : this.mUpdateList) {
                CaseMarkPasteUpdateRequest.RequestDetail req = new CaseMarkPasteUpdateRequest.RequestDetail();
                // Invoice番号
                req.setInvoiceNo(this.mInvoiceNo);
                // ケースマーク番号
                req.setCsNumber(detail.getCaseMarkNo());
                // 貼付けフラグ（1固定）
                req.setCsPasteFlag("1");
                // リストに追加
                requestParam.getList().add(req);
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
