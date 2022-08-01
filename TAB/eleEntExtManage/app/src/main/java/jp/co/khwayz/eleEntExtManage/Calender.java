package jp.co.khwayz.eleEntExtManage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.widget.TextView;

import java.util.Locale;

public class Calender {
    public static void show(Activity activity, TextView textView) {
        //Calendarインスタンスを取得
        final java.util.Calendar date = java.util.Calendar.getInstance();

        //DatePickerDialogインスタンスを取得
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                (view, year, month, dayOfMonth) -> {
                    //setした日付を取得して表示
                    textView.setText(String.format(Locale.JAPAN, "%d/%02d/%02d", year, month + 1, dayOfMonth));
                },
                date.get(java.util.Calendar.YEAR),
                date.get(java.util.Calendar.MONTH),
                date.get(java.util.Calendar.DATE)
        );
        // 外側タップのキャンセル無効
        datePickerDialog.setCancelable(false);
        // ニュートラルボタンをクリアボタンに変更
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "クリア",
                (dialog, which) -> textView.setText(""));
        //dialogを表示
        datePickerDialog.show();
    }
}
