package jp.co.khwayz.eleEntExtManage.issueregist;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueRegCaseMarkNo {
    // ケースマーク番号
    @SerializedName("csNumber")
    private int CaseMarkNo;
    // 読取済みフラグ
    private boolean read;
}
