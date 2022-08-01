package jp.co.khwayz.eleEntExtManage.instr_cfm;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceTempInfo {
    public InvoiceTempInfo() { }
    @SerializedName("tenpNo")
    private int tempNo;
    @SerializedName("tenpPath")
    private String tempPath;

    /**
     * ファイル名取得
     * @return ファイル名
     */
    public String getFileName() {
        String fileName = "";
        if (!TextUtils.isEmpty(tempPath)) {
            String[] components = tempPath.split("[\\\\/]"); //$NON-NLS-1$
            fileName = components[components.length - 1];
        }
        return fileName;
    }
}
