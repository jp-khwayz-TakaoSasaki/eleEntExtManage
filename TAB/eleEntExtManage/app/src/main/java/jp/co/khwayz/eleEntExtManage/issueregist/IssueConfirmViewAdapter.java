package jp.co.khwayz.eleEntExtManage.issueregist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.InvoiceInfo;
import jp.co.khwayz.eleEntExtManage.MainActivity;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.ShortageCasemarkDialogFragment;

public class IssueConfirmViewAdapter extends RecyclerView.Adapter<IssueConfirmViewHolder> {
    private List<InvoiceInfo> invoiceInfoList;
    private MainActivity activity;
    // モック用ケースマークリスト
    List<String> casemarkInfoList = new ArrayList<>();

    public IssueConfirmViewAdapter(List<InvoiceInfo> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    @NonNull
    @Override
    public IssueConfirmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_confirm_row, parent,false);
        // MainActivityを取得
        activity = (MainActivity) parent.getContext();
        return new IssueConfirmViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueConfirmViewHolder holder, final int position) {
        InvoiceInfo invoiceInfo = this.invoiceInfoList.get(position);
        holder.getInvoiceNo().setText(invoiceInfo.getInvoiceNo());
        holder.getDestination().setText(invoiceInfo.getDestination());
        holder.getShipDate().setText(invoiceInfo.getShipDate());
        holder.getCaseMarkCount().setText(String.valueOf(invoiceInfo.getCaseMarkCount()));
        holder.getScanedCaseMarkCount().setText(String.valueOf(invoiceInfo.getScanedCaseMarkCount()));

        // モック用ケースマークリスト
        casemarkInfoList.add("1");
        casemarkInfoList.add("10");
        casemarkInfoList.add("20");
        casemarkInfoList.add("30");
        casemarkInfoList.add("40");
        casemarkInfoList.add("50");
        casemarkInfoList.add("60");
        casemarkInfoList.add("70");
        casemarkInfoList.add("80");
        casemarkInfoList.add("90");
        casemarkInfoList.add("91");
        casemarkInfoList.add("92");
        casemarkInfoList.add("93");
        casemarkInfoList.add("94");
        casemarkInfoList.add("95");
        casemarkInfoList.add("96");
        casemarkInfoList.add("97");

        holder.getCheckShortage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShortageCasemarkDialogFragment shortageCasemarkDialogFragment = new ShortageCasemarkDialogFragment(casemarkInfoList);
                shortageCasemarkDialogFragment.show(activity.getSupportFragmentManager(), "ShortageCasemarkDialogFragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.invoiceInfoList.size();
    }
}
