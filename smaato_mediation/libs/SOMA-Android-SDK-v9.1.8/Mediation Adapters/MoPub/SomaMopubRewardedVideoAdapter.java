/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.soma.mopubcustomevent;

import android.app.Activity;
import android.text.TextUtils;

import com.mopub.common.BaseLifecycleListener;
import com.mopub.common.LifecycleListener;
import com.mopub.common.MoPubReward;
import com.mopub.mobileads.CustomEventRewardedVideo;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoManager;
import com.smaato.soma.AdSettings;
import com.smaato.soma.CrashReportTemplate;
import com.smaato.soma.video.RewardedVideo;
import com.smaato.soma.video.RewardedVideoListener;

import java.util.Map;

/**
 * Adapter class for MoPub to Smaato mediation for rewarded video interstitials.
 * For integration please follow the instructions from
 * <a href="https://wiki.smaato.com/display/IN/Android">Smaato Wiki</a> (section "Smaato as secondary").
 * </p>
 * Tested with Smaato v9.0 and Mopub v5.3.0.
 */
public class SomaMopubRewardedVideoAdapter extends CustomEventRewardedVideo implements RewardedVideoListener {

    private static final String PUBLISHER_ID = "publisherId";
    private static final String AD_SPACE_ID = "adSpaceId";

    private RewardedVideo rewardedVideo;
    private String adSpaceIdString;
    private BaseLifecycleListener lifecycleListener = new BaseLifecycleListener() {
        @Override
        public void onDestroy(Activity activity) {
            if (rewardedVideo != null) {
                rewardedVideo.destroy();
            }
        }
    };

    @Override
    public void onRewardedVideoStarted() {
        MoPubRewardedVideoManager.onRewardedVideoStarted(SomaMopubRewardedVideoAdapter.class, adSpaceIdString);
    }

    @Override
    public void onFirstQuartileCompleted() {
        // MoPub has no corresponding callback
    }

    @Override
    public void onSecondQuartileCompleted() {
        // MoPub has no corresponding callback
    }

    @Override
    public void onThirdQuartileCompleted() {
        // MoPub has no corresponding callback
    }

    @Override
    public void onRewardedVideoCompleted() {
        MoPubRewardedVideoManager.onRewardedVideoCompleted(
                SomaMopubRewardedVideoAdapter.class,
                adSpaceIdString,
                MoPubReward.success(MoPubReward.NO_REWARD_LABEL, MoPubReward.DEFAULT_REWARD_AMOUNT));
    }

    @Override
    public void onReadyToShow() {
        MoPubRewardedVideoManager.onRewardedVideoLoadSuccess(
                SomaMopubRewardedVideoAdapter.class,
                adSpaceIdString);
    }

    @Override
    public void onWillShow() {
        // MoPub has no corresponding callback
    }

    @Override
    public void onWillOpenLandingPage() {
        MoPubRewardedVideoManager.onRewardedVideoClicked(
                SomaMopubRewardedVideoAdapter.class,
                adSpaceIdString);
    }

    @Override
    public void onWillClose() {
        MoPubRewardedVideoManager.onRewardedVideoClosed(
                SomaMopubRewardedVideoAdapter.class,
                adSpaceIdString);
    }

    @Override
    public void onFailedToLoadAd() {
        MoPubRewardedVideoManager.onRewardedVideoLoadFailure(
                SomaMopubRewardedVideoAdapter.class,
                adSpaceIdString,
                MoPubErrorCode.UNSPECIFIED);
    }

    @Override
    protected LifecycleListener getLifecycleListener() {
        return lifecycleListener;
    }

    @Override
    protected boolean checkAndInitializeSdk(Activity launcherActivity, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        // not required for Smaato SDK
        return false;
    }

    @Override
    protected void loadWithSdkInitialized(final Activity activity, Map<String, Object> localExtras, final Map<String, String> serverExtras) {
        if (TextUtils.isEmpty(serverExtras.get(PUBLISHER_ID)) || TextUtils.isEmpty(serverExtras.get(AD_SPACE_ID))) {
            // Using class name as the network ID for this callback since the ad unit ID is
            // invalid.
            MoPubRewardedVideoManager.onRewardedVideoLoadFailure(
                    SomaMopubRewardedVideoAdapter.class,
                    SomaMopubRewardedVideoAdapter.class.getSimpleName(),
                    MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
        }

        new CrashReportTemplate<Void>() {
            @Override
            public Void process() {
                if (rewardedVideo == null) {
                    rewardedVideo = new RewardedVideo(activity);
                    rewardedVideo.setRewardedVideoListener(SomaMopubRewardedVideoAdapter.this);
                }

                adSpaceIdString = serverExtras.get(AD_SPACE_ID);
                setAdIdsForAdSettings(serverExtras, rewardedVideo.getAdSettings());

                // Optional: You can also set user profile targeting parameters by updating the rewardedVideo.getUserSettings() object.
                // Please check the Smaato wiki for all available properties and further details.

                rewardedVideo.asyncLoadNewBanner();

                return null;
            }
        }.execute();
    }

    @Override
    protected boolean hasVideoAvailable() {
        return rewardedVideo != null && rewardedVideo.isVideoPlayable();
    }

    @Override
    protected void showVideo() {
        if (hasVideoAvailable()) {
            rewardedVideo.show();
        } else {
            MoPubRewardedVideoManager.onRewardedVideoPlaybackError(
                    SomaMopubRewardedVideoAdapter.class,
                    adSpaceIdString,
                    MoPubErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    protected String getAdNetworkId() {
        return adSpaceIdString;
    }

    @Override
    protected void onInvalidate() {
        if (rewardedVideo != null) {
            rewardedVideo.destroy();
        }
    }

    private void setAdIdsForAdSettings(Map<String, String> serverExtras, AdSettings adSettings) {
        long publisherId = Long.parseLong(serverExtras.get(PUBLISHER_ID));
        long adSpaceId = Long.parseLong(serverExtras.get(AD_SPACE_ID));
        adSettings.setPublisherId(publisherId);
        adSettings.setAdspaceId(adSpaceId);
    }
}
