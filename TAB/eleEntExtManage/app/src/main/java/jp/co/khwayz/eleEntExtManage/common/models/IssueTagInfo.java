package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class IssueTagInfo implements Parcelable, Cloneable {
    /** Invoice番号 */
    @SerializedName("invoiceNo")
    private String invoiceNo;
    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void invoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    /** 連番 */
    @SerializedName("renban")
    private int renban;
    public int getRenban() {
        return renban;
    }
    public void setRenban(int renban) {
        this.renban = renban;
    }
    /** 行番 */
    @SerializedName("lineNo")
    private int lineNo;
    public int getLineNo() {
        return lineNo;
    }
    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }
    /** 発注番号 */
    @SerializedName("hachuNo")
    private String placeOrderNo;
    public String getPlaceOrderNo() {
        return placeOrderNo;
    }
    public void setPlaceOrderNo(String placeOrderNo) {
        this.placeOrderNo = placeOrderNo;
    }
    /** 枝番 */
    @SerializedName("hachuEda")
    private int branchNo;
    public int getBranchNo() {
        return branchNo;
    }
    public void setBranchNo(int branchNo) {
        this.branchNo = branchNo;
    }
    /** 受注番号 */
    @SerializedName("juchuNo")
    private String receiveOrderNo;
    public String getReceiveOrderNo() {
        return receiveOrderNo;
    }
    public void setReceiveOrderNo(String receiveOrderNo) {
        this.receiveOrderNo = receiveOrderNo;
    }
    /** 品目コード */
    @SerializedName("hinmokuCode")
    private String itemCode;
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    /** 品目名 */
    @SerializedName("hinmokuName")
    private String itemName;
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    /** 在庫数量 */
    @SerializedName("zaikoSu")
    private double zaikoSu;
    public double getZaikoSu() { return zaikoSu; }
    public void setZaikoSu(double zaikoSu) {
        this.zaikoSu = zaikoSu;
    }
    /** 出荷数量 */
    @SerializedName("syukkaSu")
    private double syukkaSu;
    public double getSyukkaSu() { return syukkaSu; }
    public void setSyukkaSu(double syukkaSu) {
        this.syukkaSu = syukkaSu;
    }
    /** 出荷数量合計 */
    @SerializedName("syukkaSuSum")
    private double syukkaSuSum;
    public double getSyukkaSuSum() { return syukkaSuSum; }
    public void setSyukkaSuSum(double syukkaSuSum) {
        this.syukkaSuSum = syukkaSuSum;
    }
    /** 出荷単位 */
    @SerializedName("syukkaTani")
    private String syukkaTani;
    public String getSyukkaTani() {
        return syukkaTani;
    }
    public void setSyukkaTani(String syukkaTani) {
        this.syukkaTani = syukkaTani;
    }
    /** 梱包済みフラグ */
    @SerializedName("konpozumiFlag")
    private String konpozumiFlag;
    public String getKonpozumiFlag() {
        return konpozumiFlag;
    }
    public void setKonpozumiFlag(String konpozumiFlag) {
        this.konpozumiFlag = konpozumiFlag;
    }
    /** ケースマーク番号 */
    @SerializedName("csNumber")
    private Integer csNumber;
    public Integer getCsNumber() {
        return csNumber;
    }
    public void setCsNumber(Integer csNumber) {
        this.csNumber = csNumber;
    }
    /** オーバーパック番号 */
    @SerializedName("overPackNo")
    private Integer overPackNo;
    public Integer getOverPackNo() {
        return overPackNo;
    }
    public void setOverPackNo(Integer overPackNo) {
        this.overPackNo = overPackNo;
    }
    /** 入庫荷姿 */
    @SerializedName("nyukoNisugata")
    private String packingType;
    public String getPackingType() {
        return packingType;
    }
    public void setPackingType(String packingType) {
        this.packingType = packingType;
    }
    /** 入庫日 */
    @SerializedName("nyukoDate")
    private String receiptDate;
    public String getReceiptDate() {
        return receiptDate;
    }
    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }
    public String getReceiptDateSlash() {
        StringBuilder dateBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(receiptDate)) {
            dateBuilder.append(receiptDate.substring(0, 4)).append("/");
            dateBuilder.append(receiptDate.substring(4, 6)).append("/");
            dateBuilder.append(receiptDate.substring(6, 8));
        }
        return dateBuilder.toString();
    }
    /** 同梱番号 */
    @SerializedName("combineNo")
    private Long combineNo;
    public Long getCombineNo() {
        return combineNo;
    }
    public void setCombineNo(Long combineNo) {
        this.combineNo = combineNo;
    }

    // region [ constructor ]
    public IssueTagInfo(String invoiceNo, int renban, int lineNo) {
        this.invoiceNo = invoiceNo;
        this.renban = renban;
        this.lineNo = lineNo;
    }
    // endregion

    // region [ implements ]
    @Override
    public int describeContents() { return 0; }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invoiceNo);
        dest.writeInt(renban);
        dest.writeInt(lineNo);
        dest.writeString(placeOrderNo);
        dest.writeInt(branchNo);
        dest.writeString(receiveOrderNo);
        dest.writeString(itemCode);
        dest.writeString(itemName);
        dest.writeDouble(zaikoSu);
        dest.writeDouble(syukkaSu);
        dest.writeDouble(syukkaSuSum);
        dest.writeString(syukkaTani);
        dest.writeString(konpozumiFlag);
        dest.writeInt(csNumber);
        dest.writeInt(overPackNo);
        dest.writeString(packingType);
        dest.writeString(receiptDate);
        dest.writeLong(combineNo);
    }
    public IssueTagInfo(Parcel in) {
        invoiceNo = in.readString();
        renban = in.readInt();
        lineNo = in.readInt();
        placeOrderNo = in.readString();
        branchNo = in.readInt();
        receiveOrderNo = in.readString();
        itemCode = in.readString();
        itemName = in.readString();
        zaikoSu = in.readDouble();
        syukkaSu = in.readDouble();
        syukkaSuSum = in.readDouble();
        syukkaTani = in.readString();
        konpozumiFlag = in.readString();
        csNumber = in.readInt();
        overPackNo = in.readInt();
        packingType = in.readString();
        receiptDate = in.readString();
        combineNo = in.readLong();
    }
    public static final Creator<IssueTagInfo> CREATOR = new Creator<IssueTagInfo>() {
        @Override
        public IssueTagInfo createFromParcel(Parcel in) {
            return new IssueTagInfo(in);
        }
        @Override
        public IssueTagInfo[] newArray(int size) {
            return new IssueTagInfo[size];
        }
    };
    @Override
    public IssueTagInfo clone() throws CloneNotSupportedException {
        return (IssueTagInfo) super.clone();
    }
    // endregion

    // region [ public method ]
    /**
     * 荷札番号取得
     * @return 発注番号 - 枝番
     */
    public String getTagNo() {
        return this.placeOrderNo + " - " + String.format(Locale.JAPAN, "%03d", this.branchNo);
    }

    // endregion
}
