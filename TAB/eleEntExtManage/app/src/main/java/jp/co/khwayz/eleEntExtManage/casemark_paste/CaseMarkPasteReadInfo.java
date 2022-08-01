package jp.co.khwayz.eleEntExtManage.casemark_paste;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * ケースマーク貼付け読取情報
 */
public class CaseMarkPasteReadInfo implements Parcelable {

    // InvoiceNo
    private String invoiceNo;
    // ケースマーク番号
    private int caseMarkNo;

    public CaseMarkPasteReadInfo(String invoiceNo, int caseMarkNo) {
        this.invoiceNo = invoiceNo;
        this.caseMarkNo = caseMarkNo;
    }

    protected CaseMarkPasteReadInfo(Parcel in) {
        invoiceNo = in.readString();
        caseMarkNo = in.readInt();
    }

    public static final Creator<CaseMarkPasteReadInfo> CREATOR = new Creator<CaseMarkPasteReadInfo>() {
        @Override
        public CaseMarkPasteReadInfo createFromParcel(Parcel in) {
            return new CaseMarkPasteReadInfo(in);
        }

        @Override
        public CaseMarkPasteReadInfo[] newArray(int size) {
            return new CaseMarkPasteReadInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invoiceNo);
        dest.writeInt(caseMarkNo);
    }
}
