package jp.co.khwayz.eleEntExtManage;

import android.view.View;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ButtonInfo {
    private String text;
    private View.OnClickListener clickListener;
    public ButtonInfo(String text, View.OnClickListener clickListener) {
        this.text = text;
        this.clickListener = clickListener;
    }
}
