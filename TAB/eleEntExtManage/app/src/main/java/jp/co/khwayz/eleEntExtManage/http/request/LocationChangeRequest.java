package jp.co.khwayz.eleEntExtManage.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * ロケーション更新Request
 */
public class LocationChangeRequest {
    /** 移動区分 */
    @SerializedName("idoKubun")
    private String idoKubun;
    public String getIdoKubun() {
        return idoKubun;
    }
    public void setIdoKubun(String idoKubun) {
        this.idoKubun = idoKubun;
    }
    /** ロケーション */
    @SerializedName("locationNo")
    private String locationNo;
    public String getLocationNo() {
        return locationNo;
    }
    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }
    /** 拠点コード */
    @SerializedName("kyotenCode")
    private String kyotenCode;
    public String getKyotenCode() {
        return kyotenCode;
    }
    public void setKyotenCode(String kyotenCode) { this.kyotenCode = kyotenCode; }
    /** ユーザーID */
    @SerializedName("userId")
    private String userId;
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /** 実績ファイル作成フラグ */
    @SerializedName("createFile")
    private int createFile;
    public int getCreateFile() {
        return createFile;
    }
    public void setCreateFile(int createFile) {
        this.createFile = createFile;
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
        /** セット品フラグ */
        @SerializedName("setProductFlag")
        private int setProductFlag;
        public int getSetProductFlag() { return setProductFlag; }
        public void setSetProductFlag(int setProductFlag) {
            this.setProductFlag = setProductFlag;
        }
        /** 取込区分 */
        @SerializedName("torikomiKbn")
        private String torikomiKbn;
        public String getTorikomiKbn() {
            return torikomiKbn;
        }
        public void setTorikomiKbn(String torikomiKbn) {
            this.torikomiKbn = torikomiKbn;
        }
    }

}
