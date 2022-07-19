package jp.co.khwayz.eleEntExtManage.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jp.co.khwayz.eleEntExtManage.ButtonInfo;
import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.adapter.ReaderRecyclerViewAdapter;

public class ReaderSelectFragment extends BaseFragment {
    private RecyclerView readerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reader_select, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void viewSetting() {
        super.viewSetting();
        this.readerList = getView().findViewById(R.id.reader_list);
        List<String> readerNameList = new ArrayList<>();
        readerNameList.add("接続先リーダー①");
        readerNameList.add("接続先リーダー②");
        readerNameList.add("接続先リーダー③");
        ReaderRecyclerViewAdapter adapter = new ReaderRecyclerViewAdapter(readerNameList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.readerList.setHasFixedSize(true);
        this.readerList.setLayoutManager(llm);
        this.readerList.setAdapter(adapter);
        this.mainInterface.setLoginId2(View.VISIBLE);
        this.mainInterface.setScreenId("Y05");
    }

    @Override
    public void eventSetting() {
        super.eventSetting();
    }

    @Override
    public void mainSetting() {
        super.mainSetting();
        this.mainInterface.setGroupsVisibility(true, true, true);
        this.mainInterface.setSubTitleText(getString(R.string.reader));
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).popBackStack();
            }
        };
        this.mainInterface.setBackButton(clickListener);
        View.OnClickListener connectClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(getString(R.string.app_name), "接続ボタンをクリック");
                reader.connect();
                mainInterface.connected();
                NavHostFragment.findNavController(getParentFragment()).popBackStack();
            }
        };
        ButtonInfo connectButtonInfo = new ButtonInfo(getString(R.string.connect), connectClickListener);
        this.mainInterface.setFooterButton(null, null, connectButtonInfo, null, null);
    }
}
