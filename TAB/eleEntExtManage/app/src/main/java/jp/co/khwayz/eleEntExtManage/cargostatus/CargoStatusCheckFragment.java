package jp.co.khwayz.eleEntExtManage.cargostatus;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.densowave.scannersdk.Common.CommScanner;
import com.densowave.scannersdk.Const.CommConst;
import com.densowave.scannersdk.Listener.RFIDDataDelegate;
import com.densowave.scannersdk.RFID.RFIDData;
import com.densowave.scannersdk.RFID.RFIDDataReceivedEvent;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentCargoStatusCheckBinding;
import jp.co.khwayz.eleEntExtManage.instr_cfm.CheckPackInstructionsFragment;
import jp.co.khwayz.eleEntExtManage.http.response.CargoStatusResponse;
import jp.co.khwayz.eleEntExtManage.http.task.get.GetCargoStatusInfoTask;
import jp.co.khwayz.eleEntExtManage.util.Util;

public class CargoStatusCheckFragment extends BaseFragment implements RFIDDataDelegate, GetCargoStatusInfoTask.Callback<CargoStatusResponse> {
    private static final String SAVE_INFO_LIST = "SAVE_INFO_LIST";
    // DataBinding
    private FragmentCargoStatusCheckBinding mBinding;
    private CargoStatusRecyclerViewAdapter mListAdapter;

    private boolean mSendingFlag;
    private ArrayList<CargoStatusInfo> mCargoStatusInfoList;

