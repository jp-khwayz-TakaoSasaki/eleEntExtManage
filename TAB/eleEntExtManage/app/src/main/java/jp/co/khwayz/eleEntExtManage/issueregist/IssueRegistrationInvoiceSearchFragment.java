package jp.co.khwayz.eleEntExtManage.issueregist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.densowave.scannersdk.Const.CommConst;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.Calender;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.CategorySpinnerAdapter;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.CategoryMasterDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentIssueRegistrationInvoiceSearchBinding;

public class IssueRegistrationInvoiceSearchFragment extends BaseFragment {
    // DataBinding
    FragmentIssueRegistrationInvoiceSearchBinding mBinding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentIssueRegistrationInvoiceSearchBinding.inflate(inflater, container, false);
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
        mListener.setSubTitleText(getString(R.string.issue_registration) + getString(R.string.issue_registration_invoice_search));
        mListener.setScreenId(getString(R.string.screen_id_registration_search));

        // Invoice番号
        mBinding.invoiceNoText.setText("");

        // 仕向け地
        CategoryMasterDao categoryMasterDao = new CategoryMasterDao();
        List<CategoryInfo> destinationDataSet = categoryMasterDao.getDestinationSpinnerArray(Application.dbHelper.getReadableDatabase());
        // 画面オブジェクト
        CategorySpinnerAdapter destinationSpinnerAdapter = new CategorySpinnerAdapter(getContext(), destinationDataSet);
        mBinding.destinationSpinner.setAdapter(destinationSpinnerAdapter);

        // 出荷日
        mBinding.shipDateText.setText("");
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        /* 出荷日 */
        mBinding.shipDateText.setOnClickListener(v -> Calender.show(getActivity(), mBinding.shipDateText));
        mBinding.shipDateButton.setOnClickListener(v -> Calender.show(getActivity(), mBinding.shipDateText));
        // 長押しでクリア
        mBinding.shipDateText.setOnLongClickListener(v -> {
            mBinding.shipDateText.setText("");
            return true;
        });

        /////////////////////////
        // フッターボタン
        /////////////////////////
        // 戻るボタン設定
        View.OnClickListener backClickListener = v -> onBackButtonClick(mBinding.getRoot());
        mListener.setBackButton(backClickListener);

        // データ受信
        View.OnClickListener dataReceiveClickListener = view -> receiveButtonClick();
        ButtonInfo dataReceiveButtonInfo = new ButtonInfo(getString(R.string.data_receive), dataReceiveClickListener);
        mListener.setFooterButton(null, null, null, null, dataReceiveButtonInfo);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
    }

    @Override
    public void CommStatusChanged(CommConst.ScannerStatus status) { }
    @Override
    public boolean hasScanner() { return false; }
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

    /**
     * データ受信ボタン押下
     */
    private void receiveButtonClick() {

        // InvoiceNo
        String invoiceNo = null;
        if (mBinding.invoiceNoText.getText() != null) {
            invoiceNo = mBinding.invoiceNoText.getText().toString();
        }
        if (TextUtils.isEmpty(invoiceNo)) invoiceNo = null;
        // 仕向地
        CategoryInfo categoryInfo = (CategoryInfo) mBinding.destinationSpinner.getSelectedItem();
        String destination = null;
        if (categoryInfo != null) {
            destination = categoryInfo.getElementName();
        }
        if (TextUtils.isEmpty(destination)) destination = null;
        // 出荷日
        String shipDate = null;
        if (mBinding.shipDateText.getText() != null) {
            shipDate = mBinding.shipDateText.getText().toString();
        }
        if (TextUtils.isEmpty(shipDate)) shipDate = null;

        // 検索条件が全て未入力はエラー
        if (invoiceNo == null && destination == null && shipDate == null) {
            mUtilListener.showAlertDialog(R.string.err_message_E2000);
            return;
        }

        /* 出庫確定画面に遷移する */
        // InvoiceNoがパラメータ
        Bundle arguments = new Bundle();
        arguments.putString(IssueRegCheckFragment.ARG_INVOICE_NO, invoiceNo);
        arguments.putString(IssueRegCheckFragment.ARG_DESTINATION, destination);
        arguments.putString(IssueRegCheckFragment.ARG_SHIP_DATE, shipDate);
        IssueRegCheckFragment nextFragment = new IssueRegCheckFragment();
        nextFragment.setArguments(arguments);
        mListener.replaceFragmentWithStack(nextFragment, TAG);
    }
}
