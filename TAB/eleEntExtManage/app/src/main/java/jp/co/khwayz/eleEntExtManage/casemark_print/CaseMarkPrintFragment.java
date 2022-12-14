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

    // ?????????PDF????????????
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
        // ???????????????
        mListener.setGroupsVisibility(true, true, true);
        mListener.setFooterButton(null, null, null, null, null);

        // ???????????????????????????????????????????????????
        mListener.setReaderConnectButtonVisibility(View.INVISIBLE);
        mListener.setBatteryStateImageVisibility(View.VISIBLE);

        // ????????????
        mListener.setSubTitleText(getString(R.string.casemark) + getString(R.string.casemark_print));
        mListener.setScreenId(getString(R.string.screen_id_casemark_print));

        // ???????????????
        mBinding.etCasemarkPrintInvoiceNo.setText("");
        mBinding.tvCasemarkPrintShipDate.setText("");

        // ?????????????????????
        CategoryMasterDao categoryMasterDao = new CategoryMasterDao();
        List<CategoryInfo> destinationDataSet = categoryMasterDao.getDestinationSpinnerArray(Application.dbHelper.getReadableDatabase());
        // ????????????????????????
        CategorySpinnerAdapter destinationSpinnerAdapter = new CategorySpinnerAdapter(getContext(), destinationDataSet);
        mBinding.spCasemarkPrintDestination.setAdapter(destinationSpinnerAdapter);

        // ????????????????????????
        ArrayList<String> printStatusList = new ArrayList<>();
        for (Constants.CaseMarkPintStatusChoices choice : Constants.CaseMarkPintStatusChoices.values()) {
            printStatusList.add(choice.getCaseMarkPrintStatusChoices());
        }
        ArrayAdapter<String> coolAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, printStatusList);
        coolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Spinner???Adapter????????????
        mBinding.spPrintStatus.setAdapter(coolAdapter);
        mBinding.spPrintStatus.setSelection(Constants.CaseMarkPintStatusChoices.NotPrinted.ordinal());

        // ???????????????????????????
        this.invoiceList = mBinding.invoiceList;
        if(this.invoiceInfoList == null) {
            this.invoiceInfoList = new ArrayList<>();
        }

        // ???????????????????????????
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
        // ?????????????????????
        /*************************************/
        // ?????????????????????
        View.OnClickListener backClickListener = v -> {
            onBackButtonClick(mBinding.getRoot());
        };
        mListener.setBackButton(backClickListener);

        // ?????????????????????
        View.OnClickListener printAllClickListener = v -> clickAllPrintButton();
        ButtonInfo printAllButtonInfo = new ButtonInfo(getString(R.string.print_all), printAllClickListener);

        // ???????????????
        View.OnClickListener printClickListener = v -> clickPrintButton();
        ButtonInfo printButtonInfo = new ButtonInfo(getString(R.string.print), printClickListener);

        // ???????????????????????????
        mListener.setFooterButton(null, null, null, printAllButtonInfo,printButtonInfo);

        /*************************************/
        // ??????????????????????????????
        /*************************************/
        // ?????????
        mBinding.btCasemarkPrintShipDate.setOnClickListener(v -> Calender.show(getActivity(), mBinding.tvCasemarkPrintShipDate));
        // ???????????????
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
     * ?????????????????????
     * @param view : ??????????????????????????????
     */
    @Override
    protected void onBackButtonClick(View view) {
        // ?????????????????????????????????
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

        // ??????????????????????????????
        String invoiceNo = mBinding.etCasemarkPrintInvoiceNo.getText().toString();
        CategoryInfo destinationSp = (CategoryInfo)mBinding.spCasemarkPrintDestination.getSelectedItem();
        String shimukeChi = destinationSp.getElementName();
        String shipDate = mBinding.tvCasemarkPrintShipDate.getText().toString();
        String printStatus = mBinding.spPrintStatus.getSelectedItem().toString().equals(Constants.CaseMarkPintStatusChoices.NotPrinted.getCaseMarkPrintStatusChoices()) ?
                String.valueOf(Constants.CaseMarkPintStatusChoices.NotPrinted.ordinal()) : String.valueOf(Constants.CaseMarkPintStatusChoices.Printed.ordinal());

        // ??????????????????????????????
        if( invoiceNo.isEmpty()
                && shimukeChi.isEmpty()
                && shipDate.isEmpty()
                && printStatus.isEmpty()) {

            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2000));
            return;
        }

        // ????????????
        caseMarkPrintInvoiceSearch(invoiceNo, shimukeChi, shipDate, printStatus);

    }

    /**
     * ??????????????????????????????
     */
    private void caseMarkPrintInvoiceSearch(String invoiceNo, String shimukeChi, String shipDate, String printStatus) {
        try {
            // ??????????????????????????????
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
     * ??????????????????????????????Callback
     */
    CaseMarkPrintInvoiceSearchTask.Callback<CaseMarkPrintInvoiceSearchResponse> caseMarkPrintInvoiceSearchCallback = new CaseMarkPrintInvoiceSearchTask.Callback<CaseMarkPrintInvoiceSearchResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialog???????????????
            if (showProgress) {
                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0030));
            }
        }
        @Override
        public void onTaskFinished(CaseMarkPrintInvoiceSearchResponse response) {
            // ProgressDialog????????????
            mUtilListener.dismissProgressDialog();

            // ?????????????????????????????????
            if (!Constants.API_RESPONSE_STATUS_CODE_OK.equals(response.getStatus())) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9001));
                Application.log.e(TAG, "status=" + response.getStatus() + " ,errorCode=" + response.getErrorCode());
                return;
            }

            // ????????????????????????????????????
            if (response.getData().getList().size() <= 0) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2001));
                return;
            }

            try {
                // ???????????????DB??????
                new KonpoOuterDao().bulkInsertForCsPrint(Application.dbHelper.getWritableDatabase(), response.getData().getList());
            } catch (SQLiteException e) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
                Application.log.e(TAG, e);
                return;
            }

            // ???????????????
            searchListUpdate(new KonpoOuterDao().getOuterInfoListGroupByInvoiceNo(Application.dbHelper.getWritableDatabase()));
        }

        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialog????????????
            mUtilListener.dismissProgressDialog();
            // ?????????????????????????????????
            mUtilListener.showAlertDialog(messageId);
        }
    };

    /**
     * ????????????
     */
    public void searchListUpdate(ArrayList<CaseMarkPrintStatusInfo> searchList) {
        // ????????????
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

        // ???????????????
        mBinding.detailCount.setText(String.valueOf(invoiceAdapter.getItemCount()));
    }

    /**
     * ????????????????????????????????????
     */
    private void clickAllPrintButton() {
        // ????????????????????????
        if(invoiceAdapter.getItemCount() <= 0) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2002));
            return;
        }

        // ????????????????????????
        new CaseMarkPrintDataCreateTask(caseMarkPrintTaskCallBack, "", outputPDFPath).execute();
    }

    /**
     * ?????????????????????????????????
     */
    private void clickPrintButton() {
        int selectRow = invoiceAdapter.getSelectedPosition();
        // ????????????????????????
        if(invoiceAdapter.getItemCount() <= 0) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2002));
            return;
        }

        // ???????????????
        if(selectRow == -1) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2003));
            return;
        }

        // ????????????????????????
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
            // ProgressDialog????????????
            mUtilListener.dismissProgressDialog();

            // ????????????????????????
            caseMarkPrint();

            // ??????????????????
            updatePrintedFlg(invoiceNo);
        }

        @Override
        public void onError() {
            // ProgressDialog????????????
            mUtilListener.dismissProgressDialog();
            // ?????????????????????????????????
            mUtilListener.showAlertDialog(getString(R.string.const_err_message_E9000));
        }
    };

    /**
     * ???????????????????????????
     */
    private void updatePrintedFlg(String invoiceNo){
        String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_CASEMARK_PRINTED_REGIST;
        new PostCaseMarkPrintedRegistTask(caseMarkPrintedRegisrtCallback, url, invoiceNo, "1").execute();
    }

    PostCaseMarkPrintedRegistTask.Callback<SimpleResponse> caseMarkPrintedRegisrtCallback = new PostCaseMarkPrintedRegistTask.Callback<SimpleResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialog???????????????
            if (showProgress) {
                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0031));
            }
        }

        @Override
        public void onTaskFinished(SimpleResponse response) {
            // ProgressDialog????????????
            mUtilListener.dismissProgressDialog();

            // ?????????????????????????????????
            if (!Constants.API_RESPONSE_STATUS_CODE_OK.equals(response.getStatus())) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9001));
                Application.log.e(TAG, "status=" + response.getStatus() + " ,errorCode=" + response.getErrorCode());
                return;
            }

            // ?????????????????????
            invoiceAdapter.setSelectedPosition(-1);

            // ???????????????
            searchListUpdate(new KonpoOuterDao().getOuterInfoListGroupByInvoiceNo(Application.dbHelper.getWritableDatabase()));

            // ?????????????????????
            mUtilListener.showSnackBarOnUiThread(mUtilListener.getDataBaseMessage(R.string.info_message_I0007));
        }

        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialog????????????
            mUtilListener.dismissProgressDialog();
            // ?????????????????????????????????
            mUtilListener.showAlertDialog(getString(R.string.const_err_message_E9000));
        }
    };

    // ????????????????????????
    private void caseMarkPrint() {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);

        // Set job name, which will be displayed in the print queue
        String jobName = getActivity().getString(R.string.app_name) + " Document";

        CustomDocumentPrintAdapter pda = new CustomDocumentPrintAdapter(outputPDFPath);

        // ????????????
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
        builder.setDuplexMode(PrintAttributes.DUPLEX_MODE_NONE);
        printManager.print(jobName, pda, builder.build()); //
    }
}
