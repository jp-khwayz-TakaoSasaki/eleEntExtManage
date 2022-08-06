package jp.co.khwayz.eleEntExtManage.casemark_print;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.densowave.scannersdk.Const.CommConst;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.Calender;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.CategorySpinnerAdapter;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.casemark_print.task.CaseMarkPrintDataCreateTask;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.CategoryMasterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoOuterDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentCaseMarkPrintBinding;
import jp.co.khwayz.eleEntExtManage.http.response.CaseMarkPrintInvoiceSearchResponse;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.task.get.CaseMarkPrintInvoiceSearchTask;
import jp.co.khwayz.eleEntExtManage.http.task.post.PostCaseMarkPrintedRegistTask;
import jp.co.khwayz.eleEntExtManage.pdf_print.CustomDocumentPrintAdapter;
import jp.co.khwayz.eleEntExtManage.util.Util;

public class CaseMarkPrintFragment extends BaseFragment implements CaseMarkPrintRecyclerViewAdapter.OnItemClickListener {

    // DataBinding
    private FragmentCaseMarkPrintBinding mBinding;

    // Adapter
    private CaseMarkPrintRecyclerViewAdapter invoiceAdapter;

    // List
    private RecyclerView invoiceList;
    private List<CaseMarkPrintStatusInfo> invoiceInfoList;

    // 印刷用PDFフルパス
    private final String outputPDFPath = Environment.getExternalStorageDirectory().getPath()
            + "/"
            + Environment.DIRECTORY_DOWNLOADS
            + "/caseMark.pdf";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentCaseMarkPrintBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
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
        mListener.setReaderConnectButtonVisibility(View.INVISIBLE);
        mListener.setBatteryStateImageVisibility(View.VISIBLE);

        // タイトル
        mListener.setSubTitleText(getString(R.string.casemark) + getString(R.string.casemark_print));
        mListener.setScreenId(getString(R.string.screen_id_casemark_print));

        // 項目クリア
        mBinding.etCasemarkPrintInvoiceNo.setText("");
        mBinding.tvCasemarkPrintShipDate.setText("");

        // 仕向地スピナー
        CategoryMasterDao categoryMasterDao = new CategoryMasterDao();
        List<CategoryInfo> destinationDataSet = categoryMasterDao.getDestinationSpinnerArray(Application.dbHelper.getReadableDatabase());
        // 画面オブジェクト
        CategorySpinnerAdapter destinationSpinnerAdapter = new CategorySpinnerAdapter(getContext(), destinationDataSet);
        mBinding.spCasemarkPrintDestination.setAdapter(destinationSpinnerAdapter);

