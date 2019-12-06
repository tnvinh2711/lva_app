package com.lva.shop.ui.detail;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.lva.shop.R;
import com.lva.shop.api.RestfulManager;
import com.lva.shop.callback.ButtonAlertDialogListener;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.location.LocationActivity;
import com.lva.shop.ui.login.model.ResponseUser;
import com.lva.shop.ui.login.model.UserInfo;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.CommonUtils;
import com.lva.shop.utils.Preference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ProfileActivity extends BaseActivity implements ButtonAlertDialogListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ic_change_banner)
    ImageView icChangeBanner;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.iv_ava)
    RoundedImageView ivAva;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_point)
    TextView tvPoint;
    @BindView(R.id.edt_name)
    TextInputEditText edtName;
    @BindView(R.id.edt_birthday)
    TextInputEditText edtBirthday;
    @BindView(R.id.edt_phone)
    TextInputEditText edtPhone;
    @BindView(R.id.tv_update)
    TextView btnUpdate;
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    private String TAG = ProfileActivity.class.getSimpleName();
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private UserInfo userInfo;
    private Integer mm, yy, dd;
    private RequestBody requestBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUnBinder(ButterKnife.bind(this));
        setUpView();
    }

    private void setUpView() {
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        if (Preference.getString(this, AppConstants.ACCESS_TOKEN) != null) {
            String jsonUser = Preference.getString(this, AppConstants.USER_INFO);
            Gson gson = new Gson();
            userInfo = gson.fromJson(jsonUser, UserInfo.class);
            if (userInfo.getName() != null && !userInfo.getName().equals("")) {
                tvName.setText(userInfo.getName());
                edtName.setText(userInfo.getName());
            } else {
                edtName.setText("");
                tvName.setText(getString(R.string.input_name));
            }
            tvPoint.setText(getString(R.string.point, String.valueOf(userInfo.getPoint())));
            edtPhone.setText(Preference.getString(this, AppConstants.PHONE));
            edtBirthday.setText(userInfo.getDobD() != null ? userInfo.getDobD() + "-" + userInfo.getDobM() + "-" + userInfo.getDobY() : "");
            if (userInfo.getUrlAvatar() != null) {
                Glide.with(this)
                        .load(userInfo.getUrlAvatar())
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop())
                        .into(ivAva);
            }
        }
        new Handler().postDelayed(() -> {
            if (Preference.getString(ProfileActivity.this, AppConstants.URI_BANNER) != null) {
                try {
                    ivBanner.setImageURI(Uri.parse(Preference.getString(ProfileActivity.this, AppConstants.URI_BANNER)));
                } catch (Exception e) {
                    ivBanner.setImageDrawable(getResources().getDrawable(R.mipmap.banner));
                    e.printStackTrace();
                }
            } else {
                ivBanner.setImageDrawable(getResources().getDrawable(R.mipmap.banner));
            }
        }, 150);
        date = (view, year, monthOfYear, dayOfMonth) -> {
            mm = monthOfYear;
            dd = dayOfMonth;
            yy = year;
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String birthday = sdf.format(myCalendar.getTime());
            edtBirthday.setText(birthday);
        };
        edtBirthday.setInputType(InputType.TYPE_NULL);
        edtBirthday.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                edtBirthday.clearFocus();
                new DatePickerDialog(ProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        edtPhone.setInputType(InputType.TYPE_NULL);
        edtPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                edtPhone.clearFocus();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.ic_change_banner, R.id.iv_ava, R.id.tv_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_change_banner:
                getImage(AppConstants.REQ_CODE_STORAGE_BANNER);
                break;
            case R.id.iv_ava:
                getImage(AppConstants.REQ_CODE_STORAGE_AVATAR);
                break;
            case R.id.tv_update:
                postUserInfo();
                break;
        }
    }

    private void postUserInfo() {
        showLoading();
        RestfulManager.getInstance(ProfileActivity.this, 1).postUpdateUser(edtName.getText().toString(), dd, mm, yy, new RestfulManager.OnGetUserListener() {
            @Override
            public void onGetUserSuccess(ResponseUser responseUser) {
                Gson gson = new Gson();
                String jsonUser = gson.toJson(responseUser.getUserInfo());
                Preference.save(ProfileActivity.this, AppConstants.USER_INFO, jsonUser);
                setUpView();
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                ProfileActivity.this.onError(e, "1");
            }
        });
    }

    private void postUserInfoAvatar() {
        showLoading();
        RestfulManager.getInstance(ProfileActivity.this, 1).postUpdateUser(requestBody, new RestfulManager.OnGetUserListener() {
            @Override
            public void onGetUserSuccess(ResponseUser responseUser) {
                Gson gson = new Gson();
                String jsonUser = gson.toJson(responseUser.getUserInfo());
                Preference.save(ProfileActivity.this, AppConstants.USER_INFO, jsonUser);
                setUpView();
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                ProfileActivity.this.onError(e, "2");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.REQ_CODE_STORAGE_BANNER || requestCode == AppConstants.REQ_CODE_STORAGE_AVATAR) {
            for (int s : grantResults) {
                if (s != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            getImage(requestCode);
        }
    }

    private void getImage(int requestCode) {
        if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), requestCode + 999);
        } else {
            requestPermissionsSafely(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == AppConstants.REQ_CODE_STORAGE_AVATAR + 999 || requestCode == AppConstants.REQ_CODE_STORAGE_BANNER + 999) {
                    Uri selectedImageUri = data.getData();
                    final String path = CommonUtils.getPathFromURI(selectedImageUri, this);
                    if (path != null) {
                        File f = new File(path);
                        switch (requestCode - 999) {
                            case AppConstants.REQ_CODE_STORAGE_AVATAR:
                                Log.d(TAG, "onActivityResult: ");
                                File compressFile = new Compressor(ProfileActivity.this).compressToFile(f);
//                                ivAva.setImageURI(Uri.fromFile(compressFile));
                                requestBody = RequestBody.create(MediaType.parse("image/jpeg"), compressFile);
                                postUserInfoAvatar();
                                break;
                            case AppConstants.REQ_CODE_STORAGE_BANNER:
                                try {
                                    ivBanner.setImageURI(Uri.fromFile(f));
                                    Preference.save(this, AppConstants.URI_BANNER, Uri.fromFile(f).toString());
                                } catch (Exception e) {
                                    ivBanner.setImageDrawable(getResources().getDrawable(R.mipmap.banner));
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    @Override
    public void onConfirmClick(String type) {
        switch (type) {
            case "1":
                postUserInfo();
                break;
            case "2":
                postUserInfoAvatar();
                break;
        }
    }

    @Override
    public void onCancelClick(String type) {

    }
}
