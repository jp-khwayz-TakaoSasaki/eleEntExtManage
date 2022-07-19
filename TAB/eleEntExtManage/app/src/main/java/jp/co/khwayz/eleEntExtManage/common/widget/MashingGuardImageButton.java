package jp.co.khwayz.eleEntExtManage.common.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

/**
 * 連打防止ImageButton
 */
public class MashingGuardImageButton extends AppCompatImageButton {
    private long mDelayMillis = 1500L;
    public MashingGuardImageButton(@NonNull Context context) {
        super(context);
    }

    public MashingGuardImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MashingGuardImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(final OnClickListener listener) {
        super.setOnClickListener(view -> {
            view.setEnabled(false);
            new Handler().postDelayed(() -> view.setEnabled(true), mDelayMillis);
            listener.onClick(view);
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
