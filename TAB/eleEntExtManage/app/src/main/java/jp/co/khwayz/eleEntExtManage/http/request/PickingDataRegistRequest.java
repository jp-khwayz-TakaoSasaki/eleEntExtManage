package jp.co.khwayz.eleEntExtManage.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * ピッキングデータ登録Request
 */
public class PickingDataRegistRequest {
    // Invoice番号
    @SerializedName("invoiceNo")
    private String invoiceNo;
    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    // ピッキング保留フラグ
    @SerializedName("pickingHoldFlag")
    private String pickingHoldFlag;
    public String getPickingHoldFlag() {
        return pickingHoldFlag;
    }
    public void setPickingHoldFlag(String pickingHoldFlag) {
        this.pickingHoldFlag = pickingHoldFlag;
    }
    // ピッキング済明細リスト
    @SerializedName("piczumiList")
    private ArrayList<RequestDetail> list = new ArrayList<>();
    public ArrayList<RequestDetail> getList() {
            return list;
        }
    public void setList(ArrayList<RequestDetail> list) {
            this.list = list;
        }

    public static class RequestDetail {
        /** 連番 */
        @SerializedName("renban")
        private int renban;
        public int getRenban() {
            return renban;
        }
        public void setRenban(int renban) {
            this.renban = renban;
        }
        /** 行番号 */
        @SerializedName("lineNo")
        private int lineNo;
        public int getLineNo() {
            return lineNo;
        }
        public void setLineNo(int lineNo) {
            this.lineNo = lineNo;
        }
    }
}
