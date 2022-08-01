package jp.co.khwayz.eleEntExtManage.casemark_paste;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.Constants;

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
        holder.getCaseMarkNo().setText(String.valueOf(tagScanInfo.getCaseMarkNo()));
        holder.getPurchaseOrderNo().setText(tagScanInfo.getPurchaseOrderNo());
        holder.getBranchNo().setText(String.format("%04d",tagScanInfo.getBranchNo()));
        holder.getOrderNo().setText(tagScanInfo.getOrderNo());
        holder.getItemCode().setText(tagScanInfo.getItemCode());
        holder.getItemName().setText(tagScanInfo.getItemName());
        holder.getStock().setText(String.format("%,.3f",tagScanInfo.getStock()));
        holder.getShipmentQuantity().setText(String.format("%,.3f",tagScanInfo.getShipmentQuantity()));
        holder.getShipmentUnit().setText(tagScanInfo.getShipmentUnit());
        holder.getPackingForm().setText(tagScanInfo.getPackingForm());

        // 背景色変更
        if(tagScanInfo.getOnSelectFlag().equals(Constants.FLAG_TRUE)) {
            holder.getRow().setBackgroundResource(R.color.colorCyan400A);
        } else {
            holder.getRow().setBackgroundResource(R.color.colorWhite);
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
