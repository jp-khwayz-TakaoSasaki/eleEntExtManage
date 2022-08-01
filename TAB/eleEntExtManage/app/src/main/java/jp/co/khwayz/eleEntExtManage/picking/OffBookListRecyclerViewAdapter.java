package jp.co.khwayz.eleEntExtManage.picking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;

public class OffBookListRecyclerViewAdapter extends RecyclerView.Adapter<OffBookListViewHolder> {
    private List<TagInfo> tagScanInfoList;

    public OffBookListRecyclerViewAdapter(List<TagInfo> tagScanInfoList) {
        this.tagScanInfoList = tagScanInfoList;
    }

    @NonNull
    @Override
    public OffBookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.off_book_row, parent,false);
        OffBookListViewHolder holder = new OffBookListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OffBookListViewHolder holder, final int position) {
        TagInfo scanItem = this.tagScanInfoList.get(position);
        holder.getOrderNo().setText(scanItem.getPlaceOrderNo());
        holder.getBranchNo().setText(String.format("%04d",scanItem.getBranchNo()));
    }

    @Override
    public int getItemCount() {
        return this.tagScanInfoList.size();
    }
}
