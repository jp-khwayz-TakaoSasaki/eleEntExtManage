package jp.co.khwayz.eleEntExtManage;

import android.content.Context;

public class Common {
    public static boolean isTablet(Context context) {
//        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
        return true;
    }

    public static boolean isReceiptApp(Context context) {
        // とりあえずタブレットが出庫アプリでそれ以外が入庫アプリにしておく
        return !isTablet(context);
    }
}
