package com.appspace.evybook.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appspace.appspacelibrary.manager.Contextor;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evybook.R;
import com.appspace.evybook.adapter.BookAdapter;
import com.appspace.evybook.fragment.MainActivityFragment;
import com.appspace.evybook.manager.ApiManager;
import com.appspace.evybook.model.EvyBook;
import com.appspace.evybook.util.DataStoreUtils;
import com.appspace.evybook.util.Helper;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        BookAdapter.OnEvyBookItemClickCallback {
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    Button btnProfile;
    CoordinatorLayout container;
    ImageView ivProfile;
    TextView tvUsername;

    MaterialDialog mProgressDialog;

    DownloadManager downloadManager;
    long lastDownload = -1L;

    EvyBook bookToDownload;
    MainActivityFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        initInstances();

        loadProfileData();
    }

    private void initDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                toolbar,
                R.string.open_drawer_menu,
                R.string.close_drawer_menu
        );
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initInstances() {
        container = (CoordinatorLayout) findViewById(R.id.container);

        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(this);

        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvUsername = (TextView) findViewById(R.id.tvUsername);

        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progressing)
                .autoDismiss(false)
                .progress(true, 0)
                .build();

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        registerReceiver(onEvent, filter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(onEvent);

        super.onPause();
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helper.LOGIN_RESUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Snackbar.make(container, R.string.login_ok, Snackbar.LENGTH_SHORT)
                        .show();
                loadProfileData();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Snackbar.make(container, R.string.login_cancel, Snackbar.LENGTH_SHORT)
                            .show();
                    MaterialDialog dialog = new MaterialDialog.Builder(this)
                            .title(R.string.need_login)
                            .content(R.string.need_login_description)
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    gotoLoginActivity();
                                }
                            })
                            .show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
//                    onCameraClick();
                    downloadBook(bookToDownload);
                }
                return;
            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
//                    onGalleryClick();
                }
                return;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View view) {
        if (view == btnProfile) {
            gotoLoginActivity();
        }
    }

    protected void gotoLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivityForResult(i, Helper.LOGIN_RESUEST_CODE);
    }

    private void loadProfileData() {
        setProfile();
    }

    private void setProfile() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            LoggerUtils.log2D("profile", "cannot get firebaseUser");
            gotoLoginActivity();
            return;
        }
        Glide.with(this)
                .load(firebaseUser.getPhotoUrl())
                .bitmapTransform(new CropCircleTransformation(Contextor.getInstance().getContext()))
                .into(ivProfile);

        tvUsername.setText(firebaseUser.getDisplayName());
    }

    public void showProgressDialog() {
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void onEvyBookItemDownloadClick(EvyBook book, int position) {
        LoggerUtils.log2D("callback", "onEvyBookItemDownloadClick bookId:" + book.bookId);

        bookToDownload = book;
        // check permission to write
        checkPermissionToDownloadBook(book);
    }

    @Override
    public void onEvyBookItemReadClick(EvyBook book, int position) {
        LoggerUtils.log2D("callback", "onEvyBookItemReadClick bookId:" + book.bookId);

        File file = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/EvyBook/")
                        .getAbsolutePath()
                        + "/" + book.fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        startActivity(intent);
    }

    @Override
    public void onEvyBookItemDeleteClick(EvyBook book, int position) {
        LoggerUtils.log2D("callback", "onEvyBookItemDeleteClick bookId:" + book.bookId);

        File file = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/EvyBook/")
                        .getAbsolutePath()
                        + "/" + book.fileName);

        if (file.delete())
            fragment.reloadRecyclerView();
    }

    @Override
    public void onEvyBookItemCoverClick(EvyBook book, int position) {
        LoggerUtils.log2D("callback", "onEvyBookItemCoverClick bookId:" + book.bookId);
    }

    private void checkPermissionToDownloadBook(EvyBook book) {
        // check android.permission.WRITE_EXTERNAL_STORAGE permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            downloadBook(book);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void downloadBook(EvyBook book) {
        Call<EvyBook[]> call = ApiManager.getInstance().getEvyTinkAPIService()
                .postBookDownloadStat(
                        DataStoreUtils.getInstance().getAppUserId(),
                        book.bookId,
                        "1"
                );
        call.enqueue(new Callback<EvyBook[]>() {
            @Override
            public void onResponse(Call<EvyBook[]> call, Response<EvyBook[]> response) {
                LoggerUtils.log2D("api", "postBookDownloadStat ok");
            }

            @Override
            public void onFailure(Call<EvyBook[]> call, Throwable t) {
                FirebaseCrash.report(t);
            }
        });

        Uri uri = Uri.parse(book.fileUrl);

        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/EvyBook/")
                .mkdirs();

        DownloadManager.Request request = new DownloadManager.Request(uri);

        // request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
        // | DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setAllowedOverRoaming(false)
                .setTitle(book.fileName)
                .setDescription(book.fileUrl)
                .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS + "/EvyBook/",
                        book.fileName
                );
        lastDownload = downloadManager.enqueue(request);
    }

    private BroadcastReceiver onEvent = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {
                Toast.makeText(context, R.string.downloading, Toast.LENGTH_LONG).show();
            } else if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                Toast.makeText(context, R.string.download_complete, Toast.LENGTH_LONG).show();
                fragment.reloadRecyclerView();
            }
        }
    };
}
