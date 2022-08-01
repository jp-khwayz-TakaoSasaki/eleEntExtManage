package jp.co.khwayz.eleEntExtManage.http.task.get;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.database.dao.InvoiceTempDao;
import jp.co.khwayz.eleEntExtManage.http.response.PackInstructionsResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

public class GetPackInstructionsTask extends HttpTaskBase<PackInstructionsResponse> {

    // region [ private variable ]
    private final String mInvoiceNo;
    // endregion

    public GetPackInstructionsTask(Callback<PackInstructionsResponse> callback, String invoiceNo) {
        super(callback);
        mInvoiceNo = invoiceNo;
    }

    @Override
    protected PackInstructionsResponse doInBackground() {
        PackInstructionsResponse result;
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("invoiceNo", mInvoiceNo);
            // Http Post
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + "/syukko/sijinaiyojoho/get/";
            ExecuteResponse executeResponse = httpPostExecute(url, jsonObject.toString(), true);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return null;
            }

            // JSON変換
            Gson gson = new Gson();
            result = gson.fromJson(executeResponse.getResponseJson(), PackInstructionsResponse.class);
            // 業務エラーの場合
            if (result.getStatus().equals(Constants.API_RESPONSE_STATUS_OPE_ERR)) {
                // -1(その他エラー)
                raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E2006);
                return null;
            }

            // SQLiteに登録
            new InvoiceTempDao().bulkInsert(Application.dbHelper.getWritableDatabase(), mInvoiceNo, result.getData().getList());

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
    protected void onPostExecute(PackInstructionsResponse response) {
        // エラー終了の場合
        if (response == null) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(response);
    }
}
