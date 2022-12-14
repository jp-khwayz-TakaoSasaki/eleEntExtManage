package jp.co.khwayz.eleEntExtManage;

import static android.content.ContentValues.TAG;
import static jp.co.khwayz.eleEntExtManage.Common.isReceiptApp;

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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.densowave.scannersdk.Common.CommException;
import com.densowave.scannersdk.Common.CommKeyStatusChangedEvent;
import com.densowave.scannersdk.Common.CommScanner;
import com.densowave.scannersdk.Common.CommStatusChangedEvent;
import com.densowave.scannersdk.Const.CommConst;
import com.densowave.scannersdk.Listener.ScannerKeyStatusListener;
import com.densowave.scannersdk.Listener.ScannerStatusListener;

import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.authentication.IssueLoginFragment;
import jp.co.khwayz.eleEntExtManage.common.BaseActivity;
import jp.co.khwayz.eleEntExtManage.common.BaseFragmentListener;
import jp.co.khwayz.eleEntExtManage.common.MainActivityListener;
import jp.co.khwayz.eleEntExtManage.database.RfidDataBaseOpenHelper;
import jp.co.khwayz.eleEntExtManage.databinding.ActivityMainBinding;
import jp.co.khwayz.eleEntExtManage.setting.PairingReaderActivity;

public class MainActivity extends BaseActivity implements MainActivityListener, ScannerStatusListener, ScannerKeyStatusListener {
    private ActivityMainBinding mBinding;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    //    private static Vibrator mVibrator;
    protected final Handler mBatteryHandler = new Handler(Looper.getMainLooper());

    /**
     * ???????????????????????????????????????????????????
     */
    private static final int REQUEST_PERMISSION_CODE = 1;

    // ???????????????????????????????????????
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != Activity.RESULT_OK) { return; }
                // ?????????????????????Listener??????
                Application.commScanner.addStatusListener(this);
                // ????????????listener??????
                Application.commScanner.addKeyStatusListener(this);
                // ????????????????????????
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

        // ????????????
        verifyPermissions(this);
