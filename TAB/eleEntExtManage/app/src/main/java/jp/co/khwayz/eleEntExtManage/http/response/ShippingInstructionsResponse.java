package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.instr_cfm.ShippingInstructionsInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingInstructionsResponse {
    /** ステータス */
    @SerializedName("status")
    private String status;
    /** エラーコード */
    @SerializedName("errorCode")
    private String errorCode;
    /** エラー詳細 */
    @SerializedName("errorDetail")
    private String errorDetail;
    /** data */
    @SerializedName("data")
    private ResultData data;

    @Getter
    @Setter
    public static class ResultData {
        @SerializedName("ushotSijiList")
        private ArrayList<ShippingInstructionsInfo> list;
    }
}
