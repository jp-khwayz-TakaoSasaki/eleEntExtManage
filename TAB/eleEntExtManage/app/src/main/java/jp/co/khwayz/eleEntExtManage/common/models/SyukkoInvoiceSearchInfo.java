package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * 出庫予定Invoice検索用　Class
 */
public class SyukkoInvoiceSearchInfo implements Parcelable {

    // region [ implements ]
    public SyukkoInvoiceSearchInfo(@NotNull Parcel in) {
        invoiceNo = in.readString();
        issueDate = in.readString();
        destination = in.readString();
        customerName = in.readString();
        pickingHoldFlag = in.readString();
        listReplyDesiredDate = in.readString();
        lineCount = in.readInt();
        transportTemprature = in.readString();
        remarks = in.readString();
        dangerousGoods = in.readString();
    }

    public SyukkoInvoiceSearchInfo(String invoiceNo) {
        invoiceNo = invoiceNo;
    }
    public static final Creator<SyukkoInvoiceSearchInfo> CREATOR = new Creator<SyukkoInvoiceSearchInfo>() {
        @Override
        public SyukkoInvoiceSearchInfo createFromParcel(Parcel in) {
            return new SyukkoInvoiceSearchInfo(in);
        }

        @Override
        public SyukkoInvoiceSearchInfo[] newArray(int size) {
            return new SyukkoInvoiceSearchInfo[size];
        }
    };
    // endregion

    // region [ Override ]
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NotNull Parcel dest, int flags) {
        dest.writeString(invoiceNo);
        dest.writeString(issueDate);
        dest.writeString(destination);
        dest.writeString(customerName);
        dest.writeString(pickingHoldFlag);
        dest.writeString(listReplyDesiredDate);
        dest.writeInt(lineCount);
        dest.writeString(transportTemprature);
        dest.writeString(remarks);
        dest.writeString(dangerousGoods);
    }
    // endregion

    // Invoice番号
    @SerializedName("invoiceNo")
    private String invoiceNo;
    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    // 出荷日
    @SerializedName("syukkaDate")
    private String issueDate;
    public String getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
    // 仕向地
    @SerializedName("shimukeChi")
    private String destination;
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    // 得意先名
    @SerializedName("tokuisakiName")
    private String customerName;
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
    // リスト返信希望日
    @SerializedName("listHenshinDate")
    private String listReplyDesiredDate;
    public String getListReplyDesiredDate() {
        return listReplyDesiredDate;
    }
    public void setListReplyDesiredDate(String listReplyDesiredDate) {
        this.listReplyDesiredDate = listReplyDesiredDate;
    }
    // 行数
    @SerializedName("lineCount")
    private Integer lineCount;
    public Integer getLineCount() { return lineCount; }
    public void setLineCount(Integer lineCount) {
        this.lineCount = lineCount;
    }
    // 保冷
    @SerializedName("airTpTpondo")
    private String transportTemprature;
    public String getTransportTemprature() {
        return transportTemprature;
    }
    public void setTransportTemprature(String transportTemprature) {
        this.transportTemprature = transportTemprature;
    }
    // 備考
    @SerializedName("remarks")
    private String remarks;
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    // 危険品
    @SerializedName("airTpKikenhinKbn")
    private String dangerousGoods;
    public String getDangerousGoods() { return dangerousGoods; }
    public void setDangerousGoods(String dangerousGoods) { this.dangerousGoods = dangerousGoods; }
}
