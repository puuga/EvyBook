package com.appspace.evybook.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evybook.R;
import com.appspace.evybook.activity.MainActivity;
import com.appspace.evybook.adapter.BookAdapter;
import com.appspace.evybook.manager.ApiManager;
import com.appspace.evybook.model.EvyBook;
import com.appspace.evybook.util.DataStoreUtils;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    MainActivity currentActivity;

    EvyBook[] books;
    RecyclerView recyclerView;
    private List<EvyBook> bookList;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadBook();
    }

    private void initInstances(View view) {
        currentActivity = (MainActivity) getActivity();

        bookList = new ArrayList<>();
        BookAdapter adapter = new BookAdapter(currentActivity, bookList);
        adapter.setCallback(currentActivity);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(currentActivity));
        recyclerView.setAdapter(adapter);
    }

    private void loadBook() {
        currentActivity.showProgressDialog();
        Call<EvyBook[]> call = ApiManager.getInstance().getEvyTinkAPIService()
                .loadBooks(DataStoreUtils.getInstance().getAppUserId());
        call.enqueue(new Callback<EvyBook[]>() {
            @Override
            public void onResponse(Call<EvyBook[]> call, Response<EvyBook[]> response) {
                currentActivity.hideProgressDialog();
                books = response.body();
                LoggerUtils.log2D("api", "loadBook:code - " + response.code());
                LoggerUtils.log2D("api", "loadBook:OK - " + books.length);

                loadDataToRecyclerView(books);
            }

            @Override
            public void onFailure(Call<EvyBook[]> call, Throwable t) {
                FirebaseCrash.report(t);
            }
        });
    }

    private void loadDataToRecyclerView(EvyBook[] books) {
//        bookList.clear();
        LoggerUtils.log2D("api", "loadDataToRecyclerView:OK - " + bookList.size());
        bookList.addAll(Arrays.asList(books));
        LoggerUtils.log2D("api", "loadDataToRecyclerView:OK - " + bookList.size());
        recyclerView.getAdapter().notifyDataSetChanged();
        LoggerUtils.log2D("api", "loadDataToRecyclerView:OK - " + recyclerView.getAdapter().getItemCount());
    }

    public void reloadRecyclerView() {
        recyclerView.getAdapter().notifyDataSetChanged();
        LoggerUtils.log2D("api", "reloadRecyclerView:OK - ");
    }
}
