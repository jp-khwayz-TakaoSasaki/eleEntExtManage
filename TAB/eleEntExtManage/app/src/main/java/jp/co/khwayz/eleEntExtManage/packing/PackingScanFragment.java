package jp.co.khwayz.eleEntExtManage.packing;

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
import java.util.Comparator;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.PackingScanInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoShijiKeyInfo;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPackingScanBinding;
import jp.co.khwayz.eleEntExtManage.instr_cfm.CheckPackInstructionsFragment;
import jp.co.khwayz.eleEntExtManage.util.Util;

public class PackingScanFragment extends BaseFragment
        implements RFIDDataDelegate, BarcodeDataDelegate, PackingScanRecyclerViewAdapter.OnItemClickListener, PackingScanRecyclerViewAdapter.SinglePackagingBtCallback {
    // DataBinding
    FragmentPackingScanBinding mBinding;
    // Adapter
    private PackingScanRecyclerViewAdapter mPackingScanAdapter;
    // List
    /** 画面リスト */
    private RecyclerView mPackingList;
    private List<PackingScanInfo> mPackingScanInfoList = null;

    // スキャナー制御
    private Constants.ScanMode mScanMode = Constants.ScanMode.QR;
    // 読取モード変更中フラグ
    private boolean mScanModeChangeFlg = false;

    // ARGS
    private static final String ARGS_INVOICE_NO = "invoice_no";
    private static final String ARGS_CS_NO = "caseMarkNo";
    private static String mInvoiceNo;
    private static String mCaseMarkNo;
    private int mCartonCount;
    private int mReadCount;

    /**
     * インスタンス生成（インパラ取込）
     * @param invoiceNo
     * @return
     */
    public static PackingScanFragment newInstance(String invoiceNo, String caseMarkNo) {
        PackingScanFragment fragment = new PackingScanFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_INVOICE_NO, invoiceNo);
        args.putString(ARGS_CS_NO, caseMarkNo);
        fragment.setArguments(args);

        // 初期化要でセット
        Application.initFlag = true;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentPackingScanBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mInvoiceNo = args.getString(ARGS_INVOICE_NO);
        this.mCaseMarkNo = args.getString(ARGS_CS_NO);
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
        // ボタン設定初期化
        mListener.setGroupsVisibility(true, true, true);
        mListener.setFooterButton(null, null, null, null, null);

        // リーダー接続アイコン・電池アイコン
        mListener.setReaderConnectButtonVisibility(View.VISIBLE);
        mListener.setBatteryStateImageVisibility(View.VISIBLE);

        // タイトル
        mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.packing_scan));
        mListener.setScreenId(getString(R.string.screen_id_packing_scan));

        // サブヘッダセット
        mBinding.packingScanViewInvoiceNo.setText(this.mInvoiceNo);

        // リストオブジェクト
        this.mPackingList = mBinding.packingScanList;
        if(this.mPackingScanInfoList == null) {
            this.mPackingScanInfoList = new ArrayList<>();
        }

        // リストアダプタ生成
        this.mPackingScanAdapter = new PackingScanRecyclerViewAdapter(this.mPackingScanInfoList, this);
        mPackingScanAdapter.setOnItemClickListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mPackingList.setHasFixedSize(true);
        this.mPackingList.setLayoutManager(llm);
        this.mPackingList.setAdapter(mPackingScanAdapter);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        // 簿外あり タップ時
        mBinding.packingScanButtonOffBook.setOnClickListener(new View.OnClickListener() {
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

        // 最終梱包ボタン
        ButtonInfo inputPackingInfo = new ButtonInfo(getString(R.string.button_label_final_packing), v -> inputFinalPackingButton());

        // 複数梱包ボタン
        ButtonInfo overPack = new ButtonInfo(getString(R.string.button_label_multiple_packing), v -> inputMultiplePackingButton());

        // QRボタン
        ButtonInfo qr = new ButtonInfo(getString(R.string.QR), v -> qrButton());

        mListener.setFooterButton(null, instructions, inputPackingInfo, overPack, qr);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();

        // 明細行初期化判定
        if(Application.initFlag) {
            // 明細行構築
            listCreate();
            Application.initFlag=false;
        } else {
            mPackingScanAdapter.notifyDataSetChanged();
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
        mScanMode = Constants.ScanMode.RFID;
        openScanner();
    }

    /**
     * 簿外ボタンタップ
     */
    protected void onOffBookButtonClick(){
        // スキャナー切断
        closeScanner(mScanMode);
        mListener.replaceFragmentWithStack(PackingOffBookFragment.newInstance(
                mInvoiceNo, mCartonCount, mReadCount), TAG);
    }

    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
            // スキャナー切断
            closeScanner(mScanMode);
            // 前画面に戻る
            mListener.popBackStack();
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

    private void inputMultiplePackingButton() {
        //　読込済みデータなし
        if(!isScanning()) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2005));
            return;
        }

        // スキャナー切断
        closeScanner(mScanMode);

        // 複数梱包画面へ遷移
        mListener.replaceFragmentWithStack(PackingMultiplePackingFragment.newInstance(
                mInvoiceNo, createLineNoArray()),  TAG);
    }

    private void inputFinalPackingButton() {
        //　読込済みデータなし
        if(!isScanning()) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2005));
            return;
        }

        //　修正モード、かつ、全選択状態ではない
        if(!isAllScanning()) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2024));
            return;
        }

        // 簿外ありの場合
        if(Application.scanOffBooklList.size() > 0) {
            onOffBookButtonClick();
            return;
        }

        // スキャナー切断
        closeScanner(mScanMode);

        // 複数梱包画面へ遷移
        mListener.replaceFragmentWithStack(PackingFinalPackingFragment.newInstance(
                mInvoiceNo, createLineNoArray(), !mCaseMarkNo.isEmpty()),  TAG);
    }

    // 指示内容確認ボタン
    private void instructionsButton() {
        // スキャナー切断
        closeScanner(mScanMode);

        // インパラ生成
        Bundle b = new Bundle();
        b.putString("screenPrefix", "K");

        // 次画面遷移
        CheckPackInstructionsFragment nextFragment = new CheckPackInstructionsFragment();
        nextFragment.setArguments(b);
        mListener.replaceFragmentWithStack(nextFragment, TAG);
    }

    /*
     * 画面リストタップ時処理
     *
     */
    @Override
    public void onItemClick(PackingScanViewHolder holder) {
        // タップ位置のデータ取得
        PackingScanRecyclerViewAdapter adapter = (PackingScanRecyclerViewAdapter) this.mPackingList.getAdapter();
        int position = holder.getAdapterPosition();
        PackingScanInfo updateData = this.mPackingScanInfoList.get(position);

        // 選択済みの場合
        if(updateData.getOnSelectFlag().equals(Constants.FLAG_TRUE)){
            // 選択済フラグ更新
            updateData.setOnSelectFlag(Constants.FLAG_FALSE);
            mPackingScanAdapter.notifyDataSetChanged();

            // 読取件数デクリメント
            this.mReadCount--;
            mBinding.packingScanViewReadCount.setText(String.valueOf(this.mReadCount));
        }
    }

    /* 単独梱包ボタン押下時のコールバック */
    @Override
    public void onClicked(int position) {
        // スキャナー切断
        closeScanner(mScanMode);

        mListener.replaceFragmentWithStack(PackingSinglePackingFragment.newInstance(
                mInvoiceNo, mPackingScanInfoList.get(position).getRenban(), mPackingScanInfoList.get(position).getLineNo()
                , mPackingScanInfoList.get(position).getPurchaseOrderNo(), String.format("%f", mPackingScanInfoList.get(position).getShipmentQuantity())),TAG);
    }

    /**
     * 簿外ボタン可視状態設定
     * @param buttonState   true：可視     false：不可視
     */
    private void setOffBookButtonState(boolean buttonState){
        if(buttonState){
            mBinding.packingScanButtonOffBook.setVisibility(View.VISIBLE);
        } else {
            mBinding.packingScanButtonOffBook.setVisibility(View.INVISIBLE);
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
                    if (tagInfo == null) { return; }

                    // 読取データ登録
                    registReadTagData(tagInfo);
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
        updatePackingFlg(tagInfo);
    }

    /**
     * 簿外データチェック
     * @param tagInfo
     * @return
     */
    private boolean isOffBookData(TagInfo tagInfo) {
        boolean bRet = false;
        // 画面リストに存在しない
        // 発注番号、枝番が不一致
        if(this.mPackingScanInfoList.stream().noneMatch(x ->
                x.getPurchaseOrderNo().equals(tagInfo.getPlaceOrderNo())
                        && x.getBranchNo() == tagInfo.getBranchNo())) {

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
     * 梱包済みフラグ更新（画面リストだけ。DB更新は最終梱包時）
     * @param tagInfo
     */
    private void updatePackingFlg(TagInfo tagInfo) {
        // 一致データ検索（簿外チェックしてるから必ずある）
        PackingScanInfo updateData = this.mPackingScanInfoList.stream().filter(
                list -> list.getPurchaseOrderNo().equals(tagInfo.getPlaceOrderNo())).filter(
                list -> list.getBranchNo() == tagInfo.getBranchNo()).findFirst()
                .orElse(new PackingScanInfo());

        // 梱包済は読み捨て
        if(updateData.getOnSelectFlag().equals(Constants.FLAG_TRUE)){
            return;
        }

        // 梱包済みフラグ更新
        updateData.setOnSelectFlag(Constants.FLAG_TRUE);
        mPackingScanAdapter.notifyDataSetChanged();

        // 件数更新（分子にピッキング済み件数セット）
        this.mReadCount++;
        mBinding.packingScanViewReadCount.setText(String.valueOf(this.mReadCount));
    }

    /**
     * 明細行構築
     */
    public void listCreate(){
        // スキャンリスト構築
        this.mPackingScanInfoList.clear();
        if(mCaseMarkNo.isEmpty()) {
            mPackingScanInfoList.addAll(new SyukkoShijiDetailDao().getPackingScanListByInvoice(
                    Application.dbHelper.getWritableDatabase(),mInvoiceNo));
        } else {
            mPackingScanInfoList.addAll(new SyukkoShijiDetailDao().getPackingScanListByCaseMark(
                    Application.dbHelper.getWritableDatabase(),mInvoiceNo, mCaseMarkNo));
            // 検証時有効にする
//            for(PackingScanInfo item : mPackingScanInfoList){
//                item.setOnSelectFlag(Constants.FLAG_TRUE);
//            }
        }
        mPackingScanAdapter.notifyDataSetChanged();

        // 読取件数設定
        mReadCount = getReadedCount();
        mBinding.packingScanViewReadCount.setText(String.valueOf(mReadCount));

        // カートン数設定
        mCartonCount = getCartonCount();
        mBinding.packingScanViewCartonCount.setText(String.valueOf(mCartonCount));
    }

    /**
     * 読取件数算出
     * @return
     */
    private int getReadedCount() {
        return (int)mPackingScanInfoList.stream().filter(item -> item.getOnHoldFlag().equals(Constants.FLAG_TRUE)).count();
    }

    private int getCartonCount() {
        // 複数梱包じゃない件数
        int notMultipulCount = (int)mPackingScanInfoList.stream()
                .filter(item -> item.getOverPack() == null).count();

        // 複数梱包、オーバーパック番号でリストをソート
        Comparator<PackingScanInfo> comp = Comparator.comparing(e -> e.getOverPack());
        ArrayList<String> list = new ArrayList<>();
        mPackingScanInfoList.stream()
                .filter(item -> item.getOverPack() != null).sorted(comp).forEach(v -> list.add(v.getOverPack()));
        // オーバーパック番号が重複しない件数
        int multipulCount = 0;
        String beforeNo = "0";
        for(String overPackNo : list) {
            if(!beforeNo.equals(overPackNo)){
                multipulCount++;
                beforeNo = overPackNo;
            }
        }
        return notMultipulCount + multipulCount;
    }

    /**
     * スキャン済みデータチェック
     */
    private boolean isScanning() {
        // 選択済データ検索
        PackingScanInfo scanedData = this.mPackingScanInfoList.stream().filter(
                list -> list.getOnSelectFlag().equals(Constants.FLAG_TRUE)).findFirst()
                .orElse(null);
        // スキャン済データあり
        if(scanedData != null){
            return true;
        }
        return false;
    }

    /**
     * 全スキャン済みチェック
     */
    private boolean isAllScanning() {
        // 新規モード
        if(mCaseMarkNo.isEmpty()) return true;

        // 選択済データ検索
        PackingScanInfo scanedData = this.mPackingScanInfoList.stream().filter(
                list -> list.getOnSelectFlag().equals(Constants.FLAG_FALSE)).findFirst()
                .orElse(null);
        // 未選択データあり
        if(scanedData != null){
            return false;
        }
        return true;
    }

    /**
     * 次画面パラメータ用・選択済みキー情報リスト生成
     * @return
     */
    private ArrayList<SyukkoShijiKeyInfo> createLineNoArray(){

        // 連番・行番号リスト生成
        List<SyukkoShijiKeyInfo> lineNoList = new ArrayList<>();
        for(PackingScanInfo item : mPackingScanInfoList){
            if(item.getOnSelectFlag().equals(Constants.FLAG_TRUE)){
                SyukkoShijiKeyInfo addItem = new SyukkoShijiKeyInfo(item.getRenban(), item.getLineNo());
                lineNoList.add(addItem);
            }
        }
        ArrayList<SyukkoShijiKeyInfo> lineNoArrayList = new ArrayList<>(lineNoList);
        return  lineNoArrayList;
    }
}
