package jp.co.khwayz.eleEntExtManage;

import lombok.Getter;

@Getter
public class TagScanInfo {
    private int no;
    private String orderNo;
    private String purchaseOrderNo;
    private String item;
    private int shipmentQuantity;
    private String shipmentUnit;
    private int packingQuantity;
    private String packingType;
    private String locationNo;
    private String cNo;
    public TagScanInfo(int no, String orderNo, String purchaseOrderNo, String item, int shipmentQuantity, String shipmentUnit, int packingQuantity, String packingType, String locationNo, String cNo) {
        this.no = no;
        this.orderNo = orderNo;
        this.purchaseOrderNo = purchaseOrderNo;
        this.item = item;
        this.shipmentQuantity = shipmentQuantity;
        this.shipmentUnit = shipmentUnit;
        this.packingQuantity = packingQuantity;
        this.packingType = packingType;
        this.locationNo = locationNo;
        this.cNo = cNo;
    }
}
