package com.lva.shop.ui.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.lva.shop.R;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbarLayout;
    @BindView(R.id.webview)
    WebView webview;
    private String TAG = WebActivity.class.getSimpleName();
    private String title, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setUnBinder(ButterKnife.bind(this));
        getDataIntent();
        setUpView();
    }

    private void getDataIntent() {
        if (getIntent() != null) {
            title = getIntent().getStringExtra(AppConstants.TITLE);
            url = getIntent().getStringExtra(AppConstants.URL);
            Log.e(TAG, "getDataIntent: "+ url );
        }
    }

    private void setUpView() {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setUpWebview(url);
    }

    private void setUpWebview(String url) {
        try {
            webview.clearCache(true);
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            WebSettings webSettings = webview.getSettings();
            webSettings.setDomStorageEnabled(true);
            webSettings.setBuiltInZoomControls(false);

            webSettings.setAllowFileAccess(true);
            webSettings.setDatabaseEnabled(true);

            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadsImagesAutomatically(true);
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    hideLoading();
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                }
            });
            webview.loadUrl(url);
            showLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
