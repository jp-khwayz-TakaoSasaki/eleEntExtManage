package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * 出庫予定Invoice情報受信用　Class
 */
public class SyukkoInvoiceDetailInfo implements Parcelable {

    // region [ implements ]
    public SyukkoInvoiceDetailInfo(@NotNull Parcel in) {
        invoiceNo = in.readString();
        renban = in.readInt();
        lineNo = in.readInt();
        placeOrderNo = in.readString();
        receiveOrderNo = in.readString();
        branchNo = in.readInt();
        itemCode = in.readString();
        itemName = in.readString();
        issueQuantity = in.readDouble();
        issueQuantitySummery = in.readDouble();
        unit = in.readString();
        StockQuantity = in.readDouble();
        pickingFlag = in.readString();
        packingType = in.readString();
        locationNo = in.readString();
    }

    public SyukkoInvoiceDetailInfo(String invoiceNo) {
        invoiceNo = invoiceNo;
    }

    public SyukkoInvoiceDetailInfo(String invoiceNo, int renban, int lineNo) {
        invoiceNo = invoiceNo;
        renban = renban;
        lineNo = lineNo;
    }

    public static final Creator<SyukkoInvoiceDetailInfo> CREATOR = new Creator<SyukkoInvoiceDetailInfo>() {
        @Override
        public SyukkoInvoiceDetailInfo createFromParcel(Parcel in) {
            return new SyukkoInvoiceDetailInfo(in);
        }

        @Override
        public SyukkoInvoiceDetailInfo[] newArray(int size) {
            return new SyukkoInvoiceDetailInfo[size];
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
        dest.writeInt(renban);
        dest.writeInt(lineNo);
        dest.writeString(placeOrderNo);
        dest.writeString(receiveOrderNo);
        dest.writeInt(branchNo);
        dest.writeString(itemCode);
        dest.writeString(itemName);
        dest.writeDouble(issueQuantity);
        dest.writeDouble(issueQuantitySummery);
        dest.writeString(unit);
        dest.writeDouble(StockQuantity);
        dest.writeString(pickingFlag);
        dest.writeString(packingType);
        dest.writeString(locationNo);
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
    // 連番
    @SerializedName("renban")
    private Integer renban;
    public Integer getRenban() { return renban; }
    public void setRenban(Integer renban) {
        this.renban = renban;
    }
    // 行番
    @SerializedName("lineNo")
    private Integer lineNo;
    public Integer getLineNo() { return lineNo; }
    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }
    // 発注番号
    @SerializedName("hachuNo")
    private String placeOrderNo;
    public String getPlaceOrderNo() {
        return placeOrderNo;
    }
    public void setPlaceOrderNo(String placeOrderNo) {
        this.placeOrderNo = placeOrderNo;
    }
    // 受注番号
    @SerializedName("juchuNo")
    private String receiveOrderNo;
    public String getReceiveOrderNo() {
        return receiveOrderNo;
    }
    public void setReceiveOrderNo(String receiveOrderNo) {
        this.receiveOrderNo = receiveOrderNo;
    }
    // 枝番
    @SerializedName("hachuEda")
    private int branchNo;
    public int getBranchNo() {
        return branchNo;
    }
    public void setBranchNo(int branchNo) {
        this.branchNo = branchNo;
    }
    // 品目コード
    @SerializedName("hinmokuCode")
    private String itemCode;
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    // 品目名
    @SerializedName("hinmokuMei")
    private String itemName;
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    // 出荷数量
    @SerializedName("syukkaSu")
    private double issueQuantity;
    public double getIssueQuantity() {
        return issueQuantity;
    }
    public void setIssueQuantity(double issueQuantity) {
        this.issueQuantity = issueQuantity;
    }
    // 出荷数量合計
    @SerializedName("syukkaSuSum")
    private double issueQuantitySummery;
    public double getIssueQuantitySummery() {
        return issueQuantitySummery;
    }
    public void setIssueQuantitySummery(double issueQuantitySummery) {
        this.issueQuantitySummery = issueQuantitySummery;
    }
    // 出荷単位
    @SerializedName("syukkaTani")
    private String unit;
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    // 参考在庫数量
    @SerializedName("sankoZaikoSu")
    private double StockQuantity;
    public double getStockQuantity() {
        return StockQuantity;
    }
    public void setStockQuantity(double Stockquantity) {
        this.StockQuantity = Stockquantity;
    }
    // ピッキング済みフラグ
    @SerializedName("piczumiFlg")
    private String pickingFlag;
    public String getPickingFlag() { return pickingFlag; }
    public void setPickingFlag(String pickingFlag) { this.pickingFlag = pickingFlag; }
    // 入庫荷姿
    @SerializedName("nyukoNisugata")
    private String packingType;
    public String getPackingType() {
        return packingType;
    }
    public void setPackingType(String packingType) {
        this.packingType = packingType;
    }
    // ロケ番号
    @SerializedName("locationNo")
    private String locationNo;
    public String getLocationNo() {
        return locationNo;
    }
    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }
}
