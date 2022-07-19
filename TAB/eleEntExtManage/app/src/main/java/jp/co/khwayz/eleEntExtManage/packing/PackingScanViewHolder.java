package jp.co.khwayz.eleEntExtManage.packing;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackingScanViewHolder extends RecyclerView.ViewHolder {
    private LinearLayout row;
    private TextView no;
    private TextView overPack;
    private TextView purchaseOrderNo;
    private TextView branchNo;
    private TextView orderNo;
    private TextView itemCode;
    private TextView itemName;
    private TextView stockQuantity;
    private TextView shipmentQuantity;
    private TextView shipmentQuantityTotal;
    private TextView shipmentUnit;
    private TextView receiptAppearance;
    private TextView bundledNumber;
    private TextView receiptDay;
    private Button buttonSinglePackaging;

    public PackingScanViewHolder(@NonNull View itemView) {
        super(itemView);
        row = itemView.findViewById(R.id.packing_scan_row);
        no = itemView.findViewById(R.id.packing_scan_row_no);
        overPack = itemView.findViewById(R.id.packing_scan_row_over_pack);
        purchaseOrderNo = itemView.findViewById(R.id.packing_scan_row_purchase_order_no);
        branchNo = itemView.findViewById(R.id.packing_scan_row_branch_no);
        orderNo = itemView.findViewById(R.id.packing_scan_row_order_no);
        itemCode = itemView.findViewById(R.id.packing_scan_row_item_code);
        itemName = itemView.findViewById(R.id.packing_scan_row_item_name);
        stockQuantity = itemView.findViewById(R.id.packing_scan_row_stock_quantity);
        shipmentQuantity = itemView.findViewById(R.id.packing_scan_row_shipment_quantity);
        shipmentQuantityTotal = itemView.findViewById(R.id.packing_scan_row_shipment_quantity_total);
        shipmentUnit = itemView.findViewById(R.id.packing_scan_row_shipment_unit);
        receiptAppearance = itemView.findViewById(R.id.packing_scan_row_receipt_appearance);
        bundledNumber = itemView.findViewById(R.id.packing_scan_row_bundled_number);
        receiptDay = itemView.findViewById(R.id.packing_scan_row_receipt_day);
        buttonSinglePackaging = itemView.findViewById(R.id.packing_scan_row_bt_single_packaging);
    }
}
