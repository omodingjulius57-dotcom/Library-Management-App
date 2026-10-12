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
import com.example.libraryapp.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Context context;
    private List<Book> bookList;
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public BookAdapter(Context context, List<Book> bookList, OnBookClickListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    public void updateList(List<Book> newList) {
        this.bookList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvIsbn.setText("ISBN: " + book.getIsbn());

        if (book.isAvailable()) {
            holder.tvStatus.setText("Available");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_available);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_available_text));
        } else {
            holder.tvStatus.setText("Borrowed");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_badge_borrowed);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_borrowed_text));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList != null ? bookList.size() : 0;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvIsbn, tvStatus;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvIsbn = itemView.findViewById(R.id.tvBookIsbn);
            tvStatus = itemView.findViewById(R.id.tvBookStatus);
        }
    }
}
