package jp.co.khwayz.eleEntExtManage.packing;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.PackingOuter;
import jp.co.khwayz.eleEntExtManage.R;

public class PackingListRecyclerViewAdapter extends RecyclerView.Adapter<PackingListViewHolder> {
    private List<PackingOuter> packingOuterList;
    private int selectedPosition = -1;
    private CancelButtonClickCallback callback;

    public PackingListRecyclerViewAdapter(List<PackingOuter> packingInfoList, CancelButtonClickCallback callback) {
        this.packingOuterList = packingInfoList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public PackingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.packing_list_row, parent,false);
        PackingListViewHolder holder = new PackingListViewHolder(view);
        if (clickListener != null) {
            view.setOnClickListener(v -> clickListener.onItemClick(holder));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackingListViewHolder holder, final int position) {
        PackingOuter item = this.packingOuterList.get(position);
        holder.getCno().setText(item.getNotationCno());
        holder.getFinalPackingAppearance().setText(item.getFinalPackingAppearance());
        holder.getLength().setText(String.valueOf(item.getLength()));
        holder.getWidth().setText(String.valueOf(item.getWidth()));
        holder.getHeight().setText(String.valueOf(item.getHeight()));
        holder.getWeight().setText(String.valueOf(item.getWeight()));
        holder.getInnerPackage().setText(String.valueOf(item.getInnerPackage()));
        holder.getButtonCancel().setOnClickListener( v -> cancelButton(position));

        if(position == selectedPosition) {
            holder.getRow().setBackgroundColor(Color.parseColor("#FF03A9F4"));
        } else {
            holder.getRow().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    private void cancelButton(int position) {
        if (callback != null) callback.onClicked(position);
    }

    @Override
    public int getItemCount() {
        return this.packingOuterList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(PackingListViewHolder holder);
    }

    public interface CancelButtonClickCallback {
        void onClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    public int getSelectedPosition(){
        return selectedPosition;
    }

    public void removeItem(int position) {
        if (position == -1) return;
        this.packingOuterList.remove(position);
        notifyDataSetChanged();
    }

    private OnItemClickListener clickListener = null;

}
