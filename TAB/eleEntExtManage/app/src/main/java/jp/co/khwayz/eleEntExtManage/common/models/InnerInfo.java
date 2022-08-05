package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * インナー情報受信用　Classe
 */
public class InnerInfo implements Parcelable {

    // region [ implements ]
    public InnerInfo(@NotNull Parcel in) {
        invoiceNo = in.readString();
        renban = in.readInt();
        lineNo = in.readInt();
        innerSagyo1 = in.readString();
        innerSagyo1Siyo = in.readDouble();
        innerSagyo2 = in.readString();
        innerSagyo2Siyo = in.readDouble();
        innerSagyo3 = in.readString();
        innerSagyo3Siyo = in.readDouble();
        innerSagyo4 = in.readString();
        innerSagyo4Siyo = in.readDouble();
        labelSu = in.readInt();
        netWeight = in.readDouble();
        danNaiyoRyo = in.readDouble();
        danTani = in.readString();
        danNaisoYoki = in.readString();
        danHonsu = in.readInt();
        danGaisoYoki = in.readString();
        danGaisoKosu = in.readInt();
        biko = in.readString();
    }

    public InnerInfo(String invoiceNo) {
        invoiceNo = invoiceNo;
    }
    public static final Creator<InnerInfo> CREATOR = new Creator<InnerInfo>() {
        @Override
        public InnerInfo createFromParcel(Parcel in) {
            return new InnerInfo(in);
        }

        @Override
        public InnerInfo[] newArray(int size) {
            return new InnerInfo[size];
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
        dest.writeString(innerSagyo1);
        dest.writeDouble(innerSagyo1Siyo);
        dest.writeString(innerSagyo2);
        dest.writeDouble(innerSagyo2Siyo);
        dest.writeString(innerSagyo3);
        dest.writeDouble(innerSagyo3Siyo);
        dest.writeString(innerSagyo4);
        dest.writeDouble(innerSagyo4Siyo);
        dest.writeInt(labelSu);
        dest.writeDouble(netWeight);
        dest.writeDouble(danNaiyoRyo);
        dest.writeString(danTani);
        dest.writeString(danNaisoYoki);
        dest.writeInt(danHonsu);
        dest.writeString(danGaisoYoki);
        dest.writeInt(danGaisoKosu);
        dest.writeString(biko);
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
    public Integer getRenban() {
        return renban;
    }
    public void setRenban(Integer renban) {
        this.renban = renban;
    }
    // 行番号
    @SerializedName("lineNo")
    private Integer lineNo;
    public Integer getLineNo() {
        return lineNo;
    }
    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }
    // インナー作業内容１
    @SerializedName("innerSagyo1")
    private String innerSagyo1;
    public String getInnerSagyo1() {
        return innerSagyo1;
    }
    public void setInnerSagyo1(String innerSagyo1) {
        this.innerSagyo1 = innerSagyo1;
    }
    // インナー作業内容１使用料
    @SerializedName("innerSagyo1Siyo")
    private Double innerSagyo1Siyo;
    public Double getInnerSagyo1Siyo() {
        return innerSagyo1Siyo;
    }
    public void setInnerSagyo1Siyo(Double innerSagyo1Siyo) {
        this.innerSagyo1Siyo = innerSagyo1Siyo;
    }
    // インナー作業内容２
    @SerializedName("innerSagyo2")
    private String innerSagyo2;
    public String getInnerSagyo2() {
        return innerSagyo2;
    }
    public void setInnerSagyo2(String innerSagyo2) {
        this.innerSagyo2 = innerSagyo2;
    }
    // インナー作業内容２使用料
    @SerializedName("innerSagyo2Siyo")
    private Double innerSagyo2Siyo;
    public Double getInnerSagyo2Siyo() {
        return innerSagyo2Siyo;
    }
    public void setInnerSagyo2Siyo(Double innerSagyo2Siyo) {
        this.innerSagyo2Siyo = innerSagyo2Siyo;
    }
    // インナー作業内容3
    @SerializedName("innerSagyo3")
    private String innerSagyo3;
    public String getInnerSagyo3() {
        return innerSagyo3;
    }
    public void setInnerSagyo3(String innerSagyo3) {
        this.innerSagyo3 = innerSagyo3;
    }
    // インナー作業内容3使用料
    @SerializedName("innerSagyo3Siyo")
    private Double innerSagyo3Siyo;
    public Double getInnerSagyo3Siyo() {
        return innerSagyo3Siyo;
    }
    public void setInnerSagyo3Siyo(Double innerSagyo3Siyo) {
        this.innerSagyo3Siyo = innerSagyo3Siyo;
    }
    // インナー作業内容3
    @SerializedName("innerSagyo4")
    private String innerSagyo4;
    public String getInnerSagyo4() {
        return innerSagyo4;
    }
    public void setInnerSagyo4(String innerSagyo4) {
        this.innerSagyo4 = innerSagyo4;
    }
    // インナー作業内容4使用料
    @SerializedName("innerSagyo4Siyo")
    private Double innerSagyo4Siyo;
    public Double getInnerSagyo4Siyo() {
        return innerSagyo4Siyo;
    }
    public void setInnerSagyo4Siyo(Double innerSagyo4Siyo) {
        this.innerSagyo4Siyo = innerSagyo4Siyo;
    }
    // ラベル枚数
    @SerializedName("labelSu")
    private Integer labelSu;
    public Integer getLabelSu() {
        return labelSu;
    }
    public void setLabelSu(Integer labelSu) {
        this.labelSu = labelSu;
    }
    // NW
    @SerializedName("netWeight")
    private Double netWeight;
    public Double getNetWeight() {
        return netWeight;
    }
    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }
    // 危険品内容量
    @SerializedName("danNaiyoRyo")
    private Double danNaiyoRyo;
    public Double getDanNaiyoRyo() {
        return danNaiyoRyo;
    }
    public void setDanNaiyoRyo(Double danNaiyoRyo) {
        this.danNaiyoRyo = danNaiyoRyo;
    }
    // 危険品単位
    @SerializedName("danTani")
    private String danTani;
    public String getDanTani() {
        return danTani;
    }
    public void setDanTani(String danTani) {
        this.danTani = danTani;
    }
    // 危険品内装容器
    @SerializedName("danNaisoYoki")
    private String danNaisoYoki;
    public String getDanNaisoYoki() {
        return danNaisoYoki;
    }
    public void setDanNaisoYoki(String danNaisoYoki) {
        this.danNaisoYoki = danNaisoYoki;
    }
    // 危険品本数
    @SerializedName("danHonsu")
    private Integer danHonsu;
    public Integer getDanHonsu() {
        return danHonsu;
    }
    public void setDanHonsu(Integer danHonsu) {
        this.danHonsu = danHonsu;
    }
    // 危険品外装容器
    @SerializedName("danGaisoYoki")
    private String danGaisoYoki;
    public String getDanGaisoYoki() {
        return danGaisoYoki;
    }
    public void setDanGaisoYoki(String danGaisoYoki) {
        this.danGaisoYoki = danGaisoYoki;
    }
    // 危険品外装容器個数
    @SerializedName("danGaisoKosu")
    private Integer danGaisoKosu;
    public Integer getDanGaisoKosu() {
        return danGaisoKosu;
    }
    public void setDanGaisoKosu(Integer danGaisoKosu) {
        this.danGaisoKosu = danGaisoKosu;
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
