package jp.co.khwayz.eleEntExtManage.priting_related;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;
import lombok.Getter;

@Getter
public class AppendedFileViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout row;
    private TextView appendedFileName;
    private Button buttonDownload;
    private Button buttonPrinting;

    public AppendedFileViewHolder(@NonNull View itemView) {
        super(itemView);
        this.row = itemView.findViewById(R.id.appended_files_row);
        this.appendedFileName = itemView.findViewById(R.id.file_name);
        this.buttonDownload = itemView.findViewById(R.id.button_download);
        this.buttonPrinting = itemView.findViewById(R.id.button_print);
    }

}
