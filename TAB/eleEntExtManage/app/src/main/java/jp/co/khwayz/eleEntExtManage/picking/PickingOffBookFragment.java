package jp.co.khwayz.eleEntExtManage.picking;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.densowave.scannersdk.Barcode.BarcodeData;
import com.densowave.scannersdk.Barcode.BarcodeDataReceivedEvent;
import com.densowave.scannersdk.Common.CommScanner;
import com.densowave.scannersdk.Const.CommConst;
import com.densowave.scannersdk.Listener.BarcodeDataDelegate;
import com.densowave.scannersdk.Listener.RFIDDataDelegate;
import com.densowave.scannersdk.RFID.RFIDData;
import com.densowave.scannersdk.RFID.RFIDDataReceivedEvent;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.OffBookListRecyclerViewAdapter;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoInvoiceDetailInfo;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPickingOffBookBinding;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.MessageDialogFragment;
import jp.co.khwayz.eleEntExtManage.util.Util;

public class PickingOffBookFragment extends BaseFragment implements RFIDDataDelegate, BarcodeDataDelegate {
    // DataBinding
    FragmentPickingOffBookBinding mBinding;
    // Adapter
    private OffBookListRecyclerViewAdapter mOffBookListAdapter;
    // 画面リスト
    private RecyclerView mOffBookInfoListRecyclerView;
    private List<SyukkoInvoiceDetailInfo> mOffBookInfoList;
    // スキャナー制御
    private Constants.ScanMode mScanMode = Constants.ScanMode.RFID;
    // 読取モード変更中フラグ
    private boolean mScanModeChangeFlg = false;
    // 削除処理中フラグ
    private boolean mDeletingFlg = false;

    // ARGS
    private static final String ARGS_INVOICE_NO = "invoice_no";
    private static final String ARGS_CUSTOMER_NAME = "customer_name";
    private static final String ARGS_READ_COUNT = "read_count";
    private String invoiceNo;
    private String customername;
    private String readCount;

    public static PickingOffBookFragment newInstance(String invoiceNo, String customerName, String readCount) {
        PickingOffBookFragment fragment = new PickingOffBookFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_INVOICE_NO, invoiceNo);
        args.putString(ARGS_CUSTOMER_NAME, customerName);
        args.putString(ARGS_READ_COUNT, readCount);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentPickingOffBookBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.invoiceNo = args.getString(ARGS_INVOICE_NO);
        this.customername = args.getString(ARGS_CUSTOMER_NAME);
        this.readCount = args.getString(ARGS_READ_COUNT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void viewSetting() {
        super.viewSetting();
        // ボタン設定
        mListener.setGroupsVisibility(true, true, true);
        mListener.setFooterButton(null, null, null, null, null);

        // リーダー接続アイコン・電池アイコン
        mListener.setReaderConnectButtonVisibility(View.VISIBLE);
        mListener.setBatteryStateImageVisibility(View.VISIBLE);

        // タイトル
        mListener.setSubTitleText(getString(R.string.picking) + getString(R.string.picking_invoice_scan_off_book));
        mListener.setScreenId(getString(R.string.screen_id_picking_offbook));

        // サブヘッダセット
        mBinding.offBookConfirmViewInvoiceNo.setText(invoiceNo);
        mBinding.offBookConfirmViewClient.setText(customername);
        mBinding.offBookConfirmViewReadCount.setText(readCount);

        // リストオブジェクト
        this.mOffBookInfoList = new ArrayList<>();
        this.mOffBookInfoListRecyclerView = mBinding.offBookConfirmList;

        // リストView生成
        this.mOffBookListAdapter = new OffBookListRecyclerViewAdapter(Application.scanOffBooklList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mOffBookInfoListRecyclerView.setHasFixedSize(true);
        this.mOffBookInfoListRecyclerView.setLayoutManager(llm);
        this.mOffBookInfoListRecyclerView.setAdapter(mOffBookListAdapter);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();

        /*************************************/
        // フッターボタン
        /*************************************/
        // 戻るボタン設定
        View.OnClickListener backClickListener = view -> {
            onBackButtonClick(mBinding.getRoot());
        };
        mListener.setBackButton(backClickListener);

        // OKボタン
        View.OnClickListener okClickListener = v -> onBackButtonClick(getView());
        ButtonInfo ok = new ButtonInfo(getString(R.string.ok), okClickListener);

        // QRボタン
        View.OnClickListener qrClickListener = v -> qrButton();
        ButtonInfo qr = new ButtonInfo(getString(R.string.button_label_qr), qrClickListener);

        mListener.setFooterButton(ok, null, null, null, qr);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        // スキャナー接続
        mScanMode = Constants.ScanMode.RFID;
        openScanner();
    }

    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        // リストに明細が残存
        if(Application.scanOffBooklList.size()>0){
            // リスナー生成
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                // スキャナー切断
                closeScanner(mScanMode);
                // 前画面に戻る
                mListener.popBackStack();
            };
            mUtilListener.showConfirmDialog(R.string.info_message_I3005, listener);
        } else {
            // スキャナー切断
            closeScanner(mScanMode);
            // 前画面に戻る
            mListener.popBackStack();
        }
    }

