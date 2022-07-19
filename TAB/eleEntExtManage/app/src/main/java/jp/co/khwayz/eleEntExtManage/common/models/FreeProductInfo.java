package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * 無償品情報 Class
 */
public class FreeProductInfo implements Parcelable {

    // region [ private variable ]
    @SerializedName("hachuNo")
    private String placeOrderNo;
    @SerializedName("eigyoCode")
    private String plantCode;
    @SerializedName("eigyoName")
    private String plantName;
    @SerializedName("hinmokuCode")
    private String itemCode;
    @SerializedName("hinmokuName")
    private String itemName;
    @SerializedName("hachuTaniSu")
    private double quantity;
    @SerializedName("kihonSuTani")
    private String unit;
    // endregion

    // region [ constructor ]
    /**
     * コンストラクタ
     * @param orderNo 発注番号
     */
    public FreeProductInfo(String orderNo) {
        this.placeOrderNo = orderNo;
    }
    // endregion

    // region [ implements ]
    protected FreeProductInfo(@NotNull Parcel in) {
        placeOrderNo = in.readString();
        plantCode = in.readString();
        plantName = in.readString();
        itemCode = in.readString();
        itemName = in.readString();
        quantity = in.readDouble();
        unit = in.readString();
    }

    public static final Creator<FreeProductInfo> CREATOR = new Creator<FreeProductInfo>() {
        @Override
        public FreeProductInfo createFromParcel(Parcel in) {
            return new FreeProductInfo(in);
        }
        @Override
        public FreeProductInfo[] newArray(int size) {
            return new FreeProductInfo[size];
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
        dest.writeString(plantCode);
        dest.writeString(plantName);
        dest.writeString(itemCode);
        dest.writeString(itemName);
        dest.writeDouble(quantity);
        dest.writeString(unit);
    }
    // endregion

    /* 発注番号 */
    public String getPlaceOrderNo() { return this.placeOrderNo; }
    public void setPlaceOrderNo(String orderNo) { this.placeOrderNo = orderNo; }
    /* プラントコード */
    public String getPlantCode() { return plantCode; }
    public void setPlantCode(String plantCode) { this.plantCode = plantCode; }
    /* プラント名 */
    public String getPlantName() {
        return plantName;
    }
    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }
    /* 品目コード */
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    /* 品目名 */
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    /* 発注数量 */
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    /* 単位 */
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

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
}
