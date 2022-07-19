package jp.co.khwayz.eleEntExtManage.packing;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;

public class OverPackListInfoRecyclerViewAdapter extends RecyclerView.Adapter<OverPackListInfoViewHolder> {

    private List<OverPackListInfo> overPackListInfoList;
    private List<Integer> selectPositionList = new ArrayList<>();
    private int selectedPosition = -1;

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    public interface OnItemClickListener {
        void onItemClick(OverPackListInfoViewHolder holder);
    }

    private OnItemClickListener clickListener;

    public OverPackListInfoRecyclerViewAdapter(List<OverPackListInfo> overPackListInfoList) {
        this.overPackListInfoList = overPackListInfoList;
    }

    @NonNull
    @Override
    public OverPackListInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.over_pack_row, parent,false);
        OverPackListInfoViewHolder holder = new OverPackListInfoViewHolder(view);
        if (clickListener != null) {
            view.setOnClickListener(v -> clickListener.onItemClick(holder));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OverPackListInfoViewHolder holder, final int position) {
        OverPackListInfo overPackListInfoList = this.overPackListInfoList.get(position);
        holder.getOverPackNo().setText(overPackListInfoList.getOverPackNo().equals("0") ? "" : overPackListInfoList.getOverPackNo());
        holder.getPurchaseOrderNo().setText(overPackListInfoList.getPurchaseOrderNo());
        holder.getBranchNo().setText(String.format("%04d",Integer.parseInt(overPackListInfoList.getBranchNo())));
        holder.getOrderNo().setText(overPackListInfoList.getOrderNo());
        holder.getBundledNo().setText(overPackListInfoList.getBundledNo());

        /*** 複数選択の残骸 START
        if(overPackListInfoList.isSelectFlg()) {
            holder.getRow().setBackgroundColor(Color.parseColor("#FF03A9F4"));
        } else {
            holder.getRow().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        　複数選択の残骸 END ***/

        // 行選択色
        if(position == selectedPosition) {
            holder.getRow().setBackgroundColor(Color.parseColor("#FF03A9F4"));
        } else {
            holder.getRow().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return this.overPackListInfoList.size();
    }

    public void setOnItemClickListener(OverPackListInfoRecyclerViewAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }
}
