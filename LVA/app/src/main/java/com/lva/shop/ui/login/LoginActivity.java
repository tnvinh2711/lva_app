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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.lva.shop.R;
import com.lva.shop.api.RestfulManager;
import com.lva.shop.callback.ButtonAlertDialogListener;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.location.model.AddressReqRes;
import com.lva.shop.ui.login.model.Login;
import com.lva.shop.ui.login.model.ResponseUser;
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

public class LoginActivity extends BaseActivity implements ButtonAlertDialogListener {
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
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String TAG = LoginActivity.class.getSimpleName();
    private int DELAY_TIME = 2000;
    boolean launchApp;
    private CountDownTimer timer;
    private String token;
    private String phone;
    private final String TYPE_ERROR_LOCATION = "location_error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUnBinder(ButterKnife.bind(this));
        setUpView();
        checkLaunchApp();
    }

    private void checkLaunchApp() {
        try {
            if (getIntent() != null)
                launchApp = getIntent().getBooleanExtra(AppConstants.LAUNCH_APP, true);
            DELAY_TIME = launchApp ? 2000 : 0;
            new Handler().postDelayed(() -> {
                if (Preference.getString(LoginActivity.this, AppConstants.ACCESS_TOKEN) != null && launchApp) {
                    phone = Preference.getString(LoginActivity.this, AppConstants.PHONE);
                    getUserInfo(Preference.getString(LoginActivity.this, AppConstants.ACCESS_TOKEN), phone);
                } else {
                    animationLogo();
                }
            }, DELAY_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpView() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                clLogin.getLayoutParams().height = (ScreenUtils.getScreenHeight(LoginActivity.this) - ViewUtils.dpToPx(270));
                clLogin.requestLayout();
                hideLayoutOtp();
            }
        });
    }

    private OnCompleteListener<AuthResult> mCompleteListener() {
        return task -> {
            if (task.isSuccessful()) {
                try {
                    FirebaseUser user = task.getResult().getUser();
                    if (user != null) requestToServer(user);
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
                Log.e(TAG, "onVerificationFailed: " + e.getMessage());
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
            showDialogError(getString(R.string.network_error), AppConstants.NETWORK_ERROR, this);
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
            showDialogError(getString(R.string.network_error), AppConstants.NETWORK_ERROR, this);
        }
    }

    private void setTimer() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestToServer(FirebaseUser user) {
        RestfulManager.getInstance(LoginActivity.this, 1).postLogin(edtPhone.getText().toString(), user.getUid(), new RestfulManager.OnLoginListener() {
            @Override
            public void onLoginSuccess(Login login) {
                try {
                    token = login.getToken();
                    Preference.save(LoginActivity.this, AppConstants.ACCESS_TOKEN, login.getToken());
                    Preference.save(LoginActivity.this, AppConstants.PHONE, "0" + edtPhone.getText().toString());
                    phone = "0" + edtPhone.getText().toString();
                    getUserInfo(login.getToken(), phone);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onLoginFail: " + e.getMessage());
            }
        });
    }

    private void getUserInfo(String token, String phone) {
        RestfulManager.getInstance(LoginActivity.this, 1).getUserInfo(phone, token, new RestfulManager.OnGetUserListener() {
            @Override
            public void onGetUserSuccess(ResponseUser responseUser) {
                try {
                    Gson gson = new Gson();
                    String jsonUser = gson.toJson(responseUser.getUserInfo());
                    Preference.save(LoginActivity.this, AppConstants.USER_INFO, jsonUser);
                    if (Preference.getString(LoginActivity.this, AppConstants.ADDRESS_LOCAL) != null) {
                        if (responseUser.getUserInfo().getPhoneDelivery() == null
                                && responseUser.getUserInfo().getNameDelivery() == null
                                && responseUser.getUserInfo().getProvince() == null
                                && responseUser.getUserInfo().getAddress() == null
                                && responseUser.getUserInfo().getDistrict() == null
                                && responseUser.getUserInfo().getWard() == null) {
                            postUserInfo();
                        } else {
                            Preference.remove(LoginActivity.this, AppConstants.ADDRESS_LOCAL);
                        }

                    }
                    if (!launchApp) {
                        LoginActivity.this.setResult(AppConstants.LOGIN_RESULT);
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                LoginActivity.this.onError(e, AppConstants.LOAD_DATA_LOGIN_FAIL);
            }
        });
    }

    private void postUserInfo() {
        Gson gson = new Gson();
        AddressReqRes addressReqRes = gson.fromJson(Preference.getString(this, AppConstants.ADDRESS_LOCAL), AddressReqRes.class);

        RestfulManager.getInstance(this, 1).postUpdateUser(addressReqRes.getName(), addressReqRes.getPhone(), addressReqRes.getAddress(), addressReqRes.getCity(), addressReqRes.getDistrict(), addressReqRes.getWard(), new RestfulManager.OnGetUserListener() {
            @Override
            public void onGetUserSuccess(ResponseUser responseUser) {
                try {
                    Gson gson = new Gson();
                    String jsonUser = gson.toJson(responseUser.getUserInfo());
                    Preference.save(LoginActivity.this, AppConstants.USER_INFO, jsonUser);
                    Preference.remove(LoginActivity.this, AppConstants.ADDRESS_LOCAL);
                    Log.e(TAG, "onGetUserSuccess: " + launchApp);
                    if (!launchApp) {
                        LoginActivity.this.setResult(AppConstants.LOGIN_RESULT);
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                LoginActivity.this.onError(e, TYPE_ERROR_LOCATION);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.btn_back, R.id.tv_skip, R.id.btn_next, R.id.tv_resend, R.id.btn_next_otp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                hideLayoutOtp();
                break;
            case R.id.tv_skip:
                if (launchApp) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
                break;
            case R.id.btn_next:
                if (!edtPhone.getText().toString().equals("")) {
                    sendOTP();
                } else {
                    Toast.makeText(this, R.string.input_phone_to_continues, Toast.LENGTH_SHORT).show();
                }
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


    @Override
    public void onConfirmClick(String type) {
        switch (type) {
            case AppConstants.LOAD_DATA_LOGIN_FAIL:
                getUserInfo(token, phone);
                break;
            case TYPE_ERROR_LOCATION:
                postUserInfo();
                break;
        }
    }

    @Override
    public void onCancelClick(String type) {

    }
}
