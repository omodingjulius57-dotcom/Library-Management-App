package com.example.libraryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.R;
import com.example.libraryapp.models.BorrowRecord;

import java.util.List;

public class BorrowRecordAdapter extends RecyclerView.Adapter<BorrowRecordAdapter.RecordViewHolder> {

    private final Context context;
    private List<BorrowRecord> recordList;

    public BorrowRecordAdapter(Context context, List<BorrowRecord> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    public void updateList(List<BorrowRecord> newList) {
        this.recordList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_borrow_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        BorrowRecord record = recordList.get(position);
        holder.tvBookTitle.setText(record.getBookTitle());
        holder.tvMemberName.setText("Member: " + record.getMemberName());
        holder.tvBorrowDate.setText("Borrow Date: " + record.getBorrowDate());

        if ("Returned".equalsIgnoreCase(record.getStatus())) {
            holder.tvReturnDate.setText("Return Date: " + (record.getActualReturnDate() != null ? record.getActualReturnDate() : record.getExpectedReturnDate()));
            holder.tvStatus.setText("Returned");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_returned);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_returned_text));
        } else if ("Overdue".equalsIgnoreCase(record.getStatus())) {
            holder.tvReturnDate.setText("Due Date: " + record.getExpectedReturnDate());
            holder.tvStatus.setText("Overdue");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_overdue);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_overdue_text));
        } else {
            holder.tvReturnDate.setText("Due Date: " + record.getExpectedReturnDate());
            holder.tvStatus.setText("Borrowed");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_borrowed);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_borrowed_text));
        }
    }

    @Override
    public int getItemCount() {
        return recordList != null ? recordList.size() : 0;
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvMemberName, tvBorrowDate, tvReturnDate, tvStatus;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvRecordBookTitle);
            tvMemberName = itemView.findViewById(R.id.tvRecordMemberName);
            tvBorrowDate = itemView.findViewById(R.id.tvRecordBorrowDate);
            tvReturnDate = itemView.findViewById(R.id.tvRecordReturnDate);
            tvStatus = itemView.findViewById(R.id.tvRecordStatus);
        }
    }
}
