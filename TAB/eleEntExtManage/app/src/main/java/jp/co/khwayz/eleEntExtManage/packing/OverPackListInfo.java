package jp.co.khwayz.eleEntExtManage.packing;

import lombok.Getter;

@Getter
public class OverPackListInfo {
    private String overPackNo;
    private String purchaseOrderNo;
    private String branchNo;
    private String orderNo;
    private String bundledNo;
    private int renBan;
    private int lineNo;
    private boolean selectFlg;
    public OverPackListInfo(String overPackNo, String purchaseOrderNo, String branchNo
            , String orderNo, String bundledNo, int renBan, int lineNo) {
        this.overPackNo = overPackNo;
        this.purchaseOrderNo = purchaseOrderNo;
        this.branchNo = branchNo;
        this.orderNo = orderNo;
        this.bundledNo = bundledNo;
        this.renBan = renBan;
        this.lineNo = lineNo;
        this.selectFlg = false;
    }
    public void setOverPackNo(String overPackNo){
        this.overPackNo = overPackNo;
    }
}
