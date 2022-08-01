package jp.co.khwayz.eleEntExtManage.instr_cfm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;


public class CheckShippingInstructionsRecyclerViewAdapter extends RecyclerView.Adapter<CheckShippingInstructionsViewHolder> {

    private List<ShippingInstructionsInfo> invoiceInfoList;

    public CheckShippingInstructionsRecyclerViewAdapter(List<ShippingInstructionsInfo> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    @NonNull
    @Override
    public CheckShippingInstructionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_shipping_instructions_row, parent,false);
        return new CheckShippingInstructionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckShippingInstructionsViewHolder holder, final int position) {
        ShippingInstructionsInfo invoiceInfo = this.invoiceInfoList.get(position);
        holder.getNo().setText(invoiceInfo.getNo());
        holder.getOrderNo().setText(invoiceInfo.getOrderNo());
        holder.getShippingInstructions().setText(invoiceInfo.getShippingInstructions());
    }

    @Override
    public int getItemCount() {
        return this.invoiceInfoList.size();
    }
}
