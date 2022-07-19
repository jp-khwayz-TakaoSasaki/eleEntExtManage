package jp.co.khwayz.eleEntExtManage.packing;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.densowave.scannersdk.Const.CommConst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.CategorySpinnerAdapter;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.filter.AlphaNumericSingInputFilter;
import jp.co.khwayz.eleEntExtManage.common.filter.DecimalDigitsInputFilter;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.common.models.IssueTagInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoShijiKeyInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPackingFinalPackingBinding;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.MessageDialogFragment;
import jp.co.khwayz.eleEntExtManage.fragment.CheckPackInstructionsFragment;

public class PackingFinalPackingFragment extends BaseFragment {
    // DataBinding
    FragmentPackingFinalPackingBinding mBinding;

    // ARGS
    private static final String ARGS_INVOICE_NO = "invoice_no";
    private static final String ARGS_LINE_NO_LIST = "lineNoList";
    private static String mInvoiceNo;
    private static ArrayList<SyukkoShijiKeyInfo> mlineNoList;

    // Adapter
    private CategorySpinnerAdapter mWorkDescriptionSpinner_1;
    private CategorySpinnerAdapter mWorkDescriptionSpinner_2;
    private CategorySpinnerAdapter mWorkDescriptionSpinner_3;
    private CategorySpinnerAdapter mWorkDescriptionSpinner_4;
    private CategorySpinnerAdapter mPackingNameSpinner;

    // List
    List<CategoryInfo> mWorkDescriptionDataSet;
    List<CategoryInfo> mPackingNameDataSet;
    ArrayList<IssueTagInfo> mScanList = new ArrayList<>();

    // ケースマーク番号開始値
    private int mStartCaseMarkNum;

