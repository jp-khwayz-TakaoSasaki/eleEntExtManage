package jp.co.khwayz.eleEntExtManage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceInfo {
    private String onhold;
    private int siNo;
    // InvoiceNo
    private String invoiceNo;
    private String deadline;
    // 仕向地
    private String destination;
    private String packingInstruction;
    private String packingDate;
    // 出荷日
    private String shipDate;
    private String replayDate;
    private String lineCount;
    private String cool;
    private String dangerousGoods;
    private String remarks;
    private Boolean isSiNo = true;
    // ケースマーク数
    private String caseMarkCount;
    // スキャン済みケースマーク数
    private String scanedCaseMarkCount;


    // 出庫確認用
    public InvoiceInfo(String invoiceNo, String destination, String shipDate, String caseMarkCount, String scanedCaseMarkCount) {
        this.invoiceNo = invoiceNo;
        this.destination = destination;
        this.shipDate = shipDate;
        this.caseMarkCount = caseMarkCount;
        this.scanedCaseMarkCount = scanedCaseMarkCount;
    }

    public InvoiceInfo(String invoiceNo, String destination, String shipDate) {
        this.invoiceNo = invoiceNo;
        this.destination = destination;
        this.shipDate = shipDate;
    }

    public InvoiceInfo(int siNo, String invoiceNo, String deadline, String destination, String packingInstruction) {
        this.siNo = siNo;
        this.invoiceNo = invoiceNo;
        this.deadline = deadline;
        this.destination = destination;
        this.packingInstruction = packingInstruction;
    }

    public InvoiceInfo(int siNo, String invoiceNo, String destination, String replayDate, String shipDate, String lineCount, String cool, String danger, String rem, String hold) {
        this.siNo = siNo;
        this.invoiceNo = invoiceNo;
        this.destination = destination;
        this.replayDate = replayDate;
        this.shipDate = shipDate;
        this.lineCount = lineCount;
        this.cool = cool;
        this.dangerousGoods = danger;
        this.remarks = rem;
        this.onhold = hold;
    }

    public InvoiceInfo(int siNo, String invoiceNo, String deadline, String destination, String packingInstruction, String packingDate) {
        this.siNo = siNo;
        this.invoiceNo = invoiceNo;
        this.deadline = deadline;
        this.destination = destination;
        this.packingInstruction = packingInstruction;
        this.packingDate = packingDate;
    }

    public InvoiceInfo(int siNo, String invoiceNo, String deadline, String destination, String packingInstruction, String packingDate, Boolean isSiNo) {
        this.siNo = siNo;
        this.invoiceNo = invoiceNo;
        this.deadline = deadline;
        this.destination = destination;
        this.packingInstruction = packingInstruction;
        this.packingDate = packingDate;
        this.isSiNo = isSiNo;
    }
}
