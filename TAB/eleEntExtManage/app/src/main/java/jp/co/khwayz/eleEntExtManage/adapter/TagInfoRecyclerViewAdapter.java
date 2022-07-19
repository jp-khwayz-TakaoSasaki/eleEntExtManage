package jp.co.khwayz.eleEntExtManage.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.TagScanInfo;

public class TagInfoRecyclerViewAdapter extends RecyclerView.Adapter<TagInfoViewHolder> {
    private List<TagScanInfo> tagScanInfoList;
    private List<Integer> selectPositionList = new ArrayList<>();
    private boolean isAbleSelect = true;

    public TagInfoRecyclerViewAdapter(List<TagScanInfo> tagScanInfoList) {
        this.tagScanInfoList = tagScanInfoList;
    }

    public TagInfoRecyclerViewAdapter(List<TagScanInfo> tagScanInfoList, boolean isAbleSelect) {
        this.tagScanInfoList = tagScanInfoList;
        this.isAbleSelect = isAbleSelect;
    }

    @NonNull
    @Override
    public TagInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_scan_row, parent,false);
        return new TagInfoViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TagInfoViewHolder holder, final int position) {
        TagScanInfo tagScanInfo = this.tagScanInfoList.get(position);
        holder.getNo().setText(String.valueOf(tagScanInfo.getNo()));
        holder.getOrderNo().setText(String.valueOf(tagScanInfo.getOrderNo()));
        holder.getPurchaseOrderNo().setText(String.valueOf(tagScanInfo.getPurchaseOrderNo()));
        holder.getItem().setText(String.valueOf(tagScanInfo.getItem()));
        holder.getShipmentQuantity().setText(String.valueOf(tagScanInfo.getShipmentQuantity()));
        holder.getShipmentUnit().setText(String.valueOf(tagScanInfo.getShipmentUnit()));
        holder.getPackingQuantity().setText(String.valueOf(tagScanInfo.getPackingQuantity()));
        holder.getPackingType().setText(String.valueOf(tagScanInfo.getPackingType()));
        holder.getLocationNo().setText(String.valueOf(tagScanInfo.getLocationNo()));
        holder.getCNo().setText(String.valueOf(tagScanInfo.getCNo()));
        holder.getRow().setBackgroundColor(this.selectPositionList.indexOf(position) >= 0 ? Color.YELLOW : Color.WHITE);
        View.OnClickListener rowClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAbleSelect) return;
                if (selectPositionList.indexOf(position) >= 0) {
                    selectPositionList.remove(selectPositionList.indexOf(position));
                } else {
                    selectPositionList.add(position);
                }
                notifyDataSetChanged();
            }
        };
        holder.getRow().setOnClickListener(rowClickListener);
    }

    @Override
    public int getItemCount() {
        return this.tagScanInfoList.size();
    }

    public List<TagScanInfo> getSelectedTagInfo() {
        List<TagScanInfo> result = new ArrayList<>();
        for (int i = 0; i < this.tagScanInfoList.size(); i++) {
            if (this.selectPositionList.indexOf(i) < 0) continue;
            result.add(this.tagScanInfoList.get(i));
        }
        return result;
    }

    public boolean isAllSelect() {
        return this.selectPositionList.size() >= this.tagScanInfoList.size();
    }

    public void removeSelectTagInfo() {
        List<TagScanInfo> result = new ArrayList<>();
        for (int i = 0; i < this.tagScanInfoList.size(); i++) {
            if (this.selectPositionList.indexOf(i) >= 0) continue;
            result.add(this.tagScanInfoList.get(i));
        }
        this.tagScanInfoList = result;
        notifyDataSetChanged();
    }
}
