package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

import jp.co.khwayz.eleEntExtManage.common.models.ReceiptPlanInfo;

/**
 * 入庫予定取得Response
 */
public class ReceiptPlanResponse {
    @SerializedName("status")
    private String status;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @SerializedName("errorCode")
    private String errorCode;
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    @SerializedName("data")
    private ReceiptPlanInfo data;
    public ReceiptPlanInfo getData() {
        return data;
    }
    public void setData(ReceiptPlanInfo data) {
        this.data = data;
    }
}
