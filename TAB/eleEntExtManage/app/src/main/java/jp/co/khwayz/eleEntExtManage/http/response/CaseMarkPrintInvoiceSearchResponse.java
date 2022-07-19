package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.common.models.CaseMarkPrintInvoiceSearchInfo;

/**
 * ケースマーク情報受信Response
 */
public class CaseMarkPrintInvoiceSearchResponse {
    // ステータス
    @SerializedName("status")
    private String status;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    // エラーコード
    @SerializedName("errorCode")
    private String errorCode;
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    // 結果リスト
    @SerializedName("data")
    private ResultData data;
    public ResultData getData() {
        return data;
    }
    public void setData(ResultData data) {
        this.data = data;
    }

    public static class ResultData {
        @SerializedName("konpoZumiList")
        private ArrayList<CaseMarkPrintInvoiceSearchInfo> list;
        public ArrayList<CaseMarkPrintInvoiceSearchInfo> getList() {
            return list;
        }
        public void setList(ArrayList<CaseMarkPrintInvoiceSearchInfo> list) {
            this.list = list;
        }
    }
}
