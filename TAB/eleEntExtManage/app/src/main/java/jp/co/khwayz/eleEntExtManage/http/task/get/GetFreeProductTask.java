package jp.co.khwayz.eleEntExtManage.http.task.get;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.response.FreeProductResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * 無償品取得Task
 */
public class GetFreeProductTask extends HttpTaskBase<FreeProductResponse> {
    // region [ private variable ]
    private final String mURL;
    // endregion

    // region [ constructor ]
    public GetFreeProductTask(@NonNull Callback<FreeProductResponse> callback, String url) {
        super(callback);
        mURL = url;
    }
    // endregion

    @Override
    protected FreeProductResponse doInBackground() {
        FreeProductResponse result;
        try {
            // Http Post
            ExecuteResponse executeResponse = httpPostExecute(mURL, null, true);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return null;
            }
            // JSON変換
            Gson gson = new Gson();
            result = gson.fromJson(executeResponse.getResponseJson(), FreeProductResponse.class);
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
    protected void onPostExecute(FreeProductResponse response) {
        // エラー終了の場合
        if (response == null) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(response);
    }
}
