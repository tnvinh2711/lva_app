package com.lva.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;
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
import com.lva.shop.ui.base.BaseActivity;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.btn_login_fb)
    Button btnLoginFb;
    @BindView(R.id.edtOTP)
    EditText edtOTP;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private String OTP;
    private CallbackManager callbackManager;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        sendOTP();
        setupFacebook();

    }

    private void setupFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "LoginManager onSuccess: " + loginResult.toString());
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "LoginManager onCancel: ");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "LoginManager onError: " + exception.toString());
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, mCompleteListener());
    }


    private OnCompleteListener<AuthResult> mCompleteListener() {
        return task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                Toast.makeText(this, "Login success: UID-" + user.getUid(), Toast.LENGTH_SHORT).show();
            } else {
                // Sign in failed, display a message and update the UI
                Log.w(TAG, "signInWithCredential:failure", task.getException());
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                }
            }
        };
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mSendOTPCallBack() {
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(MainActivity.this, mCompleteListener());
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.e(TAG, "onCodeSent: " + s);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void setUp() {

    }

    @OnClick({R.id.btn_login, R.id.btn_login_fb})
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
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email"));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
