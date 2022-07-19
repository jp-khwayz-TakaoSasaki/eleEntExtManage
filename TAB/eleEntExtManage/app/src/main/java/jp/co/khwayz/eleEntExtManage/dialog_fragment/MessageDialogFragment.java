package jp.co.khwayz.eleEntExtManage.dialog_fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import jp.co.khwayz.eleEntExtManage.R;

public class MessageDialogFragment extends DialogFragment {
    private Context context;
    private TextView message;
    private Button positiveButton;
    private Button negativeButton;
    private View view;

    public MessageDialogFragment(Context context) {
        this.context = context;
        this.view = LayoutInflater.from(this.context).inflate(R.layout.dialog_simple, null);
        // viewの設定
        this.message = this.view.findViewById(R.id.message);
        this.positiveButton = this.view.findViewById(R.id.positive_button);
        this.negativeButton = this.view.findViewById(R.id.negative_button);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setView(this.view);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setPositiveButton(String text, View.OnClickListener clickListener) {
        this.positiveButton.setText(text);
        this.positiveButton.setOnClickListener(clickListener);
    }

    public void setNegativeButton(String text, View.OnClickListener clickListener) {
        this.negativeButton.setText(text);
        this.negativeButton.setVisibility(View.VISIBLE);
        this.negativeButton.setOnClickListener(clickListener);
    }
}
