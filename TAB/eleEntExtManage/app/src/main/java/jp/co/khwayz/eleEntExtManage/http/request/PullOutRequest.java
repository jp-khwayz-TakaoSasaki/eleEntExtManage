package jp.co.khwayz.eleEntExtManage.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PullOutRequest {
    // 拠点コード
    @SerializedName("kyotenCode")
    private String kyotenCode;
    public String getKyotenCode() {
        return kyotenCode;
    }
    public void setKyotenCode(String kyotenCode) { this.kyotenCode = kyotenCode; }
    // ユーザーID
    @SerializedName("userId")
    private String userId;
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /** パラメーターリスト */
    @SerializedName("data")
    private RequestData data = new RequestData();
    public RequestData getData() { return data; }
    public void setData(RequestData data) {
        this.data = data;
    }

    /**
     * 荷札リスト
     */
    public static class RequestData {
        @SerializedName("paramList")
        private ArrayList<RequestDetail> list = new ArrayList<>();
        public ArrayList<RequestDetail> getList() {
            return list;
        }
        public void setList(ArrayList<RequestDetail> list) {
            this.list = list;
        }
    }

    /**
     * リクエスト明細
     */
    public static class RequestDetail {
        /** 発注番号 */
        @SerializedName("hachuNo")
        private String hachuNo;
        public String getHachuNo() {
            return hachuNo;
        }
        public void setHachuNo(String hachuNo) {
            this.hachuNo = hachuNo;
        }
        /** 枝番 */
        @SerializedName("hachuEda")
        private int hachuEda;
        public int getHachuEda() {
            return hachuEda;
        }
        public void setHachuEda(int hachuEda) {
            this.hachuEda = hachuEda;
        }
    }
}
