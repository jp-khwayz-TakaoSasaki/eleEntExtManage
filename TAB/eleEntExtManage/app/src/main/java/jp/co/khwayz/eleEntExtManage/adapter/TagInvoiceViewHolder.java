package jp.co.khwayz.eleEntExtManage.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class TagInvoiceViewHolder extends RecyclerView.ViewHolder {
    private LinearLayout row;
    private TextView orderNo;
    private TextView purchaseOrderNo;
    private TextView itemCode;
    private TextView itemName;
    private TextView branchNo;
    private TextView shipmentQuantity;
    private TextView shipmentUnit;
    private TextView stockQuantity;
    private TextView packing;
    private TextView locationNo;

    public TagInvoiceViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.cargo_scan_row);
        this.orderNo = itemView.findViewById(R.id.cargo_scan_row_order_no);
        this.branchNo = itemView.findViewById(R.id.cargo_scan_row_branch_no);
        this.purchaseOrderNo = itemView.findViewById(R.id.cargo_scan_row_orders_res_no);
        this.itemCode = itemView.findViewById(R.id.cargo_scan_row_item_code);
        this.itemName = itemView.findViewById(R.id.cargo_scan_row_item_name);
        this.shipmentQuantity = itemView.findViewById(R.id.cargo_scan_row_shipment_quantity);
        this.shipmentUnit = itemView.findViewById(R.id.cargo_scan_row_shipment_unit);
        this.stockQuantity =itemView.findViewById(R.id.cargo_scan_row_stock_quantity);
        this.packing = itemView.findViewById(R.id.cargo_scan_row_packing);
        this.locationNo = itemView.findViewById(R.id.cargo_scan_row_location_no);
    }
}
