package jp.co.khwayz.eleEntExtManage.casemark_paste;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseMarkPasteReadInfo {

    // InvoiceNo
    private String invoiceNo;
    // ケースマーク番号
    private String caseMarkNo;

    public CaseMarkPasteReadInfo(String invoiceNo, String caseMarkNo) {
        this.invoiceNo = invoiceNo;
        this.caseMarkNo = caseMarkNo;
    }
}
