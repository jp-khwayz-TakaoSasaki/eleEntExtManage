package jp.co.khwayz.eleEntExtManage.cargostatus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;

public class CargoStatusRecyclerViewAdapter extends RecyclerView.Adapter<CargoStatusViewHolder> {
    private List<CargoStatusInfo> mCargoStatusInfoList;

    public CargoStatusRecyclerViewAdapter(List<CargoStatusInfo> cargoStatusInfoList) {
        this.mCargoStatusInfoList = cargoStatusInfoList;
    }

    @NonNull
    @Override
    public CargoStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cargo_status_row, parent,false);
        return new CargoStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CargoStatusViewHolder holder, final int position) {
        CargoStatusInfo info = this.mCargoStatusInfoList.get(position);
        holder.getReceipt().setText(info.getReceiptDateSlash());
        holder.getStorage().setText(info.getStorage());
        holder.getPicking().setText(info.getPicking());
        holder.getInvoiceNo().setText(info.getInvoiceNo());
        holder.getPacking().setText(info.getPacking());
        holder.getCaseMark().setText(info.getCaseMark());
        holder.getC_SlashNo().setText(info.getCsNo());
        holder.getIssue().setText(info.getIssue());
    }

    @Override
    public int getItemCount() {
        return this.mCargoStatusInfoList.size();
    }
}
