package jp.co.khwayz.eleEntExtManage.casemark_paste;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class CaseMarkPasteReadViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView invoiceNo;
    private TextView caseMarkNo;

    public CaseMarkPasteReadViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.casemark_paste_read_row);
        this.invoiceNo = itemView.findViewById(R.id.casemark_paste_read_row_invoice_no);
        this.caseMarkNo = itemView.findViewById(R.id.casemark_paste_read_row_casemark_no);
    }
}
