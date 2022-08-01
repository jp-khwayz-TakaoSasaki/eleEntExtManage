package jp.co.khwayz.eleEntExtManage.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * ケースマークデータ登録Request
 */
public class CaseMarkDataRegistRequest {
    // 更新対象リスト
    @SerializedName("putlist")
    private ArrayList<RequestDetail> list = new ArrayList<>();
    public ArrayList<RequestDetail> getList() {
            return list;
        }
    public void setList(ArrayList<RequestDetail> list) {
            this.list = list;
        }

    public static class RequestDetail {
        // Invoice番号
        @SerializedName("invoiceNo")
        private String invoiceNo;
        public String getInvoiceNo() {
            return invoiceNo;
        }
        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }
        /** ケースマーク番号 */
        /** ケースマーク番号 */
        @SerializedName("csNumber")
        private int csNumber;
        public int getCsNumber() {
            return csNumber;
        }
        public void setCsNumber(int csNumber) {
            this.csNumber = csNumber;
        }
        // 印刷済みフラグ
        @SerializedName("csPrintFlag")
        private String csPrintFlag;
        public String getCsPrintFlag() {
            return csPrintFlag;
        }
        public void setCsPrintFlag(String csPrintFlag) {
            this.csPrintFlag = csPrintFlag;
        }
    }
}
