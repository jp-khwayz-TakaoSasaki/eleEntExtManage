package jp.co.khwayz.eleEntExtManage.http.task.post;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.request.IssueRegCheckRequest;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

public class PostIssueRegCheckTask extends HttpTaskBase<SimpleResponse> {

    private IssueRegCheckRequest mRequestParam;

    public PostIssueRegCheckTask(@NonNull Callback<SimpleResponse> callback, IssueRegCheckRequest requestParam) {
        super(callback);
        mRequestParam = requestParam;
    }

    @Override
    protected SimpleResponse doInBackground() {
        SimpleResponse result;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().create();

        try {
            // リクエストパラメータ作成
            String json = gson.toJson(mRequestParam);
            Application.log.i(TAG, "Request : " + json);

            // Http Post
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + "/syukko/regist/put/";
            ExecuteResponse executeResponse = httpPostExecute(url, json, true);
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
