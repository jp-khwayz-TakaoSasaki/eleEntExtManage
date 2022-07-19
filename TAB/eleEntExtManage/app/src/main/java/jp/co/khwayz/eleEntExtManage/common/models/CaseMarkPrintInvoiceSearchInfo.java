package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * ケースマーク情報受信用 Class
 */
public class CaseMarkPrintInvoiceSearchInfo implements Parcelable {

    // region [ implements ]
    public CaseMarkPrintInvoiceSearchInfo(@NotNull Parcel in) {
        invoiceNo = in.readString();
        syukkaDate = in.readString();
        shimukeChi = in.readString();
        csNumber = in.readInt();
        hyoukiCsNumber = in.readString();
        csPrintFlg = in.readString();
        yusoMode = in.readString();
        casemark1 = in.readString();
        casemark2 = in.readString();
        casemark3 = in.readString();
        casemark4 = in.readString();
        casemark5 = in.readString();
        casemark6 = in.readString();
        casemark7 = in.readString();
        casemark8 = in.readString();
        casemark9 = in.readString();
        casemark10 = in.readString();
        casemark11 = in.readString();
        casemark12 = in.readString();
    }

    public CaseMarkPrintInvoiceSearchInfo(String invoiceNo) {
        invoiceNo = invoiceNo;
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
        dest.writeString(syukkaDate);
        dest.writeString(shimukeChi);
        dest.writeInt(csNumber);
        dest.writeString(hyoukiCsNumber);
        dest.writeString(csPrintFlg);
        dest.writeString(yusoMode);
        dest.writeString(casemark1);
        dest.writeString(casemark2);
        dest.writeString(casemark3);
        dest.writeString(casemark4);
        dest.writeString(casemark5);
        dest.writeString(casemark6);
        dest.writeString(casemark7);
        dest.writeString(casemark8);
        dest.writeString(casemark9);
        dest.writeString(casemark10);
        dest.writeString(casemark11);
        dest.writeString(casemark12);
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
    private String syukkaDate;
    public String getSyukkaDate() { return syukkaDate; }
    public void setSyukkaDate(String syukkaDate) {
        this.syukkaDate = syukkaDate;
    }
    // 仕向地
    @SerializedName("shimukeChi")
    private String shimukeChi;
    public String getShimukeChi() { return shimukeChi; }
    public void setShimukeChi(String shimukeChi) {
        this.shimukeChi = shimukeChi;
    }
    // ケースマーク番号
    @SerializedName("csNumber")
    private Integer csNumber;
    public Integer getCsNumber() { return csNumber; }
    public void setCsNumber(Integer csNumber) {
        this.csNumber = csNumber;
    }
    // 表記用ケースマーク番号
    @SerializedName("hyoukiCsNumber")
    private String hyoukiCsNumber;
    public String getHyoukiCsNumber() {
        return hyoukiCsNumber;
    }
    public void setHyoukiCsNumber(String hyoukiCsNumber) {
        this.hyoukiCsNumber = hyoukiCsNumber;
    }
    // ケースマーク印刷済フラグ
    @SerializedName("csPrintFlg")
    private String csPrintFlg;
    public String getCsPrintFlg() {
        return csPrintFlg;
    }
    public void setCsPrintFlg(String csPrintFlg) {
        this.csPrintFlg = csPrintFlg;
    }
    // 輸送モード
    @SerializedName("yusoMode")
    private String yusoMode;
    public String getYusoMode() {
        return yusoMode;
    }
    public void setYusoMode(String yusoMode) {
        this.yusoMode = yusoMode;
    }
    // ケースマーク1行目
    @SerializedName("casemark1")
    private String casemark1;
    public String getCasemark1() {
        return casemark1;
    }
    public void setCasemark1(String casemark1) {
        this.casemark1 = casemark1;
    }
    // ケースマーク2行目
    @SerializedName("casemark2")
    private String casemark2;
    public String getCasemark2() {
        return casemark2;
    }
    public void setCasemark2(String casemark2) {
        this.casemark2 = casemark2;
    }
    // ケースマーク3行目
    @SerializedName("casemark3")
    private String casemark3;
    public String getCasemark3() {
        return casemark3;
    }
    public void setCasemark3(String casemark3) {
        this.casemark3 = casemark3;
    }
    // ケースマーク4行目
    @SerializedName("casemark4")
    private String casemark4;
    public String getCasemark4() {
        return casemark4;
    }
    public void setCasemark4(String casemark4) {
        this.casemark4 = casemark4;
    }
    // ケースマーク5行目
    @SerializedName("casemark5")
    private String casemark5;
    public String getCasemark5() {
        return casemark5;
    }
    public void setCasemark5(String casemark5) {
        this.casemark5 = casemark5;
    }
    // ケースマーク6行目
    @SerializedName("casemark6")
    private String casemark6;
    public String getCasemark6() {
        return casemark6;
    }
    public void setCasemark6(String casemark6) {
        this.casemark6 = casemark6;
    }
    // ケースマーク7行目
    @SerializedName("casemark7")
    private String casemark7;
    public String getCasemark7() {
        return casemark7;
    }
    public void setCasemark7(String casemark7) { this.casemark7 = casemark7; }
    // ケースマーク8行目
    @SerializedName("casemark8")
    private String casemark8;
    public String getCasemark8() {
        return casemark8;
    }
    public void setCasemark8(String casemark8) {
        this.casemark8 = casemark8;
    }
    // ケースマーク9行目
    @SerializedName("casemark9")
    private String casemark9;
    public String getCasemark9() {
        return casemark9;
    }
    public void setCasemark9(String casemark9) {
        this.casemark9 = casemark9;
    }
    // ケースマーク10行目
    @SerializedName("casemark10")
    private String casemark10;
    public String getCasemark10() {
        return casemark10;
    }
    public void setCasemark10(String casemark10) {
        this.casemark10 = casemark10;
    }
    // ケースマーク11行目
    @SerializedName("casemark11")
    private String casemark11;
    public String getCasemark11() {
        return casemark11;
    }
    public void setCasemark11(String casemark11) {
        this.casemark11 = casemark11;
    }
    // ケースマーク12行目
    @SerializedName("casemark12")
    private String casemark12;
    public String getCasemark12() {
        return casemark12;
    }
    public void setCasemark12(String casemark12) {
        this.casemark12 = casemark12;
    }
}

