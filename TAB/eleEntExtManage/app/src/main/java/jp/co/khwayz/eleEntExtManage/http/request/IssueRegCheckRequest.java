package jp.co.khwayz.eleEntExtManage.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueRegCheckRequest {
    // 明細リスト
    @SerializedName("putlist")
    private ArrayList<RequestDetail> list = new ArrayList<>();
    @Getter
    @Setter
    public static class RequestDetail {
        /** InvoiceNo */
        @SerializedName("invoiceNo")
        private String invoiceNo;
    }
}
