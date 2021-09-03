/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.soma.MediationAdapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.smaato.soma.AdSettings;
import com.smaato.soma.video.ExtendedVASTAdListener;
import com.smaato.soma.video.VASTAdListener;
import com.smaato.soma.video.Video;

/**
 * Adapter class for AdMob to Smaato mediation for video interstitials.
 * For integration please follow the instructions from
 * <a href="https://wiki.smaato.com/display/IN/Android">Smaato Wiki</a> (section "Smaato as secondary").
 */
public class VideoAdMobMediationAdapter implements CustomEventInterstitial, ExtendedVASTAdListener {

    private Video video;
    private CustomEventInterstitialListener interstitialListener;
    private Handler mainHandler;

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        if (video != null) {
            video.destroy();
        }
    }

    @Override
    public void requestInterstitialAd(Context context,
            CustomEventInterstitialListener listener, String serverParameter,
            MediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        interstitialListener = listener;
        mainHandler = new Handler(Looper.getMainLooper());

        video = new Video(context);
        setAdIdsForAdSettings(serverParameter, video.getAdSettings());
        video.setVastAdListener(this);

        // Optional: You can also set user profile targeting parameters by updating the video.getUserSettings() object.
        // Please check the Smaato wiki for all available properties and further details.

        video.asyncLoadNewBanner();
    }

    @Override
    public void showInterstitial() {
        video.show();
    }

    @Override
    public void onReadyToShow() {
        interstitialListener.onAdLoaded();
    }

    @Override
    public void onWillShow() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                interstitialListener.onAdOpened();
            }
        });
    }

    @Override
    public void onWillOpenLandingPage() {
        interstitialListener.onAdClicked();
    }

    @Override
    public void onWillClose() {
        interstitialListener.onAdClosed();
    }

    @Override
    public void onFailedToLoadAd() {
        interstitialListener.onAdFailedToLoad(0);
    }

    @Override
    public void onWillLeaveApp() {
        interstitialListener.onAdLeftApplication();
    }

    private void setAdIdsForAdSettings(String serverParameter, AdSettings adSettings) {
        String[] serverParams = serverParameter.split("&");
        String publisherId = serverParams[0].split("=")[1];
        String adSpaceId = serverParams[1].split("=")[1];

        adSettings.setAdspaceId(Long.parseLong(adSpaceId));
        adSettings.setPublisherId(Long.parseLong(publisherId));
    }
}
