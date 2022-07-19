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

    // 画面オブジェクト
    private TextView invoiceNoText;
    private RecyclerView casemarkList;
    private List<String> casemarkInfoList;
    private IssueShortageCasemarkRecyclerViewAdapter casemarkAdapter;
    private ImageButton backButton;

    public ShortageCasemarkDialogFragment(List<String> casemarkList) {
        this.casemarkInfoList = casemarkList;
    }

    @Override
    public void onStart() {
        super.onStart();
//        Dialog dialog = getDialog();
//        DisplayMetrics metrics = getResources().getDisplayMetrics();

        // TODO:サイズについては要調整
//        int width = (int)(metrics.widthPixels * 0.417);
//        int height = (int)(metrics.heightPixels * 0.38);
//
//        dialog.getWindow().setLayout(width, height);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_fragment_shortage_casemark);

        // 不足ケースマークList構築
        this.casemarkList = dialog.findViewById(R.id.issue_shortage_casemark_list);

        this.casemarkAdapter = new IssueShortageCasemarkRecyclerViewAdapter(this.casemarkInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.casemarkList.setHasFixedSize(true);
        this.casemarkList.setLayoutManager(llm);
        this.casemarkList.setAdapter(casemarkAdapter);

        // Left Allow ボタン
        this.backButton = dialog.findViewById(R.id.issue_shortage_casemark_negative_button);
        this.backButton.setOnClickListener(v -> dismiss());
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
