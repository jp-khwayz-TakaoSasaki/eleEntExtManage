package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * オーバーパック梱包資材情報取得用　Class
 */
public class OverPackKonpoShizaiInfo implements Parcelable {

    // region [ implements ]
    public OverPackKonpoShizaiInfo(@NotNull Parcel in) {
        invoiceNo = in.readString();
        overPackNo = in.readInt();
        packingMaterialNo = in.readInt();
        packingMaterial = in.readString();
    }

    public OverPackKonpoShizaiInfo(String invoiceNo, int overPackNo, int materialNo, String material){
        this.invoiceNo = invoiceNo;
        this.overPackNo = overPackNo;
        this.packingMaterialNo = materialNo;
        this.packingMaterial = material;
    }

    public OverPackKonpoShizaiInfo(String invoiceNo) {
        invoiceNo = invoiceNo;
    }
    public static final Creator<OverPackKonpoShizaiInfo> CREATOR = new Creator<OverPackKonpoShizaiInfo>() {
        @Override
        public OverPackKonpoShizaiInfo createFromParcel(Parcel in) {
            return new OverPackKonpoShizaiInfo(in);
        }

        @Override
        public OverPackKonpoShizaiInfo[] newArray(int size) {
            return new OverPackKonpoShizaiInfo[size];
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
        dest.writeInt(overPackNo);
        dest.writeInt(packingMaterialNo);
        dest.writeString(packingMaterial);
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
    // オーバーパック番号
    @SerializedName("overPackNo")
    private int overPackNo;
    public int getOverPackNo() {
        return overPackNo;
    }
    public void setOverPackNo(int overPackNo) {
        this.overPackNo = overPackNo;
    }
    // 梱包資材番号
    @SerializedName("packingMaterialNo")
    private int packingMaterialNo;
    public int getPackingMaterialNo() {
        return packingMaterialNo;
    }
    public void setPackingMaterialNo(int packingMaterialNo) {
        this.packingMaterialNo = packingMaterialNo;
    }
    // 梱包資材
    @SerializedName("packingMaterial")
    private String packingMaterial;
    public String getPackingMaterial() {
        return packingMaterial;
    }
    public void setPackingMaterial(String packingMaterial) {
        this.packingMaterial = packingMaterial;
    }
}