    /**
     *  QR/RFIDボタン押下時処理
     * */
    private void qrButton() {
        // スキャナーと接続されていない場合
        if (Application.commScanner == null) {
            // メッセージを表示
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                // リーダー選択画面を表示
                mListener.showParingReaderActivity();
            };
            mUtilListener.showInformationDialog(R.string.err_message_E9005, listener);
            return;
        }

        if(!this.mScanModeChangeFlg) {
            this.mScanModeChangeFlg = true;

            // QRモード
            if (this.mScanMode == Constants.ScanMode.QR) {
                // SP1をRFIDモードに設定
                mScanMode = Constants.ScanMode.RFID;
                stopBarcodeScan();
                startRFIDScan(this, 0);
                toastOnUiThread(Constants.TOAST_CHANGE_RFID);
            } else {
                // SP1をQRモードに設定
                mScanMode = Constants.ScanMode.QR;
                stopRFIDScan();
                startBarcodeScan(this);
                toastOnUiThread(Constants.TOAST_CHANGE_QR);
            }

            // ボタンラベル変更
            mListener.changeFooterButtonLabel(5);
            this.mScanModeChangeFlg = false;
        }
    }

    /**
     * スキャナー使用有無
     * @return  true：スキャナー使用    false：スキャナー不使用
     */
    @Override
    public boolean hasScanner() {
        return true;
    }

    /**
     * スキャナー接続時処理
     */
    @Override
    protected void openScanner() {
        if (mScanMode == Constants.ScanMode.RFID) {
            startRFIDScan(this, 0);
        } else {
            startBarcodeScan(this);
        }
    }

    /**
     * スキャナー接続状態変更時処理
     * @param status 接続状態
     */
    @Override
    public void CommStatusChanged(CommConst.ScannerStatus status) {
        try {
            switch (status) {
                case CLAIMED:
                    openScanner();
                    break;
                case CLOSED:
                    closeScanner(mScanMode);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showSnackBarOnUiThread(e.getMessage());
        }
    }

    @Override
    public void onBarcodeDataReceived(CommScanner commScanner, BarcodeDataReceivedEvent barcodeDataReceivedEvent) {
        try {
            //barcodeデータ取得
            if (barcodeDataReceivedEvent == null) { return; }
            final List<BarcodeData> barcodeDataList = barcodeDataReceivedEvent.getBarcodeData();
            if (barcodeDataList != null && barcodeDataList.size() > 0) {
                mHandler.post(() -> {
                    // QRコード生成
                    byte[] bytes = barcodeDataList.get(0).getData();
                    final String qrCode = Util.convertBytesToASCII(bytes);

                    // 荷札情報生成
                    TagInfo tagInfo = Util.convertQrCodeToTagInfo(qrCode);

                    // nullの場合は読み飛ばし
                    if (tagInfo == null) {
                        return;
                    }

                    // 読取データ行削除
                    deleteCheckedOffBookData(tagInfo);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showSnackBarOnUiThread(TAG + " onBarcodeDataReceived \n" + e.getMessage());
        }
    }

    @Override
    public void onRFIDDataReceived(CommScanner commScanner, RFIDDataReceivedEvent rfidDataReceivedEvent) {
        mHandler.post(() -> {
            List<RFIDData> rfidDataList = rfidDataReceivedEvent.getRFIDData();
            try {
                for (RFIDData rfidData : rfidDataList) {
                    // 読取データ取得
                    byte[] uii = rfidData.getUII();
                    // 荷札情報生成
                    // 　★ビット数、企業コード、読取付加チェック異常はnullを返却★
                    TagInfo tagInfo = Util.convertEpcToTagInfo(uii);
                    // 対象外は読み飛ばし
                    if (tagInfo == null) { continue; }

                    // 読取データ行削除
                    deleteCheckedOffBookData(tagInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Application.log.e(TAG, e);
                mUtilListener.showSnackBarOnUiThread(TAG + " onRFIDDataReceived \n" + e.getMessage());
            }
        });
    }

    /**
     * 確認簿外データ消去
     * @param tagInfo
     */
    private void deleteCheckedOffBookData(TagInfo tagInfo) {
        if(mDeletingFlg)return;

        // リスト内存在確認
        TagInfo offBookData = Application.scanOffBooklList.stream().filter(x -> x.getPlaceOrderNo().equals(tagInfo.getPlaceOrderNo()))
                .filter(x -> x.getBranchNo() == tagInfo.getBranchNo()).findFirst().orElse(null);
        if(offBookData != null){
            mDeletingFlg = true;
            MessageDialogFragment dialog = new MessageDialogFragment(getContext());
            String message = mUtilListener.getDataBaseMessage(R.string.info_message_I3006);
            dialog.setMessage(String.format(message, tagInfo.getPlaceOrderNo(), String.valueOf(tagInfo.getBranchNo())));
            dialog.setPositiveButton(getString(R.string.ok), v -> {
                Application.scanOffBooklList.removeIf(list -> list.getPlaceOrderNo().equals(tagInfo.getPlaceOrderNo())
                        && list.getBranchNo() == tagInfo.getBranchNo());
                // 再描画
                this.mOffBookListAdapter.notifyDataSetChanged();
                mDeletingFlg = false;
                dialog.dismiss();
            });
            dialog.setNegativeButton(getString(R.string.cancel), v -> {
                mDeletingFlg = false;
                dialog.dismiss();
            });
            dialog.show(getActivity().getSupportFragmentManager(), "MessageDialogFragment");
        }
    }
}
