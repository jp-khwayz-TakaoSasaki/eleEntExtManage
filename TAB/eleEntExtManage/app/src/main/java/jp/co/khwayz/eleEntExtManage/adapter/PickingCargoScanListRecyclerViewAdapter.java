package jp.co.khwayz.eleEntExtManage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoInvoiceDetailInfo;

public class PickingCargoScanListRecyclerViewAdapter extends RecyclerView.Adapter<TagInvoiceViewHolder> {
    private List<SyukkoInvoiceDetailInfo> tagScanInfoList;
    private List<Integer> selectPositionList = new ArrayList<>();
    private Set<Integer> selectedItemPositionSet = new ArraySet<>();
    private int readTotalCount = 0;
    private int selectedPosition = -1;
    private boolean isAbleSelect = true;

    public interface OnItemClickListener {
        void onItemClick(TagInvoiceViewHolder holder);
    }

    private PickingCargoScanListRecyclerViewAdapter.OnItemClickListener clickListener;

    public PickingCargoScanListRecyclerViewAdapter(List<SyukkoInvoiceDetailInfo> tagScanInfoList) {
        this.tagScanInfoList = tagScanInfoList;
    }

    public PickingCargoScanListRecyclerViewAdapter(List<SyukkoInvoiceDetailInfo> tagScanInfoList, boolean isAbleSelect) {
        this.tagScanInfoList = tagScanInfoList;
        this.isAbleSelect = isAbleSelect;
        this.readTotalCount = getItemCount();
    }

    @NonNull
    @Override
    public TagInvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_scan_invoice_row, parent,false);
        TagInvoiceViewHolder holder = new TagInvoiceViewHolder(view);
        view.setOnClickListener(v -> clickListener.onItemClick(holder));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TagInvoiceViewHolder holder, final int position) {
        SyukkoInvoiceDetailInfo scanItem = this.tagScanInfoList.get(position);
        holder.getOrderNo().setText(scanItem.getPlaceOrderNo());
        holder.getBranchNo().setText(String.format("%03d",scanItem.getBranchNo()));
        holder.getPurchaseOrderNo().setText(scanItem.getReceiveOrderNo());
        holder.getItemCode().setText(scanItem.getItemCode());
        holder.getItemName().setText(scanItem.getItemName());
        holder.getShipmentQuantity().setText(String.format("%,.3f", scanItem.getIssueQuantity()));
        holder.getShipmentUnit().setText(scanItem.getUnit());
        holder.getStockQuantity().setText(String.format("%,.3f", scanItem.getStockQuantity()));
        holder.getPacking().setText(scanItem.getPackingType());
        holder.getLocationNo().setText(scanItem.getLocationNo());

        // ???????????????
        if(scanItem.getPickingFlag().equals(Constants.FLAG_TRUE)) {
            holder.getRow().setBackgroundResource(R.color.colorCyan400A);
        } else {
            holder.getRow().setBackgroundResource(R.color.colorWhite);
        }
    }

    @Override
    public int getItemCount() {
        return this.tagScanInfoList.size();
    }

    public int getReadTotalCount() {
        return this.readTotalCount;
    }

    public boolean isSelected() {
        return selectedPosition != -1;
    }
    public SyukkoInvoiceDetailInfo getSelectedItem() {
        return this.tagScanInfoList.get(selectedPosition);
    }

    /* ??????????????????(???????????????) */
    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    /* ???????????????????????????(???????????????) */
    public void removeItemOne() {
        this.tagScanInfoList.remove(selectedPosition);
        this.selectedPosition = -1;
        notifyDataSetChanged();
    }

    /* ?????????????????? */
    public void setOnItemClickListener(PickingCargoScanListRecyclerViewAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    /* ??????????????????(???????????????) */
    public void addSelectedItemPosition(int position) {
        this.selectedItemPositionSet.add(position);
        notifyDataSetChanged();
    }

    /* ??????????????????(???????????????) */
    public boolean isSelectedItem(int position) {
        return this.selectedItemPositionSet.contains(position);
    }

    /* ????????????(???????????????) */
    public void removeSelectedItemPosition(int position) {
        this.selectedItemPositionSet.remove(position);
        notifyDataSetChanged();
    }

    /* ???????????????????????????(???????????????) */
    public void removeItem() {
        ArrayList<SyukkoInvoiceDetailInfo> delList = new ArrayList<>();
        for(Integer i : this.selectedItemPositionSet) {
            delList.add(this.tagScanInfoList.get(i));
        }
        this.tagScanInfoList.removeAll(delList);
        this.selectedItemPositionSet.clear();
        notifyDataSetChanged();
    }
}
