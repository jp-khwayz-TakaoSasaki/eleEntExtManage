package jp.co.khwayz.eleEntExtManage.instr_cfm;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.densowave.scannersdk.Const.CommConst;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentCheckShippingInstructionsBinding;
import jp.co.khwayz.eleEntExtManage.http.response.ShippingInstructionsResponse;
import jp.co.khwayz.eleEntExtManage.http.task.get.GetShippingInstructionsTask;

/**
 * 出荷指示確認画面
 */
public class CheckShippingInstructionsFragment extends BaseFragment implements GetShippingInstructionsTask.Callback<ShippingInstructionsResponse> {
    public static final String ARG_INVOICE_NO = "ARG_INVOICE_NO";
    public static final String ARG_PREFIX = "screenPrefix";

    // DataBinding
    FragmentCheckShippingInstructionsBinding mBinding;

    // private variable
    private String mInvoiceNo;
    private String mScreenPrefix = "P";
    private ArrayList<ShippingInstructionsInfo> mListData;
    private CheckShippingInstructionsRecyclerViewAdapter mListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // InvoiceNoを取得
        mInvoiceNo = null;
        if (getArguments() != null) {
            mInvoiceNo = getArguments().getString(ARG_INVOICE_NO, null);
            mScreenPrefix = getArguments().getString(ARG_PREFIX, null);
        }
        // InvoiceNo or 遷移元パラメータ無しはエラー
        if (TextUtils.isEmpty(mInvoiceNo) || TextUtils.isEmpty(mScreenPrefix)) {
            throw new RuntimeException(TAG + "：nothing arguments.");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentCheckShippingInstructionsBinding.inflate(inflater, container, false);
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

        // タイトル設定
        mListener.setGroupsVisibility(true, true, true);
        if (mScreenPrefix.equals("P")) {
            // ピッキング機能からの場合
            mListener.setSubTitleText(getString(R.string.picking) + getString(R.string.common_check_shipping_instructions));
        } else {
            // 梱包機能からの場合
            mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.common_check_shipping_instructions));
        }
        mListener.setScreenId(mScreenPrefix.equals("P") ? getString(R.string.screen_id_check_shipping_inst_p) : getString(R.string.screen_id_check_shipping_inst_k));

        // RecyclerViewの設定
        mListData = new ArrayList<>();
        mListAdapter = new CheckShippingInstructionsRecyclerViewAdapter(mListData);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mBinding.invoiceList.setHasFixedSize(true);
        mBinding.invoiceList.setLayoutManager(llm);
        mBinding.invoiceList.setAdapter(mListAdapter);
    }

    @Override
    public void eventSetting() {
        /* フッターボタン */
        // 戻るボタン設定
        View.OnClickListener backClickListener = v -> onBackButtonClick(mBinding.getRoot());
        mListener.setBackButton(backClickListener);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        // データ取得
        new GetShippingInstructionsTask(this, mInvoiceNo).execute();
    }

    @Override
    public void CommStatusChanged(CommConst.ScannerStatus status) { }
    @Override
    public boolean hasScanner() {
        return false;
    }
    @Override
    protected void openScanner() { }

    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        // 呼び出し元の画面に戻る
        mListener.popBackStack();
    }

    // region [ http callback ]
    @Override
    public void onPreExecute(boolean showProgress) {
        // ProgressDialogを表示する
        if (showProgress) {
//                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0030));
            mUtilListener.showProgressDialog(R.string.info_message_I0030);
        }
    }

    @Override
    public void onTaskFinished(ShippingInstructionsResponse response) {
        // ProgressDialogを閉じる
        mUtilListener.dismissProgressDialog();
        try {
            // 一覧クリア
            mListData.clear();
            // 一覧表示
            mListData.addAll(response.getData().getList());
        } catch (Exception e) {
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(R.string.const_err_message_E9000);
        } finally {
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(int httpResponseStatusCode, int messageId) {
        // ProgressDialogを閉じる
        mUtilListener.dismissProgressDialog();
        // エラーメッセージを表示
        mUtilListener.showAlertDialog(messageId);
    }
    // endregion
}
