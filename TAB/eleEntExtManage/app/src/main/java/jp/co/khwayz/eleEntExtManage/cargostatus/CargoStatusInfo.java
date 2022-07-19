package jp.co.khwayz.eleEntExtManage.cargostatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoStatusInfo {
    private String receipt;
    private String storage;
    private String picking;
    private String invoiceNo;
    private String packing;
    private String caseMark;
    private String c_SlashNo;
    private String issue;

    public CargoStatusInfo(String receipt, String storage, String pikcing, String invoiceNo
            , String packing, String caseMark, String c_SlashNo, String issue) {
        this.receipt = receipt;
        this.storage = storage;
        this.picking = pikcing;
        this.invoiceNo = invoiceNo;
        this.packing = packing;
        this.caseMark = caseMark;
        this.c_SlashNo = c_SlashNo;
        this.issue = issue;
    }
}
