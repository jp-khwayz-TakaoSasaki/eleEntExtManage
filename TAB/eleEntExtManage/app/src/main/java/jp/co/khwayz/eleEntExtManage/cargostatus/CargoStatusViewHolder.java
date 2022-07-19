package jp.co.khwayz.eleEntExtManage.cargostatus;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class CargoStatusViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView receipt;
    private TextView storage;
    private TextView picking;
    private TextView invoiceNo;
    private TextView packing;
    private TextView caseMark;
    private TextView c_SlashNo;
    private TextView issue;

    public CargoStatusViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.cargo_status_row);
        this.receipt = itemView.findViewById(R.id.tv_receipt);
        this.storage = itemView.findViewById(R.id.tv_storage);
        this.picking = itemView.findViewById(R.id.tv_picking);
        this.invoiceNo = itemView.findViewById(R.id.tv_invoice_no);
        this.packing = itemView.findViewById(R.id.tv_packing);
        this.caseMark = itemView.findViewById(R.id.tv_case_mark);
        this.c_SlashNo = itemView.findViewById(R.id.tv_c_slash_no);
        this.issue = itemView.findViewById(R.id.tv_issue);
    }
}
