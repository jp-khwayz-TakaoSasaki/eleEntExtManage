package jp.co.khwayz.eleEntExtManage.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jp.co.khwayz.eleEntExtManage.R;

public class ReaderRecyclerViewAdapter extends RecyclerView.Adapter<ReaderViewHolder> {
    private List<String> readerList;
    private int selectPosition = -1;

    public ReaderRecyclerViewAdapter(List<String> readerList) {
        this.readerList = readerList;
    }

    @NonNull
    @Override
    public ReaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.reader_row, parent,false);
        return new ReaderViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ReaderViewHolder holder, final int position) {
        String reader = this.readerList.get(position);
        holder.getReaderName().setText(reader);
        holder.getRow().setBackgroundColor(selectPosition == position ? Color.GRAY : Color.WHITE);
        View.OnClickListener rowClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPosition = position;
                notifyDataSetChanged();
            }
        };
        holder.getRow().setOnClickListener(rowClickListener);
    }

    @Override
    public int getItemCount() {
        return this.readerList.size();
    }
}
