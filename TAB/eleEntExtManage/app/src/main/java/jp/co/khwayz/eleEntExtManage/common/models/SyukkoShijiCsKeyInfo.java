package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

public class SyukkoShijiCsKeyInfo implements Parcelable {

    // ケースマーク番号
    private String invoiceNo;
    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    // 連番
    private int renban;
    public int getRenban() {
        return renban;
    }
    public void setRenban(int renban) {
        this.renban = renban;
    }

    // 行番
    private int lineNo;
    public int getLineNo() {
        return lineNo;
    }
    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    // ケースマーク番号
    private int csNo;
    public int getCsNo() {
        return csNo;
    }
    public void setCsNo(int csNo) {
        this.csNo = csNo;
    }

    public SyukkoShijiCsKeyInfo(@NotNull Parcel in){
        renban = in.readInt();
        lineNo = in.readInt();
        csNo = in.readInt();
    }

    public SyukkoShijiCsKeyInfo(String invoiceNo, int renban, int lineNo, int csNo){
        this.invoiceNo = invoiceNo;
        this.renban = renban;
        this.lineNo = lineNo;
        this.csNo = csNo;
    }

    public static final Creator<SyukkoShijiCsKeyInfo> CREATOR = new Creator<SyukkoShijiCsKeyInfo>() {
        @Override
        public SyukkoShijiCsKeyInfo createFromParcel(Parcel in) {
            return new SyukkoShijiCsKeyInfo(in);
        }

        @Override
        public SyukkoShijiCsKeyInfo[] newArray(int size) {
            return new SyukkoShijiCsKeyInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invoiceNo);
        dest.writeInt(renban);
        dest.writeInt(lineNo);
        dest.writeInt(csNo);
    }
}
