package jp.co.khwayz.eleEntExtManage.dialog_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


import jp.co.khwayz.eleEntExtManage.R;

public class InputCaseMarkNoDialogFragment extends DialogFragment {
    private Context context;
    private Button confirmButton;
    private ImageButton backButton;

    public InputCaseMarkNoDialogFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_fragment_input_case_mark_no);

        // 設定ボタン TODO:実際は入力されたC/Noを保持する。
        this.confirmButton = dialog.findViewById(R.id.positive_button);
        this.confirmButton.setOnClickListener(v -> dismiss());

        // Left Allow ボタン
        this.backButton = dialog.findViewById(R.id.negative_button);
        this.backButton.setOnClickListener(v -> dismiss());
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
