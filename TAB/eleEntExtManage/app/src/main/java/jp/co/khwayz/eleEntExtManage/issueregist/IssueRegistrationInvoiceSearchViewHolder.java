package jp.co.khwayz.eleEntExtManage.issueregist;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class IssueRegistrationInvoiceSearchViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView invoiceNo;
    private TextView destination;
    private TextView shipDate;

    public IssueRegistrationInvoiceSearchViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.issue_registration_invoice_search_row);
        this.invoiceNo = itemView.findViewById(R.id.issue_registration_invoice_search_row_invoice_no);
        this.destination = itemView.findViewById(R.id.issue_registration_invoice_search_row_destination);
        this.shipDate = itemView.findViewById(R.id.issue_registration_invoice_search_row_ship_date);
    }
}
