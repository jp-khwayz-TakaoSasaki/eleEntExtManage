package jp.co.khwayz.eleEntExtManage.common;

import android.view.View;

import androidx.fragment.app.Fragment;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;

public interface MainActivityListener {

    // viewSettings
    void setGroupsVisibility(boolean isHeader, boolean isSubHeader, boolean isFooter);
    void setSubTitleText(String text);
    void setSubHeaderExplanationText(String text);
    void setFooterButton(ButtonInfo buttonInfo);
    void setFooterButton(ButtonInfo buttonInfo1, ButtonInfo buttonInfo2);
    void setFooterButton(ButtonInfo b1, ButtonInfo b2, ButtonInfo b3, ButtonInfo b4, ButtonInfo b5);
    void changeFooterButtonLabel(int buttonNumber);
    void setBackButton(View.OnClickListener clickListener);
    void setReaderConnectButtonVisibility(final int visibility);
    void setBatteryStateImageVisibility(final int visibility);
    void setLoginId2( int visibility);
    void connected();
    void setLoginId(String loginId);
    void setFooterButton(ButtonInfo buttonInfo1, ButtonInfo buttonInfo2,ButtonInfo buttonInfo3);
    void setlogo2(int visibility);
    void setlogo(int visibility);
    void setScreenId(String id);
    void setSijikakuninCheck(int visibility);
    void setSijikakuninCheckOn();
    void setSijikakuninCheckOff();
    boolean getSijikakuninCheck();

    // 画面遷移
    void replaceFragment(Fragment fragment);
    void replaceFragmentWithStack(Fragment fragment, String stackName);
    void popBackStack();
    void popBackStack(String stackName);
    void clearBackStack();

    // 部品
    void showUserId();
    void showParingReaderActivity();
}
