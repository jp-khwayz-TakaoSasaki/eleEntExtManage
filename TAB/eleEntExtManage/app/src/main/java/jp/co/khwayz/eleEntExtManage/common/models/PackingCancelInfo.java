package jp.co.khwayz.eleEntExtManage.common.models;

import lombok.Data;

/**
 * 梱包解除 Class
 */
@Data
public class PackingCancelInfo {

    // region [ private variable ]
    private String invoiceNo;
    private int renban;
    private int lineNo;
    private int csNo;
    private int overPackNo;
    // endregion


    public PackingCancelInfo(String invoiceNo, int renban, int lineNo, int csNo, int overPackNo) {
        this.invoiceNo = invoiceNo;
        this.renban = renban;
        this.csNo = csNo;
        this.lineNo = lineNo;
        this.overPackNo = overPackNo;
    }
}
