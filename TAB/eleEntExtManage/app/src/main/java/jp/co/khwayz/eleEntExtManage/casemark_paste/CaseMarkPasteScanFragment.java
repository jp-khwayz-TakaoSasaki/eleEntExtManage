package jp.co.khwayz.eleEntExtManage.casemark_paste;

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
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.CaseMarkPasteTagInfo;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.CaseMarkDetailInfo;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.CategoryMasterDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentCaseMarkPasteScanBinding;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.ManagePwInputDialogFragment;
import jp.co.khwayz.eleEntExtManage.http.response.CaseMarkPasteTagGetResponse;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.task.get.CaseMarkPasteTagGetTask;
import jp.co.khwayz.eleEntExtManage.http.task.post.PostCsPasteUpdateTask;
import jp.co.khwayz.eleEntExtManage.util.Util;

public class CaseMarkPasteScanFragment extends BaseFragment
        implements RFIDDataDelegate, BarcodeDataDelegate, CaseMarkPasteScanRecyclerViewAdapter.OnItemClickListener
        , ManagePwInputDialogFragment.ManagePwInputDialogListener {
    // DataBinding
    FragmentCaseMarkPasteScanBinding mBinding;

    // ARGS
    private static final String ARGS_DISPDATA = "display_data";
    private static final String ARGS_CASEMARK_LIST = "casemark_list";
    private static CaseMarkDetailInfo mDisplayData;
    private static ArrayList<CaseMarkPasteReadInfo> mCaseMarkList;

    // Adapter
    CaseMarkPasteScanRecyclerViewAdapter caseMarkPasteScanAdapter;

    // List
    private RecyclerView mTagScanListView;
    private List<CaseMarkPasteScanInfo> mTagScanInfoList;

    // スキャナー制御
    private Constants.ScanMode mScanMode = Constants.ScanMode.QR;
    // 読取モード変更中フラグ
    private boolean mScanModeChangeFlg = false;
    // リスト件数
    private int mListCheckedCount;
    // 認証パスワード
    private String mCmManagePass;

    public static CaseMarkPasteScanFragment newInstance(CaseMarkDetailInfo displayData
            , ArrayList<CaseMarkPasteReadInfo> caseMarkList){
        CaseMarkPasteScanFragment fragment = new CaseMarkPasteScanFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_DISPDATA, displayData);
        args.putParcelableArrayList(ARGS_CASEMARK_LIST, caseMarkList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentCaseMarkPasteScanBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mDisplayData = args.getParcelable(ARGS_DISPDATA);
        this.mCaseMarkList = args.getParcelableArrayList(ARGS_CASEMARK_LIST);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
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
        mListener.setSubTitleText(getString(R.string.casemark_paste) + getString(R.string.casemark_scan));
        mListener.setScreenId(getString(R.string.screen_id_casemark_paste_scan));

        // Invoice番号
        mBinding.casemarkPasteScanViewInvoiceNo.setText(mCaseMarkList.get(0).getInvoiceNo());

        // リストオブジェクト
        this.mTagScanListView = mBinding.invoiceTagScanList;
        if(this.mTagScanInfoList == null) {
            this.mTagScanInfoList = new ArrayList<>();
        }

        // リストアダプタ生成
        this.caseMarkPasteScanAdapter = new CaseMarkPasteScanRecyclerViewAdapter(this.mTagScanInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mTagScanListView.setHasFixedSize(true);
        this.mTagScanListView.setLayoutManager(llm);
        this.mTagScanListView.setAdapter(caseMarkPasteScanAdapter);
        caseMarkPasteScanAdapter.setOnItemClickListener(this);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        // 簿外あり タップ時
        mBinding.casemarkPasteScanButtonOffBook.setOnClickListener(new View.OnClickListener() {
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

        // 完了ボタン
        ButtonInfo finish = new ButtonInfo(getString(R.string.button_label_finish), v -> finishButton());

        // QRボタン
        ButtonInfo qr = new ButtonInfo(getString(R.string.button_label_rfid), v -> qrButton());

        mListener.setFooterButton(null, null, finish, null, qr);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        // 認証パスワード取得
        ArrayList<CategoryInfo> infoList = mUtilListener.getCategoryList(Constants.KBN_EMANAGEPASSWD);
        if (infoList.size() == 0) {
            this.mCmManagePass = Constants.DEFAULT_CM_MANEGEPASSWORD;
        } else {
            this.mCmManagePass = infoList.get(0).getElementName();
        }

        // 画面表示データ生成
        if(this.mTagScanInfoList.size() <=0) {
            // リスト０件（初期描画時）
            caseMarkTagReceive();
        } else {
            // リストあり（簿外から戻った場合）
            mBinding.casemarkPasteScanViewReadCount.setText(String.valueOf(this.mListCheckedCount));
            this.caseMarkPasteScanAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(CaseMarkPasteScanViewHolder holder) {
        // タップ位置のデータ取得
        CaseMarkPasteScanRecyclerViewAdapter adapter = (CaseMarkPasteScanRecyclerViewAdapter) this.mTagScanListView.getAdapter();
        int position = holder.getAdapterPosition();
        CaseMarkPasteScanInfo updateData = this.mTagScanInfoList.get(position);

        // 選択済みの場合
        if(updateData.getOnSelectFlag().equals(Constants.FLAG_TRUE)){
            // 選択済フラグ更新
            updateData.setOnSelectFlag(Constants.FLAG_FALSE);
            caseMarkPasteScanAdapter.notifyDataSetChanged();
        }
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
     * 簿外ボタン可視状態設定
     * @param buttonState   true：可視     false：不可視
     */
    private void setOffBookButtonState(boolean buttonState){
        if(buttonState){
            mBinding.casemarkPasteScanButtonOffBook.setVisibility(View.VISIBLE);
        } else {
            mBinding.casemarkPasteScanButtonOffBook.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 簿外ボタンタップ
     */
    protected void onOffBookButtonClick(){
        // スキャナー切断
        closeScanner(mScanMode);

        // 簿外画面遷移
        mListener.replaceFragmentWithStack(CaseMarkPasteOffBookFragment.newInstance(
                mBinding.casemarkPasteScanViewInvoiceNo.getText().toString()), TAG);
    }

    /**
     * ケースマーク貼付け荷札情報受信
     */
    private void caseMarkTagReceive(){
        try {
            // ケースマーク貼付け荷札情報受信パラメータセット
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("invoiceNo", mCaseMarkList.get(0).getInvoiceNo());
            jsonObject.addProperty("hyoukiCsNumber", mDisplayData.getHyokiCsNumber());
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_CASEMARK_NIFUDA_INFO_GET;
            new CaseMarkPasteTagGetTask(caseMarkTagGetCallback, url, jsonObject.toString()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
        }
    }

    /**
     * ケースマーク貼付け荷札情報受信Callback
     */
    CaseMarkPasteTagGetTask.Callback<CaseMarkPasteTagGetResponse> caseMarkTagGetCallback = new CaseMarkPasteTagGetTask.Callback<CaseMarkPasteTagGetResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialogを表示する
            if (showProgress) {
                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0030));
            }
        }
        @Override
        public void onTaskFinished(CaseMarkPasteTagGetResponse response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();

            // エラーレスポンスの場合
            if (!Constants.API_RESPONSE_STATUS_CODE_OK.equals(response.getStatus())) {
                Application.log.e(TAG, "SyukkoInvoiceSearchTask ERR status=" + response.getStatus() + " ,errorCode=" + response.getErrorCode());
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9001));
                return;
            }

            // 検索結果ヒットなしの場合
            if (response.getData().getList().size() <= 0) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2006));
                mListener.popBackStack();
                return;
            }

            // 画面リスト更新
            searchListUpdate(response.getData().getList());
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
        if(this.mTagScanInfoList.stream().noneMatch(x ->
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
     * ピッキング済みフラグ更新（ローカル）
     * @param tagInfo
     */
    private void updatePickingFlg(TagInfo tagInfo) {
        // 一致データ検索（簿外チェックしてるから必ずある）
        CaseMarkPasteScanInfo updateData = this.mTagScanInfoList.stream().filter(
                list -> list.getPurchaseOrderNo().equals(tagInfo.getPlaceOrderNo())).filter(
                list -> list.getBranchNo() == tagInfo.getBranchNo()).findFirst()
                .orElse(new CaseMarkPasteScanInfo());

        // ピッキング済は読捨て
        if(updateData.getOnSelectFlag().equals(Constants.FLAG_TRUE)){
            return;
        }

        // ピッキング済フラグ更新
        updateData.setOnSelectFlag(Constants.FLAG_TRUE);
        caseMarkPasteScanAdapter.notifyDataSetChanged();

        // 件数更新
        this.mListCheckedCount++;
        mBinding.casemarkPasteScanViewReadCount.setText(String.valueOf(this.mListCheckedCount));
    }

    /**
     * ケースマーク貼付け完了
     */
    private void finishButton() {
        // 簿外ありの場合
        if(Application.scanOffBooklList.size() > 0) {
            onOffBookButtonClick();
            return;
        }

        // リストデータなし
        if(getSelectedCount() <= 0) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2005));
            return;
        }

        // 読取件数不一致
        if(mTagScanInfoList.size() != mListCheckedCount) {
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                // 管理者パスワード入力ダイアログ表示
                final ManagePwInputDialogFragment dialogFlagment = new ManagePwInputDialogFragment(getContext(), mCmManagePass);
                dialogFlagment.show(getActivity().getSupportFragmentManager(), "ManagePwInputDialogFragment");
                dialog.dismiss();
            };
            mUtilListener.showConfirmDialog(R.string.info_message_I0002, listener);

        } else {

            // 貼付け完了登録
            caseMarkPasteRegist();
        }
    }

    /**
     * 管理者パスワード認証成功
     */
    @Override
    public void onPwAuthSuccess() {
        // 貼付け完了登録
        caseMarkPasteRegist();
    }

    /**
     * 貼付け完了登録
     */
    private void caseMarkPasteRegist() {
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            try {
                // 登録用チェック済みデータ生成
                List<CaseMarkPasteScanInfo> updateList = this.mTagScanInfoList.stream()
                        .filter(x -> x.getOnSelectFlag().equals(Constants.FLAG_TRUE))
                        .collect(Collectors.toList());

                // ケースマークデータ貼付け更新
                String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_CASEMARK_PASTE_UPD;
                new PostCsPasteUpdateTask(caseMarkPasteRegistCallback, url
                        , mBinding.casemarkPasteScanViewInvoiceNo.getText().toString(), updateList).execute();
            } catch (Exception e) {
                e.printStackTrace();
                Application.log.e(TAG, e);
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
            }        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0013, listener);
    }


    /**
     * ピッキングデータ登録Callback
     */
    PostCsPasteUpdateTask.Callback<SimpleResponse> caseMarkPasteRegistCallback = new PostCsPasteUpdateTask.Callback<SimpleResponse>() {
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
            if (Constants.API_RESPONSE_STATUS_SYS_ERR.equals(response.getStatus())) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9001));
                Application.log.e(TAG, "status=" + response.getStatus() + " ,errorCode=" + response.getErrorCode());
                return;
            }

            // スキャナー切断
            closeScanner(mScanMode);

            // 業務エラーの場合
            if(Constants.API_RESPONSE_STATUS_OPE_ERR.equals(response.getStatus())){
                // エラーメッセージ編集
                String[] errCodeArray = response.getErrorCode().split(",");
                String errCodeRowString="";
                for(String errCode : errCodeArray){
                    errCodeRowString = errCodeRowString + "  " + errCode + "\n";
                }
                String errMessage = mUtilListener.getDataBaseMessage(R.string.err_message_E0010);
                errMessage = String.format(errMessage, errCodeRowString);

                // ダイアログ表示
                DialogInterface.OnClickListener listener = (dialog, which) -> {
                    // 読取画面に戻る
                    mListener.popBackStack(CaseMarkPasteReadFragment.class.getSimpleName());
                };
                mUtilListener.showInformationDialog(errMessage, listener);
                return;
            }

            // 正常終了
            mUtilListener.showSnackBarOnUiThread(mUtilListener.getDataBaseMessage(R.string.info_message_I0007));
            // 読取画面に戻る
            mListener.popBackStack(CaseMarkPasteReadFragment.class.getSimpleName());
        }

        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // エラーメッセージを表示
            mUtilListener.showAlertDialog(messageId);
        }
    };

    /**
     * 明細更新
     */
    private void searchListUpdate(ArrayList<CaseMarkPasteTagInfo> searchList) {
        // 明細更新
        this.mTagScanInfoList.clear();
        int no = 0;
        for (CaseMarkPasteTagInfo item : searchList) {
            no++;
            CaseMarkPasteScanInfo new_item = new CaseMarkPasteScanInfo();
            new_item.setNo(no);
            new_item.setCaseMarkNo(item.getCsNumber());
            new_item.setPurchaseOrderNo(item.getHachuNo());
            new_item.setBranchNo(item.getHachuEda());
            new_item.setOrderNo(item.getJyuchuNo());
            new_item.setItemCode(item.getItemCode());
            new_item.setItemName(item.getItemName());
            new_item.setStock(item.getZaikoSuryo());
            new_item.setShipmentQuantity(item.getSyukkaSuryo());
            new_item.setShipmentUnit(item.getSyukkaTani());
            new_item.setPackingForm(new CategoryMasterDao().getCategory(
                    Application.dbHelper.getReadableDatabase(),"ENISUGATA",item.getKonpoKeitai()).getElementName());
            new_item.setOnSelectFlag(Constants.FLAG_FALSE);
            this.mTagScanInfoList.add(new_item);
        }
        caseMarkPasteScanAdapter.notifyDataSetChanged();

        // 結果数更新
        mBinding.casemarkPasteScanViewReadCount.setText(String.valueOf(caseMarkPasteScanAdapter.getItemCount()));
    }

    private int getSelectedCount(){
        int count = 0;
        for(CaseMarkPasteScanInfo info : mTagScanInfoList) {
            if(info.getOnSelectFlag().equals(Constants.FLAG_TRUE)) count++;
        }
        return count;
    }
}
