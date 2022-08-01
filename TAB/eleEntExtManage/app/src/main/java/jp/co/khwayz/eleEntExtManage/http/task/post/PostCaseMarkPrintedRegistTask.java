package jp.co.khwayz.eleEntExtManage.http.task.post;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.casemark_print.CaseMarkPrintInfo;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoOuterDao;
import jp.co.khwayz.eleEntExtManage.http.request.CaseMarkDataRegistRequest;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;

/**
 * ケースマークデータ更新Task
 */
public class PostCaseMarkPrintedRegistTask extends HttpTaskBase<SimpleResponse> {
    // region [ private variable ]
    private final String mURL;
    private final String mInvoiceNo;
    private final String mPrintedFlag;

    // endregion

    // region [ constructor ]
    public PostCaseMarkPrintedRegistTask(@NonNull Callback<SimpleResponse> callback
            , String url, String invoiceNo, String printedFlag) {
        super(callback);
        mURL = url;
        mInvoiceNo = invoiceNo;
        mPrintedFlag = printedFlag;
    }
    // endregion

    @Override
    protected SimpleResponse doInBackground() {
        SimpleResponse result;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().create();

        try {
            // リクエストパラメータ作成
            CaseMarkDataRegistRequest requestParam = new CaseMarkDataRegistRequest();
            // 印刷済み対象リスト
            ArrayList<CaseMarkPrintInfo> updateList;
            // All指定の場合
            if(this.mInvoiceNo.isEmpty()){
                // 印刷済みフラグ更新
                new KonpoOuterDao().updatePrintedFlgAll(Application.dbHelper.getWritableDatabase(), mPrintedFlag);
                // 全情報取得
                updateList = new KonpoOuterDao().getAllPrintInfoList(Application.dbHelper.getWritableDatabase());
            } else {
                // 印刷済みフラグ更新
                new KonpoOuterDao().updatePrintedFlg(Application.dbHelper.getWritableDatabase(), mInvoiceNo, mPrintedFlag);
                // 指定Invoice番号情報取得
                updateList = new KonpoOuterDao().getPrintInfoList(Application.dbHelper.getWritableDatabase(),
                        this.mInvoiceNo);
            }

            // 明細構築
            for (CaseMarkPrintInfo detail : updateList) {
                CaseMarkDataRegistRequest.RequestDetail req = new CaseMarkDataRegistRequest.RequestDetail();
                // 連番
                req.setInvoiceNo(detail.getInvoiceNo());
                // ケースマーク番号
                req.setCsNumber(Integer.parseInt(detail.getCaseMarkNo()));
                // 印刷済みフラグ
                req.setCsPrintFlag(this.mPrintedFlag);
                // リストに追加
                requestParam.getList().add(req);
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
