package jp.co.khwayz.eleEntExtManage.packing;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
import jp.co.khwayz.eleEntExtManage.common.models.OuterInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoShijiCsKeyInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoShijiKeyInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoOuterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPackingFinalPackingBinding;
import jp.co.khwayz.eleEntExtManage.instr_cfm.CheckPackInstructionsFragment;
import jp.co.khwayz.eleEntExtManage.packing.task.FinalPackingRegistTask;
import jp.co.khwayz.eleEntExtManage.util.Util;

public class PackingFinalPackingFragment extends BaseFragment {
    // DataBinding
    FragmentPackingFinalPackingBinding mBinding;

    // ARGS
    private static final String ARGS_INVOICE_NO = "invoice_no";
    private static final String ARGS_LINE_NO_LIST = "lineNoList";
    private static final String ARGS_EDIT_MODE = "editMode";
    private static String mInvoiceNo;
    private static ArrayList<SyukkoShijiKeyInfo> mlineNoList;
    private static boolean mEditMode;

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

    // ケースマーク番号開始値・終了値
    private int mStartCaseMarkNum;
    private int mEndCaseMarkNum;

    // オーバーパック有無    false：オーバーパック無し     true：オーバーパックあり
    private boolean mOverPackFlg;
    // 同一オーバーパック番号件数
    private int mOverPackCount;
    // 同一オーバーパック梱包数
    private int mOverPackCurtonCount;
    // 梱包数（変更判定用）
    String mKonpoCount;
    // 初期表記ケースマーク番号     「修正」モードの場合に初期値を保存
    private String mInitHyokiCaseMarkNum="";

