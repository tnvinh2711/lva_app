package com.lva.shop.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.lva.shop.R;
import com.lva.shop.ui.base.BaseFragment;
import com.lva.shop.ui.detail.ProfileActivity;
import com.lva.shop.ui.detail.WebActivity;
import com.lva.shop.ui.login.LoginActivity;
import com.lva.shop.ui.main.adapter.SettingAdapter;
import com.lva.shop.ui.main.model.Setting;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.Preference;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment {

    public static final String TAG = SettingFragment.class.getSimpleName();
    private static final int TYPE_LOGOUT = 0;
    List<Setting> settingList = new ArrayList<>();
    @BindView(R.id.iv_ava)
    ImageView ivAva;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.appBarLayout)
    ConstraintLayout appBarLayout;
    @BindView(R.id.rcv_feature)
    RecyclerView rcvFeature;
    @BindView(R.id.ll_feature)
    LinearLayout llFeature;
    @BindView(R.id.tv_logout)
    TextView tvLogout;

    private SettingAdapter settingAdapter;

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        setUnBinder(ButterKnife.bind(this, view));

        return view;
    }

    @Override
    protected void setUp(View view) {
        setUpAppBar();
        setData();
    }

    private void setUpAppBar() {
        if (Preference.getString(getBaseActivity(), AppConstants.ACCESS_TOKEN) != null) {
            tvLogin.setText("Logged in");
            tvLogout.setVisibility(View.VISIBLE);
        } else {
            tvLogin.setText("Login");
            tvLogout.setVisibility(View.GONE);
        }
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false);
        rcvFeature.setLayoutManager(layoutManager);
        settingAdapter = new SettingAdapter(getBaseActivity(), (setting, position) -> {
            switch (setting.getId()) {
                case 0:
                    //TODO API
                    if (Preference.getString(getBaseActivity(), AppConstants.ACCESS_TOKEN) != null) {
                        Intent intentProfile = new Intent(getBaseActivity(), ProfileActivity.class);
                        startActivity(intentProfile);
                    } else {
                        showDialogError(getString(R.string.you_need_login), 1);
                    }
                    break;
                case 1:
                    if (Preference.getString(getBaseActivity(), AppConstants.ACCESS_TOKEN) != null) {
                        //TODO API
                        Toast.makeText(getBaseActivity(), "history", Toast.LENGTH_SHORT).show();
                    } else {
                        showDialogError(getString(R.string.you_need_login), 1);
                    }
                    break;
                case 2:
                    break;
                case 3:
                    Intent intent = new Intent(getBaseActivity(), WebActivity.class);
                    intent.putExtra(AppConstants.TITLE, getString(R.string.help));
                    intent.putExtra(AppConstants.URL, "http://levananh.com/lien-he");
                    startActivity(intent);
                    break;

            }
        }, settingList);
        rcvFeature.setAdapter(settingAdapter);

    }

    private void setData() {
        settingList.clear();
        settingList.add(new Setting(0, R.mipmap.ic_user, getString(R.string.account_info)));
        settingList.add(new Setting(1, R.mipmap.ic_history, getString(R.string.history_ship)));
        settingList.add(new Setting(2, R.mipmap.ic_privacy, getString(R.string.policy)));
        settingList.add(new Setting(3, R.mipmap.ic_contact, getString(R.string.help)));
        setUpRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.appBarLayout, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.appBarLayout:
                if (Preference.getString(getBaseActivity(), AppConstants.ACCESS_TOKEN) != null) {
                    //TODO API
                    Intent intentProfile = new Intent(getBaseActivity(), ProfileActivity.class);
                    startActivity(intentProfile);
                } else {
                    Intent intentLogin = new Intent(getBaseActivity(), LoginActivity.class);
                    intentLogin.putExtra(AppConstants.LAUNCH_APP, false);
                    startActivityForResult(intentLogin,AppConstants.REQ_LOGIN_FROM_PROFILE);
                }
                break;
            case R.id.tv_logout:
                showDialogError(getString(R.string.logout_text), TYPE_LOGOUT);
                break;
        }
    }

    private void showDialogError(String string, int type) {
        new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.attention))
                .setContentText(string)
                .setCancelText(getString(R.string.skip))
                .setConfirmText(getString(R.string.ok))
                .showCancelButton(true)
                .setCancelClickListener(SweetAlertDialog::cancel)
                .setConfirmClickListener(sweetAlertDialog -> {
                    if(type == TYPE_LOGOUT){
                        sweetAlertDialog.cancel();
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        Preference.remove(getBaseActivity(), AppConstants.ACCESS_TOKEN);
                        Preference.remove(getBaseActivity(), AppConstants.URI_BANNER);
                        Preference.remove(getBaseActivity(), AppConstants.LIST_CART);
                        setUpAppBar();
                    } else {
                        sweetAlertDialog.cancel();
                        Intent intentLogin = new Intent(getBaseActivity(), LoginActivity.class);
                        intentLogin.putExtra(AppConstants.LAUNCH_APP, false);
                        startActivityForResult(intentLogin,AppConstants.REQ_LOGIN_FROM_PROFILE);
                    }

                })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppConstants.REQ_LOGIN_FROM_PROFILE && resultCode == AppConstants.LOGIN_RESULT){
            setUpAppBar();
            setData();
        }
    }
}
