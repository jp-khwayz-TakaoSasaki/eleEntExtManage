package jp.co.khwayz.eleEntExtManage.issueregist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.densowave.scannersdk.Barcode.BarcodeData;
import com.densowave.scannersdk.Barcode.BarcodeDataReceivedEvent;
import com.densowave.scannersdk.Common.CommScanner;
import com.densowave.scannersdk.Const.CommConst;
import com.densowave.scannersdk.Listener.BarcodeDataDelegate;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentIssueRegistrationCheckBinding;
import jp.co.khwayz.eleEntExtManage.http.request.IssueRegCheckRequest;
import jp.co.khwayz.eleEntExtManage.http.response.IssueRegInvoiceSearchResponse;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.task.get.GetIssueRegInvoiceSearchTask;
import jp.co.khwayz.eleEntExtManage.http.task.post.PostIssueRegCheckTask;
import jp.co.khwayz.eleEntExtManage.util.Util;

/**
 * 出庫登録：出庫確認画面
 */
public class IssueRegCheckFragment extends BaseFragment implements BarcodeDataDelegate {
    public static final String ARG_INVOICE_NO = "ARG_INVOICE_NO";
    public static final String ARG_DESTINATION = "ARG_DESTINATION";
    public static final String ARG_SHIP_DATE = "ARG_SHIP_DATE";

    // DataBinding
    FragmentIssueRegistrationCheckBinding mBinding;
    IssueRegCheckRecyclerViewAdapter mInvoiceAdapter;
    private ArrayList<IssueRegInvoiceSearchInfo> mIssueRegInvoiceList;
    private boolean mIsStopScanner = false;
    private String mInvoiceNo;
    private String mDestination;
    private String mShipDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // InvoiceNoを取得
        mInvoiceNo = null;
        if (getArguments() != null) {
            mInvoiceNo = getArguments().getString(ARG_INVOICE_NO, null);
            mDestination = getArguments().getString(ARG_DESTINATION, null);
            mShipDate = getArguments().getString(ARG_SHIP_DATE, null);
        }
        // パラメータ全部無しはエラー
        if (TextUtils.isEmpty(mInvoiceNo)
                && TextUtils.isEmpty(mDestination)
                && TextUtils.isEmpty(mShipDate)) {
            throw new RuntimeException(TAG + "：nothing arguments.");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentIssueRegistrationCheckBinding.inflate(inflater, container, false);
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
        mListener.setSubTitleText(getString(R.string.issue_registration) + getString(R.string.issue_registration_confirm));
        mListener.setScreenId(getString(R.string.screen_id_registration_check));

        this.mIssueRegInvoiceList = new ArrayList<>();
        mInvoiceAdapter = new IssueRegCheckRecyclerViewAdapter(this.mIssueRegInvoiceList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mBinding.invoiceList.setHasFixedSize(true);
        mBinding.invoiceList.setLayoutManager(llm);
        mBinding.invoiceList.setAdapter(mInvoiceAdapter);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();

        /////////////////////////
        // フッターボタン
        /////////////////////////
        // 戻るボタン設定
        View.OnClickListener backClickListener = v -> onBackButtonClick(mBinding.getRoot());
        mListener.setBackButton(backClickListener);

        // 確定ボタン
        View.OnClickListener confirmClickListener = v -> confirmButton();
        ButtonInfo tagScanButtonInfo = new ButtonInfo(getString(R.string.confirm), confirmClickListener);
        mListener.setFooterButton(null, null, tagScanButtonInfo, null, null);

        //BarcodeScan開始
        startBarcodeScan(this);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        // JSON作成
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("invoiceNo", mInvoiceNo);
        jsonObject.addProperty("shimukeChi", mDestination);
        jsonObject.addProperty("syukkaDate", mShipDate);
        // 検索データ取得
        new GetIssueRegInvoiceSearchTask(getCallback, jsonObject.toString()).execute();
    }

    @Override
    public void CommStatusChanged(CommConst.ScannerStatus status) {
        mIsStopScanner = false;
        Application.log.d(TAG, "CommStatusChanged ： " + status.name());
        switch (status) {
            case CLAIMED:
                startBarcodeScan(this);
                break;
            case CLOSED:
                stopBarcodeScan();
                break;
            default:
                break;
        }
    }
    @Override
    public boolean hasScanner() {
        return true;
    }
    /* QRコード読取だけなので使用しない */
    @Override
    protected void openScanner() { }

    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        // BarcodeScan停止
        stopBarcodeScan();
        // 呼び出し元の画面に戻る
        mListener.popBackStack();
    }

