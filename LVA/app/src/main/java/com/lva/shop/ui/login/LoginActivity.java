package com.lva.shop.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.lva.shop.R;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.main.MainActivity;
import com.lva.shop.utils.ScreenUtils;
import com.lva.shop.utils.ViewUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    Button btnSendOtp;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_logo)
    TextView tvLogo;
    @BindView(R.id.cl_login)
    ConstraintLayout clLogin;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.tv_hello)
    TextView tvHello;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.btn_next)
    TextView btnNext;
    @BindView(R.id.ll_login_phone)
    LinearLayout llLoginPhone;
    @BindView(R.id.btn_facebook)
    LoginButton btnFacebook;

    private String OTP;
    private CallbackManager callbackManager;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String TAG = LoginActivity.class.getSimpleName();
    private int DELAY_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUnBinder(ButterKnife.bind(this));
        setUpView();
        setupFacebook();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animationLogo();
            }
        }, DELAY_TIME);
    }

    private void setUpView() {
        tvLogo.setText(Html.fromHtml("Lựa chọn "
                + "<font color=\"#FF42A2\">" + "trải nghiệm " + "</font>"
                + "cảm nhận và "
                + "<font color=\"#FF42A2\">" + "chia sẻ." + "</font>™"));
    }

    private void animationLogo() {
        YoYo.with(Techniques.FadeOut)
                .duration(500)
                .onEnd(animator -> tvLogo.setVisibility(View.GONE))
                .playOn(tvLogo);
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(ivLogo, "scaleX", 0.7f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(ivLogo, "scaleY", 0.7f);
        scaleDownX.setDuration(500);
        scaleDownY.setDuration(500);

        ObjectAnimator moveUpY = ObjectAnimator.ofFloat(ivLogo, "translationY", ViewUtils.dpToPx(-170));
        moveUpY.setDuration(500);

        AnimatorSet scaleDown = new AnimatorSet();
        AnimatorSet moveUp = new AnimatorSet();

        scaleDown.play(scaleDownX).with(scaleDownY);
        moveUp.play(moveUpY);

        scaleDown.start();
        moveUp.start();
        moveUp.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivLogo.setElevation(ViewUtils.dpToPx(1));
                }
                ivLogo.setBackground(getResources().getDrawable(R.drawable.bg_logo_splash));
                tvSkip.setVisibility(View.VISIBLE);
                clLogin.getLayoutParams().height = (ScreenUtils.getScreenHeight(LoginActivity.this) - ViewUtils.dpToPx(220));
                clLogin.requestLayout();
                YoYo.with(Techniques.FadeIn)
                        .duration(500)
                        .onStart(animator -> clLogin.setVisibility(View.VISIBLE))
                        .playOn(clLogin);
            }
        });
    }

    private void setupFacebook() {
        callbackManager = CallbackManager.Factory.create();
        btnFacebook.setPermissions("email", "public_profile");
        btnFacebook.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener(LoginActivity.this, mCompleteListener());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "LoginManager onCancel: ");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "LoginManager onError: " + exception.toString());
                        Toast.makeText(LoginActivity.this, getString(R.string.something_when_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private OnCompleteListener<AuthResult> mCompleteListener() {
        return task -> {
            if (task.isSuccessful()) {
                try {
                    FirebaseUser user = task.getResult().getUser();
                    if (user != null) requestToServer(user.getUid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "signInWithCredential:failure", task.getException());
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(LoginActivity.this, getString(R.string.something_when_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mSendOTPCallBack() {
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(LoginActivity.this, mCompleteListener());
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(LoginActivity.this, getString(R.string.something_when_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                OTP = s;
                resendToken = forceResendingToken;
            }
        };
    }

    private void sendOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84975477088",        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mSendOTPCallBack());
    }

    private void resendOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84975477088",        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mSendOTPCallBack(), resendToken);
    }

    private void requestToServer(String uid) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }

//    @OnClick({R.id.btn_login, R.id.btn_login_fb, R.id.btn_send_otp})
//    public void onItemClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_login:
//                if (OTP != null) {
//                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP, edtOTP.getText().toString());
//                    FirebaseAuth.getInstance().signInWithCredential(credential)
//                            .addOnCompleteListener(this, mCompleteListener());
//                }
//                break;
//            case R.id.btn_login_fb:
//                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Collections.singletonList("email"));
//                break;
//            case R.id.btn_send_otp:
//                sendOTP();
//                break;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.iv_logo)
    public void onViewClicked() {
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.tv_skip, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.btn_next:
                sendOTP();
                break;
        }
    }
}
