package jp.co.khwayz.eleEntExtManage.casemark_print;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.Constants;

public class CaseMarkPrintRecyclerViewAdapter extends RecyclerView.Adapter<CaseMarkPrintViewHolder> {

    private List<CaseMarkPrintStatusInfo> invoiceInfoList;
    private int selectedPosition = -1;

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = (selectedPosition == adapterPosition) ? -1 : adapterPosition;
    }

    public int getSelectedPosition(){
        return selectedPosition;
    }

    public interface OnItemClickListener {
        void onItemClick(CaseMarkPrintViewHolder holder);
    }

    private OnItemClickListener clickListener;

    public CaseMarkPrintRecyclerViewAdapter(List<CaseMarkPrintStatusInfo> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    @NonNull
    @Override
    public CaseMarkPrintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.casemark_print_row, parent,false);
        CaseMarkPrintViewHolder holder = new CaseMarkPrintViewHolder(view);
        if (clickListener != null) {
            view.setOnClickListener(v -> clickListener.onItemClick(holder));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CaseMarkPrintViewHolder holder, final int position) {
        CaseMarkPrintStatusInfo invoiceInfo = this.invoiceInfoList.get(position);
        holder.getInvoiceNo().setText(invoiceInfo.getInvoiceNo());
        holder.getDestination().setText(invoiceInfo.getDestination());
        holder.getShipDate().setText(invoiceInfo.getShipDate());
        holder.getShippingMode().setText(invoiceInfo.getShippingMode());
        holder.getPrintStatus().setText(invoiceInfo.getPrintStatus().equals("0") ? Constants.CASEMARK_NOT_PRINTED : Constants.CASEMARK_PRINTED);

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
