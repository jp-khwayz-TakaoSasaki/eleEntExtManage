package jp.co.khwayz.eleEntExtManage.http.task.get;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.response.CaseMarkPasteTagGetResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * ケースマーク貼付け荷札情報受信 Task
 */
public class CaseMarkPasteTagGetTask extends HttpTaskBase<CaseMarkPasteTagGetResponse> {

    private final String mURL;
    private final String mJsonParam;

    // region [ constructor ]
    public CaseMarkPasteTagGetTask(@NonNull Callback<CaseMarkPasteTagGetResponse> callback
            , String url, String paramJson) {
        super(callback);
        mURL = url;
        mJsonParam = paramJson;
    }
    // endregion

   @Override
    protected CaseMarkPasteTagGetResponse doInBackground() {
        CaseMarkPasteTagGetResponse result;
        try {
            // Http Post
            ExecuteResponse executeResponse = httpPostExecute(mURL, mJsonParam, true);
            //エラー
            if (executeResponse.isError()) {
                // エラーCallBack起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                Application.log.e(TAG, "CaseMarkPasteTagGetTask : StatusCode : " + executeResponse.getHttpStatusCode() + " MsgId : " + executeResponse.getErrorMsgId());
                return null;
            } else {
                Gson gson = new Gson();
                result = gson.fromJson(executeResponse.getResponseJson(), CaseMarkPasteTagGetResponse.class);
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
    protected void onPostExecute(CaseMarkPasteTagGetResponse response) {
        if (response != null) {
            mCallback.onTaskFinished(response);
        }
    }
}
