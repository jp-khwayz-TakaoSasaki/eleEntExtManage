package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

public class SyukkoShijiKeyInfo implements Parcelable {

    private int renban;

    public int getRenban() {
        return renban;
    }

    public void setRenban(int renban) {
        this.renban = renban;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    private int lineNo;

    public SyukkoShijiKeyInfo(@NotNull Parcel in){
        renban = in.readInt();
        lineNo = in.readInt();
    }

    public SyukkoShijiKeyInfo(int renban, int lineNo){
        this.renban = renban;
        this.lineNo = lineNo;
    }

    public static final Creator<SyukkoShijiKeyInfo> CREATOR = new Creator<SyukkoShijiKeyInfo>() {
        @Override
        public SyukkoShijiKeyInfo createFromParcel(Parcel in) {
            return new SyukkoShijiKeyInfo(in);
        }

        @Override
        public SyukkoShijiKeyInfo[] newArray(int size) {
            return new SyukkoShijiKeyInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(renban);
        dest.writeInt(lineNo);
    }
}
