package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

import jp.co.khwayz.eleEntExtManage.common.models.UserInfo;

/**
 * ログイン認証Response
 */
public class AuthenticationResponse {
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
    private UserInfo data;
    public UserInfo getData() {
        return data;
    }
    public void setData(UserInfo data) {
        this.data = data;
    }
}
