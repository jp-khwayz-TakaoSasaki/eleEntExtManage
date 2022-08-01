package jp.co.khwayz.eleEntExtManage.casemark_paste.task;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.casemark_paste.CaseMarkPasteReadInfo;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.request.CaseMarkDetailGetRequest;
import jp.co.khwayz.eleEntExtManage.http.response.CaseMarkDetailGetResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * ケースマーク詳細取得 Task
 */
public class CaseMarkDetailGetTask extends HttpTaskBase<CaseMarkDetailGetResponse> {

    private final String mURL;
    private final List<CaseMarkPasteReadInfo> mReqInfo;

    // region [ constructor ]
    public CaseMarkDetailGetTask(@NonNull Callback<CaseMarkDetailGetResponse> callback, String url, List<CaseMarkPasteReadInfo> reqInfo) {
        super(callback);
        mURL = url;
        mReqInfo = reqInfo;
    }
    // endregion

    @Override
    protected CaseMarkDetailGetResponse doInBackground() {
        CaseMarkDetailGetResponse msgResult;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().create();
        try {
            // リクエストパラメータ作成
            CaseMarkDetailGetRequest requestParam = new CaseMarkDetailGetRequest();
            // ケースマーク明細
            for (CaseMarkPasteReadInfo detail : mReqInfo) {
                CaseMarkDetailGetRequest.RequestDetail req = new CaseMarkDetailGetRequest.RequestDetail();
                // Invoice番号
                req.setInvoiceNo(detail.getInvoiceNo());
                // ケースマーク番号
                req.setCsNumber(detail.getCaseMarkNo());
                // リストに追加
                requestParam.getList().add(req);
            }

            // jsonに変換
            String json = gson.toJson(requestParam);

            // HttpPost実行
            ExecuteResponse executeResponse = httpPostExecute(mURL, json, true);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                Application.log.e(TAG, "getOuterInfo : StatusCode : " + executeResponse.getHttpStatusCode() + " MsgId : " + executeResponse.getErrorMsgId());
                return null;
            }
            // 取得データ取り出し
            msgResult = gson.fromJson(executeResponse.getResponseJson(), CaseMarkDetailGetResponse.class);
            // 処理終了
            return msgResult;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, "getOuterInfo : " + e.getMessage());
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return null;
        }
    }

    @Override
    protected void onPostExecute(CaseMarkDetailGetResponse result) {
        if (result != null) {
            mCallback.onTaskFinished(result);
        }
    }
}
