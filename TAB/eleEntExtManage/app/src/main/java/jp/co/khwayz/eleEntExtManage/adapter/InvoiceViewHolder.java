package jp.co.khwayz.eleEntExtManage.adapter;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class InvoiceViewHolder extends RecyclerView.ViewHolder {
    private TextView siNo;
    private TextView invoiceNo;
    private TextView deadline;
    private TextView destination;
    private TextView packingInstruction;
    private TextView packingDate;

    public InvoiceViewHolder(@NonNull View itemView) {
        super(itemView);
        this.siNo = itemView.findViewById(R.id.si_no);
        this.invoiceNo = itemView.findViewById(R.id.invoice_no);
        this.deadline = itemView.findViewById(R.id.deadline);
        this.destination = itemView.findViewById(R.id.destination);
        this.packingInstruction = itemView.findViewById(R.id.packing_instruction);
        this.packingDate = itemView.findViewById(R.id.packing_date);
    }
}
