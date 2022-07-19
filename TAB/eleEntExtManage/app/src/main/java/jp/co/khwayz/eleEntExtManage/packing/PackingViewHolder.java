package jp.co.khwayz.eleEntExtManage.packing;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackingViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView invoiceNo;
    private TextView destination;
    private TextView replayDate;
    private TextView shipDate;
    private TextView lineCount;
    private TextView cool;
    private TextView dangerous;
    private TextView remarks;
    private TextView onHold;

    public PackingViewHolder(@NonNull View itemView) {
        super(itemView);
        row = itemView.findViewById(R.id.packing_invoice_row);
        invoiceNo = itemView.findViewById(R.id.invoice_no);
        destination = itemView.findViewById(R.id.destination);
        replayDate = itemView.findViewById(R.id.reply_date);
        shipDate = itemView.findViewById(R.id.ship_date);
        lineCount = itemView.findViewById(R.id.line_count);
        cool = itemView.findViewById(R.id.cool);
        dangerous = itemView.findViewById(R.id.dangerous);
        remarks = itemView.findViewById(R.id.remarks);
        onHold = itemView.findViewById(R.id.onhold);
    }
}
