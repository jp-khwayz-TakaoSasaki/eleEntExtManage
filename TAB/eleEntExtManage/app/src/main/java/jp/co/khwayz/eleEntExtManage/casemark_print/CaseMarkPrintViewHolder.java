package jp.co.khwayz.eleEntExtManage.casemark_print;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class CaseMarkPrintViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView invoiceNo;
    private TextView destination;
    private TextView shipDate;
    private TextView shippingMode;
    private TextView printStatus;

    public CaseMarkPrintViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.casemark_print_row);
        this.invoiceNo = itemView.findViewById(R.id.casemark_print_row_invoice_no);
        this.destination = itemView.findViewById(R.id.casemark_print_row_destination);
        this.shipDate = itemView.findViewById(R.id.casemark_print_row_ship_date);
        this.shippingMode = itemView.findViewById(R.id.casemark_print_row_shipping_mode);
        this.printStatus = itemView.findViewById(R.id.casemark_print_row_print_status);
    }
}
