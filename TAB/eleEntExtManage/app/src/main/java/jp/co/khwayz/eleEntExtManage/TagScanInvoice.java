package jp.co.khwayz.eleEntExtManage;

import lombok.Getter;

@Getter
public class TagScanInvoice {
    private int no;
    // 発注番号
    private String orderNo;
    // 枝番
    private int branchNo;
    // 受注番号
    private String purchaseOrderNo;
    // 品目コード
    private String itemCode;
    // 品目名
    private String itemName;
    // 出荷数量
    private String shipmentQuantity;
    // 出荷単位
    private String shipmentUnit;
    // 参考在庫数量
    private String stockQuantity;
    // 入荷荷姿
    private String packing;
    // ロケ番号
    private int packingQuantity;
    private String locationNo;
    private String item;
    private String packingType;
    private String labelInstruction;

    public TagScanInvoice(String orderNo, int branchNo, String purchaseOrderNo, String itemCode,
                          String itemName, String shipmentQuantity, String shipmentUnit, String stockQuantity, String packing, String locationNo) {
        this.orderNo = orderNo;
        this.branchNo = branchNo;
        this.purchaseOrderNo = purchaseOrderNo;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.shipmentQuantity = shipmentQuantity;
        this.shipmentUnit = shipmentUnit;
        this.stockQuantity = stockQuantity;
        this.packing = packing;
        this.locationNo = locationNo;
    }

    public TagScanInvoice(int no, String orderNo, String purchaseOrderNo, String item, String shipmentQuantity, String shipmentUnit, int packingQuantity, String packingType, String locationNo, String labelInstruction) {
        this.no = no;
        this.orderNo = orderNo;
        this.purchaseOrderNo = purchaseOrderNo;
        this.item = item;
        this.shipmentQuantity = shipmentQuantity;
        this.shipmentUnit = shipmentUnit;
        this.packingQuantity = packingQuantity;
        this.packingType = packingType;
        this.locationNo = locationNo;
        this.labelInstruction = labelInstruction;
    }
}
