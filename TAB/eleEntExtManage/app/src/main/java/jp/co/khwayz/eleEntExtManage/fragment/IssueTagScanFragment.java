package jp.co.khwayz.eleEntExtManage.fragment;

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
import jp.co.khwayz.eleEntExtManage.TagScanInfo;
import jp.co.khwayz.eleEntExtManage.adapter.TagInfoRecyclerViewAdapter;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.MessageDialogFragment;

public class IssueTagScanFragment extends BaseFragment {
    private RecyclerView tagScanList;
    private TextView siNoText;
    private List<TagScanInfo> tagScanInfoList;
    private String siNo = "1";
    private String invoiceNo = "19UT-0092";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_issue_tag_scan, container, false);
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
        this.siNoText = getView().findViewById(R.id.tag_scan_si_no_text);
        this.siNoText.setText(this.invoiceNo);

        this.tagScanList = getView().findViewById(R.id.tag_scan_list);
        this.tagScanInfoList = new ArrayList<>();
        this.tagScanInfoList.add(new TagScanInfo(1, "1004010001", "4502345563", "A", 7000, "KG", 7, "TN", "A-11-2", "1-2"));
        this.tagScanInfoList.add(new TagScanInfo(2, "1004017777", "4508912345", "B", 5, "CAN", 5, "CAN", "B-02-2", "1-2"));
        this.tagScanInfoList.add(new TagScanInfo(3, "1004013387", "4507112995", "C", 12, "CTN", 12, "TN", "B-05-1", "2-5"));
        this.tagScanInfoList.add(new TagScanInfo(4, "1005013308", "4507412545", "D", 13, "CAN", 13, "CAN", "B-05-2", "2-5"));
        this.tagScanInfoList.add(new TagScanInfo(5, "1004013387", "4507415365", "E", 14, "KG", 14, "TN", "B-05-3", "2-5"));
        this.tagScanInfoList.add(new TagScanInfo(6, "1004013387", "4507414276", "F", 15, "CTN", 15, "CAN", "B-05-4", "2-5"));
        this.tagScanInfoList.add(new TagScanInfo(7, "1004013387", "4507412543", "G", 16, "CTN", 16, "CTN", "B-05-5", "2-5"));
        TagInfoRecyclerViewAdapter issueTagScanAdapter = new TagInfoRecyclerViewAdapter(this.tagScanInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.tagScanList.setHasFixedSize(true);
        this.tagScanList.setLayoutManager(llm);
        this.tagScanList.setAdapter(issueTagScanAdapter);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        this.mainInterface.setGroupsVisibility(true, true, true);
        this.mainInterface.setSubTitleText(getString(R.string.tag_scan));
        View.OnClickListener confirmClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: ダイアログを表示
                final MessageDialogFragment messageDialogFragment = new MessageDialogFragment(getContext());
                String message = "";
                message += "SINo : " + siNo + "\n";
                message += "19UT-0092" + "\n";
                message += "13CZE-0060" + "\n";
                message += "19TPUSDPTY-0291" + "\n";
                message += "以上を確定させ、送信しますか？";
                messageDialogFragment.setMessage(message);
                messageDialogFragment.setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        messageDialogFragment.dismiss();
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_issueTagScanFragment_to_issueInvoiceFragment);
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
        ButtonInfo confirmButtonInfo = new ButtonInfo(getString(R.string.confirm), confirmClickListener);
        this.mainInterface.setFooterButton(confirmButtonInfo);
        View.OnClickListener backClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).popBackStack();
            }
        };
        this.mainInterface.setBackButton(backClickListener);
    }
}
