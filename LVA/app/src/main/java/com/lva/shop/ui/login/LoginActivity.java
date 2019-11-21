package com.lva.shop.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.btn_login_fb)
    Button btnLoginFb;
    @BindView(R.id.edtOTP)
    EditText edtOTP;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_send_otp)
    Button btnSendOtp;

    private String OTP;
    private CallbackManager callbackManager;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUnBinder(ButterKnife.bind(this));
        setupFacebook();

    }

    private void setupFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }

    @OnClick({R.id.btn_login, R.id.btn_login_fb, R.id.btn_send_otp})
    public void onItemClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (OTP != null) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP, edtOTP.getText().toString());
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(this, mCompleteListener());
                }
                break;
            case R.id.btn_login_fb:
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Collections.singletonList("email"));
                break;
            case R.id.btn_send_otp:
                sendOTP();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