    /**
     * コンストラクタ
     * @param invoiceNo
     * @param lineNno
     * @return
     */
    public static PackingFinalPackingFragment newInstance(String invoiceNo, ArrayList<SyukkoShijiKeyInfo> lineNno){
        PackingFinalPackingFragment fragment = new PackingFinalPackingFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_INVOICE_NO, invoiceNo);
        args.putParcelableArrayList(ARGS_LINE_NO_LIST, lineNno);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentPackingFinalPackingBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mInvoiceNo = args.getString(ARGS_INVOICE_NO);
        this.mlineNoList = args.getParcelableArrayList(ARGS_LINE_NO_LIST);
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
        mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.final_packing_input));
        mListener.setScreenId(getString(R.string.screen_id_packing_final));
        mListener.setSubHeaderExplanationText(getString(R.string.final_packing_explanation));

        // ****************************
        // スピナー選択肢生成
        // ****************************
        // 作業内容
        mWorkDescriptionDataSet = mUtilListener.getCategoryList(Constants.KBN_EKONPOMEISAI);;
        CategoryInfo topValue = new CategoryInfo("","");
        mWorkDescriptionDataSet.add(0, topValue);
        // 梱包名
        mPackingNameDataSet = mUtilListener.getCategoryList(Constants.KBN_ESAISHUKONPO);;
        mPackingNameDataSet.add(0, topValue);

        // ****************************
        // スピナーへセット
        // ****************************
        // 作業内容①梱包
        mWorkDescriptionSpinner_1 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.finalPackingInputSpinner1.setAdapter(mWorkDescriptionSpinner_1);

        // 作業内容②梱包
        mWorkDescriptionSpinner_2 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.finalPackingInputSpinner2.setAdapter(mWorkDescriptionSpinner_2);

        // 作業内容③梱包
        mWorkDescriptionSpinner_3 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.finalPackingInputSpinner3.setAdapter(mWorkDescriptionSpinner_3);

        // 作業内容④梱包
        mWorkDescriptionSpinner_4 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.finalPackingInputSpinner4.setAdapter(mWorkDescriptionSpinner_4);

        // 梱包名
        mPackingNameSpinner = new CategorySpinnerAdapter(getContext(), mPackingNameDataSet);
        mBinding.finalPackingInputSpinnerPackingName.setAdapter(mPackingNameSpinner);
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

        // 指示内容確認ボタン
        View.OnClickListener instructionsClickListener = v -> instructionsButton();
        ButtonInfo instructions = new ButtonInfo("指示内容確認", instructionsClickListener);

        // 確定ボタン
        ButtonInfo confirmButtonInfo = new ButtonInfo(getString(R.string.confirm), v -> confirmButton());
        mListener.setFooterButton(null, instructions, null, confirmButtonInfo, null);

        // 梱包数入力（ケースマーク番号算出起点）
        mBinding.finalPackingInputPackingQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO：ケースマーク番号算出処理
                if (s.equals("")) {
                    mBinding.finalPackingInputPackingCno.setText("");
                    return;
                }
                mBinding.finalPackingInputPackingCno.setText("1～100");
            }
        });    }

    @Override
    public void mainSetting() {
        super.mainSetting();

        // 入力フィルター設定
        mBinding.finalPackingInputPackingCapacity1.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.finalPackingInputPackingCapacity2.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.finalPackingInputPackingCapacity3.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.finalPackingInputPackingCapacity4.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.finalPackingInputEditPackingBlueIce.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.finalPackingInputDryIce.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.finalPackingInputEditLength.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 1)});
        mBinding.finalPackingInputEditWidth.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 1)});
        mBinding.finalPackingInputEditHeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 1)});
        mBinding.finalPackingInputNetWeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 3)});
        mBinding.finalPackingInputGrossWeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 3)});

        // パレット内訳を英数字記号のみに設定
        mBinding.finalPackingInputPaletteBreakdown.setFilters(new InputFilter[]{new AlphaNumericSingInputFilter(), new InputFilter.LengthFilter(100)});

        // 内部処理用スキャンリスト取得
        mScanList = new SyukkoShijiDetailDao().getSyukkoShijiListByKey(Application.dbHelper.getWritableDatabase()
                ,mInvoiceNo, mlineNoList);

        // 入庫日設定
        mBinding.finalPackingReceiptDay.setText(getReceiptDayString());
    }

    @Override
    public void CommStatusChanged(CommConst.ScannerStatus status) { }
    @Override
    public boolean hasScanner() {
        return false;
    }
    @Override
    protected void openScanner() { }
    // endregion

    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        // リスナー生成
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // 呼び出し元の画面に戻る
            mListener.popBackStack();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0024, listener);
    }

    /**
     * 指示内容確認ボタン
     */
    private void instructionsButton() {
        // インパラ作成
        Bundle b = new Bundle();
        b.putString("screenPrefix", "K");

        // 次画面遷移
        CheckPackInstructionsFragment nextFragment = new CheckPackInstructionsFragment();
        nextFragment.setArguments(b);
        mListener.replaceFragmentWithStack(nextFragment, TAG);
    }

    /**
     * 確定ボタンタップ
     */
    private void confirmButton() {
        MessageDialogFragment confirmDialog = new MessageDialogFragment(getContext());
        // キャンセル押下
        confirmDialog.setNegativeButton(getString(R.string.cancel), v -> confirmDialog.dismiss());
        // OK押下
        confirmDialog.setPositiveButton(getString(R.string.ok), v-> {

            // 確定処理　★TODO：後で実装

            // 画面遷移
            confirmDialog.dismiss();
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_packingFinalPacking_to_packingListDisplayFragment);
        });
        confirmDialog.setMessage("確定します。\nよろしいですか？");
        confirmDialog.show(getActivity().getSupportFragmentManager(), "MessageDialogFragment");
    }

    /**
     * 入庫日文字列取得
     * @return
     */
    private String getReceiptDayString(){
        String receiptDate = mScanList.get(0).getReceiptDate();
        for(IssueTagInfo item : mScanList){
            if(!item.getReceiptDate().equals(receiptDate)){
                receiptDate = getString(R.string.multiple_date);
                break;
            }
        }
        return receiptDate;
    }

    /**
     * 表記用ケースマーク番号文字列取得
     * @return  nullの場合はチェックエラー
     */
    private String getDisplayCsNoString(){
        int packingCount = Integer.parseInt(mBinding.finalPackingInputPackingQuantity.getText().toString());
        // 梱包数が 2 以上
        if(packingCount >= 2){
            // オーバーパック混在
            if(isOverPackMixed()){
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2022));
                return null;
            }

            // オーバーパック数不一致
            if(!isOverPackCountAllSame()){
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2014));
                return null;
            }

            // 「発注番号」「出荷数量」不一致
            if(!isSameContents()){
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2013));
                return null;
            }

            // 梱包数妥当性チェック
            if(true){
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2015));
                return null;
            }
        }

        // ケースマーク番号開始値算出

        // 表記用ケースマーク番号決定

        return "";
    }

    /**
     * オーバーパック番号有無混在チェック
     * @return  true：混在
     */
    private boolean isOverPackMixed(){
        // オーバーパック番号有無
        boolean isOverPackExist=mScanList.get(0).getOverPackNo()==0 ? false : true;

        for(IssueTagInfo item : mScanList){
            boolean tmpExit = item.getOverPackNo()==0 ? false : true;

            if(tmpExit != isOverPackExist){
                return false;
            }
        }
        return true;
    }

    /**
     * オーバーパック番号全一致チェック
     * @return  true：全一致
     */
    private boolean isOverPackCountAllSame(){
        // オーバーパック番号無しはチェック不要
        if(mScanList.get(0).getOverPackNo()==0){
            return true;
        }

        // ケースマーク番号ごとカウント数取得
        ArrayList<Integer> countList = new ArrayList<>();
        int tmpOverPackCount=0;
        int overPackNo=mScanList.get(0).getOverPackNo();
        for(IssueTagInfo item : mScanList){
            if(overPackNo != item.getOverPackNo()){
                overPackNo = item.getOverPackNo();
                countList.add(tmpOverPackCount);
                tmpOverPackCount=0;
            }
            tmpOverPackCount++;
        }
        countList.add(tmpOverPackCount);

        // カウント数チェック
        return (Collections.frequency(countList, tmpOverPackCount) == countList.size());
    }

    /**
     * 発注番号、出荷数比較
     * @return  true：同じである
     */
    private boolean isSameContents(){
        String basePlaceOderNo = mScanList.get(0).getPlaceOrderNo();
        double baseSyukkaSu = mScanList.get(0).getSyukkaSu();
        for(IssueTagInfo item : mScanList){
            if(!basePlaceOderNo.equals(item.getPlaceOrderNo())
                || Double.compare(baseSyukkaSu, item.getSyukkaSu()) != 0){
                return false;
            }
        }
        return true;
    }
}
