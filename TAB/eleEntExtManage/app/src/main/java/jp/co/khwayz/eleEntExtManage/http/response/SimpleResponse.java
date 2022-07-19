package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

/**
 * 最小項目Response
 */
public class SimpleResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("errorCode")
    private String errorCode;
    // ステータス
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    // エラーコード
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
