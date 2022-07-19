package jp.co.khwayz.eleEntExtManage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.InvoiceInfo;
import jp.co.khwayz.eleEntExtManage.R;

public class InvoiceRecyclerViewAdapter extends RecyclerView.Adapter<InvoiceViewHolder> {
    private List<InvoiceInfo> invoiceInfoList;

    public InvoiceRecyclerViewAdapter(List<InvoiceInfo> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_row, parent,false);
        return new InvoiceViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, final int position) {
        InvoiceInfo invoiceInfo = this.invoiceInfoList.get(position);
        holder.getSiNo().setText(String.valueOf(invoiceInfo.getSiNo()));
        holder.getInvoiceNo().setText(invoiceInfo.getInvoiceNo());
        holder.getDeadline().setText(invoiceInfo.getDeadline());
        holder.getDestination().setText(invoiceInfo.getDestination());
        holder.getPackingInstruction().setText(invoiceInfo.getPackingInstruction());
        holder.getPackingDate().setText(invoiceInfo.getPackingDate());
        holder.getSiNo().setVisibility(invoiceInfo.getIsSiNo() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return this.invoiceInfoList.size();
    }
}
