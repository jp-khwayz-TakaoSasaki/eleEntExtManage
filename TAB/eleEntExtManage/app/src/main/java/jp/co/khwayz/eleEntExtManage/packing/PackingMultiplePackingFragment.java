package jp.co.khwayz.eleEntExtManage.packing;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.densowave.scannersdk.Const.CommConst;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.CategorySpinnerAdapter;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.common.models.OverPackKonpoShizaiInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoShijiKeyInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.OverpackKonpoShizaiDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPackingMultiplePackingBinding;

public class PackingMultiplePackingFragment extends BaseFragment implements OverPackListInfoRecyclerViewAdapter.OnItemClickListener{
    // DataBinding
    FragmentPackingMultiplePackingBinding mBinding;

    // Adapter
    private CategorySpinnerAdapter mPackingMaterialSpinner_1;
    private CategorySpinnerAdapter mPackingMaterialSpinner_2;
    OverPackListInfoRecyclerViewAdapter mOverPackListInfoAdapter;

    // List
    private RecyclerView mOverPackList;
    private List<OverPackListInfo> mOverPackInfoList;
    List<CategoryInfo> mPackingMaterialsDataSet;

    private TextView overPackNo;
    private Button overPackDelete;

    // ARGS
    private static final String ARGS_INVOICE_NO = "invoice_no";
    private static final String ARGS_LINE_NO_LIST = "lineNoList";
    private static String mInvoiceNo;
    private static ArrayList<SyukkoShijiKeyInfo> mlineNoList;

