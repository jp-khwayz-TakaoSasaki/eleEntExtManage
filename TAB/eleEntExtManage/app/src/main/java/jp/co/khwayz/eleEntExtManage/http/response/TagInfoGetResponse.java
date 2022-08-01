package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.common.models.IssueTagInfo;

/**
 * 荷札情報受信Response
 */
public class TagInfoGetResponse {
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
        @SerializedName("nifudaJouhouList")
        private ArrayList<IssueTagInfo> list;
        public ArrayList<IssueTagInfo> getList() {
            return list;
        }
        public void setList(ArrayList<IssueTagInfo> list) {
            this.list = list;
        }
    }
}
