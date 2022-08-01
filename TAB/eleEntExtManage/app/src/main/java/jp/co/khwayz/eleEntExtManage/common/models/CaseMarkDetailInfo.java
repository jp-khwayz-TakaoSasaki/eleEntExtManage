package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * ケースマーク詳細取得用 Class
 */
public class CaseMarkDetailInfo implements Parcelable {

    // region [ implements ]
    public CaseMarkDetailInfo(@NotNull Parcel in) {
        CASEMARK1 = in.readString();
        CASEMARK2 = in.readString();
        CASEMARK3 = in.readString();
        CASEMARK4 = in.readString();
        CASEMARK5 = in.readString();
        CASEMARK6 = in.readString();
        CASEMARK7 = in.readString();
        CASEMARK8 = in.readString();
        CASEMARK9 = in.readString();
        CASEMARK10 = in.readString();
        CASEMARK11 = in.readString();
        CASEMARK12 = in.readString();
        konpoSu = in.readInt();
        hyokiCsNumber = in.readString();
        outerLength = in.readDouble();
        outerWidth = in.readDouble();
        outerHeight = in.readDouble();
        netWeight = in.readDouble();
        grossWeight = in.readDouble();
        biko = in.readString();
    }

    public CaseMarkDetailInfo(String caseMark1, String caseMark2, String caseMark3, String caseMark4, String caseMark5, String caseMark6
            ,String caseMark7, String caseMark8, String caseMark9, String caseMark10, String caseMark11, String caseMark12
            , int konpoSu, String hyokiCsNumber, double outerLength, double outerWidth, double outerHeight, double netWeight
            , double grossWeight, String biko) {
        this.CASEMARK1 = caseMark1;
        this.CASEMARK2 = caseMark2;
        this.CASEMARK3 = caseMark3;
        this.CASEMARK4 = caseMark4;
        this.CASEMARK5 = caseMark5;
        this.CASEMARK6 = caseMark6;
        this.CASEMARK7 = caseMark7;
        this.CASEMARK8 = caseMark8;
        this.CASEMARK9 = caseMark9;
        this.CASEMARK10 = caseMark10;
        this.CASEMARK11 = caseMark11;
        this.CASEMARK12 = caseMark12;
        this.konpoSu = konpoSu;
        this.hyokiCsNumber = hyokiCsNumber;
        this.outerLength = outerLength;
        this.outerWidth = outerWidth;
        this.outerHeight = outerHeight;
        this.netWeight = netWeight;
        this.grossWeight = grossWeight;
        this.biko = biko;
    }

