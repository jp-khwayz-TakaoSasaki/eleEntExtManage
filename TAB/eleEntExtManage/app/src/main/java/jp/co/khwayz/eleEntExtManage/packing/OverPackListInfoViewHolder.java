package jp.co.khwayz.eleEntExtManage.packing;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class OverPackListInfoViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView overPackNo;
    private TextView purchaseOrderNo;
    private TextView branchNo;
    private TextView orderNo;
    private TextView bundledNo;

    public OverPackListInfoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.over_pack_row);
        this.overPackNo = itemView.findViewById(R.id.tv_over_pack_row_over_pack_no);
        this.purchaseOrderNo = itemView.findViewById(R.id.tv_over_pack_row_purchase_order_no);
        this.branchNo = itemView.findViewById(R.id.tv_over_pack_row_purchase_branch_no);
        this.orderNo = itemView.findViewById(R.id.tv_over_pack_row_order_no);
        this.bundledNo = itemView.findViewById(R.id.tv_over_pack_row_bundled_no);
    }
}
