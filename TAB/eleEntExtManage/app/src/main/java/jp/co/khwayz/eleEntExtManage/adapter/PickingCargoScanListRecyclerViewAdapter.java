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
        holder.getBranchNo().setText(String.format("%04d",scanItem.getBranchNo()));
        holder.getPurchaseOrderNo().setText(scanItem.getReceiveOrderNo());
        holder.getItemCode().setText(scanItem.getItemCode());
        holder.getItemName().setText(scanItem.getItemName());
        holder.getShipmentQuantity().setText(String.format("%,.3f", scanItem.getIssueQuantity()));
        holder.getShipmentUnit().setText(scanItem.getUnit());
        holder.getStockQuantity().setText(String.format("%,.3f", scanItem.getStockQuantity()));
        holder.getPacking().setText(scanItem.getPackingType());
        holder.getLocationNo().setText(scanItem.getLocationNo());

        // 背景色変更
        if(scanItem.getPickingFlag().equals(Constants.FLAG_TRUE)) {
            holder.getRow().setBackgroundResource(R.color.colorCyan400A);
        } else {
            holder.getRow().setBackgroundResource(R.color.colorWhite);
        }

        // 単一選択用 選択時背景色変更
//        if(position == selectedPosition) {
//            holder.getRow().setBackgroundResource(R.color.colorCyan400A);
//        } else {
//            holder.getRow().setBackgroundResource(R.color.colorWhite);
//        }

        // 複数選択用処理 選択時背景色変更 TODO: 複数選択の場合下記コメントアウト削除
//        if(isSelectedItem(position)) {
//            holder.getRow().setBackgroundColor(Color.parseColor("#FF03A9F4"));
//        } else {
//            holder.getRow().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
//        }

//        holder.getRow().setBackgroundColor(this.selectPositionList.indexOf(position) >= 0 ? Color.YELLOW : Color.WHITE);
//        View.OnClickListener rowClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isAbleSelect) return;
//                if (selectPositionList.indexOf(position) >= 0) {
//                    selectPositionList.remove(selectPositionList.indexOf(position));
//                } else {
//                    selectPositionList.add(position);
//                }
//                notifyDataSetChanged();
//            }
//        };
//        holder.getRow().setOnClickListener(rowClickListener);
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
    public void setOnItemClickListener(PickingCargoScanListRecyclerViewAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    /* 選択位置設定(複数選択用) */
    public void addSelectedItemPosition(int position) {
        this.selectedItemPositionSet.add(position);
        notifyDataSetChanged();
    }

    /* 選択位置判定(複数選択用) */
    public boolean isSelectedItem(int position) {
        return this.selectedItemPositionSet.contains(position);
    }

    /* 選択解除(複数選択用) */
    public void removeSelectedItemPosition(int position) {
        this.selectedItemPositionSet.remove(position);
        notifyDataSetChanged();
    }

    /* リストアイテム削除(複数選択用) */
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
