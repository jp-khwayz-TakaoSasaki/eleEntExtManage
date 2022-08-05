package jp.co.khwayz.eleEntExtManage.packing;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.densowave.scannersdk.Const.CommConst;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.PackingOuter;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.OuterInfo;
import jp.co.khwayz.eleEntExtManage.common.models.PickedInvoiceSearchInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoOuterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiHeaderDao;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentPackingListDisplayBinding;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.task.get.PackingRelatedInfoGetTask;
import jp.co.khwayz.eleEntExtManage.http.task.post.PostPackingDataRegistTask;
import jp.co.khwayz.eleEntExtManage.instr_cfm.CheckPackInstructionsFragment;
import jp.co.khwayz.eleEntExtManage.packing.task.PackingCancelTask;

/**
 * 梱包一覧表示画面
 */
public class PackingListDisplayFragment extends BaseFragment
        implements PackingListRecyclerViewAdapter.OnItemClickListener, PackingListRecyclerViewAdapter.CancelButtonClickCallback {

    // DataBinding
    FragmentPackingListDisplayBinding mBinding;

    // Adapter
    private PackingListRecyclerViewAdapter mPackingListAdapter;

    // List
    private RecyclerView mPackingOuterListRecycleView;
    private List<PackingOuter> mPackingOuterList;

    // ARGS
    private static final String ARGS_INVOICE_NO = "invoice_no";
    private static final String ARGS_INIT_FLAG = "init_flag";
    private static String mInvoiceNo;
    private static int mPosition;
    private static boolean mInitFlag;

    /**
     * インスタンス生成（インパラ取込）
     * @param invoiceNo
     * @param initFlag　初期化フラグ   true：初期化要   false：不要
     * @return
     */
    public static PackingListDisplayFragment newInstance(String invoiceNo, Boolean initFlag) {
        PackingListDisplayFragment fragment = new PackingListDisplayFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_INVOICE_NO, invoiceNo);
        args.putBoolean(ARGS_INIT_FLAG,  initFlag);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentPackingListDisplayBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mInvoiceNo = args.getString(ARGS_INVOICE_NO);
        this.mInitFlag = args.getBoolean(ARGS_INIT_FLAG);
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
        mListener.setSubTitleText(getString(R.string.packing) + getString(R.string.packing_list));
        mListener.setScreenId(getString(R.string.screen_id_packing_list));

        // サブヘッダセット
        SyukkoShijiHeaderDao dao = new SyukkoShijiHeaderDao();
        PickedInvoiceSearchInfo headerInfo = dao.getSyukkoShijiDataByInvoiceNo(Application.dbHelper.getWritableDatabase(), this.mInvoiceNo);
        mBinding.packingListViewInvoiceNo.setText(headerInfo.getInvoiceNo());
        mBinding.packingListViewDestination.setText(headerInfo.getDestination());
        mBinding.packingListViewReplyDate.setText(headerInfo.getListReplyDesiredDate());
        mBinding.packingListViewShipDate.setText(headerInfo.getIssueDate());

        // リストオブジェクト
        this.mPackingOuterListRecycleView = mBinding.packingList;
        if(this.mPackingOuterList == null) {
            this.mPackingOuterList = new ArrayList<>();
        }

        // リストアダプタ生成
        this.mPackingListAdapter = new PackingListRecyclerViewAdapter(this.mPackingOuterList, this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.mPackingOuterListRecycleView.setHasFixedSize(true);
        this.mPackingOuterListRecycleView.setLayoutManager(llm);
        this.mPackingOuterListRecycleView.setAdapter(mPackingListAdapter);
        this.mPackingListAdapter.setOnItemClickListener(this);
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        // 新規ボタン
        mBinding.packingListButtonNew.setOnClickListener(v -> newButton());

        /*************************************/
        // フッターボタンT
        /*************************************/
        // 戻るボタン設定
        View.OnClickListener backClickListener = view -> {
            onBackButtonClick(mBinding.getRoot());
        };
        mListener.setBackButton(backClickListener);

        // 指示内容確認ボタン
        View.OnClickListener instructionsClickListener = v -> instructionsButton();
        ButtonInfo instructions = new ButtonInfo("指示内容確認", instructionsClickListener);

        // 確定ボタン
        View.OnClickListener confirmClickListener = v -> confirmButton();
        ButtonInfo confirm = new ButtonInfo("確定", confirmClickListener);

        // 保留ボタン
        View.OnClickListener onHoldClickListener = v -> onHoldButton();
        ButtonInfo onHold = new ButtonInfo("保留", onHoldClickListener);

        // 修正ボタン
        View.OnClickListener replaceListener = v -> replaceButton();
        ButtonInfo replace = new ButtonInfo("修正", replaceListener);

        mListener.setFooterButton(null, instructions, confirm, onHold, replace);

        // 指示確認チェックボックス
        mListener.setSijikakuninCheck(View.VISIBLE);
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        //梱包Invoice検索画面からの遷移の場合
        if(mInitFlag) {
            // 指示内容確認チェックオフ
            mListener.setSijikakuninCheckOff();

            // 梱包関連情報取得
            packingRelatedInfoGet();
            mInitFlag = false;
        } else {
            // 画面リスト更新
            searchListUpdate();
        }
    }

    /**
     * 新規ボタンタップ時処理
     */
    private void newButton() {
        // 簿外リスト初期化
        Application.scanOffBooklList=null;

        // 梱包スキャン画面遷移
        mListener.replaceFragmentWithStack(PackingScanFragment.newInstance(this.mInvoiceNo, ""),  TAG);
    }

    /* 修正ボタン */
    private void replaceButton() {
        // 簿外リスト初期化
        Application.scanOffBooklList=null;
        // リスト選択行なし
        int selectPosition = mPackingListAdapter.getSelectedPosition();
        if(selectPosition == -1) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2003));
            return;
        }

        // 梱包スキャン画面遷移
        mListener.replaceFragmentWithStack(PackingScanFragment.newInstance(this.mInvoiceNo
                , mPackingOuterList.get(selectPosition).getNotationCno()),  TAG);
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
     * 戻るボタンタップ時処理
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        // リスナー生成
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // 呼び出し元の画面に戻る
            mListener.popBackStack();
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0016, listener);
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

    /* 確定ボタン */
    private void confirmButton() {
        if(mListener.getSijikakuninCheck()) {

            // 未梱包チェック
            if(new SyukkoShijiDetailDao().isPackingFinished(Application.dbHelper.getWritableDatabase(), mInvoiceNo)){
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2009));
                return;
            }

            // GW未入力チェック
            if(new KonpoOuterDao().isGwInputFinished(Application.dbHelper.getWritableDatabase(), mInvoiceNo)) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2010));
                return;
            }

            // 梱包情報登録
            // リスナー生成
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                // 梱包情報登録
                packingInfoRegist("0");
            };
            mUtilListener.showConfirmDialog(R.string.info_message_I0018, listener
                    , mInvoiceNo
                    , String.valueOf(new KonpoOuterDao().countOuterTable(Application.dbHelper.getWritableDatabase(), mInvoiceNo)));
        } else {
            // 指示内容未確認
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2023));
        }
    }

    /* 保留ボタン */
    private void onHoldButton() {

        // リスト件数チェック
        if(new KonpoOuterDao().countOuterTable(Application.dbHelper.getWritableDatabase(), mInvoiceNo) <= 0) {
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E2011));
            return;
        }

        // 梱包情報登録
        // リスナー生成
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // 梱包情報登録
            packingInfoRegist("1");
        };
        mUtilListener.showConfirmDialog(R.string.info_message_I0019, listener
                , mInvoiceNo
                , String.valueOf(new KonpoOuterDao().countOuterTable(Application.dbHelper.getWritableDatabase(), mInvoiceNo)));
    }

    /**
     * 梱包情報登録
     * @param type  0：完了登録  1：保留登録
     */
    private void packingInfoRegist(String type){
        try {
            // 梱包登録
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + Constants.API_ADDRESS_PACKING_INFO_REGIST;
            new PostPackingDataRegistTask(packingInfoRegistCallback, url, mInvoiceNo, type).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9000));
        }
    }

    /**
     * 梱包情報登録Callback
     */
    PostPackingDataRegistTask.Callback<SimpleResponse> packingInfoRegistCallback = new PostPackingDataRegistTask.Callback<SimpleResponse>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialogを表示する
            if (showProgress) {
                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0031));
            }
        }

        @Override
        public void onTaskFinished(SimpleResponse response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();

            // エラーレスポンスの場合
            if (!Constants.API_RESPONSE_STATUS_CODE_OK.equals(response.getStatus())) {
                mUtilListener.showAlertDialog(mUtilListener.getDataBaseMessage(R.string.err_message_E9001));
                Application.log.e(TAG, "status=" + response.getStatus() + " ,errorCode=" + response.getErrorCode());
                return;
            }

            // メッセージ表示
            mUtilListener.showSnackBarOnUiThread(mUtilListener.getDataBaseMessage(R.string.info_message_I0007));

            // 画面遷移
            mListener.popBackStack();
        }

        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // エラーメッセージを表示
            mUtilListener.showAlertDialog(messageId);
        }
    };

    /*
     * リストタップ時処理
     */
    @Override
    public void onItemClick(PackingListViewHolder holder) {
        PackingListRecyclerViewAdapter adapter = (PackingListRecyclerViewAdapter)this.mPackingOuterListRecycleView.getAdapter();
        int position = holder.getAdapterPosition();

        // 選択済み行の場合（選択解除）
        if(position == adapter.getSelectedPosition()) {
            position = -1;
        }

        // 単一選択
        adapter.setSelectedPosition(position);
        adapter.notifyDataSetChanged();
    }

    /**
     * 梱包一覧：取消ボタンタップ
     * @param position
     */
    @Override
    public void onClicked(int position) {
        PackingListRecyclerViewAdapter adapter = (PackingListRecyclerViewAdapter) this.mPackingOuterListRecycleView.getAdapter();
        // 梱包取消
        String csNo = mPackingOuterList.get(position).getNotationCno();
        cancelPacking(mInvoiceNo, csNo, position);
    }

    /**
     * アウター関連情報受信
     */
    private void packingRelatedInfoGet() {
        new PackingRelatedInfoGetTask(packingRelatedInfoGetCallback, mInvoiceNo).execute();
    }

    /**
     * 梱包関連情報全受信Callback
     */
    PackingRelatedInfoGetTask.Callback<Boolean> packingRelatedInfoGetCallback = new PackingRelatedInfoGetTask.Callback<Boolean>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialogを表示する
            if (showProgress) {
                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0030));
            }
        }

        @Override
        public void onTaskFinished(Boolean response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // 出庫指示明細テーブルのInvoice番号、行番号が、梱包インナーテーブルのInvoice番号、行番号と一致するレコードについて、
            // 単独梱包フラグを「１：単独梱包情報あり」に更新する。
            new SyukkoShijiDetailDao().updateSinglePackingFlag(Application.dbHelper.getWritableDatabase());

            // リスト更新
            searchListUpdate();
        }

        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // エラーメッセージを表示
            mUtilListener.showAlertDialog(messageId);
        }
    };

    /**
     * 梱包解除
     * @param invoiceNo
     * @param csNo
     */
    private void cancelPacking(String invoiceNo, String csNo, int position) {
        mPosition = position;
        new PackingCancelTask(packingCancelCallback, invoiceNo, csNo).execute();
    }

    /**
     * 梱包解除Callback
     */
    PackingCancelTask.Callback<Boolean> packingCancelCallback = new PackingCancelTask.Callback<Boolean>() {
        @Override
        public void onPreExecute(boolean showProgress) {
            // ProgressDialogを表示する
            if (showProgress) {
                mUtilListener.showProgressDialog(mUtilListener.getDataBaseMessage(R.string.info_message_I0026));
            }
        }

        @Override
        public void onTaskFinished(Boolean response) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();

            // リスト当該行削除
            mPackingOuterList.remove(mPosition);

            // リスト更新
            searchListUpdate();
        }

        @Override
        public void onError(int httpResponseStatusCode, int messageId) {
            // ProgressDialogを閉じる
            mUtilListener.dismissProgressDialog();
            // エラーメッセージを表示
            mUtilListener.showAlertDialog(messageId);
        }
    };

    /**
     * 明細行更新
     * 　端末の梱包アウターテーブルから当該INVOICE番号のレコードを表記CS番号でグルーピング取得して更新
     */
    public void searchListUpdate(){
        // 明細データ取得
        ArrayList<OuterInfo> list = new KonpoOuterDao().getOuterInfoListGroupByCsNo(Application.dbHelper.getWritableDatabase(), mInvoiceNo);
        // 明細更新
        this.mPackingOuterList.clear();
        for (OuterInfo item : list) {
            PackingOuter new_item = new PackingOuter(
                    item.getHyokiCsNumber(),
                    item.getSaisyuKonpoNisugata(),
                    item.getOuterLength() == null ? "" : String.format("%,.1f", item.getOuterLength()),
                    item.getOuterWidth() == null ? "" : String.format("%,.1f", item.getOuterWidth()),
                    item.getOuterHeight() == null ? "" : String.format("%,.1f", item.getOuterHeight()),
                    item.getNetWeight() == null ? "" : String.format("%,.3f", item.getNetWeight()),
                    item.getNifudaSu()
            );

            this.mPackingOuterList.add(new_item);
        }
        mPackingListAdapter.notifyDataSetChanged();
    }
}
