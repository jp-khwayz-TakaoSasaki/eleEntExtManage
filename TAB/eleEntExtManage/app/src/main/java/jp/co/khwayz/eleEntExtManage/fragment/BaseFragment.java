package jp.co.khwayz.eleEntExtManage.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import jp.co.khwayz.eleEntExtManage.MainActivity;
import jp.co.khwayz.eleEntExtManage.Reader;
import jp.co.khwayz.eleEntExtManage.common.MainActivityListener;

public class BaseFragment extends Fragment implements FragmentInterface {
    public MainActivityListener mainInterface;
    public Reader reader;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mainInterface = (MainActivity) getActivity();
        mainInit();
        viewSetting();
        eventSetting();
        mainSetting();
    }

    private void mainInit() {
        this.mainInterface.setGroupsVisibility(true, true, true);
        this.mainInterface.setSubTitleText("");
        this.mainInterface.setSubHeaderExplanationText("");
        this.mainInterface.setFooterButton(null, null, null, null, null);
        this.mainInterface.setBackButton(null);
        this.mainInterface.setReaderConnectButtonVisibility( View.VISIBLE);
        this.mainInterface.setSijikakuninCheck(View.INVISIBLE);
    }

    @Override
    public void viewSetting() { }

    @Override
    public void eventSetting() { }

    @Override
    public void mainSetting() { }
}
