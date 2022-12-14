package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * アウター情報受信用　Class
 */
public class OuterInfo implements Parcelable {

    // region [ implements ]
    public OuterInfo( Parcel in) {
        invoiceNo = in.readString();
        csNumber = in.readInt();
        hyokiCsNumber = in.readString();
        outerSagyo1 = in.readString();
        outerSagyo1Siyo = in.readDouble();
        outerSagyo2 = in.readString();
        outerSagyo2Siyo = in.readDouble();
        outerSagyo3 = in.readString();
        outerSagyo3Siyo = in.readDouble();
        outerSagyo4 = in.readString();
        outerSagyo4Siyo = in.readDouble();
        blueIceSiyo = in.readDouble();
        dryIceSiyo = in.readDouble();
        labelSu = in.readInt();
        konpoSu = in.readInt();
        outerLength = in.readDouble();
        outerWidth = in.readDouble();
        outerHeight = in.readDouble();
        netWeight = in.readDouble();
        grossWeight = in.readDouble();
        saisyuKonpoNisugata = in.readString();
        biko = in.readString();
        palettUchiwake = in.readString();
        cartonSu = in.readInt();
        nifudaSu = in.readInt();
    }

    public OuterInfo(String invoiceNo) {
        invoiceNo = invoiceNo;
    }
    public static final Creator<OuterInfo> CREATOR = new Creator<OuterInfo>() {
        @Override
        public OuterInfo createFromParcel(Parcel in) {
            return new OuterInfo(in);
        }

        @Override
        public OuterInfo[] newArray(int size) {
            return new OuterInfo[size];
        }
    };
    // endregion

