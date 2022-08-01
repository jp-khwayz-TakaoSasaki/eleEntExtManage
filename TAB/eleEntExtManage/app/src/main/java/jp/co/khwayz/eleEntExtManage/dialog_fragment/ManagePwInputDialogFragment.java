package jp.co.khwayz.eleEntExtManage.dialog_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseActivityListener;

public class ManagePwInputDialogFragment extends DialogFragment {
    protected BaseActivityListener mUtilListener;
    private Context context;
    // 管理用パスワード
    private static String mManagePw;

    // 画面要素
    private EditText inputPwEt;
    private Button yesButton;
    private Button noButton;
    private Dialog dialog;

    // Listener
    ManagePwInputDialogListener listener;

    public ManagePwInputDialogFragment(Context context, String managePw) {
        this.context = context;
        this.mManagePw = managePw;
    }

    public interface  ManagePwInputDialogListener {
        public void onPwAuthSuccess();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mUtilListener = (BaseActivityListener) context;
            listener = (ManagePwInputDialogListener)Application.currentFragment;
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
        dialog.setContentView(R.layout.dialog_fragment_input_managepw);

        // はいボタン
        this.yesButton = dialog.findViewById(R.id.positive_button);
        this.yesButton.setOnClickListener(v -> onYesClick());

        // いいえボタン
        this.noButton = dialog.findViewById(R.id.negative_button);
        this.noButton.setOnClickListener(v -> dismiss());

        // 入力パスワード
        this.inputPwEt = dialog.findViewById(R.id.edit_text_password);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 「はい」押下
     */
    void onYesClick(){
        // 入力パスワードチェック
        if(!this.inputPwEt.getText().toString().equals(mManagePw)){
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E0014));
            return;
        }
        // 親フラグメントに返す
        dialog.dismiss();
        listener.onPwAuthSuccess();
    }
}
