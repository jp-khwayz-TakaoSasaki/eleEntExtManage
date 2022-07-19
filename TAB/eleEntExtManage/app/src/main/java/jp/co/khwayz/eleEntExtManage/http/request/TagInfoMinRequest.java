package jp.co.khwayz.eleEntExtManage.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 荷札情報(最小項目)取得Request
 */
public class TagInfoMinRequest {
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
