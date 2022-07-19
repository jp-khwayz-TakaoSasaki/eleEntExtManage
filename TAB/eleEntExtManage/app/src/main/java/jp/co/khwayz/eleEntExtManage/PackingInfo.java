package jp.co.khwayz.eleEntExtManage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackingInfo {
    private int no;
    /* Invoice番号 */
    private String invoiceNo;
    /* 仕向地 */
    private String destination;
    /* リクエスト返信希望日 */
    private String replyDate;
    /* 出荷日 */
    private String shipDate;
    /* 行数 */
    private String lineCount;
    /* 保冷 */
    private String cool;
    /* 危険品 */
    private String dangerous;
    /* 備考 */
    private String remarks;
    /* 保留 */
    private String onHold;

    public PackingInfo(String pInvoiceNo, String pDestination, String pReplyDate, String pShipDate,
                       String pLineCount, String pCool, String pDangerous, String pRemarks, String pOnHold) {
        invoiceNo = pInvoiceNo;
        destination = pDestination;
        replyDate = pReplyDate;
        shipDate = pShipDate;
        lineCount = pLineCount;
        cool = pCool;
        dangerous = pDangerous;
        remarks = pRemarks;
        onHold = pOnHold;
    }
}
