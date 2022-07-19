package jp.co.khwayz.eleEntExtManage.adapter;

import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, IM> extends RecyclerView.Adapter<VH> {

    // region [ protected variable ]
    protected List<IM> mLocalDataSet;
    protected int mSelectPosition = RecyclerView.NO_POSITION;
    protected boolean mIsPossibleSelect;

    protected OnItemClickListener<IM> mOnItemClickListener;
    protected OnItemLongClickListener<IM> mOnItemLongClickListener;
    // endregion

    // region [ interface ]
    public interface OnItemClickListener<IM> {
        void onItemClick(IM item);
    }

    public interface OnItemLongClickListener<IM> {
        void onItemLongClick(IM item);
    }
    // endregion

    // region [ Override ]
    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        throw new IllegalStateException();
    }

    @Override
    public int getItemCount() {
        return mLocalDataSet.size();
    }
    // endregion

    // region [ public method ]
    /**
     * リストクリア
     */
    public void clear() {
        int size = this.mLocalDataSet.size();
        this.mLocalDataSet.clear();
        this.notifyItemRangeRemoved(0, size);
    }

    /**
     * 明細一括追加
     * @param itemList 追加する明細リスト
     */
    public void bulkAddItem(List<IM> itemList) {
        this.mLocalDataSet.addAll(itemList);
        notifyDataSetChanged();
    }

    /**
     * 明細追加
     * @param item 追加するPackingInputListItem
     */
    public void addItem(IM item) {
        this.mLocalDataSet.add(item);
        int position = mLocalDataSet.size() - 1;
        notifyItemInserted(position);
        notifyItemRangeChanged(position + 1, mLocalDataSet.size());
    }

    /**
     * 編集完了処理
     */
    public void doneRevised() {
        // 未選択は何もしない
        if (this.mSelectPosition == RecyclerView.NO_POSITION) { return; }
        // 変更通知
        notifyItemChanged(this.mSelectPosition);
        notifyItemRangeChanged(this.mSelectPosition, mLocalDataSet.size());
    }

    /**
     * 明細削除
     * @param position 削除する明細のposition
     */
    public void removeItem(int position) {
        // 明細数よりも位置が超えている場合は何もしない
        if (mLocalDataSet.size() <= position) { return; }
        // 未選択に設定
        this.mSelectPosition = RecyclerView.NO_POSITION;
        // 明細削除
        this.mLocalDataSet.remove(position);
        // 削除を通知
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mLocalDataSet.size());
    }
    // endregion

    // region [ getter ]
    /**
     * 選択された明細のpositionを取得
     * @return 選択された明細のposition
     */
    public int getSelectedItemPosition() {
        return mSelectPosition;
    }

    /**
     * 選択された明細(PackingInputListItem)を取得
     * @return 選択されたPackingInputListItem
     */
    public IM getSelectedItem() {
        // 未選択はnullを返す
        if (this.mSelectPosition != RecyclerView.NO_POSITION) {
            return this.mLocalDataSet.get(this.mSelectPosition);
        }
        return null;
    }

    /**
     * 明細リスト取得
     * @return Adapterの明細情報リスト
     */
    public List<IM> getItemList() {
        return this.mLocalDataSet;
    }
    // endregion

    // region [ setter ]
    /**
     * 選択する明細を設定
     * @param position 選択する明細の位置
     */
    public void setSelectItemPosition(int position) {
        // 明細数よりも位置が超えている場合は何もしない
        if (mLocalDataSet.size() <= position) { return; }
        // 明細を選択して通知
        this.mSelectPosition = position;
        notifyDataSetChanged();
    }
    // endregion

    // region [ Listener setting ]
    public void setOnItemClickListener(@Nullable OnItemClickListener<IM> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public void setOnItemLongClickListener(@Nullable OnItemLongClickListener<IM> onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
    // endregion
}
