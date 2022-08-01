package jp.co.khwayz.eleEntExtManage.issueregist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueRegInvoiceSearchInfo implements Parcelable {
    // コンストラクタ
    public IssueRegInvoiceSearchInfo() { }

    // InvoiceNo
    @SerializedName("invoiceNo")
    private String invoiceNo;
    // 仕向地
    @SerializedName("shimukeChi")
    private String destination;
    // 出荷日
    @SerializedName("syukkaDate")
    private String shipDate;
    // ケースマーク数
    @SerializedName("caseMarkTotal")
    private int caseMarkTotal;
    // ケースマーク番号リスト
    @SerializedName("csNumberList")
    private ArrayList<IssueRegCaseMarkNo> caseMarkNoList;

    /**
     * ケースマークスキャン数取得
     * @return スキャン数
     */
    public long getScannedCaseMarkCount() {
        return caseMarkNoList.stream().filter(IssueRegCaseMarkNo::isRead).count();
    }

    protected IssueRegInvoiceSearchInfo(Parcel in) {
        invoiceNo = in.readString();
        destination = in.readString();
        shipDate = in.readString();
        caseMarkTotal = in.readInt();
//        caseMarkNoList = in.readArrayList(IssueRegCaseMarkNo.class.getClassLoader());
    }

    public static final Creator<IssueRegInvoiceSearchInfo> CREATOR = new Creator<IssueRegInvoiceSearchInfo>() {
        @Override
        public IssueRegInvoiceSearchInfo createFromParcel(Parcel in) {
            return new IssueRegInvoiceSearchInfo(in);
        }

        @Override
        public IssueRegInvoiceSearchInfo[] newArray(int size) {
            return new IssueRegInvoiceSearchInfo[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invoiceNo);
        dest.writeString(destination);
        dest.writeString(shipDate);
        dest.writeInt(caseMarkTotal);
    }
}
