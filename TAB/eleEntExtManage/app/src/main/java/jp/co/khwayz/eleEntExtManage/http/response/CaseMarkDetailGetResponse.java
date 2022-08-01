package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

import jp.co.khwayz.eleEntExtManage.common.models.CaseMarkDetailInfo;

/**
 * アウター情報受信Response
 */
public class CaseMarkDetailGetResponse {
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
    // 結果データ
    @SerializedName("data")
    private CaseMarkDetailInfo data;
    public CaseMarkDetailInfo getData() {
        return data;
    }
    public void setData(CaseMarkDetailInfo data) {
        this.data = data;
    }
}
