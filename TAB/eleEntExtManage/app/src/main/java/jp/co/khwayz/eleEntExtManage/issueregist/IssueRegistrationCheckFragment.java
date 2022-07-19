package jp.co.khwayz.eleEntExtManage.issueregist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.densowave.scannersdk.Const.CommConst;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.InvoiceInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentIssueRegistrationCheckBinding;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.MessageDialogFragment;

public class IssueRegistrationCheckFragment extends BaseFragment {
    // DataBinding
    FragmentIssueRegistrationCheckBinding mBinding;

    private RecyclerView invoiceList;
    private List<InvoiceInfo> invoiceInfoList;
    private TextView siNoText;
    private String siNo = "";
    private String readCount = "0/10";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        mListener.setSubTitleText(getString(R.string.picking) + getString(R.string.picking_invoice_scan_off_book));
        mListener.setScreenId(getString(R.string.screen_id_registration_check));




        // ===========  以下、モック向け設定　===============================
        this.siNoText = getView().findViewById(R.id.si_no_text);
        this.siNoText.setText(this.siNo);

        this.invoiceList = getView().findViewById(R.id.si_invoice_list);
        this.invoiceInfoList = new ArrayList<>();
        this.invoiceInfoList.add(new InvoiceInfo("19UT-0092", "CHI", "2022/1/8",  String.format("%,d", 123456), String.format("%,d", 1000)));
        IssueConfirmViewAdapter invoiceAdapter = new IssueConfirmViewAdapter(this.invoiceInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.invoiceList.setHasFixedSize(true);
        this.invoiceList.setLayoutManager(llm);
        this.invoiceList.setAdapter(invoiceAdapter);
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

        // 確定ボタン
        View.OnClickListener confirmClickListener = v -> confirmButton();
        ButtonInfo tagScanButtonInfo = new ButtonInfo(getString(R.string.confirm), confirmClickListener);
        mListener.setFooterButton(null, null, tagScanButtonInfo, null, null);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
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

    /**
     * 確定ボタン押下時処理
     */
    private void confirmButton() {

        // 全てのケースマークがスキャンされていない場合はエラー（メッセージ表示）★TODO：後で実装

        // メッセージダイアログ生成
        MessageDialogFragment confirmDialog = new MessageDialogFragment(getContext());
        // キャンセル押下
        confirmDialog.setNegativeButton(getString(R.string.cancel), v -> confirmDialog.dismiss());
        // OK押下
        confirmDialog.setPositiveButton(getString(R.string.ok), v-> {

            // 確定処理　★TODO：後で実装

            // 画面遷移
            confirmDialog.dismiss();
            mListener.popBackStack();
        });
        confirmDialog.setMessage("確定します。\nよろしいですか？");
        confirmDialog.show(getActivity().getSupportFragmentManager(), "MessageDialogFragment");
    }
}
