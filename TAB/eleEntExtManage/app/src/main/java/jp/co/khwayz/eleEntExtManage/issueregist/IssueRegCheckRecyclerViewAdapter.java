package jp.co.khwayz.eleEntExtManage.issueregist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import jp.co.khwayz.eleEntExtManage.MainActivity;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.ShortageCasemarkDialogFragment;

public class IssueRegCheckRecyclerViewAdapter extends RecyclerView.Adapter<IssueRegCheckViewHolder> {
    private List<IssueRegInvoiceSearchInfo> invoiceInfoList;
    private MainActivity activity;
    // モック用ケースマークリスト
    List<String> casemarkInfoList = new ArrayList<>();

    public IssueRegCheckRecyclerViewAdapter(List<IssueRegInvoiceSearchInfo> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    @NonNull
    @Override
    public IssueRegCheckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_confirm_row, parent,false);
        // MainActivityを取得
        activity = (MainActivity) parent.getContext();
        return new IssueRegCheckViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueRegCheckViewHolder holder, final int position) {
        // 情報取得
        IssueRegInvoiceSearchInfo invoiceInfo = this.invoiceInfoList.get(position);
        // Invoice番号
        holder.getInvoiceNo().setText(invoiceInfo.getInvoiceNo());
        // 仕向地
        holder.getDestination().setText(invoiceInfo.getDestination());
        // 出荷日
        holder.getShipDate().setText(invoiceInfo.getShipDate());
        // ケースマーク数
        long caseMarkCount = invoiceInfo.getCaseMarkTotal();
        holder.getCaseMarkCount().setText(String.format(Locale.JAPAN, "%,d", caseMarkCount));
        // スキャン数
        long scannedCount = invoiceInfo.getScannedCaseMarkCount();
        holder.getScanedCaseMarkCount().setText(String.format(Locale.JAPAN, "%,d", scannedCount));

        // Button Click
        holder.getCheckShortage().setOnClickListener(v -> {
            // 未読取のケースマークを取得する
            final List<IssueRegCaseMarkNo> list = invoiceInfo.getCaseMarkNoList().stream().filter(f -> !f.isRead()).collect(Collectors.toList());
            // ダイアログ表示
            ArrayList<String> paramList = new ArrayList<>();
            for (IssueRegCaseMarkNo item : list) {
                paramList.add(String.valueOf(item.getCaseMarkNo()));
            }
            final ShortageCasemarkDialogFragment shortageCasemarkDialogFragment = new ShortageCasemarkDialogFragment(invoiceInfo.getInvoiceNo(), paramList);
            shortageCasemarkDialogFragment.show(activity.getSupportFragmentManager(), "ShortageCasemarkDialogFragment");
        });
    }

    @Override
    public int getItemCount() {
        return this.invoiceInfoList.size();
    }
}
