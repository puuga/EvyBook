package com.appspace.evybook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspace.appspacelibrary.manager.Contextor;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evybook.R;
import com.appspace.evybook.activity.LoginActivity;
import com.appspace.evybook.activity.MainActivity;
import com.appspace.evybook.manager.ApiManager;
import com.appspace.evybook.model.EvyTinkUser;
import com.appspace.evybook.util.DataStoreUtils;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    private final String TAG = "LoginActivity";

    LoginButton btnFacebookLogin;
    ImageView ivProfile;
    TextView tvUsername;

    CallbackManager fbCallbackManager;
    AccessTokenTracker fbAccessTokenTracker;
    AccessToken fbAccessToken;
    ProfileTracker fbProfileTracker;
    Profile fbProfile;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser firebaseUser;

    String facebookUid;
    String facebookName;
    String firebaseName;

    LoginActivity activity;

    public LoginActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFacebook();
        initFirebase();

        activity = (LoginActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initInstances(view);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            setUserProfileWithFirebase();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fbAccessTokenTracker.stopTracking();
        fbProfileTracker.stopTracking();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initInstances(View view) {
        ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        btnFacebookLogin = (LoginButton) view.findViewById(R.id.btnFacebookLogin);
        btnFacebookLogin.setReadPermissions("public_profile", "email");
        btnFacebookLogin.setFragment(this);
        btnFacebookLogin.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoggerUtils.log2D(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                activity.showProgressDialog();
            }

            @Override
            public void onCancel() {
                LoggerUtils.log2D(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                LoggerUtils.log2DT(TAG, "facebook:onError", error);
            }
        });
    }

    private void initFacebook() {
        fbCallbackManager = CallbackManager.Factory.create();
        fbAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                if (currentAccessToken == null) {
                    LoggerUtils.log2D(TAG, "facebook:Logout");
                    FirebaseAuth.getInstance().signOut();
                    DataStoreUtils.getInstance().setLogin(false);
                    DataStoreUtils.getInstance().setAppUserId("");
                    DataStoreUtils.getInstance().setFacebookId("");
                    setUserProfileToNotLogin();
                }
                fbAccessToken = currentAccessToken;
            }
        };
        // If the access token is available already assign it.
        fbAccessToken = AccessToken.getCurrentAccessToken();

        fbProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
                fbProfile = currentProfile;
                LoggerUtils.log2D(TAG, "facebook:Login");
            }
        };
    }

    private void initFirebase() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    // User is signed in
                    LoggerUtils.log2D(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
//                    gotoMainActivity();

                    firebaseName = firebaseUser.getDisplayName();

                    if (fbProfile != null) {
                        facebookUid = fbProfile.getId();
                        facebookName = fbProfile.getName();
                    }

                } else {
                    // User is signed out
                    LoggerUtils.log2D(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    private void handleFacebookAccessToken(AccessToken token) {
        LoggerUtils.log2D(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        LoggerUtils.log2D(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        LoggerUtils.log2D(TAG, "signInWithCredential:onComplete:Uid:" + firebaseUser.getUid());


                        registerEvyTinkUser(facebookUid, firebaseName, firebaseUser.getUid());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            LoggerUtils.log2DT(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    protected void registerEvyTinkUser(String id, String name, String farebaseUid) {
        LoggerUtils.log2D(TAG, "registerEvyTinkUser:id:" + id);
        LoggerUtils.log2D(TAG, "registerEvyTinkUser:name:" + name);
        LoggerUtils.log2D(TAG, "registerEvyTinkUser:firebase_uid:" + farebaseUid);
        Call<EvyTinkUser[]> call = ApiManager.getInstance().getEvyTinkAPIService()
                .register(id, name, farebaseUid);
        call.enqueue(new Callback<EvyTinkUser[]>() {
            @Override
            public void onResponse(Call<EvyTinkUser[]> call, Response<EvyTinkUser[]> response) {

                LoggerUtils.log2D(TAG, "registerEvyTinkUser:message:" + response.message());
                EvyTinkUser evyTinkUser = response.body()[0];

                DataStoreUtils.getInstance().setLogin(true);
                DataStoreUtils.getInstance().setAppUserId(evyTinkUser.evyaccountid);
                DataStoreUtils.getInstance().setFacebookId(evyTinkUser.evyfacebookid);

                activity.hideProgressDialog();
                setUserProfileWithFirebase();

                activity.setResult();

//                gotoMainActivity();
            }

            @Override
            public void onFailure(Call<EvyTinkUser[]> call, Throwable t) {
                LoggerUtils.log2DT(TAG, "registerEvyTinkUser:onFailure", t);
                FirebaseCrash.report(t);
            }
        });
    }

    public void setUserProfileWithFirebase() {
        Glide.with(this)
                .load(firebaseUser.getPhotoUrl())
                .bitmapTransform(new CropCircleTransformation(Contextor.getInstance().getContext()))
                .into(ivProfile);

        tvUsername.setText(firebaseUser.getDisplayName());
    }

    public void setUserProfileToNotLogin() {
        ivProfile.setImageResource(R.drawable.image_placeholder_profile);
        ivProfile.setScaleType(ImageView.ScaleType.FIT_CENTER);

        tvUsername.setText("");
    }

    protected void gotoMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        // close this activity
        getActivity().finish();
    }
}
