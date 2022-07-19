package jp.co.khwayz.eleEntExtManage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingInstructionsInfo {
    /* No */
    private String no;
    /* 受注番号 */
    private String orderNo;
    /* 出荷指示欄 */
    private String shippingInstructions;

    public ShippingInstructionsInfo(String pNo, String pOrderNo, String pShippingInstructions) {
        this.no = pNo;
        this.orderNo = pOrderNo;
        this.shippingInstructions = pShippingInstructions;
    }
}
