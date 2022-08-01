package jp.co.khwayz.eleEntExtManage.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * ケースマーク情報受信用 Class
 */
public class CaseMarkPasteTagInfo implements Parcelable {

    // region [ implements ]
    public CaseMarkPasteTagInfo(@NotNull Parcel in) {
        invoiceNo = in.readString();
        csNumber = in.readInt();
        hachuNo = in.readString();
        hachuEda = in.readInt();
        jyuchuNo = in.readString();
        itemCode = in.readString();
        itemName = in.readString();
        zaikoSuryo = in.readDouble();
        syukkaSuryo = in.readDouble();
        syukkaTani = in.readString();
        konpoKeitai = in.readString();
    }

    public CaseMarkPasteTagInfo(String invoiceNo) {
        invoiceNo = invoiceNo;
    }

    public static final Creator<CaseMarkPasteTagInfo> CREATOR = new Creator<CaseMarkPasteTagInfo>() {
        @Override
        public CaseMarkPasteTagInfo createFromParcel(Parcel in) {
            return new CaseMarkPasteTagInfo(in);
        }

        @Override
        public CaseMarkPasteTagInfo[] newArray(int size) {
            return new CaseMarkPasteTagInfo[size];
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
        dest.writeInt(csNumber);
        dest.writeString(hachuNo);
        dest.writeInt(hachuEda);
        dest.writeString(jyuchuNo);
        dest.writeString(itemCode);
        dest.writeString(itemName);
        dest.writeDouble(zaikoSuryo);
        dest.writeDouble(syukkaSuryo);
        dest.writeString(syukkaTani);
        dest.writeString(konpoKeitai);
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
    public Integer getCsNumber() { return csNumber; }
    public void setCsNumber(Integer csNumber) {
        this.csNumber = csNumber;
    }
    // 発注番号
    @SerializedName("hachuNo")
    private String hachuNo;
    public String getHachuNo() {
        return hachuNo;
    }
    public void setHachuNo(String hachuNo) {
        this.hachuNo = hachuNo;
    }
    // 枝番
    @SerializedName("hachuEda")
    private int hachuEda;
    public int getHachuEda() {
        return hachuEda;
    }
    public void setHachuEda(int hachuEda) {
        this.hachuEda = hachuEda;
    }
    // 受注番号
    @SerializedName("jyuchuNo")
    private String jyuchuNo;
    public String getJyuchuNo() {
        return jyuchuNo;
    }
    public void setJyuchuNo(String jyuchuNo) {
        this.jyuchuNo = jyuchuNo;
    }
    // 品目コード
    @SerializedName("itemCode")
    private String itemCode;
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    // 品目名
    @SerializedName("itemName")
    private String itemName;
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    // 在庫数量
    @SerializedName("zaikoSuryo")
    private double zaikoSuryo;
    public double getZaikoSuryo() {
        return zaikoSuryo;
    }
    public void setZaikoSuryo(double zaikoSuryo) {
        this.zaikoSuryo = zaikoSuryo;
    }
    // 出荷数量
    @SerializedName("syukkaSuryo")
    private double syukkaSuryo;
    public double getSyukkaSuryo() {
        return syukkaSuryo;
    }
    public void setSyukkaSuryo(double syukkaSuryo) {
        this.syukkaSuryo = syukkaSuryo;
    }
    // 出荷単位
    @SerializedName("syukkaTani")
    private String syukkaTani;
    public String getSyukkaTani() {
        return syukkaTani;
    }
    public void setSyukkaTani(String syukkaTani) {
        this.syukkaTani = syukkaTani;
    }
    // 梱包携帯
    @SerializedName("konpoKeitai")
    private String konpoKeitai;
    public String getKonpoKeitai() {
        return konpoKeitai;
    }
    public void setKonpoKeitai(String konpoKeitai) {
        this.konpoKeitai = konpoKeitai;
    }
}

