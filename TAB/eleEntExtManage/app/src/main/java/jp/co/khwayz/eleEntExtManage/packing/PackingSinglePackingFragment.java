package jp.co.khwayz.eleEntExtManage.packing;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.densowave.scannersdk.Const.CommConst;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.CategorySpinnerAdapter;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.filter.DecimalDigitsInputFilter;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.common.models.InnerInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoShijiKeyInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoInnerDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPackingSinglePackingBinding;
import jp.co.khwayz.eleEntExtManage.packing.task.SinglePackingRegistTask;
import jp.co.khwayz.eleEntExtManage.util.Util;

public class PackingSinglePackingFragment extends BaseFragment {
    // DataBinding
    FragmentPackingSinglePackingBinding mBinding;

    // ARGS
    private static final String ARGS_INVOICE_NO = "invoice_no";
    private static final String ARGS_RENBAN = "renban";
    private static final String ARGS_LINE_NO = "lineNo";
    private static final String ARGS_HACHU_NO = "hachuNo";
    private static final String ARGS_SYUKKA_SU = "syukkaSu";
    private static String mInvoiceNo;
    private static int mRenban;
    private static int mLineNo;
    private static String mPlaceOrderNo;
    private static String mIssueQuantity;

    // Adapter
    private CategorySpinnerAdapter mWorkDescriptionSpinner_1;
    private CategorySpinnerAdapter mWorkDescriptionSpinner_2;
    private CategorySpinnerAdapter mWorkDescriptionSpinner_3;
    private CategorySpinnerAdapter mWorkDescriptionSpinner_4;
    private CategorySpinnerAdapter mUnitSpinner;
    private CategorySpinnerAdapter mInnerContainerSpinner;
    private CategorySpinnerAdapter mOuterContainerSpinner;

    // List
    List<CategoryInfo> mWorkDescriptionDataSet;
    List<CategoryInfo> mUnitDataSet;
    List<CategoryInfo> mInnerContainerDataSet;
    List<CategoryInfo> mOuterContainerDataSet;

