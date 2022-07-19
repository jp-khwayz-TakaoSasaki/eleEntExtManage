package jp.co.khwayz.eleEntExtManage;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static jp.co.khwayz.eleEntExtManage.Common.isReceiptApp;

import com.densowave.scannersdk.Common.CommException;
import com.densowave.scannersdk.Common.CommKeyStatusChangedEvent;
import com.densowave.scannersdk.Common.CommScanner;
import com.densowave.scannersdk.Common.CommStatusChangedEvent;
import com.densowave.scannersdk.Const.CommConst;
import com.densowave.scannersdk.Listener.ScannerKeyStatusListener;
import com.densowave.scannersdk.Listener.ScannerStatusListener;

import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragmentListener;
import jp.co.khwayz.eleEntExtManage.common.MainActivityListener;
import jp.co.khwayz.eleEntExtManage.common.BaseActivity;
import jp.co.khwayz.eleEntExtManage.database.RfidDataBaseOpenHelper;
import jp.co.khwayz.eleEntExtManage.databinding.ActivityMainBinding;
import jp.co.khwayz.eleEntExtManage.authentication.IssueLoginFragment;
import jp.co.khwayz.eleEntExtManage.setting.PairingReaderActivity;

public class MainActivity extends BaseActivity implements MainActivityListener, ScannerStatusListener, ScannerKeyStatusListener {
    private ActivityMainBinding mBinding;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    //    private static Vibrator mVibrator;
    protected final Handler mBatteryHandler = new Handler(Looper.getMainLooper());

    /**
     * 権限要求時のリクエストコードです。
     */
    private static final int REQUEST_PERMISSION_CODE = 1;

    // リーダー接続画面からの戻り
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != Activity.RESULT_OK) { return; }
                // スキャナー状態Listener登録
                Application.commScanner.addStatusListener(this);
                // トリガのlistener登録
                Application.commScanner.addKeyStatusListener(this);
                // 接続イベント通知
                final CommStatusChangedEvent eventArgs = new CommStatusChangedEvent(CommConst.ScannerStatus.CLAIMED);
                onScannerStatusChanged(Application.commScanner, eventArgs);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        Application.mainActivity = this;

        // 権限確認
        verifyPermissions(this);
