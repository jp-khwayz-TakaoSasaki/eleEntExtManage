package jp.co.khwayz.eleEntExtManage.cargostatus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;

public class CargoStatusRecyclerViewAdapter extends RecyclerView.Adapter<CargoStatusViewHolder> {
    private List<CargoStatusInfo> invoiceInfoList;
    private int selectedPosition = -1;

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    public CargoStatusRecyclerViewAdapter(List<CargoStatusInfo> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    @NonNull
    @Override
    public CargoStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cargo_status_row, parent,false);
        CargoStatusViewHolder holder = new CargoStatusViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CargoStatusViewHolder holder, final int position) {
        CargoStatusInfo invoiceInfo = this.invoiceInfoList.get(position);
        holder.getReceipt().setText(invoiceInfo.getReceipt());
        holder.getStorage().setText(invoiceInfo.getStorage());
        holder.getPicking().setText(invoiceInfo.getPicking());
        holder.getInvoiceNo().setText(invoiceInfo.getInvoiceNo());
        holder.getPacking().setText(invoiceInfo.getPacking());
        holder.getCaseMark().setText(invoiceInfo.getCaseMark());
        holder.getC_SlashNo().setText(invoiceInfo.getC_SlashNo());
        holder.getIssue().setText(invoiceInfo.getIssue());
    }

    @Override
    public int getItemCount() {
        return this.invoiceInfoList.size();
    }
}
