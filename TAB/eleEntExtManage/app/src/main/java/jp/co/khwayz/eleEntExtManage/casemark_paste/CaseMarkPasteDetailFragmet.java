package jp.co.khwayz.eleEntExtManage.casemark_paste;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.densowave.scannersdk.Const.CommConst;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.models.CaseMarkDetailInfo;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentCaseMarkPasteDetailBinding;

public class CaseMarkPasteDetailFragmet extends BaseFragment {
    // DataBinding
    FragmentCaseMarkPasteDetailBinding mBinding;

    // ARGS
    private static final String ARGS_DISPDATA = "display_data";
    private static final String ARGS_CASEMARK_LIST = "casemark_list";
    private static CaseMarkDetailInfo mDisplayData;
    private static ArrayList<CaseMarkPasteReadInfo> mCaseMarkList;

    public static CaseMarkPasteDetailFragmet newInstance(CaseMarkDetailInfo displayData
            , ArrayList<CaseMarkPasteReadInfo> caseMarkList){
        CaseMarkPasteDetailFragmet fragment = new CaseMarkPasteDetailFragmet();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_DISPDATA, displayData);
        args.putParcelableArrayList(ARGS_CASEMARK_LIST, caseMarkList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentCaseMarkPasteDetailBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mDisplayData = args.getParcelable(ARGS_DISPDATA);
        this.mCaseMarkList = args.getParcelableArrayList(ARGS_CASEMARK_LIST);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
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
        mListener.setSubTitleText(getString(R.string.casemark_paste) + getString(R.string.casemark_detail));
        mListener.setScreenId(getString(R.string.screen_id_casemark_paste_detail));

        // 画面構築
        mBinding.tvCasemarkPasteCasemark01.setText(mDisplayData.getCASEMARK1());
        mBinding.tvCasemarkPasteCasemark02.setText(mDisplayData.getCASEMARK2());
        mBinding.tvCasemarkPasteCasemark03.setText(mDisplayData.getCASEMARK3());
        mBinding.tvCasemarkPasteCasemark04.setText(mDisplayData.getCASEMARK4());
        mBinding.tvCasemarkPasteCasemark05.setText(mDisplayData.getCASEMARK5());
        mBinding.tvCasemarkPasteCasemark06.setText(mDisplayData.getCASEMARK6());
        mBinding.tvCasemarkPasteCasemark07.setText(mDisplayData.getCASEMARK7());
        mBinding.tvCasemarkPasteCasemark08.setText(mDisplayData.getCASEMARK8());
        mBinding.tvCasemarkPasteCasemark09.setText(mDisplayData.getCASEMARK9());
        mBinding.tvCasemarkPasteCasemark10.setText(mDisplayData.getCASEMARK10());
        mBinding.tvCasemarkPasteCasemark11.setText(mDisplayData.getCASEMARK11());
        mBinding.tvCasemarkPasteCasemark12.setText(mDisplayData.getCASEMARK12());
        mBinding.tvCasemarkPastePackingQuantityValue.setText(String.valueOf(mDisplayData.getKonpoSu()));
        mBinding.tvCasemarkPasteCSlashNoValue.setText(mDisplayData.getHyokiCsNumber());
        mBinding.tvCasemarkPasteLwhValue.setText(
                mDisplayData.getOuterLength() + " * " + mDisplayData.getOuterWidth() + " * " + mDisplayData.getOuterHeight() + " cm");
        mBinding.tvCasemarkPasteNwValue.setText(String.valueOf(mDisplayData.getNetWeight()));
        mBinding.tvCasemarkPasteGwValue.setText(String.valueOf(mDisplayData.getGrossWeight()));
        mBinding.tvCasemarkPasteRemarksValue.setText(mDisplayData.getBiko());
        mBinding.tvCasemarkPasteInvoiceNoValue.setText(mCaseMarkList.get(0).getInvoiceNo());
    }

    @Override
    public void eventSetting() {
        super.eventSetting();

        /*************************************/
        // フッターボタン
        /*************************************/
        // 戻るボタン設定
        View.OnClickListener backClickListener = view -> {
            onBackButtonClick(mBinding.getRoot());
        };
        mListener.setBackButton(backClickListener);

        // 次へボタン
        View.OnClickListener nextclickListener = v -> onNextButtonClick();
        ButtonInfo nextButtonInfo = new ButtonInfo(getString(R.string.next), nextclickListener);

        // フッターボタン登録
        mListener.setFooterButton(null, null, nextButtonInfo, null, null);
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

    private void onNextButtonClick(){
        // 簿外リスト初期化
        Application.scanOffBooklList=null;
        // 次画面遷移
        mListener.replaceFragmentWithStack(CaseMarkPasteScanFragment.newInstance(
                mDisplayData, mCaseMarkList ), TAG);
    }
}
