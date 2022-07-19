package jp.co.khwayz.eleEntExtManage.common.models;

import lombok.Data;

/**
 * Invoice検索条件用 Class
 */
@Data
public class InvoiceSearchInfo  {

    // region [ private variable ]
    private String invoiceNo;
    private String shimukeChi;
    private String listHenshinDate;
    private String syukkaDate;
    private String horei;
    private String kikenhinKbn;
    private String yusoKbn;
    // endregion


    public InvoiceSearchInfo(String invoiceNo, String shimukeChi, String listHenshinDate, String syukkaDate, String horei, String kikenhinKbn, String yusoKbn) {
        this.invoiceNo = invoiceNo;
        this.shimukeChi = shimukeChi;
        this.listHenshinDate = listHenshinDate;
        this.syukkaDate = syukkaDate;
        this.horei = horei;
        this.kikenhinKbn = kikenhinKbn;
        this.yusoKbn = yusoKbn;
    }
}
