package jp.co.khwayz.eleEntExtManage.http.task.get;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.response.ShippingInstructionsResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

public class GetShippingInstructionsTask extends HttpTaskBase<ShippingInstructionsResponse> {

    // region [ private variable ]
    private final String mInvoiceNo;
    // endregion

    public GetShippingInstructionsTask(@NonNull Callback<ShippingInstructionsResponse> callback, String invoiceNo) {
        super(callback);
        mInvoiceNo = invoiceNo;
    }

    @Override
    protected ShippingInstructionsResponse doInBackground() {
        ShippingInstructionsResponse result;
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("invoiceNo", mInvoiceNo);
            // Http Post
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + "/syukko/syukkashijiran/get/";
            ExecuteResponse executeResponse = httpPostExecute(url, jsonObject.toString(), true);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return null;
            }

            // JSON変換
            Gson gson = new Gson();
            result = gson.fromJson(executeResponse.getResponseJson(), ShippingInstructionsResponse.class);
            // 業務エラーの場合
            if (result.getStatus().equals(Constants.API_RESPONSE_STATUS_OPE_ERR)) {
                // -1(その他エラー)
                raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E2006);
                return null;
            }

            // 処理終了
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
    protected void onPostExecute(ShippingInstructionsResponse response) {
        // エラー終了の場合
        if (response == null) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(response);
    }
}
