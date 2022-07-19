package jp.co.khwayz.eleEntExtManage.http.task.post;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;
import jp.co.khwayz.eleEntExtManage.http.request.PullOutRequest;
import jp.co.khwayz.eleEntExtManage.http.response.TagInfoResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * 抜き取り実績登録Task
 */
public class PostPullOutTask extends HttpTaskBase<TagInfoResponse> {
    // region [ private variable ]
    private final String mURL;
    private final ArrayList<TagInfo> mParamList;
    // endregion

    // region [ constructor ]
    public PostPullOutTask(@NonNull Callback<TagInfoResponse> callback, String url, ArrayList<TagInfo> paramList) {
        super(callback);
        mURL = url;
        mParamList = paramList;
    }
    // endregion

    @Override
    protected TagInfoResponse doInBackground() {
        TagInfoResponse result;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().create();

        try {
            // リクエストパラメータ作成
            PullOutRequest requestParam = new PullOutRequest();
            // 拠点コード
            requestParam.setKyotenCode(Application.affiliationBase);
            // ユーザーID
            requestParam.setUserId(Application.userInfo.getUserId());
            // 明細
            for (TagInfo tag : mParamList) {
                PullOutRequest.RequestDetail req = new PullOutRequest.RequestDetail();
                // 発注番号
                req.setHachuNo(tag.getPlaceOrderNo());
                // 枝番
                req.setHachuEda(tag.getBranchNo());
                // リストに追加
                requestParam.getData().getList().add(req);
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

            // 戻り値に変換
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
