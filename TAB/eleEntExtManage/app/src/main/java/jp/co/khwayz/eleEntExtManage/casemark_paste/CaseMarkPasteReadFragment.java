package jp.co.khwayz.eleEntExtManage.casemark_paste;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.fragment.BaseFragment;

public class CaseMarkPasteReadFragment extends BaseFragment {

    // 画面オブジェクト
    private RecyclerView invoiceList;
    private List<CaseMarkPasteReadInfo> invoiceInfoList;
    private CaseMarkPasteReadRecyclerViewAdapter invoiceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_case_mark_paste_read, container, false);
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

        this.mainInterface.setScreenId("C02");

        // スキャン結果：Invoiceリスト
        this.invoiceList = getView().findViewById(R.id.invoice_list);
        this.invoiceInfoList = new ArrayList<>();
        // 明細構築
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "1"));
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "2"));
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "3"));
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "4"));
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "5"));
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "6"));
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "7"));
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "8"));
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "9"));
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "10"));
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "11"));
        this.invoiceInfoList.add(new CaseMarkPasteReadInfo("19UT-0092",  "12"));

        this.invoiceAdapter = new CaseMarkPasteReadRecyclerViewAdapter(this.invoiceInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.invoiceList.setHasFixedSize(true);
        this.invoiceList.setLayoutManager(llm);
        this.invoiceList.setAdapter(invoiceAdapter);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        this.mainInterface.setGroupsVisibility(true, true, true);
        this.mainInterface.setSubTitleText(getString(R.string.casemark_paste) + getString(R.string.casemark_read));

        // 次へボタン
        View.OnClickListener nextClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_caseMarkPasteReadFragment_to_caseMarkPasteDetailFragment);
            }
        };
        ButtonInfo printAllButtonInfo = new ButtonInfo(getString(R.string.next), nextClickListener);

        // フッターボタン登録
        this.mainInterface.setFooterButton(null, null, printAllButtonInfo, null, null);

        // 戻る
        View.OnClickListener backClickListener = v -> {
            this.mainInterface.popBackStack();
        };
        this.mainInterface.setBackButton(backClickListener);
    }
}