    /**
     * ?????????????????????
     * @param invoiceNo
     * @param renban
     * @param lineNo
     * @return
     */
    public static PackingSinglePackingFragment newInstance(String invoiceNo, int renban, int lineNo, String hachuNo, String syukkaSu){
        PackingSinglePackingFragment fragment = new PackingSinglePackingFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_INVOICE_NO, invoiceNo);
        args.putInt(ARGS_RENBAN, renban);
        args.putInt(ARGS_LINE_NO, lineNo);
        args.putString(ARGS_HACHU_NO, hachuNo);
        args.putString(ARGS_SYUKKA_SU, syukkaSu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentPackingSinglePackingBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mInvoiceNo = args.getString(ARGS_INVOICE_NO);
        this.mRenban = args.getInt(ARGS_RENBAN);
        this.mLineNo = args.getInt(ARGS_LINE_NO);
        this.mPlaceOrderNo = args.getString(ARGS_HACHU_NO);
        this.mIssueQuantity = args.getString(ARGS_SYUKKA_SU);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListener.setSubHeaderExplanationText("");
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
        mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.packing_single_packing_input));
        mListener.setScreenId(getString(R.string.screen_id_packing_single));
        mListener.setSubHeaderExplanationText(getString(R.string.single_packing_explanation));

        // ****************************
        // ???????????????????????????
        // ****************************
        // ????????????
        mWorkDescriptionDataSet = mUtilListener.getCategoryList(Constants.KBN_EKONPOMEISAI);;
        CategoryInfo topValue = new CategoryInfo("","");
        mWorkDescriptionDataSet.add(0, topValue);
        // ??????
        mUnitDataSet = mUtilListener.getCategoryList(Constants.KBN_EKIKENTANI);;
        mUnitDataSet.add(0, topValue);
        // ????????????
        mInnerContainerDataSet = mUtilListener.getCategoryList(Constants.KBN_EKIKENNAISO);;
        mInnerContainerDataSet.add(0, topValue);
        // ????????????
        mOuterContainerDataSet = mUtilListener.getCategoryList(Constants.KBN_EKIKENGAISO);;
        mOuterContainerDataSet.add(0, topValue);

        // ****************************
        // ????????????????????????
        // ****************************
        // ?????????????????????
        mWorkDescriptionSpinner_1 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.packingSinglePackagingSpinner1.setAdapter(mWorkDescriptionSpinner_1);

        // ?????????????????????
        mWorkDescriptionSpinner_2 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.packingSinglePackagingSpinner2.setAdapter(mWorkDescriptionSpinner_2);

        // ?????????????????????
        mWorkDescriptionSpinner_3 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.packingSinglePackagingSpinner3.setAdapter(mWorkDescriptionSpinner_3);

        // ?????????????????????
        mWorkDescriptionSpinner_4 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.packingSinglePackagingSpinner4.setAdapter(mWorkDescriptionSpinner_4);

        // ??????
        mUnitSpinner = new CategorySpinnerAdapter(getContext(), mUnitDataSet);
        mBinding.packingSinglePackagingSpinnerUnit.setAdapter(mUnitSpinner);

        // ????????????
        mInnerContainerSpinner = new CategorySpinnerAdapter(getContext(), mInnerContainerDataSet);
        mBinding.packingSinglePackagingSpinnerInteriorContainer.setAdapter(mInnerContainerSpinner);

        // ????????????
        mOuterContainerSpinner = new CategorySpinnerAdapter(getContext(), mOuterContainerDataSet);
        mBinding.packingSinglePackagingSpinnerOuterContainer.setAdapter(mOuterContainerSpinner);
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

        // ???????????????
        ButtonInfo confirmButtonInfo = new ButtonInfo(getString(R.string.confirm), v -> confirmationButton());
        mListener.setFooterButton(null, null, confirmButtonInfo, null, null);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();

        // ???????????????????????????
        mBinding.packingSinglePackagingPackingCapacity1.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.packingSinglePackagingPackingCapacity2.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.packingSinglePackagingPackingCapacity3.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.packingSinglePackagingPackingCapacity4.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.packingSinglePackagingPackingNetWeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 3)});
        mBinding.packingSinglePackagingPackingInternalCapacity.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});

        // ???????????????
        String nyukoDate = new SyukkoShijiDetailDao().getNyukoDate(Application.dbHelper.getWritableDatabase()
                , mInvoiceNo, mRenban, mLineNo);
        mBinding.packingSinglePackagingReceiptDay.setText(nyukoDate == null ? "" : nyukoDate);

        // ?????????????????????????????????
        InnerInfo innerRecord = new KonpoInnerDao().getRecord(Application.dbHelper.getWritableDatabase()
                , mInvoiceNo, mRenban, mLineNo);
        if(innerRecord != null){
            // ???????????????
            int index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, innerRecord.getInnerSagyo1());
            mBinding.packingSinglePackagingSpinner1.setSelection(index);
            mBinding.packingSinglePackagingPackingCapacity1.setText(innerRecord.getInnerSagyo1Siyo() == null ?
                    null : String.format("%.1f",innerRecord.getInnerSagyo1Siyo()));
            // ???????????????
            index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, innerRecord.getInnerSagyo2());
            mBinding.packingSinglePackagingSpinner2.setSelection(index);
            mBinding.packingSinglePackagingPackingCapacity2.setText(innerRecord.getInnerSagyo2Siyo() == null ?
                    null : String.format("%.1f",innerRecord.getInnerSagyo2Siyo()));
            // ???????????????
            index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, innerRecord.getInnerSagyo3());
            mBinding.packingSinglePackagingSpinner3.setSelection(index);
            mBinding.packingSinglePackagingPackingCapacity3.setText(innerRecord.getInnerSagyo3Siyo() == null ?
                    null : String.format("%.1f",innerRecord.getInnerSagyo3Siyo()));
            // ???????????????
            index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, innerRecord.getInnerSagyo4());
            mBinding.packingSinglePackagingSpinner4.setSelection(index);
            mBinding.packingSinglePackagingPackingCapacity4.setText(innerRecord.getInnerSagyo4Siyo() == null ?
                    null : String.format("%.1f",innerRecord.getInnerSagyo4Siyo()));
            // ???????????????
            mBinding.packingSinglePackagingPackingLabelNumberOfSheets.setText(innerRecord.getLabelSu() == null ?
                    null : String.valueOf(innerRecord.getLabelSu()));
            // NW
            mBinding.packingSinglePackagingPackingNetWeight.setText(innerRecord.getNetWeight() == null ?
                    null : String.format("%.3f",innerRecord.getNetWeight()));
            // ?????????
            mBinding.packingSinglePackagingPackingInternalCapacity.setText(innerRecord.getDanNaiyoRyo() == null ?
                    null : String.format("%.1f",innerRecord.getDanNaiyoRyo()));
            // ??????
            index = Util.getSpinnerSelectPosition(mUnitDataSet, innerRecord.getDanTani());
            mBinding.packingSinglePackagingSpinnerUnit.setSelection(index);
            // ????????????
            index = Util.getSpinnerSelectPosition(mInnerContainerDataSet, innerRecord.getDanNaisoYoki());
            mBinding.packingSinglePackagingSpinnerInteriorContainer.setSelection(index);
            // ??????
            mBinding.packingSinglePackagingPackingNumber.setText(innerRecord.getDanHonsu() == null ?
                    null : String.valueOf(innerRecord.getDanHonsu()));
            // ????????????
            index = Util.getSpinnerSelectPosition(mOuterContainerDataSet, innerRecord.getDanGaisoYoki());
            mBinding.packingSinglePackagingSpinnerOuterContainer.setSelection(index);
            // ??????
            mBinding.packingSinglePackagingRemarks.setText(innerRecord.getBiko());
        }
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

    /**
     * ?????????????????????
     * @param view : ??????????????????????????????
     */
    @Override
    protected void onBackButtonClick(View view) {
        // ??????????????????
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // ?????????????????????????????????
            mListener.popBackStack();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0022, listener);
    }

    private void confirmationButton() {
        // ******************************************************
        // ?????????????????????????????????????????????????????????
        // ******************************************************
        InnerInfo innerInfoItem = new InnerInfo(mInvoiceNo);
        innerInfoItem.setRenban(mRenban);
        innerInfoItem.setLineNo(mLineNo);
        // ???????????????
        CategoryInfo infoSpSagyo1 = (CategoryInfo) mBinding.packingSinglePackagingSpinner1.getSelectedItem();
        innerInfoItem.setInnerSagyo1(infoSpSagyo1.getElement().isEmpty() ? null : infoSpSagyo1.getElement());
        innerInfoItem.setInnerSagyo1Siyo(mBinding.packingSinglePackagingPackingCapacity1.getText().toString().isEmpty() ?
                null : Double.parseDouble(mBinding.packingSinglePackagingPackingCapacity1.getText().toString()));
        // ???????????????
        CategoryInfo infoSpSagyo2 = (CategoryInfo) mBinding.packingSinglePackagingSpinner2.getSelectedItem();
        innerInfoItem.setInnerSagyo2(infoSpSagyo2.getElement().isEmpty() ? null : infoSpSagyo2.getElement());
        innerInfoItem.setInnerSagyo2Siyo(mBinding.packingSinglePackagingPackingCapacity2.getText().toString().isEmpty() ?
                null : Double.parseDouble(mBinding.packingSinglePackagingPackingCapacity2.getText().toString()));
        // ???????????????
        CategoryInfo infoSpSagyo3 = (CategoryInfo) mBinding.packingSinglePackagingSpinner3.getSelectedItem();
        innerInfoItem.setInnerSagyo3(infoSpSagyo3.getElement().isEmpty() ? null : infoSpSagyo3.getElement());
        innerInfoItem.setInnerSagyo3Siyo(mBinding.packingSinglePackagingPackingCapacity3.getText().toString().isEmpty() ?
                null : Double.parseDouble(mBinding.packingSinglePackagingPackingCapacity3.getText().toString()));
        // ???????????????
        CategoryInfo infoSpSagyo4 = (CategoryInfo) mBinding.packingSinglePackagingSpinner4.getSelectedItem();
        innerInfoItem.setInnerSagyo4(infoSpSagyo4.getElement().isEmpty() ? null : infoSpSagyo4.getElement());
        innerInfoItem.setInnerSagyo4Siyo(mBinding.packingSinglePackagingPackingCapacity4.getText().toString().isEmpty() ?
                null : Double.parseDouble(mBinding.packingSinglePackagingPackingCapacity4.getText().toString()));
        // ???????????????
        innerInfoItem.setLabelSu(mBinding.packingSinglePackagingPackingLabelNumberOfSheets.getText().toString().isEmpty() ?
                null : Integer.parseInt(mBinding.packingSinglePackagingPackingLabelNumberOfSheets.getText().toString()));
        // NW
        innerInfoItem.setNetWeight(mBinding.packingSinglePackagingPackingNetWeight.getText().toString().isEmpty() ?
                null : Double.parseDouble(mBinding.packingSinglePackagingPackingNetWeight.getText().toString()));
        // ?????????
        innerInfoItem.setDanNaiyoRyo(mBinding.packingSinglePackagingPackingInternalCapacity.getText().toString().isEmpty() ?
                null : Double.parseDouble(mBinding.packingSinglePackagingPackingInternalCapacity.getText().toString()));
        // ??????
        CategoryInfo infoDanTani = (CategoryInfo) mBinding.packingSinglePackagingSpinnerUnit.getSelectedItem();
        innerInfoItem.setDanTani(infoDanTani.getElement().isEmpty() ? null : infoDanTani.getElement());
        // ????????????
        CategoryInfo infoDanNaisoYoki = (CategoryInfo) mBinding.packingSinglePackagingSpinnerInteriorContainer.getSelectedItem();
        innerInfoItem.setDanNaisoYoki(infoDanNaisoYoki.getElement().isEmpty() ? null : infoDanNaisoYoki.getElement());
        // ??????
        innerInfoItem.setDanHonsu(mBinding.packingSinglePackagingPackingNumber.getText().toString().isEmpty() ?
                null : Integer.parseInt(mBinding.packingSinglePackagingPackingNumber.getText().toString()));
        // ????????????
        CategoryInfo infoDanGaisoYoki = (CategoryInfo) mBinding.packingSinglePackagingSpinnerOuterContainer.getSelectedItem();
        innerInfoItem.setDanGaisoYoki(infoDanGaisoYoki.getElement().isEmpty() ? null : infoDanGaisoYoki.getElement());
        // ??????
        innerInfoItem.setBiko(mBinding.packingSinglePackagingRemarks.getText().toString().isEmpty()?
                null : mBinding.packingSinglePackagingRemarks.getText().toString());

        // ?????????????????????
        if(innerInfoItem.getInnerSagyo1() == null &&
                innerInfoItem.getInnerSagyo1Siyo() == null &&
                innerInfoItem.getInnerSagyo2() == null &&
                innerInfoItem.getInnerSagyo2Siyo() == null &&
                innerInfoItem.getInnerSagyo3() == null &&
                innerInfoItem.getInnerSagyo3Siyo() == null &&
                innerInfoItem.getInnerSagyo4() == null &&
                innerInfoItem.getInnerSagyo4Siyo() == null &&
                innerInfoItem.getLabelSu() == null &&
                innerInfoItem.getNetWeight() == null &&
                innerInfoItem.getDanNaiyoRyo() == null &&
                innerInfoItem.getDanTani() == null &&
                innerInfoItem.getDanNaisoYoki() == null &&
                innerInfoItem.getDanHonsu() == null &&
                innerInfoItem.getDanGaisoYoki() == null &&
                innerInfoItem.getBiko() == null
        ){
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2025));
            return;
        }

        // ??????????????????
        DialogInterface.OnClickListener listener = (dialog, which) -> {

            // ******************************************************
            // ????????????????????????
            // ******************************************************
            ArrayList<SyukkoShijiKeyInfo> updateKeyInfo = new ArrayList<>();
            // ????????????????????????????????????????????????????????????
            if(mBinding.packingSinglePackagingCheckSameItem.isChecked()){
                updateKeyInfo = new SyukkoShijiDetailDao().getSameDetailInfo (Application.dbHelper.getWritableDatabase()
                        ,mInvoiceNo, mPlaceOrderNo, mIssueQuantity);
            } else {
                // 1?????????
                SyukkoShijiKeyInfo item = new SyukkoShijiKeyInfo(mRenban, mLineNo);
                updateKeyInfo.add(item);
            }

            // ????????????????????????
            new SinglePackingRegistTask(singlePackingRegistCallBack
                    , mInvoiceNo, updateKeyInfo, innerInfoItem).execute();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0023, listener);
    }

    SinglePackingRegistTask.Callback singlePackingRegistCallBack = new SinglePackingRegistTask.Callback() {
        @Override
        public void onPreExecute() {
            mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0027));
        }

        @Override
        public void onTaskFinished(boolean result) {
            // ProgressDialog????????????
            mUtilListener.dismissProgressDialog();
            // ???????????????????????????
            Application.initFlag = true;
            // ?????????????????????????????????
            mListener.popBackStack();
            // ?????????????????????
            mUtilListener.showSnackBarOnUiThread(mUtilListener.getDataBaseMessage(R.string.info_message_I0007));
        }

        @Override
        public void onError() {
            // ProgressDialog????????????
            mUtilListener.dismissProgressDialog();
            // ?????????????????????????????????
            mUtilListener.showAlertDialog(getString(R.string.const_err_message_E9000));
        }
    };
}