//        viewSetting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
        // Battery監視スレッド停止
        mBatteryHandler.removeCallbacks(rnBattery);
        // SP1の接続解除
        Application.disConnectReader();
    }

    private void viewSetting() {
        boolean isReceiptApp = isReceiptApp(this);
        mBinding.loginIdText.setVisibility(isReceiptApp ? View.VISIBLE : View.GONE);
        mBinding.loginIdText2.setVisibility(isReceiptApp ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void setGroupsVisibility(boolean isHeader, boolean isSubHeader, boolean isFooter) {
        mBinding.header.setVisibility(isHeader ? View.VISIBLE : View.GONE);
        mBinding.subHeader.setVisibility(isSubHeader ? View.VISIBLE : View.GONE);
        mBinding.footer.setVisibility(isFooter ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSubTitleText(String text) {
        mBinding.subTitle.setText(text);
    }

    @Override
    public void setSubHeaderExplanationText(String text) {
        mBinding.subHeaderExplanation.setText(text);
    }

    @Deprecated
    @Override
    public void setFooterButton(ButtonInfo buttonInfo) {
        if (isReceiptApp(this)) {
            mBinding.footerButton1.setVisibility(buttonInfo != null ? View.VISIBLE : View.INVISIBLE);
            mBinding.footerButton1.setText(buttonInfo != null ? buttonInfo.getText() : "");
            mBinding.footerButton1.setOnClickListener(buttonInfo != null ? buttonInfo.getClickListener() : null);
            mBinding.footerButton5.setVisibility(View.GONE);
        } else {
            mBinding.footerButton5.setVisibility(buttonInfo != null ? View.VISIBLE : View.INVISIBLE);
            mBinding.footerButton5.setText(buttonInfo != null ? buttonInfo.getText() : "");
            mBinding.footerButton5.setOnClickListener(buttonInfo != null ? buttonInfo.getClickListener() : null);
        }
        mBinding.footerButton2.setVisibility(View.GONE);
        mBinding.footerButton3.setVisibility(View.GONE);
        mBinding.footerButton4.setVisibility(View.GONE);
    }

    @Deprecated
    @Override
    public void setFooterButton(ButtonInfo buttonInfo1, ButtonInfo buttonInfo2) {
        mBinding.footerButton1.setVisibility(View.INVISIBLE);
        mBinding.footerButton3.setVisibility(buttonInfo2 != null ? View.VISIBLE : View.GONE);
        mBinding.footerButton3.setText(buttonInfo2 != null ? buttonInfo2.getText() : "");
        mBinding.footerButton3.setOnClickListener(buttonInfo2 != null ? buttonInfo2.getClickListener() : null);
    }

    @Deprecated
    @Override
    public void setFooterButton(ButtonInfo buttonInfo1,ButtonInfo buttonInfo2,ButtonInfo buttonInfo3) {
        mBinding.footerButton4.setVisibility(buttonInfo1 != null ? View.VISIBLE : View.INVISIBLE);
        mBinding.footerButton4.setText(buttonInfo1 != null ? buttonInfo1.getText() : "");
        mBinding.footerButton4.setOnClickListener(buttonInfo1 != null ? buttonInfo1.getClickListener() : null);

        mBinding.footerButton2.setVisibility(buttonInfo2 != null ? View.VISIBLE : View.INVISIBLE);
        mBinding.footerButton2.setText(buttonInfo2 != null ? buttonInfo2.getText() : "");
        mBinding.footerButton2.setOnClickListener(buttonInfo2 != null ? buttonInfo2.getClickListener() : null);

        mBinding.footerButton3.setVisibility(View.VISIBLE);
        mBinding.footerButton3.setText(buttonInfo3 != null ? buttonInfo3.getText() : "");
        mBinding.footerButton3.setOnClickListener(buttonInfo3 != null ? buttonInfo3.getClickListener() : null);
    }

    public void setFooterButton(ButtonInfo b1, ButtonInfo b2, ButtonInfo b3, ButtonInfo b4, ButtonInfo b5) {
        mBinding.footerButton1.setVisibility(b1 != null ? View.VISIBLE : View.INVISIBLE);
        mBinding.footerButton1.setText(b1 != null ? b1.getText() : "");
        mBinding.footerButton1.setOnClickListener(b1 != null ? b1.getClickListener() : null);

        mBinding.footerButton2.setVisibility(b2 != null ? View.VISIBLE : View.INVISIBLE);
        mBinding.footerButton2.setText(b2 != null ? b2.getText() : "");
        mBinding.footerButton2.setOnClickListener(b2 != null ? b2.getClickListener() : null);

        mBinding.footerButton3.setVisibility(b3 != null ? View.VISIBLE : View.INVISIBLE);
        mBinding.footerButton3.setText(b3 != null ? b3.getText() : "");
        mBinding.footerButton3.setOnClickListener(b3 != null ? b3.getClickListener() : null);

        mBinding.footerButton4.setVisibility(b4 != null ? View.VISIBLE : View.INVISIBLE);
        mBinding.footerButton4.setText(b4 != null ? b4.getText() : "");
        mBinding.footerButton4.setOnClickListener(b4 != null ? b4.getClickListener() : null);

        mBinding.footerButton5.setVisibility(b5 != null ? View.VISIBLE : View.INVISIBLE);
        mBinding.footerButton5.setText(b5 != null ? b5.getText() : "");
        mBinding.footerButton5.setOnClickListener(b5 != null ? b5.getClickListener() : null);
    }

    /**
     * QR/RFIDボタン用テキスト張替
     * @param buttonNumber
     */
    @Override
    public void changeFooterButtonLabel(int buttonNumber) {
        Button editButton;
        switch (buttonNumber){
            case 1:
                editButton = mBinding.footerButton1;
                break;
            case 2:
                editButton = mBinding.footerButton2;
                break;
            case 3:
                editButton = mBinding.footerButton3;
                break;
            case 4:
                editButton = mBinding.footerButton4;
                break;
            case 5:
                editButton = mBinding.footerButton5;
                break;
            default:
                editButton = mBinding.footerButton1;
        }

        String beforeText = editButton.getText().toString();
        String afterText = beforeText.equals("QR") ? "RFID" : "QR";
        editButton.setText(afterText);
    }

    @Override
    public void setBackButton(View.OnClickListener clickListener) {
        mBinding.backButton.setOnClickListener(clickListener);
        mBinding.backButton.setVisibility(clickListener == null ? View.GONE : View.VISIBLE);
    }

    public void setLoginId2(int visivility){
        mBinding.loginIdText2.setVisibility(visivility);
        setLoginId2Label(visivility);
    }

    private void setLoginId2Label(int visibility) {
        mBinding.loginIdLabel.setVisibility(visibility);
    }

    public void setlogo(int visivility){
        mBinding.logo.setVisibility(visivility);
    }


    public void setlogo2(int visivility){
        mBinding.logo2.setVisibility(visivility);
    }

    @Override
    public void connected() {
        mBinding.readerConnectButton.setImageResource(R.drawable.battery_max);
    }

    @Override
        public void setLoginId(String loginId) {
        String login_text = loginId;
        mBinding.loginIdText2.setText(login_text);
    }

    @Override
    public void setScreenId(String id) {mBinding.screenId.setText(id);
    }

    /**
     * ログインID取得
     */
    @Override
    public void showUserId() {
        mBinding.loginIdText2.setText(Application.userInfo.getUserId());
    }

    /**
     * 画面遷移（スタック無し版）
     * @param fragment
     */
    @Override
    public void replaceFragment(Fragment fragment) {
        // 操作禁止view非表示
        mHandler.post(() -> mBinding.disabledCover.setVisibility(View.GONE));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_view, fragment);
        fragmentTransaction.commit();
        Application.log.d(TAG, "replace fragment => " + fragment.getClass().getSimpleName());

        if (fragment instanceof BaseFragmentListener) {
            Application.currentFragment = (BaseFragmentListener) fragment;
        }

    }

    /**
     * 画面遷移（スタックあり版、popBackStakで戻る）
     * @param fragment
     * @param stackName
     */
    @Override
    public void replaceFragmentWithStack(Fragment fragment, String stackName) {
        // 操作禁止view非表示
        mHandler.post(() -> mBinding.disabledCover.setVisibility(View.GONE));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(stackName);
        fragmentTransaction.replace(R.id.fragment_view, fragment);
        fragmentTransaction.commit();
        Application.log.d(TAG, "replace fragment => " + fragment.getClass().getSimpleName());

        if (fragment instanceof BaseFragmentListener) {
            Application.currentFragment = (BaseFragmentListener) fragment;
        }
    }

    @Override
    public void popBackStack() {
        Application.log.d(TAG, "popBackStack");
        // 操作禁止view非表示
        mHandler.post(() -> mBinding.disabledCover.setVisibility(View.GONE));
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void popBackStack(String stackName) {
        Application.log.d(TAG, "popBackStack to : " + stackName);
        // 操作禁止view非表示
        mHandler.post(() -> mBinding.disabledCover.setVisibility(View.GONE));
        getSupportFragmentManager().popBackStack(stackName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void clearBackStack() {
        // 操作禁止view非表示
        mHandler.post(() -> mBinding.disabledCover.setVisibility(View.GONE));
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    @Override
    public void setReaderConnectButtonVisibility(int visibility) {
        mBinding.readerConnectButton.setVisibility(visibility);
    }

    @Override
    public void setBatteryStateImageVisibility(int visibility) {
        mBinding.batteryStateImage.setVisibility(visibility);
    }

    /**
     * リーダ選択画面表示処理
     */
    @Override
    public void showParingReaderActivity() {
        Intent intent = new Intent(this, PairingReaderActivity.class);
        activityResultLauncher.launch(intent);
    }

    /**
     * Activityの初期設定
     */
    private void initActivity() {

        // データベース初期化
        Application.dbHelper = new RfidDataBaseOpenHelper(getApplicationContext());

        // リーダー選択ボタン
        mBinding.readerConnectButton.setOnClickListener(v -> {
            // リーダー接続画面表示
            showParingReaderActivity();
        });

        // トリガーイベントに操作禁止にするViewを非表示
        mBinding.disabledCover.setVisibility(View.GONE);

        // HTボタンのImage設定
        if (Application.commScanner == null) {
            mBinding.readerConnectButton.setImageResource(R.drawable.ic_ht_off);
        } else {
            mBinding.readerConnectButton.setImageResource(R.drawable.ic_ht_on);
        }

        // Battery監視スレッド開始
        mBinding.batteryStateImage.setImageResource(R.drawable.action_battery_level);
        mBatteryHandler.post(rnBattery);

        // ログイン画面表示
        replaceFragment(new IssueLoginFragment());
    }

    /**
     * permission 確認
     *
     * @param activity アクティビティ
     */
    public void verifyPermissions(Activity activity) {

        /**
         * 権限要求時の要求権限です。
         */
        final String[] REQUEST_PERMISSIONS
                = {Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_COARSE_LOCATION};

        // Check if we have read or write permission
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (readPermission != PackageManager.PERMISSION_GRANTED
                || writePermission != PackageManager.PERMISSION_GRANTED
                || locationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    REQUEST_PERMISSIONS,
                    REQUEST_PERMISSION_CODE
            );
        } else {
            // 許可済み
            Application.initLog();
            initActivity();
        }
    }

    /**
     * 権限許可ダイアログのボタンをタップしたときのリスナー
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            boolean permitted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permitted = false;
                    break;
                }
            }
            if (!permitted) {
                Toast.makeText(this, R.string.msg_request_permission, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Application.initLog();
                initActivity();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * SP1の状態監視リスナー
     * @param commScanner : 接続しているSP1
     * @param commStatusChangedEvent : スキャナーの状態
     */
    @Override
    public void onScannerStatusChanged(CommScanner commScanner, CommStatusChangedEvent commStatusChangedEvent) {
        try {
            // 操作禁止view非表示
            mHandler.post(() -> mBinding.disabledCover.setVisibility(View.GONE));

            final CommConst.ScannerStatus status = commStatusChangedEvent.getStatus();
            switch (status) {
                case CLAIMED:
                    Application.log.d(TAG, "onScannerStatusChanged - CLAIMED");
                    /*
                     * スキャナが獲得された
                     */
                    //以下メソッドがアベンドするとCLAIMEDだが、CommScanner内部でエラーは発生している
                    //Exceptionでcatchし、アプリ再起動させる
                    commScanner.getBTLocalName();
                    // バッテリー状態イメージ設定
                    setBatteryImage();
                    //接続状態変更を通知
                    if (Application.currentFragment != null) {
                        runOnUiThread(() -> Application.currentFragment.CommStatusChanged(status));
                    }
                    break;
                case CLOSE_WAIT:
                    Application.log.d(TAG, "onScannerStatusChanged - CLOSE_WAIT");
                    /*
                     * クローズ待ち
                     * ComScanner#closeされるまでこの状態となるのでアプリ側でCloseする。
                     */
                    try {
                        Application.commScanner.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case CLOSED:
                    Application.log.d(TAG, "onScannerStatusChanged - CLOSED");
                    /*
                     * スキャナが解放された
                     */
                    Application.commScanner.removeStatusListener(this);
                    Application.commScanner.removeKeyStatusListener(this);
                    Application.commScanner = null;
                    //接続状態変更を通知
                    if (Application.currentFragment != null) {
                        runOnUiThread(() -> Application.currentFragment.CommStatusChanged(status));
                    }
                    showSnackBarOnUiThread(R.string.err_message_E9010);
                    break;
                case UNKNOWN:
                    Application.log.d(TAG, "onScannerStatusChanged - UNKNOWN");
                    /*
                     * 不明な状態です。
                     */
                    break;
                default:
                    //通過することはない
                    Application.log.d(TAG, "onScannerStatusChanged - OTHER");
                    break;
            }

            mHandler.post(() -> {
                // バッテリー状態イメージ設定
                setBatteryImage();
                // HTボタンのImage設定
                if (Application.commScanner == null) {
                    mBinding.readerConnectButton.setImageResource(R.drawable.ic_ht_off);
                } else {
                    mBinding.readerConnectButton.setImageResource(R.drawable.ic_ht_on);
                }
            });

        } catch(Exception ex) {
            ex.printStackTrace();
            Application.log.e(TAG, "onScannerStatusChanged - Exception");
        }
    }

    /**
     * トリガーイベントlistener
     * @param commScanner : 接続されているSP1のインスタンス
     * @param commKeyStatusChangedEvent : イベントパラメータ
     */
    @Override
    public void onScannerKeyStatusChanged(CommScanner commScanner, CommKeyStatusChangedEvent commKeyStatusChangedEvent) {
        try {

            // スキャナーを使用しないFragmentの場合
            if (!Application.currentFragment.hasScanner()) {
                mHandler.post(() -> {
                    // 操作禁止view非表示
                    mBinding.disabledCover.setVisibility(View.GONE);
                });
                return;
            }

            switch (commKeyStatusChangedEvent.getKeyStatus()) {
                case RELEASE:
                    mHandler.post(() -> {
                        // 操作禁止view非表示
                        mBinding.disabledCover.setVisibility(View.GONE);
                    });
                    break;
                case PRESS:
                    mHandler.post(() -> {
                        // 操作禁止view表示
                        mBinding.disabledCover.setVisibility(View.VISIBLE);
                        mBinding.disabledCover.bringToFront();
                    });
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 電池残量の表示
     */
    private void setBatteryImage() {
        // SP1と接続されている場合
        if (Application.commScanner != null) {
            // バッテリー状態取得
            CommConst.CommBattery battery = null;
            try {
                battery = Application.commScanner.getRemainingBattery();
            } catch (CommException e) {
                e.printStackTrace();
            }
            // バッテリーイメージ設定
            if (battery != null) {
                switch (battery) {
                    case UNDER10:
                        mBinding.batteryStateImage.getDrawable().setLevel(10);
                        break;
                    case UNDER40:
                        mBinding.batteryStateImage.getDrawable().setLevel(60);
                        break;
                    case OVER40:
                        mBinding.batteryStateImage.getDrawable().setLevel(100);
                        break;
                }
            } else {
                mBinding.batteryStateImage.getDrawable().setLevel(999);
            }
        } else {
            mBinding.batteryStateImage.getDrawable().setLevel(999);
        }
    }

    /**
     * バッテリー監視タイマ
     */
    Runnable rnBattery = new Runnable() {
        @Override
        public void run() {
            // バッテリー状態イメージの設定
            setBatteryImage();
            // １分間待機
            mBatteryHandler.postDelayed(this, 60000);
        }
    };

    @Override
    public void setSijikakuninCheck(int visibility) {
        mBinding.sijikakunin.setVisibility(visibility);
        mBinding.sijikakuninCheck.setVisibility(visibility);
    }

    @Override
    public void setSijikakuninCheckOn() {
        mBinding.sijikakuninCheck.setChecked(true);
    }

    @Override
    public void setSijikakuninCheckOff() {
        mBinding.sijikakuninCheck.setChecked(false);
    }

    @Override
    public boolean getSijikakuninCheck() {
        return mBinding.sijikakuninCheck.isChecked();
    }
}