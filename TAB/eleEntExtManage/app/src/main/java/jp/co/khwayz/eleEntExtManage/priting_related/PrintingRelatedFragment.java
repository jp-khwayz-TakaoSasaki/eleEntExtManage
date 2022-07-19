package jp.co.khwayz.eleEntExtManage.priting_related;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.densowave.scannersdk.Const.CommConst;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPrintingRelatedBinding;

public class PrintingRelatedFragment extends BaseFragment {
    // DataBinding
    FragmentPrintingRelatedBinding mBinding;



    private TextView invoiceNoText;
    private List<String> appendedFiles;
    private String invoiceNo = "19UT-0092";
    private AppendedFileRecyclerViewAdapter appendedFilesAdapter;
    private RecyclerView appendedFilesList;
    private String screenPrefix = "P";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        screenPrefix = getArguments().getString("screenPrefix");
        mBinding = FragmentPrintingRelatedBinding.inflate(inflater, container, false);
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
            mListener.setSubTitleText(getString(R.string.picking) + getString(R.string.title_printing_related));
        }
        if (screenPrefix.equals("K")) {
            mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.title_printing_related));
        }
        mListener.setScreenId(screenPrefix.equals("P") ?
                getString(R.string.screen_id_printing_related_p) : getString(R.string.screen_id_printing_related_k));




        // ===========  以下、モック向け設定　===============================
        this.invoiceNoText = getView().findViewById(R.id.printing_invoice_no);
        this.invoiceNoText.setText(invoiceNo);

        // Invoice番号毎指示添付リスト
        this.appendedFilesList = getView().findViewById(R.id.appended_files);

        this.appendedFiles = new ArrayList<>();
        this.appendedFiles.add("指示添付①.pdf");
        this.appendedFiles.add("指示添付②.pdf");
        this.appendedFiles.add("指示添付③.pdf");
        this.appendedFilesAdapter = new AppendedFileRecyclerViewAdapter(this.appendedFiles);
        this.appendedFilesAdapter.setPrintManager((PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE));
        this.appendedFilesAdapter.setJobName(getString(R.string.app_name) + " Document");
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.appendedFilesList.setHasFixedSize(true);
        this.appendedFilesList.setLayoutManager(llm);
        this.appendedFilesList.setAdapter(this.appendedFilesAdapter);
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
}
