package jp.co.khwayz.eleEntExtManage.common.filter;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数字規制Filter
 */
public class DecimalDigitsInputFilter implements InputFilter {
    private final Pattern mPattern;
    private static final String DIGITS_BEFORE_ZERO_DEFAULT = "7";
    private static final String DIGITS_AFTER_ZERO_DEFAULT = "3";

    public DecimalDigitsInputFilter(Integer digitsBeforeZero, Integer digitsAfterZero) {
        String mDigitsBeforeZero = (digitsBeforeZero != null ? String.valueOf(digitsBeforeZero) : DIGITS_BEFORE_ZERO_DEFAULT);
        String mDigitsAfterZero = (digitsAfterZero != null ? String.valueOf(digitsAfterZero) : DIGITS_AFTER_ZERO_DEFAULT);
        //mPattern = Pattern.compile("-?[0-9]{0," + mDigitsBeforeZero + "}+((\\.[0-9]{0," + mDigitsAfterZero + "})?)||(\\.)?");
        mPattern = Pattern.compile(String.format("-?[0-9]{0,%s}+((\\.[0-9]{0,%s})?)||(\\.)?", mDigitsBeforeZero, mDigitsAfterZero));
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int d_start, int d_end) {
        String replacement = source.subSequence(start, end).toString();
        String newVal = dest.subSequence(0, d_start).toString() + replacement + dest.subSequence(d_end, dest.length()).toString();
        Matcher matcher = mPattern.matcher(newVal);
        if (matcher.matches()) { return null; }
        if (TextUtils.isEmpty(source)) {
            return dest.subSequence(d_start, d_end);
        } else {
            return "";
        }
    }
}
