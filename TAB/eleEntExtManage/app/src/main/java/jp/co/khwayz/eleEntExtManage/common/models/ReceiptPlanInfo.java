package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class ReceiptPlanInfo implements Parcelable {

    // region [ implements ]
    public ReceiptPlanInfo(@NotNull Parcel in) {
        placeOrderNo = in.readString();
        receiveOrderNo = in.readString();
        plantCode = in.readString();
        plantName = in.readString();
        customerName = in.readString();
        itemCode = in.readString();
        itemName = in.readString();
        customerItemName = in.readString();
        sellUnit = in.readString();
        receiveOrderUnitQty = in.readDouble();
        cvtReceiveOrderDenominator = in.readInt();
        cvtReceiveOrderNumerator = in.readInt();
        placeOrderUnit = in.readString();
        placeOrderUnitQty = in.readDouble();
        basicQtyUnit = in.readString();
        cvtPlaceOrderDenominator = in.readInt();
        cvtPlaceOrderNumerator = in.readInt();
        shelfNo = in.readString();
        customerOrderNo = in.readString();
        salesStaffName = in.readString();
        supplierItemCode = in.readString();
        importType = in.readString();
        resultsFlag = in.readString();
        receiptQuantity = in.readDouble();
        itemRemarks = in.readString();
    }

    public static final Creator<ReceiptPlanInfo> CREATOR = new Creator<ReceiptPlanInfo>() {
        @Override
        public ReceiptPlanInfo createFromParcel(Parcel in) {
            return new ReceiptPlanInfo(in);
        }

        @Override
        public ReceiptPlanInfo[] newArray(int size) {
            return new ReceiptPlanInfo[size];
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
        dest.writeString(placeOrderNo);
        dest.writeString(receiveOrderNo);
        dest.writeString(plantCode);
        dest.writeString(plantName);
        dest.writeString(customerName);
        dest.writeString(itemCode);
        dest.writeString(itemName);
        dest.writeString(customerItemName);
        dest.writeString(sellUnit);
        dest.writeDouble(receiveOrderUnitQty);
        dest.writeInt(cvtReceiveOrderDenominator);
        dest.writeInt(cvtReceiveOrderNumerator);
        dest.writeString(placeOrderUnit);
        dest.writeDouble(placeOrderUnitQty);
        dest.writeString(basicQtyUnit);
        dest.writeInt(cvtPlaceOrderDenominator);
        dest.writeInt(cvtPlaceOrderNumerator);
        dest.writeString(shelfNo);
        dest.writeString(customerOrderNo);
        dest.writeString(salesStaffName);
        dest.writeString(supplierItemCode);
        dest.writeString(importType);
        dest.writeString(resultsFlag);
        dest.writeDouble(receiptQuantity);
        dest.writeString(itemRemarks);
    }
    // endregion

    // ????????????
    @SerializedName("hachuNo")
    private String placeOrderNo;
    public String getPlaceOrderNo() {
        return placeOrderNo;
    }
    public void setPlaceOrderNo(String placeOrderNo) {
        this.placeOrderNo = placeOrderNo;
    }
    // ????????????
    @SerializedName("juchuNo")
    private String receiveOrderNo;
    public String getReceiveOrderNo() {
        return receiveOrderNo;
    }
    public void setReceiveOrderNo(String receiveOrderNo) {
        this.receiveOrderNo = receiveOrderNo;
    }
    // ??????????????????
    @SerializedName("eigyoCode")
    private String plantCode;
    public String getPlantCode() {
        return plantCode;
    }
    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }
    // ????????????
    @SerializedName("eigyoName")
    private String plantName;
    public String getPlantName() {
        return plantName;
    }
    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }
    // ????????????
    @SerializedName("tokuisakiName")
    private String customerName;
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    // ???????????????
    @SerializedName("hinmokuCode")
    private String itemCode;
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    // ?????????
    @SerializedName("hinmokuName")
    private String itemName;
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    // ???????????????
    @SerializedName("tokuisakiHinmoku")
    private String customerItemName;
    public String getCustomerItemName() {
        return customerItemName;
    }
    public void setCustomerItemName(String customerItemName) {
        this.customerItemName = customerItemName;
    }
    // ????????????
    @SerializedName("hanbaiTani")
    private String sellUnit;
    public String getSellUnit() { return sellUnit; }
    public void setSellUnit(String sellUnit) { this.sellUnit = sellUnit; }
    // ??????????????????
    @SerializedName("juchuTaniSu")
    private double receiveOrderUnitQty;
    public double getReceiveOrderUnitQty() { return receiveOrderUnitQty; }
    public void setReceiveOrderUnitQty(double receiveOrderUnitQty) { this.receiveOrderUnitQty = receiveOrderUnitQty; }
    // ??????????????????
    @SerializedName("juchuKanzanBunbo")
    private int cvtReceiveOrderDenominator;
    public int getCvtReceiveOrderDenominator() { return cvtReceiveOrderDenominator; }
    public void setCvtReceiveOrderDenominator(int cvtReceiveOrderDenominator) { this.cvtReceiveOrderDenominator = cvtReceiveOrderDenominator; }
    // ??????????????????
    @SerializedName("juchuKanzanBunsi")
    private int cvtReceiveOrderNumerator;
    public int getCvtReceiveOrderNumerator() { return cvtReceiveOrderNumerator; }
    public void setCvtReceiveOrderNumerator(int cvtReceiveOrderNumerator) { this.cvtReceiveOrderNumerator = cvtReceiveOrderNumerator; }
    // ????????????
    @SerializedName("hachuTani")
    private String placeOrderUnit;
    public String getPlaceOrderUnit() { return placeOrderUnit; }
    public void setPlaceOrderUnit(String placeOrderUnit) { this.placeOrderUnit = placeOrderUnit; }
    // ??????????????????
    @SerializedName("hachuTaniSu")
    private double placeOrderUnitQty;
    public double getPlaceOrderUnitQty() { return placeOrderUnitQty; }
    public void setPlaceOrderUnitQty(double placeOrderUnitQty) { this.placeOrderUnitQty = placeOrderUnitQty; }
    // ??????????????????
    @SerializedName("kihonSuTani")
    private String basicQtyUnit;
    public String getBasicQtyUnit() { return basicQtyUnit; }
    public void setBasicQtyUnit(String basicQtyUnit) { this.basicQtyUnit = basicQtyUnit; }
    // ??????????????????
    @SerializedName("hachuKanzanBunbo")
    private int cvtPlaceOrderDenominator;
    public int getCvtPlaceOrderDenominator() { return cvtPlaceOrderDenominator; }
    public void setCvtPlaceOrderDenominator(int cvtPlaceOrderDenominator) { this.cvtPlaceOrderDenominator = cvtPlaceOrderDenominator; }
    // ??????????????????
    @SerializedName("hachuKanzanBunsi")
    private int cvtPlaceOrderNumerator;
    public int getCvtPlaceOrderNumerator() { return cvtPlaceOrderNumerator; }
    public void setCvtPlaceOrderNumerator(int cvtPlaceOrderNumerator) { this.cvtPlaceOrderNumerator = cvtPlaceOrderNumerator; }
    // ??????
    @SerializedName("tanaban")
    private String shelfNo;
    public String getShelfNo() {
        return shelfNo;
    }
    public void setShelfNo(String shelfNo) {
        this.shelfNo = shelfNo;
    }
    // ?????????????????????
    @SerializedName("tokuisakiHachuNo")
    private String customerOrderNo;
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }
    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }
    // ??????????????????
    @SerializedName("eigyoTantouName")
    private String salesStaffName;
    public String getSalesStaffName() { return salesStaffName; }
    public void setSalesStaffName(String salesStaffName) { this.salesStaffName = salesStaffName; }
    // ???????????????????????????
    @SerializedName("siiresakiHinmokuCode")
    private String supplierItemCode;
    public String getSupplierItemCode() { return supplierItemCode; }
    public void setSupplierItemCode(String supplierItemCode) { this.supplierItemCode = supplierItemCode; }
    // ????????????
    @SerializedName("torikomiKbn")
    private String importType;
    public String getImportType() { return importType; }
    public void setImportType(String importType) { this.importType = importType; }
    // ???????????????
    @SerializedName("jissekiFlag")
    private String resultsFlag;
    public String getResultsFlag() { return resultsFlag; }
    public void setResultsFlag(String resultsFlag) { this.resultsFlag = resultsFlag; }
    // ????????????
    private double receiptQuantity;
    public double getReceiptQuantity() { return receiptQuantity; }
    public void setReceiptQuantity(double receiptQuantity) { this.receiptQuantity = receiptQuantity; }
    // ????????????
    private String itemRemarks;
    public String getItemRemarks() { return itemRemarks; }
    public void setItemRemarks(String itemRemarks) { this.itemRemarks = itemRemarks; }

    /**
     * ???????????????????????????
     * @param withUnit ???????????????????????????????????? -> true
     * @return ?????????????????????????????????????????????
     */
    public String getOrderQuantityString(boolean withUnit) {
        DecimalFormat decimalFormat = new DecimalFormat("0.###");
        String result = decimalFormat.format(this.placeOrderUnitQty);
        return result + (withUnit ? this.placeOrderUnit : "");
    }

    /**
     * ???????????????????????????
     * @param withUnit ???????????????????????????????????? -> true
     * @return ?????????????????????????????????????????????
     */
    public String getReceiptQuantityString(boolean withUnit) {
        DecimalFormat decimalFormat = new DecimalFormat("0.###");
        String result = decimalFormat.format(this.receiptQuantity);
        return result + (withUnit ? this.placeOrderUnit : "");
    }
}
