package jp.co.khwayz.eleEntExtManage.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.densowave.scannersdk.Const.CommConst;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentIssueMenuBinding;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentIssueSettingBinding;
import jp.co.khwayz.eleEntExtManage.menu.IssueMenuFragment;

public class IssueSettingFragment extends BaseFragment {

    // DataBinding
    private FragmentIssueSettingBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentIssueSettingBinding.inflate(inflater, container, false);
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
        mListener.setSubTitleText(getString(R.string.setting));
        mListener.setScreenId(getString(R.string.screen_id_setting_menu));
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
    }

    @Override
    public void eventSetting() {
        super.eventSetting();

        // 端末設定
        mBinding.issueDeviceSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 端末設定画面に遷移
                mListener.replaceFragmentWithStack(new IssueDeviceSettingFragment(), TAG);
            }
        });

        // RFID設定
        mBinding.issueRfidSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // RFID設定画面に遷移
                mListener.replaceFragmentWithStack(new IssueRFIDSettingFragment(), TAG);
            }
        });

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
        // 呼び出し元の画面に戻る
        mListener.popBackStack();
    }

    @Override
    protected void openScanner() { }
    // endregion
}
