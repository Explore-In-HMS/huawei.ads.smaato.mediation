package com.smaato.soma.mediation;

import android.content.Context;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.smaato.soma.ErrorCode;
import com.smaato.soma.bannerutilities.constant.Values;
import com.smaato.soma.debug.DebugCategory;
import com.smaato.soma.debug.Debugger;
import com.smaato.soma.debug.LogMessage;

import java.util.Map;

/*
 *  Sample Custom Adapter implementation for Interstitial using Google's AdMob
 */

public class CustomMediationInterstitialAdapterSample extends MediationEventInterstitial {

    private static String TAG = "CustomMediationInterstitialAdapterSample";
    private MediationEventInterstitialListener mSmaatoMediationInterstitialListener;

    /**
     * KEY_STRING needs to be updated and to be matched with as provided in SPX PORTAL
     */
    private static final String ID_KEY = "AD_UNIT_ID";

    private InterstitialAd mGoogleInterstitialAd;


    public void loadCustomInterstitial(Context context, MediationEventInterstitialListener mediationEventInterstitialListener, Map<String, String> serverBundle) {

        try {
        mSmaatoMediationInterstitialListener = mediationEventInterstitialListener;

        if (!mediationInputsAreValid(serverBundle)) {

            mSmaatoMediationInterstitialListener.onInterstitialFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }


            mGoogleInterstitialAd = new InterstitialAd(context);
            mGoogleInterstitialAd.setAdListener(new InterstitialAdListener());
            mGoogleInterstitialAd.setAdUnitId(serverBundle.get(ID_KEY));

            final AdRequest adRequest = new AdRequest.Builder()
                    .setRequestAgent(Values.MEDIATION_AGENT)
                    .addTestDevice("9CD6511F651989A0A3ED8B1928D84152")
                    .build();

            mGoogleInterstitialAd.loadAd(adRequest);

        } catch (RuntimeException e) {
            notifyMediationConfigIssues();
            return;

        } catch (NoClassDefFoundError e) {
            notifyMediationConfigIssues();
            return;
        }
        catch (Exception ex){
            notifyMediationException();
            return;
        }

    }

    @Override
    public void showInterstitial() {
        try {

            if (mGoogleInterstitialAd.isLoaded()) {
                mGoogleInterstitialAd.show();
            } else {

                Debugger.showLog(new LogMessage(TAG,
                        "Tried to show a MoPub interstitial ad, even before it finished loading. Please try again.",
                        Debugger.Level_1,
                        DebugCategory.ERROR));

            }

        } catch (NoClassDefFoundError e) {
            notifyMediationConfigIssues();
            return;
        }
        catch (Exception ex){
            notifyMediationException();
            return;
        }

    }



    @Override
    public void onInvalidate() {
        try {
        if (mGoogleInterstitialAd != null) {
            mGoogleInterstitialAd.setAdListener(null);
        }

        } catch (NoClassDefFoundError e) {
            return;
        } catch (Exception e) {
            return;
        }
    }

    private boolean mediationInputsAreValid(Map<String,String> serverBundle) {
        try {

            if (serverBundle == null)
                return false;

            // TODO Need to be updated as per Custom Network Mandatory Fields requirement
            if (serverBundle != null && !serverBundle.get(ID_KEY).isEmpty() )
                return true;

        } catch (Exception e) {
            return false;
        }
        return false;
    }


    private class InterstitialAdListener extends AdListener {
        /*
    	 * Google Play Services AdListener implementation
    	 */
        @Override
        public void onAdClosed() {

            if (mSmaatoMediationInterstitialListener != null) {
                mSmaatoMediationInterstitialListener.onInterstitialDismissed();
            }

            onInvalidate();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {

            try {
                Debugger.showLog(new LogMessage(TAG,
                        "Google Play Services interstitial ad failed to load.",
                        Debugger.Level_1,
                        DebugCategory.DEBUG));


                if (mSmaatoMediationInterstitialListener != null) {
                    mSmaatoMediationInterstitialListener.onInterstitialFailed(ErrorCode.NETWORK_NO_FILL);
                }

                // clean up
                onInvalidate();

            } catch (NoClassDefFoundError e) {
                notifyMediationConfigIssues();
                return;
            } catch (Exception ex) {
                notifyMediationException();
                return;
            }

        }

        @Override
        public void onAdLeftApplication() {

            if (mSmaatoMediationInterstitialListener != null)
                mSmaatoMediationInterstitialListener.onInterstitialClicked();

        }

        @Override
        public void onAdLoaded() {

            try {

                Debugger.showLog(new LogMessage(TAG,
                        "Google Play Services interstitial ad loaded successfully.",
                        Debugger.Level_1,
                        DebugCategory.DEBUG));

                if (mSmaatoMediationInterstitialListener != null) {
                    mSmaatoMediationInterstitialListener.onInterstitialLoaded();
                }


            } catch (NoClassDefFoundError e) {
                notifyMediationConfigIssues();
                return;
            } catch (Exception ex) {
                notifyMediationException();
                return;
            }
        }

    }



    private void notifyMediationConfigIssues() {
        Debugger.showLog(new LogMessage(TAG,
                "Dependencies missing. Check configurations of "+TAG,
                Debugger.Level_1,
                DebugCategory.ERROR));

        mSmaatoMediationInterstitialListener.onInterstitialFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR);
        onInvalidate();
    }

    private void notifyMediationException() {

        Debugger.showLog(new LogMessage(TAG,
                "Exception happened with Mediation inputs. Check in "+TAG,
                Debugger.Level_1,
                DebugCategory.ERROR));

        mSmaatoMediationInterstitialListener.onInterstitialFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR);
        onInvalidate();
    }

}