    // region [ Override ]
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags) {
        dest.writeString(invoiceNo);
        dest.writeInt(csNumber);
        dest.writeString(hyokiCsNumber);
        dest.writeString(outerSagyo1);
        dest.writeDouble(outerSagyo1Siyo);
        dest.writeString(outerSagyo2);
        dest.writeDouble(outerSagyo2Siyo);
        dest.writeString(outerSagyo3);
        dest.writeDouble(outerSagyo3Siyo);
        dest.writeString(outerSagyo4);
        dest.writeDouble(outerSagyo4Siyo);
        dest.writeDouble(blueIceSiyo);
        dest.writeDouble(dryIceSiyo);
        dest.writeInt(labelSu);
        dest.writeInt(konpoSu);
        dest.writeDouble(outerLength);
        dest.writeDouble(outerWidth);
        dest.writeDouble(outerHeight);
        dest.writeDouble(netWeight);
        dest.writeDouble(grossWeight);
        dest.writeString(saisyuKonpoNisugata);
        dest.writeString(biko);
        dest.writeString(palettUchiwake);
        dest.writeInt(cartonSu);
        dest.writeInt(nifudaSu);
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
    // ケースマーク番号
    @SerializedName("csNumber")
    private Integer csNumber;
    public Integer getCsNumber() {
        return csNumber;
    }
    public void setCsNumber(Integer csNumber) {
        this.csNumber = csNumber;
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
    // アウター作業内容1
    @SerializedName("outerSagyo1")
    private String outerSagyo1;
    public String getOuterSagyo1() {
        return outerSagyo1;
    }
    public void setOuterSagyo1(String outerSagyo1) {
        this.outerSagyo1 = outerSagyo1;
    }
    // アウター作業内容1使用料
    @SerializedName("outerSagyo1Siyo")
    private Double outerSagyo1Siyo;
    public Double getOuterSagyo1Siyo() {
        return outerSagyo1Siyo;
    }
    public void setOuterSagyo1Siyo(Double outerSagyo1Siyo) {
        this.outerSagyo1Siyo = outerSagyo1Siyo;
    }
    // アウター作業内容2
    @SerializedName("outerSagyo2")
    private String outerSagyo2;
    public String getOuterSagyo2() {
        return outerSagyo2;
    }
    public void setOuterSagyo2(String outerSagyo2) {
        this.outerSagyo2 = outerSagyo2;
    }
    // アウター作業内容2使用料
    @SerializedName("outerSagyo2Siyo")
    private Double outerSagyo2Siyo;
    public Double getOuterSagyo2Siyo() {
        return outerSagyo2Siyo;
    }
    public void setOuterSagyo2Siyo(Double outerSagyo2Siyo) {
        this.outerSagyo2Siyo = outerSagyo2Siyo;
    }
    // アウター作業内容3
    @SerializedName("outerSagyo3")
    private String outerSagyo3;
    public String getOuterSagyo3() {
        return outerSagyo3;
    }
    public void setOuterSagyo3(String outerSagyo3) {
        this.outerSagyo3 = outerSagyo3;
    }
    // アウター作業内容3使用料
    @SerializedName("outerSagyo3Siyo")
    private Double outerSagyo3Siyo;
    public Double getOuterSagyo3Siyo() {
        return outerSagyo3Siyo;
    }
    public void setOuterSagyo3Siyo(Double outerSagyo3Siyo) {
        this.outerSagyo3Siyo = outerSagyo3Siyo;
    }
    // アウター作業内容4
    @SerializedName("outerSagyo4")
    private String outerSagyo4;
    public String getOuterSagyo4() {
        return outerSagyo4;
    }
    public void setOuterSagyo4(String outerSagyo4) {
        this.outerSagyo4 = outerSagyo4;
    }
    // アウター作業内容4使用料
    @SerializedName("outerSagyo4Siyo")
    private Double outerSagyo4Siyo;
    public Double getOuterSagyo4Siyo() {
        return outerSagyo4Siyo;
    }
    public void setOuterSagyo4Siyo(Double outerSagyo4Siyo) {
        this.outerSagyo4Siyo = outerSagyo4Siyo;
    }
    // ブル―アイス使用料
    @SerializedName("blueIceSiyo")
    private Double blueIceSiyo;
    public Double getBlueIceSiyo() {
        return blueIceSiyo;
    }
    public void setBlueIceSiyo(Double blueIceSiyo) {
        this.blueIceSiyo = blueIceSiyo;
    }
    // ドライアイス使用量
    @SerializedName("dryIceSiyo")
    private Double dryIceSiyo;
    public Double getDryIceSiyo() {
        return dryIceSiyo;
    }
    public void setDryIceSiyo(Double dryIceSiyo) {
        this.dryIceSiyo = dryIceSiyo;
    }
    // ラベル数
    @SerializedName("labelSu")
    private Integer labelSu;
    public Integer getLabelSu() {
        return labelSu;
    }
    public void setLabelSu(Integer labelSu) {
        this.labelSu = labelSu;
    }
    // 梱包数
    @SerializedName("konpoSu")
    private Integer konpoSu;
    public Integer getKonpoSu() {
        return konpoSu;
    }
    public void setKonpoSu(Integer konpoSu) {
        this.konpoSu = konpoSu;
    }
    // アウター長さ(L)
    @SerializedName("outerLength")
    private Double outerLength;
    public Double getOuterLength() {
        return outerLength;
    }
    public void setOuterLength(Double outerLength) {
        this.outerLength = outerLength;
    }
    // アウター幅(W)
    @SerializedName("outerWidth")
    private Double outerWidth;
    public Double getOuterWidth() {
        return outerWidth;
    }
    public void setOuterWidth(Double outerWidth) {
        this.outerWidth = outerWidth;
    }
    // アウター高さ(H)
    @SerializedName("outerHeight")
    private Double outerHeight;
    public Double getOuterHeight() {
        return outerHeight;
    }
    public void setOuterHeight(Double outerHeight) {
        this.outerHeight = outerHeight;
    }
    // NW(net weight)
    @SerializedName("netWeight")
    private Double netWeight;
    public Double getNetWeight() {
        return netWeight;
    }
    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }
    // GW(gross weight)
    @SerializedName("grossWeight")
    private Double grossWeight;
    public Double getGrossWeight() {
        return grossWeight;
    }
    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }
    // 最終梱包荷姿
    @SerializedName("saisyuKonpoNisugata")
    private String saisyuKonpoNisugata;
    public String getSaisyuKonpoNisugata() {
        return saisyuKonpoNisugata;
    }
    public void setSaisyuKonpoNisugata(String saisyuKonpoNisugata) {
        this.saisyuKonpoNisugata = saisyuKonpoNisugata;
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
    // パレット内訳
    @SerializedName("palettUchiwake")
    private String palettUchiwake;
    public String getPalettUchiwake() {
        return palettUchiwake;
    }
    public void setPalettUchiwake(String palettUchiwake) {
        this.palettUchiwake = palettUchiwake;
    }
    // 箱数
    @SerializedName("cartonSu")
    private Integer cartonSu;
    public Integer getCartonSu() {
        return cartonSu;
    }
    public void setCartonSu(Integer cartonSu) {
        this.cartonSu = cartonSu;
    }
    // 荷札数
    @SerializedName("nifudaSu")
    private Integer nifudaSu;
    public Integer getNifudaSu() {
        return nifudaSu;
    }
    public void setNifudaSu(Integer nifudaSu) {
        this.nifudaSu = nifudaSu;
    }
}
