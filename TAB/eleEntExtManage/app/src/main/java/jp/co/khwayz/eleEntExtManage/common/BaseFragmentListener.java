package jp.co.khwayz.eleEntExtManage.common;

import com.densowave.scannersdk.Const.CommConst;

public interface BaseFragmentListener {

    /**
     * 初期処理
     */
    void viewSetting();
    void eventSetting();
    void mainSetting();

    /**
     * SP1接続状態の変化を通知します。
     * @param status 接続状態
     */
    void CommStatusChanged(CommConst.ScannerStatus status);

    /**
     * スキャナー使用プロパティ
     * @return true -> スキャナーあり, false -> スキャナー無し
     */
    boolean hasScanner();
}