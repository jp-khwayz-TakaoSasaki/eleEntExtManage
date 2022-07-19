package jp.co.khwayz.eleEntExtManage.casemark_paste;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class CaseMarkPasteScanViewHolder extends RecyclerView.ViewHolder {
    private LinearLayout row;
    private TextView no;
    private TextView caseMarkNo;
    private TextView purchaseOrderNo;
    private TextView branchNo;
    private TextView orderNo;
    private TextView itemCode;
    private TextView itemName;
    private TextView stock;
    private TextView shipmentQuantity;
    private TextView shipmentUnit;
    private TextView packingForm;

    public CaseMarkPasteScanViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.casemark_paste_scan_row);
        this.no = itemView.findViewById(R.id.tv_casemark_paste_scan_no);
        this.caseMarkNo = itemView.findViewById(R.id.tv_casemark_paste_scan_cno);
        this.purchaseOrderNo = itemView.findViewById(R.id.tv_casemark_paste_scan_purchase_order_no);
        this.branchNo = itemView.findViewById(R.id.tv_casemark_paste_scan_branch_no);
        this.orderNo = itemView.findViewById(R.id.tv_casemark_paste_scan_order_no);
        this.itemCode = itemView.findViewById(R.id.tv_casemark_paste_scan_item_code);
        this.itemName = itemView.findViewById(R.id.tv_casemark_paste_scan_item_name);
        this.stock = itemView.findViewById(R.id.tv_casemark_paste_scan_stock);
        this.shipmentQuantity = itemView.findViewById(R.id.tv_casemark_paste_scan_shipment_quantity);
        this.shipmentUnit = itemView.findViewById(R.id.tv_casemark_paste_scan_shipment_unit);
        this.packingForm = itemView.findViewById(R.id.tv_casemark_paste_scan_packing_form);
    }
}
