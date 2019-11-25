package com.lva.shop.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lva.shop.R;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends BaseActivity implements Player.EventListener {

    @BindView(R.id.videoFullScreenPlayer)
    PlayerView videoFullScreenPlayer;
    @BindView(R.id.spinnerVideoDetails)
    ProgressBar spinnerVideoDetails;
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.rl_close)
    RelativeLayout rlClose;
    private String TAG = VideoActivity.class.getSimpleName();
    private SimpleExoPlayer player;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);
        setUnBinder(ButterKnife.bind(this));
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getIntent() != null) {
            url = getIntent().getStringExtra(AppConstants.VIDEO_URL);
        }
        initializePlayer();
    }

    private void initializePlayer() {
        videoFullScreenPlayer.setControllerVisibilityListener(visibility -> {
            if (visibility == 0) {
                rlClose.setVisibility(View.VISIBLE);
            } else {
                rlClose.setVisibility(View.GONE);
            }
        });
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        DefaultTrackSelector.Parameters defaultTrackParam = trackSelector.buildUponParameters().build();
        trackSelector.setParameters(defaultTrackParam);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        DefaultDataSourceFactory fac = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "video"));
        videoFullScreenPlayer.setPlayer(player);
        MediaSource videoSource = new ExtractorMediaSource.Factory(fac)
                .createMediaSource(Uri.parse(url));
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        player.addListener(this);
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

    @OnClick({R.id.videoFullScreenPlayer, R.id.btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.videoFullScreenPlayer:
                break;
            case R.id.btn_close:
                onBackPressed();
                break;
        }
    }


    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }

    private void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        resumePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {

            case Player.STATE_BUFFERING:
                spinnerVideoDetails.setVisibility(View.VISIBLE);
                break;
            case Player.STATE_ENDED:
                // Activate the force enable
                break;
            case Player.STATE_IDLE:

                break;
            case Player.STATE_READY:
                spinnerVideoDetails.setVisibility(View.GONE);

                break;
            default:
                // status = PlaybackStatus.IDLE;
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @OnClick(R.id.rl_close)
    public void onViewClicked() {
    }
}
