package jp.co.khwayz.eleEntExtManage;

import jp.co.khwayz.eleEntExtManage.common.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackingScanInfo {
    private Integer no;
    private String overPack;
    private String purchaseOrderNo;
    private Integer branchNo;
    private String orderNo;
    private String itemCode;
    private String itemName;
    private double stockQuantity;
    private double shipmentQuantity;
    private double shipmentQuantityTotal;
    private String shipmentUnit;
    private String receiptAppearance;
    private String bundledNumber;
    private String receiptDay;
    private Integer renban;
    private Integer lineNo;
    private String singlePackFlag;
    private String onHoldFlag;
    private String onSelectFlag;

    public PackingScanInfo() {
    }

    public PackingScanInfo(Integer pNo, String pOverPack, String pPurchaseOrderNo,
                           Integer pBranchNo, String pOrderNo, String pItemCode, String pItemName,
                           double pStockQuantity, double pShipmentQuantity, double pShipmentQuantityTotal,
                           String pShipmentUnit, String pReceiptAppearance, String pBundledNumber,
                           String pReceiptDay, Integer pRenban, Integer pLineNo, String pSinglePackFlag, String pOnHoldFlag) {
        no = pNo;
        overPack = pOverPack;
        purchaseOrderNo = pPurchaseOrderNo;
        branchNo = pBranchNo;
        orderNo = pOrderNo;
        itemCode = pItemCode;
        itemName = pItemName;
        stockQuantity = pStockQuantity;
        shipmentQuantity = pShipmentQuantity;
        shipmentQuantityTotal = pShipmentQuantityTotal;
        shipmentUnit = pShipmentUnit;
        receiptAppearance = pReceiptAppearance;
        bundledNumber = pBundledNumber;
        receiptDay = pReceiptDay;
        renban = pRenban;
        lineNo = pLineNo;
        singlePackFlag = pSinglePackFlag;
        onHoldFlag = pOnHoldFlag;
        onSelectFlag = Constants.FLAG_FALSE;
    }
}
