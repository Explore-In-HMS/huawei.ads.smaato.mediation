/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.soma.MediationAdapter;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.smaato.soma.AdDimension;
import com.smaato.soma.AdDimensionHelper;
import com.smaato.soma.AdDownloaderInterface;
import com.smaato.soma.AdListenerInterface;
import com.smaato.soma.AdSettings;
import com.smaato.soma.BannerStateListener;
import com.smaato.soma.BannerView;
import com.smaato.soma.BaseView;
import com.smaato.soma.ErrorCode;
import com.smaato.soma.ExtendedBannerStateListener;
import com.smaato.soma.ReceivedBannerInterface;

/**
 * Adapter class for AdMob to Smaato mediation for banners.
 * For integration please follow the instructions from
 * <a href="https://wiki.smaato.com/display/IN/Android">Smaato Wiki</a> (section "Smaato as secondary").
 */
public class AdMobMediationAdapter implements CustomEventBanner {

    private BannerView bannerView;

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        if (bannerView != null) {
            bannerView.destroy();
            bannerView = null;
        }
    }

    @Override
    public void requestBannerAd(final Context context, final CustomEventBannerListener listener,
            final String serverParameter, final AdSize size, final MediationAdRequest mediationAdRequest,
            final Bundle customEventExtras) {
        if (bannerView == null) {
            bannerView = new BannerView(context);
        }

        AdSettings adSettings = bannerView.getAdSettings();

        setAdIdsForAdSettings(serverParameter, adSettings);
        adSettings.setBannerHeight(size.getHeight());
        adSettings.setBannerWidth(size.getWidth());
        AdDimension adDimension = AdDimensionHelper.getAdDimensionForValues(size.getHeight(), size.getWidth());

        if (adDimension != null) {
            adSettings.setAdDimension(adDimension);
        }

        bannerView.setAdSettings(adSettings);
        bannerView.addAdListener(new AdListenerInterface() {

            @Override
            public void onReceiveAd(AdDownloaderInterface arg0,
                    ReceivedBannerInterface arg1) {
                if (arg1.getErrorCode() == ErrorCode.NO_ERROR) {
                    listener.onAdLoaded(bannerView);
                } else if (arg1.getErrorCode() == ErrorCode.NO_AD_AVAILABLE) {
                    listener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
                } else if (arg1.getErrorCode() == ErrorCode.NO_CONNECTION_ERROR) {
                    listener.onAdFailedToLoad(AdRequest.ERROR_CODE_NETWORK_ERROR);
                } else {
                    listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
                }
            }
        });

        bannerView.setBannerStateListener(new ExtendedBannerStateListener() {

            @Override
            public void onWillLeaveApp() {
                listener.onAdLeftApplication();
            }

            @Override
            public void onWillOpenLandingPage(BaseView arg0) {
                listener.onAdClicked();
                listener.onAdOpened();
            }

            @Override
            public void onWillCloseLandingPage(BaseView arg0) {
                listener.onAdClosed();
            }
        });

        // Optional: You can also set user profile targeting parameters by updating the bannerView.getUserSettings() object.
        // Please check the Smaato wiki for all available properties and further details.

        bannerView.asyncLoadNewBanner();
    }

    private void setAdIdsForAdSettings(String serverParameter, AdSettings adSettings) {
        String[] serverParams = serverParameter.split("&");
        String publisherId = serverParams[0].split("=")[1];
        String adSpaceId = serverParams[1].split("=")[1];

        adSettings.setAdspaceId(Long.parseLong(adSpaceId));
        adSettings.setPublisherId(Long.parseLong(publisherId));
    }
}
