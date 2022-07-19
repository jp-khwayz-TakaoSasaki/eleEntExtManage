package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

/**
 * 品目備考取得Response
 */
public class ItemRemarksResponse {
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

    // データ
    @SerializedName("data")
    private ResultData data;
    public ResultData getData() {
        return data;
    }
    public void setData(ResultData data) {
        this.data = data;
    }

    public static class ResultData {
        @SerializedName("hinmokuRemarks")
        private String itemRemarks;
        public String getItemRemarks() {
            return itemRemarks;
        }
        public void setItemRemarks(String itemRemarks) {
            this.itemRemarks = itemRemarks;
        }
    }
}