        // 印刷状態スピナー
        ArrayList<String> printStatusList = new ArrayList<>();
        for (Constants.CaseMarkPintStatusChoices choice : Constants.CaseMarkPintStatusChoices.values()) {
            printStatusList.add(choice.getCaseMarkPrintStatusChoices());
        }
        ArrayAdapter<String> coolAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, printStatusList);
        coolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // SpinnerにAdapterをセット
        mBinding.spPrintStatus.setAdapter(coolAdapter);
        mBinding.spPrintStatus.setSelection(Constants.CaseMarkPintStatusChoices.NotPrinted.ordinal());

        // リストオブジェクト
        this.invoiceList = mBinding.invoiceList;
        if(this.invoiceInfoList == null) {
            this.invoiceInfoList = new ArrayList<>();
        }

        // リストアダプタ生成
        this.invoiceAdapter = new CaseMarkPrintRecyclerViewAdapter(this.invoiceInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.invoiceList.setHasFixedSize(true);
        this.invoiceList.setLayoutManager(llm);
        this.invoiceList.setAdapter(invoiceAdapter);
        invoiceAdapter.setOnItemClickListener(this);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        /*************************************/
        // フッターボタン
        /*************************************/
        // 戻るボタン設定
        View.OnClickListener backClickListener = v -> {
            onBackButtonClick(mBinding.getRoot());
        };
        mListener.setBackButton(backClickListener);

        // 一括印刷ボタン
        View.OnClickListener printAllClickListener = v -> clickAllPrintButton();
        ButtonInfo printAllButtonInfo = new ButtonInfo(getString(R.string.print_all), printAllClickListener);

        // 印刷ボタン
        View.OnClickListener printClickListener = v -> clickPrintButton();
        ButtonInfo printButtonInfo = new ButtonInfo(getString(R.string.print), printClickListener);

        // フッターボタン登録
        mListener.setFooterButton(null, null, null, printAllButtonInfo,printButtonInfo);

        /*************************************/
        // イベントリスナー登録
        /*************************************/
        // 出荷日
        mBinding.btCasemarkPrintShipDate.setOnClickListener(v -> Calender.show(getActivity(), mBinding.tvCasemarkPrintShipDate));
        // 検索ボタン
        mBinding.btInvoiceSearch.setOnClickListener(v -> onSearchButtonClick());
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
    }

    @Override
    public void onItemClick(CaseMarkPrintViewHolder holder) {
        invoiceAdapter.setSelectedPosition(holder.getAdapterPosition());
        invoiceAdapter.notifyDataSetChanged();
    }

    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        // 呼び出し元の画面に戻る
        mListener.popBackStack();
    }

    @Override
    public void CommStatusChanged(CommConst.ScannerStatus status) { }
    @Override
    public boolean hasScanner() {
        return false;
    }
    @Override
    protected void openScanner() { }
    // endregion

    public void onSearchButtonClick() {

        // 検索条件入力チェック
        String invoiceNo = mBinding.etCasemarkPrintInvoiceNo.getText().toString();
        CategoryInfo destinationSp = (CategoryInfo)mBinding.spCasemarkPrintDestination.getSelectedItem();
        String shimukeChi = destinationSp.getElementName();
        String shipDate = mBinding.tvCasemarkPrintShipDate.getText().toString();
        String printStatus = mBinding.spPrintStatus.getSelectedItem().toString().equals(Constants.CaseMarkPintStatusChoices.NotPrinted.getCaseMarkPrintStatusChoices()) ?
                String.valueOf(Constants.CaseMarkPintStatusChoices.NotPrinted.ordinal()) : String.valueOf(Constants.CaseMarkPintStatusChoices.Printed.ordinal());

        // 検索条件が全て未入力
        if( invoiceNo.isEmpty()
                && shimukeChi.isEmpty()
                && shipDate.isEmpty()
                && printStatus.isEmpty()) {

            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2000));
            return;
        }

        // 検索実行
        caseMarkPrintInvoiceSearch(invoiceNo, shimukeChi, shipDate, printStatus);

    }

    /**
     * ケースマーク情報受信
     */
    private void caseMarkPrintInvoiceSearch(String invoiceNo, String shimukeChi, String shipDate, String printStatus) {
        try {
            // ケースマーク情報受信
            JsonObject jsonObject = new JsonObject();
            Util.putPropertyStr(jsonObject, "invoiceNo", invoiceNo);
            Util.putPropertyStr(jsonObject, "shimukeChi", shimukeChi);
            Util.putPropertyStr(jsonObject, "syukkaDate", shipDate);
            jsonObject.addProperty("printState", printStatus);
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_CASEMARN_PRINT_INVOICE_SEARCH;
            new CaseMarkPrintInvoiceSearchTask(caseMarkPrintInvoiceSearchCallback, url, jsonObject.toString()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
        }
    }

    /**
     * ケースマーク情報受信Callback
     */
    CaseMarkPrintInvoiceSearchTask.Callback<CaseMarkPrintInvoiceSearchResponse> caseMarkPrintInvoiceSearchCallback = new CaseMarkPrintInvoiceSearchTask.Callback<CaseMarkPrintInvoiceSearchResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialogを表示する
            if (showProgress) {
                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0030));
            }
        }
        @Override
        public void onTaskFinished(CaseMarkPrintInvoiceSearchResponse response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();

            // エラーレスポンスの場合
            if (!Constants.API_RESPONSE_STATUS_CODE_OK.equals(response.getStatus())) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9001));
                Application.log.e(TAG, "status=" + response.getStatus() + " ,errorCode=" + response.getErrorCode());
                return;
            }

            // 検索結果ヒットなしの場合
            if (response.getData().getList().size() <= 0) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2001));
                return;
            }

            try {
                // 取得データDB登録
                new KonpoOuterDao().bulkInsertForCsPrint(Application.dbHelper.getWritableDatabase(), response.getData().getList());
            } catch (SQLiteException e) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
                Application.log.e(TAG, e);
                return;
            }

            // リスト更新
            searchListUpdate(new KonpoOuterDao().getOuterInfoListGroupByInvoiceNo(Application.dbHelper.getWritableDatabase()));
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
    public void searchListUpdate(ArrayList<CaseMarkPrintStatusInfo> searchList) {
        // 明細更新
        this.invoiceInfoList.clear();
        for (CaseMarkPrintStatusInfo item : searchList) {
            CaseMarkPrintStatusInfo new_item = new CaseMarkPrintStatusInfo(
                    item.getInvoiceNo()
                    ,item.getDestination()
                    ,item.getShipDate()
                    ,new CategoryMasterDao().getCategory(Application.dbHelper.getReadableDatabase(),"EYUSOMEANS",item.getShippingMode()).getElementName()
                    ,item.getPrintStatus());
            this.invoiceInfoList.add(new_item);
        }
        invoiceAdapter.notifyDataSetChanged();

        // 結果数更新
        mBinding.detailCount.setText(String.valueOf(invoiceAdapter.getItemCount()));
    }

    /**
     * 一括印刷ボタンタップ処理
     */
    private void clickAllPrintButton() {
        // リストデータなし
        if(invoiceAdapter.getItemCount() <= 0) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2002));
            return;
        }

        // ケースマーク印刷
        new CaseMarkPrintDataCreateTask(caseMarkPrintTaskCallBack, "", outputPDFPath).execute();
    }

    /**
     * 印刷ボタンタップ時処理
     */
    private void clickPrintButton() {
        int selectRow = invoiceAdapter.getSelectedPosition();
        // リストデータなし
        if(invoiceAdapter.getItemCount() <= 0) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2002));
            return;
        }

        // 選択行なし
        if(selectRow == -1) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2003));
            return;
        }

        // ケースマーク印刷
        new CaseMarkPrintDataCreateTask(caseMarkPrintTaskCallBack
                , this.invoiceInfoList.get(selectRow).getInvoiceNo(), outputPDFPath).execute();
    }

    CaseMarkPrintDataCreateTask.Callback<CaseMarkPrintInfo> caseMarkPrintTaskCallBack = new CaseMarkPrintDataCreateTask.Callback<CaseMarkPrintInfo>() {
        @Override
        public void onPreExecute() {
            mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0028));
        }

        @Override
        public void onTaskFinished(String invoiceNo) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();

            // ケースマーク印刷
            caseMarkPrint();

            // 印刷済み送信
            updatePrintedFlg(invoiceNo);
        }

        @Override
        public void onError() {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // エラーメッセージを表示
            mUtilListener.showAlertDialog(getString(R.string.const_err_message_E9000));
        }
    };

    /**
     * 印刷済みフラグ更新
     */
    private void updatePrintedFlg(String invoiceNo){
        String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_CASEMARK_PRINTED_REGIST;
        new PostCaseMarkPrintedRegistTask(caseMarkPrintedRegisrtCallback, url, invoiceNo, "1").execute();
    }

    PostCaseMarkPrintedRegistTask.Callback<SimpleResponse> caseMarkPrintedRegisrtCallback = new PostCaseMarkPrintedRegistTask.Callback<SimpleResponse>() {
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

            // リスト選択解除
            invoiceAdapter.setSelectedPosition(-1);

            // リスト更新
            searchListUpdate(new KonpoOuterDao().getOuterInfoListGroupByInvoiceNo(Application.dbHelper.getWritableDatabase()));

            // メッセージ表示
            mUtilListener.showSnackBarOnUiThread(mUtilListener.getDataBaseMessage(R.string.info_message_I0007));
        }

        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // エラーメッセージを表示
            mUtilListener.showAlertDialog(getString(R.string.const_err_message_E9000));
        }
    };

    // ケースマーク印刷
    private void caseMarkPrint() {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);

        // Set job name, which will be displayed in the print queue
        String jobName = getActivity().getString(R.string.app_name) + " Document";

        CustomDocumentPrintAdapter pda = new CustomDocumentPrintAdapter(outputPDFPath);

        // 印刷設定
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
        builder.setDuplexMode(PrintAttributes.DUPLEX_MODE_NONE);
        printManager.print(jobName, pda, builder.build()); //
    }
}
