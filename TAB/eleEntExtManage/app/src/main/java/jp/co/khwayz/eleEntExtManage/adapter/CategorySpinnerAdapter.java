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
 * 区分マスタSpinner用 Adapter
 */
public class CategorySpinnerAdapter extends ArrayAdapter<CategoryInfo> {
    // region [ private variable ]
    private final List<CategoryInfo> mCategorySpinnerList;
    // endregion

    // region [ constructor ]
    /**
     * コンストラクタ
     * @param context Contextクラス
     * @param list 区分リスト
     */
    public CategorySpinnerAdapter(Context context, List<CategoryInfo> list) {
        super(context, R.layout.default_spinner_item, list);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mCategorySpinnerList = list;
    }
    // endregion

    // region [ Override ]
    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        TextView textView = (TextView)super.getView(position, convertView, parent);
        textView.setText(this.mCategorySpinnerList.get(position).getElementName());
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
        TextView textView = (TextView)super.getDropDownView(position, convertView, parent);
        textView.setText(mCategorySpinnerList.get(position).getElementName());
        return textView;
    }
    // endregion

    public int getItemPosition(String code) {
        Optional<CategoryInfo> item = mCategorySpinnerList.stream().filter(x -> x.getElement().equals(code)).findFirst();
        return item.map(mCategorySpinnerList::indexOf).orElse(0);
    }
}
