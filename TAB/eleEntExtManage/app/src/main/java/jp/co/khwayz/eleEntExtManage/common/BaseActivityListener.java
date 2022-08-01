package jp.co.khwayz.eleEntExtManage.common;

import android.content.DialogInterface;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;

public interface BaseActivityListener {

    /**
     * AlertDialog表示
     * @param message : 表示するメッセージ
     */
    void showAlertDialog(String message);

    /**
     * AlertDialog表示
     * @param msgId : リソースID
     * @param option : メッセージに埋め込む文字列
     */
    void showAlertDialog(int msgId, Object... option);

    /**
     * 確認Dialog表示
     * @param msgId : リソースID
     * @param listener : positiveButtonのClick Listener
     * @param option : メッセージに埋め込む文字列
     */
    void showConfirmDialog(int msgId, DialogInterface.OnClickListener listener, Object... option);

    /**
     * 確認Dialog表示（キャンセルリスナーあり）
     * @param msgId : リソースID
     * @param positiveListener : positiveButtonのClick Listener
     * @param negativeListener : negativeButtonのClick Listener
     * @param option : メッセージに埋め込む文字列
     */
    void showConfirmDialog(int msgId, DialogInterface.OnClickListener positiveListener
            ,DialogInterface.OnClickListener negativeListener, Object... option);

    /**
     * 情報Dialog表示
     * @param msgId : リソースID
     * @param listener : positiveButtonのClick Listener
     * @param option : メッセージに埋め込む文字列
     */
    void showInformationDialog(int msgId, DialogInterface.OnClickListener listener, Object... option);

    /**
     * 情報Dialog表示
     * @param message : 表示するメッセージ
     * @param listener : positiveButtonのClick Listener
     */
    void showInformationDialog(String message, DialogInterface.OnClickListener listener);

    /**
     * AlertDialogを閉じる
     */
    void dismissAlertDialog();

    /**
     *
     * @param msgId
     */
    void showProgressDialog(int msgId);
    /**
     * ProgressDialogのメッセージを表示する
     * @param message : 表示するメッセージ
     */
    void showProgressDialog(String message);

    /**
     * ProgressDialogを表示する
     * @param message : 表示するメッセージ
     */
    void setProgressMessage(String message);

    /**
     * ProgressDialogを閉じる
     */
    void dismissProgressDialog();

    /**
     * SQLiteからメッセージ取得
     * @param msgId : リソースID
     * @return メッセージマスタから取得したメッセージ
     */
    String getDataBaseMessage(int msgId);

    /**
     * 区分マスタ取得
     * @param category : 区分
     * @return 取得した区分マスタList
     */
    ArrayList<CategoryInfo> getCategoryList(String category);

    /**
     * 区分マスタ取得
     * @return 取得した区分マスタList
     */
    ArrayList<CategoryInfo> getTransportListDistinct();
    /**
     * SnackBarを表示
     * @param messageId : 表示するメッセージリソースID
     * @param option : メッセージに埋め込むOption
     */
    void showSnackBarOnUiThread(final int messageId, Object... option);

    /**
     * SnackBarを表示します
     * @param message : 表示するメッセージ文字列
     */
    void showSnackBarOnUiThread(String message);
}
