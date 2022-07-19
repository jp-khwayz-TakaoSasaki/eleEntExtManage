package jp.co.khwayz.eleEntExtManage.casemark_paste;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;

public class CaseMarkPasteScanRecyclerViewAdapter extends RecyclerView.Adapter<CaseMarkPasteScanViewHolder> {

    private List<CaseMarkPasteScanInfo> tagScanInfoList;
    private int selectedPosition = -1;

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    public interface OnItemClickListener {
        void onItemClick(CaseMarkPasteScanViewHolder holder);
    }

    private OnItemClickListener clickListener;

    public CaseMarkPasteScanRecyclerViewAdapter(List<CaseMarkPasteScanInfo> tagScanInfoList) {
        this.tagScanInfoList = tagScanInfoList;
    }

    @NonNull
    @Override
    public CaseMarkPasteScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.casemark_paste_scan_row, parent,false);
        CaseMarkPasteScanViewHolder holder = new CaseMarkPasteScanViewHolder(view);
        if (clickListener != null) {
            view.setOnClickListener(v -> clickListener.onItemClick(holder));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CaseMarkPasteScanViewHolder holder, final int position) {
        CaseMarkPasteScanInfo tagScanInfo = this.tagScanInfoList.get(position);
        holder.getNo().setText(String.valueOf(tagScanInfo.getNo()));
        holder.getCaseMarkNo().setText(tagScanInfo.getCaseMarkNo());
        holder.getPurchaseOrderNo().setText(tagScanInfo.getPurchaseOrderNo());
        holder.getBranchNo().setText(tagScanInfo.getBranchNo());
        holder.getOrderNo().setText(tagScanInfo.getOrderNo());
        holder.getItemCode().setText(tagScanInfo.getItemCode());
        holder.getItemName().setText(tagScanInfo.getItemName());
        holder.getStock().setText(tagScanInfo.getStock());
        holder.getShipmentQuantity().setText(String.valueOf(tagScanInfo.getShipmentQuantity()));
        holder.getShipmentUnit().setText(String.valueOf(tagScanInfo.getShipmentUnit()));
        holder.getPackingForm().setText(tagScanInfo.getPackingForm());

        if(position == selectedPosition) {
            holder.getRow().setBackgroundColor(Color.parseColor("#FF03A9F4"));
        } else {
            holder.getRow().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return this.tagScanInfoList.size();
    }

    public void setOnItemClickListener(CaseMarkPasteScanRecyclerViewAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }
}
