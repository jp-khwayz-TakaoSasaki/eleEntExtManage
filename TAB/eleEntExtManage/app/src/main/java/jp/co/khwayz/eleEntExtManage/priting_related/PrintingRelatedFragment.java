package jp.co.khwayz.eleEntExtManage.priting_related;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.densowave.scannersdk.Const.CommConst;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.database.dao.InvoiceTempDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPrintingRelatedBinding;
import jp.co.khwayz.eleEntExtManage.http.task.get.PrintingRelatedFileDownload;
import jp.co.khwayz.eleEntExtManage.instr_cfm.InvoiceTempInfo;
import jp.co.khwayz.eleEntExtManage.pdf_print.CustomDocumentPrintAdapter;

/**
 * 印刷関連画面
 */
public class PrintingRelatedFragment extends BaseFragment
        implements AppendedFileRecyclerViewAdapter.OnItemClickListener, PrintingRelatedFileDownload.Callback<String> {
    public static final String ARG_INVOICE_NO = "ARG_INVOICE_NO";
    public static final String ARG_PREFIX = "screenPrefix";
    // DataBinding
    FragmentPrintingRelatedBinding mBinding;
    private String mInvoiceNo;
    private String mScreenPrefix = "P";
    private ArrayList<InvoiceTempInfo> mDataList;
    private AppendedFileRecyclerViewAdapter mListAdapter;
    private boolean mIsPrinting = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // InvoiceNoを取得
        mInvoiceNo = null;
        if (getArguments() != null) {
            mInvoiceNo = getArguments().getString(ARG_INVOICE_NO, null);
            mScreenPrefix = getArguments().getString(ARG_PREFIX, null);
        }
        // InvoiceNo or 遷移元パラメータ無しはエラー
        if (TextUtils.isEmpty(mInvoiceNo) || TextUtils.isEmpty(mScreenPrefix)) {
            throw new RuntimeException(TAG + "：nothing arguments.");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        if (mScreenPrefix.equals("P")) {
            mListener.setSubTitleText(getString(R.string.picking) + getString(R.string.title_printing_related));
        } else {
            mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.title_printing_related));
        }
        mListener.setScreenId(mScreenPrefix.equals("P") ? getString(R.string.screen_id_printing_related_p) : getString(R.string.screen_id_printing_related_k));

        // InvoiceNo表示
        mBinding.invoiceNoText.setText(mInvoiceNo);
        mDataList = new ArrayList<>();
        mListAdapter = new AppendedFileRecyclerViewAdapter(mDataList, this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mBinding.fileList.setHasFixedSize(true);
        mBinding.fileList.setLayoutManager(llm);
        mBinding.fileList.setAdapter(mListAdapter);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        // 戻るボタン設定
        View.OnClickListener backClickListener = v -> onBackButtonClick(mBinding.getRoot());
        mListener.setBackButton(backClickListener);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        try {
            // SQLiteからデータを取得
            InvoiceTempDao dao = new InvoiceTempDao();
            ArrayList<InvoiceTempInfo> result = dao.getIssueRegInvoiceList(Application.dbHelper.getReadableDatabase(), mInvoiceNo);
            mDataList.addAll(result);
        } catch (Exception e) {
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(R.string.const_err_message_E9000);
        } finally {
            mListAdapter.notifyDataSetChanged();
        }
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

    // region [ RecyclerView Row Button Listener ]
    /**
     * 印刷ボタン押下
     * @param info ボタンが押下された添付情報
     */
    @Override
    public void onPrintButtonClick(InvoiceTempInfo info) {
        // 印刷フラグOn
        mIsPrinting = true;
        // ファイルをDownloadする
        new PrintingRelatedFileDownload(this, mInvoiceNo, info).execute();
    }

    /**
     * ダウウンロードボタン押下
     * @param info ボタンが押下された添付情報
     */
    @Override
    public void onDownloadButtonClick(InvoiceTempInfo info) {
        // 印刷フラグOff
        mIsPrinting = false;
        // ファイルをDownloadする
        new PrintingRelatedFileDownload(this, mInvoiceNo, info).execute();
    }
    // endregion

    // region [ download task ]
    @Override
    public void onPreExecute(boolean showProgress) {
        mUtilListener.showProgressDialog(R.string.info_message_I0030);
    }

    @Override
    public void onTaskFinished(String response) {
        // ProgressDialogを閉じる
        mUtilListener.dismissProgressDialog();
        try {
            // 印刷フラグOnの場合
            if (mIsPrinting) {
                CustomDocumentPrintAdapter pda = new CustomDocumentPrintAdapter(response);
                // 印刷設定
                PrintManager printManager = (PrintManager) Application.mainActivity.getSystemService(Context.PRINT_SERVICE);
                PrintAttributes.Builder builder = new PrintAttributes.Builder();
                builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
                builder.setDuplexMode(PrintAttributes.DUPLEX_MODE_LONG_EDGE);
                String jobName = getString(R.string.app_name) + " Document";
                printManager.print(jobName, pda, builder.build());
            } else {
                // 完了メッセージ表示して終了
                mUtilListener.showSnackBarOnUiThread(R.string.info_message_I0007);
            }
        } catch (Exception e) {
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(R.string.const_err_message_E9000);
        }
    }

    @Override
    public void onError(int httpResponseStatusCode, int messageId) {
        // ProgressDialogを閉じる
        mUtilListener.dismissProgressDialog();
        // エラーメッセージを表示
        mUtilListener.showAlertDialog(messageId);
    }
    // endregion
}
