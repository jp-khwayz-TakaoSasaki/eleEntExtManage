package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.common.models.SyukkoInvoiceDetailInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoInvoiceSearchInfo;

/**
 * 出庫予定Invoice情報受信Response
 */
public class SyukkoInvoiceDetailGetResponse {
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
        @SerializedName("invoiceJyohoList")
        private ArrayList<SyukkoInvoiceDetailInfo> list;
        public ArrayList<SyukkoInvoiceDetailInfo> getList() {
            return list;
        }
        public void setList(ArrayList<SyukkoInvoiceDetailInfo> list) {
            this.list = list;
        }
    }
}
