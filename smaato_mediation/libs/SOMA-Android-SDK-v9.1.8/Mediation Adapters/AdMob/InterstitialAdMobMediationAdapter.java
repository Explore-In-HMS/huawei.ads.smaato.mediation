/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.soma.MediationAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.smaato.soma.AdSettings;
import com.smaato.soma.interstitial.ExtendedInterstitialAdListener;
import com.smaato.soma.interstitial.Interstitial;
import com.smaato.soma.interstitial.InterstitialAdListener;

/**
 * Adapter class for AdMob to Smaato mediation for image and rich media interstitials.
 * For integration please follow the instructions from
 * <a href="https://wiki.smaato.com/display/IN/Android">Smaato Wiki</a> (section "Smaato as secondary").
 *
 * @author Chouaieb Nabil
 */
public class InterstitialAdMobMediationAdapter implements CustomEventInterstitial, ExtendedInterstitialAdListener {

    private Interstitial interstitial;
    private CustomEventInterstitialListener customEventInterstitialListener;
    private Activity activity;

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        if (interstitial != null) {
            interstitial.destroy();
        }
    }

    @Override
    public void requestInterstitialAd(Context context,
            CustomEventInterstitialListener listener, String serverParameter,
            MediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        this.customEventInterstitialListener = listener;
        Activity activity = (Activity) context;
        this.activity = activity;
        interstitial = new Interstitial(activity);
        interstitial.setInterstitialAdListener(this);

        setAdIdsForAdSettings(serverParameter, interstitial.getAdSettings());

        // Optional: You can also set user profile targeting parameters by updating the interstitial.getUserSettings() object.
        // Please check the Smaato wiki for all available properties and further details.

        interstitial.asyncLoadNewBanner();
    }

    @Override
    public void onReadyToShow() {
        customEventInterstitialListener.onAdLoaded();
    }

    @Override
    public void onWillShow() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customEventInterstitialListener.onAdOpened();
            }
        });
    }

    @Override
    public void onWillOpenLandingPage() {
        customEventInterstitialListener.onAdClicked();
    }

    @Override
    public void onWillClose() {
        customEventInterstitialListener.onAdClosed();
    }

    @Override
    public void onFailedToLoadAd() {
        customEventInterstitialListener
                .onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
    }

    @Override
    public void onWillLeaveApp() {
        customEventInterstitialListener.onAdLeftApplication();
    }

    @Override
    public void showInterstitial() {
        interstitial.show();
    }

    private void setAdIdsForAdSettings(String serverParameter, AdSettings adSettings) {
        String[] serverParams = serverParameter.split("&");
        String publisherId = serverParams[0].split("=")[1];
        String adSpaceId = serverParams[1].split("=")[1];

        adSettings.setAdspaceId(Long.parseLong(adSpaceId));
        adSettings.setPublisherId(Long.parseLong(publisherId));
    }
}
