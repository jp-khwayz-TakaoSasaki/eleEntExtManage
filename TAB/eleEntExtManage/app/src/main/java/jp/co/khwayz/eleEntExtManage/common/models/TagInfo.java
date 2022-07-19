package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.util.Locale;

public class TagInfo implements Parcelable, Cloneable {
    /** 発注番号 */
    @SerializedName("hachuNo")
    private String placeOrderNo;
    public String getPlaceOrderNo() {
        return placeOrderNo;
    }
    public void setPlaceOrderNo(String placeOrderNo) {
        this.placeOrderNo = placeOrderNo;
    }
    /** 枝番 */
    @SerializedName("hachuEda")
    private int branchNo;
    public int getBranchNo() {
        return branchNo;
    }
    public void setBranchNo(int branchNo) {
        this.branchNo = branchNo;
    }
    /** 受注番号 */
    @SerializedName("juchuNo")
    private String receiveOrderNo;
    public String getReceiveOrderNo() {
        return receiveOrderNo;
    }
    public void setReceiveOrderNo(String receiveOrderNo) {
        this.receiveOrderNo = receiveOrderNo;
    }
    /** 営業所コード */
    @SerializedName("eigyoCode")
    private String plantCode;
    public String getPlantCode() {
        return plantCode;
    }
    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }
    /** 営業所名 */
    @SerializedName("eigyoName")
    private String plantName;
    public String getPlantName() {
        return plantName;
    }
    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }
    /** 得意先名 */
    @SerializedName("tokuisakiName")
    private String customerName;
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    /** 品目コード */
    @SerializedName("hinmokuCode")
    private String itemCode;
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    /** 品目名 */
    @SerializedName("hinmokuName")
    private String itemName;
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    /** 得意先品目名 */
    @SerializedName("tokuisakiHinmoku")
    private String customerItemName;
    public String getCustomerItemName() {
        return customerItemName;
    }
    public void setCustomerItemName(String customerItemName) {
        this.customerItemName = customerItemName;
    }
    /** 発注単位 */
    @SerializedName("hachuTani")
    private String unit;
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    /** 棚番 */
    @SerializedName("tanaban")
    private String shelfNo;
    public String getShelfNo() {
        return shelfNo;
    }
    public void setShelfNo(String shelfNo) {
        this.shelfNo = shelfNo;
    }
    /** 得意先発注番号 */
    @SerializedName("tokuisakiHachuNo")
    private String customerOrderNo;
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }
    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }
    /** 入庫日 */
    @SerializedName("nyukoDate")
    private String receiptDate;
    public String getReceiptDate() {
        return receiptDate;
    }
    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }
    public String getReceiptDateSlash() {
        StringBuilder dateBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(receiptDate)) {
            dateBuilder.append(receiptDate.substring(0, 4)).append("/");
            dateBuilder.append(receiptDate.substring(4, 6)).append("/");
            dateBuilder.append(receiptDate.substring(6, 8));
        }
        return dateBuilder.toString();
    }
    /** 入庫荷姿 */
    @SerializedName("nyukoNisugata")
    private String packingType;
    public String getPackingType() {
        return packingType;
    }
    public void setPackingType(String packingType) {
        this.packingType = packingType;
    }
    /** 数量分母 */
    @SerializedName("nyukoSuBunbo")
    private double quantityDenominator;
    public double getQuantityDenominator() {
        return quantityDenominator;
    }
    public void setQuantityDenominator(double quantityDenominator) {
        this.quantityDenominator = quantityDenominator;
    }
    /** 数量(分子) */
    @SerializedName("nyukoSuBunsi")
    private double quantity;
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    /** 箱数分母 */
    @SerializedName("packageSuBunbo")
    private int boxesDenominator;
    public int getBoxesDenominator() {
        return boxesDenominator;
    }
    public void setBoxesDenominator(int boxesDenominator) {
        this.boxesDenominator = boxesDenominator;
    }
    /** 箱数分子 */
    @SerializedName("packageSuBunsi")
    private int boxesNumerator;
    public int getBoxesNumerator() {
        return boxesNumerator;
    }
    public void setBoxesNumerator(int boxesNumerator) {
        this.boxesNumerator = boxesNumerator;
    }
    /** ロケーション */
    @SerializedName("location")
    private String locationNo;
    public String getLocationNo() {
        return locationNo;
    }
    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }
    /** 同梱番号 */
    @SerializedName("combineNo")
    private long combineNo;
    public long getCombineNo() {
        return combineNo;
    }
    public void setCombineNo(long combineNo) {
        this.combineNo = combineNo;
    }
    /** セット品フラグ */
    @SerializedName("setProductFlag")
    private boolean groupingProductFlag;
    public boolean isGroupingProductFlag() {
        return groupingProductFlag;
    }
    public void setGroupingProductFlag(boolean groupingProductFlag) {
        this.groupingProductFlag = groupingProductFlag;
    }
    /** 取込区分 */
    @SerializedName("torikomiKbn")
    private String importKbn;
    public String getImportKbn() {
        return importKbn;
    }
    public void setImportKbn(String importKbn) {
        this.importKbn = importKbn;
    }
    /** 更新前枝番 */
    @SerializedName("prevHachuEda")
    private int prevBranchNo;
    public int getPrevBranchNo() {
        return prevBranchNo;
    }
    public void setPrevBranchNo(int prevBranchNo) { this.prevBranchNo = prevBranchNo; }

    /* JSON対象外 */
    /** 入庫荷姿名 */
    private String packingName;
    public String getPackingName() { return this.packingName; }
    public void setPackingName(String packingName) { this.packingName = packingName; }
    /** リストで選択されているかのフラグ */
    private boolean selected;
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    /** タグの有効/無効 */
    private boolean invalid = false;
    public boolean isInvalid() {
        return invalid;
    }
    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    // region [ constructor ]
    public TagInfo(String placeOrderNo, int branchNo) {
        this.placeOrderNo = placeOrderNo;
        this.branchNo = branchNo;
    }
    // endregion

    // region [ implements ]
    @Override
    public int describeContents() { return 0; }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeOrderNo);
        dest.writeInt(branchNo);
        dest.writeString(receiveOrderNo);
        dest.writeString(plantCode);
        dest.writeString(plantName);
        dest.writeString(customerName);
        dest.writeString(itemCode);
        dest.writeString(itemName);
        dest.writeString(customerItemName);
        dest.writeString(unit);
        dest.writeString(shelfNo);
        dest.writeString(customerOrderNo);
        dest.writeString(receiptDate);
        dest.writeString(packingType);
        dest.writeDouble(quantityDenominator);
        dest.writeDouble(quantity);
        dest.writeInt(boxesDenominator);
        dest.writeInt(boxesNumerator);
        dest.writeString(locationNo);
        dest.writeLong(combineNo);
        dest.writeByte((byte) (groupingProductFlag ? 1 : 0));
        dest.writeString(importKbn);
        dest.writeString(packingName);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeByte((byte) (invalid ? 1 : 0));
        dest.writeInt(prevBranchNo);
    }
    public TagInfo(Parcel in) {
        placeOrderNo = in.readString();
        branchNo = in.readInt();
        receiveOrderNo = in.readString();
        plantCode = in.readString();
        plantName = in.readString();
        customerName = in.readString();
        itemCode = in.readString();
        itemName = in.readString();
        customerItemName = in.readString();
        unit = in.readString();
        shelfNo = in.readString();
        customerOrderNo = in.readString();
        receiptDate = in.readString();
        packingType = in.readString();
        quantityDenominator = in.readDouble();
        quantity = in.readDouble();
        boxesDenominator = in.readInt();
        boxesNumerator = in.readInt();
        locationNo = in.readString();
        combineNo = in.readLong();
        groupingProductFlag = in.readByte() != 0;
        importKbn = in.readString();
        packingName = in.readString();
        selected = in.readByte() != 0;
        invalid = in.readByte() != 0;
        prevBranchNo = in.readInt();
    }
    public static final Creator<TagInfo> CREATOR = new Creator<TagInfo>() {
        @Override
        public TagInfo createFromParcel(Parcel in) {
            return new TagInfo(in);
        }
        @Override
        public TagInfo[] newArray(int size) {
            return new TagInfo[size];
        }
    };
    @Override
    public TagInfo clone() throws CloneNotSupportedException {
        return (TagInfo) super.clone();
    }
    // endregion

    // region [ public method ]
    /**
     * 荷札番号取得
     * @return 発注番号 - 枝番
     */
    public String getTagNo() {
        return this.placeOrderNo + " - " + String.format(Locale.JAPAN, "%03d", this.branchNo);
    }

    /**
     * 数量文字列取得
     * @param withUnit 末尾に単位を付与する場合 -> true
     * @return 数量と単位を組み合わせた文字列
     */
    public String getQuantityString(boolean withUnit) {
        DecimalFormat decimalFormat = new DecimalFormat("0.###");
        String result = decimalFormat.format(this.quantity);
        return result + (withUnit ? this.unit : "");
    }
    // endregion
}
