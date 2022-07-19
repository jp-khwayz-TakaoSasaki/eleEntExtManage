package jp.co.khwayz.eleEntExtManage.common.models;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("authToken")
    private String authToken;
    @SerializedName("userId")
    private String userId;
    @SerializedName("userType")
    private String userType;
    @SerializedName("passwordReset")
    private String passwordReset;

    public String getAuthToken() { return this.authToken; }
    public void setAuthToken(String authToken) { this.authToken = authToken; }

    public String getUserId() { return this.userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserType() { return this.userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getPasswordReset() { return this.passwordReset; }
    public void setPasswordReset(String passwordReset) { this.passwordReset = passwordReset; }
}