//        viewSetting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
        // Battery????????????????????????
        mBatteryHandler.removeCallbacks(rnBattery);
        // SP1???????????????
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
     * QR/RFID??????????????????????????????
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
     * ????????????ID??????
     */
    @Override
    public void showUserId() {
        mBinding.loginIdText2.setText(Application.userInfo.getUserId());
    }

    /**
     * ???????????????????????????????????????
     * @param fragment
     */
    @Override
    public void replaceFragment(Fragment fragment) {
        // ????????????view?????????
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
     * ???????????????????????????????????????popBackStak????????????
     * @param fragment
     * @param stackName
     */
    @Override
    public void replaceFragmentWithStack(Fragment fragment, String stackName) {
        // ????????????view?????????
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
        // ????????????view?????????
        mHandler.post(() -> mBinding.disabledCover.setVisibility(View.GONE));
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void popBackStack(String stackName) {
        Application.log.d(TAG, "popBackStack to : " + stackName);
        // ????????????view?????????
        mHandler.post(() -> mBinding.disabledCover.setVisibility(View.GONE));
        getSupportFragmentManager().popBackStack(stackName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void clearBackStack() {
        // ????????????view?????????
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
     * ?????????????????????????????????
     */
    @Override
    public void showParingReaderActivity() {
        Intent intent = new Intent(this, PairingReaderActivity.class);
        activityResultLauncher.launch(intent);
    }

    /**
     * Activity???????????????
     */
    private void initActivity() {

        // ???????????????????????????
        Application.dbHelper = new RfidDataBaseOpenHelper(getApplicationContext());

        // ???????????????????????????
        mBinding.readerConnectButton.setOnClickListener(v -> {
            // ??????????????????????????????
            showParingReaderActivity();
        });

        // ????????????????????????????????????????????????View????????????
        mBinding.disabledCover.setVisibility(View.GONE);

        // HT????????????Image??????
        if (Application.commScanner == null) {
            mBinding.readerConnectButton.setImageResource(R.drawable.ic_ht_off);
        } else {
            mBinding.readerConnectButton.setImageResource(R.drawable.ic_ht_on);
        }

        // Battery????????????????????????
        mBinding.batteryStateImage.setImageResource(R.drawable.action_battery_level);
        mBatteryHandler.post(rnBattery);

        // ????????????????????????
        replaceFragment(new IssueLoginFragment());
    }

    /**
     * permission ??????
     *
     * @param activity ?????????????????????
     */
    public void verifyPermissions(Activity activity) {

        /**
         * ???????????????????????????????????????
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
            // ????????????
            Application.initLog();
            initActivity();
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????
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
     * SP1???????????????????????????
     * @param commScanner : ??????????????????SP1
     * @param commStatusChangedEvent : ????????????????????????
     */
    @Override
    public void onScannerStatusChanged(CommScanner commScanner, CommStatusChangedEvent commStatusChangedEvent) {
        try {
            // ????????????view?????????
            mHandler.post(() -> mBinding.disabledCover.setVisibility(View.GONE));

            final CommConst.ScannerStatus status = commStatusChangedEvent.getStatus();
            switch (status) {
                case CLAIMED:
                    Application.log.d(TAG, "onScannerStatusChanged - CLAIMED");
                    /*
                     * ??????????????????????????????
                     */
                    //??????????????????????????????????????????CLAIMED?????????CommScanner???????????????????????????????????????
                    //Exception???catch?????????????????????????????????
                    commScanner.getBTLocalName();
                    // ???????????????????????????????????????
                    setBatteryImage();
                    //???????????????????????????
                    if (Application.currentFragment != null) {
                        runOnUiThread(() -> Application.currentFragment.CommStatusChanged(status));
                    }
                    break;
                case CLOSE_WAIT:
                    Application.log.d(TAG, "onScannerStatusChanged - CLOSE_WAIT");
                    /*
                     * ??????????????????
                     * ComScanner#close?????????????????????????????????????????????????????????Close?????????
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
                     * ??????????????????????????????
                     */
                    Application.commScanner.removeStatusListener(this);
                    Application.commScanner.removeKeyStatusListener(this);
                    Application.commScanner = null;
                    //???????????????????????????
                    if (Application.currentFragment != null) {
                        runOnUiThread(() -> Application.currentFragment.CommStatusChanged(status));
                    }
                    showSnackBarOnUiThread(R.string.err_message_E9010);
                    break;
                case UNKNOWN:
                    Application.log.d(TAG, "onScannerStatusChanged - UNKNOWN");
                    /*
                     * ????????????????????????
                     */
                    break;
                default:
                    //???????????????????????????
                    Application.log.d(TAG, "onScannerStatusChanged - OTHER");
                    break;
            }

            mHandler.post(() -> {
                // ???????????????????????????????????????
                setBatteryImage();
                // HT????????????Image??????
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
     * ????????????????????????listener
     * @param commScanner : ?????????????????????SP1?????????????????????
     * @param commKeyStatusChangedEvent : ???????????????????????????
     */
    @Override
    public void onScannerKeyStatusChanged(CommScanner commScanner, CommKeyStatusChangedEvent commKeyStatusChangedEvent) {
        try {

            // ?????????????????????????????????Fragment?????????
            if (!Application.currentFragment.hasScanner()) {
                mHandler.post(() -> {
                    // ????????????view?????????
                    mBinding.disabledCover.setVisibility(View.GONE);
                });
                return;
            }

            switch (commKeyStatusChangedEvent.getKeyStatus()) {
                case RELEASE:
                    mHandler.post(() -> {
                        // ????????????view?????????
                        mBinding.disabledCover.setVisibility(View.GONE);
                    });
                    break;
                case PRESS:
                    mHandler.post(() -> {
                        // ????????????view??????
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
     * ?????????????????????
     */
    private void setBatteryImage() {
        // SP1??????????????????????????????
        if (Application.commScanner != null) {
            // ???????????????????????????
            CommConst.CommBattery battery = null;
            try {
                battery = Application.commScanner.getRemainingBattery();
            } catch (CommException e) {
                e.printStackTrace();
            }
            // ?????????????????????????????????
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
     * ??????????????????????????????
     */
    Runnable rnBattery = new Runnable() {
        @Override
        public void run() {
            // ??????????????????????????????????????????
            setBatteryImage();
            // ???????????????
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