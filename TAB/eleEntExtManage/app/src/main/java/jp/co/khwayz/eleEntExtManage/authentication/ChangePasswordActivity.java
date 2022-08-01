package jp.co.khwayz.eleEntExtManage.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.JsonObject;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseActivity;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.databinding.ActivityChangePasswordBinding;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.task.post.PostChangePasswordTask;

public class ChangePasswordActivity extends BaseActivity {
    // region [ Constants ]
    public static final String TAG = ChangePasswordActivity.class.getSimpleName();
    public static final String INTENT_KEY_USER_ID = "INTENT_KEY_USER_ID";
    public static final String RESULT_KEY_USER_ID = "RESULT_KEY_USER_ID";
    public static final String RESULT_KEY_PASSWORD = "RESULT_KEY_PASSWORD";
    // endregion

    // region [ private variable ]
    private ActivityChangePasswordBinding mBinding;
    private String mParamLoginId;
    private String mNewPassword;
    // endregion

    //region [ Lifecycle ]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // パラメータのログインIDを取得
        Intent param = getIntent();
        if (param != null) {
            mParamLoginId = param.getStringExtra(INTENT_KEY_USER_ID);
        }

        // 戻るボタン
        mBinding.backButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });

        // パスワード変更ボタン
        mBinding.footerButtonSetPassword.setOnClickListener(v -> {
            try {
                // 新しいパスワード
                mNewPassword = "";
                if (mBinding.editTextNewPassword.getText() != null) {
                    mNewPassword = mBinding.editTextNewPassword.getText().toString();
                }
                // 確認用パスワード
                String confirmationPassword = "";
                if (mBinding.editTextConfirmationPassword.getText() != null) {
                    confirmationPassword = mBinding.editTextConfirmationPassword.getText().toString();
                }
                // 入力チェック
                if (!isValid(mNewPassword, confirmationPassword)) { return; }

                // 更新処理
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userId", mParamLoginId);
                jsonObject.addProperty("oldPassword", Constants.RESET_PASSWORD);
                jsonObject.addProperty("newPassword", mNewPassword);
                String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + "/user/password/reset";
                new PostChangePasswordTask(setPasswordCallback, url, jsonObject.toString()).execute();
            } catch (Exception e) {
                e.printStackTrace();
                Application.log.e(TAG, e);
                // エラーメッセージを表示
                showAlertDialog(getString(R.string.err_message_E9000));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }
    // endregion

    // region [ private method ]
    /**
     * 入力チェック
     * @param newPWD : 新しいパスワード
     * @param confPDW : 確認用パスワード
     * @return : true -> エラー無し, false -> エラー有り
     */
    private boolean isValid(String newPWD, String confPDW) {

        // 新しいパスワード未入力
        if (TextUtils.isEmpty(newPWD)) {
            showAlertDialog(getString(R.string.const_err_message_E0004));
            return false;
        }

        // 確認用パスワード未入力
        if (TextUtils.isEmpty(confPDW)) {
            showAlertDialog(getString(R.string.const_err_message_E0005));
            return false;
        }

        // 新しいパスワードと確認用が違う
        if (!newPWD.equals(confPDW)) {
            showAlertDialog(getString(R.string.const_err_message_E0006));
            return false;
        }

        return true;
    }
    // endregion

    // region [ http task call back ]
    /**
     * パスワード変更callback
     */
    PostChangePasswordTask.Callback<SimpleResponse> setPasswordCallback = new PostChangePasswordTask.Callback<SimpleResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialog表示
            showProgressDialog(getString(R.string.auth_info_message_I0002));
        }
        @Override
        public void onTaskFinished(SimpleResponse response) {
            // ProgressDialogを閉じる
            dismissProgressDialog();
            /* 更新成功したらLoginFragmentに戻る */

            // 戻り値にログインIDと新しいパスワードをセットする
            Intent result = new Intent();
            result.putExtra(RESULT_KEY_USER_ID, mParamLoginId);
            result.putExtra(RESULT_KEY_PASSWORD, mNewPassword);
            setResult(RESULT_OK, result);
            // 画面終了
            finish();
        }
        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialogを閉じる
            dismissProgressDialog();
            // エラーメッセージを表示
            showAlertDialog(messageId);
        }
    };
    // endregion
}