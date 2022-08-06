package jp.co.khwayz.eleEntExtManage.packing;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.densowave.scannersdk.Const.CommConst;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.Calender;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.CategorySpinnerAdapter;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.common.models.InvoiceSearchInfo;
import jp.co.khwayz.eleEntExtManage.common.models.PickedInvoiceSearchInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.CategoryMasterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoOuterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiHeaderDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPackingInvoiceSearchBinding;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.SortDialogFragment;
import jp.co.khwayz.eleEntExtManage.http.response.PickedInvoiceSearchResponse;
import jp.co.khwayz.eleEntExtManage.http.task.get.PickedInvoiceSearchTask;
import jp.co.khwayz.eleEntExtManage.picking.PickedInvoiceSearchRecyclerViewAdapter;
import jp.co.khwayz.eleEntExtManage.picking.PickedInvoiceSearchViewHolder;
import jp.co.khwayz.eleEntExtManage.util.Util;

/**
 * 梱包Invoice検索画面
 */
public class PackingInvoiceSearchFragment extends BaseFragment implements PickedInvoiceSearchRecyclerViewAdapter.OnItemClickListener
        , SortDialogFragment.SortDialogListener {

    // DataBinding
    private FragmentPackingInvoiceSearchBinding mBinding;

    // Adapter
    private PickedInvoiceSearchRecyclerViewAdapter invoiceAdapter;

    // List
    private RecyclerView invoiceList;
    private List<PickedInvoiceSearchInfo> invoiceInfoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentPackingInvoiceSearchBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        invoiceInfoList = null;
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
        mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.packing_invoice_search));
        mListener.setScreenId(getString(R.string.screen_id_packing_search));

        // 項目クリア
        mBinding.invoiceNoText.setText("");
        mBinding.txtPackingdeadline.setText("");
        mBinding.textviewShipDate.setText("");

        // 仕向地スピナー
        CategoryMasterDao categoryMasterDao = new CategoryMasterDao();
        List<CategoryInfo> destinationDataSet = categoryMasterDao.getDestinationSpinnerArray(Application.dbHelper.getReadableDatabase());
        // 画面オブジェクト
        CategorySpinnerAdapter destinationSpinnerAdapter = new CategorySpinnerAdapter(getContext(), destinationDataSet);
        mBinding.destinationSpinner.setAdapter(destinationSpinnerAdapter);

        // 保冷スピナー
        ArrayList<String> coolList = new ArrayList<>();
        for (Constants.CoolChoices coolChoice : Constants.CoolChoices.values()) {
            coolList.add(coolChoice.getCoolChoices());
        }
        ArrayAdapter<String> coolAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, coolList);
        coolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // SpinnerにAdapterをセット
        mBinding.coolSpinner.setAdapter(coolAdapter);

        // 危険品
        ArrayList<String> dangerousList = new ArrayList<>();
        for (Constants.DengerousChoices dangerousChoice : Constants.DengerousChoices.values()) {
            dangerousList.add(dangerousChoice.getDengerousChoices());
        }
        ArrayAdapter<String> dangerousAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, dangerousList);
        dangerousAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // SpinnerにAdapterをセット
        mBinding.spinnerDangerous.setAdapter(dangerousAdapter);

        // 輸送区分
        List<CategoryInfo> transportDataSet = mUtilListener.getCategoryList(Constants.KBN_EYUSOMEANS);
        CategoryInfo transportTopValue = new CategoryInfo("","");
        transportDataSet.add(0, transportTopValue);
        CategorySpinnerAdapter transportSpinnerAdapter = new CategorySpinnerAdapter(getContext(), transportDataSet);
        mBinding.spinnerTransport.setAdapter(transportSpinnerAdapter);

        // リストオブジェクト
        this.invoiceList = mBinding.invoiceList;
        if(this.invoiceInfoList == null) {
            this.invoiceInfoList = new ArrayList<>();
        }

        // リストアダプタ生成
        this.invoiceAdapter = new PickedInvoiceSearchRecyclerViewAdapter(this.invoiceInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.invoiceList.setHasFixedSize(true);
        this.invoiceList.setLayoutManager(llm);
        this.invoiceList.setAdapter(invoiceAdapter);
        invoiceAdapter.setOnItemClickListener(this);
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

        // データ受信
        View.OnClickListener dataReceiveClickListener = v -> {
            onDataReceiveClick();
        };
        ButtonInfo dataReceiveButtonInfo = new ButtonInfo(getString(R.string.data_receive), dataReceiveClickListener);
        mListener.setFooterButton(null, null, null, null, dataReceiveButtonInfo);


        /*************************************/
        // イベントリスナー登録
        /*************************************/
        // ソートボタン
        mBinding.buttonSort.setOnClickListener(v -> detailSorter());
        // リスト返信希望日
        mBinding.packingdeadlineButton.setOnClickListener(v -> Calender.show(getActivity(), mBinding.txtPackingdeadline));
        // 出荷日
        mBinding.shipDateButton.setOnClickListener(v -> Calender.show(getActivity(), mBinding.textviewShipDate));
        // 検索ボタン
        mBinding.invoiceSearchButton.setOnClickListener(v -> onSearchButtonClick());
    }

    @Override
    public void mainSetting() {
        super.mainSetting();

        // 前回保存データありの場合
        if( Application.invoiceSearchInfo != null) {
            // カレンダー日付設定
            mBinding.txtPackingdeadline.setText(Application.invoiceSearchInfo.getListHenshinDate());
            mBinding.textviewShipDate.setText(Application.invoiceSearchInfo.getSyukkaDate());

            // 前回条件でリスト構築
            InvoiceSearchInfo searchInfo = new InvoiceSearchInfo(
                    Application.invoiceSearchInfo.getInvoiceNo(),
                    Application.invoiceSearchInfo.getShimukeChi(),
                    Application.invoiceSearchInfo.getListHenshinDate(),
                    Application.invoiceSearchInfo.getSyukkaDate(),
                    Application.invoiceSearchInfo.getHorei(),
                    Application.invoiceSearchInfo.getKikenhinKbn(),
                    Application.invoiceSearchInfo.getYusoKbn());
            pickedInvoiceSearch(searchInfo);
        }
    }

    @Override
    public void onItemClick(PickedInvoiceSearchViewHolder holder) {
        invoiceAdapter.setSelectedPosition(holder.getAdapterPosition());
        invoiceAdapter.notifyDataSetChanged();
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
     * 検索ボタン押下
     */
    private void onSearchButtonClick() {

        // 検索条件入力チェック
        String invoiceNo = mBinding.invoiceNoText.getText().toString();
        CategoryInfo destinationSp = (CategoryInfo)mBinding.destinationSpinner.getSelectedItem();
        String shimukeChi = destinationSp.getElementName();
        String pickingDeadLine = mBinding.txtPackingdeadline.getText().toString();
        String shipDate = mBinding.textviewShipDate.getText().toString();
        String coolTemp = mBinding.coolSpinner.getSelectedItem().toString();
        String dangerous = mBinding.spinnerDangerous.getSelectedItem().toString();
        CategoryInfo transportSp = (CategoryInfo) mBinding.spinnerTransport.getSelectedItem();
        String transport = transportSp.getElement();

        // 検索条件が全て未入力
        if( invoiceNo.isEmpty()
                && shimukeChi.isEmpty()
                && pickingDeadLine.isEmpty()
                && shipDate.isEmpty()
                && coolTemp.isEmpty()
                && dangerous.isEmpty()
                && transport.isEmpty()) {

            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2000));
            return;
        }

        // 検索条件セット
        InvoiceSearchInfo searchInfo = new InvoiceSearchInfo(invoiceNo, shimukeChi, pickingDeadLine
                , shipDate, coolTemp, dangerous, transport);

        // 検索実行
        pickedInvoiceSearch(searchInfo);
    }

    /**
     * ピッキング済みInvoice検索
     */
    private void pickedInvoiceSearch(InvoiceSearchInfo searchInfo) {
        try {
            // ピッキングInvoice検索
            JsonObject jsonObject = new JsonObject();
            Util.putPropertyStr(jsonObject, "invoiceNo", searchInfo.getInvoiceNo());
            Util.putPropertyStr(jsonObject, "shimukeChi", searchInfo.getShimukeChi());
            Util.putPropertyStr(jsonObject, "listHenshinDate", searchInfo.getListHenshinDate());
            Util.putPropertyStr(jsonObject, "syukkaDate", searchInfo.getSyukkaDate());
            Util.putPropertyStr(jsonObject, "horei", searchInfo.getHorei());
            Util.putPropertyStr(jsonObject, "kikenhinKbn", searchInfo.getKikenhinKbn());
            Util.putPropertyStr(jsonObject, "yusoKbn", searchInfo.getYusoKbn());
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_PICKEDINVOICE_SEARCH;
            new PickedInvoiceSearchTask(pickedInvoiceSearchCallback, url, jsonObject.toString()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
        }
    }

    /**
     * 出庫予定Invoice検索Callback
     */
    PickedInvoiceSearchTask.Callback<PickedInvoiceSearchResponse> pickedInvoiceSearchCallback = new PickedInvoiceSearchTask.Callback<PickedInvoiceSearchResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialogを表示する
            if (showProgress) {
                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0030));
            }
        }
        @Override
        public void onTaskFinished(PickedInvoiceSearchResponse response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();

            // エラーレスポンスの場合
            if (!Constants.API_RESPONSE_STATUS_CODE_OK.equals(response.getStatus())) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9001));
                Application.log.e(TAG, "status=" + response.getStatus() + " ,errorCode=" + response.getErrorCode());
                return;
            }

            // 検索結果ヒットなしの場合
            if (response.getData().getList().size() <= 0) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2001));
                return;
            }

            try {
                // 取得データDB登録
                new SyukkoShijiHeaderDao().bulkInsertPicked(Application.dbHelper.getWritableDatabase(), response.getData().getList());
            } catch (SQLiteException e) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
                Application.log.e(TAG, e);
                return;
            }

            // リスト更新
            searchListUpdate(response.getData().getList());
        }

        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // エラーメッセージを表示
            mUtilListener.showAlertDialog(messageId);
        }
    };

    // region [ BaseFragment Override ]
    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        // 検索条件削除
        Application.invoiceSearchInfo = null;
        // 呼び出し元の画面に戻る
        mListener.popBackStack();
    }

    /**
     * 明細行ソート
     */
    private void detailSorter() {

        // 明細未検索
        if( this.invoiceInfoList.size() <= 0 ) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2002));
            return;
        }

        ArrayList<String> fields = new ArrayList<>();
        for (Constants.SortChoices sortChoice : Constants.SortChoices.values()) {
            fields.add(sortChoice.getSortChoices());
        }
        final SortDialogFragment dialog = new SortDialogFragment(getContext(), fields);
        dialog.show(getActivity().getSupportFragmentManager(), "SortDialogFragment");
    }

    /**
     * 明細更新
     */
    public void searchListUpdate(ArrayList<PickedInvoiceSearchInfo> searchList) {
        // 明細更新
        this.invoiceInfoList.clear();
        for (PickedInvoiceSearchInfo item : searchList) {
            PickedInvoiceSearchInfo new_item = new PickedInvoiceSearchInfo(item.getInvoiceNo());
            new_item.setInvoiceNo(item.getInvoiceNo());
            new_item.setCustomerName(item.getCustomerName());
            new_item.setDestination(item.getDestination());
            new_item.setListReplyDesiredDate(item.getListReplyDesiredDate());
            new_item.setIssueDate(item.getIssueDate());
            new_item.setLineCount(item.getLineCount());
            new_item.setTransportTemprature(new CategoryMasterDao().getCategory(Application.dbHelper.getReadableDatabase(),"EYUSOONDO",item.getTransportTemprature()).getElementName());
            new_item.setDangerousGoods((item.getDangerousGoods() == null || item.getDangerousGoods().isEmpty()) ? "" : "✔");
            new_item.setRemarks(item.getRemarks());
            new_item.setKonpoHoldFlag(item.getKonpoHoldFlag().equals("0") ? "" : "✔");
            this.invoiceInfoList.add(new_item);
        }
        invoiceAdapter.notifyDataSetChanged();

        // 結果数更新
        mBinding.detailCount.setText(String.valueOf(invoiceAdapter.getItemCount()));
    }
    // endregion

    /**
     * ソート画面の設定クリック時処理
     * @param dialog
     * @param order     昇順：0　降順：1
     * @param sortKey   ソート項目
     */
    @Override
    public void onDialogSettingClick(DialogFragment dialog, int order, String sortKey){
        String sortOrder = order == 0 ? "ASC" : "DESC";
        String orderKey = Constants.SortChoices.getSortKey(sortKey).toString();

        // 検索実行
        SyukkoShijiHeaderDao dao = new SyukkoShijiHeaderDao();
        // リスト更新
        searchListUpdate(dao.getSyukkoShijiSortListPicked(Application.dbHelper.getWritableDatabase(), orderKey, sortOrder));
    }

    /**
     * データ受信ボタン押下
     */
    private void onDataReceiveClick() {

        // 明細未検索
        if( this.invoiceInfoList.size() <= 0 ) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2002));
            return;
        }

        // 明細未選択
        int index = invoiceAdapter.getSelectedPosition();
        if( index == -1) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2003));
            return;
        }

        // 検索条件保存
        CategoryInfo destinationSp = (CategoryInfo)mBinding.destinationSpinner.getSelectedItem();
        CategoryInfo transportSp = (CategoryInfo) mBinding.spinnerTransport.getSelectedItem();
        Application.invoiceSearchInfo = new InvoiceSearchInfo(
                mBinding.invoiceNoText.getText().toString(),
                destinationSp.getElementName(),
                mBinding.txtPackingdeadline.getText().toString(),
                mBinding.textviewShipDate.getText().toString(),
                mBinding.coolSpinner.getSelectedItem().toString(),
                mBinding.spinnerDangerous.getSelectedItem().toString(),
                transportSp.getElement()
        );

        // 梱包アウターテーブルクリア
        new KonpoOuterDao().upgradeTable(Application.dbHelper.getWritableDatabase());

        // 次画面遷移
        Application.scanOffBooklList = null;
        mListener.replaceFragmentWithStack(PackingListDisplayFragment.newInstance(
                this.invoiceInfoList.get(index).getInvoiceNo(), true),  TAG);
    }
}

