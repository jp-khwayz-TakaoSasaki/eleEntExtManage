package jp.co.khwayz.eleEntExtManage.casemark_print;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseMarkPrintStatusInfo {

    // InvoiceNo
    private String invoiceNo;
    // 仕向地
    private String destination;
    // 出荷日
    private String shipDate;
    // 輸送モード
    private String shippingMode;
    // 印刷済みフラグ
    private String printStatus;

    public CaseMarkPrintStatusInfo(String invoiceNo, String destination, String shipDate, String shippingMode, String printStatus) {
        this.invoiceNo = invoiceNo;
        this.destination = destination;
        this.shipDate = shipDate;
        this.shippingMode = shippingMode;
        this.printStatus = printStatus;
    }
}
