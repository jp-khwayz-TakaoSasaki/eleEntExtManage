package jp.co.khwayz.eleEntExtManage.packing;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class PackingScanOffBookViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView no;
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
    private TextView item;
    private TextView packingQuantity;
    private TextView packingType;
    private TextView labelInstruction;

    public PackingScanOffBookViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.off_book_confirm_row);
        this.orderNo = itemView.findViewById(R.id.off_book_confirm_row_order_no);
        this.branchNo = itemView.findViewById(R.id.off_book_confirm_row_branch_no);
    }
}