    private ArrayList<CategoryInfo> mMovingCategoryList;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_INFO_LIST, mCargoStatusInfoList);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCargoStatusInfoList = savedInstanceState.getParcelableArrayList(SAVE_INFO_LIST);
        } else {
            mCargoStatusInfoList = new ArrayList<>();
            mCargoStatusInfoList.add(new CargoStatusInfo());
        }
        mBinding = FragmentCargoStatusCheckBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListAdapter = null;
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
        mListener.setReaderConnectButtonVisibility(View.VISIBLE);
        mListener.setBatteryStateImageVisibility(View.VISIBLE);

        // ????????????
        mListener.setSubTitleText(getString(R.string.cargo_status));
        mListener.setScreenId(getString(R.string.screen_id_cargo_status_check));

        // RecyclerView?????????
        mListAdapter = new CargoStatusRecyclerViewAdapter(mCargoStatusInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mBinding.invoiceList.setHasFixedSize(true);
        mBinding.invoiceList.setLayoutManager(llm);
        mBinding.invoiceList.setAdapter(mListAdapter);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        // ?????????????????????
        View.OnClickListener backClickListener = v -> onBackButtonClick(mBinding.getRoot());
        mListener.setBackButton(backClickListener);

        // TODO ????????????
        mBinding.testButton.setOnClickListener(v -> {
            Bundle arg = new Bundle();
            arg.putString(CheckPackInstructionsFragment.ARG_INVOICE_NO, "03BTTB2-0311");
            arg.putString(CheckPackInstructionsFragment.ARG_PREFIX, "K");
            CheckPackInstructionsFragment fragment = new CheckPackInstructionsFragment();
            fragment.setArguments(arg);
            mListener.replaceFragmentWithStack(fragment, TAG);
//            // JSON??????
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("hachuNo", "2206004672");
//            jsonObject.addProperty("hachuEda", 1);
//            // ??????????????????
//            new GetCargoStatusInfoTask(this, jsonObject.toString()).execute();
        });
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        // ?????????????????????(????????????)
        mMovingCategoryList = mUtilListener.getCategoryList(Constants.KBN_EIDO);

        // ????????????
        showCargoInfo(mCargoStatusInfoList.get(0));
        // RFID???????????????????????????Open
        startRFIDScan(this, 0);
    }

    @Override
    public void CommStatusChanged(CommConst.ScannerStatus status) {
        try {
            switch (status) {
                case CLAIMED:
                    startRFIDScan(this, 0);
                    break;
                case CLOSED:
                    stopRFIDScan();
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
    public boolean hasScanner() {
        return true;
    }
    /* RFID??????????????????????????????????????? */
    @Override
    protected void openScanner() { }

    /**
     * ?????????????????????
     * @param view : ??????????????????????????????
     */
    @Override
    protected void onBackButtonClick(View view) {
        // ???????????????????????????
        stopRFIDScan();
        // ?????????????????????????????????
        mListener.popBackStack();
    }

    // region [ scanner delegate & listener ]
    @Override
    public void onRFIDDataReceived(CommScanner commScanner, RFIDDataReceivedEvent rfidDataReceivedEvent) {
        // ????????????????????????????????????
        if (mSendingFlag) { return; }
        mHandler.post(() -> {
            try {
                // ????????????????????????????????????
                if (mSendingFlag) { return; }
                List<RFIDData> rfidDataList = rfidDataReceivedEvent.getRFIDData();
                for (RFIDData rfidData : rfidDataList) {
                    // ?????????????????????
                    byte[] uii = rfidData.getUII();
                    // ??????????????????
                    TagInfo tagInfo = Util.convertEpcToTagInfo(uii);
                    // ???????????????????????????
                    if (tagInfo == null) { continue; }
                    // ??????????????????On
                    mSendingFlag = true;
                    // JSON??????
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("hachuNo", tagInfo.getPlaceOrderNo());
                    jsonObject.addProperty("hachuEda", tagInfo.getBranchNo());
                    // ??????????????????
                    new GetCargoStatusInfoTask(this, jsonObject.toString()).execute();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Application.log.e(TAG, e);
                mUtilListener.showSnackBarOnUiThread(TAG + " onRFIDDataReceived \n" + e.getMessage());
                mSendingFlag = false;
            }
        });
    }
    // endregion

    // region [ http callback ]
    @Override
    public void onPreExecute(boolean showProgress) {
        // ProgressDialog???????????????
        if (showProgress) {
//            mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0030));
            mUtilListener.showProgressDialog(R.string.info_message_I0030);
        }
    }

    @Override
    public void onTaskFinished(CargoStatusResponse response) {
        // ProgressDialog????????????
        mUtilListener.dismissProgressDialog();
        // ????????????
        showCargoInfo(response.getData());
        // ??????????????????Off
        mSendingFlag = false;
    }

    @Override
    public void onError(int httpResponseStatusCode, int messageId) {
        // ProgressDialog????????????
        mUtilListener.dismissProgressDialog();
        // ?????????????????????????????????
        mUtilListener.showInformationDialog(messageId, (dialog, which) -> {
            // ??????????????????Off
            mSendingFlag = false;
        });
    }
    // endregion

    private void showCargoInfo(CargoStatusInfo info) {
        final String mark = "???";
        try {

            // ???????????????????????????No??????????????????
            String location = info.getLocationNo();
            info.setStorage(location);
            // ???????????????????????????????????????????????????
            for (CategoryInfo item : mMovingCategoryList) {
                // ?????????????????????????????????No???????????????
                if (item.getElementMemo().equals(location)) {
                    // ?????????????????????????????????
                    info.setStorage(item.getElementName());
                    break;
                }
            }

            /* ??????????????????????????? */
            String status = info.getIssueStatus();
            if (!TextUtils.isEmpty(status)) {
                // ???????????????
                if ("05".compareTo(status) <= 0) {
                    info.setPicking(mark);
                }
                // ??????
                if ("06".compareTo(status) <= 0) {
                    info.setPacking(mark);
                }
                // ??????????????????
                if ("07".compareTo(status) <= 0) {
                    info.setCaseMark(mark);
                }
                // ??????
                if ("09".compareTo(status) <= 0) {
                    info.setIssue(mark);
                }
            }
            // ?????????????????????????????????
            mCargoStatusInfoList.clear();
            mCargoStatusInfoList.add(info);
            // RecyclerView???????????????
            mListAdapter.notifyDataSetChanged();

            /* TextView????????????????????? */
            // ???????????? + ??????
            mBinding.tvPurchaseOrderNoAndBranchNoValue.setText(info.getTagNo());
            // ????????????
            mBinding.tvOrderNoValue.setText(info.getReceiveOrderNo());
            // ???????????????
            mBinding.tvPlantNameValue.setText(info.getPlantName());
            // ????????????
            mBinding.tvCustomerNameValue.setText(info.getCustomerName());
            // ???????????????
            mBinding.tvItemCodeValue.setText(info.getItemCode());
            // ?????????
            mBinding.tvItemNameValue.setText(info.getItemName());
            // ?????????
            mBinding.tvDangerousGoodsValue.setText(info.getHazmatCode());
            // ??????
            mBinding.tvShelfNumberValue.setText(info.getShelfNo());
            // ???????????????
            mBinding.tvSalesStaffValue.setText(info.getSalesStaffName());
            // ????????????????????????
            mBinding.tvSupplierItemCodeValue.setText(info.getSupplierItemCode());
            // ????????????
            mBinding.tvPlaceOrderUnitValue.setText(info.getPlaceOrderUnit());
            // ????????????
            mBinding.tvReceiveOrderUnitValue.setText(info.getSalesUnit());
            // ????????????
            mBinding.tvStockUnitValue.setText(info.getBaseUnit());

        } catch (Exception e) {
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(R.string.const_err_message_E9000);
        }
    }
}