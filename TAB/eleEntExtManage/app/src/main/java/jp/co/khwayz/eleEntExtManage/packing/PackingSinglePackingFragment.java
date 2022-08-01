package jp.co.khwayz.eleEntExtManage.packing;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.densowave.scannersdk.Const.CommConst;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.CategorySpinnerAdapter;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.filter.DecimalDigitsInputFilter;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.common.models.InnerInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoShijiKeyInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoInnerDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPackingSinglePackingBinding;
import jp.co.khwayz.eleEntExtManage.packing.task.SinglePackingRegistTask;
import jp.co.khwayz.eleEntExtManage.util.Util;

public class PackingSinglePackingFragment extends BaseFragment {
    // DataBinding
    FragmentPackingSinglePackingBinding mBinding;

    // ARGS
    private static final String ARGS_INVOICE_NO = "invoice_no";
    private static final String ARGS_RENBAN = "renban";
    private static final String ARGS_LINE_NO = "lineNo";
    private static final String ARGS_HACHU_NO = "hachuNo";
    private static final String ARGS_SYUKKA_SU = "syukkaSu";
    private static String mInvoiceNo;
    private static int mRenban;
    private static int mLineNo;
    private static String mPlaceOrderNo;
    private static String mIssueQuantity;

    // Adapter
    private CategorySpinnerAdapter mWorkDescriptionSpinner_1;
    private CategorySpinnerAdapter mWorkDescriptionSpinner_2;
    private CategorySpinnerAdapter mWorkDescriptionSpinner_3;
    private CategorySpinnerAdapter mWorkDescriptionSpinner_4;
    private CategorySpinnerAdapter mUnitSpinner;
    private CategorySpinnerAdapter mInnerContainerSpinner;
    private CategorySpinnerAdapter mOuterContainerSpinner;

    // List
    List<CategoryInfo> mWorkDescriptionDataSet;
    List<CategoryInfo> mUnitDataSet;
    List<CategoryInfo> mInnerContainerDataSet;
    List<CategoryInfo> mOuterContainerDataSet;

