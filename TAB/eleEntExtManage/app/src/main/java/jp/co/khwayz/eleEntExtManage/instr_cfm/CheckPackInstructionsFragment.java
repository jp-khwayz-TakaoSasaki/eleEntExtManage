package jp.co.khwayz.eleEntExtManage.instr_cfm;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.densowave.scannersdk.Const.CommConst;

import org.jetbrains.annotations.NotNull;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentCheckPackInstructionsBinding;
import jp.co.khwayz.eleEntExtManage.http.response.PackInstructionsResponse;
import jp.co.khwayz.eleEntExtManage.http.task.get.GetPackInstructionsTask;
import jp.co.khwayz.eleEntExtManage.priting_related.PrintingRelatedFragment;

/**
 * 梱包指示確認画面
 */
public class CheckPackInstructionsFragment extends BaseFragment implements GetPackInstructionsTask.Callback<PackInstructionsResponse> {
    public static final String ARG_INVOICE_NO = "ARG_INVOICE_NO";
    public static final String ARG_PREFIX = "screenPrefix";

    FragmentCheckPackInstructionsBinding mBinding;
    private String mInvoiceNo;
    private String mScreenPrefix = "P";

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
        mBinding = FragmentCheckPackInstructionsBinding.inflate(inflater, container, false);
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
        mListener.setReaderConnectButtonVisibility(View.INVISIBLE);
        mListener.setBatteryStateImageVisibility(View.VISIBLE);

        // タイトル
        if (mScreenPrefix.equals("P")) {
            // ピッキング機能からの場合
            mListener.setSubTitleText(getString(R.string.picking) + getString(R.string.common_check_instructions));
        } else if (mScreenPrefix.equals("K")) {
            // 梱包機能からの場合
            mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.common_check_instructions));
            // 梱包一覧指示確認チェック
            mListener.setSijikakuninCheckOn();
        }
        mListener.setScreenId(mScreenPrefix.equals("P") ?  getString(R.string.screen_id_check_inst_p): getString(R.string.screen_id_check_inst_k));

        // InvoiceNo表示
        mBinding.invoiceNoText.setText(mInvoiceNo);
        // 梱包指示クリア
        mBinding.instructionsText.setText("");
        mBinding.instructionsText.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        // 戻るボタン設定
        View.OnClickListener backClickListener = v -> onBackButtonClick(mBinding.getRoot());
        mListener.setBackButton(backClickListener);
        // 印刷関連ボタン
        View.OnClickListener printClickListener = v -> printButtonClick();
        ButtonInfo print = new ButtonInfo("印刷関連", printClickListener);
        // 出荷指示確認ボタン
        View.OnClickListener checkShippingInstructionsClickListener = v -> checkShippingInstructionsButtonClick();
        ButtonInfo checkShippingInstructions = new ButtonInfo("出荷指示確認", checkShippingInstructionsClickListener);
        // フッターボタン設定
        mListener.setFooterButton(null, null, null, checkShippingInstructions, print);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        // データ取得
        new GetPackInstructionsTask(this, mInvoiceNo).execute();
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
     * 印刷関連ボタン押下
     */
    private void printButtonClick() {
        Bundle arg = new Bundle();
        arg.putString(ARG_INVOICE_NO, mInvoiceNo);
        arg.putString(ARG_PREFIX, mScreenPrefix);
        PrintingRelatedFragment nextFragment = new PrintingRelatedFragment();
        nextFragment.setArguments(arg);
        mListener.replaceFragmentWithStack(nextFragment, TAG);
    }

    /**
     * 出荷指示確認ボタン押下
     */
    private void checkShippingInstructionsButtonClick() {
        Bundle arg = new Bundle();
        arg.putString(ARG_INVOICE_NO, mInvoiceNo);
        arg.putString(ARG_PREFIX, mScreenPrefix);
        CheckShippingInstructionsFragment nextFragment = new CheckShippingInstructionsFragment();
        nextFragment.setArguments(arg);
        mListener.replaceFragmentWithStack(nextFragment, TAG);
    }

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
    public void onTaskFinished(PackInstructionsResponse response) {
        // ProgressDialogを閉じる
        mUtilListener.dismissProgressDialog();
        // 梱包指示を表示
        mBinding.instructionsText.setText(response.getData().getPackingInstructions());
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
