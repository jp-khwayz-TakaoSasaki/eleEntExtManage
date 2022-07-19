package jp.co.khwayz.eleEntExtManage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;

public class Calender {
    public static void show(Activity activity, TextView textView) {
        //Calendarインスタンスを取得
        final java.util.Calendar date = java.util.Calendar.getInstance();
        //DatePickerDialogインスタンスを取得
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //setした日付を取得して表示
                        textView.setText(String.format("%d/%02d/%02d", year, month+1, dayOfMonth));
                    }
                },
                date.get(java.util.Calendar.YEAR),
                date.get(java.util.Calendar.MONTH),
                date.get(java.util.Calendar.DATE)
        );
        //dialogを表示
        datePickerDialog.show();
    }
}
