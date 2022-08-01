package jp.co.khwayz.eleEntExtManage.util;

import android.text.TextUtils;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.casemark_paste.CaseMarkPasteReadInfo;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;
import jp.co.khwayz.eleEntExtManage.common.models.TagInfo;

public class Util {
    private static final String TAG = Util.class.getSimpleName();

    /**
     * TextViewの値を取得します
     * @param textView 値を取得するTextView
     * @return TextViewの値
     */
    public static String getTextViewValue(TextView textView) {
        String result = null;
        Optional<CharSequence> opt = Optional.ofNullable(textView.getText());
        if (opt.isPresent()) {
            result = opt.get().toString();
        }
        return  result;
    }

    /**
     * 10進文字列かを判定
     * @param str 判定する文字列
     * @return true = 10進文字列, false = 10進文字列以外
     */
    public static boolean isNumber(String str) {
        if(TextUtils.isEmpty(str)) { return false; }
        return str.matches("^[0-9]+$");
    }

    public static boolean isDouble(String str) {
        if(TextUtils.isEmpty(str)) { return false; }
        return str.matches("\\d+($|(\\.($|\\d+$)))");
    }

    /**
     * 半角英数判定
     * @param str　判定する文字列
     * @return true = 半角英数字, false = 半角英数字以外が混在
     */
    public static boolean isAlphaNum(String str) {
        if(str != null) {
            return str.matches("^[a-zA-Z0-9]*$");
        }
        return false;
    }

    /**
     * 半角英数記号判定
     * @param str 判定する文字列
     * @return true = 半角英数記号, false = 半角英数記号以外が混在
     */
    public static boolean isAlphaNumSymbol(String str) {
        if(str != null) {
            return str.matches("^[a-zA-Z0-9!-/:-@\\[-`{-~]*$");
        }
        return false;
    }

