package jp.co.khwayz.eleEntExtManage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import jp.co.khwayz.eleEntExtManage.R;

import static jp.co.khwayz.eleEntExtManage.Common.isReceiptApp;

public class StartFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        int id = isReceiptApp(getContext()) ? R.id.action_startFragment_to_receiptLoginFragment : R.id.action_startFragment_to_issueLoginFragment;
        int id = isReceiptApp(getContext()) ? R.id.action_startFragment_to_issueLoginFragment : R.id.action_startFragment_to_issueLoginFragment;
//        NavHostFragment.findNavController(this).navigate(id);
    }
}
