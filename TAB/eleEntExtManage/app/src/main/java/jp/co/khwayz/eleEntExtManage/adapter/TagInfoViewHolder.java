package jp.co.khwayz.eleEntExtManage.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class TagInfoViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView no;
    private TextView orderNo;
    private TextView purchaseOrderNo;
    private TextView item;
    private TextView shipmentQuantity;
    private TextView shipmentUnit;
    private TextView packingQuantity;
    private TextView packingType;
    private TextView locationNo;
    private TextView cNo;

    public TagInfoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.tag_scan_row);
        this.no = itemView.findViewById(R.id.cargo_scan_row_order_no);
        this.orderNo = itemView.findViewById(R.id.cargo_scan_row_branch_no);
        this.purchaseOrderNo = itemView.findViewById(R.id.cargo_scan_row_orders_res_no);
        this.item = itemView.findViewById(R.id.cargo_scan_row_item_code);
        this.shipmentQuantity = itemView.findViewById(R.id.cargo_scan_row_item_name);
        this.shipmentUnit = itemView.findViewById(R.id.cargo_scan_row_shipment_quantity);
        this.packingQuantity = itemView.findViewById(R.id.cargo_scan_row_shipment_unit);
        this.packingType = itemView.findViewById(R.id.cargo_scan_row_stock_quantity);
        this.locationNo = itemView.findViewById(R.id.cargo_scan_row_packing);
        this.cNo = itemView.findViewById(R.id.issue_tag_scan_c_no);
    }
}
