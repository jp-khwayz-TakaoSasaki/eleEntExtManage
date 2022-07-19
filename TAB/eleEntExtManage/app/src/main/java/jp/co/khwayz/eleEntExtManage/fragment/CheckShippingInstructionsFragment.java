package jp.co.khwayz.eleEntExtManage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.densowave.scannersdk.Const.CommConst;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.ShippingInstructionsInfo;
import jp.co.khwayz.eleEntExtManage.adapter.CheckShippingInstructionsRecyclerViewAdapter;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentCheckShippingInstructionsBinding;

public class CheckShippingInstructionsFragment extends BaseFragment {
    // DataBinding
    FragmentCheckShippingInstructionsBinding mBinding;

    // 画面オブジェクト
    private RecyclerView invoiceList;
    private List<ShippingInstructionsInfo> invoiceInfoList;
    private CheckShippingInstructionsRecyclerViewAdapter invoiceAdapter;
    private String screenPrefix = "P";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        screenPrefix = getArguments().getString("screenPrefix");
        mBinding = FragmentCheckShippingInstructionsBinding.inflate(inflater, container, false);
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

        // タイトル設定
        mListener.setGroupsVisibility(true, true, true);
        if (screenPrefix.equals("P")) {
            // ピッキング機能からの場合
            mListener.setSubTitleText(getString(R.string.picking) + getString(R.string.common_check_shipping_instructions));
        }
        if (screenPrefix.equals("K")) {
            // 梱包機能からの場合
            mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.common_check_shipping_instructions));
        }
        mListener.setScreenId(screenPrefix.equals("P") ?
                getString(R.string.screen_id_check_shipping_inst_p) : getString(R.string.screen_id_check_shipping_inst_k));





        // ===========  以下、モック向け設定　===============================
        // スキャン結果：Invoiceリスト
        this.invoiceList = getView().findViewById(R.id.invoice_list);
        this.invoiceInfoList = new ArrayList<>();
        // 明細構築
        this.invoiceInfoList.add(new ShippingInstructionsInfo("1111",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("2222",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("3333",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("4444",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("5555",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("6666",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("7777",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("8888",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("9999",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("1234",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("2345",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("3456",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("4567",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("5678",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("6789",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("7890",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));
        this.invoiceInfoList.add(new ShippingInstructionsInfo("8900",  "1234567890", "明細ごとの指示内容。明細ごとの指示内容。"));

        this.invoiceAdapter = new CheckShippingInstructionsRecyclerViewAdapter(this.invoiceInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.invoiceList.setHasFixedSize(true);
        this.invoiceList.setLayoutManager(llm);
        this.invoiceList.setAdapter(invoiceAdapter);
    }

    @Override
    public void eventSetting() {
        /*************************************/
        // フッターボタン
        /*************************************/
        // 戻るボタン設定
        View.OnClickListener backClickListener = v -> {
            onBackButtonClick(mBinding.getRoot());
        };
        mListener.setBackButton(backClickListener);
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
    }}
