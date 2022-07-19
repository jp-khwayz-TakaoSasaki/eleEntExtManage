package jp.co.khwayz.eleEntExtManage.http.task.get;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.response.ReceiptPlanResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * 入庫予定取得 Task
 */
public class GetReceiptPlanTask extends HttpTaskBase<ReceiptPlanResponse> {

    // region [ private variable ]
    private final String mURL;
    private final String mJsonParam;
    // endregion

    // region [ constructor ]
    public GetReceiptPlanTask(@NonNull Callback<ReceiptPlanResponse> callback, String url, String paramJson) {
        super(callback);
        mURL = url;
        mJsonParam = paramJson;
    }
    // endregion

    @Override
    protected ReceiptPlanResponse doInBackground() {
        ReceiptPlanResponse result;
        try {
            // Http Post
            ExecuteResponse executeResponse = httpPostExecute(mURL, mJsonParam, true);
            //エラー
            if (executeResponse.isError()) {
                // エラーCallBack起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return null;
            } else {
                Gson gson = new Gson();
                result = gson.fromJson(executeResponse.getResponseJson(), ReceiptPlanResponse.class);
                return result;
            }
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return null;
        }
    }

    @Override
    protected void onPostExecute(ReceiptPlanResponse response) {
        if (response != null) {
            mCallback.onTaskFinished(response);
        }
    }
}
