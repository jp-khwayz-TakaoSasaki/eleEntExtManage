package jp.co.khwayz.eleEntExtManage.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 入庫実績Request
 */
public class ReceiptResultsRequest {
    // 発注番号
    @SerializedName("hachuNo")
    private String hachuNo;
    public String getHachuNo() {
        return hachuNo;
    }
    public void setHachuNo(String hachuNo) {
        this.hachuNo = hachuNo;
    }
    // 枝番
    @SerializedName("hachuEda")
    private int hachuEda;
    public int getHachuEda() {
        return hachuEda;
    }
    public void setHachuEda(int hachuEda) {
        this.hachuEda = hachuEda;
    }
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
    // パラメーターリスト
    @SerializedName("data")
    private RequestData data = new RequestData();
    public RequestData getData() { return data; }
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
        /** 入庫荷姿 */
        @SerializedName("nyukoNisugata")
        private String nyukoNisugata;
        public String getNyukoNisugata() {
            return nyukoNisugata;
        }
        public void setNyukoNisugata(String nyukoNisugata) {
            this.nyukoNisugata = nyukoNisugata;
        }
        /** 入庫数量分母 */
        @SerializedName("nyukoSuBunbo")
        private double nyukoSuBunbo;
        public double getNyukoSuBunbo() {
            return nyukoSuBunbo;
        }
        public void setNyukoSuBunbo(double nyukoSuBunbo) {
            this.nyukoSuBunbo = nyukoSuBunbo;
        }
        /** 入庫数量分子 */
        @SerializedName("nyukoSuBunsi")
        private double nyukoSuBunsi;
        public double getNyukoSuBunsi() {
            return nyukoSuBunsi;
        }
        public void setNyukoSuBunsi(double nyukoSuBunsi) {
            this.nyukoSuBunsi = nyukoSuBunsi;
        }
        /** 箱数分母 */
        @SerializedName("packageSuBunbo")
        private int packageSuBunbo;
        public int getPackageSuBunbo() {
            return packageSuBunbo;
        }
        public void setPackageSuBunbo(int packageSuBunbo) {
            this.packageSuBunbo = packageSuBunbo;
        }
        /** 箱数分子 */
        @SerializedName("packageSuBunsi")
        private int packageSuBunsi;
        public int getPackageSuBunsi() {
            return packageSuBunsi;
        }
        public void setPackageSuBunsi(int packageSuBunsi) {
            this.packageSuBunsi = packageSuBunsi;
        }
        /** セット品フラグ */
        @SerializedName("setProductFlag")
        private int setProductFlag;
        public int getSetProductFlag() {
            return setProductFlag;
        }
        public void setSetProductFlag(int setProductFlag) {
            this.setProductFlag = setProductFlag;
        }
    }
}
