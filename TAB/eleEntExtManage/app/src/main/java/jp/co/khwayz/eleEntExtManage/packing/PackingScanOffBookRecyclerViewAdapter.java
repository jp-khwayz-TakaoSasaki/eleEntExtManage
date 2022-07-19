package jp.co.khwayz.eleEntExtManage.packing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;

public class PackingScanOffBookRecyclerViewAdapter extends RecyclerView.Adapter<PackingScanOffBookViewHolder> {
    private List<TagInfo> TagInfoList;
    private int readTotalCount = 0;
    private int selectedPosition = -1;
    private boolean isAbleSelect = true;

    public interface OnItemClickListener {
        void onItemClick(PackingScanOffBookViewHolder holder);
    }

    private OnItemClickListener clickListener;

    public PackingScanOffBookRecyclerViewAdapter(List<TagInfo> TagInfoList) {
        this.TagInfoList = TagInfoList;
    }

    @NonNull
    @Override
    public PackingScanOffBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.off_book_row, parent,false);
        PackingScanOffBookViewHolder holder = new PackingScanOffBookViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackingScanOffBookViewHolder holder, final int position) {
        TagInfo scanItem = this.TagInfoList.get(position);

        holder.getOrderNo().setText(scanItem.getPlaceOrderNo());
        holder.getBranchNo().setText(String.valueOf(String.format("%04d",scanItem.getBranchNo())));

        // 単一選択用 選択時背景色変更
//        if(position == selectedPosition) {
//            holder.getRow().setBackgroundColor(Color.parseColor("#FF03A9F4"));
//        } else {
//            holder.getRow().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
//        }
    }

    @Override
    public int getItemCount() {
        return this.TagInfoList.size();
    }

    public int getReadTotalCount() {
        return this.readTotalCount;
    }

    public TagInfo getSelectedItem() {
        return this.TagInfoList.get(selectedPosition);
    }

    /* 選択位置設定(単一選択用) */
    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    /* リストアイテム削除(単一選択用) */
    public void removeItemOne() {
        this.TagInfoList.remove(selectedPosition);
        this.selectedPosition = -1;
        notifyDataSetChanged();
    }

    /* リスナー設定 */
    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }
}
