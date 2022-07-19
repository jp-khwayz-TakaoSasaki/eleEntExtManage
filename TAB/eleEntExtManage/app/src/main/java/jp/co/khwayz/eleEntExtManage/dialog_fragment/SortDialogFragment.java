package jp.co.khwayz.eleEntExtManage.dialog_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;

public class SortDialogFragment extends DialogFragment {
    private Context context;
    private ArrayList<String> fieldList = null;
    private Spinner fieldSpinner;
    private Spinner spinnerSortKey;
    private Button confirmButton;
    private ImageButton backButton;
    private Dialog dialog;

    public SortDialogFragment(Context context, ArrayList<String> sortTarget) {
        this.context = context;
        this.fieldList = sortTarget;
    }

    public interface  SortDialogListener {
        public void onDialogSettingClick(DialogFragment dialog, int order, String sortKey);
    }

    SortDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SortDialogListener)Application.currentFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SortDialogListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_fragment_sort);

        // Spinner設定
        this.spinnerSortKey = dialog.findViewById(R.id.spinner_sort_field);
        ArrayAdapter<String> sortKeyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, fieldList);
        sortKeyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerSortKey.setAdapter(sortKeyAdapter);

        // 設定ボタン
        this.confirmButton = dialog.findViewById(R.id.positive_button);
        this.confirmButton.setOnClickListener(v -> onSettingClick());

        // Left Allow ボタン
        this.backButton = dialog.findViewById(R.id.negative_button);
        this.backButton.setOnClickListener(v -> dismiss());
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 設定押下
     */
    void onSettingClick(){
        // ======================
        // ラジオボタン取得
        // ======================
        // ラジオグループのオブジェクトを取得
        RadioGroup rg = (RadioGroup)dialog.findViewById(R.id.radio_group_sort);
        // チェックされているラジオボタンの ID を取得
        int id = rg.getCheckedRadioButtonId();
        // 昇順のラジオボタンのIDを取得
        int asc_id = dialog.findViewById(R.id.radioAsc).getId();;
        // 昇順：0　降順：1
        int order = id == asc_id ? 0 : 1;

        // ======================
        // ソートキー
        // ======================
        String sortKey = this.spinnerSortKey.getSelectedItem().toString();

        // 親フラグメントに返す
        dialog.dismiss();
        listener.onDialogSettingClick(SortDialogFragment.this, order, sortKey);
    }
}
