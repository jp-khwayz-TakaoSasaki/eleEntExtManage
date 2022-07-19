package jp.co.khwayz.eleEntExtManage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.Calender;
import jp.co.khwayz.eleEntExtManage.InvoiceInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.InvoiceRecyclerViewAdapter;

public class IssueInvoiceFragment extends BaseFragment {
    private Spinner destinationSpinner;
    private Spinner packingInstructionSpinner;
    private TextView invoiceNoText;
    private RecyclerView invoiceList;
    private TextView deadlineText;
    private TextView packingDate;
    private ImageButton deadlineButton;
    private ImageButton packingDateButton;
    private List<InvoiceInfo> invoiceInfoList;
    private String invoiceNo = "19UT-0092";
    private final String[] destinationSpinnerItems = {"仕向け地", "北海道", "沖縄", "インド"};
    private final String[] packingInstructionSpinnerItems = {"梱包指示", "梱包", "指示", "パッキング"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_issue_invoice, container, false);
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

        this.mainInterface.setScreenId("S01");

        this.invoiceNoText = getView().findViewById(R.id.invoice_no_text);
        this.invoiceNoText.setText(invoiceNo);

        this.destinationSpinner = getView().findViewById(R.id.destination_spinner);
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, destinationSpinnerItems);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.destinationSpinner.setAdapter(destinationAdapter);

//        this.packingInstructionSpinner = getView().findViewById(R.id.packing_instruction_spinner);
//        ArrayAdapter<String> packingInstructionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, packingInstructionSpinnerItems);
//        packingInstructionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        this.packingInstructionSpinner.setAdapter(packingInstructionAdapter);

        this.deadlineText = getView().findViewById(R.id.deadline);
        //this.packingDate = getView().findViewById(R.id.packingDate);
        this.deadlineButton = getView().findViewById(R.id.deadlineButton);
        //this.packingDateButton = getView().findViewById(R.id.packingDateButton);
        this.invoiceList = getView().findViewById(R.id.invoice_list);
        this.invoiceInfoList = new ArrayList<>();
        this.invoiceInfoList.add(new InvoiceInfo(1, "19UT-0092", "2021/12/8", "CHI", "バレタイズ", "2021/11/22"));
        this.invoiceInfoList.add(new InvoiceInfo(1, "13CZE-0060", "2021/12/16", "CHI", "ラベル貼り", "2021/12/1"));
        this.invoiceInfoList.add(new InvoiceInfo(1, "19TPUSDPTY-0291", "2021/12/21", "CHI", "なし", "2021/12/8"));
        InvoiceRecyclerViewAdapter invoiceAdapter = new InvoiceRecyclerViewAdapter(this.invoiceInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.invoiceList.setHasFixedSize(true);
        this.invoiceList.setLayoutManager(llm);
        this.invoiceList.setAdapter(invoiceAdapter);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        this.deadlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calender.show(getActivity(), deadlineText);
            }
        });
//        this.packingDateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calender.show(getActivity(), packingDate);
//            }
//        });

    }


    @Override
    public void mainSetting() {
        super.mainSetting();
        this.mainInterface.setGroupsVisibility(true, true, true);
        this.mainInterface.setSubTitleText(getString(R.string.issue_mask));
        View.OnClickListener dataReceiveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_issueInvoiceFragment_to_issueRegistrationCheck);
            }
        };
        ButtonInfo dataReceiveButtonInfo = new ButtonInfo(getString(R.string.data_receive), dataReceiveClickListener);
        this.mainInterface.setFooterButton(dataReceiveButtonInfo);
        View.OnClickListener backClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_issueInvoiceFragment_to_issueMenuFragment);
            }
        };
        this.mainInterface.setBackButton(backClickListener);
    }
}
