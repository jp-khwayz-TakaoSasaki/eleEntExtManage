package jp.co.khwayz.eleEntExtManage.setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.densowave.scannersdk.Common.CommException;
import com.densowave.scannersdk.Common.CommManager;
import com.densowave.scannersdk.Common.CommScanner;
import com.densowave.scannersdk.Dto.CommScannerParams;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.PairingReaderRecyclerAdapter;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.BaseActivity;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.databinding.ActivityPairingReaderBinding;
import jp.co.khwayz.eleEntExtManage.setting.task.PairingReaderTask;

/**
 * リーダー選択画面
 */
public class PairingReaderActivity extends BaseActivity implements PairingReaderTask.Callback {
    // region [ Constants ]
    public static final String TAG = PairingReaderActivity.class.getSimpleName();
    // endregion

    // region [ private variable ]
    private ActivityPairingReaderBinding mBinding;
    private PairingReaderRecyclerAdapter mListAdapter;
    // endregion

    //region [ Lifecycle ]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPairingReaderBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // 戻るボタン
        mBinding.backButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });

        // 接続ボタン
        mBinding.footerButtonPairing.setOnClickListener(v -> PairingButtonClick());

        //ペアリング済みデバイスを取得
        List<CommScanner> listCommScanner;
        listCommScanner = CommManager.getScanners();

        // ペアリングされていない場合
        if (listCommScanner == null || listCommScanner.size() == 0) {
            showSnackBarOnUiThread(R.string.err_message_E9006);
        }

        // リストの作成
        List<String> readerList = new ArrayList<>();
        if (listCommScanner != null) {
            for (CommScanner scanner: listCommScanner){
                readerList.add(scanner.getBTLocalName());
            }
        }

        /* RecyclerViewの設定 */
        mBinding.readerList.setHasFixedSize(true);
        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(this);
        mBinding.readerList.setLayoutManager(rvLayoutManager);
        mListAdapter = new PairingReaderRecyclerAdapter(readerList);
        mBinding.readerList.setAdapter(mListAdapter);

        // ログインIDを表示
        mBinding.loginIdText.setText(Application.userInfo.getUserId());

        // 画面IDを表示
        mBinding.programIdText.setText(getString(R.string.screen_id_pairing_reader));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListAdapter = null;
        mBinding = null;
    }
    // endregion

    // region [ private method ]
    /**
     * 接続ボタン押下
     */
    private void PairingButtonClick() {
        try {
            // 選択されたリーダー名取得
            String readerName = mListAdapter.getSelectedItem();
            // 未選択の場合
            if (readerName == null) {
                showSnackBarOnUiThread(R.string.err_message_E1006, Constants.MSG_OPTION_READER, Constants.MSG_OPTION_READER);
                return;
            }

            // 既に接続されている場合
            if (Application.commScanner != null) {
                // 選択されたリーダーに接続済みの場合
                if (Application.commScanner.getBTLocalName().equals(readerName)) {
                    showSnackBarOnUiThread(R.string.err_message_E9008);
                } else {
                    // 他のリーダーと接続されている
                    showSnackBarOnUiThread(R.string.err_message_E9009);
//                    // 接続されているリーダーとの接続を解除
//                    Application.disConnectReader();
                }
                return;
            }

            // ペアリング済みデバイスを取得
            List<CommScanner> listCommScanner;
            listCommScanner = CommManager.getScanners();

            // 目的のデバイスのインスタンスを取得
            CommScanner scanner = null;
            for (int i = 0; i < listCommScanner.size(); ++i) {
                if (listCommScanner.get(i).getBTLocalName().equals(readerName)) {
                    scanner = listCommScanner.get(i);
                    break;
                }
            }

            // リーダー接続
            new PairingReaderTask(this, scanner).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // ProgressDialogを閉じる
            dismissProgressDialog();
            // エラーメッセージを表示
            showAlertDialog(getString(R.string.err_message_E9000));
        }
    }
    // endregion

    // region [ task callback ]
    @Override
    public void onPreExecute() {
        // ProgressDialog表示
        showProgressDialog(getString((R.string.info_message_I1006)));
    }

    @Override
    public void onTaskFinished() {
        // ProgressDialogを閉じる
        dismissProgressDialog();
        try {
            // 設定値を反映
            CommScannerParams params = Application.commScanner.getParams();
            // Beep音設定
            params.notification.sound.buzzer = Application.scannerBuzzerEnable;
            Application.commScanner.setParams(params);
        } catch (CommException e) {
            e.printStackTrace();
        }

        // 完了通知
        showInformationDialog(R.string.info_message_I0008, (dialog, which) -> {
            // 画面終了
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public void onError() {
        // ProgressDialogを閉じる
        dismissProgressDialog();
        // エラーメッセージを表示
        showSnackBarOnUiThread(R.string.err_message_E9007);
    }
    // endregion
}