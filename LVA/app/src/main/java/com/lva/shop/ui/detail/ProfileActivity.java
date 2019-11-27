package com.lva.shop.ui.detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.lva.shop.R;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.CommonUtils;
import com.lva.shop.utils.Preference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {

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
        }, 50);

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
                break;
        }
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
}
