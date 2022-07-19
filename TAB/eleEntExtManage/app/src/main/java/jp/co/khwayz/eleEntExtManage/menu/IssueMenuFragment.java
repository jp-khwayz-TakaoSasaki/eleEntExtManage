package jp.co.khwayz.eleEntExtManage.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.densowave.scannersdk.Const.CommConst;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.cargostatus.CargoStatusCheckFragment;
import jp.co.khwayz.eleEntExtManage.casemark_paste.CaseMarkPasteReadFragment;
import jp.co.khwayz.eleEntExtManage.casemark_print.CaseMarkPrintFragment;
import jp.co.khwayz.eleEntExtManage.common.BaseFragment;
import jp.co.khwayz.eleEntExtManage.databinding.FragmentIssueMenuBinding;
import jp.co.khwayz.eleEntExtManage.dialog_fragment.MessageDialogFragment;
import jp.co.khwayz.eleEntExtManage.issueregist.IssueRegistrationInvoiceSearchFragment;
import jp.co.khwayz.eleEntExtManage.packing.PackingInvoiceSearchFragment;
import jp.co.khwayz.eleEntExtManage.picking.PickingInvoiceSearchFragment;
import jp.co.khwayz.eleEntExtManage.setting.IssueSettingFragment;

public class IssueMenuFragment extends BaseFragment {

    // DataBinding
    private FragmentIssueMenuBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentIssueMenuBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    // region [ BaseFragmentListener Override ]
    @Override
    public void viewSetting() {
        super.viewSetting();
        // ボタン設定
        mListener.setlogo2(View.INVISIBLE);
        mListener.setlogo(View.VISIBLE);
        mListener.setGroupsVisibility(true, true, true);
        mListener.setFooterButton(null, null, null, null, null);

        // リーダー接続アイコン・電池アイコン
        mListener.setReaderConnectButtonVisibility(View.INVISIBLE);
        mListener.setBatteryStateImageVisibility(View.VISIBLE);

        // タイトル
        mListener.setSubTitleText(getString(R.string.menu));
        mListener.setScreenId(getString(R.string.screen_id_menu));
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
        /*************************************/
        // フッターボタン
        /*************************************/
        // 戻るボタン設定
        View.OnClickListener clickListener = view -> {
            MessageDialogFragment dialog = new MessageDialogFragment(getContext());
            dialog.setMessage("ログアウトします。\nよろしいですか？");
            dialog.setPositiveButton(getString(R.string.ok), v -> {
                dialog.dismiss();
                onBackButtonClick(view);
            });
            dialog.setNegativeButton(getString(R.string.cancel), v -> dialog.dismiss());
            dialog.show(getActivity().getSupportFragmentManager(), "MessageDialogFragment");
        };
        mListener.setBackButton(clickListener);

        /*************************************/
        // イベントリスナー登録
        /*************************************/
        // ピッキングボタン
        mBinding.issueMenuButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.replaceFragmentWithStack(new PickingInvoiceSearchFragment(), TAG);
            }
        });

        // 梱包ボタン
        mBinding.issueMenuButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.replaceFragmentWithStack(new PackingInvoiceSearchFragment(), TAG);
            }
        });

        // ケースマーク印刷
        mBinding.issueMenuButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.replaceFragmentWithStack(new CaseMarkPrintFragment(), TAG);
            }
        });

        // ケースマーク貼付
        mBinding.issueMenuButtonCasemarkPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.replaceFragmentWithStack(new CaseMarkPasteReadFragment(), TAG);
            }
        });

        // 出庫登録
        mBinding.issueMenuButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.replaceFragmentWithStack(new IssueRegistrationInvoiceSearchFragment(), TAG);
            }
        });

        // 設定画面
        mBinding.issueMenuButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.replaceFragmentWithStack(new IssueSettingFragment(), TAG);
            }
        });

        // 貨物ステータス確認
        mBinding.issueMenuButtonCargo.setOnClickListener(view -> {
            mListener.replaceFragmentWithStack(new CargoStatusCheckFragment(), TAG);
        });
    }

    @Override
    public void CommStatusChanged(CommConst.ScannerStatus status) { }
    @Override
    public boolean hasScanner() {
        return false;
    }
    // endregion

    // region [ BaseFragment Override ]
    /**
     * 戻るボタン押下
     * @param view : クリックされたボタン
     */
    @Override
    protected void onBackButtonClick(View view) {
        // 呼び出し元の画面に戻る
        mListener.popBackStack();
    }

    @Override
    protected void openScanner() { }
    // endregion
}
