package jp.co.khwayz.eleEntExtManage.casemark_print;

import android.graphics.Bitmap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseMarkPrintInfo {

    // InvoiceNo
    private String invoiceNo;
    // ケースマーク番号
    private String caseMarkNo;
    // QRコード
    private Bitmap qrCodeImage;
    // ケースマーク１行目
    private String caseMarkRow01;
    // ケースマーク２行目
    private String caseMarkRow02;
    // ケースマーク３行目
    private String caseMarkRow03;
    // ケースマーク４行目
    private String caseMarkRow04;
    // ケースマーク５行目
    private String caseMarkRow05;
    // ケースマーク６行目
    private String caseMarkRow06;
    // ケースマーク７行目
    private String caseMarkRow07;
    // ケースマーク８行目
    private String caseMarkRow08;
    // ケースマーク９行目
    private String caseMarkRow09;
    // ケースマーク１０行目
    private String caseMarkRow10;
    // ケースマーク１１行目
    private String caseMarkRow11;
    // ケースマーク１２行目
    private String caseMarkRow12;

    public CaseMarkPrintInfo(String invoiceNo, String caseMarkNo, Bitmap qrCodeImage, String caseMarkRow01, String caseMarkRow02
            , String caseMarkRow03, String caseMarkRow04, String caseMarkRow05, String caseMarkRow06, String caseMarkRow07
            , String caseMarkRow08, String caseMarkRow09, String caseMarkRow10, String caseMarkRow11, String caseMarkRow12) {
        this.invoiceNo = invoiceNo;
        this.caseMarkNo = caseMarkNo;
        this.qrCodeImage = qrCodeImage;
        this.caseMarkRow01 = caseMarkRow01;
        this.caseMarkRow02 = caseMarkRow02;
        this.caseMarkRow03 = caseMarkRow03;
        this.caseMarkRow04 = caseMarkRow04;
        this.caseMarkRow05 = caseMarkRow05;
        this.caseMarkRow06 = caseMarkRow06;
        this.caseMarkRow07 = caseMarkRow07;
        this.caseMarkRow08 = caseMarkRow08;
        this.caseMarkRow09 = caseMarkRow09;
        this.caseMarkRow10 = caseMarkRow10;
        this.caseMarkRow11 = caseMarkRow11;
        this.caseMarkRow12 = caseMarkRow12;
    }
}
