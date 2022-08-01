package jp.co.khwayz.eleEntExtManage.casemark_paste;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;

public class CaseMarkPasteReadRecyclerViewAdapter extends RecyclerView.Adapter<CaseMarkPasteReadViewHolder> {

    private List<CaseMarkPasteReadInfo> invoiceInfoList;
    private int selectedPosition = -1;
    protected OnItemLongClickListener longClickListener = null;

    public interface OnItemLongClickListener {
        boolean onItemLongClick(CaseMarkPasteReadViewHolder holder);
    }

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    public CaseMarkPasteReadRecyclerViewAdapter(List<CaseMarkPasteReadInfo> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    @NonNull
    @Override
    public CaseMarkPasteReadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.casemark_paste_read_row, parent,false);
        CaseMarkPasteReadViewHolder holder = new CaseMarkPasteReadViewHolder(view);
        if (longClickListener != null) {
            view.setOnLongClickListener(v -> longClickListener.onItemLongClick(holder));
        }        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CaseMarkPasteReadViewHolder holder, final int position) {
        CaseMarkPasteReadInfo invoiceInfo = this.invoiceInfoList.get(position);
        holder.getInvoiceNo().setText(invoiceInfo.getInvoiceNo());
        holder.getCaseMarkNo().setText(String.valueOf(invoiceInfo.getCaseMarkNo()));

        // 行選択色
        if(position == selectedPosition) {
            holder.getRow().setBackgroundColor(Color.parseColor("#FF03A9F4"));
        } else {
            holder.getRow().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return this.invoiceInfoList.size();
    }

    public void setOnItemLongClickListener(CaseMarkPasteReadRecyclerViewAdapter.OnItemLongClickListener listener) {
        longClickListener = listener;
    }

}
