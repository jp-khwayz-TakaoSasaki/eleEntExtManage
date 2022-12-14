package jp.co.khwayz.eleEntExtManage.common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.densowave.scannersdk.Barcode.BarcodeException;
import com.densowave.scannersdk.Barcode.BarcodeScanner;
import com.densowave.scannersdk.Dto.BarcodeScannerSettings;
import com.densowave.scannersdk.Dto.RFIDScannerSettings;
import com.densowave.scannersdk.Exception.ErrorCode;
import com.densowave.scannersdk.Listener.BarcodeDataDelegate;
import com.densowave.scannersdk.Listener.RFIDDataDelegate;
import com.densowave.scannersdk.RFID.RFIDException;
import com.densowave.scannersdk.RFID.RFIDScanner;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.util.Util;

public abstract class BaseFragment extends Fragment implements BaseFragmentListener {

    protected final String TAG = this.getClass().getSimpleName();
    protected static final String ARG_MOVING_MODE = "ARG_MOVING_MODE";
//    protected static final String SAVE_MOVING_MODE = "SAVE_MOVING_MODE";
    protected static final String ARG_TAG_LIST = "ARG_TAG_LIST";
    protected static final String SAVE_TAG_LIST = "SAVE_TAG_LIST";

    protected MainActivityListener mListener;
    protected BaseActivityListener mUtilListener;
    protected final Handler mHandler = new Handler(Looper.getMainLooper());
    protected BarcodeScanner mBarcodeScanner = null;
    protected RFIDScanner mRfidScanner = null;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainInit();
        viewSetting();
        eventSetting();
        mainSetting();
    }

    private void mainInit() {
        mListener.setGroupsVisibility(true, true, true);
        mListener.setSubTitleText("");
        mListener.setFooterButton(null, null, null, null, null);
        mListener.setBackButton(null);
        mListener.setReaderConnectButtonVisibility(View.VISIBLE);
        mListener.setBatteryStateImageVisibility(View.VISIBLE);
        mListener.setSijikakuninCheck(View.INVISIBLE);
    }

    /**
     * View???????????????
     */
    @Override
    public void viewSetting() { }

    /**
     * ???????????????????????????
     */
    @Override
    public void eventSetting() { }

    /**
     * ???????????????????????????
     */
    @Override
    public void mainSetting() { }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityListener) {
            mListener = (MainActivityListener) context;
            mUtilListener = (BaseActivityListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Application.currentFragment = this;
    }

    @Override
    public void onStop() {
        super.onStop();
        //????????????????????????????????????
        if ( getView()  != null ) {
            InputMethodManager imm =
                    (InputMethodManager) Application.mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow( getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
            getView().clearFocus();
        }
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//    }

    /**
     * ??????????????????????????????
     * @param message : ???????????????????????????
     */
    protected void toastOnUiThread(final String message) {
        mHandler.post(() -> Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show());
    }

    /**
     * ??????????????????????????????
     * @param messageId : ???????????????????????????????????????ID
     */
    protected void toastOnUiThread(final int messageId, Object... option) {
        String msg;
        // ????????????????????????
        msg = mUtilListener.getDataBaseMessage(messageId);
        // Option???????????????
        if (option != null && 0 < option.length) {
            msg = String.format(msg, option);
        }
        // ??????????????????
        toastOnUiThread(msg);
    }

    /**
     * ??????????????????????????????
     * @param orderNo ????????????????????????
     * @return true = ??????, false = ??????
     */
    protected boolean isNotValidOrderNo(String orderNo) {
        // ?????????OK
        if (Util.isNumber(orderNo)) { return false; }
        // ??????????????????????????????
        mUtilListener.showAlertDialog(R.string.err_message_E1003,
                Constants.MSG_OPTION_ORDER_NO, Constants.MSG_OPTION_ORDER_NO);
        return true;
    }

    /**
     * ????????????????????????
     * @param orderNo ????????????????????????
     * @param branchNo ??????????????????
     * @return true = ??????, false = ??????
     */
    protected boolean isNotValidTagNo(String orderNo, String branchNo) {
        // ???????????????????????????????????????????????????
        if (!Util.isNumber(orderNo)) {
            // ??????????????????????????????
            mUtilListener.showAlertDialog(R.string.err_message_E1003,
                    Constants.MSG_OPTION_ORDER_NO, Constants.MSG_OPTION_ORDER_NO);
            return true;
        }
        // ?????????????????????????????????????????????
        if (!Util.isNumber(branchNo)) {
            // ??????????????????????????????
            mUtilListener.showAlertDialog(R.string.err_message_E1003,
                    Constants.MSG_OPTION_BRANCH_NO, Constants.MSG_OPTION_BRANCH_NO);
            return true;
        }
        return false;
    }

    /**
     * ?????????????????????????????????????????????
     */
    protected void hideKeyboard() {
        // ?????????????????????????????????????????????
        if (Application.mainActivity.getCurrentFocus() != null) {
            InputMethodManager mgr = (InputMethodManager) Application.mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(Application.mainActivity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * ??????????????????????????????????????????
     * @param view ??????????????????????????????????????????View
     */
    protected void showKeyboard(View view) {
        // ??????????????????????????????????????????
        if (Application.mainActivity.getCurrentFocus() != null) {
            InputMethodManager mgr = (InputMethodManager) Application.mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view.requestFocus()) {
                mgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    // region [ ??????????????? ]
    /**
     * ???????????????????????????
     * @param view : ??????????????????????????????
     */
    protected abstract void onBackButtonClick(View view);

    // endregion

    // region [ ????????????????????? ]
    /**
     * ?????????????????????
     */
    protected abstract void openScanner();

    /**
     * ???????????????????????????
     * @param mode : ????????????????????????
     */
    protected void closeScanner(Constants.ScanMode mode) {
        if (mode == Constants.ScanMode.RFID) {
            stopRFIDScan();
        } else {
            stopBarcodeScan();
        }
    }

    /*
     *???????????????????????????????????????????????????
     */
    protected void startBarcodeScan(BarcodeDataDelegate delegate) {

        if (Application.commScanner == null) { return; }

        //?????????????????????????????????????????????????????????
        if (mBarcodeScanner == null) {
            Application.log.d(TAG, "START:Barcode instance??????");
            mBarcodeScanner = Application.commScanner.getBarcodeScanner();
        }

        if (mBarcodeScanner != null) {
            try {
                //??????????????????Delegate?????????(Listener)
                Application.log.d(TAG, "START:Barcode Delegate??????");
                mBarcodeScanner.setDataDelegate(delegate);

                //????????????????????????????????????
                Application.log.d(TAG, "START:BarcodeScanner??????");
                BarcodeScannerSettings barcodeScannerSettings = mBarcodeScanner.getSettings();
                barcodeScannerSettings.scan.triggerMode = BarcodeScannerSettings.Scan.TriggerMode.MOMENTARY;

                // QR?????????????????????
                barcodeScannerSettings.decode.symbologies.qrCode.enabled = true;
                barcodeScannerSettings.decode.symbologies.qrCode.model1.enabled = true;
                barcodeScannerSettings.decode.symbologies.qrCode.model2.enabled = true;
                barcodeScannerSettings.decode.symbologies.microQr.enabled = true;
                mBarcodeScanner.setSettings(barcodeScannerSettings);

                //??????????????????????????????????????????
                Application.log.d(TAG, "START:Barcode??????????????????");
                mBarcodeScanner.openReader();

            } catch (BarcodeException e) {
                e.printStackTrace();
                Application.log.e(TAG, e);
                mUtilListener.showSnackBarOnUiThread("StartBarcodeErr \n" + e.getMessage());
            }
        }
    }

    /*
     *????????????????????????????????????
     */
    protected void stopBarcodeScan() {
        if (Application.commScanner == null) { return; }
        if (mBarcodeScanner == null) { return; }

        try {
            //??????????????????????????????????????????
            Application.log.d(TAG, "START:Barcode??????????????????");
            this.mBarcodeScanner.closeReader();

            Application.log.d(TAG, "START:Barcode delegate??????");
            this.mBarcodeScanner.setDataDelegate(null);

            this.mBarcodeScanner = null;
        } catch (BarcodeException e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showSnackBarOnUiThread("StopBarcodeErr \n" + e.getMessage());
        }
    }

    /*
     *RFID?????????????????????
     */
    protected void startRFIDScan(RFIDDataDelegate delegate, int powerLevel) {

        // SP1???????????????????????????????????????????????????
        if (Application.commScanner == null) { return; }

        try {

            // RFID?????????????????????
            if (this.mRfidScanner == null) {
                this.mRfidScanner = Application.commScanner.getRFIDScanner();
            }

            // ????????????????????????
            if (powerLevel <= 0) {
                // ???????????????????????????????????????
                powerLevel = Application.scannerPowerLevel;
            }

            //Delegate??????
            Application.log.d(TAG, "START:Setting Delegate");
            this.mRfidScanner.setDataDelegate(delegate);

            // RFID???????????????
            Application.log.d(TAG, "START:RFIDSetting");
            // RFID?????????????????????
            RFIDScannerSettings settings = this.mRfidScanner.getSettings();
            // ?????????????????????
            settings.scan.triggerMode = RFIDScannerSettings.Scan.TriggerMode.MOMENTARY;
            // ????????????
            settings.scan.powerLevelRead = powerLevel;
            // ??????????????????(?????????)
            settings.scan.doubleReading = RFIDScannerSettings.Scan.DoubleReading.PREVENT1;
            // ??????(??????)
            settings.scan.polarization = RFIDScannerSettings.Scan.Polarization.Both;
            // ???????????????
            settings.scan.sessionFlag = Application.scannerSessionFlag;
            // ???????????????
            settings.scan.channel = Application.scannerChannel;
            this.mRfidScanner.setSettings(settings);

            // inventory????????????
            Application.log.d(TAG, "START:Open Inventory");
            this.mRfidScanner.openInventory();
        } catch (RFIDException re) {
            re.printStackTrace();
            Application.log.e(TAG, "StartRFIDScanErr:" + re.getMessage());
            // ?????????
            if (re.getErrorCode() == ErrorCode.SCANNER_CHARGING) {
                mUtilListener.showSnackBarOnUiThread(R.string.err_message_E9012);
            } else {
                mUtilListener.showSnackBarOnUiThread("Err.StartRFID \n" + re.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, "StartRFIDScanErr:" + e.getMessage());
            mUtilListener.showSnackBarOnUiThread("Err.StartRFID \n" + e.getMessage());
        }
    }

    /*
     *RFID?????????????????????
     */
    protected void stopRFIDScan() {

        if (Application.commScanner == null) {  return; }
        if (this.mRfidScanner == null) { return; }

        try {
            //inventoryClose
            Application.log.d(TAG, "STOP:Close Inventory");
            this.mRfidScanner.close();

            //Delegate??????(null)
            Application.log.d(TAG, "STOP:Setting Delegate = null");
            this.mRfidScanner.setDataDelegate(null);

            this.mRfidScanner = null;
        } catch (RFIDException e) {
            e.printStackTrace();
            Application.log.e(TAG, "stopRFIDScan:" + e.getMessage());
            mUtilListener.showSnackBarOnUiThread("Err.StopRFID \n" + e.getMessage());
        }
    }
    // endregion
}
