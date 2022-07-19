package jp.co.khwayz.eleEntExtManage.picking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.TagScanInvoice;

public class OffBookRecyclerViewAdapter extends RecyclerView.Adapter<OffBookViewHolder> {
    private List<TagScanInvoice> tagScanInfoList;
    private int readTotalCount = 0;
    private int selectedPosition = -1;
    private boolean isAbleSelect = true;

    public interface OnItemClickListener {
        void onItemClick(OffBookViewHolder holder);
    }

    private OffBookRecyclerViewAdapter.OnItemClickListener clickListener;

    public OffBookRecyclerViewAdapter(List<TagScanInvoice> tagScanInfoList) {
        this.tagScanInfoList = tagScanInfoList;
    }

    public OffBookRecyclerViewAdapter(List<TagScanInvoice> tagScanInfoList, boolean isAbleSelect) {
        this.tagScanInfoList = tagScanInfoList;
        this.isAbleSelect = isAbleSelect;
        this.readTotalCount = getItemCount();
    }

    @NonNull
    @Override
    public OffBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.off_book_row, parent,false);
        OffBookViewHolder holder = new OffBookViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OffBookViewHolder holder, final int position) {
        TagScanInvoice scanItem = this.tagScanInfoList.get(position);

        holder.getOrderNo().setText(scanItem.getOrderNo());
        holder.getBranchNo().setText(String.valueOf(scanItem.getBranchNo()));

        // 単一選択用 選択時背景色変更
//        if(position == selectedPosition) {
//            holder.getRow().setBackgroundColor(Color.parseColor("#FF03A9F4"));
//        } else {
//            holder.getRow().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
//        }
    }

    @Override
    public int getItemCount() {
        return this.tagScanInfoList.size();
    }

    public int getReadTotalCount() {
        return this.readTotalCount;
    }

    public TagScanInvoice getSelectedItem() {
        return this.tagScanInfoList.get(selectedPosition);
    }

    /* 選択位置設定(単一選択用) */
    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    /* リストアイテム削除(単一選択用) */
    public void removeItemOne() {
        this.tagScanInfoList.remove(selectedPosition);
        this.selectedPosition = -1;
        notifyDataSetChanged();
    }

    /* リスナー設定 */
    public void setOnItemClickListener(OffBookRecyclerViewAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }
}
