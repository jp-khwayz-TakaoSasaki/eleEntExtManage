package jp.co.khwayz.eleEntExtManage.casemark_paste;

import lombok.Getter;

@Getter
public class CaseMarkPasteScanInfo {
    private int no;
    private String caseMarkNo;
    private String purchaseOrderNo;
    private String branchNo;
    private String orderNo;
    private String itemCode;
    private String itemName;
    private String stock;
    private String shipmentQuantity;
    private String shipmentUnit;
    private String packingForm;

    public CaseMarkPasteScanInfo(int no, String caseMarkNo, String purchaseOrderNo, String branchNo, String orderNo, String itemCode, String itemName, String stock, String shipmentQuantity, String shipmentUnit, String packingForm) {
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
    }
}
