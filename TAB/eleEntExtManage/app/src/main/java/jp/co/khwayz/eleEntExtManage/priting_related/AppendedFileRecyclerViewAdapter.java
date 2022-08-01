package jp.co.khwayz.eleEntExtManage.priting_related;

import android.print.PrintManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.instr_cfm.InvoiceTempInfo;

public class AppendedFileRecyclerViewAdapter extends RecyclerView.Adapter<AppendedFileViewHolder> {
    private ArrayList<InvoiceTempInfo> mInvoiceTempList;
    private PrintManager printManager;
    private String jobName;

    public interface OnItemClickListener {
        void onPrintButtonClick(InvoiceTempInfo info);
        void onDownloadButtonClick(InvoiceTempInfo info);
    }

    private OnItemClickListener mClickListener;

    public AppendedFileRecyclerViewAdapter(ArrayList<InvoiceTempInfo> invoiceTempList, OnItemClickListener onItemClickListener) {
        mInvoiceTempList = invoiceTempList;
        mClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AppendedFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appended_file_row, parent,false);
        return new AppendedFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppendedFileViewHolder holder, final int position) {
        InvoiceTempInfo info = this.mInvoiceTempList.get(position);
        holder.getAppendedFileName().setText(info.getFileName());
        // Downloadボタン押下
        holder.getButtonDownload().setOnClickListener(v -> mClickListener.onDownloadButtonClick(info));
        // 印刷ボタン押下
        holder.getButtonPrinting().setOnClickListener(v -> mClickListener.onPrintButtonClick(info));
    }

    @Override
    public int getItemCount() {
        return this.mInvoiceTempList.size();
    }
}
