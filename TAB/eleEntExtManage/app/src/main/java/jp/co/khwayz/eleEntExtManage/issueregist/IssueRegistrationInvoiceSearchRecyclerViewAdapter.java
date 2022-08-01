package jp.co.khwayz.eleEntExtManage.issueregist;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;

public class IssueRegistrationInvoiceSearchRecyclerViewAdapter extends RecyclerView.Adapter<IssueRegistrationInvoiceSearchViewHolder> {

    private List<IssueRegInvoiceSearchInfo> mDataList;
    private int mSelectPosition = RecyclerView.NO_POSITION;

    public IssueRegistrationInvoiceSearchRecyclerViewAdapter(List<IssueRegInvoiceSearchInfo> issueRegInvoiceSearchInfoList) {
        this.mDataList = issueRegInvoiceSearchInfoList;
    }

    @NonNull
    @Override
    public IssueRegistrationInvoiceSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_registration_invoice_search_row, parent,false);
        return new IssueRegistrationInvoiceSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueRegistrationInvoiceSearchViewHolder holder, final int position) {
        IssueRegInvoiceSearchInfo invoiceInfo = this.mDataList.get(position);
        holder.getInvoiceNo().setText(invoiceInfo.getInvoiceNo());
        holder.getDestination().setText(invoiceInfo.getDestination());
        holder.getShipDate().setText(invoiceInfo.getShipDate());

        if(position == mSelectPosition) {
            holder.getRow().setBackgroundColor(Color.parseColor("#FF03A9F4"));
        } else {
            holder.getRow().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }

        // Clickイベントで選択された明細のpositionを保持する
        View.OnClickListener rowClickListener = view -> {
            // 前回Clickされた明細の変更通知
            if (mSelectPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(mSelectPosition);
            }

            // 今回Clickされた明細の変更通知
            mSelectPosition = position;
            notifyItemChanged(mSelectPosition);
        };
        holder.getRow().setOnClickListener(rowClickListener);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 選択された明細のpositionを取得
     * @return 選択された明細のposition
     */
    public int getSelectedItemPosition() {
        return mSelectPosition;
    }

    /**
     * 選択する明細を設定
     * @param position 選択する明細の位置
     */
    public void setSelectItemPosition(int position) {
        // 明細数よりも位置が超えている場合は何もしない
        if (mDataList.size() <= position) { return; }
        // 明細を選択して通知
        this.mSelectPosition = position;
        notifyDataSetChanged();
    }

    /**
     * 選択された明細を取得
     * @return 選択された明細
     */
    public IssueRegInvoiceSearchInfo getSelectedItem() {
        // 未選択はnullを返す
        if (this.mSelectPosition != RecyclerView.NO_POSITION) {
            return mDataList.get(this.mSelectPosition);
        }
        return null;
    }
}
