package jp.co.khwayz.eleEntExtManage.adapter;

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
public class ReaderViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView readerName;

    public ReaderViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.reader_row);
        this.readerName = itemView.findViewById(R.id.reader_name);
    }
}
