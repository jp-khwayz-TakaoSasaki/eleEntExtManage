package jp.co.khwayz.eleEntExtManage.http.task.get;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.response.ItemRemarksResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * 品目備考取得Task
 */
public class GetItemRemarksTask extends HttpTaskBase<ItemRemarksResponse> {
    // region [ private variable ]
    private final String mURL;
    private final String mJsonParam;
    // endregion

    // region [ constructor ]
    public GetItemRemarksTask(@NonNull Callback<ItemRemarksResponse> callback, String url, String paramJson) {
        super(callback);
        mURL = url;
        mJsonParam = paramJson;
    }
    // endregion

    @Override
    protected ItemRemarksResponse doInBackground() {
        ItemRemarksResponse result;
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
            result = gson.fromJson(executeResponse.getResponseJson(), ItemRemarksResponse.class);
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
    protected void onPostExecute(ItemRemarksResponse response) {
        // エラー終了の場合
        if (response == null) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(response);
    }
}