    /**
     * 確定ボタン押下時処理
     */
    private void confirmButton() {
        // リクエストパラメータ
        IssueRegCheckRequest requestParam = new IssueRegCheckRequest();

        // エラーチェック
        for (IssueRegInvoiceSearchInfo info : mIssueRegInvoiceList) {
            // スキャンされていない場合は更新対象外
            if (info.getScannedCaseMarkCount() == 0) { continue; }
            // ケースマーク数とスキャン数が異なる場合はエラー
            long caseMarkCount = info.getCaseMarkNoList().size();
            long scannedCount = info.getScannedCaseMarkCount();
            if (caseMarkCount != scannedCount) {
                mUtilListener.showAlertDialog(R.string.err_message_E2004);
                return;
            }
            // リクエストパラメータにInvoiceNoを追加
            IssueRegCheckRequest.RequestDetail detail = new IssueRegCheckRequest.RequestDetail();
            detail.setInvoiceNo(info.getInvoiceNo());
            requestParam.getList().add(detail);
        }

        // 1件もケースマークが読取られていない場合
        if (requestParam.getList().size() == 0) {
            // エラーメッセージを表示
            mUtilListener.showAlertDialog(R.string.err_message_E2026);
            return;
        }

        // 確認メッセージ表示
        mUtilListener.showInformationDialog(R.string.info_message_I0005, ((dialog, which) -> {
            // 更新API実行
            new PostIssueRegCheckTask(postCallback, requestParam).execute();
        }));

    }

