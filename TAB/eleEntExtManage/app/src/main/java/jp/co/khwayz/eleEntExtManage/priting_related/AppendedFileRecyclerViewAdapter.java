package jp.co.khwayz.eleEntExtManage.priting_related;

import android.print.PrintAttributes;
import android.print.PrintManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.pdf_print.CustomDocumentPrintAdapter;

public class AppendedFileRecyclerViewAdapter extends RecyclerView.Adapter<AppendedFileViewHolder> {
    private List<String> appendedFiles;
    private int selectedPosition = -1;
    private PrintManager printManager;
    private String jobName;

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    public interface OnItemClickListener {
        void onItemClick(AppendedFileViewHolder holder);
    }

    private OnItemClickListener clickListener;

    public AppendedFileRecyclerViewAdapter(List<String> appendedFiles) {
        this.appendedFiles = appendedFiles;
    }

    @NonNull
    @Override
    public AppendedFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appended_file_row, parent,false);
        AppendedFileViewHolder holder = new AppendedFileViewHolder(view);
        if (clickListener != null) {
            view.setOnClickListener(v -> clickListener.onItemClick(holder));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppendedFileViewHolder holder, final int position) {
        String fileName = this.appendedFiles.get(position);
        holder.getAppendedFileName().setText(fileName);
        holder.getButtonDownload().setOnClickListener(v -> {
            // ダウンロード処理を実装
        });
        holder.getButtonPrinting().setOnClickListener(v -> printing());
    }

    // 添付ファイル印刷
    private void printing() {
        // TODO:実際は選択された行のファイルを指定すること
        final String filePathText = "/storage/emulated/0/Download/testCaseMark.pdf";

        CustomDocumentPrintAdapter pda = new CustomDocumentPrintAdapter(filePathText);

        // 印刷設定
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
        builder.setDuplexMode(PrintAttributes.DUPLEX_MODE_LONG_EDGE);
        printManager.print(this.jobName, pda, builder.build()); //
    }

    public void setPrintManager(PrintManager manager) {
        this.printManager = manager;
    }

    public void setJobName(String name) {
        this.jobName = name;
    }

    @Override
    public int getItemCount() {
        return this.appendedFiles.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }
}
