package jp.co.khwayz.eleEntExtManage.picking;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
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
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.PickingCargoScanListRecyclerViewAdapter;
import jp.co.khwayz.eleEntExtManage.adapter.TagInvoiceViewHolder;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoInvoiceDetailInfo;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPickingCargoScanBinding;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.MessageDialogFragment;
import jp.co.khwayz.eleEntExtManage.fragment.CheckPackInstructionsFragment;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.response.SyukkoInvoiceDetailGetResponse;
import jp.co.khwayz.eleEntExtManage.http.task.get.SyukkoInvoiceDetailGetTask;
import jp.co.khwayz.eleEntExtManage.http.task.get.SyukkoInvoiceSearchTask;
import jp.co.khwayz.eleEntExtManage.http.task.post.PostPickingDataRegistTask;
import jp.co.khwayz.eleEntExtManage.util.Util;

public class PickingCargoScanFragment extends BaseFragment
        implements RFIDDataDelegate, BarcodeDataDelegate, PickingCargoScanListRecyclerViewAdapter.OnItemClickListener {

    // DataBinding
    FragmentPickingCargoScanBinding mBinding;
    // Adapter
    private PickingCargoScanListRecyclerViewAdapter mTagListAdapter;
    // List
    /** 画面リスト */
    private RecyclerView mTagScanList;
    /** 出庫予定Invoice情報受信データ */
    private List <SyukkoInvoiceDetailInfo> mPickingInvoiceDetailList;
    // スキャナー制御
    private Constants.ScanMode mScanMode = Constants.ScanMode.QR;
    // 読取モード変更中フラグ
    private boolean mScanModeChangeFlg = false;
    // 動作モード
    private boolean mActionMode = false; // false：ロケーション番号読取　true：荷札読取
    // ロケーション番号
    private String mLocationNumber;
    // リスト件数
    private int mListCheckedCount;
    private int mListTotalCount;

    // ARGS
    private static final String ARGS_INVOICE_NO = "invoice_no";
    private static final String ARGS_CUSTOMER_NAME = "customer_name";
    private String invoiceNo;
    private String customername;

    public static PickingCargoScanFragment newInstance(String invoiceNo, String customerName) {
        PickingCargoScanFragment fragment = new PickingCargoScanFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_INVOICE_NO, invoiceNo);
        args.putString(ARGS_CUSTOMER_NAME, customerName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentPickingCargoScanBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        invoiceNo = args.getString(ARGS_INVOICE_NO);
        customername = args.getString(ARGS_CUSTOMER_NAME);
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
        mListener.setSubTitleText(getString(R.string.picking) + getString(R.string.picking_invoice_scan));
        mListener.setScreenId(getString(R.string.screen_id_picking_scan));

        // サブヘッダセット
        mBinding.cargoScanViewInvoiceNo.setText(invoiceNo);
        mBinding.cargoScanViewClient.setText(customername);

        // リストオブジェクト
        this.mTagScanList = mBinding.invoiceTagScanList;
        if(this.mPickingInvoiceDetailList == null) {
            this.mPickingInvoiceDetailList = new ArrayList<>();
        }

        // リストアダプタ生成
        this.mTagListAdapter = new PickingCargoScanListRecyclerViewAdapter(this.mPickingInvoiceDetailList, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mTagScanList.setHasFixedSize(true);
        this.mTagScanList.setLayoutManager(llm);
        this.mTagScanList.setAdapter(mTagListAdapter);
        this.mTagListAdapter.setOnItemClickListener(this);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        // 簿外あり タップ時
        mBinding.cargoScanButtonOffBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOffBookButtonClick();
            }
        });

        /*************************************/
        // フッターボタン
        /*************************************/
        // 戻るボタン設定
        View.OnClickListener backClickListener = view -> {
            onBackButtonClick(mBinding.getRoot());
        };
        mListener.setBackButton(backClickListener);

        // 指示内容確認ボタン
        ButtonInfo instructions = new ButtonInfo(getString(R.string.button_label_instructions_check), v -> instructionsButton());

        // 確定ボタン
        ButtonInfo confirm = new ButtonInfo(getString(R.string.confirm), v -> confirmButton());

        // 保留ボタン
        ButtonInfo onHold = new ButtonInfo(getString(R.string.button_label_on_hold), v -> onHoldButton());

        // QRボタン
        ButtonInfo qr = new ButtonInfo(getString(R.string.button_label_rfid), v -> qrButton());

        mListener.setFooterButton(null, instructions, confirm, onHold, qr);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();

        // 出庫予定Invoice情報受信
        if(this.mPickingInvoiceDetailList.size() <=0) {
            // リスト０件（初期描画時）
            syukkoyoteiInvoiceInfoReceive();
            changeActionMode(false);
        } else {
            // リストあり（簿外から戻った場合）
            mBinding.cargoScanViewReadCount.setText(this.mListCheckedCount + "/" + this.mListTotalCount);
            this.mTagListAdapter.notifyDataSetChanged();
            changeActionMode(true);
        }

        // 簿外リスト判定
        setOffBookButtonState(false);
        if(Application.scanOffBooklList == null) {
            Application.scanOffBooklList = new ArrayList<>();
        } else {
            // 簿外ありの場合、ボタン可視化
            if(Application.scanOffBooklList.size() > 0) setOffBookButtonState(true);
        }

        // スキャナー接続
        mScanMode = Constants.ScanMode.QR;
        openScanner();
    }

    /**
     * 簿外ボタンタップ
     */
    protected void onOffBookButtonClick(){
        // スキャナー切断
        closeScanner(mScanMode);
        mListener.replaceFragmentWithStack(PickingOffBookFragment.newInstance(
                this.invoiceNo, this.customername, mBinding.cargoScanViewReadCount.getText().toString()), TAG);
    }

    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        // リスナー生成
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // スキャナー切断
            closeScanner(mScanMode);
            // 前画面に戻る
            mListener.popBackStack();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I3001, listener);
    }

    /**
     * 指示内容確認ボタンタップ
     */
    private void instructionsButton() {
        // スキャナー切断
        closeScanner(mScanMode);

        // インパラ生成
        Bundle b = new Bundle();
        b.putString("screenPrefix", "P");

        // 次画面遷移
        CheckPackInstructionsFragment nextFragment = new CheckPackInstructionsFragment();
        nextFragment.setArguments(b);
        mListener.replaceFragmentWithStack(nextFragment, TAG);
    }

    private void confirmButton() {
        //　読込済みデータなし
        if(!isPicking()) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2005));
            return;
        }

        // 未読込みあり（１ロケーションピッキング完了）
        if(this.mListCheckedCount < this.mListTotalCount) {
            // リスナー生成
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                // １ロケーション完了処理
                completeOneLocation();
            };
            mUtilListener.showConfirmDialog(R.string.info_message_I3002, listener
                    , mLocationNumber, String.valueOf(mListCheckedCount), String.valueOf(mListTotalCount));
        } else {
            // ピッキング完了
            // リスナー生成
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                // ピッキング全完了処理
                completePickingAll();
            };
            mUtilListener.showConfirmDialog(R.string.info_message_I3003, listener
                    , invoiceNo, String.valueOf(mListCheckedCount), String.valueOf(mListTotalCount));
        }
    }

    /* 保留ボタン */
    private void onHoldButton() {
        // リスナー生成
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // 保留登録
            pickingHoldRegist();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I3004, listener
                , invoiceNo, String.valueOf(mListCheckedCount), String.valueOf(mListTotalCount));
    }

    /**
     * １ロケーションピッキング完了
      */
    private void completeOneLocation(){
        // 簿外ありの場合
        if(Application.scanOffBooklList.size() > 0) {
            onOffBookButtonClick();
            return;
        }

        // ピッキング済行消去
        this.mPickingInvoiceDetailList.removeIf(list -> list.getPickingFlag().equals("1"));
        mTagListAdapter.notifyDataSetChanged();

        // 動作モード変更
        changeActionMode(false);
    }

    /**
     * 全ピッキング完了
      */
    private void completePickingAll(){
        // 簿外ありの場合
        if(Application.scanOffBooklList.size() > 0) {
            onOffBookButtonClick();
             return;
        }

        // ピッキング完了登録
        pickingDataRegist(Constants.FLAG_FALSE);
    }

    /**
     * 保留登録
     */
    private void pickingHoldRegist(){
        // 簿外ありの場合
        if(Application.scanOffBooklList.size() > 0) {
            onOffBookButtonClick();
            return;
        }

        // ピッキング完了登録
        pickingDataRegist(Constants.FLAG_TRUE);
    }

    /**
     * 動作モード変更
     */
    private void changeActionMode(boolean actionMode){
        // 荷札読取モード
        if(actionMode){
            mBinding.cargoScanTextMessage.setText(R.string.cargo_scan_msg_cargo_read);
            mBinding.cargoScanTextLocationNumber.setVisibility(View.VISIBLE);
            mBinding.cargoScanTextLocationNumber.setText(getString(R.string.cargo_scan_msg_location_no_title) + this.mLocationNumber);
        }else{
            // ロケ番号読取りモード
            mBinding.cargoScanTextMessage.setText(R.string.cargo_scan_msg_location_no_read);
            mBinding.cargoScanTextLocationNumber.setVisibility(View.INVISIBLE);
        }
        this.mActionMode = actionMode;
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
            // 動作モード：ロケーション読取の場合は変更しない
            if(!mActionMode) return;

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
     * リスト行タップ時処理：ピッキング済の場合に解除する
      * @param holder
     */
    @Override
    public void onItemClick(TagInvoiceViewHolder holder) {
        // タップ位置のデータ取得
        PickingCargoScanListRecyclerViewAdapter adapter = (PickingCargoScanListRecyclerViewAdapter)this.mTagScanList.getAdapter();
        int position = holder.getAdapterPosition();
        SyukkoInvoiceDetailInfo updateData = this.mPickingInvoiceDetailList.get(position);

        // ピッキング済フラグ更新
        updateData.setPickingFlag(Constants.FLAG_FALSE);
        new SyukkoShijiDetailDao().updatePickingFlg(Application.dbHelper.getWritableDatabase()
                , updateData.getInvoiceNo(), updateData.getRenban().toString(), updateData.getLineNo().toString(), Constants.FLAG_FALSE);
        mTagListAdapter.notifyDataSetChanged();

        // 件数更新（分子にピッキング済み件数セット）
        this.mListCheckedCount--;
        mBinding.cargoScanViewReadCount.setText(this.mListCheckedCount + "/" + this.mListTotalCount);
    }

    /**
     * 出庫予定Invoice情報受信
     */
    private void syukkoyoteiInvoiceInfoReceive(){
        try {
            // 出庫予定Invoice情報受信
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("invoiceNo", mBinding.cargoScanViewInvoiceNo.getText().toString());
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_SYUKKOINVOICEDETAIL_GET;
            new SyukkoInvoiceDetailGetTask(syukkoInvoiceDetailGetCallback, url, jsonObject.toString()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
        }
    }

    /**
     * 出庫予定Invoice情報受信Callback
     */
    SyukkoInvoiceSearchTask.Callback<SyukkoInvoiceDetailGetResponse> syukkoInvoiceDetailGetCallback = new SyukkoInvoiceDetailGetTask.Callback<SyukkoInvoiceDetailGetResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialogを表示する
            if (showProgress) {
                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0030));
            }
        }
        @Override
        public void onTaskFinished(SyukkoInvoiceDetailGetResponse response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();

            // エラーレスポンスの場合
            if (!Constants.API_RESPONSE_STATUS_CODE_OK.equals(response.getStatus())) {
                Application.log.e(TAG, "SyukkoInvoiceSearchTask ERR status=" + response.getStatus() + " ,errorCode=" + response.getErrorCode());
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9001));
                mListener.popBackStack();
                return;
            }

            // 検索結果ヒットなしの場合
            if (response.getData().getList().size() <= 0) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2006));
                mListener.popBackStack();
                return;
            }

            try {
                // 取得データDB登録
                new SyukkoShijiDetailDao().bulkInsert(Application.dbHelper.getWritableDatabase(), response.getData().getList());
            } catch (SQLiteException e) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
                Application.log.e(TAG, e);
                return;
            }

            // リスト更新
            listupdate(response.getData().getList());
        }

        /**
         *
         * @param httpResponseStatusCode
         * @param messageId
         */
        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // エラーメッセージを表示
            mUtilListener.showAlertDialog(messageId);
        }
    };

    /**
     * 明細行更新
     * @param list  ピッキング済も含んだ出庫指示明細リスト全量
     */
    public void listupdate(ArrayList<SyukkoInvoiceDetailInfo> list){
        // 明細更新：リストに「ピッキング済み」データのみ詰める
        int readedCount = 0;
        this.mPickingInvoiceDetailList.clear();
        for (SyukkoInvoiceDetailInfo item : list) {
            if(item.getPickingFlag().equals("1")) {
                readedCount++;
                continue;
            }
            SyukkoInvoiceDetailInfo new_item = new SyukkoInvoiceDetailInfo(item.getInvoiceNo());
            new_item.setInvoiceNo(item.getInvoiceNo());
            new_item.setLineNo(item.getLineNo());
            new_item.setPlaceOrderNo(item.getPlaceOrderNo());
            new_item.setReceiveOrderNo(item.getReceiveOrderNo());
            new_item.setBranchNo(item.getBranchNo());
            new_item.setItemCode(item.getItemCode());
            new_item.setItemName(item.getItemName());
            new_item.setIssueQuantity(item.getIssueQuantity());
            new_item.setIssueQuantitySummery(item.getIssueQuantitySummery());
            new_item.setUnit(item.getUnit());
            new_item.setStockQuantity(item.getStockQuantity());
            new_item.setPickingFlag(item.getPickingFlag());
            new_item.setPackingType(item.getPackingType());
            new_item.setLocationNo(item.getLocationNo());
            this.mPickingInvoiceDetailList.add(new_item);
        }
        mTagListAdapter.notifyDataSetChanged();


        // 件数更新（分子にピッキング済み件数セット）
        this.mListTotalCount = list.size();
        this.mListCheckedCount = readedCount;
        mBinding.cargoScanViewReadCount.setText(this.mListCheckedCount + "/" + this.mListTotalCount);
    }

    /**
     * 簿外ボタン可視状態設定
     * @param buttonState   true：可視     false：不可視
     */
    private void setOffBookButtonState(boolean buttonState){
        if(buttonState){
            mBinding.cargoScanButtonOffBook.setVisibility(View.VISIBLE);
        } else {
            mBinding.cargoScanButtonOffBook.setVisibility(View.INVISIBLE);
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

                    // ロケーション番号読取モードの場合
                    if(!this.mActionMode){
                        // QRコードからロケ番号取得
                        this.mLocationNumber = qrCode;

                        // 作業確認メッセージ
                        // 警告メッセージ表示
                        MessageDialogFragment dialog = new MessageDialogFragment(getContext());
                        dialog.setMessage(String.format(mUtilListener.getDataBaseMessage(R.string.info_message_I0017), this.mLocationNumber));
                        dialog.setPositiveButton(getString(R.string.ok), v -> {
                            dialog.dismiss();
                            // モード切替
                            changeActionMode(true);                        });
                        dialog.setNegativeButton(getString(R.string.cancel), v -> dialog.dismiss());
                        dialog.show(getActivity().getSupportFragmentManager(), "MessageDialogFragment");

                    } else {
                        // 荷札読取モードの場合
                        // 荷札情報生成
                        TagInfo tagInfo = Util.convertQrCodeToTagInfo(qrCode);

                        // nullの場合は読み飛ばし
                        if (tagInfo == null) { return; }

                        // 読取データ登録
                        registReadTagData(tagInfo);
                    }
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

                    // 読取データ登録
                    registReadTagData(tagInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Application.log.e(TAG, e);
                mUtilListener.showSnackBarOnUiThread(TAG + " onRFIDDataReceived \n" + e.getMessage());
            }
        });
    }

    /**
     * 読込データ登録
     * @param tagInfo
     */
    private void registReadTagData(TagInfo tagInfo) {
        // 簿外データチェック
        if(isOffBookData(tagInfo)) return;

        // リスト突合せ処理
        updatePickingFlg(tagInfo);
    }

    /**
     * 簿外データチェック
     * @param tagInfo
     * @return
     */
    private boolean isOffBookData(TagInfo tagInfo) {
        boolean bRet = false;
        // 画面リストに存在しない
        // 発注番号、枝番、ロケ番号が不一致
        if(this.mPickingInvoiceDetailList.stream().noneMatch(x ->
                x.getPlaceOrderNo().equals(tagInfo.getPlaceOrderNo())
                        && x.getBranchNo() == tagInfo.getBranchNo()
                        && x.getLocationNo().equals(this.mLocationNumber))) {

            // 簿外データボタン活性化
            bRet = true;
            setOffBookButtonState(true);

            // 簿外リストに存在しない場合は追加（重複登録ガード）
            if (Application.scanOffBooklList.stream().noneMatch(x ->
                    x.getPlaceOrderNo().equals(tagInfo.getPlaceOrderNo()) && x.getBranchNo() == tagInfo.getBranchNo())) {
                TagInfo offBookData = new TagInfo(tagInfo.getPlaceOrderNo(), tagInfo.getBranchNo());
                Application.scanOffBooklList.add(offBookData);
            }
        }
        return bRet;
    }

    /**
     * ピッキング済みフラグ更新（ローカル）
     * @param tagInfo
     */
    private void updatePickingFlg(TagInfo tagInfo) {
        // 一致データ検索（簿外チェックしてるから必ずある）
        SyukkoInvoiceDetailInfo updateData = this.mPickingInvoiceDetailList.stream().filter(
                list -> list.getPlaceOrderNo().equals(tagInfo.getPlaceOrderNo())).filter(
                        list -> list.getBranchNo() == tagInfo.getBranchNo()).findFirst()
                .orElse(new SyukkoInvoiceDetailInfo(""));

        // ピッキング済の場合
        if(updateData.getPickingFlag().equals(Constants.FLAG_TRUE)){
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2007));
            return;
        }

        // ピッキング済フラグ更新
        updateData.setPickingFlag(Constants.FLAG_TRUE);
        new SyukkoShijiDetailDao().updatePickingFlg(Application.dbHelper.getWritableDatabase()
                , updateData.getInvoiceNo(), updateData.getRenban().toString(), updateData.getLineNo().toString(), Constants.FLAG_TRUE);
        mTagListAdapter.notifyDataSetChanged();

        // 件数更新（分子にピッキング済み件数セット）
        this.mListCheckedCount++;
        mBinding.cargoScanViewReadCount.setText(this.mListCheckedCount + "/" + this.mListTotalCount);
    }

    /**
     * ピッキング済みデータチェック
     */
    private boolean isPicking() {
        // ピッキング済データ検索
        SyukkoInvoiceDetailInfo updateData = this.mPickingInvoiceDetailList.stream().filter(
                list -> list.getPickingFlag().equals(Constants.FLAG_TRUE)).findFirst()
                .orElse(null);
        // ピッキング済データあり
        if(updateData != null){
            return true;
        }
        return false;
    }

    /**
     * ピッキングデータ登録
     * @param type  0：完了登録  1：保留登録
     */
    private void pickingDataRegist(String type) {
        try {
            // ピッキングデータ登録
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_PICKING_REGIST;
            new PostPickingDataRegistTask(pickingDataRegistCallback, url, this.invoiceNo, type).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
        }
    }

    /**
     * ピッキングデータ登録Callback
     */
    PostPickingDataRegistTask.Callback<SimpleResponse> pickingDataRegistCallback = new PostPickingDataRegistTask.Callback<SimpleResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialogを表示する
            if (showProgress) {
                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0031));
            }
        }

        @Override
        public void onTaskFinished(SimpleResponse response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();

            // エラーレスポンスの場合
            if (!Constants.API_RESPONSE_STATUS_CODE_OK.equals(response.getStatus())) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9001));
                Application.log.e(TAG, "status=" + response.getStatus() + " ,errorCode=" + response.getErrorCode());
                return;
            }

            // メッセージ表示
            mUtilListener.showSnackBarOnUiThread(mUtilListener.getDataBaseMessage(R.string.info_message_I0007));

            // 画面遷移
            mListener.popBackStack();
        }

        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // エラーメッセージを表示
            mUtilListener.showAlertDialog(messageId);
        }
    };
}
