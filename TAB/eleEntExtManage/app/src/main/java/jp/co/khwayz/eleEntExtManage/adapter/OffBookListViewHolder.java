package jp.co.khwayz.eleEntExtManage.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class OffBookListViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView orderNo;
    private TextView branchNo;

    public OffBookListViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.off_book_confirm_row);
        this.orderNo = itemView.findViewById(R.id.off_book_confirm_row_order_no);
        this.branchNo = itemView.findViewById(R.id.off_book_confirm_row_branch_no);
    }
}