    /**
     * hex⇒ASCII変換
     */
    public static String hexToASCII(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            if (output.equalsIgnoreCase("00")) {
                continue;
            }
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);
        }
        return sb.toString();
    }

    /**
     * 16進数 -> 10進数変換
     * @param hex : 変換する文字列
     * @return 10進数に変換した値
     */
    public static String hexToDecimal(String hex) {
        long decimal = Long.parseLong(hex, 16);
        return String.valueOf(decimal);
    }

    /**
     * byte -> ASCII変換
     * @param bytes バイト配列
     * @return ASCII文字列
     */
    public static String convertBytesToASCII(byte[] bytes) {
        StringBuilder data = new StringBuilder();
        for (byte b : bytes) {
            data.append(hexToASCII(String.format("%02X", b).trim()));
        }
        return data.toString().trim();
    }

    /**
     * バイト配列を16進文字列へ変換します。
     * @param bytes バイト配列
     * @return 16進文字列
     */
    public static String convertBytesToHexString(byte[] bytes){
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte value : bytes) {
            hexStringBuilder.append(String.format("%02X", value).trim());
        }
        return hexStringBuilder.toString();
    }

    /**
     * 文字列をbyteに変換
     * @param hex : 変換する文字列
     * @return : 変換したbyte配列
     */
    public static byte[] stringToByte(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int index = 0; index < bytes.length; index++) {
            bytes[index] = (byte) Integer.parseInt(hex.substring(index * 2, (index + 1) * 2), 16);
        }
        return bytes;
    }

    /**
     * EPCから荷札情報を作成する
     * @param uii : epcのbyte配列
     * @return 荷札情報クラス
     */
    public static TagInfo convertEpcToTagInfo(byte[] uii) {
        // epc変換
        String epc = convertBytesToHexString(uii);
        Application.log.d(TAG, epc);
        if (epc.length() != 24) { return null; }

        // 16進数 -> 10進数
        String splitValue;
        // 企業コード(32 / 4 = 8桁)
        splitValue = epc.substring(0, 8);
        String corporate = Util.hexToDecimal(splitValue);
        // 企業コードが違う場合は読み飛ばし
        if (!Application.corporateCode.equals(corporate)) { return null; }

        // 発注番号(36 / 4 = 9桁)
        splitValue = epc.substring(8, 17);
        String orderNo = Util.hexToDecimal(splitValue);
        // 枝番
        splitValue = epc.substring(17, 22);
        int branchNo;
        if (Util.isNumber(splitValue)) {
            branchNo = Integer.parseInt(splitValue);
        } else {
            // 数値以外は読み飛ばし
            return null;
        }
        // 枝番 = 0はありえないので読み飛ばし
        if (branchNo == 0) { return null; }

        // 読取不可フラグ
        splitValue = epc.substring(22, 23);
        String flag = Util.hexToDecimal(splitValue);
        // 読取不可の場合は読み飛ばし
        if (TextUtils.isEmpty(flag) || !flag.equals("0")) { return null; }

        // 荷札情報生成
        return new TagInfo(orderNo, branchNo);
    }

    /**
     * QRコードから荷札情報を作成する
     * @param qrCode : QRコード
     * @return 荷札情報クラス
     */
    @Nullable
    public static TagInfo convertQrCodeToTagInfo(@NotNull String qrCode) {
        String splitValue;
        // 企業コード(9桁)
        String corporate = qrCode.substring(0, 9);
        // 企業コードが違う場合は読み飛ばし
        if (!Application.corporateCode.equals(corporate)) { return null; }

        // 発注番号(10桁)
        String orderNo = qrCode.substring(9, 19);

        // 枝番
        splitValue = qrCode.substring(19, 24);
        // 数値チェック
        int branchNo;
        if (Util.isNumber(splitValue)) {
            branchNo = Integer.parseInt(splitValue);
        } else {
            // 数値以外は読み飛ばし
            return null;
        }
        // 枝番 = 0はありえないので読み飛ばし
        if (branchNo == 0) { return null; }

        // 読取不可フラグ
        String flag = qrCode.substring(24, 25);
        // 読取不可の場合は読み飛ばし
        if (TextUtils.isEmpty(flag) || !flag.equals("0")) { return null; }

        // 荷札情報生成
        return new TagInfo(orderNo, branchNo);
    }

    /**
     * QRコードからケースマーク貼付け読取情報を作成する
     * @param qrCode : QRコード
     * @return ケースマーク貼付け読取情報クラス
     */
    @Nullable
    public static CaseMarkPasteReadInfo convertQrCodeToCaseMarkPasteReadInfo(@NotNull String qrCode) {
        String splitValue;
        // 企業コード(9桁)
        String corporate = qrCode.substring(0, 9);
        // 企業コードが違う場合は読み飛ばし
        if (!Application.corporateCode.equals(corporate)) { return null; }

        // Invoice番号(15桁)：後方空白TRIM
        String orderNo = qrCode.substring(9, 24).trim();

        // ケースマーク番号（4桁）：先頭0サプレス
        splitValue = qrCode.substring(24, 28);

        // 数値チェック
        int caseMarkNo;
        if (Util.isNumber(splitValue)) {
            caseMarkNo = Integer.parseInt(splitValue);
        } else {
            // 数値以外は読み飛ばし
            return null;
        }
        // 枝番 = 0はありえないので読み飛ばし
        if (caseMarkNo == 0) { return null; }

        // 荷札情報生成
        return new CaseMarkPasteReadInfo(orderNo, caseMarkNo);
    }


    /**
     * スピナーインデックス取得（区分マスタ―用）
     * @param selectionValue
     * @param dataset
     * @return
     */
    public static int getSpinnerSelectPosition(List<CategoryInfo> dataset, String selectionValue) {
        int retvalue = 0;
        for(CategoryInfo item : dataset){
            if(item.getElement().equals(selectionValue)){
                return retvalue;
            }
            retvalue++;
        }
        return 0;
    }
}