    // region [ http get callback ]
    /**
     * 出庫Invoice検索のCallback
     */
    GetIssueRegInvoiceSearchTask.Callback<IssueRegInvoiceSearchResponse> getCallback = new GetIssueRegInvoiceSearchTask.Callback<IssueRegInvoiceSearchResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialogを表示する
            if (showProgress) {
//                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0030));
                mUtilListener.showProgressDialog(R.string.info_message_I0030);
            }
        }

        @Override
        public void onTaskFinished(IssueRegInvoiceSearchResponse response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // 一覧表示
            mIssueRegInvoiceList.clear();
            mIssueRegInvoiceList.addAll(response.getData().getList());
            mInvoiceAdapter.notifyDataSetChanged();
            // 総ケースマーク数表示
            long totalCount = mIssueRegInvoiceList.stream().mapToLong(f -> f.getCaseMarkNoList().size()).sum();
            mBinding.caseMarkCount.setText(String.format(Locale.JAPAN, "%,d", totalCount));
            // スキャン数表示
            long scannedCount = mIssueRegInvoiceList.stream().mapToLong(IssueRegInvoiceSearchInfo::getScannedCaseMarkCount).sum();
            mBinding.scannedCaseMarkCount.setText(String.format(Locale.JAPAN, "%,d", scannedCount));
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
     * 出庫情報更新のCallback
     */
    PostIssueRegCheckTask.Callback<SimpleResponse> postCallback = new PostIssueRegCheckTask.Callback<SimpleResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialogを表示する
            if (showProgress) {
//                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0031));
                mUtilListener.showProgressDialog(R.string.info_message_I0031);
            }
        }

        @Override
        public void onTaskFinished(SimpleResponse response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // BarcodeScan停止
            stopBarcodeScan();
            // 完了メッセージ表示
            mUtilListener.showSnackBarOnUiThread(R.string.info_message_I0007);
            // 呼び出し元の画面に戻る
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
    // endregion

    // region [ http post callback ]
    // endregion

    // region [ Scanner listener ]
    @Override
    public void onBarcodeDataReceived(CommScanner commScanner, BarcodeDataReceivedEvent barcodeDataReceivedEvent) {
        try {

            // 読取停止フラグOnは何もしない
            if (mIsStopScanner) return;

            //barcodeデータ取得
            if (barcodeDataReceivedEvent == null) { return; }
            final List<BarcodeData> barcodeDataList = barcodeDataReceivedEvent.getBarcodeData();
            if (barcodeDataList != null && barcodeDataList.size() > 0) {
                mHandler.post(() -> {
                    // QRコード生成
                    byte[] bytes = barcodeDataList.get(0).getData();
                    final String qrCode = Util.convertBytesToASCII(bytes);
                    // 28桁以外はは表示しない
                    if (28 != qrCode.length()) { return; }

                    // 企業コード(9桁)
                    String corporate = qrCode.substring(0, 9);
                    // 企業コードが違う場合は読み飛ばし
                    if (!Application.corporateCode.equals(corporate)) { return; }

                    // Invoice番号(15桁)
                    String invoiceNo = qrCode.substring(9, 24).trim();

                    // ケースマーク(4桁)
                    String caseMark = qrCode.substring(24, 28);
                    // 数値チェック
                    int caseMarkNo;
                    if (Util.isNumber(caseMark)) {
                        caseMarkNo = Integer.parseInt(caseMark);
                    } else {
                        // 数値以外は読み飛ばし
                        return;
                    }
                    // 対象InvoiceNoを検索
                    Optional<IssueRegInvoiceSearchInfo> findItem = mIssueRegInvoiceList.stream()
                            .filter(x -> x.getInvoiceNo().equals(invoiceNo))
                            .findFirst();
                    // 対象無しはメッセージ表示して読み飛ばし
                    if (!findItem.isPresent()) {
                        // 読取停止フラグOn
                        mIsStopScanner = true;
                        mUtilListener.showInformationDialog(R.string.err_message_E2017, (dialog, which) -> mIsStopScanner = false, invoiceNo + caseMark);
                        return;
                    }
                    // InvoiceNoに紐づくケースマークを検索
                    IssueRegInvoiceSearchInfo invoiceSearchInfo;
                    invoiceSearchInfo = findItem.get();
                    List<IssueRegCaseMarkNo> result =
                            invoiceSearchInfo.getCaseMarkNoList().stream()
                                    .filter(x -> x.getCaseMarkNo() == caseMarkNo)
                                    .collect(Collectors.toList());
                    // 対象無しはメッセージ表示して読み飛ばし
                    if (result.size() == 0) {
                        // 読取停止フラグOn
                        mIsStopScanner = true;
                        mUtilListener.showInformationDialog(R.string.err_message_E2017, (dialog, which) -> mIsStopScanner = false, invoiceNo + caseMark);
                        return;
                    }

                    // 存在する場合は読取済みに更新
                    for (IssueRegCaseMarkNo item : result) {
                        item.setRead(true);
                    }
                    // RecycleViewer通知
                    mInvoiceAdapter.notifyDataSetChanged();
                    // 画面のスキャン数更新
                    long scannedCount = mIssueRegInvoiceList.stream().mapToLong(IssueRegInvoiceSearchInfo::getScannedCaseMarkCount).sum();
                    mBinding.scannedCaseMarkCount.setText(String.format(Locale.JAPAN, "%,d", scannedCount));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showSnackBarOnUiThread(TAG + " BarcodeDataReceived \n" + e.getMessage());
        }
    }
    // endregion
}
