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

        // ボタン設定
        mListener.setGroupsVisibility(true, true, true);
        mListener.setFooterButton(null, null, null, null, null);

        // リーダー接続アイコン・電池アイコン
        mListener.setReaderConnectButtonVisibility(View.VISIBLE);
        mListener.setBatteryStateImageVisibility(View.VISIBLE);

        // タイトル
        mListener.setSubTitleText(getString(R.string.cargo_status));
        mListener.setScreenId(getString(R.string.screen_id_cargo_status_check));

        // RecyclerViewの設定
        mListAdapter = new CargoStatusRecyclerViewAdapter(mCargoStatusInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mBinding.invoiceList.setHasFixedSize(true);
        mBinding.invoiceList.setLayoutManager(llm);
        mBinding.invoiceList.setAdapter(mListAdapter);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        // 戻るボタン設定
        View.OnClickListener backClickListener = v -> onBackButtonClick(mBinding.getRoot());
        mListener.setBackButton(backClickListener);

        // TODO テスト用
        mBinding.testButton.setOnClickListener(v -> {
            Bundle arg = new Bundle();
            arg.putString(CheckPackInstructionsFragment.ARG_INVOICE_NO, "03BTTB2-0311");
            arg.putString(CheckPackInstructionsFragment.ARG_PREFIX, "K");
            CheckPackInstructionsFragment fragment = new CheckPackInstructionsFragment();
            fragment.setArguments(arg);
            mListener.replaceFragmentWithStack(fragment, TAG);
//            // JSON作成
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("hachuNo", "2206004672");
//            jsonObject.addProperty("hachuEda", 1);
//            // 貨物情報取得
//            new GetCargoStatusInfoTask(this, jsonObject.toString()).execute();
        });
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        // 区分マスタ取得(移動区分)
        mMovingCategoryList = mUtilListener.getCategoryList(Constants.KBN_EIDO);

        // 画面表示
        showCargoInfo(mCargoStatusInfoList.get(0));
        // RFIDモードでスキャナーOpen
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
    /* RFIDモードだけなので使用しない */
    @Override
    protected void openScanner() { }

    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        // スキャナークローズ
        stopRFIDScan();
        // 呼び出し元の画面に戻る
        mListener.popBackStack();
    }

    // region [ scanner delegate & listener ]
    @Override
    public void onRFIDDataReceived(CommScanner commScanner, RFIDDataReceivedEvent rfidDataReceivedEvent) {
        // データ通信中は何もしない
        if (mSendingFlag) { return; }
        mHandler.post(() -> {
            try {
                // データ通信中は何もしない
                if (mSendingFlag) { return; }
                List<RFIDData> rfidDataList = rfidDataReceivedEvent.getRFIDData();
                for (RFIDData rfidData : rfidDataList) {
                    // 読取データ取得
                    byte[] uii = rfidData.getUII();
                    // タグ情報生成
                    TagInfo tagInfo = Util.convertEpcToTagInfo(uii);
                    // 対象外は読み飛ばし
                    if (tagInfo == null) { continue; }
                    // 通信中フラグOn
                    mSendingFlag = true;
                    // JSON作成
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("hachuNo", tagInfo.getPlaceOrderNo());
                    jsonObject.addProperty("hachuEda", tagInfo.getBranchNo());
                    // 貨物情報取得
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
        // ProgressDialogを表示する
        if (showProgress) {
//            mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0030));
            mUtilListener.showProgressDialog(R.string.info_message_I0030);
        }
    }

    @Override
    public void onTaskFinished(CargoStatusResponse response) {
        // ProgressDialogを閉じる
        mUtilListener.dismissProgressDialog();
        // 画面表示
        showCargoInfo(response.getData());
        // 通信中フラグOff
        mSendingFlag = false;
    }

    @Override
    public void onError(int httpResponseStatusCode, int messageId) {
        // ProgressDialogを閉じる
        mUtilListener.dismissProgressDialog();
        // エラーメッセージを表示
        mUtilListener.showInformationDialog(messageId, (dialog, which) -> {
            // 通信中フラグOff
            mSendingFlag = false;
        });
    }
    // endregion

    private void showCargoInfo(CargoStatusInfo info) {
        final String mark = "●";
        try {

            // 蔵置にロケーションNoをセットする
            String location = info.getLocationNo();
            info.setStorage(location);
            // 移動区分の要素備考と同じかチェック
            for (CategoryInfo item : mMovingCategoryList) {
                // 要素備考とロケーションNoが同じ場合
                if (item.getElementMemo().equals(location)) {
                    // 蔵置を要素名に変更する
                    info.setStorage(item.getElementName());
                    break;
                }
            }

            /* 出庫ステータス判定 */
            String status = info.getIssueStatus();
            if (!TextUtils.isEmpty(status)) {
                // ピッキング
                if ("05".compareTo(status) <= 0) {
                    info.setPicking(mark);
                }
                // 梱包
                if ("06".compareTo(status) <= 0) {
                    info.setPacking(mark);
                }
                // ケースマーク
                if ("07".compareTo(status) <= 0) {
                    info.setCaseMark(mark);
                }
                // 出庫
                if ("09".compareTo(status) <= 0) {
                    info.setIssue(mark);
                }
            }
            // リストをクリアして追加
            mCargoStatusInfoList.clear();
            mCargoStatusInfoList.add(info);
            // RecyclerViewに反映する
            mListAdapter.notifyDataSetChanged();

            /* TextViewに情報をセット */
            // 発注番号 + 枝番
            mBinding.tvPurchaseOrderNoAndBranchNoValue.setText(info.getTagNo());
            // 受注番号
            mBinding.tvOrderNoValue.setText(info.getReceiveOrderNo());
            // プラント名
            mBinding.tvPlantNameValue.setText(info.getPlantName());
            // 得意先名
            mBinding.tvCustomerNameValue.setText(info.getCustomerName());
            // 品目コード
            mBinding.tvItemCodeValue.setText(info.getItemCode());
            // 品目名
            mBinding.tvItemNameValue.setText(info.getItemName());
            // 危険物
            mBinding.tvDangerousGoodsValue.setText(info.getHazmatCode());
            // 棚番
            mBinding.tvShelfNumberValue.setText(info.getShelfNo());
            // 営業担当者
            mBinding.tvSalesStaffValue.setText(info.getSalesStaffName());
            // 仕入先品目コード
            mBinding.tvSupplierItemCodeValue.setText(info.getSupplierItemCode());
            // 発注単位
            mBinding.tvPlaceOrderUnitValue.setText(info.getPlaceOrderUnit());
            // 販売単位
            mBinding.tvReceiveOrderUnitValue.setText(info.getSalesUnit());
            // 在庫単位
            mBinding.tvStockUnitValue.setText(info.getBaseUnit());

        } catch (Exception e) {
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(R.string.const_err_message_E9000);
        }
    }
}