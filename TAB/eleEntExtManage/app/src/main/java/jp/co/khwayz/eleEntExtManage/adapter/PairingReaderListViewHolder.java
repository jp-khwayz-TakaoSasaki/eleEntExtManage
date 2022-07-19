package jp.co.khwayz.eleEntExtManage.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import jp.co.khwayz.eleEntExtManage.R;

public class PairingReaderListViewHolder extends RecyclerView.ViewHolder {
    // region [ private variable ]
    private final TextView readerName;
    private final ConstraintLayout rowContainer;
    // endregion

    // region [ constructor ]
    public PairingReaderListViewHolder(View view) {
        super(view);
        readerName = view.findViewById(R.id.rv_readerName_text);
        rowContainer = view.findViewById(R.id.rv_view_holder_container);
    }
    // endregion

    /* リーダー名 */
    public TextView getReaderName() { return readerName; }
    /* Viewコンテナ */
    public ConstraintLayout getRowContainer(){ return rowContainer; }

}
