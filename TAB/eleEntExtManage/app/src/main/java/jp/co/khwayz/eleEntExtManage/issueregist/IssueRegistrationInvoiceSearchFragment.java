package jp.co.khwayz.eleEntExtManage.issueregist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.densowave.scannersdk.Const.CommConst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.Calender;
import jp.co.khwayz.eleEntExtManage.InvoiceInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentIssueRegistrationInvoiceSearchBinding;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.SortDialogFragment;

public class IssueRegistrationInvoiceSearchFragment extends BaseFragment
        implements IssueRegistrationInvoiceSearchRecyclerViewAdapter.OnItemClickListener {
    // DataBinding
    FragmentIssueRegistrationInvoiceSearchBinding mBinding;

    // 画面オブジェクト
    private TextView invoiceNoText;
    private Spinner destinationSpinner;
    private TextView shipDateText;
    private ImageButton shipDateButton;
    private RecyclerView invoiceList;
    private List<InvoiceInfo> invoiceInfoList;
    private IssueRegistrationInvoiceSearchRecyclerViewAdapter invoiceAdapter;
    private TextView detailCount;
    private Button invoiceSearchButton;
    private Button sortButton;
    // モック用
    private final String[] destinationSpinnerItems = {"", "CHI", "BFV", "HKG", "HUI", "TKG"};
    private final String[] sortKeys = {"Invoice番号", "仕向地", "出荷日"};
    private String invoiceNo = "19UT-0092";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentIssueRegistrationInvoiceSearchBinding.inflate(inflater, container, false);
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
        mListener.setSubTitleText(getString(R.string.issue_registration) + getString(R.string.issue_registration_invoice_search));
        mListener.setScreenId(getString(R.string.screen_id_registration_search));



        // ===========  以下、モック向け設定　===============================
        // Invoice番号
        this.invoiceNoText = getView().findViewById(R.id.et_issue_registration_invoice_no);
        this.invoiceNoText.setText(invoiceNo);

        // 仕向け地
        this.destinationSpinner = getView().findViewById(R.id.sp_issue_registration_destination);
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, destinationSpinnerItems);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.destinationSpinner.setAdapter(destinationAdapter);

        // 出荷日
        this.shipDateText = getView().findViewById(R.id.tv_issue_registration_ship_date);
        this.shipDateButton = getView().findViewById(R.id.bt_issue_registration_ship_date);

        // 検索結果：Invoiceリスト
        this.invoiceList = getView().findViewById(R.id.invoice_list);
        this.invoiceInfoList = new ArrayList<>();

        // 検索結果
        this.detailCount = getView().findViewById(R.id.detail_count);

        // 検索ボタン
        this.invoiceSearchButton = getView().findViewById(R.id.bt_invoice_search);

        // ソートボタン
        this.sortButton = getView().findViewById(R.id.bt_sort);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        // 出荷日
        this.shipDateButton.setOnClickListener(v -> Calender.show(getActivity(), this.shipDateText));
        // 検索ボタン
        this.invoiceSearchButton.setOnClickListener(v -> invoiceSearch());
        // ソートボタン
        this.sortButton.setOnClickListener(v -> detailSorter());

        /*************************************/
        // フッターボタン
        /*************************************/
        // 戻るボタン設定
        View.OnClickListener backClickListener = v -> {
            onBackButtonClick(mBinding.getRoot());
        };
        mListener.setBackButton(backClickListener);

        // データ受信
        View.OnClickListener dataReceiveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.replaceFragmentWithStack(new IssueRegistrationCheckFragment(), TAG);
            }
        };
        ButtonInfo dataReceiveButtonInfo = new ButtonInfo(getString(R.string.data_receive)
                , dataReceiveClickListener);
        mListener.setFooterButton(null,null,null,null,dataReceiveButtonInfo);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
    }

    @Override
    public void onItemClick(IssueRegistrationInvoiceSearchViewHolder holder) {
        invoiceAdapter.setSelectedPosition(holder.getAdapterPosition());
        invoiceAdapter.notifyDataSetChanged();
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

    public void invoiceSearch() {

          // 明細構築
        this.invoiceInfoList.clear();
        this.invoiceInfoList.add(new InvoiceInfo("19UT-0092",  "CHI", "2022/1/8" ));
        this.invoiceInfoList.add(new InvoiceInfo("13CZE-0060", "CHI", "2022/1/26"));
        this.invoiceInfoList.add(new InvoiceInfo("14AZ-1060",  "BKK", "2022/4/1" ));
        this.invoiceInfoList.add(new InvoiceInfo("14AZ-1060",  "CHI", "2022/4/1" ));
        this.invoiceInfoList.add(new InvoiceInfo("19AZ-1060",  "BKK", "2022/4/1" ));
        this.invoiceInfoList.add(new InvoiceInfo("14AB-1460",  "HKG", "2022/4/1" ));
        this.invoiceInfoList.add(new InvoiceInfo("12AZ-0061",  "BKK", "2022/4/1" ));
        this.invoiceInfoList.add(new InvoiceInfo("13AZ-1560",  "BKK", "2022/1/8" ));
        this.invoiceInfoList.add(new InvoiceInfo("19UT-0093",  "CHI", "2022/4/1" ));
        this.invoiceInfoList.add(new InvoiceInfo("19UT-0095",  "CHI", "2022/1/8" ));

        this.invoiceAdapter = new IssueRegistrationInvoiceSearchRecyclerViewAdapter(this.invoiceInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.invoiceList.setHasFixedSize(true);
        this.invoiceList.setLayoutManager(llm);
        this.invoiceList.setAdapter(invoiceAdapter);
        invoiceAdapter.setOnItemClickListener(this);

        // 結果数
        this.detailCount.setText(String.valueOf(invoiceAdapter.getItemCount()));
    }

    private void detailSorter() {
        ArrayList<String> fields = new ArrayList<>(Arrays.asList(sortKeys));
        final SortDialogFragment dialog = new SortDialogFragment(getContext(), fields);
        dialog.show(getActivity().getSupportFragmentManager(), "SortDialogFragment");
    }
}
