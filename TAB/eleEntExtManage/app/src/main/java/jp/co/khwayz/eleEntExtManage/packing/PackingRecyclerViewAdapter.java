package jp.co.khwayz.eleEntExtManage.packing;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.PackingInfo;
import jp.co.khwayz.eleEntExtManage.R;

public class PackingRecyclerViewAdapter extends RecyclerView.Adapter<PackingViewHolder> {
    private List<PackingInfo> packingInfoList;
    private int selectedPosition = -1;

    public PackingRecyclerViewAdapter(List<PackingInfo> packingInfoList) {
        this.packingInfoList = packingInfoList;
    }

    @NonNull
    @Override
    public PackingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.packing_row, parent,false);
        PackingViewHolder holder = new PackingViewHolder(view);
        if (clickListener != null) {
            view.setOnClickListener(v -> clickListener.onItemClick(holder));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackingViewHolder holder, final int position) {
        PackingInfo item = this.packingInfoList.get(position);
        holder.getInvoiceNo().setText(item.getInvoiceNo());
        holder.getDestination().setText(item.getDestination());
        holder.getReplayDate().setText(item.getReplyDate());
        holder.getShipDate().setText(item.getShipDate());
        holder.getLineCount().setText(String.valueOf(item.getLineCount()));
        holder.getCool().setText(item.getCool());
        holder.getDangerous().setText(item.getDangerous());
        holder.getRemarks().setText(item.getRemarks());
        holder.getOnHold().setText(item.getOnHold());

        if(position == selectedPosition) {
            holder.getRow().setBackgroundColor(Color.parseColor("#FF03A9F4"));
        } else {
            holder.getRow().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return this.packingInfoList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(PackingViewHolder holder);
    }

    public void setOnItemClickListener(PackingRecyclerViewAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    private PackingRecyclerViewAdapter.OnItemClickListener clickListener = null;

}
