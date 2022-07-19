package jp.co.khwayz.eleEntExtManage.packing;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackingListViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView cno;
    private TextView finalPackingAppearance;
    private TextView length;
    private TextView width;
    private TextView height;
    private TextView weight;
    private TextView innerPackage;
    private Button buttonCancel;

    public PackingListViewHolder(@NonNull View itemView) {
        super(itemView);
        row = itemView.findViewById(R.id.packing_list_row);
        cno = itemView.findViewById(R.id.packing_list_row_cno);
        finalPackingAppearance = itemView.findViewById(R.id.packing_list_row_final_packing_appearance);
        length = itemView.findViewById(R.id.packing_list_row_length);
        width = itemView.findViewById(R.id.packing_list_row_width);
        height = itemView.findViewById(R.id.packing_list_row_height);
        weight = itemView.findViewById(R.id.packing_list_row_weight);
        innerPackage = itemView.findViewById(R.id.packing_list_row_inner_package);
        buttonCancel = itemView.findViewById(R.id.packing_list_row_button_cancel);
    }
}
