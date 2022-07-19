package jp.co.khwayz.eleEntExtManage.http.task.get;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoInnerDao;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoOuterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.OverpackKonpoShizaiDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.http.response.InnerInfoGetResponse;
import jp.co.khwayz.eleEntExtManage.http.response.OuterInfoGetResponse;
import jp.co.khwayz.eleEntExtManage.http.response.OverPackPackingMaterialInfoGetResponse;
import jp.co.khwayz.eleEntExtManage.http.response.TagInfoGetResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * アウター情報受信 Task
 */
public class PackingRelatedInfoGetTask extends HttpTaskBase<Boolean> {

    private final String mInvoiceNo;

    // region [ constructor ]
    public PackingRelatedInfoGetTask(@NonNull Callback<Boolean> callback, String invoiceNo) {
        super(callback);
        mInvoiceNo = invoiceNo;
    }
    // endregion

    /**
     * アウター情報受信
     * 　WebAPI「アウター情報受信」でインボイスNoに紐づいた梱包情報を取得する。
     *
     * @return 正常終了 = true
     */
    private boolean getOuterInfo(){
        OuterInfoGetResponse msgResult;
        try {
            /* パラメータを作成 */
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_OUTER_INFO_GET;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("invoiceNo", mInvoiceNo);
            // HttpPost実行
            ExecuteResponse executeResponse = httpPostExecute(url, jsonObject.toString(), false);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                Application.log.e(TAG, "getOuterInfo : StatusCode : " + executeResponse.getHttpStatusCode() + " MsgId : " + executeResponse.getErrorMsgId());
                return false;
            }
            // メッセージマスタに登録
            Gson gson = new Gson();
            msgResult = gson.fromJson(executeResponse.getResponseJson(), OuterInfoGetResponse.class);
            new KonpoOuterDao().bulkInsert(Application.dbHelper.getWritableDatabase(), msgResult.getData().getList());
            // 処理終了
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, "getOuterInfo : " + e.getMessage());
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return false;
        }
    }

    /**
     * インナー情報受信
     * 　WebAPI「インナー情報受信」でインボイスNoに紐づいたインナー情報を取得する。
     *
     * @return 正常終了 = true
     */
    private boolean getInnerInfo(){
        InnerInfoGetResponse msgResult;
        try {
            /* パラメータを作成 */
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_INNER_INFO_GET;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("invoiceNo", mInvoiceNo);
            // HttpPost実行
            ExecuteResponse executeResponse = httpPostExecute(url, jsonObject.toString(), false);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                Application.log.e(TAG, "getInnerInfo : StatusCode : " + executeResponse.getHttpStatusCode() + " MsgId : " + executeResponse.getErrorMsgId());
                return false;
            }
            // メッセージマスタに登録
            Gson gson = new Gson();
            msgResult = gson.fromJson(executeResponse.getResponseJson(), InnerInfoGetResponse.class);
            new KonpoInnerDao().bulkInsert(Application.dbHelper.getWritableDatabase(), msgResult.getData().getList());
            // 処理終了
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, "getInnerInfo : " + e.getMessage());
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return false;
        }
    }

    /**
     * オーバーパック梱包資材情報受信
     * 　WebAPI「オーバーパック梱包資材情報受信」でインボイスNoに紐づいたオーバーパック梱包資材情報を取得する。
     *
     * @return 正常終了 = true
     */
    private boolean getOverPackPackingMaterialInfo(){
        OverPackPackingMaterialInfoGetResponse msgResult;
        try {
            /* パラメータを作成 */
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_OVER_PACK_KONPOSHIZAI_GET;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("invoiceNo", mInvoiceNo);
            // HttpPost実行
            ExecuteResponse executeResponse = httpPostExecute(url, jsonObject.toString(), false);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                Application.log.e(TAG, "getOverPackPackingMaterialInfo : StatusCode : " + executeResponse.getHttpStatusCode() + " MsgId : " + executeResponse.getErrorMsgId());
                return false;
            }
            // メッセージマスタに登録
            Gson gson = new Gson();
            msgResult = gson.fromJson(executeResponse.getResponseJson(), OverPackPackingMaterialInfoGetResponse.class);
            new OverpackKonpoShizaiDao().bulkInsert(Application.dbHelper.getWritableDatabase(), msgResult.getData().getList());
            // 処理終了
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, "getOverPackPackingMaterialInfo : " + e.getMessage());
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return false;
        }
    }

    /**
     * 荷札情報受信
     * 　WebAPI「荷札情報受信」でインボイスNoに紐づいた荷札情報を取得する。
     *
     * @return 正常終了 = true
     */
    private boolean getTagInfo(){
        TagInfoGetResponse msgResult;
        try {
            /* パラメータを作成 */
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_NIFUDA_INFO_GET;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("invoiceNo", mInvoiceNo);
            // HttpPost実行
            ExecuteResponse executeResponse = httpPostExecute(url, jsonObject.toString(), false);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                Application.log.e(TAG, "getTagInfo : StatusCode : " + executeResponse.getHttpStatusCode() + " MsgId : " + executeResponse.getErrorMsgId());
                return false;
            }
            // メッセージマスタに登録
            Gson gson = new Gson();
            msgResult = gson.fromJson(executeResponse.getResponseJson(), TagInfoGetResponse.class);
            new SyukkoShijiDetailDao().bulkInsertTagInfoGet(Application.dbHelper.getWritableDatabase(), msgResult.getData().getList());
            // 処理終了
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, "getTagInfo : " + e.getMessage());
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return false;
        }
    }

    @Override
    protected Boolean doInBackground() {
        OuterInfoGetResponse result;
        try {
            // アウター情報受信
            if (!getOuterInfo()) { return false; }
            // インナー情報受信
            if (!getInnerInfo()) { return false; }
            // オーバーパック梱包資材情報受信
            if (!getOverPackPackingMaterialInfo()) { return false; }
            // 荷札情報受信
            return getTagInfo();
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        // エラー終了の場合
        if (!result) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(true);
    }
}