    /**
     * コンストラクタ
     * @param invoiceNo
     * @param renban
     * @param lineNo
     * @return
     */
    public static PackingSinglePackingFragment newInstance(String invoiceNo, int renban, int lineNo, String hachuNo, String syukkaSu){
        PackingSinglePackingFragment fragment = new PackingSinglePackingFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_INVOICE_NO, invoiceNo);
        args.putInt(ARGS_RENBAN, renban);
        args.putInt(ARGS_LINE_NO, lineNo);
        args.putString(ARGS_HACHU_NO, hachuNo);
        args.putString(ARGS_SYUKKA_SU, syukkaSu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentPackingSinglePackingBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mInvoiceNo = args.getString(ARGS_INVOICE_NO);
        this.mRenban = args.getInt(ARGS_RENBAN);
        this.mLineNo = args.getInt(ARGS_LINE_NO);
        this.mPlaceOrderNo = args.getString(ARGS_HACHU_NO);
        this.mIssueQuantity = args.getString(ARGS_SYUKKA_SU);
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
        mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.packing_single_packing_input));
        mListener.setScreenId(getString(R.string.screen_id_packing_single));
        mListener.setSubHeaderExplanationText(getString(R.string.single_packing_explanation));

        // ****************************
        // スピナー選択肢生成
        // ****************************
        // 作業内容
        mWorkDescriptionDataSet = mUtilListener.getCategoryList(Constants.KBN_EKONPOMEISAI);;
        CategoryInfo topValue = new CategoryInfo("","");
        mWorkDescriptionDataSet.add(0, topValue);
        // 単位
        mUnitDataSet = mUtilListener.getCategoryList(Constants.KBN_EKIKENTANI);;
        mUnitDataSet.add(0, topValue);
        // 内装容器
        mInnerContainerDataSet = mUtilListener.getCategoryList(Constants.KBN_EKIKENNAISO);;
        mInnerContainerDataSet.add(0, topValue);
        // 外装容器
        mOuterContainerDataSet = mUtilListener.getCategoryList(Constants.KBN_EKIKENGAISO);;
        mOuterContainerDataSet.add(0, topValue);

        // ****************************
        // スピナーへセット
        // ****************************
        // 作業内容①梱包
        mWorkDescriptionSpinner_1 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.packingSinglePackagingSpinner1.setAdapter(mWorkDescriptionSpinner_1);

        // 作業内容②梱包
        mWorkDescriptionSpinner_2 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.packingSinglePackagingSpinner2.setAdapter(mWorkDescriptionSpinner_2);

        // 作業内容③梱包
        mWorkDescriptionSpinner_3 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.packingSinglePackagingSpinner3.setAdapter(mWorkDescriptionSpinner_3);

        // 作業内容④梱包
        mWorkDescriptionSpinner_4 = new CategorySpinnerAdapter(getContext(), mWorkDescriptionDataSet);
        mBinding.packingSinglePackagingSpinner4.setAdapter(mWorkDescriptionSpinner_4);

        // 単位
        mUnitSpinner = new CategorySpinnerAdapter(getContext(), mUnitDataSet);
        mBinding.packingSinglePackagingSpinnerUnit.setAdapter(mUnitSpinner);

        // 内装容器
        mInnerContainerSpinner = new CategorySpinnerAdapter(getContext(), mInnerContainerDataSet);
        mBinding.packingSinglePackagingSpinnerInteriorContainer.setAdapter(mInnerContainerSpinner);

        // 外装容器
        mOuterContainerSpinner = new CategorySpinnerAdapter(getContext(), mOuterContainerDataSet);
        mBinding.packingSinglePackagingSpinnerOuterContainer.setAdapter(mOuterContainerSpinner);
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

        // 確定ボタン
        ButtonInfo confirmButtonInfo = new ButtonInfo(getString(R.string.confirm), v -> confirmationButton());
        mListener.setFooterButton(null, null, confirmButtonInfo, null, null);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();

        // 入力フィルター設定
        mBinding.packingSinglePackagingPackingCapacity1.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.packingSinglePackagingPackingCapacity2.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.packingSinglePackagingPackingCapacity3.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.packingSinglePackagingPackingCapacity4.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
        mBinding.packingSinglePackagingPackingNetWeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 3)});
        mBinding.packingSinglePackagingPackingInternalCapacity.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});

        // 入庫日取得
        String nyukoDate = new SyukkoShijiDetailDao().getNyukoDate(Application.dbHelper.getWritableDatabase()
                , mInvoiceNo, mRenban, mLineNo);
        mBinding.packingSinglePackagingReceiptDay.setText(nyukoDate == null ? "" : nyukoDate);

        // 梱包インナー確認～設定
        InnerInfo innerRecord = new KonpoInnerDao().getRecord(Application.dbHelper.getWritableDatabase()
                , mInvoiceNo, mRenban, mLineNo);
        if(innerRecord != null){
            // 作業内容①
            int index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, innerRecord.getInnerSagyo1());
            mBinding.packingSinglePackagingSpinner1.setSelection(index);
            mBinding.packingSinglePackagingPackingCapacity1.setText(String.format("%.1f",innerRecord.getInnerSagyo1Siyo()).equals("0.0") ? "" : String.format("%.1f",innerRecord.getInnerSagyo1Siyo()));
            // 作業内容②
            index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, innerRecord.getInnerSagyo2());
            mBinding.packingSinglePackagingSpinner2.setSelection(index);
            mBinding.packingSinglePackagingPackingCapacity2.setText(String.format("%.1f",innerRecord.getInnerSagyo2Siyo()).equals("0.0") ? "" : String.format("%.1f",innerRecord.getInnerSagyo2Siyo()));
            // 作業内容③
            index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, innerRecord.getInnerSagyo3());
            mBinding.packingSinglePackagingSpinner3.setSelection(index);
            mBinding.packingSinglePackagingPackingCapacity3.setText(String.format("%.1f",innerRecord.getInnerSagyo3Siyo()).equals("0.0") ? "" : String.format("%.1f",innerRecord.getInnerSagyo3Siyo()));
            // 作業内容④
            index = Util.getSpinnerSelectPosition(mWorkDescriptionDataSet, innerRecord.getInnerSagyo4());
            mBinding.packingSinglePackagingSpinner4.setSelection(index);
            mBinding.packingSinglePackagingPackingCapacity4.setText(String.format("%.1f",innerRecord.getInnerSagyo4Siyo()).equals("0.0") ? "" : String.format("%.1f",innerRecord.getInnerSagyo4Siyo()));
            // ラベル枚数
            mBinding.packingSinglePackagingPackingLabelNumberOfSheets.setText(String.valueOf(innerRecord.getLabelSu()).equals("0") ? "" : String.valueOf(innerRecord.getLabelSu()));
            // NW
            mBinding.packingSinglePackagingPackingNetWeight.setText(String.format("%.3f",innerRecord.getNetWeight()).equals("0.000") ? "" : String.format("%.3f",innerRecord.getNetWeight()));
            // 内容量
            mBinding.packingSinglePackagingPackingInternalCapacity.setText(String.format("%.1f",innerRecord.getDanNaiyoRyo()).equals("0.0") ? "" : String.format("%.1f",innerRecord.getDanNaiyoRyo()));
            // 単位
            index = Util.getSpinnerSelectPosition(mUnitDataSet, innerRecord.getDanTani());
            mBinding.packingSinglePackagingSpinnerUnit.setSelection(index);
            // 内装容器
            index = Util.getSpinnerSelectPosition(mInnerContainerDataSet, innerRecord.getDanNaisoYoki());
            mBinding.packingSinglePackagingSpinnerInteriorContainer.setSelection(index);
            // 本数
            mBinding.packingSinglePackagingPackingNumber.setText(String.valueOf(innerRecord.getDanHonsu()).equals("0") ? "" : String.valueOf(innerRecord.getDanHonsu()));
            // 外装容器
            index = Util.getSpinnerSelectPosition(mOuterContainerDataSet, innerRecord.getDanGaisoYoki());
            mBinding.packingSinglePackagingSpinnerOuterContainer.setSelection(index);
            // 備考
            mBinding.packingSinglePackagingRemarks.setText(innerRecord.getBiko());
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
        mUtilListener.showConfirmDialog(R.string.info_message_I0022, listener);
    }

    private void confirmationButton() {
        // リスナー生成
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // ******************************************************
            // インナーテーブル保存情報を画面から取得
            // ******************************************************
            InnerInfo innerInfoItem = new InnerInfo(mInvoiceNo);
            innerInfoItem.setRenban(mRenban);
            innerInfoItem.setLineNo(mLineNo);
            // 作業内容①
            CategoryInfo infoSpSagyo1 = (CategoryInfo) mBinding.packingSinglePackagingSpinner1.getSelectedItem();
            innerInfoItem.setInnerSagyo1(infoSpSagyo1.getElement());
            innerInfoItem.setInnerSagyo1Siyo(mBinding.packingSinglePackagingPackingCapacity1.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.packingSinglePackagingPackingCapacity1.getText().toString()));
            // 作業内容②
            CategoryInfo infoSpSagyo2 = (CategoryInfo) mBinding.packingSinglePackagingSpinner2.getSelectedItem();
            innerInfoItem.setInnerSagyo2(infoSpSagyo2.getElement());
            innerInfoItem.setInnerSagyo2Siyo(mBinding.packingSinglePackagingPackingCapacity2.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.packingSinglePackagingPackingCapacity2.getText().toString()));
            // 作業内容③
            CategoryInfo infoSpSagyo3 = (CategoryInfo) mBinding.packingSinglePackagingSpinner3.getSelectedItem();
            innerInfoItem.setInnerSagyo3(infoSpSagyo3.getElement());
            innerInfoItem.setInnerSagyo3Siyo(mBinding.packingSinglePackagingPackingCapacity3.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.packingSinglePackagingPackingCapacity3.getText().toString()));
            // 作業内容④
            CategoryInfo infoSpSagyo4 = (CategoryInfo) mBinding.packingSinglePackagingSpinner4.getSelectedItem();
            innerInfoItem.setInnerSagyo4(infoSpSagyo4.getElement());
            innerInfoItem.setInnerSagyo4Siyo(mBinding.packingSinglePackagingPackingCapacity4.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.packingSinglePackagingPackingCapacity4.getText().toString()));
            // ラベル枚数
            innerInfoItem.setLabelSu(mBinding.packingSinglePackagingPackingLabelNumberOfSheets.getText().toString().isEmpty() ?
                    0 : Integer.parseInt(mBinding.packingSinglePackagingPackingLabelNumberOfSheets.getText().toString()));
            // NW
            innerInfoItem.setNetWeight(mBinding.packingSinglePackagingPackingNetWeight.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.packingSinglePackagingPackingNetWeight.getText().toString()));
            // 内容量
            innerInfoItem.setDanNaiyoRyo(mBinding.packingSinglePackagingPackingInternalCapacity.getText().toString().isEmpty() ?
                    0 : Double.parseDouble(mBinding.packingSinglePackagingPackingInternalCapacity.getText().toString()));
            // 単位
            CategoryInfo infoDanTani = (CategoryInfo) mBinding.packingSinglePackagingSpinnerUnit.getSelectedItem();
            innerInfoItem.setDanTani(infoDanTani.getElement());
            // 内装容器
            CategoryInfo infoDanNaisoYoki = (CategoryInfo) mBinding.packingSinglePackagingSpinnerInteriorContainer.getSelectedItem();
            innerInfoItem.setDanNaisoYoki(infoDanNaisoYoki.getElement());
            // 本数
            innerInfoItem.setDanHonsu(mBinding.packingSinglePackagingPackingNumber.getText().toString().isEmpty() ?
                    0 : Integer.parseInt(mBinding.packingSinglePackagingPackingNumber.getText().toString()));
            // 外装容器
            CategoryInfo infoDanGaisoYoki = (CategoryInfo) mBinding.packingSinglePackagingSpinnerOuterContainer.getSelectedItem();
            innerInfoItem.setDanGaisoYoki(infoDanGaisoYoki.getElement());
            // 備考
            innerInfoItem.setBiko(mBinding.packingSinglePackagingRemarks.getText().toString());

            // ******************************************************
            // 更新キー情報構築
            // ******************************************************
            ArrayList<SyukkoShijiKeyInfo> updateKeyInfo = new ArrayList<>();
            // 同一明細ありチェックの場合、キー情報取得
            if(mBinding.packingSinglePackagingCheckSameItem.isChecked()){
                updateKeyInfo = new SyukkoShijiDetailDao().getSameDetailInfo (Application.dbHelper.getWritableDatabase()
                        ,mInvoiceNo, mPlaceOrderNo, mIssueQuantity);
            } else {
                // 1件登録
                SyukkoShijiKeyInfo item = new SyukkoShijiKeyInfo(mRenban, mLineNo);
                updateKeyInfo.add(item);
            }


            // 単独梱包登録処理
            new SinglePackingRegistTask(singlePackingRegistCallBack
                    , mInvoiceNo, updateKeyInfo, innerInfoItem).execute();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0023, listener);
    }

    SinglePackingRegistTask.Callback singlePackingRegistCallBack = new SinglePackingRegistTask.Callback() {
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
            // 呼び出し元の画面に戻る
            mListener.popBackStack();
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
}
