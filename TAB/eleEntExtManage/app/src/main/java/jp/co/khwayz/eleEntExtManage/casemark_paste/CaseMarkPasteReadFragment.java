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

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.casemark_paste.task.CaseMarkDetailGetTask;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.CaseMarkDetailInfo;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentCaseMarkPasteReadBinding;
import jp.co.khwayz.eleEntExtManage.http.response.CaseMarkDetailGetResponse;
import jp.co.khwayz.eleEntExtManage.util.Util;

/**
 * ケースマーク貼付：読取画面
 */
public class CaseMarkPasteReadFragment extends BaseFragment implements BarcodeDataDelegate
            , CaseMarkPasteReadRecyclerViewAdapter.OnItemLongClickListener {
    // DataBinding
    FragmentCaseMarkPasteReadBinding mBinding;

    // Adapter
    private CaseMarkPasteReadRecyclerViewAdapter mInvoiceInfoListAdapter;

    // List
    private RecyclerView mInvoiceListRecycleView;
    private List<CaseMarkPasteReadInfo> mInvoiceInfoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentCaseMarkPasteReadBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
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
        mListener.setSubTitleText(getString(R.string.casemark_paste) + getString(R.string.casemark_read));
        mListener.setScreenId(getString(R.string.screen_id_casemark_paste_read));

        // リストオブジェクト
        this.mInvoiceListRecycleView = mBinding.invoiceList;
        if(this.mInvoiceInfoList == null){
            this.mInvoiceInfoList = new ArrayList<>();
        }

        // リストView生成
        this.mInvoiceInfoListAdapter = new CaseMarkPasteReadRecyclerViewAdapter(this.mInvoiceInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mInvoiceListRecycleView.setHasFixedSize(true);
        this.mInvoiceListRecycleView.setLayoutManager(llm);
        this.mInvoiceListRecycleView.setAdapter(mInvoiceInfoListAdapter);
        // ロングタップリスナーセット
        this.mInvoiceInfoListAdapter.setOnItemLongClickListener(this);
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

        // 次へボタン
        View.OnClickListener nextclickListener = v -> nextButton();
        ButtonInfo nextButtonInfo = new ButtonInfo(getString(R.string.next), nextclickListener);

        // フッターボタン登録
        mListener.setFooterButton(null, null, nextButtonInfo, null, null);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        // スキャナー接続
        openScanner();
    }

    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        // スキャナー切断
        closeScanner(Constants.ScanMode.QR);
        // 呼び出し元の画面に戻る
        mListener.popBackStack();
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
        // バーコードスキャン
        startBarcodeScan(this);
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
                    closeScanner(Constants.ScanMode.QR);
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

                    // 読取情報生成
                    CaseMarkPasteReadInfo readInfo = Util.convertQrCodeToCaseMarkPasteReadInfo(qrCode);

                    // nullの場合は読み飛ばし
                    if (readInfo == null) {
                        return;
                    }

                    // 読取データ行追加
                    addReadList(readInfo);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showSnackBarOnUiThread(TAG + " onBarcodeDataReceived \n" + e.getMessage());
        }
    }

    /**
     * 読取リスト追加
      */
    private void addReadList(CaseMarkPasteReadInfo readInfo){
        // 同一データは読み飛ばし
        CaseMarkPasteReadInfo sameData = mInvoiceInfoList.stream().filter(list -> list.getInvoiceNo().equals(readInfo.getInvoiceNo()))
                .filter(list -> list.getCaseMarkNo() == readInfo.getCaseMarkNo()).findFirst().orElse(null);
        if(sameData != null) return;

        // リスト追加
        mInvoiceInfoList.add(readInfo);
        mInvoiceInfoListAdapter.notifyDataSetChanged();
    }

    /**
     * 行ロングタップ時処理
     */
    @Override
    public boolean onItemLongClick(CaseMarkPasteReadViewHolder holder){
        CaseMarkPasteReadRecyclerViewAdapter adapter = (CaseMarkPasteReadRecyclerViewAdapter)this.mInvoiceListRecycleView.getAdapter();
        int position = holder.getAdapterPosition();
        // 選択行色変更
        adapter.setSelectedPosition(position);
        adapter.notifyDataSetChanged();
        // 「はい」用リスナー生成
        DialogInterface.OnClickListener positiveListener = (dialog, which) -> {
            // 選択行削除
            this.mInvoiceInfoList.remove(position);
            adapter.setSelectedPosition(-1);
            adapter.notifyDataSetChanged();
        };
        // 「いいえ」用リスナー生成
        DialogInterface.OnClickListener negativeListener = (dialog, which) -> {
            // 選択解除
            adapter.setSelectedPosition(-1);
            adapter.notifyDataSetChanged();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0012, positiveListener, negativeListener);
        return true;
    }

    /**
     * 「次へ」クリック時処理
     */
    private void nextButton() {
        // リストデータ有無チェック
        if(mInvoiceInfoList.size() <= 0) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2005));
            return;
        }

        // Invoice番号不一致チェック
        if(!invoiceSameChk()) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2018));
            return;
        }

        // 詳細情報受信＆チェック
        getCaseMarkDetail();
    }

    /**
     * Invoice番号同一チェック
     * @return  true：同一である      false：同一ではない
     */
    private boolean invoiceSameChk(){
        int counter = 0;
        String chckerStr = mInvoiceInfoList.get(0).getInvoiceNo();
        for(CaseMarkPasteReadInfo item : mInvoiceInfoList) {
            if(item.getInvoiceNo().equals(chckerStr)) counter++;
        }
        return(mInvoiceInfoList.size() == counter);
    }

    /**
     * ケースマーク詳細情報の受信、およびチェック
     */
    private void getCaseMarkDetail(){
        try {
            /* パラメータを作成 */
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_CASEMARK_DETAIL_GET;
            new CaseMarkDetailGetTask(caseMarkDetailGetCallback, url, this.mInvoiceInfoList).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
        }
    }

    /**
     * ケースマーク詳細受信Callback
     */
    CaseMarkDetailGetTask.Callback<CaseMarkDetailGetResponse> caseMarkDetailGetCallback = new CaseMarkDetailGetTask.Callback<CaseMarkDetailGetResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialogを表示する
            if (showProgress) {
                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0030));
            }
        }
        @Override
        public void onTaskFinished(CaseMarkDetailGetResponse response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();

            // エラーレスポンスの場合
            if (!Constants.API_RESPONSE_STATUS_CODE_OK.equals(response.getStatus())) {
                // 業務エラー
                if (Constants.API_RESPONSE_STATUS_OPE_ERR.equals(response.getStatus())) {
                    switch (Integer.parseInt(response.getErrorCode())) {
                        case 1101:
                            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2019));
                            break;
                        case 1102:
                            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2020));
                            break;
                        case 1103:
                            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2021));
                            break;
                        default:
                            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9001));
                    }
                } else {
                    mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9001));
                }
                Application.log.e(TAG, "status=" + response.getStatus() + " ,errorCode=" + response.getErrorCode());
                return;
            }

            // レスポンス取り出し
            CaseMarkDetailInfo detailInfo = new CaseMarkDetailInfo(
                    response.getData().getCASEMARK1()
                    ,response.getData().getCASEMARK2()
                    ,response.getData().getCASEMARK3()
                    ,response.getData().getCASEMARK4()
                    ,response.getData().getCASEMARK5()
                    ,response.getData().getCASEMARK6()
                    ,response.getData().getCASEMARK7()
                    ,response.getData().getCASEMARK8()
                    ,response.getData().getCASEMARK9()
                    ,response.getData().getCASEMARK10()
                    ,response.getData().getCASEMARK11()
                    ,response.getData().getCASEMARK12()
                    ,response.getData().getKonpoSu()
                    ,response.getData().getHyokiCsNumber()
                    ,response.getData().getOuterLength()
                    ,response.getData().getOuterWidth()
                    ,response.getData().getOuterHeight()
                    ,response.getData().getNetWeight()
                    ,response.getData().getGrossWeight()
                    ,response.getData().getBiko()
            );

            // スキャナー切断
            closeScanner(Constants.ScanMode.QR);
            // 次画面遷移
            mListener.replaceFragmentWithStack(CaseMarkPasteDetailFragmet.newInstance(
                    detailInfo, createCaseMarkArrayList()),  TAG);
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
     * 次画面パラメータ用・キー情報リスト生成
     * @return
     */
    private ArrayList<CaseMarkPasteReadInfo> createCaseMarkArrayList(){
        ArrayList<CaseMarkPasteReadInfo> lineNoArrayList = new ArrayList<>(this.mInvoiceInfoList);
        return  lineNoArrayList;
    }
}
