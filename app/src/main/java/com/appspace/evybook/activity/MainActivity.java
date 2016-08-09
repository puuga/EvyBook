package com.appspace.evybook.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appspace.appspacelibrary.manager.Contextor;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evybook.R;
import com.appspace.evybook.adapter.BookAdapter;
import com.appspace.evybook.model.EvyBook;
import com.appspace.evybook.util.Helper;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        BookAdapter.OnEvyBookItemClickCallback {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    Button btnProfile;
    CoordinatorLayout container;
    ImageView ivProfile;
    TextView tvUsername;

    MaterialDialog mProgressDialog;

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
                if (FirebaseAuth.getInstance().getCurrentUser()==null) {
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
        LoggerUtils.log2D("callback", "onEvyBookItemDownloadClick bookId:"+book.bookId);
    }

    @Override
    public void onEvyBookItemCoverClick(EvyBook book, int position) {
        LoggerUtils.log2D("callback", "onEvyBookItemCoverClick bookId:"+book.bookId);
    }
}
