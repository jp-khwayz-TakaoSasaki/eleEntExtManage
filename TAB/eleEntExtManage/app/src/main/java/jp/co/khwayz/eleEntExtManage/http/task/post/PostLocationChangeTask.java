package jp.co.khwayz.eleEntExtManage.http.task.post;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;
import jp.co.khwayz.eleEntExtManage.http.request.LocationChangeRequest;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * ロケーション更新Task
 */
public class PostLocationChangeTask extends HttpTaskBase<SimpleResponse> {
    // region [ private variable ]
    private final String mURL;
    private final LocationChangeRequest mRequestParam;
    private final ArrayList<TagInfo> mParamList;
    // endregion

    // region [ constructor ]
    public PostLocationChangeTask(@NonNull Callback<SimpleResponse> callback, String url,
                                  LocationChangeRequest requestParam, ArrayList<TagInfo> paramList) {
        super(callback);
        mURL = url;
        mRequestParam = requestParam;
        mParamList = paramList;
    }
    // endregion

    @Override
    protected SimpleResponse doInBackground() {
        SimpleResponse result;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().create();

        try {
            // リクエストパラメータ作成
            for (TagInfo tag : mParamList) {
                LocationChangeRequest.RequestDetail req = new LocationChangeRequest.RequestDetail();
                // 発注番号
                req.setHachuNo(tag.getPlaceOrderNo());
                // 枝番
                req.setHachuEda(tag.getBranchNo());
                // セット品フラグ
                req.setSetProductFlag(tag.isGroupingProductFlag() ? 1 : 0);
                // 取込区分
                req.setTorikomiKbn(tag.getImportKbn());
                // リストに追加
                mRequestParam.getData().getList().add(req);
            }
            // jsonに変換
            String json = gson.toJson(mRequestParam);

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