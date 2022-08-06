package jp.co.khwayz.eleEntExtManage.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.densowave.scannersdk.Const.CommConst;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.filter.AlphaNumericSingInputFilter;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.http.response.AuthenticationResponse;
import jp.co.khwayz.eleEntExtManage.http.task.get.AuthenticationTask;
import jp.co.khwayz.eleEntExtManage.http.task.get.GetMasterDataTask;
import jp.co.khwayz.eleEntExtManage.menu.IssueMenuFragment;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentIssueLoginBinding;
import jp.co.khwayz.eleEntExtManage.setting.IssueSettingFragment;

public class IssueLoginFragment extends BaseFragment {

    // DataBinding
    private FragmentIssueLoginBinding mBinding;

    // region [ ActivityResultLauncher ]
    // パスワード再設定画面からの戻り
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() != Activity.RESULT_OK) { return; }
                        Intent data = result.getData();
                        if (data == null) { return; }
                        String newPWD = data.getStringExtra(ChangePasswordActivity.RESULT_KEY_PASSWORD);
                        mBinding.editTextPassword.setText(newPWD);
                        // 区分マスタ・メッセージマスタを取得
                        getMasterData(true);
                    });
    // endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentIssueLoginBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    // region [ BaseFragmentListener Override ]
    @Override
    public void viewSetting() {
        super.viewSetting();
        mListener.setGroupsVisibility(true, false, true);
        mListener.setlogo2(View.VISIBLE);
        mListener.setlogo(View.INVISIBLE);
        mListener.setReaderConnectButtonVisibility(View.INVISIBLE);
        mListener.setBatteryStateImageVisibility(View.INVISIBLE);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();

        // ログインIDを英数字記号のみに設定
        mBinding.editTextLoginId.setFilters(new InputFilter[]{new AlphaNumericSingInputFilter(), new InputFilter.LengthFilter(20)});
    }

    @Override
    public void eventSetting() {
        super.eventSetting();

        // 設定ボタン押下
        View.OnClickListener settingClickListener = view -> settingClick();
        ButtonInfo settingButtonInfo = new ButtonInfo(getString(R.string.setting), settingClickListener);

        // ログインボタン押下
        View.OnClickListener loginClickListener = view -> loginClick();
        ButtonInfo loginButtonInfo = new ButtonInfo(getString(R.string.login), loginClickListener);

        // フッターボタン設定
        mListener.setFooterButton(null, null, loginButtonInfo, null, settingButtonInfo);
    }

    @Override
    public void onStart() {
        super.onStart();
        Application.currentFragment = this;
        // 入力済みデータクリア
        mBinding.editTextLoginId.setText(null);
        mBinding.editTextPassword.setText(null);
    }

    /**
     * 設定ボタン押下
     */
    private void settingClick(){
        mListener.replaceFragmentWithStack(new IssueSettingFragment(), TAG);
    }

    /**
     * ログインボタン押下
     */
    private void loginClick() {
        try {
            // ログインID取得
            final String loginId;
            if (mBinding.editTextLoginId.getText() != null) {
                loginId = mBinding.editTextLoginId.getText().toString();
                //画面要素にセット
                mListener.setLoginId2(View.VISIBLE);
                mListener.setLoginId(loginId);
            } else {
                loginId = null;
            }

            // ログインIDチェック
            if (TextUtils.isEmpty(loginId)) {
                mUtilListener.showAlertDialog(getString(R.string.const_err_message_E0001));
                return;
            }

            // パスワード取得
            String password = null;
            if (mBinding.editTextPassword.getText() != null) {
                password = mBinding.editTextPassword.getText().toString();
            }

            // パスワードチェック
            if (TextUtils.isEmpty(password)) {
                mUtilListener.showAlertDialog(getString(R.string.const_err_message_E0002));
                return;
            }

            // ログイン認証
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId", loginId);
            jsonObject.addProperty("password", password);
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + "/user/auth/";
            new AuthenticationTask(authCallback, url, jsonObject.toString()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(getString(R.string.const_err_message_E9000));
        }
    }

    // region [ BaseFragment Override ]
    @Override
    protected void onBackButtonClick(View view) { }
    @Override
    protected void openScanner() { }
    // endregion

    // region [ ApplicationFragmentInterface Override ]
    @Override
    public void CommStatusChanged(CommConst.ScannerStatus status) { }
    @Override
    public boolean hasScanner() {
        return false;
    }
    // endregion

    // region [ private method ]
    /**
     * マスタ取得
     */
    private void getMasterData(boolean showProgressDialog) {
        new GetMasterDataTask(getMasterCallBack)
                .setShowProgress(showProgressDialog)
                .execute();
    }
    // endregion

    // region [ task call back ]
    /**
     * 認証処理Callback
     */
    AuthenticationTask.Callback<AuthenticationResponse> authCallback = new AuthenticationTask.Callback<AuthenticationResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            if (showProgress) {
                mUtilListener.showProgressDialog(getString(R.string.auth_info_message_I0001));
            }
        }
        @Override
        public void onTaskFinished(AuthenticationResponse response) {
            // APIからの戻り値がNGだった場合
            if (!Constants.API_RESPONSE_STATUS_CODE_OK.equals(response.getStatus())) {
                // ProgressDialogを閉じる
                mUtilListener.dismissProgressDialog();
                // エラーメッセージを表示して終了
                mUtilListener.showAlertDialog(getString(R.string.const_err_message_E0003));
                return;
            }

            // ログイン情報取得
            Application.userInfo = response.getData();
            mListener.showUserId();

            // 認証できたがパスワード再設定の場合、パスワード再設定画面に遷移する
            if (Application.userInfo.getPasswordReset().equals("1")) {
                // ProgressDialogを閉じる
                mUtilListener.dismissProgressDialog();
                // メッセージを表示
                mUtilListener.showInformationDialog(getString(R.string.const_err_message_E0007),
                        (dialog, which) -> {
                            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                            intent.putExtra(ChangePasswordActivity.INTENT_KEY_USER_ID, Application.userInfo.getUserId());
                            activityResultLauncher.launch(intent);
                        }
                );
                return;
            }

            // ProgressDialogのメッセージを変更
            mUtilListener.setProgressMessage(getString(R.string.auth_info_message_I0003));
            // 区分マスタ・メッセージマスタを取得
            getMasterData(false);
        }
        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // メッセージを表示
            mUtilListener.showAlertDialog(messageId);
        }
    };

    /**
     * 区分マスタ・メッセージマスタ取得Callback
     */
    GetMasterDataTask.Callback<Boolean> getMasterCallBack = new GetMasterDataTask.Callback<Boolean>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            if (showProgress) {
                mUtilListener.showProgressDialog(getString(R.string.auth_info_message_I0003));
            }
        }
        @Override
        public void onTaskFinished(Boolean response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // 企業コードをアプリ変数にセット
            ArrayList<CategoryInfo> infoList = mUtilListener.getCategoryList(Constants.KBN_EEPCSHIKIBETUSHI);
            if (infoList.size() == 0) {
                Application.corporateCode = Constants.DEFAULT_COM_CD;
            } else {
                Application.corporateCode = infoList.get(0).getElement();
            }
            // メニュー画面を表示する
            mListener.replaceFragmentWithStack(new IssueMenuFragment(), TAG);
        }
        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // メッセージを表示
            mUtilListener.showAlertDialog(messageId);
        }
    };
    // endregion
}