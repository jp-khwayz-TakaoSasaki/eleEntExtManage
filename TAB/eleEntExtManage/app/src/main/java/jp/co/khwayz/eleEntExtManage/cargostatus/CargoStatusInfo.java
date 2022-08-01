package jp.co.khwayz.eleEntExtManage.cargostatus;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoStatusInfo implements Parcelable {
    // コンストラクタ
    public CargoStatusInfo() { }

    /** 発注番号 */
    @SerializedName("hachuNo")
    private String placeOrderNo;
    /** 枝番 */
    @SerializedName("hachuEda")
    private int branchNo;
    /** 受注番号 */
    @SerializedName("juchuNo")
    private String receiveOrderNo;
    /** 営業所名 */
    @SerializedName("eigyoName")
    private String plantName;
    /** 得意先名 */
    @SerializedName("tokuisakiName")
    private String customerName;
    /** 品目コード */
    @SerializedName("hinmokuCode")
    private String itemCode;
    /** 品目名 */
    @SerializedName("hinmokuName")
    private String itemName;
    /** 販売単位 */
    @SerializedName("hanbaiTani")
    private String salesUnit;
    /** 発注単位 */
    @SerializedName("hachuTani")
    private String placeOrderUnit;
    /** 基本単位 */
    @SerializedName("kihonSuTani")
    private String baseUnit;
    /** 危険物コード */
    @SerializedName("kikenCode")
    private String hazmatCode;
    /** 棚番 */
    @SerializedName("tanaban")
    private String shelfNo;
    /** 営業担当者名 */
    @SerializedName("eigyoTantouName")
    private String salesStaffName;
    /** 仕入れ先品目コード */
    @SerializedName("siiresakiHinmokuCode")
    private String supplierItemCode;
    /** 入庫日 */
    @SerializedName("nyukoDate")
    private String receiptDate;
    /** ロケーション */
    @SerializedName("nLocation")
    private String locationNo;
    /** 出庫ステータス */
    @SerializedName("shyukkoStatus")
    private String issueStatus;
    /** Invoice番号 */
    @SerializedName("invoiceNo")
    private String invoiceNo;
    /** C/No */
    @SerializedName("csNumber")
    private String csNo;

    /** 蔵置 */
    private String storage;
    /** ピッキング */
    private String picking;
    /** パッキング */
    private String packing;
    /** ケースマーク */
    private String caseMark;
    /** 出庫 */
    private String issue;

    public String getTagNo() {
        String result = "";
        if (!TextUtils.isEmpty(placeOrderNo)) {
            result = placeOrderNo;
        }
        if (0 < branchNo) {
            result += "-" + String.format(Locale.JAPAN, "%03d", branchNo);;
        }
        return result;
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

    @Override
    public int describeContents() {
        return 0;
    }
    protected CargoStatusInfo(Parcel in) {
        placeOrderNo = in.readString();
        branchNo = in.readInt();
        receiveOrderNo = in.readString();
        plantName = in.readString();
        customerName = in.readString();
        itemCode = in.readString();
        itemName = in.readString();
        salesUnit = in.readString();
        placeOrderUnit = in.readString();
        baseUnit = in.readString();
        hazmatCode = in.readString();
        shelfNo = in.readString();
        salesStaffName = in.readString();
        supplierItemCode = in.readString();
        receiptDate = in.readString();
        locationNo = in.readString();
        issueStatus = in.readString();
        invoiceNo = in.readString();
        csNo = in.readString();
        storage = in.readString();
        picking = in.readString();
        packing = in.readString();
        caseMark = in.readString();
        issue = in.readString();
    }

    public static final Creator<CargoStatusInfo> CREATOR = new Creator<CargoStatusInfo>() {
        @Override
        public CargoStatusInfo createFromParcel(Parcel in) {
            return new CargoStatusInfo(in);
        }

        @Override
        public CargoStatusInfo[] newArray(int size) {
            return new CargoStatusInfo[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeOrderNo);
        dest.writeInt(branchNo);
        dest.writeString(receiveOrderNo);
        dest.writeString(plantName);
        dest.writeString(customerName);
        dest.writeString(itemCode);
        dest.writeString(itemName);
        dest.writeString(salesUnit);
        dest.writeString(placeOrderUnit);
        dest.writeString(baseUnit);
        dest.writeString(hazmatCode);
        dest.writeString(shelfNo);
        dest.writeString(salesStaffName);
        dest.writeString(supplierItemCode);
        dest.writeString(receiptDate);
        dest.writeString(locationNo);
        dest.writeString(issueStatus);
        dest.writeString(invoiceNo);
        dest.writeString(csNo);
        dest.writeString(storage);
        dest.writeString(picking);
        dest.writeString(packing);
        dest.writeString(caseMark);
        dest.writeString(issue);
    }
}
