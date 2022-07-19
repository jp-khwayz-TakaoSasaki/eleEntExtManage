package jp.co.khwayz.eleEntExtManage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;

public class PairingReaderRecyclerAdapter extends BaseRecyclerViewAdapter<PairingReaderListViewHolder, String> {

    // region [ constructor ]
    public PairingReaderRecyclerAdapter(List<String> dataSet) {
        this.mLocalDataSet = dataSet;
    }
    // endregion

    // region [ Override ]
    @NonNull
    @Override
    public PairingReaderListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_row_pairing_reader, viewGroup, false);
        return new PairingReaderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PairingReaderListViewHolder holder, int position) {
        // リーダー名取得
        String readerName = mLocalDataSet.get(position);

        // リーダー名表示
        holder.getReaderName().setText(readerName);

        // 選択されている場合は背景色を設定する
        ConstraintLayout rowContainer = holder.getRowContainer();
        if(mSelectPosition == position){
            rowContainer.setBackgroundResource(R.color.colorCyan400A);
        } else {
            rowContainer.setBackgroundResource(R.color.colorWhite);
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

            /* Click通知 */
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(readerName);
            }
        };
        rowContainer.setOnClickListener(rowClickListener);

        // LongClickイベントで選択された明細のpositionを保持する
        rowContainer.setOnLongClickListener(v -> {
            // 前回Clickされた明細の変更通知
            if (mSelectPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(mSelectPosition);
            }

            // 今回Clickされた明細の変更通知
            mSelectPosition = position;
            notifyItemChanged(mSelectPosition);

            /* 長押し通知 */
            if (mOnItemLongClickListener != null) {
                mOnItemLongClickListener.onItemLongClick(readerName);
            }
            return false;
        });
    }
    // endregion
}
