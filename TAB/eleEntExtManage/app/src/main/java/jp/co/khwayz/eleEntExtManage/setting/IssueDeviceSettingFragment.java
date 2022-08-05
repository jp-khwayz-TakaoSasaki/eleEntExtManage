package jp.co.khwayz.eleEntExtManage.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;

import com.densowave.scannersdk.Const.CommConst;

import java.util.Locale;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentIssueDeviceSettingBinding;
import jp.co.khwayz.eleEntExtManage.util.Util;
import timber.log.Timber;

public class IssueDeviceSettingFragment extends BaseFragment {

    // region [ Filter ]
    // 半角英数字フィルタ
    private  InputFilter mAlphabetFilter = new InputFilter() {
        final String TEMP = "^[a-zA-Z0-9_]+$";
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int d_start, int d_end) {
            if (source.toString().matches(TEMP)) {
                return source.toString().toUpperCase(Locale.ROOT);
            } else {
                return "";
            }
        }
    };
    // endregion

    // DataBinding
    private FragmentIssueDeviceSettingBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentIssueDeviceSettingBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        // ボタン設定
        mListener.setGroupsVisibility(true, true, true);
        mListener.setFooterButton(null, null, null, null, null);

        // リーダー接続アイコン・電池アイコン
        mListener.setReaderConnectButtonVisibility(View.INVISIBLE);
        mListener.setBatteryStateImageVisibility(View.VISIBLE);

        // タイトル
        mListener.setSubTitleText(getString(R.string.device_setting));
        mListener.setScreenId(getString(R.string.screen_id_setting_device));

        // 端末ID
        mBinding.terminalIdText.setText(Application.terminalID);
        mBinding.terminalIdText.setFilters(new InputFilter[] { mAlphabetFilter, new InputFilter.LengthFilter(10) });

        // 所属拠点
        mBinding.affiliationBaseText.setText(Application.affiliationBase);
        mBinding.affiliationBaseText.setFilters(new InputFilter[] { mAlphabetFilter, new InputFilter.LengthFilter(4) });

        // WebAPIのアドレス
        mBinding.apiConnectionDestinationText.setText(Application.apiUrl);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
    }

    @Override
    public void eventSetting() {
        super.eventSetting();

        // 戻るボタン設定
        View.OnClickListener backClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackButtonClick(view);
            }
        };
        mListener.setBackButton(backClickListener);
    }

    @Override
    public void CommStatusChanged(CommConst.ScannerStatus status) { }
    @Override
    public boolean hasScanner() {
        return false;
    }
    // endregion

    // region [ BaseFragment Override ]
    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        try {
            /* 入力チェック */
            // 端末ID
            Application.terminalID = Util.getTextViewValue(mBinding.terminalIdText);
            // 端末ID未入力
            if (TextUtils.isEmpty(Application.terminalID) || !Util.isAlphaNum(Application.terminalID)) {
                mUtilListener.showAlertDialog(R.string.const_err_message_E0011);
                return;
            }

            // 所属拠点
            Application.affiliationBase = Util.getTextViewValue(mBinding.affiliationBaseText);
            // 所属拠点未入力
            if (TextUtils.isEmpty(Application.affiliationBase) || !Util.isAlphaNum(Application.affiliationBase)) {
                mUtilListener.showAlertDialog(R.string.const_err_message_E0013);
                return;
            }


            // API接続先
            Application.apiUrl = Util.getTextViewValue(mBinding.apiConnectionDestinationText);
            // URL形式かチェック
            if (!URLUtil.isValidUrl(Application.apiUrl)) {
                mUtilListener.showAlertDialog(R.string.const_err_message_E0012);
                return;
            }

            // 設定を保存
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Constants.PREFS_TERMINAL_ID, Application.terminalID);
            editor.putString(Constants.PREFS_AFFILIATION_BASE, Application.affiliationBase);
            editor.putString(Constants.PREFS_API_URL, Application.apiUrl);
            editor.apply();

            // 呼び出し元の画面に戻る
            mListener.popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
            Timber.e(e);
            mUtilListener.showAlertDialog(getString(R.string.const_err_message_E9000));
        }
    }

    @Override
    protected void openScanner() { }
    // endregion

    private void saveSetting(){
    }
}
