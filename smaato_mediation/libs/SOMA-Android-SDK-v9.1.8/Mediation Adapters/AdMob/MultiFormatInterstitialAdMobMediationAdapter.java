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
import com.smaato.soma.multiadformat.MultiFormatInterstitial;

/**
 * Adapter class for AdMob to Smaato mediation for image, rich media and video interstitials.
 * For integration please follow the instructions from
 * <a href="https://wiki.smaato.com/display/IN/Android">Smaato Wiki</a> (section "Smaato as secondary").
 *
 * @author Chouaieb Nabil
 */
public class MultiFormatInterstitialAdMobMediationAdapter implements CustomEventInterstitial, ExtendedInterstitialAdListener {

    private Activity activity;
    private CustomEventInterstitialListener customEventInterstitialListener;
    private MultiFormatInterstitial multiFormatInterstitial;

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        if (multiFormatInterstitial != null) {
            multiFormatInterstitial.destroy();
        }
    }

    @Override
    public void requestInterstitialAd(Context context, CustomEventInterstitialListener listener, String serverParameter,
            MediationAdRequest mediationAdRequest, Bundle customEventExtras) {
        activity = (Activity) context;
        customEventInterstitialListener = listener;

        multiFormatInterstitial = new MultiFormatInterstitial(activity);
        multiFormatInterstitial.setInterstitialAdListener(this);
        setAdIdsForAdSettings(serverParameter, multiFormatInterstitial.getAdSettings());

        // Optional: You can also set user profile targeting parameters by updating the multiFormatInterstitial.getUserSettings() object.
        // Please check the Smaato wiki for all available properties and further details.

        multiFormatInterstitial.asyncLoadNewBanner();
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
        customEventInterstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
    }

    @Override
    public void onWillLeaveApp() {
        customEventInterstitialListener.onAdLeftApplication();
    }

    @Override
    public void showInterstitial() {
        multiFormatInterstitial.show();
    }

    private void setAdIdsForAdSettings(String serverParameter, AdSettings adSettings) {
        String[] serverParams = serverParameter.split("&");
        String publisherId = serverParams[0].split("=")[1];
        String adSpaceId = serverParams[1].split("=")[1];

        adSettings.setAdspaceId(Long.parseLong(adSpaceId));
        adSettings.setPublisherId(Long.parseLong(publisherId));
    }
}