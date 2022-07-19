package jp.co.khwayz.eleEntExtManage.http.task.get;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;
import jp.co.khwayz.eleEntExtManage.http.request.TagInfoMinRequest;
import jp.co.khwayz.eleEntExtManage.http.response.TagInfoResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * 荷札情報(最小項目)取得Task
 */
public class GetTagInfoMinSpecTask extends HttpTaskBase<TagInfoResponse> {
    // region [ private variable ]
    private final String mURL;
    private final ArrayList<TagInfo> mTagList;
    // endregion

    // region [ constructor ]
    public GetTagInfoMinSpecTask(@NonNull Callback<TagInfoResponse> callback, String url, ArrayList<TagInfo> tagList) {
        super(callback);
        mURL = url;
        mTagList = tagList;
    }
    // endregion

    @Override
    protected TagInfoResponse doInBackground() {
        TagInfoResponse result;
        try {
            // JSONパラメータを作成
            TagInfoMinRequest reqParam = new TagInfoMinRequest();
            for (TagInfo param : mTagList) {
                TagInfoMinRequest.RequestDetail detail = new TagInfoMinRequest.RequestDetail();
                detail.setHachuNo(param.getPlaceOrderNo());
                detail.setHachuEda(param.getBranchNo());
                reqParam.getData().getList().add(detail);
            }
            // GSONインスタンス生成
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.serializeNulls().create();
            // JSON文字列生成
            String json = gson.toJson(reqParam);

            // Http Post
            ExecuteResponse executeResponse = httpPostExecute(mURL, json, true);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return null;
            }
            // JSONを変換
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