    /**
     * コンストラクタ
     * @param invoiceNo
     * @param lineNno
     * @param editMode  false:新規    true:修正
     * @return
     */
    public static PackingFinalPackingFragment newInstance(String invoiceNo, ArrayList<SyukkoShijiKeyInfo> lineNno, boolean editMode){
        PackingFinalPackingFragment fragment = new PackingFinalPackingFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_INVOICE_NO, invoiceNo);
        args.putParcelableArrayList(ARGS_LINE_NO_LIST, lineNno);
        args.putBoolean(ARGS_EDIT_MODE, editMode);
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
        this.mEditMode = args.getBoolean(ARGS_EDIT_MODE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListener.setSubHeaderExplanationText("");
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
        CategoryInfo topValue = new CategoryInfo("","");
        // 梱包名
        mPackingNameDataSet = mUtilListener.getCategoryList(Constants.KBN_ESAISHUKONPO);
        mPackingNameDataSet.add(0, topValue);
        // 作業内容
        mWorkDescriptionDataSet = mUtilListener.getCategoryList(Constants.KBN_EKONPOMEISAI);
        mWorkDescriptionDataSet.add(0, topValue);

        // ****************************
        // スピナーへセット
        // ****************************
        // 梱包名
        mPackingNameSpinner = new CategorySpinnerAdapter(getContext(), mPackingNameDataSet);
        mBinding.finalPackingInputSpinnerPackingName.setAdapter(mPackingNameSpinner);

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
        ButtonInfo confirmButtonInfo = new ButtonInfo(getString(R.string.confirm), v -> onConfirmClick());
        mListener.setFooterButton(null, instructions, null, confirmButtonInfo, null);

        // 梱包数入力（ケースマーク番号算出起点）
        mBinding.finalPackingInputPackingQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 確定ボタン押下時
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    v.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                    return true;
                }
                return false;
            }
        });

        mBinding.finalPackingInputPackingQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    // 番号変更ナシ（フォーカスして抜けただけとか）はスルー
                    String tmpKonpoCount = mBinding.finalPackingInputPackingQuantity.getText().toString();
                    if(tmpKonpoCount.equals(mKonpoCount)){
                        return;
                    }

                    // ケースマーク番号算出処理
                    mKonpoCount = tmpKonpoCount;
                    if (mKonpoCount.equals("")) {
                        mBinding.finalPackingInputPackingCno.setText("");
                        return;
                    }
                    mBinding.finalPackingInputPackingCno.setText(getDisplayCsNoString());
                }
            }
        });
    }

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

        // 内部処理用スキャンリスト取得（オーバーパック番号でソート済み）
        mScanList = new SyukkoShijiDetailDao().getSyukkoShijiListByKey(Application.dbHelper.getWritableDatabase()
                ,mInvoiceNo, mlineNoList);

        // 入庫日設定
        mBinding.finalPackingReceiptDay.setText(getReceiptDayString());

        // 修正モードの場合、梱包アウターを画面に表示
        if(mEditMode){
            OuterInfo outerRecord = new KonpoOuterDao().getOuterInfoRecord(Application.dbHelper.getWritableDatabase()
                ,mInvoiceNo, mScanList.get(0).getCsNumber()) ;

            if(outerRecord == null){
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
                Application.log.e(TAG, "最終梱包画面：梱包アウター該当レコードなし InvoiceNo:" + mInvoiceNo + " CS_No:" + mScanList.get(0).getCsNumber());
                return;
            }

            /************************************/
            // 画面項目に表示
            /************************************/
            // 梱包名
            int index = Util.getSpinnerSelectPosition(mPackingNameDataSet, outerRecord.getSaisyuKonpoNisugata());
            mBinding.finalPackingInputSpinnerPackingName.setSelection(index);
            // 作業内容①
            index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, outerRecord.getOuterSagyo1());
            mBinding.finalPackingInputSpinner1.setSelection(index);
            mBinding.finalPackingInputPackingCapacity1.setText(String.format("%.1f",outerRecord.getOuterSagyo1Siyo()).equals("0.0") ?
                    "" : String.format("%.1f",outerRecord.getOuterSagyo1Siyo()));
            // 作業内容②
            index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, outerRecord.getOuterSagyo2());
            mBinding.finalPackingInputSpinner2.setSelection(index);
            mBinding.finalPackingInputPackingCapacity2.setText(String.format("%.1f",outerRecord.getOuterSagyo2Siyo()).equals("0.0") ?
                    "" : String.format("%.1f",outerRecord.getOuterSagyo2Siyo()));
            // 作業内容③
            index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, outerRecord.getOuterSagyo3());
            mBinding.finalPackingInputSpinner3.setSelection(index);
            mBinding.finalPackingInputPackingCapacity3.setText(String.format("%.1f",outerRecord.getOuterSagyo3Siyo()).equals("0.0") ?
                    "" : String.format("%.1f",outerRecord.getOuterSagyo3Siyo()));
            // 作業内容④
            index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, outerRecord.getOuterSagyo4());
            mBinding.finalPackingInputSpinner4.setSelection(index);
            mBinding.finalPackingInputPackingCapacity4.setText(String.format("%.1f",outerRecord.getOuterSagyo4Siyo()).equals("0.0") ?
                    "" : String.format("%.1f",outerRecord.getOuterSagyo4Siyo()));
            // ブルーアイス
            mBinding.finalPackingInputEditPackingBlueIce.setText(String.format("%.1f",outerRecord.getBlueIceSiyo()).equals("0.0") ?
                    "" : String.format("%.1f",outerRecord.getBlueIceSiyo()));
            // ドライアイス
            mBinding.finalPackingInputDryIce.setText(String.format("%.1f",outerRecord.getDryIceSiyo()).equals("0.0") ?
                    "" : String.format("%.1f",outerRecord.getDryIceSiyo()));
            // 梱包数
            mBinding.finalPackingInputPackingQuantity.setText(String.valueOf(outerRecord.getKonpoSu()).equals("0") ?
                    "" : String.valueOf(outerRecord.getKonpoSu()));
            // C/No
            mBinding.finalPackingInputPackingCno.setText(outerRecord.getHyokiCsNumber());
            this.mInitHyokiCaseMarkNum = outerRecord.getHyokiCsNumber();
            // ケースマーク番号初期保存
            spritCaseMarkNum(this.mInitHyokiCaseMarkNum);
            // ラベル枚数入力
            mBinding.finalPackingInputLabelCount.setText(String.valueOf(outerRecord.getLabelSu()).equals("0") ?
                    "" : String.valueOf(outerRecord.getLabelSu()));
            // L
            mBinding.finalPackingInputEditLength.setText(String.format("%.1f",outerRecord.getOuterLength()).equals("0.0") ?
                    "" : String.format("%.1f",outerRecord.getOuterLength()));
            // W
            mBinding.finalPackingInputEditWidth.setText(String.format("%.1f",outerRecord.getOuterWidth()).equals("0.0") ?
                    "" : String.format("%.1f",outerRecord.getOuterWidth()));
            // H
            mBinding.finalPackingInputEditHeight.setText(String.format("%.1f",outerRecord.getOuterHeight()).equals("0.0") ?
                    "" : String.format("%.1f",outerRecord.getOuterHeight()));
            // NW
            mBinding.finalPackingInputNetWeight.setText(String.format("%.3f",outerRecord.getNetWeight()).equals("0.000") ?
                    "" : String.format("%.3f",outerRecord.getNetWeight()));
            // GW
            mBinding.finalPackingInputGrossWeight.setText(String.format("%.3f",outerRecord.getGrossWeight()).equals("0.000") ?
                    "" : String.format("%.3f",outerRecord.getGrossWeight()));
            // 備考
            mBinding.finalPackingInputRemarks.setText(outerRecord.getBiko());
            // パレット内訳
            mBinding.finalPackingInputPaletteBreakdown.setText(outerRecord.getPalettUchiwake());
        }
        // 初期梱包数保存
        mKonpoCount = mBinding.finalPackingInputPackingQuantity.getText().toString();
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
    private void onConfirmClick() {
        // 必須入力チェック
        CategoryInfo packingName = (CategoryInfo) mBinding.finalPackingInputSpinnerPackingName.getSelectedItem();
        if(packingName.getElementName().isEmpty() ||
            mBinding.finalPackingInputPackingQuantity.getText().toString().isEmpty() ||
            mBinding.finalPackingInputEditLength.getText().toString().isEmpty() ||
            mBinding.finalPackingInputEditWidth.getText().toString().isEmpty() ||
            mBinding.finalPackingInputEditHeight.getText().toString().isEmpty()
        ){
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2016));
            return;
        }

        // リスナー生成
        DialogInterface.OnClickListener listener = (dialog, which) -> {

            /***********************************************/
            // 梱包アウター情報共通項目セット
            /***********************************************/
            OuterInfo outer = new OuterInfo(mInvoiceNo);
            // Invoice番号
            outer.setInvoiceNo(mInvoiceNo);
            // 表記用ケースマーク番号
            outer.setHyokiCsNumber(mBinding.finalPackingInputPackingCno.getText().toString());
            // アウター作業内容1
            CategoryInfo sagyo1 = (CategoryInfo) mBinding.finalPackingInputSpinner1.getSelectedItem();
            outer.setOuterSagyo1(sagyo1.getElement());
            // アウター作業内容1使用量
            outer.setOuterSagyo1Siyo(mBinding.finalPackingInputPackingCapacity1.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.finalPackingInputPackingCapacity1.getText().toString()));
            // アウター作業内容2
            CategoryInfo sagyo2 = (CategoryInfo) mBinding.finalPackingInputSpinner2.getSelectedItem();
            outer.setOuterSagyo2(sagyo2.getElement());
            // アウター作業内容2使用量
            outer.setOuterSagyo2Siyo(mBinding.finalPackingInputPackingCapacity2.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.finalPackingInputPackingCapacity2.getText().toString()));
            // アウター作業内容3
            CategoryInfo sagyo3 = (CategoryInfo) mBinding.finalPackingInputSpinner3.getSelectedItem();
            outer.setOuterSagyo3(sagyo3.getElement());
            // アウター作業内容3使用量
            outer.setOuterSagyo3Siyo(mBinding.finalPackingInputPackingCapacity3.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.finalPackingInputPackingCapacity3.getText().toString()));
            // アウター作業内容4
            CategoryInfo sagyo4 = (CategoryInfo) mBinding.finalPackingInputSpinner4.getSelectedItem();
            outer.setOuterSagyo4(sagyo4.getElement());
            // アウター作業内容4使用量
            outer.setOuterSagyo4Siyo(mBinding.finalPackingInputPackingCapacity4.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.finalPackingInputPackingCapacity4.getText().toString()));
            // ブルーアイス使用量
            outer.setBlueIceSiyo(mBinding.finalPackingInputEditPackingBlueIce.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.finalPackingInputEditPackingBlueIce.getText().toString()));
            // ドライアイス使用量
            outer.setDryIceSiyo(mBinding.finalPackingInputDryIce.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.finalPackingInputDryIce.getText().toString()));
            // ラベル枚数
            outer.setLabelSu(mBinding.finalPackingInputLabelCount.getText().toString().isEmpty() ?
                    0 : Integer.parseInt(mBinding.finalPackingInputLabelCount.getText().toString()));
            // 梱包数
            outer.setKonpoSu(mBinding.finalPackingInputPackingQuantity.getText().toString().isEmpty() ?
                    0 : Integer.parseInt(mBinding.finalPackingInputPackingQuantity.getText().toString()));
            // アウター長さ(L)
            outer.setOuterLength(mBinding.finalPackingInputEditLength.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.finalPackingInputEditLength.getText().toString()));
            // アウター幅(W)
            outer.setOuterWidth(mBinding.finalPackingInputEditWidth.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.finalPackingInputEditWidth.getText().toString()));
            // アウター高さ(H)
            outer.setOuterHeight(mBinding.finalPackingInputEditHeight.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.finalPackingInputEditHeight.getText().toString()));
            // NW(net weight)
            outer.setNetWeight(mBinding.finalPackingInputNetWeight.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.finalPackingInputNetWeight.getText().toString()));
            // GW(gross weight)
            outer.setGrossWeight(mBinding.finalPackingInputGrossWeight.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.finalPackingInputGrossWeight.getText().toString()));
            // 最終梱包荷姿
            outer.setSaisyuKonpoNisugata(packingName.getElement());
            // 備考
            outer.setBiko(mBinding.finalPackingInputRemarks.getText().toString());
            // パレット内訳
            outer.setPalettUchiwake(mBinding.finalPackingInputPaletteBreakdown.getText().toString());
            // 箱数
            outer.setCartonSu(calcCartonCount());
            // 荷札数
            outer.setNifudaSu(calcNifdaCount());

            // 出庫指示明細キー情報生成
            List<SyukkoShijiCsKeyInfo> keyInfo = createKeyInfo();

            // 最終梱包確定処理
            new FinalPackingRegistTask(completeFinalPackingCallBack, mStartCaseMarkNum, mEndCaseMarkNum, mInitHyokiCaseMarkNum, outer, keyInfo).execute();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0025, listener
                , mScanList.size(), mBinding.finalPackingInputPackingQuantity.getText().toString());
    }

    FinalPackingRegistTask.Callback completeFinalPackingCallBack = new FinalPackingRegistTask.Callback() {
        @Override
        public void onPreExecute() {
            mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0027));
        }

        @Override
        public void onTaskFinished(boolean result) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // スキャン画面初期化
            Application.initFlag = true;
            // 梱包一覧画面に戻る
            mListener.popBackStack(PackingListDisplayFragment.class.getSimpleName());
            // メッセージ表示
            mUtilListener.showSnackBarOnUiThread(mUtilListener.getDataBaseMessage(R.string.info_message_I0007));
        }

        @Override
        public void onError() {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // エラーメッセージを表示
            mUtilListener.showAlertDialog(getString(R.string.const_err_message_E9000));
        }
    };

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
                return "";
            }

            // オーバーパック件数不一致
            if(!isOverPackCountAllSame()){
                this.mOverPackCount=0;
                this.mOverPackCurtonCount=0;
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2014));
                return "";
            }

            // 「発注番号」「出荷数量」不一致
            if(!isSameContents()){
                this.mOverPackCount=0;
                this.mOverPackCurtonCount=0;
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2013));
                return "";
            }

            // 梱包数妥当性チェック
            if(!isReasonableKonpoCount()){
                // 全梱包数セット
                int allPackingCount = mScanList.size();
                if(mOverPackFlg){
                    allPackingCount = this.mOverPackCount;
                }
                this.mOverPackCount=0;
                this.mOverPackCurtonCount=0;
                mUtilListener.showAlertDialog(R.string.err_message_E2015, allPackingCount);
                return "";
            }
        }
        // 表記用ケースマーク番号算出
        return calcDisplayCaseMarkNo();
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
                return true;
            }
        }
        mOverPackFlg = isOverPackExist;
        return false;
    }

    /**
     * オーバーパック番号件数全一致チェック
     * @return  true：全一致
     */
    private boolean isOverPackCountAllSame(){
        // オーバーパック番号無しはチェック不要
        if(mScanList.get(0).getOverPackNo()==0){
            return true;
        }

        // オーバーパックごとカウント数取得
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

        // オーバーパック件数内部保存
        this.mOverPackCurtonCount = tmpOverPackCount;
        this.mOverPackCount = countList.size();

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

    /**
     * 梱包数妥当性チェック
     * @return
     */
    private boolean isReasonableKonpoCount(){
        int konpoCount = Integer.parseInt(mBinding.finalPackingInputPackingQuantity.getText().toString());
        // オーバーパックあり
        if(mOverPackFlg){
            return ( mOverPackCount % konpoCount == 0);
        }
        // オーバーパック無し
        return (mScanList.size() % konpoCount == 0);
    }

    /**
     * 表記用ケースマーク番号生成
     * @return
     */
    private String calcDisplayCaseMarkNo(){
        // ケースマーク番号開始値算出
        mStartCaseMarkNum = new KonpoOuterDao().getMaxCsNo(Application.dbHelper.getWritableDatabase()
                ,mInvoiceNo) + 1;
        // 梱包数
        int konpoCount = Integer.parseInt(mBinding.finalPackingInputPackingQuantity.getText().toString());
        if(konpoCount == 1){
            mEndCaseMarkNum = mStartCaseMarkNum;
            return String.valueOf(mStartCaseMarkNum);
        }
        // ケースマーク番号終了値
        mEndCaseMarkNum = mStartCaseMarkNum + konpoCount - 1;
        return mStartCaseMarkNum + Constants.CASEMARK_SPLIT + mEndCaseMarkNum;
    }

    /**
     * 箱数算出
     * @return
     */
    private int calcCartonCount(){
        // 梱包数
        int konpoCount = Integer.parseInt(mBinding.finalPackingInputPackingQuantity.getText().toString());

        // オーバーパックあり
        if(mOverPackFlg){
            return (mScanList.size() / mOverPackCurtonCount / konpoCount);
        }
        // オーバーパック無し
        return (mScanList.size() / konpoCount);
    }

    /**
     * 荷札数算出
     * @return
     */
    private int calcNifdaCount(){
        // 梱包数
        int konpoCount = Integer.parseInt(mBinding.finalPackingInputPackingQuantity.getText().toString());
        return (mScanList.size() / konpoCount);
    }

    /**
     * 出庫指示明細更新用キー情報生成
     * @return
     */
    private List<SyukkoShijiCsKeyInfo> createKeyInfo() {
        List<SyukkoShijiCsKeyInfo> list = new ArrayList<SyukkoShijiCsKeyInfo>();

        // 梱包数1の場合
        if(Integer.parseInt(mKonpoCount) == 1){
            for (IssueTagInfo taginfo : mScanList) {
                SyukkoShijiCsKeyInfo item = new SyukkoShijiCsKeyInfo(mInvoiceNo, taginfo.getRenban()
                        , taginfo.getLineNo(), mStartCaseMarkNum);
                list.add(item);
            }
        } else {
            // 同一ケースマーク番号件数
            int sameCaseMarkCount = mScanList.size() / Integer.parseInt(mBinding.finalPackingInputPackingQuantity.getText().toString());

            // キー情報生成
            int caseMarkNum = mStartCaseMarkNum;
            int i = 0;
            for (IssueTagInfo taginfo : mScanList) {
                i++;
                SyukkoShijiCsKeyInfo item = new SyukkoShijiCsKeyInfo(mInvoiceNo, taginfo.getRenban()
                        , taginfo.getLineNo(), caseMarkNum);
                list.add(item);
                if(i % sameCaseMarkCount == 0){
                    caseMarkNum++;
                }
            }
        }
        return list;
    }

    private void spritCaseMarkNum(String hyokiCaseMarkNum){
        // 区切り文字で分割
        String[] caseMarkSp = hyokiCaseMarkNum.split(Constants.CASEMARK_SPLIT);
        mStartCaseMarkNum = Integer.parseInt(caseMarkSp[0]);
        mEndCaseMarkNum = mStartCaseMarkNum;
        if(caseMarkSp.length > 1){
            mEndCaseMarkNum = Integer.parseInt(caseMarkSp[1]);
        }
    }
}
