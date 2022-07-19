package jp.co.khwayz.eleEntExtManage.common;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.CategoryMasterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.MessageMasterDao;

public class BaseActivity extends AppCompatActivity implements BaseActivityListener {
//    public final String TAG = this.getClass().getSimpleName();

    protected Dialog mProgressDialog;
    protected Dialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // バックボタン無効
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        dismissAlertDialog();
    }

    /**
     * ProgressDialogを表示する
     * @param message : 表示するメッセージ
     */
    @Override
    public void showProgressDialog(@NonNull String message) {
        // 一旦、ProgressDialogを閉じる
        dismissProgressDialog();

        // ProgressDialogを表示
        mProgressDialog = new Dialog(this);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setContentView(R.layout.dialog_progress);
        ((TextView) mProgressDialog.findViewById(R.id.progress_message)).setText(message);
//        if (mProgressDialog.getWindow() != null) {
//            mProgressDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        }
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * ProgressDialogのメッセージを表示する
     * @param message : 表示するメッセージ
     */
    @Override
    public void setProgressMessage(String message) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            ((TextView) mProgressDialog.findViewById(R.id.progress_message)).setText(message);
        }
    }

    /**
     * ProgressDialogを閉じる
     */
    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    /**
     * AlertDialog表示
     * @param message : 表示するメッセージ
     */
    @Override
    public void showAlertDialog(String message) {
        // Dialog用レイアウトの読み込み
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_simple, findViewById(R.id.layout_root));

        // メッセージをセット
        TextView textView = layout.findViewById(R.id.message);
        textView.setText(message);

        // OKボタンの設定
        Button positiveButton = layout.findViewById(R.id.positive_button);
        positiveButton.setText(R.string.ok);
        positiveButton.setOnClickListener(v -> mAlertDialog.dismiss());

        // 「いいえ」ボタンの設定
        Button negativeButton = layout.findViewById(R.id.negative_button);
        negativeButton.setVisibility(View.GONE);

        // Dialogを表示
        mAlertDialog = new AlertDialog.Builder(this)
                .setView(layout)
                .create();
        mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    /**
     * AlertDialog表示
     * @param msgId : リソースID
     * @param option : メッセージに埋め込む文字列
     */
    @Override
    public void showAlertDialog(int msgId, Object... option) {
        // メッセージ取得
        String msg = getDataBaseMessage(msgId);
        // Optionがある場合
        if (option != null && 0 < option.length) {
            msg = String.format(msg, option);
        }
        // Dialog表示
        showAlertDialog(msg);
    }

    /**
     * 確認Dialog表示
     * @param msgId : リソースID
     * @param listener : positiveButtonのClick Listener
     * @param option : メッセージに埋め込む文字列
     */
    @Override
    public void showConfirmDialog(int msgId, DialogInterface.OnClickListener listener, Object... option) {
        // メッセージ取得
        String msg = getDataBaseMessage(msgId);
        // Optionがある場合
        if (option != null && 0 < option.length) {
            msg = String.format(msg, option);
        }

        // Dialog用レイアウトの読み込み
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_simple, findViewById(R.id.layout_root));

        // メッセージをセット
        TextView textView = layout.findViewById(R.id.message);
        textView.setText(msg);

        // 「はい」ボタンの設定
        Button positiveButton = layout.findViewById(R.id.positive_button);
        positiveButton.setText(R.string.yes);
        positiveButton.setOnClickListener(v -> {
            listener.onClick(mAlertDialog, DialogInterface.BUTTON_POSITIVE);
            mAlertDialog.dismiss();
        });

        // 「いいえ」ボタンの設定
        Button negativeButton = layout.findViewById(R.id.negative_button);
        negativeButton.setVisibility(View.VISIBLE);
        negativeButton.setText(R.string.no);
        negativeButton.setOnClickListener(v -> mAlertDialog.dismiss());

        // Dialogを表示
        mAlertDialog = new AlertDialog.Builder(this)
                .setView(layout)
                .create();
        mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    /**
     * 情報Dialog表示
     * @param msgId : リソースID
     * @param listener : positiveButtonのClick Listener
     * @param option : メッセージに埋め込む文字列
     */
    @Override
    public void showInformationDialog(int msgId, DialogInterface.OnClickListener listener, Object... option) {
        // メッセージ取得
        String msg = getDataBaseMessage(msgId);
        // Optionがある場合
        if (option != null && 0 < option.length) {
            msg = String.format(msg, option);
        }
        // Dialog表示
        showInformationDialog(msg, listener);
    }

    /**
     * 情報Dialog表示
     * @param message : 表示するメッセージ
     * @param listener : positiveButtonのClick Listener
     */
    @Override
    public void showInformationDialog(String message, DialogInterface.OnClickListener listener) {

        // Dialog用レイアウトの読み込み
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_simple, findViewById(R.id.layout_root));

        // メッセージをセット
        TextView textView = layout.findViewById(R.id.message);
        textView.setText(message);

        // 「OK」ボタンの設定
        Button positiveButton = layout.findViewById(R.id.positive_button);
        positiveButton.setText(R.string.ok);
        positiveButton.setOnClickListener(v -> {
            listener.onClick(mAlertDialog, DialogInterface.BUTTON_POSITIVE);
            mAlertDialog.dismiss();
        });

        // 「いいえ」ボタンの設定
        Button negativeButton = layout.findViewById(R.id.negative_button);
        negativeButton.setVisibility(View.GONE);

        // Dialogを表示
        mAlertDialog = new AlertDialog.Builder(this)
                .setView(layout)
                .create();
        mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    /**
     * AlertDialogを閉じる
     */
    @Override
    public void dismissAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = null;
    }

    /**
     * SnackBarを表示
     * @param messageId : 表示するメッセージリソースID
     * @param option : メッセージに埋め込むOption
     */
    @Override
    public void showSnackBarOnUiThread(final int messageId, Object... option) {
        // メッセージを取得
        String msg = getDataBaseMessage(messageId);
        // Optionがある場合
        if (option != null && 0 < option.length) {
            msg = String.format(msg, option);
        }
        // SnackBar表示
        showSnackBarOnUiThread(msg);
    }

    /**
     * SnackBarを表示します
     * @param message : 表示するメッセージ文字列
     */
    @Override
    public void showSnackBarOnUiThread(String message) {
        final View rootLayout = findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG);
        // Buttonの設定
        snackbar.setAction(R.string.ok, v -> snackbar.dismiss());
        snackbar.setActionTextColor(Color.WHITE);
        // メッセージの文字サイズ変更
        TextView snackTextView =
                snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_large));
        // SnackBar表示
        this.runOnUiThread(snackbar::show);
    }

    /**
     * SQLiteからメッセージ取得
     * @param msgId : リソースID
     * @return メッセージマスタから取得したメッセージ
     */
    @Override
    public String getDataBaseMessage(int msgId) {
        // メッセージID取得
        String msg = getString(msgId);
        // SQLiteからメッセージ本文を取得
        MessageMasterDao dao = new MessageMasterDao();
        return dao.getMessage(Application.dbHelper.getReadableDatabase(), msg);
    }

    /**
     * 区分マスタ取得
     * @param category : 区分
     * @return 取得した区分マスタList
     */
    @Override
    public ArrayList<CategoryInfo> getCategoryList(String category) {
        // SQLiteから区分リストを取得
        CategoryMasterDao dao = new CategoryMasterDao();
        return dao.getCategoryList(Application.dbHelper.getReadableDatabase(), category);
    }

    /**
     * 仕向地取得（重複削除）
     * @return 取得した区分マスタList
     */
    @Override
    public ArrayList<CategoryInfo> getTransportListDistinct() {
        // SQLiteから区分リスト（仕向地：重複削除したもの）を取得
        CategoryMasterDao dao = new CategoryMasterDao();
        return dao.getTransportListDistinct(Application.dbHelper.getReadableDatabase());
    }

    /**
     * リソースのエラーメッセージを取得
     * ※ ログイン画面・パスワード変更画面がメッセージマスタから取得できない為の対処
     * @param msgId : リソースID
     * @return 対応メッセージ
     */
    @Override
    public String getResourceErrorMessage(int msgId) {
        // エラーメッセージの設定
        String msg;
        switch (msgId) {
            case R.string.err_message_E9001:
                msg = getString(R.string.const_err_message_E9001);
                break;
            case R.string.err_message_E9002:
                msg = getString(R.string.const_err_message_E9002);
                break;
            case R.string.err_message_E9003:
                msg = getString(R.string.const_err_message_E9003);
                break;
            case R.string.err_message_E9004:
                msg = getString(R.string.const_err_message_E9004);
                break;
            default:
                msg = getString(R.string.const_err_message_E9000);
                break;
        }
        return msg;
    }
}
