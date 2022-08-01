package jp.co.khwayz.eleEntExtManage.picking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoInvoiceSearchInfo;

public class InvoiceSearchRecyclerViewAdapter extends RecyclerView.Adapter<InvoiceSearchViewHolder> {
    private List<SyukkoInvoiceSearchInfo> invoiceInfoList;
    private int selectedPosition = -1;

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    public interface OnItemClickListener {
        void onItemClick(InvoiceSearchViewHolder holder);
    }

    private OnItemClickListener clickListener;

    public InvoiceSearchRecyclerViewAdapter(List<SyukkoInvoiceSearchInfo> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    @NonNull
    @Override
    public InvoiceSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_search_row, parent,false);
        InvoiceSearchViewHolder holder = new InvoiceSearchViewHolder(view);
        if (clickListener != null) {
            view.setOnClickListener(v -> clickListener.onItemClick(holder));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceSearchViewHolder holder, final int position) {
        SyukkoInvoiceSearchInfo invoiceInfo = this.invoiceInfoList.get(position);
        holder.getInvoiceNo().setText(invoiceInfo.getInvoiceNo());
        holder.getDestination().setText(invoiceInfo.getDestination());
        holder.getReplayDate().setText(invoiceInfo.getListReplyDesiredDate());
        holder.getShipDate().setText(invoiceInfo.getIssueDate());
        holder.getLineCount().setText(String.format("%,d",invoiceInfo.getLineCount()));
        holder.getCool().setText(invoiceInfo.getTransportTemprature());
        holder.getDangerous().setText(invoiceInfo.getDangerousGoods());
        holder.getRemarks().setText(invoiceInfo.getRemarks());
        holder.getOnhold().setText(invoiceInfo.getPickingHoldFlag());

        if(position == selectedPosition) {
            holder.getRow().setBackgroundResource(R.color.colorCyan400A);
        } else {
            holder.getRow().setBackgroundResource(R.color.colorWhite);
        }
    }

    @Override
    public int getItemCount() {
        return this.invoiceInfoList.size();
    }

    public int getSelectedPosition() { return this.selectedPosition; }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }
}
