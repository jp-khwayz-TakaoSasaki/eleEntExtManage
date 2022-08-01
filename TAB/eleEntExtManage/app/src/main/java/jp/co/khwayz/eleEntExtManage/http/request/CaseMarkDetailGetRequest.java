package jp.co.khwayz.eleEntExtManage.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * ケースマーク情報取得Request
 */
public class CaseMarkDetailGetRequest {
    // ケースマークチェックリスト
    @SerializedName("csChkList")
    private ArrayList<RequestDetail> list = new ArrayList<>();
    public ArrayList<RequestDetail> getList() {
            return list;
        }
    public void setList(ArrayList<RequestDetail> list) {
            this.list = list;
        }

    public static class RequestDetail {
        /** Invoice番号 */
        @SerializedName("invoiceNo")
        private String invoiceNo;
        public String getInvoiceNo() {
            return invoiceNo;
        }
        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }
        /** ケースマーク番号 */
        @SerializedName("csNumber")
        private int csNumber;
        public int getCsNumber() {
            return csNumber;
        }
        public void setCsNumber(int csNumber) {
            this.csNumber = csNumber;
        }
    }
}
