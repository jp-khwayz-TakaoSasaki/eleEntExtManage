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
     * View関連の設定
     */
    @Override
    public void viewSetting() { }

    /**
     * イベント関連の設定
     */
    @Override
    public void eventSetting() { }

    /**
     * 内部処理関連の設定
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
        //ソフトキーボードを非表示
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
     * トーストを表示します
     * @param message : 表示するメッセージ
     */
    protected void toastOnUiThread(final String message) {
        mHandler.post(() -> Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show());
    }

    /**
     * トーストを表示します
     * @param messageId : 表示するメッセージリソースID
     */
    protected void toastOnUiThread(final int messageId, Object... option) {
        String msg;
        // メッセージを取得
        msg = mUtilListener.getDataBaseMessage(messageId);
        // Optionがある場合
        if (option != null && 0 < option.length) {
            msg = String.format(msg, option);
        }
        // トースト表示
        toastOnUiThread(msg);
    }

    /**
     * 発注番号入力チェック
     * @param orderNo 検証する発注番号
     * @return true = 無効, false = 有効
     */
    protected boolean isNotValidOrderNo(String orderNo) {
        // 数値はOK
        if (Util.isNumber(orderNo)) { return false; }
        // エラーメッセージ表示
        mUtilListener.showAlertDialog(R.string.err_message_E1003,
                Constants.MSG_OPTION_ORDER_NO, Constants.MSG_OPTION_ORDER_NO);
        return true;
    }

    /**
     * 荷札入力チェック
     * @param orderNo 検証する発注番号
     * @param branchNo 検証する枝番
     * @return true = 無効, false = 有効
     */
    protected boolean isNotValidTagNo(String orderNo, String branchNo) {
        // 発注番号が未入力または半角数字以外
        if (!Util.isNumber(orderNo)) {
            // エラーメッセージ表示
            mUtilListener.showAlertDialog(R.string.err_message_E1003,
                    Constants.MSG_OPTION_ORDER_NO, Constants.MSG_OPTION_ORDER_NO);
            return true;
        }
        // 枝番が未入力または半角数字以外
        if (!Util.isNumber(branchNo)) {
            // エラーメッセージ表示
            mUtilListener.showAlertDialog(R.string.err_message_E1003,
                    Constants.MSG_OPTION_BRANCH_NO, Constants.MSG_OPTION_BRANCH_NO);
            return true;
        }
        return false;
    }

    /**
     * ソフトウェアキーボードを閉じる
     */
    protected void hideKeyboard() {
        // ソフトウェアキーボードを閉じる
        if (Application.mainActivity.getCurrentFocus() != null) {
            InputMethodManager mgr = (InputMethodManager) Application.mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(Application.mainActivity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * ソフトウェアキーボードを表示
     * @param view フォーカスがセットされているView
     */
    protected void showKeyboard(View view) {
        // ソフトウェアキーボードを表示
        if (Application.mainActivity.getCurrentFocus() != null) {
            InputMethodManager mgr = (InputMethodManager) Application.mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view.requestFocus()) {
                mgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    // region [ ボタン押下 ]
    /**
     * 戻るボタン押下処理
     * @param view : クリックされたボタン
     */
    protected abstract void onBackButtonClick(View view);

    // endregion

    // region [ スキャナー関連 ]
    /**
     * スキャナー接続
     */
    protected abstract void openScanner();

    /**
     * スキャナーを閉じる
     * @param mode : スキャナーモード
     */
    protected void closeScanner(Constants.ScanMode mode) {
        if (mode == Constants.ScanMode.RFID) {
            stopRFIDScan();
        } else {
            stopBarcodeScan();
        }
    }

    /*
     *バーコードスキャンを行うための準備
     */
    protected void startBarcodeScan(BarcodeDataDelegate delegate) {

        if (Application.commScanner == null) { return; }

        //バーコードスキャナのインスタンスを取得
        if (mBarcodeScanner == null) {
            Application.log.d(TAG, "START:Barcode instance作成");
            mBarcodeScanner = Application.commScanner.getBarcodeScanner();
        }

        if (mBarcodeScanner != null) {
            try {
                //バーコードのDelegateを設定(Listener)
                Application.log.d(TAG, "START:Barcode Delegate設定");
                mBarcodeScanner.setDataDelegate(delegate);

                //バーコードスキャナの設定
                Application.log.d(TAG, "START:BarcodeScanner設定");
                BarcodeScannerSettings barcodeScannerSettings = mBarcodeScanner.getSettings();
                barcodeScannerSettings.scan.triggerMode = BarcodeScannerSettings.Scan.TriggerMode.MOMENTARY;

                // QRコードのみ読取
                barcodeScannerSettings.decode.symbologies.qrCode.enabled = true;
                barcodeScannerSettings.decode.symbologies.qrCode.model1.enabled = true;
                barcodeScannerSettings.decode.symbologies.qrCode.model2.enabled = true;
                barcodeScannerSettings.decode.symbologies.microQr.enabled = true;
                mBarcodeScanner.setSettings(barcodeScannerSettings);

                //バーコードスキャナを開始する
                Application.log.d(TAG, "START:Barcodeスキャン開始");
                mBarcodeScanner.openReader();

            } catch (BarcodeException e) {
                e.printStackTrace();
                Application.log.e(TAG, e);
                mUtilListener.showSnackBarOnUiThread("StartBarcodeErr \n" + e.getMessage());
            }
        }
    }

    /*
     *バーコードスキャンを停止
     */
    protected void stopBarcodeScan() {
        if (Application.commScanner == null) { return; }
        if (mBarcodeScanner == null) { return; }

        try {
            //バーコードスキャナを終了する
            Application.log.d(TAG, "START:Barcodeスキャン終了");
            this.mBarcodeScanner.closeReader();

            Application.log.d(TAG, "START:Barcode delegate解放");
            this.mBarcodeScanner.setDataDelegate(null);

            this.mBarcodeScanner = null;
        } catch (BarcodeException e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showSnackBarOnUiThread("StopBarcodeErr \n" + e.getMessage());
        }
    }

    /*
     *RFIDスキャンの開始
     */
    protected void startRFIDScan(RFIDDataDelegate delegate, int powerLevel) {

        // SP1と接続されていない場合は何もしない
        if (Application.commScanner == null) { return; }

        try {

            // RFIDスキャナー取得
            if (this.mRfidScanner == null) {
                this.mRfidScanner = Application.commScanner.getRFIDScanner();
            }

            // 電波強度指定なし
            if (powerLevel <= 0) {
                // 設定から電波強度を取得する
                powerLevel = Application.scannerPowerLevel;
            }

            //Delegate設定
            Application.log.d(TAG, "START:Setting Delegate");
            this.mRfidScanner.setDataDelegate(delegate);

            // RFID設定を設定
            Application.log.d(TAG, "START:RFIDSetting");
            // RFIDスキャナー設定
            RFIDScannerSettings settings = this.mRfidScanner.getSettings();
            // トリガーモード
            settings.scan.triggerMode = RFIDScannerSettings.Scan.TriggerMode.MOMENTARY;
            // 電波強度
            settings.scan.powerLevelRead = powerLevel;
            // 二度読み防止(読取中)
            settings.scan.doubleReading = RFIDScannerSettings.Scan.DoubleReading.PREVENT1;
            // 偏波(両方)
            settings.scan.polarization = RFIDScannerSettings.Scan.Polarization.Both;
            // セッション
            settings.scan.sessionFlag = Application.scannerSessionFlag;
            // チャンネル
            settings.scan.channel = Application.scannerChannel;
            this.mRfidScanner.setSettings(settings);

            // inventoryオープン
            Application.log.d(TAG, "START:Open Inventory");
            this.mRfidScanner.openInventory();
        } catch (RFIDException re) {
            re.printStackTrace();
            Application.log.e(TAG, "StartRFIDScanErr:" + re.getMessage());
            // 充電中
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
     *RFIDスキャンの終了
     */
    protected void stopRFIDScan() {

        if (Application.commScanner == null) {  return; }
        if (this.mRfidScanner == null) { return; }

        try {
            //inventoryClose
            Application.log.d(TAG, "STOP:Close Inventory");
            this.mRfidScanner.close();

            //Delegate設定(null)
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
