package jp.co.khwayz.eleEntExtManage.picking;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class PickedInvoiceSearchViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView invoiceNo;
    private TextView destination;
    private TextView replyDate;
    private TextView shipDate;
    private TextView lineCount;
    private TextView cool;
    private TextView dangerous;
    private TextView remarks;
    private TextView onhold;

    public PickedInvoiceSearchViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.packing_invoice_row);
        this.invoiceNo = itemView.findViewById(R.id.invoice_no);
        this.destination = itemView.findViewById(R.id.destination);
        this.replyDate = itemView.findViewById(R.id.reply_date);
        this.shipDate = itemView.findViewById(R.id.ship_date);
        this.lineCount = itemView.findViewById(R.id.line_count);
        this.cool = itemView.findViewById(R.id.cool);
        this.dangerous = itemView.findViewById(R.id.dangerous);
        this.remarks = itemView.findViewById(R.id.remarks);
        this.onhold = itemView.findViewById(R.id.onhold);
    }
}