    public static final Creator<CaseMarkDetailInfo> CREATOR = new Creator<CaseMarkDetailInfo>() {
        @Override
        public CaseMarkDetailInfo createFromParcel(Parcel in) {
            return new CaseMarkDetailInfo(in);
        }

        @Override
        public CaseMarkDetailInfo[] newArray(int size) {
            return new CaseMarkDetailInfo[size];
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
        dest.writeString(CASEMARK1);
        dest.writeString(CASEMARK2);
        dest.writeString(CASEMARK3);
        dest.writeString(CASEMARK4);
        dest.writeString(CASEMARK5);
        dest.writeString(CASEMARK6);
        dest.writeString(CASEMARK7);
        dest.writeString(CASEMARK8);
        dest.writeString(CASEMARK9);
        dest.writeString(CASEMARK10);
        dest.writeString(CASEMARK11);
        dest.writeString(CASEMARK12);
        dest.writeInt(konpoSu);
        dest.writeString(hyokiCsNumber);
        dest.writeDouble(outerLength);
        dest.writeDouble(outerWidth);
        dest.writeDouble(outerHeight);
        dest.writeDouble(netWeight);
        dest.writeDouble(grossWeight);
        dest.writeString(biko);
    }
    // endregion

    // ケースマーク1行目
    @SerializedName("CASEMARK1")
    private String CASEMARK1;
    public String getCASEMARK1() {
        return CASEMARK1;
    }
    public void setCASEMARK1(String CASEMARK1) {
        this.CASEMARK1 = CASEMARK1;
    }
    // ケースマーク2行目
    @SerializedName("CASEMARK2")
    private String CASEMARK2;
    public String getCASEMARK2() {
        return CASEMARK2;
    }
    public void setCASEMARK2(String CASEMARK2) {
        this.CASEMARK2 = CASEMARK2;
    }
    // ケースマーク3行目
    @SerializedName("CASEMARK3")
    private String CASEMARK3;
    public String getCASEMARK3() {
        return CASEMARK3;
    }
    public void setCASEMARK3(String CASEMARK3) {
        this.CASEMARK3 = CASEMARK3;
    }
    // ケースマーク4行目
    @SerializedName("CASEMARK4")
    private String CASEMARK4;
    public String getCASEMARK4() {
        return CASEMARK4;
    }
    public void setCASEMARK4(String CASEMARK4) {
        this.CASEMARK4 = CASEMARK4;
    }
    // ケースマーク5行目
    @SerializedName("CASEMARK5")
    private String CASEMARK5;
    public String getCASEMARK5() {
        return CASEMARK5;
    }
    public void setCASEMARK5(String CASEMARK5) {
        this.CASEMARK5 = CASEMARK5;
    }
    // ケースマーク6行目
    @SerializedName("CASEMARK6")
    private String CASEMARK6;
    public String getCASEMARK6() {
        return CASEMARK6;
    }
    public void setCASEMARK6(String CASEMARK6) {
        this.CASEMARK6 = CASEMARK6;
    }
    // ケースマーク7行目
    @SerializedName("CASEMARK7")
    private String CASEMARK7;
    public String getCASEMARK7() {
        return CASEMARK7;
    }
    public void setCASEMARK7(String CASEMARK7) { this.CASEMARK7 = CASEMARK7; }
    // ケースマーク8行目
    @SerializedName("CASEMARK8")
    private String CASEMARK8;
    public String getCASEMARK8() {
        return CASEMARK8;
    }
    public void setCASEMARK8(String CASEMARK8) {
        this.CASEMARK8 = CASEMARK8;
    }
    // ケースマーク9行目
    @SerializedName("CASEMARK9")
    private String CASEMARK9;
    public String getCASEMARK9() {
        return CASEMARK9;
    }
    public void setCASEMARK9(String CASEMARK9) {
        this.CASEMARK9 = CASEMARK9;
    }
    // ケースマーク10行目
    @SerializedName("CASEMARK10")
    private String CASEMARK10;
    public String getCASEMARK10() {
        return CASEMARK10;
    }
    public void setCASEMARK10(String CASEMARK10) {
        this.CASEMARK10 = CASEMARK10;
    }
    // ケースマーク11行目
    @SerializedName("CASEMARK11")
    private String CASEMARK11;
    public String getCASEMARK11() {
        return CASEMARK11;
    }
    public void setCASEMARK11(String CASEMARK11) {
        this.CASEMARK11 = CASEMARK11;
    }
    // ケースマーク12行目
    @SerializedName("CASEMARK12")
    private String CASEMARK12;
    public String getCASEMARK12() {
        return CASEMARK12;
    }
    public void setCASEMARK12(String CASEMARK12) {
        this.CASEMARK12 = CASEMARK12;
    }
    // 梱包数
    @SerializedName("konpoSu")
    private int konpoSu;
    public int getKonpoSu() { return konpoSu; }
    public void setKonpoSu(int konpoSu) {
        this.konpoSu = konpoSu;
    }
    // 表記用ケースマーク番号
    @SerializedName("hyokiCsNumber")
    private String hyokiCsNumber;
    public String getHyokiCsNumber() {
        return hyokiCsNumber;
    }
    public void setHyokiCsNumber(String hyokiCsNumber) {
        this.hyokiCsNumber = hyokiCsNumber;
    }
    // アウター長さ
    @SerializedName("outerLength")
    private double outerLength;
    public double getOuterLength() {
        return outerLength;
    }
    public void setOuterLength(double outerLength) {
        this.outerLength = outerLength;
    }
    // アウター幅
    @SerializedName("outerWidth")
    private double outerWidth;
    public double getOuterWidth() {
        return outerWidth;
    }
    public void setOuterWidth(double outerWidth) {
        this.outerWidth = outerWidth;
    }
    // アウター高さ
    @SerializedName("outerHeight")
    private double outerHeight;
    public double getOuterHeight() {
        return outerHeight;
    }
    public void setOuterHeight(double outerHeight) {
        this.outerHeight = outerHeight;
    }
    // NW
    @SerializedName("netWeight")
    private double netWeight;
    public double getNetWeight() {
        return netWeight;
    }
    public void setNetWeight(double netWeight) {
        this.netWeight = netWeight;
    }
    // GW
    @SerializedName("grossWeight")
    private double grossWeight;
    public double getGrossWeight() {
        return grossWeight;
    }
    public void setGrossWeight(double grossWeight) {
        this.grossWeight = grossWeight;
    }
    // 備考
    @SerializedName("biko")
    private String biko;
    public String getBiko() {
        return biko;
    }
    public void setBiko(String biko) {
        this.biko = biko;
    }
}

