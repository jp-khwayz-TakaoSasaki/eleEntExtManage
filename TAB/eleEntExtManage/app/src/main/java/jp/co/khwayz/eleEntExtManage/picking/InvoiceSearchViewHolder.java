package jp.co.khwayz.eleEntExtManage.picking;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class InvoiceSearchViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView onhold;
    private TextView siNo;
    private TextView invoiceNo;
    private TextView deadline;
    private TextView destination;
    private TextView packingInstruction;
    private TextView replayDate;
    private TextView shipDate;
    private TextView lineCount;
    private TextView cool;
    private TextView dangerous;
    private TextView remarks;
    private TextView packingDate;

    public InvoiceSearchViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.picking_invoice_row);
        this.invoiceNo = itemView.findViewById(R.id.invoice_no);
        this.deadline = itemView.findViewById(R.id.deadline);
        this.destination = itemView.findViewById(R.id.destination);
        this.packingInstruction = itemView.findViewById(R.id.packing_instruction);
        this.replayDate = itemView.findViewById(R.id.row_replay_date);
        this.shipDate = itemView.findViewById(R.id.picking_row_ship_date);
        this.lineCount = itemView.findViewById(R.id.picking_row_line_count);
        this.cool = itemView.findViewById(R.id.picking_row_cool);
        this.dangerous = itemView.findViewById(R.id.picking_row_dangerous);
        this.remarks = itemView.findViewById(R.id.picking_row_remarks);
        this.onhold = itemView.findViewById(R.id.picking_row_onhold);
    }

}
