package jp.co.khwayz.eleEntExtManage.common.filter;

import android.text.InputFilter;
import android.text.Spanned;

public class AlphaNumericSingInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int d_start, int d_end) {
//        final String TEMP = "^[a-zA-Z0-9 -/:-@\\[-`{-~]+$";
        final String TEMP = "^[a-zA-Z0-9!-/:-@\\[-`{-~]+$";
        //英数字記号の場合
        if (source.toString().matches(TEMP)) {
            //入力値を問題なく返す
            return source;
        } else {
            //空白を返す
            return "";
        }
    }
}
