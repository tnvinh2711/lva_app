package com.lva.shop.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.CommonUtils;
import com.lva.shop.utils.NetworkUtils;
import com.lva.shop.utils.Preference;
import com.lva.shop.utils.ScreenUtils;
import com.lva.shop.utils.ViewUtils;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
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
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.layout_login)
    ConstraintLayout layoutLogin;
    @BindView(R.id.tv_text_otp)
    TextView tvTextOtp;
    @BindView(R.id.otp_view)
    OtpView otpView;
    @BindView(R.id.tv_resend)
    TextView tvResend;
    @BindView(R.id.btn_next_otp)
    TextView btnNextOtp;
    @BindView(R.id.layout_otp)
    RelativeLayout layoutOtp;

    private String hash;
    private String OTP;
    private CallbackManager callbackManager;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String TAG = LoginActivity.class.getSimpleName();
    private int DELAY_TIME = 2000;
    boolean launchApp;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUnBinder(ButterKnife.bind(this));
        setUpView();
        setupFacebook();
        checkLaunchApp();
    }

    private void checkLaunchApp() {
        if (getIntent() != null)
            launchApp = getIntent().getBooleanExtra(AppConstants.LAUNCH_APP, true);
        DELAY_TIME = launchApp ? 2000 : 0;
        new Handler().postDelayed(() -> {
            if (Preference.getString(LoginActivity.this, AppConstants.ACCESS_TOKEN) != null && launchApp) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                animationLogo();
            }
        }, DELAY_TIME);
    }

    private void setUpView() {
        hideLayoutOtp();
        tvLogo.setText(Html.fromHtml("Lựa chọn "
                + "<font color=\"#FF42A2\">" + "trải nghiệm " + "</font>"
                + "cảm nhận và "
                + "<font color=\"#FF42A2\">" + "chia sẻ." + "</font>™"));
        otpView.setOtpCompletionListener(otp -> OTP = otp);
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() <= 0) {
                    btnNext.setEnabled(false);
                    btnNext.setBackground(getResources().getDrawable(R.drawable.bg_btn_login_gray));
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackground(getResources().getDrawable(R.drawable.bg_btn_login));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void hideLayoutOtp() {
        layoutOtp.setVisibility(View.GONE);
        btnBack.setVisibility(View.GONE);
        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .onStart(animator -> layoutLogin.setVisibility(View.VISIBLE))
                .playOn(layoutLogin);
    }

    private void showLayoutOtp() {
        layoutLogin.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .onStart(animator -> layoutOtp.setVisibility(View.VISIBLE))
                .playOn(layoutOtp);
    }

    private void animationLogo() {
        YoYo.with(Techniques.FadeOut)
                .duration(launchApp ? 500 : 0)
                .onEnd(animator -> tvLogo.setVisibility(View.GONE))
                .playOn(tvLogo);
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(ivLogo, "scaleX", 0.7f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(ivLogo, "scaleY", 0.7f);
        scaleDownX.setDuration(launchApp ? 500 : 0);
        scaleDownY.setDuration(launchApp ? 500 : 0);

        ObjectAnimator moveUpY = ObjectAnimator.ofFloat(ivLogo, "translationY", ViewUtils.dpToPx(-170));
        moveUpY.setDuration(launchApp ? 500 : 0);

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
                clLogin.setVisibility(View.VISIBLE);
                clLogin.getLayoutParams().height = (ScreenUtils.getScreenHeight(LoginActivity.this) - ViewUtils.dpToPx(220));
                clLogin.requestLayout();
                hideLayoutOtp();
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
                hideLoading();
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(LoginActivity.this, mCompleteListener());
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                hideLoading();
                Log.e(TAG, "onVerificationFailed: "+ e.getMessage() );
                Toast.makeText(LoginActivity.this, getString(R.string.something_when_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                hideLoading();
                hash = s;
                resendToken = forceResendingToken;
                showLayoutOtp();
            }
        };
    }

    private void sendOTP() {
        if (NetworkUtils.isNetworkConnected(this)) {
            showLoading();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    CommonUtils.getConvertPhone(edtPhone.getText().toString()),        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mSendOTPCallBack());
        } else {
            showDialogError(getString(R.string.network_error), AppConstants.NETWORK_ERROR);
        }
    }

    private void resendOTP() {
        Log.e(TAG, "resendOTP: " + CommonUtils.getConvertPhone(edtPhone.getText().toString()));
        if (NetworkUtils.isNetworkConnected(this)) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    CommonUtils.getConvertPhone(edtPhone.getText().toString()),        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mSendOTPCallBack(), resendToken);
        } else {
            showDialogError(getString(R.string.network_error), AppConstants.NETWORK_ERROR);
        }
    }

    private void setTimer() {
        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvResend.setText(getString(R.string.resend_otp) + "(" + millisUntilFinished / 1000 + "s)");
                tvResend.setTextColor(getResources().getColor(R.color.color_text_gray));
                tvResend.setEnabled(false);
            }

            public void onFinish() {
                tvResend.setText(getString(R.string.resend_otp));
                tvResend.setTextColor(getResources().getColor(R.color.color_text_root));
                tvResend.setEnabled(true);
            }

        }.start();
    }

    private void requestToServer(String uid) {
        Preference.save(this, AppConstants.ACCESS_TOKEN, uid);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if(timer!= null) timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.btn_back, R.id.tv_skip, R.id.btn_next, R.id.btn_facebook, R.id.tv_resend, R.id.btn_next_otp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                hideLayoutOtp();
                break;
            case R.id.tv_skip:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.btn_next:
                sendOTP();
                break;
            case R.id.tv_resend:
                resendOTP();
                setTimer();
                break;
            case R.id.btn_next_otp:
                Log.e(TAG, "onViewClicked: " + OTP + "   " + hash);
                if (OTP != null && hash != null) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(hash, OTP);
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(this, mCompleteListener());
                }
                break;
        }
    }


}
