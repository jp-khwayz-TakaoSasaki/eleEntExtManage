package jp.co.khwayz.eleEntExtManage.issueregist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;

public class IssueShortageCasemarkRecyclerViewAdapter extends RecyclerView.Adapter<IssueShortageCasemarkViewHolder> {

    private List<String> casemarkList;
    private int selectedPosition = -1;

    public void setSelectedPosition(int adapterPosition) {
        selectedPosition = adapterPosition;
    }

    public IssueShortageCasemarkRecyclerViewAdapter(List<String> casemarkList) {
        this.casemarkList = casemarkList;
    }

    @NonNull
    @Override
    public IssueShortageCasemarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_shortage_casemark_row, parent,false);
        IssueShortageCasemarkViewHolder holder = new IssueShortageCasemarkViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull IssueShortageCasemarkViewHolder holder, final int position) {
        String casemarkNo = this.casemarkList.get(position);
        holder.getInvoiceNo().setText(casemarkNo);
    }

    @Override
    public int getItemCount() {
        return this.casemarkList.size();
    }
}
