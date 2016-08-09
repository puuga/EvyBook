package com.appspace.evybook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspace.evybook.R;
import com.appspace.evybook.model.EvyBook;
import com.appspace.evybook.view.BookHolder;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by siwaweswongcharoen on 8/9/2016 AD.
 */
public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<EvyBook> bookList;
    private OnEvyBookItemClickCallback callback;

    public interface OnEvyBookItemClickCallback {
        void onEvyBookItemDownloadClick(EvyBook book, int position);

        void onEvyBookItemCoverClick(EvyBook book, int position);
    }

    public BookAdapter(Context context, List<EvyBook> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    public void setCallback(OnEvyBookItemClickCallback callback) {
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_book,parent,false);
        return new BookHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EvyBook book = bookList.get(position);
        BookHolder bookHolder = (BookHolder) holder;
        bookHolder.tvBookTitle.setText(book.title);
        bookHolder.tvPublisher.setText(book.publisher);
        Glide.with(context)
                .load(book.coverUrl)
                .centerCrop()
                .placeholder(R.drawable.book_cover_placeholder_fixed)
                .crossFade()
                .into(bookHolder.ivBookCover);
        bookHolder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onEvyBookItemDownloadClick(book, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }
}
