package jp.co.khwayz.eleEntExtManage.casemark_paste;

import jp.co.khwayz.eleEntExtManage.common.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseMarkPasteScanInfo {
    private int no;
    private int caseMarkNo;
    private String purchaseOrderNo;         // 発注番号
    private int branchNo;                // 枝番
    private String orderNo;
    private String itemCode;
    private String itemName;
    private double stock;
    private double shipmentQuantity;
    private String shipmentUnit;
    private String packingForm;
    private String onSelectFlag;

    public CaseMarkPasteScanInfo(){
    }

    public CaseMarkPasteScanInfo(int no, int caseMarkNo, String purchaseOrderNo, int branchNo, String orderNo, String itemCode, String itemName, double stock, double shipmentQuantity, String shipmentUnit, String packingForm) {
        this.no = no;
        this.caseMarkNo = caseMarkNo;
        this.purchaseOrderNo = purchaseOrderNo;
        this.branchNo = branchNo;
        this.orderNo = orderNo;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.stock = stock;
        this.shipmentQuantity = shipmentQuantity;
        this.shipmentUnit = shipmentUnit;
        this.packingForm = packingForm;
        onSelectFlag = Constants.FLAG_FALSE;
    }
}
