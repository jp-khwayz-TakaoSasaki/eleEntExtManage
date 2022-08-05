package jp.co.khwayz.eleEntExtManage.http.task.get;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.response.AuthenticationResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * ログイン認証Task
 */
public class AuthenticationTask extends HttpTaskBase<AuthenticationResponse> {
    // region [ private variable ]
    private final String mURL;
    private final String mJsonParam;
    // endregion

    // region [ constructor ]
    public AuthenticationTask(@NonNull Callback<AuthenticationResponse> callback, String url, String paramJson) {
        super(callback);
        mURL = url;
        mJsonParam = paramJson;
    }
    // endregion

    @Override
    protected void onPostExecute(AuthenticationResponse result) {
        // 正常終了の場合
        if (result != null) {
            // 処理終了通知
            mCallback.onTaskFinished(result);
        }
    }

    @Override
    protected AuthenticationResponse doInBackground() {
        AuthenticationResponse result;
        try {
            // Http Post
            ExecuteResponse executeResponse = httpPostExecute(mURL, mJsonParam, false);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return null;
            }
            // JSONを変換
            Gson gson = new Gson();
            result = gson.fromJson(executeResponse.getResponseJson(), AuthenticationResponse.class);
            return result;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.const_err_message_E9000);
            return null;
        }
    }
}
