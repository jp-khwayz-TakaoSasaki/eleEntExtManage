package jp.co.khwayz.eleEntExtManage.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.densowave.scannersdk.Common.CommException;
import com.densowave.scannersdk.Const.CommConst;
import com.densowave.scannersdk.Dto.CommScannerParams;
import com.densowave.scannersdk.Dto.RFIDScannerSettings;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentIssueRfidSettingBinding;
import timber.log.Timber;

public class IssueRFIDSettingFragment extends BaseFragment {

    // DataBinding
    private FragmentIssueRfidSettingBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentIssueRfidSettingBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* セッションSpinnerの設定 */
        RFIDScannerSettings.Scan.SessionFlag[] sessionValues = RFIDScannerSettings.Scan.SessionFlag.values();
        ArrayList<String> sessionFlags = new ArrayList<>();
        for (RFIDScannerSettings.Scan.SessionFlag flag : sessionValues) {
            sessionFlags.add(flag.name());
        }
        // Adapterの作成
        ArrayAdapter<String> sessionAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item, sessionFlags);
        // ドロップダウンのレイアウトを指定
        sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // SpinnerにAdapterをセット
        mBinding.sessionSpinner.setAdapter(sessionAdapter);

        // セッションSpinnerに設定値をセット
        mBinding.sessionSpinner.setSelection(Application.scannerSessionFlag.ordinal());

        /* チャンネルSpinnerの設定 */
        ArrayList<String> channelList = new ArrayList<>();
        for (Constants.ScannerChanel scannerChanel : Constants.ScannerChanel.values()) {
            channelList.add(scannerChanel.name());
        }
        // Adapterの作成
        ArrayAdapter<String> channelAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item, channelList);
        // ドロップダウンのレイアウトを指定
        channelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // SpinnerにAdapterをセット
        mBinding.channelSpinner.setAdapter(channelAdapter);

        // チャンネルSpinnerに設定値をセット
        int selection = 0;
        for (Constants.ScannerChanel channel : Constants.ScannerChanel.values()) {
            if (channel.getScannerChannel() == Application.scannerChannel) {
                selection = channel.ordinal();
                break;
            }
        }
        mBinding.channelSpinner.setSelection(selection);

        // SeekBarの設定
        mBinding.powerLevelSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    // ツマミがドラッグされると呼ばれる
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // スライドされた値を表示
                        // 0～26の範囲なので最小値(4)を加算して表示する
                        int value = progress + 4;
                        mBinding.powerLevelText.setText(String.valueOf(value));
                    }
                    // ツマミがタッチされた時に呼ばれる
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    // ツマミがリリースされた時に呼ばれる
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
        // SeekBarに設定値をセット
        mBinding.powerLevelSlider.setProgress(Application.scannerPowerLevel - 4);
        // 値を表示
        mBinding.powerLevelText.setText(String.valueOf(Application.scannerPowerLevel));

        // Beep音設定
        if (Application.scannerBuzzerEnable == CommScannerParams.Notification.Sound.Buzzer.ENABLE) {
            mBinding.buzzerSwitch.setChecked(true);
        } else {
            mBinding.buzzerSwitch.setChecked(false);
        }    }

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
        mListener.setSubTitleText(getString(R.string.rfid_setting));
        mListener.setScreenId(getString(R.string.screen_id_setting_rfid));
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
    public void mainSetting() {
        super.mainSetting();
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

            /* セッション */
            // 選択されたセッション名を取得
            String sessionName = (String) mBinding.sessionSpinner.getSelectedItem();
            // 列挙順を取得
            RFIDScannerSettings.Scan.SessionFlag sessionValue = RFIDScannerSettings.Scan.SessionFlag.valueOf(sessionName);
            // Application変数にセット
            Application.scannerSessionFlag = sessionValue;

            /* パワーレベル */
            int powerLevel = mBinding.powerLevelSlider.getProgress() + 4;
            Application.scannerPowerLevel = powerLevel;

            /* チャンネル */
            String channelName = (String) mBinding.channelSpinner.getSelectedItem();
            Constants.ScannerChanel scannerChanel = Constants.ScannerChanel.valueOf(channelName);
            Application.scannerChannel = scannerChanel.getScannerChannel();

            /* Beep音 */
            if (mBinding.buzzerSwitch.isChecked()) {
                Application.scannerBuzzerEnable = CommScannerParams.Notification.Sound.Buzzer.ENABLE;
            } else {
                Application.scannerBuzzerEnable = CommScannerParams.Notification.Sound.Buzzer.DISABLE;
            }

            // 設定を保存
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(Constants.PREFS_SESSION_FLAG, sessionValue.ordinal());
            editor.putInt(Constants.PREFS_POWER_LEVEL, powerLevel);
            editor.putLong(Constants.PREFS_CHANNEL, scannerChanel.getScannerChannel());
            editor.putBoolean(Constants.PREFS_BUZZER_ENABLE, mBinding.buzzerSwitch.isChecked());
            editor.apply();

            // 設定値を反映
            if (Application.commScanner != null) {
                try {
                    CommScannerParams params = Application.commScanner.getParams();
                    // Beep音設定
                    params.notification.sound.buzzer = Application.scannerBuzzerEnable;
                    Application.commScanner.setParams(params);
                } catch (CommException e) {
                    e.printStackTrace();
                }
            }

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