    /**
     * コンストラクタ
     * @param invoiceNo
     * @param lineNno
     * @return
     */
    public static PackingMultiplePackingFragment newInstance(String invoiceNo, ArrayList<SyukkoShijiKeyInfo> lineNno){
        PackingMultiplePackingFragment fragment = new PackingMultiplePackingFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_INVOICE_NO, invoiceNo);
        args.putParcelableArrayList(ARGS_LINE_NO_LIST, lineNno);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentPackingMultiplePackingBinding.inflate(inflater, container, false);
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
    public void onDestroy() {
        super.onDestroy();
        mOverPackList = null;
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
        mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.packing_multiple_packing));
        mListener.setScreenId(getString(R.string.screen_id_packing_multiple));
        mListener.setSubHeaderExplanationText(getString(R.string.multiple_packing_explanation));

        // 梱包資材スピナー
        mPackingMaterialsDataSet = mUtilListener.getCategoryList(Constants.KBN_EKONPOSHIZAI);;
        CategoryInfo topValue = new CategoryInfo("","");
        mPackingMaterialsDataSet.add(0, topValue);
        mPackingMaterialSpinner_1 = new CategorySpinnerAdapter(getContext(), mPackingMaterialsDataSet);
        mPackingMaterialSpinner_2 = new CategorySpinnerAdapter(getContext(), mPackingMaterialsDataSet);
        mBinding.spOverPackPackingMaterial1.setAdapter(mPackingMaterialSpinner_1);
        mBinding.spOverPackPackingMaterial2.setAdapter(mPackingMaterialSpinner_2);

        // リストオブジェクト
        this.mOverPackList = mBinding.overPackList;
        if(this.mOverPackInfoList == null) {
            this.mOverPackInfoList = new ArrayList<>();
        }

        // リストアダプタ生成
        this.mOverPackListInfoAdapter = new OverPackListInfoRecyclerViewAdapter(this.mOverPackInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mOverPackList.setHasFixedSize(true);
        this.mOverPackList.setLayoutManager(llm);
        this.mOverPackList.setAdapter(mOverPackListInfoAdapter);
        mOverPackListInfoAdapter.setOnItemClickListener(this);

        // サブリスト：オーバーパック番号
        overPackNo = getView().findViewById(R.id.tv_over_pack_no_value);
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
        ButtonInfo confirmButtonInfo = new ButtonInfo(getString(R.string.confirm), v -> onConfirmClick());
        mListener.setFooterButton(null, null, confirmButtonInfo, null, null);

        /*************************************/
        // イベントリスナー登録
        /*************************************/
        // 取消ボタン
        this.overPackDelete= getView().findViewById(R.id.bt_over_pack_delete);
        this.overPackDelete.setOnClickListener(v -> deleteOverPack());
    }

    @Override
    public void mainSetting() {
        super.mainSetting();

        // リスト情報構築
        listCreate();
    }

    @Override
    public void onItemClick(OverPackListInfoViewHolder holder) {
        int selectedPostion = holder.getAdapterPosition();

        // 選択位置セット
        mOverPackListInfoAdapter.setSelectedPosition(selectedPostion);
        mOverPackListInfoAdapter.notifyDataSetChanged();

        // 削除対象オーバーパック番号更新
        overPackNo.setText(this.mOverPackInfoList.get(selectedPostion).getOverPackNo().equals("0") ?
                "" : this.mOverPackInfoList.get(selectedPostion).getOverPackNo());

        // 梱包資材更新
        mBinding.spOverPackPackingMaterial1.setSelection(0);
        mBinding.spOverPackPackingMaterial2.setSelection(0);
        if(!this.mOverPackInfoList.get(selectedPostion).getOverPackNo().isEmpty()){
            // 登録済み梱包資材取得
            ArrayList<OverPackKonpoShizaiInfo> konpoShizaiList = new OverpackKonpoShizaiDao().getOverPackKonpoShizaiListByOverPackNo(
                    Application.dbHelper.getWritableDatabase(), mInvoiceNo, this.mOverPackInfoList.get(selectedPostion).getOverPackNo());
            if(konpoShizaiList.size() >= 1) {
                int packingMaterialIndex = getSpinnerSelectPosition(konpoShizaiList.get(0).getPackingMaterial());
                mBinding.spOverPackPackingMaterial1.setSelection(packingMaterialIndex);
                if(konpoShizaiList.size() >= 2) {
                    packingMaterialIndex = getSpinnerSelectPosition(konpoShizaiList.get(1).getPackingMaterial());
                    mBinding.spOverPackPackingMaterial2.setSelection(packingMaterialIndex);
                }
            }
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
        mUtilListener.showConfirmDialog(R.string.info_message_I0015, listener);
    }

    /**
     * 確定ボタン押下時処理
     */
    private void onConfirmClick(){
        // 複数梱包設定行あり
        OverPackListInfo item = mOverPackInfoList.stream().filter(v -> !v.getOverPackNo().isEmpty()).findFirst().orElse(null);
        if(item != null){
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2012));
            return;
        }

        // リスナー生成
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // 複数梱包確定処理
            completeMultiplePacking();

            // スキャン画面初期化
            Application.initFlag = true;

            // 呼び出し元の画面に戻る
            mListener.popBackStack();

            // メッセージ表示
            mUtilListener.showSnackBarOnUiThread(mUtilListener.getDataBaseMessage(R.string.info_message_I0007));
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0014, listener
                , mOverPackInfoList.size());
    }

    /**
     * 複数梱包確定
     */
    private void completeMultiplePacking(){

        // ***************************
        // オーバーパック番号最大値取得
        // ***************************
        int maxOverPackNo = new SyukkoShijiDetailDao().getMaxOverPackNo(Application.dbHelper.getWritableDatabase());

        // ***************************
        // オーバーパック番号設定
        // ***************************
        ArrayList<OverPackListInfo> updateList = new ArrayList<>(mOverPackInfoList);
        new SyukkoShijiDetailDao().registOverPackNo(Application.dbHelper.getWritableDatabase()
                ,maxOverPackNo
                ,mInvoiceNo
                ,updateList
        );

        // ***************************
        // 梱包資材テーブル登録
        // ***************************
        ArrayList<OverPackKonpoShizaiInfo> konpoShizaiList = new ArrayList<>();
        if(mBinding.spOverPackPackingMaterial1.getSelectedItemPosition() != 0){
            CategoryInfo info = (CategoryInfo) mBinding.spOverPackPackingMaterial1.getSelectedItem();
            OverPackKonpoShizaiInfo item1 = new OverPackKonpoShizaiInfo(mInvoiceNo, maxOverPackNo,1
                    ,info.getElementName()
            );
            konpoShizaiList.add(item1);
        }
        if(mBinding.spOverPackPackingMaterial2.getSelectedItemPosition() != 0){
            CategoryInfo info = (CategoryInfo) mBinding.spOverPackPackingMaterial2.getSelectedItem();
            OverPackKonpoShizaiInfo item2 = new OverPackKonpoShizaiInfo(mInvoiceNo, maxOverPackNo,2
                    ,info.getElementName()
            );
            konpoShizaiList.add(item2);
        }
        if(konpoShizaiList.size() > 0){
            new OverpackKonpoShizaiDao().insert(Application.dbHelper.getWritableDatabase(), konpoShizaiList);
        }

    }

    /**
     * オーバーパック取消
     */
    private void deleteOverPack() {

        // 複数梱包あり
        if (!overPackNo.getText().toString().isEmpty()) {

            // メッセージを表示
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                // オーバーパック解除
                releaseOverPackNo();
            };
            mUtilListener.showConfirmDialog(R.string.info_message_I0021, listener
                    , String.valueOf(overPackNo.getText().toString()));
        }
    }

    /**
     * オーバーパック解除
     */
    private void releaseOverPackNo(){
        // 対象番号
        String deleteOverPackNo = overPackNo.getText().toString();

        // 出庫指示明細テーブル更新（オーバーパック番号削除）
        new SyukkoShijiDetailDao().releaseOverPackNo(Application.dbHelper.getWritableDatabase(), mInvoiceNo, deleteOverPackNo);

        // オーバーパック梱包資材テーブルレコード削除
        new OverpackKonpoShizaiDao().deleteByOverPackNo(Application.dbHelper.getWritableDatabase(), mInvoiceNo, deleteOverPackNo);

        // 画面から消去
        mOverPackInfoList.forEach(list -> updateOverPackNo(list, deleteOverPackNo));
        mOverPackListInfoAdapter.notifyDataSetChanged();
        overPackNo.setText("");

        // 梱包資材更新
        mBinding.spOverPackPackingMaterial1.setSelection(0);
        mBinding.spOverPackPackingMaterial2.setSelection(0);

        // メッセージ表示
        mUtilListener.showSnackBarOnUiThread(mUtilListener.getDataBaseMessage(R.string.info_message_I0007));
    }

    /**
     * オーバーパック番号書換え
     */
    private void updateOverPackNo(OverPackListInfo listInfo, String updateNo){
        if(listInfo.getOverPackNo().equals(updateNo)){
            listInfo.setOverPackNo("");
        }
    }

    /**
     * 梱包資材スピナーインデックス取得
     * @param selectionValue
     * @return
     */
    private int getSpinnerSelectPosition(String selectionValue) {
        int retvalue = 0;
        for(CategoryInfo item : mPackingMaterialsDataSet){
            if(item.getElementName().equals(selectionValue)){
                return retvalue;
            }
            retvalue++;
        }
        return 0;
    }

    /**
     * 明細行構築
     */
    public void listCreate(){
        // スキャンリスト構築
        this.mOverPackInfoList.clear();
        mOverPackInfoList.addAll(new SyukkoShijiDetailDao().getSyukkoShijiListByOverPackInfo(
                Application.dbHelper.getWritableDatabase(), mInvoiceNo, mlineNoList));
        mOverPackListInfoAdapter.notifyDataSetChanged();
    }
}
