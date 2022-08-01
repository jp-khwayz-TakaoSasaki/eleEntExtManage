package jp.co.khwayz.eleEntExtManage.issueregist;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class IssueRegCheckViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView invoiceNo;
    private TextView destination;
    private TextView shipDate;
    private TextView caseMarkCount;
    private TextView scanedCaseMarkCount;
    private Button checkShortage;

    public IssueRegCheckViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.issue_comfirm_row);
        this.invoiceNo = itemView.findViewById(R.id.issue_comfirm_row_invoice_no);
        this.destination = itemView.findViewById(R.id.issue_comfirm_row_destination);
        this.shipDate = itemView.findViewById(R.id.issue_comfirm_row_ship_date);
        this.caseMarkCount = itemView.findViewById(R.id.issue_comfirm_row_casemark_count);
        this.scanedCaseMarkCount = itemView.findViewById(R.id.issue_comfirm_row_readed_casemark_count);
        this.checkShortage = itemView.findViewById(R.id.bt_issue_comfirm_row_check_shortage);
    }
}
