package com.appspace.evybook.adapter;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appspace.evybook.R;
import com.appspace.evybook.activity.MainActivity;
import com.appspace.evybook.model.EvyBook;
import com.appspace.evybook.util.Helper;
import com.appspace.evybook.view.holder.BookDownloadHolder;
import com.appspace.evybook.view.holder.BookHolder;
import com.appspace.evybook.view.holder.BookReadHolder;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

/**
 * Created by siwaweswongcharoen on 8/9/2016 AD.
 */
public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<EvyBook> bookList;
    private OnEvyBookItemClickCallback callback;
    private MainActivity.ListType layoutType;

    public interface OnEvyBookItemClickCallback {
        void onEvyBookItemDownloadClick(EvyBook book, int position);

        void onEvyBookItemReadClick(EvyBook book, int position);

        void onEvyBookItemDeleteClick(EvyBook book, int position);

        void onEvyBookItemCoverClick(EvyBook book, int position);
    }

    public BookAdapter(Context context, List<EvyBook> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    public void setCallback(OnEvyBookItemClickCallback callback) {
        this.callback = callback;
    }

    public void setLayoutType(MainActivity.ListType layoutType) {
        this.layoutType = layoutType;
    }

    @Override
    public int getItemViewType(int position) {
        EvyBook book = bookList.get(position);
        if (!hasFileInStorage(book)) { // not download
            if (layoutType == MainActivity.ListType.LIST)
                return Helper.HOLDER_LIST_BOOK_DOWNLOAD;
            else
                return Helper.HOLDER_GRID_BOOK_DOWNLOAD;
        } else {
            if (layoutType == MainActivity.ListType.LIST)
                return Helper.HOLDER_LIST_BOOK_READ;
            else
                return Helper.HOLDER_GRID_BOOK_READ;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case Helper.HOLDER_LIST_BOOK_DOWNLOAD:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_book_list_download, parent, false);
                return new BookDownloadHolder(itemView);
            case Helper.HOLDER_LIST_BOOK_READ:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_book_list_read, parent, false);
                return new BookReadHolder(itemView);
            case Helper.HOLDER_GRID_BOOK_DOWNLOAD:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_book_grid_download, parent, false);
                return new BookDownloadHolder(itemView);
            case Helper.HOLDER_GRID_BOOK_READ:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_book_grid_read, parent, false);
                return new BookReadHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EvyBook book = bookList.get(position);
        switch (holder.getItemViewType()) {
            case Helper.HOLDER_LIST_BOOK_DOWNLOAD:
            case Helper.HOLDER_GRID_BOOK_DOWNLOAD:
                BookDownloadHolder bookDownloadHolder = (BookDownloadHolder) holder;
                setDataToBookHolder(bookDownloadHolder, book);

                setEventToDownloadHolder(bookDownloadHolder, book, position);

                break;
            case Helper.HOLDER_LIST_BOOK_READ:
            case Helper.HOLDER_GRID_BOOK_READ:
                BookReadHolder bookReadHolder = (BookReadHolder) holder;
                setDataToBookHolder(bookReadHolder, book);

                setEventToReadHolder(bookReadHolder, book, position);
        }
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }

    void setDataToBookHolder(BookHolder holder, EvyBook book) {
        holder.tvBookTitle.setText(book.title);
        holder.tvPublisher.setText(book.publisher);
        Glide.with(context)
                .load(book.coverUrl)
                .centerCrop()
                .placeholder(R.drawable.book_cover_placeholder_fixed)
                .crossFade()
                .into(holder.ivBookCover);
    }

    void setEventToDownloadHolder(BookDownloadHolder bookDownloadHolder, final EvyBook book, final int position) {
        bookDownloadHolder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onEvyBookItemDownloadClick(book, position);

                view.setClickable(false);
                ((Button) view).setText(R.string.downloading);
            }
        });
    }

    void setEventToReadHolder(BookReadHolder bookReadHolder, final EvyBook book, final int position) {
        bookReadHolder.btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onEvyBookItemReadClick(book, position);
            }
        });
        bookReadHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onEvyBookItemDeleteClick(book, position);
            }
        });
    }

    boolean hasFileInStorage(EvyBook book) {
        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/EvyBook/");
        File[] files = path.listFiles();
        if (files == null) {
            return false;
        }
        for (File file : files) {
            if (file.getName().equals(book.fileName)) {
                return true;
            }
        }
        return false;
    }
}
