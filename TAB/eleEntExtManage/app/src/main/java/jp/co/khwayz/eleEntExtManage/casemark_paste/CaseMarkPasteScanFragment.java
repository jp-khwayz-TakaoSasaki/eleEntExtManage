package jp.co.khwayz.eleEntExtManage.casemark_paste;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.MessageDialogFragment;
import jp.co.khwayz.eleEntExtManage.fragment.BaseFragment;

public class CaseMarkPasteScanFragment extends BaseFragment implements CaseMarkPasteScanRecyclerViewAdapter.OnItemClickListener {
    private TextView invoiceNoText;
    private RecyclerView tagScanList;
    private List<CaseMarkPasteScanInfo> tagScanInfoList;
    CaseMarkPasteScanRecyclerViewAdapter caseMarkPasteScanAdapter;
    private String invoiceNo = "19UT-0092";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_case_mark_paste_scan, container, false);
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
        this.mainInterface.setScreenId("C04");
        this.invoiceNoText = getView().findViewById(R.id.casemark_paste_scan_view_invoice_no);
        this.invoiceNoText.setText(this.invoiceNo);

        this.tagScanList = getView().findViewById(R.id.invoice_tag_scan_list);
        this.tagScanInfoList = new ArrayList<>();
        this.tagScanInfoList.add(new CaseMarkPasteScanInfo(1, "1", "4502345563", "001", "4502345563", "KE-140", "MI PRESS CA29(200L)press", "30.000", "30.000", "KG", "CTN"));
        this.tagScanInfoList.add(new CaseMarkPasteScanInfo(2, "2", "4502345563", "001", "4502345563", "KE-140", "MI PRESS CA29(200L)press", "30.000", "30.000", "KG", "CTN"));
        this.tagScanInfoList.add(new CaseMarkPasteScanInfo(3, "3", "4502345563", "001", "4502345563", "KE-140", "MI PRESS CA29(200L)press", "30.000", "30.000", "KG", "CTN"));
        this.tagScanInfoList.add(new CaseMarkPasteScanInfo(4, "4", "4502345563", "001", "4502345563", "KE-140", "MI PRESS CA29(200L)press", "30.000", "30.000", "KG", "CTN"));
        this.tagScanInfoList.add(new CaseMarkPasteScanInfo(5, "5", "4502345563", "001", "4502345563", "KE-140", "MI PRESS CA29(200L)press", "30.000", "30.000", "KG", "CTN"));
        this.tagScanInfoList.add(new CaseMarkPasteScanInfo(6, "6", "4502345563", "001", "4502345563", "KE-140", "MI PRESS CA29(200L)press", "30.000", "30.000", "KG", "CTN"));
        this.tagScanInfoList.add(new CaseMarkPasteScanInfo(7, "7", "4502345563", "001", "4502345563", "KE-140", "MI PRESS CA29(200L)press", "30.000", "30.000", "KG", "CTN"));
        this.tagScanInfoList.add(new CaseMarkPasteScanInfo(8, "8", "4502345563", "001", "4502345563", "KE-140", "MI PRESS CA29(200L)press", "30.000", "30.000", "KG", "CTN"));
        this.tagScanInfoList.add(new CaseMarkPasteScanInfo(9, "9", "4502345563", "001", "4502345563", "KE-140", "MI PRESS CA29(200L)press", "30.000", "30.000", "KG", "CTN"));
        this.tagScanInfoList.add(new CaseMarkPasteScanInfo(10, "10", "4502345563", "001", "4502345563", "KE-140", "MI PRESS CA29(200L)press", "30.000", "30.000", "KG", "CTN"));

        this.caseMarkPasteScanAdapter = new CaseMarkPasteScanRecyclerViewAdapter(this.tagScanInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.tagScanList.setHasFixedSize(true);
        this.tagScanList.setLayoutManager(llm);
        this.tagScanList.setAdapter(caseMarkPasteScanAdapter);
        caseMarkPasteScanAdapter.setOnItemClickListener(this);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        this.mainInterface.setGroupsVisibility(true, true, true);
        this.mainInterface.setSubTitleText(getString(R.string.casemark_paste) + getString(R.string.casemark_scan));

        // 貼付完了ボタン
        View.OnClickListener finishClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: ダイアログを表示
                final MessageDialogFragment messageDialogFragment = new MessageDialogFragment(getContext());
                String message = "貼付を完了します。\nよろしいですか？";
                messageDialogFragment.setMessage(message);
                messageDialogFragment.setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        messageDialogFragment.dismiss();
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_caseMarkPasteScan_to_caseMarkPasteReadFragment);
                    }
                });
                messageDialogFragment.setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        messageDialogFragment.dismiss();
                    }
                });
                messageDialogFragment.show(getActivity().getSupportFragmentManager(), "MessageDialogFragment");
            }
        };
        ButtonInfo finishButtonInfo = new ButtonInfo(getString(R.string.button_label_finish), finishClickListener);

        // QRボタン
        View.OnClickListener qrClickListener = v -> qrButton();
        ButtonInfo qrButtonInfo = new ButtonInfo("QR", qrClickListener);

        // フッターボタン登録
        this.mainInterface.setFooterButton(null, null, finishButtonInfo, null, qrButtonInfo);

        // 戻るボタン
        View.OnClickListener backClickListener = view -> {
            MessageDialogFragment dialog = new MessageDialogFragment(getContext());
            dialog.setMessage(getString(R.string.common_casemark_paste_scandata_del_message));
            dialog.setPositiveButton(getString(R.string.ok), v -> {
                dialog.dismiss();
                NavHostFragment.findNavController(getParentFragment()).popBackStack();
            });
            dialog.setNegativeButton(getString(R.string.cancel), v -> dialog.dismiss());
            dialog.show(getActivity().getSupportFragmentManager(), "MessageDialogFragment");
        };
        this.mainInterface.setBackButton(backClickListener);
    }

    @Override
    public void onItemClick(CaseMarkPasteScanViewHolder holder) {
        caseMarkPasteScanAdapter.setSelectedPosition(holder.getAdapterPosition());
        caseMarkPasteScanAdapter.notifyDataSetChanged();
    }

    /**
     *  QR/RFIDボタン押下時処理
     * */
    private void qrButton() {
        this.mainInterface.changeFooterButtonLabel(5);
    }
}
