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
import jp.co.khwayz.eleEntExtManage.packing.task.MultiplePackingRegistTask;

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
     * ?????????????????????
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
        mListener.setSubHeaderExplanationText("");
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

        // ???????????????
        mListener.setGroupsVisibility(true, true, true);
        mListener.setFooterButton(null, null, null, null, null);

        // ???????????????????????????????????????????????????
        mListener.setReaderConnectButtonVisibility(View.INVISIBLE);
        mListener.setBatteryStateImageVisibility(View.VISIBLE);

        // ????????????
        mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.packing_multiple_packing));
        mListener.setScreenId(getString(R.string.screen_id_packing_multiple));
        mListener.setSubHeaderExplanationText(getString(R.string.multiple_packing_explanation));

        // ????????????????????????
        mPackingMaterialsDataSet = mUtilListener.getCategoryList(Constants.KBN_EKONPOSHIZAI);;
        CategoryInfo topValue = new CategoryInfo("","");
        mPackingMaterialsDataSet.add(0, topValue);
        mPackingMaterialSpinner_1 = new CategorySpinnerAdapter(getContext(), mPackingMaterialsDataSet);
        mPackingMaterialSpinner_2 = new CategorySpinnerAdapter(getContext(), mPackingMaterialsDataSet);
        mBinding.spOverPackPackingMaterial1.setAdapter(mPackingMaterialSpinner_1);
        mBinding.spOverPackPackingMaterial2.setAdapter(mPackingMaterialSpinner_2);

        // ???????????????????????????
        this.mOverPackList = mBinding.overPackList;
        if(this.mOverPackInfoList == null) {
            this.mOverPackInfoList = new ArrayList<>();
        }

        // ???????????????????????????
        this.mOverPackListInfoAdapter = new OverPackListInfoRecyclerViewAdapter(this.mOverPackInfoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mOverPackList.setHasFixedSize(true);
        this.mOverPackList.setLayoutManager(llm);
        this.mOverPackList.setAdapter(mOverPackListInfoAdapter);
        mOverPackListInfoAdapter.setOnItemClickListener(this);

        // ?????????????????????????????????????????????
        overPackNo = getView().findViewById(R.id.tv_over_pack_no_value);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        /*************************************/
        // ?????????????????????
        /*************************************/
        // ?????????????????????
        View.OnClickListener backClickListener = v -> {
            onBackButtonClick(mBinding.getRoot());
        };
        mListener.setBackButton(backClickListener);

        // ???????????????
        ButtonInfo confirmButtonInfo = new ButtonInfo(getString(R.string.confirm), v -> onConfirmClick());
        mListener.setFooterButton(null, null, confirmButtonInfo, null, null);

        /*************************************/
        // ??????????????????????????????
        /*************************************/
        // ???????????????
        this.overPackDelete= getView().findViewById(R.id.bt_over_pack_delete);
        this.overPackDelete.setOnClickListener(v -> deleteOverPack());
    }

    @Override
    public void mainSetting() {
        super.mainSetting();

        // ?????????????????????
        listCreate();
    }

    @Override
    public void onItemClick(OverPackListInfoViewHolder holder) {
        int selectedPostion = holder.getAdapterPosition();

        // ?????????????????????
        mOverPackListInfoAdapter.setSelectedPosition(selectedPostion);
        mOverPackListInfoAdapter.notifyDataSetChanged();

        String updateOverPackNo = this.mOverPackInfoList.get(selectedPostion).getOverPackNo();
        // ?????????????????????????????????????????????
        overPackNo.setText(updateOverPackNo == null ?
                null : updateOverPackNo);

        // ??????????????????
        mBinding.spOverPackPackingMaterial1.setSelection(0);
        mBinding.spOverPackPackingMaterial2.setSelection(0);
        if(updateOverPackNo != null){
            // ??????????????????????????????
            ArrayList<OverPackKonpoShizaiInfo> konpoShizaiList = new OverpackKonpoShizaiDao().getOverPackKonpoShizaiListByOverPackNo(
                    Application.dbHelper.getWritableDatabase(), mInvoiceNo, updateOverPackNo);
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
     * ?????????????????????
     * @param view : ??????????????????????????????
     */
    @Override
    protected void onBackButtonClick(View view) {
        // ??????????????????
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // ?????????????????????????????????
            mListener.popBackStack();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0015, listener);
    }

    /**
     * ??????????????????????????????
     */
    private void onConfirmClick(){
        // ???????????????????????????
        OverPackListInfo item = mOverPackInfoList.stream().filter(v -> v.getOverPackNo() != null).findFirst().orElse(null);
        if(item != null){
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2012));
            return;
        }

        // ??????????????????
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // ***************************
            // ??????????????????????????????????????????
            // ***************************
            int newOverPackNo = new SyukkoShijiDetailDao().getMaxOverPackNo(Application.dbHelper.getWritableDatabase());

            // ***************************
            // ????????????????????????????????????????????????
            // ***************************
            ArrayList<OverPackKonpoShizaiInfo> konpoShizaiList = new ArrayList<>();
            if(mBinding.spOverPackPackingMaterial1.getSelectedItemPosition() != 0){
                CategoryInfo info = (CategoryInfo) mBinding.spOverPackPackingMaterial1.getSelectedItem();
                OverPackKonpoShizaiInfo item1 = new OverPackKonpoShizaiInfo(mInvoiceNo, newOverPackNo,1
                        ,info.getElementName()
                );
                konpoShizaiList.add(item1);
            }
            if(mBinding.spOverPackPackingMaterial2.getSelectedItemPosition() != 0){
                CategoryInfo info = (CategoryInfo) mBinding.spOverPackPackingMaterial2.getSelectedItem();
                OverPackKonpoShizaiInfo item2 = new OverPackKonpoShizaiInfo(mInvoiceNo, newOverPackNo,2
                        ,info.getElementName()
                );
                konpoShizaiList.add(item2);
            }

            // ????????????????????????
            new MultiplePackingRegistTask(multiplePackingRegistCallBack, mInvoiceNo, newOverPackNo
                    , mOverPackInfoList, konpoShizaiList).execute();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0014, listener
                , mOverPackInfoList.size());
    }

    MultiplePackingRegistTask.Callback multiplePackingRegistCallBack = new MultiplePackingRegistTask.Callback() {
        @Override
        public void onPreExecute() {
            mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0027));
        }

        @Override
        public void onTaskFinished(boolean result) {
            // ProgressDialog????????????
            mUtilListener.dismissProgressDialog();
            // ???????????????????????????
            Application.initFlag = true;
            // ?????????????????????????????????
            mListener.popBackStack();
            // ?????????????????????
            mUtilListener.showSnackBarOnUiThread(mUtilListener.getDataBaseMessage(R.string.info_message_I0007));
        }

        @Override
        public void onError() {
            // ProgressDialog????????????
            mUtilListener.dismissProgressDialog();
            // ?????????????????????????????????
            mUtilListener.showAlertDialog(getString(R.string.const_err_message_E9000));
        }
    };

    /**
     * ???????????????????????????
     */
    private void deleteOverPack() {
        // ????????????
        String deleteOverPackNo = overPackNo.getText().toString();

        // ???????????????
        if(deleteOverPackNo.isEmpty()) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2027));
            return;
        }

        // ????????????????????????
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // ???????????????????????????
            releaseOverPackNo();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0021, listener
                , String.valueOf(overPackNo.getText().toString()));
    }

    /**
     * ???????????????????????????
     */
    private void releaseOverPackNo(){
        // ????????????
        String deleteOverPackNo = overPackNo.getText().toString();

        // ???????????????????????????????????????????????????????????????????????????
        new SyukkoShijiDetailDao().releaseOverPackNo(Application.dbHelper.getWritableDatabase(), mInvoiceNo, deleteOverPackNo);

        // ???????????????????????????????????????????????????????????????
        new OverpackKonpoShizaiDao().deleteByOverPackNo(Application.dbHelper.getWritableDatabase(), mInvoiceNo, deleteOverPackNo);

        // ??????????????????
        mOverPackInfoList.forEach(list -> updateOverPackNo(list, deleteOverPackNo));
        mOverPackListInfoAdapter.notifyDataSetChanged();
        overPackNo.setText(null);

        // ??????????????????
        mBinding.spOverPackPackingMaterial1.setSelection(0);
        mBinding.spOverPackPackingMaterial2.setSelection(0);

        // ?????????????????????
        mUtilListener.showSnackBarOnUiThread(mUtilListener.getDataBaseMessage(R.string.info_message_I0007));
    }

    /**
     * ????????????????????????????????????
     */
    private void updateOverPackNo(OverPackListInfo listInfo, String updateNo){
        if(listInfo.getOverPackNo() == null){
            return;
        }
        if(listInfo.getOverPackNo().equals(updateNo)){
            listInfo.setOverPackNo(null);
        }
    }

    /**
     * ????????????????????????????????????????????????
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
     * ???????????????
     */
    public void listCreate(){
        // ???????????????????????????
        this.mOverPackInfoList.clear();
        mOverPackInfoList.addAll(new SyukkoShijiDetailDao().getSyukkoShijiListByOverPackInfo(
                Application.dbHelper.getWritableDatabase(), mInvoiceNo, mlineNoList));
        mOverPackListInfoAdapter.notifyDataSetChanged();
    }
}
