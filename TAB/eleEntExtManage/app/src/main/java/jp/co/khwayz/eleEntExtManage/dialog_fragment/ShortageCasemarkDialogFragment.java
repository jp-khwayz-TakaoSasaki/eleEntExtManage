package jp.co.khwayz.eleEntExtManage.dialog_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.issueregist.IssueShortageCasemarkRecyclerViewAdapter;

public class ShortageCasemarkDialogFragment extends DialogFragment {

    private String mInvoiceNo;
    private List<String> mCaseMarkInfoList;

    public ShortageCasemarkDialogFragment(String invoiceNo, List<String> caseMarkList) {
        this.mCaseMarkInfoList = caseMarkList;
        this.mInvoiceNo = invoiceNo;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_fragment_shortage_casemark);

        // InvoiceNo表示
        TextView invoiceNoText = dialog.findViewById(R.id.tv_invoice_no_value);
        invoiceNoText.setText(mInvoiceNo);

        // 不足ケースマークList構築
        RecyclerView caseMarkList = dialog.findViewById(R.id.issue_shortage_casemark_list);

        IssueShortageCasemarkRecyclerViewAdapter caseMarkAdapter = new IssueShortageCasemarkRecyclerViewAdapter(this.mCaseMarkInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        caseMarkList.setHasFixedSize(true);
        caseMarkList.setLayoutManager(llm);
        caseMarkList.setAdapter(caseMarkAdapter);

        // Left Allow ボタン
        ImageButton backButton = dialog.findViewById(R.id.issue_shortage_casemark_negative_button);
        backButton.setOnClickListener(v -> dismiss());
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
