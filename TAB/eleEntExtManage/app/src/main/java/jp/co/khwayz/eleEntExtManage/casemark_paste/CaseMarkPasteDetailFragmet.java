package jp.co.khwayz.eleEntExtManage.casemark_paste;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.cargostatus.CargoStatusInfo;
import jp.co.khwayz.eleEntExtManage.fragment.BaseFragment;

public class CaseMarkPasteDetailFragmet extends BaseFragment {
    private RecyclerView invoiceList;
    private List<CargoStatusInfo> invoiceInfoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_case_mark_paste_detail, container, false);
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
        this.mainInterface.setScreenId("C03");
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        this.mainInterface.setGroupsVisibility(true, true, true);
        this.mainInterface.setSubTitleText(getString(R.string.casemark_paste) + getString(R.string.casemark_detail));

        // 次へボタン
        View.OnClickListener nextClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_caseMarkPasteDetailFragment_to_caseMarkPasteScanFragment);
            }
        };
        ButtonInfo printAllButtonInfo = new ButtonInfo(getString(R.string.next), nextClickListener);

        // フッターボタン登録
        this.mainInterface.setFooterButton(null, null, printAllButtonInfo, null, null);

        // 戻る
        View.OnClickListener backClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_caseMarkPasteDetailFragment_to_caseMarkPasteReadFragment);
            }
        };
        this.mainInterface.setBackButton(backClickListener);
    }
}
