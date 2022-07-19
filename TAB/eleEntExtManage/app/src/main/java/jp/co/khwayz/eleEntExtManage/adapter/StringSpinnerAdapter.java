package jp.co.khwayz.eleEntExtManage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;

/**
 * 文字列リストSpinner用 Adapter
 */
public class StringSpinnerAdapter extends ArrayAdapter<String> {
    // region [ private variable ]
    private final List<String> mStringSpinnerList;
    // endregion

    // region [ constructor ]
    /**
     * コンストラクタ
     * @param context Contextクラス
     * @param list Spinner候補リスト
     */
    public StringSpinnerAdapter(Context context, List<String> list) {
        super(context, R.layout.default_spinner_item, list);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mStringSpinnerList = list;
    }
    // endregion

    // region [ Override ]
    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        TextView textView = (TextView)super.getView(position, convertView, parent);
        textView.setText(this.mStringSpinnerList.get(position));
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
        TextView textView = (TextView)super.getDropDownView(position, convertView, parent);
        textView.setText(mStringSpinnerList.get(position));
        return textView;
    }
    // endregion

    /**
     * 対象のインデックスを取得
     * @param searchElement
     * @return
     */
    public int getItemPosition(String searchElement) {
        int index = mStringSpinnerList.indexOf(searchElement);
        return index == -1 ? 0 : index;
    }
}
