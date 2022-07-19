package jp.co.khwayz.eleEntExtManage.issueregist;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class IssueShortageCasemarkViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView invoiceNo;

    public IssueShortageCasemarkViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.issue_shortage_casemark_row);
        this.invoiceNo = itemView.findViewById(R.id.issue_shortage_casemark_row_inovice);
    }
}
