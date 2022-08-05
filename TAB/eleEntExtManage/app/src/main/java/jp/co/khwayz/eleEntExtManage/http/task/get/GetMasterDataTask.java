package jp.co.khwayz.eleEntExtManage.http.task.get;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.database.dao.CategoryMasterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.MessageMasterDao;
import jp.co.khwayz.eleEntExtManage.http.response.KbnMasterResponse;
import jp.co.khwayz.eleEntExtManage.http.response.MsgMasterResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * 区分・メッセージマスタ取得 & SQLite登録Task
 */
public class GetMasterDataTask extends HttpTaskBase<Boolean> {

    // region [ constructor ]
    public GetMasterDataTask(@NonNull Callback<Boolean> callback) {
        super(callback);
    }
    // endregion

    @Override
    protected void onPostExecute(Boolean result) {
        // エラー終了の場合
        if (!result) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(true);
    }

    /**
     * メッセージマスタ取得 & SQLite登録
     * @return 正常終了 = true
     */
    private boolean getMsgMaster() {
        MsgMasterResponse msgResult;
        try {
            /* パラメータを作成 */
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + "/message/get";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("appKubun", "TAB");
            // HttpPost実行
            ExecuteResponse executeResponse = httpPostExecute(url, jsonObject.toString(), false);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return false;
            }
            // メッセージマスタに登録
            Gson gson = new Gson();
            msgResult = gson.fromJson(executeResponse.getResponseJson(), MsgMasterResponse.class);
            new MessageMasterDao().bulkInsert(Application.dbHelper.getWritableDatabase(), msgResult.getData().getList());
            // 処理終了
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, "GetMsgMaster : " + e.getMessage());
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.const_err_message_E9000);
            return false;
        }
    }

    /**
     * 区分マスタ取得 & SQLite登録
     * @return 正常終了 = true
     */
    private boolean getKbnMaster() {
        KbnMasterResponse kbnResult;
        try {
            /* パラメータを作成 */
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + "/kubun/get";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("kubun", "");
            // HttpPost実行
            ExecuteResponse executeResponse = httpPostExecute(url, jsonObject.toString(), false);
            //エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return false;
            }
            // 区分マスタに登録
            Gson gson = new Gson();
            kbnResult = gson.fromJson(executeResponse.getResponseJson(), KbnMasterResponse.class);
            new CategoryMasterDao().bulkInsert(Application.dbHelper.getWritableDatabase(), kbnResult.getData().getList());
            // 処理終了
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, "GetKbnMaster : " + e.getMessage());
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.const_err_message_E9000);
            return false;
        }
    }

    @Override
    protected Boolean doInBackground() {
        try {
            // メッセージマスタ取得
            if (!getMsgMaster()) { return false; }
            // 区分マスタ取得
            return getKbnMaster();
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.const_err_message_E9000);
            return false;
        }
    }
}
