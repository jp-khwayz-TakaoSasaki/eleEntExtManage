package jp.co.khwayz.eleEntExtManage.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

public class MashingGuardCheckBox extends AppCompatCheckBox {
    private long mDelayMillis = 1500L;
    public MashingGuardCheckBox(@NonNull Context context) {
        super(context);
    }

    public MashingGuardCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MashingGuardCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setOnTouchListener(OnTouchListener listener) {
        super.setOnTouchListener((view, event) -> {
            view.setEnabled(false);
            new Handler().postDelayed(() -> view.setEnabled(true), mDelayMillis);
            listener.onTouch(view, event);
            return false;
        });
    }

    /**
     * Enable = falseにする時間を設定します。
     * @param delayMillis 遅延時間(ミリ秒)
     */
    public void setDelayMillis(long delayMillis) {
        this.mDelayMillis = delayMillis;
    }
}
