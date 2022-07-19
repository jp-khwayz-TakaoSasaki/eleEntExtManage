package jp.co.khwayz.eleEntExtManage;

import android.view.View;

public interface MainActivityInterface {
    void setGroupsVisibility(boolean isHeader, boolean isSubHeader, boolean isFooter);
    void setSubTitleText(String text);
    void setFooterButton(ButtonInfo buttonInfo);
    void setFooterButton(ButtonInfo buttonInfo1, ButtonInfo buttonInfo2);
    void setFooterButton(ButtonInfo b1, ButtonInfo b2, ButtonInfo b3, ButtonInfo b4, ButtonInfo b5);
    void setRightFooterButton(ButtonInfo b1, ButtonInfo b2, ButtonInfo b3, ButtonInfo b4, ButtonInfo b5);
    void setFooterButton3(ButtonInfo b);
    void changeFooterButtonLabel(int buttonNumber);
    void setBackButton(View.OnClickListener clickListener);
    void setBatteryButton(View.OnClickListener clickListener, int visibility);
    void setLoginId2( int visibility);
    void connected();
    void setLoginId(String loginId);
    void setFooterButton(ButtonInfo buttonInfo1, ButtonInfo buttonInfo2,ButtonInfo buttonInfo3);
    void setlogo2(int visibility);
    void setlogo(int visibility);
    void setScreenId(String id);
}
