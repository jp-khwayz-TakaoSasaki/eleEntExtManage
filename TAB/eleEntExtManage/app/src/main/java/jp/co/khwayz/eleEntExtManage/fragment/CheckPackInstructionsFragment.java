package jp.co.khwayz.eleEntExtManage.fragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.densowave.scannersdk.Const.CommConst;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentCheckPackInstructionsBinding;
import jp.co.khwayz.eleEntExtManage.priting_related.PrintingRelatedFragment;

public class CheckPackInstructionsFragment extends BaseFragment {
    FragmentCheckPackInstructionsBinding mBinding;



    private TextView invoiceNoText;
    private String invoiceNo = "19UT-0092";
    private TextView instructions;
    private String screenPrefix = "P";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        screenPrefix = getArguments().getString("screenPrefix");
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
        if (screenPrefix.equals("P")) {
            // ピッキング機能からの場合
            mListener.setSubTitleText(getString(R.string.picking) + getString(R.string.common_check_instructions));
        } else if (screenPrefix.equals("K")) {
            // 梱包機能からの場合
            mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.common_check_instructions));
            // 梱包一覧指示確認チェック
            mListener.setSijikakuninCheckOn();
        }
        mListener.setScreenId(screenPrefix.equals("P") ?
                getString(R.string.screen_id_check_inst_p): getString(R.string.screen_id_check_inst_k));




        // ===========  以下、モック向け設定　===============================
        this.invoiceNoText = getView().findViewById(R.id.check_view_invoice_no);
        this.invoiceNoText.setText(invoiceNo);

        this.instructions = getView().findViewById(R.id.text_instructions);
        this.instructions.setMovementMethod(new ScrollingMovementMethod());
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

        // 印刷関連ボタン
        View.OnClickListener printClickListener = v -> printButton();
        ButtonInfo print = new ButtonInfo("印刷関連", printClickListener);

        // 出荷指示確認ボタン
        View.OnClickListener checkShippingInstructionsClickListener = v -> checkShippingInstructionsButton();
        ButtonInfo checkShippingInstructtions = new ButtonInfo("出荷指示確認", checkShippingInstructionsClickListener);

        mListener.setFooterButton(null, null, null, checkShippingInstructtions, print);
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

    private void printButton() {
        Bundle b = new Bundle();
        b.putString("screenPrefix", screenPrefix);
        mListener.replaceFragmentWithStack(new PrintingRelatedFragment(), TAG);
    }

    private void checkShippingInstructionsButton() {
        Bundle b = new Bundle();
        b.putString("screenPrefix", screenPrefix);
        mListener.replaceFragmentWithStack(new CheckShippingInstructionsFragment(), TAG);
    }
}
