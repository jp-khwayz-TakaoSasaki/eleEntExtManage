package jp.co.khwayz.eleEntExtManage.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 同梱更新Request
 */
public class CombineRegistrationRequest {
    // チェックフラグ
    @SerializedName("checkFlag")
    private int checkFlag;
    public int getCheckFlag() {
        return checkFlag;
    }
    public void setCheckFlag(int checkFlag) {
        this.checkFlag = checkFlag;
    }
    // クリアフラグ
    @SerializedName("clearFlag")
    private int clearFlag;
    public int getClearFlag() {
        return clearFlag;
    }
    public void setClearFlag(int clearFlag) {
        this.clearFlag = clearFlag;
    }
    // ユーザーID
    @SerializedName("userId")
    private String userId;
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    // パラメーターリスト
    @SerializedName("data")
    private RequestData data = new RequestData();
    public RequestData getData() {
        return data;
    }
    public void setData(RequestData data) {
        this.data = data;
    }

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
