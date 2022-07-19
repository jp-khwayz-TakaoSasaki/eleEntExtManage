package jp.co.khwayz.eleEntExtManage.http.task.get;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.response.TagInfoResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * 荷札情報(全項目)取得Task
 */
public class GetTagInfoFullSpecTask extends HttpTaskBase<TagInfoResponse> {
    // region [ private variable ]
    private final String mURL;
    private final String mJsonParam;
    // endregion

    // region [ constructor ]
    public GetTagInfoFullSpecTask(@NonNull Callback<TagInfoResponse> callback, String url, String jsonParam) {
        super(callback);
        mURL = url;
        mJsonParam = jsonParam;
    }
    // endregion

    @Override
    protected TagInfoResponse doInBackground() {
        TagInfoResponse result;
        try {
            // Http Post
            ExecuteResponse executeResponse = httpPostExecute(mURL, mJsonParam, true);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return null;
            }
            // JSONを変換
            Gson gson = new Gson();
            result = gson.fromJson(executeResponse.getResponseJson(), TagInfoResponse.class);
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
    protected void onPostExecute(TagInfoResponse response) {
        // エラー終了の場合
        if (response == null) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(response);
    }
}
