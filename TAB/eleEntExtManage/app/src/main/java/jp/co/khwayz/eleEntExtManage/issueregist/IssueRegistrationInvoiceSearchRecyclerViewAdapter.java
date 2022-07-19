package jp.co.khwayz.eleEntExtManage.issueregist;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.InvoiceInfo;
import jp.co.khwayz.eleEntExtManage.R;

public class IssueRegistrationInvoiceSearchRecyclerViewAdapter extends RecyclerView.Adapter<IssueRegistrationInvoiceSearchViewHolder> {

    private List<InvoiceInfo> invoiceInfoList;
    private int selectedPosition = -1;

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    public interface OnItemClickListener {
        void onItemClick(IssueRegistrationInvoiceSearchViewHolder holder);
    }

    private OnItemClickListener clickListener;

    public IssueRegistrationInvoiceSearchRecyclerViewAdapter(List<InvoiceInfo> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    @NonNull
    @Override
    public IssueRegistrationInvoiceSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_registration_invoice_search_row, parent,false);
        IssueRegistrationInvoiceSearchViewHolder holder = new IssueRegistrationInvoiceSearchViewHolder(view);
        if (clickListener != null) {
            view.setOnClickListener(v -> clickListener.onItemClick(holder));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull IssueRegistrationInvoiceSearchViewHolder holder, final int position) {
        InvoiceInfo invoiceInfo = this.invoiceInfoList.get(position);
        holder.getInvoiceNo().setText(invoiceInfo.getInvoiceNo());
        holder.getDestination().setText(invoiceInfo.getDestination());
        holder.getShipDate().setText(invoiceInfo.getShipDate());

        if(position == selectedPosition) {
            holder.getRow().setBackgroundColor(Color.parseColor("#FF03A9F4"));
        } else {
            holder.getRow().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return this.invoiceInfoList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }
}
