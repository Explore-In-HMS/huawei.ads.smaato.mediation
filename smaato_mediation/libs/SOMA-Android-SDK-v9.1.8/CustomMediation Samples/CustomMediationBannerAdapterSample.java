package com.smaato.soma.mediation;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.smaato.soma.ErrorCode;
import com.smaato.soma.bannerutilities.constant.Values;
import com.smaato.soma.debug.DebugCategory;
import com.smaato.soma.debug.Debugger;
import com.smaato.soma.debug.LogMessage;

import java.util.Map;

import static com.google.android.gms.ads.AdSize.BANNER;
import static com.google.android.gms.ads.AdSize.FULL_BANNER;
import static com.google.android.gms.ads.AdSize.LEADERBOARD;
import static com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE;


/*
 *  Sample Custom Adapter implementation using Google's AdMob
 */
public class CustomMediationBannerAdapterSample extends MediationEventBanner {


    /**
     * KEY_STRING needs to be updated and to be matched with SPX PORTAL inputs
     */
    private static final String ID_KEY = "AD_UNIT_ID";


    /**
     * TAG used for Log.
     */
    private static final String TAG = "CustomMediationBannerAdapterSample";


    private MediationEventBannerListener mBannerListener;

    // consider to have static and single instance based on the Adapter requirement
    private AdView mGoogleAdView;
    int width = 0;
    int height = 0;

    /*
     * The Method name could be changed as per the name given in the Smaato SPX portal, but the params should be fixed.
     */
    public void loadCustomBanner(Context context, MediationEventBannerListener mediationEventBannerListener, Map<String, String> serverBundle) {

        mBannerListener = mediationEventBannerListener;

        if (!mediationInputsAreValid(serverBundle)) {
            mBannerListener.onBannerFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        try {

            mGoogleAdView = new AdView(context);
            mGoogleAdView.setAdListener(new AdViewListener());
            mGoogleAdView.setAdUnitId(serverBundle.get(ID_KEY));

            // default is Banner
            AdSize adSize = AdSize.BANNER;

            adSize =   calculateAdSize(width, height);

            if (adSize == null) {
                // Wrong dimension will get defaulted to BANNER
                adSize = AdSize.BANNER;
            }

            mGoogleAdView.setAdSize(adSize);


            final AdRequest adRequest = new AdRequest.Builder()
                    .setRequestAgent(Values.MEDIATION_AGENT)
                    .addTestDevice("9CD6511F651989A0A3ED8B1928D84152")
                    .build();


            mGoogleAdView.loadAd(adRequest);
        } catch (NoClassDefFoundError e) {
            notifyMediationConfigIssues();
            return;
        } catch (Exception e) {
            notifyMediationException();
            return;
        }


    }


    @Override
    public void onInvalidate() {
        try {
            Views.removeFromParent(mGoogleAdView);
            destroy();


        } catch (NoClassDefFoundError e) {
            notifyMediationConfigIssues();
            return;
        } catch (Exception e) {
            notifyMediationException();
            return;
        }

    }


    public void destroy() {
        try {

            if (mGoogleAdView != null) {
                mGoogleAdView.destroy();
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

            try{
                // Check and update whether widht and Height are needed for your custom Adapter
                if(serverBundle.get(Values.MEDIATION_WIDTH)!=null && serverBundle.get(Values.MEDIATION_HEIGHT) !=null){
                    width = Integer.valueOf(serverBundle.get(Values.MEDIATION_WIDTH));
                    height = Integer.valueOf(serverBundle.get(Values.MEDIATION_HEIGHT));
                }
            } catch (Exception ex) { // check if width ht params are mandatory return false;
             }

            // ### Needs to be updated as per Custom Network Mandatory Fields
            if (serverBundle != null && !serverBundle.get(ID_KEY).isEmpty() )
                return true;


        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public AdSize calculateAdSize(int width, int height) {
        // Use the smallest AdSize that will properly contain the adView
        if (width <= BANNER.getWidth() && height <= BANNER.getHeight()) {
            return BANNER;
        } else if (width <= MEDIUM_RECTANGLE.getWidth() && height <= MEDIUM_RECTANGLE.getHeight()) {
            return MEDIUM_RECTANGLE;
        } else if (width <= FULL_BANNER.getWidth() && height <= FULL_BANNER.getHeight()) {
            return FULL_BANNER;
        } else if (width <= LEADERBOARD.getWidth() && height <= LEADERBOARD.getHeight()) {
            return LEADERBOARD;
        } else {
            return null;
        }
    }

    public class AdViewListener extends AdListener {
        /*
         * Google Play Services AdListener implementation
         */
        @Override
        public void onAdClosed() {

        }

        @Override
        public void onAdFailedToLoad(int errorCode) {

            try {
                Debugger.showLog(new LogMessage(TAG,
                        "Google Play Services banner ad failed to load.",
                        Debugger.Level_1,
                        DebugCategory.DEBUG));

                if (mBannerListener != null) {
                    mBannerListener.onBannerFailed(ErrorCode.NETWORK_NO_FILL);
                }

                if (mGoogleAdView != null) {
                    mGoogleAdView.pause();
                }

                onInvalidate();

            } catch (NoClassDefFoundError e) {
                notifyMediationConfigIssues();
                return;
            } catch (Exception e) {
                notifyMediationException();
                return;
            }

        }

        @Override
        public void onAdLeftApplication() {

            // cleanup
            onInvalidate();
        }

        @Override
        public void onAdLoaded() {

            try {
                Debugger.showLog(new LogMessage(TAG,
                        "Google Play banner ad loaded successfully",
                        Debugger.Level_1,
                        DebugCategory.DEBUG));

                if (mBannerListener != null) {
                    mBannerListener.onReceiveAd(mGoogleAdView);
                }

            } catch (NoClassDefFoundError e) {
                notifyMediationConfigIssues();
                return;
            } catch (Exception e) {
                notifyMediationException();
                return;
            }

        }

        @Override
        public void onAdOpened() {

            Debugger.showLog(new LogMessage(TAG,
                    "Google Play Services banner ad clicked.",
                    Debugger.Level_1,
                    DebugCategory.DEBUG));

            if (mBannerListener != null ) {
                mBannerListener.onBannerClicked();
            }
        }
    }


    private void notifyMediationConfigIssues() {

        Debugger.showLog(new LogMessage(TAG,
                "Dependencies missing. Check configurations of " + TAG,
                Debugger.Level_1,
                DebugCategory.ERROR));

        mBannerListener.onBannerFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR);
        onInvalidate();
    }

    private void notifyMediationException() {

        Debugger.showLog(new LogMessage(TAG,
                "Exception happened with Mediation inputs. Check in "+TAG,
                Debugger.Level_1,
                DebugCategory.ERROR));

        mBannerListener.onBannerFailed(ErrorCode.GENERAL_ERROR);

        onInvalidate();
    }

}
