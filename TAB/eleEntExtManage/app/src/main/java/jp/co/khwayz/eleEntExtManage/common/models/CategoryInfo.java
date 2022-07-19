package jp.co.khwayz.eleEntExtManage.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 区分マスタSpinner用 Class
 */
public class CategoryInfo implements Parcelable {
    // region [ private variable ]
    @SerializedName("kubun")
    private String category;
    @SerializedName("element")
    private String element;
    @SerializedName("elementNm")
    private String elementName;
    @SerializedName("elementMemo")
    private String elementMemo;
    @SerializedName("sortOrder")
    private int sortOrder;
    // endregion

    // region [ constructor ]
    /**
     * コンストラクタ
     * @param element 区分マスタの要素
     */
    public CategoryInfo(String category, String element) {
        this.category = category;
        this.element = element;
        this.elementName = "";
        this.elementMemo = "";
        this.sortOrder = 0;
    }
    // endregion

    // region [ Override ]
    protected CategoryInfo(Parcel in) {
        this.category = in.readString();
        this.element = in.readString();
        this.elementName = in.readString();
        this.elementMemo = in.readString();
        this.sortOrder = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeString(this.element);
        dest.writeString(this.elementName);
        dest.writeString(this.elementMemo);
        dest.writeInt(this.sortOrder);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryInfo> CREATOR = new Creator<CategoryInfo>() {
        @Override
        public CategoryInfo createFromParcel(Parcel in) {
            return new CategoryInfo(in);
        }

        @Override
        public CategoryInfo[] newArray(int size) {
            return new CategoryInfo[size];
        }
    };
    // endregion

    /* 区分 */
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    /* 要素 */
    public String getElement() { return this.element; }
    public void setElement(String element) { this.element = element; }

    /* 要素名 */
    public String getElementName() { return this.elementName; }
    public void setElementName(String elementName) { this.elementName = elementName; }

    /* 要素備考 */
    public String getElementMemo() { return this.elementMemo; }
    public void setElementMemo(String elementMemo) { this.elementMemo = elementMemo; }

    /* 表示順 */
    public int getSortOrder() { return this.sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
