package jp.co.khwayz.eleEntExtManage.instr_cfm;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class CheckShippingInstructionsViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView no;
    private TextView orderNo;
    private TextView shippingInstructions;

    public CheckShippingInstructionsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.check_shipping_instructions_row);
        this.no = itemView.findViewById(R.id.check_shipping_instructions_row_no);
        this.orderNo = itemView.findViewById(R.id.check_shipping_instructions_row_order_no);
        this.shippingInstructions = itemView.findViewById(R.id.check_shipping_instructions_row_shipping_instructions);
    }
}
