package jp.co.khwayz.eleEntExtManage.packing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.PackingScanInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.Constants;

public class PackingScanRecyclerViewAdapter extends RecyclerView.Adapter<PackingScanViewHolder> {
    private List<PackingScanInfo> packingScanInfoList;
    private int selectedPosition = -1;
    private SinglePackagingBtCallback callback;

    public PackingScanRecyclerViewAdapter(List<PackingScanInfo> scanList, SinglePackagingBtCallback callback) {
        this.packingScanInfoList = scanList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public PackingScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.packing_scan_row, parent,false);
        PackingScanViewHolder holder = new PackingScanViewHolder(view);
        if (clickListener != null) {
            view.setOnClickListener(v -> clickListener.onItemClick(holder));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackingScanViewHolder holder, final int position) {
        PackingScanInfo item = this.packingScanInfoList.get(position);
        holder.getNo().setText(String.valueOf(item.getNo()));
        holder.getOverPack().setText(item.getOverPack().equals("0") ? "0" : item.getOverPack());
        holder.getPurchaseOrderNo().setText(item.getPurchaseOrderNo());
        holder.getBranchNo().setText(String.format("%04d",item.getBranchNo()));
        holder.getOrderNo().setText(item.getOrderNo());
        holder.getItemCode().setText(item.getItemCode());
        holder.getItemName().setText(item.getItemName());
        holder.getStockQuantity().setText(String.format("%,.3f",item.getStockQuantity()));
        holder.getShipmentQuantity().setText(String.format("%,.3f",item.getShipmentQuantity()));
        holder.getShipmentQuantityTotal().setText(String.format("%,.3f",item.getShipmentQuantityTotal()));
        holder.getShipmentUnit().setText(item.getShipmentUnit());
        holder.getReceiptAppearance().setText(item.getReceiptAppearance());
        holder.getBundledNumber().setText(item.getBundledNumber());
        holder.getReceiptDay().setText(item.getReceiptDay());

        // 単独梱包ボタン色変更
        if (item.getSinglePackFlag().equals(Constants.FLAG_TRUE)) {
            holder.getButtonSinglePackaging().setBackgroundResource(R.drawable.button_round_orange_2);
        } else {
            holder.getButtonSinglePackaging().setBackgroundResource(R.drawable.button_round_blue_2);
        }

        holder.getButtonSinglePackaging().setOnClickListener(v -> singlePackagingButton(position));

        // 背景色変更
        if(item.getOnSelectFlag().equals(Constants.FLAG_TRUE)) {
            holder.getRow().setBackgroundResource(R.color.colorCyan400A);
        } else {
            holder.getRow().setBackgroundResource(R.color.colorWhite);
        }
    }

    private void singlePackagingButton(int position) {
        if (this.callback != null) callback.onClicked(position);
    }

    @Override
    public int getItemCount() {
        return this.packingScanInfoList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(PackingScanViewHolder holder);
    }

    public interface SinglePackagingBtCallback {
        void onClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    private OnItemClickListener clickListener = null;

}
