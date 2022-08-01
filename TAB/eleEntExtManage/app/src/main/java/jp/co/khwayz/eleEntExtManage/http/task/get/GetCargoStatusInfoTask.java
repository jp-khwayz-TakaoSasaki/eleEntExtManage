package jp.co.khwayz.eleEntExtManage.http.task.get;

import com.google.gson.Gson;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.response.CargoStatusResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

public class GetCargoStatusInfoTask  extends HttpTaskBase<CargoStatusResponse> {

    // region [ private variable ]
    private final String mJsonParam;
    // endregion

    // region [ constructor ]
    public GetCargoStatusInfoTask(Callback<CargoStatusResponse> callback, String paramJson) {
        super(callback);
        mJsonParam = paramJson;
    }
    // endregion

    @Override
    protected CargoStatusResponse doInBackground() {
        CargoStatusResponse result;
        try {
            // Http Post
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + "/syukko/kamotujyotai/get/";
            ExecuteResponse executeResponse = httpPostExecute(url, mJsonParam, true);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return null;
            }
            // JSON変換
            Gson gson = new Gson();
            result = gson.fromJson(executeResponse.getResponseJson(), CargoStatusResponse.class);
            // 業務エラーの場合
            if (result.getStatus().equals(Constants.API_RESPONSE_STATUS_OPE_ERR)) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), R.string.err_message_E2006);
                return null;
            }
            return result;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.const_err_message_E9000);
            return null;
        }
    }

    @Override
    protected void onPostExecute(CargoStatusResponse response) {
        // エラー終了の場合
        if (response == null) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(response);
    }
}
