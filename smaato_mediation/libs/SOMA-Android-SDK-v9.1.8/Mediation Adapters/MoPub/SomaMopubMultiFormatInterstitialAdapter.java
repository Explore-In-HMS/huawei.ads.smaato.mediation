/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.soma.mopubcustomevent;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.mopub.mobileads.CustomEventInterstitial;
import com.mopub.mobileads.MoPubErrorCode;
import com.smaato.soma.AdSettings;
import com.smaato.soma.CrashReportTemplate;
import com.smaato.soma.debug.DebugCategory;
import com.smaato.soma.debug.Debugger;
import com.smaato.soma.debug.LogMessage;
import com.smaato.soma.interstitial.InterstitialAdListener;
import com.smaato.soma.multiadformat.MultiFormatInterstitial;

import java.util.Map;

/**
 * Adapter class for MoPub to Smaato mediation for image, rich media and video interstitials.
 * For integration please follow the instructions from
 * <a href="https://wiki.smaato.com/display/IN/Android">Smaato Wiki</a> (section "Smaato as secondary").
 * </p>
 * Tested with Smaato v9.0 and Mopub v5.3.0.
 *
 * @author Chouaieb Nabil
 */
public class SomaMopubMultiFormatInterstitialAdapter extends CustomEventInterstitial implements InterstitialAdListener {

    private static final String TAG = "SomaMopubMultiFormatInterstitialAdapter";
    private MultiFormatInterstitial multiFormatInterstitial;
    private CustomEventInterstitialListener customEventInterstitialListener;
    private Handler handler;

    @Override
    public void onFailedToLoadAd() {
        new CrashReportTemplate<Void>() {
            @Override
            public Void process() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.NO_FILL);
                    }
                });

                return null;
            }
        }.execute();
    }

    @Override
    public void onReadyToShow() {
        new CrashReportTemplate<Void>() {
            @Override
            public Void process() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        customEventInterstitialListener.onInterstitialLoaded();
                    }
                });

                return null;
            }
        }.execute();
    }

    @Override
    public void onWillClose() {
        new CrashReportTemplate<Void>() {
            @Override
            public Void process() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        customEventInterstitialListener.onInterstitialDismissed();
                    }
                });

                return null;
            }
        }.execute();
    }

    @Override
    public void onWillOpenLandingPage() {
        new CrashReportTemplate<Void>() {
            @Override
            public Void process() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        customEventInterstitialListener.onInterstitialClicked();
                    }
                });

                return null;
            }
        }.execute();
    }

    @Override
    public void onWillShow() {
        new CrashReportTemplate<Void>() {
            @Override
            public Void process() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        printDebugLogs("onWillShow ", DebugCategory.ERROR);
                        customEventInterstitialListener.onInterstitialShown();
                    }
                });

                return null;
            }
        }.execute();
    }

    @Override
    protected void loadInterstitial(Context context, final CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras,
            final Map<String, String> serverExtras) {
        this.customEventInterstitialListener = customEventInterstitialListener;
        handler = new Handler(Looper.getMainLooper());

        if (multiFormatInterstitial == null) {
            multiFormatInterstitial = new MultiFormatInterstitial(context);
            multiFormatInterstitial.setInterstitialAdListener(this);
        }

        // Optional: You can also set user profile targeting parameters by updating the multiFormatInterstitial.getUserSettings() object.
        // Please check the Smaato wiki for all available properties and further details.

        new CrashReportTemplate<Void>() {
            @Override
            public Void process() {
                setAdIdsForAdSettings(serverExtras, multiFormatInterstitial.getAdSettings());
                multiFormatInterstitial.asyncLoadNewBanner();

                return null;
            }
        }.execute();
    }

    @Override
    protected void onInvalidate() {
        if (multiFormatInterstitial != null) {
            multiFormatInterstitial.destroy();
            multiFormatInterstitial = null;
        }
    }

    @Override
    protected void showInterstitial() {
        new CrashReportTemplate<Void>() {
            @Override
            public Void process() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (multiFormatInterstitial.isReadyToShow()) {
                            multiFormatInterstitial.show();
                        }
                    }
                });

                return null;
            }
        }.execute();
    }

    private void setAdIdsForAdSettings(Map<String, String> serverExtras, AdSettings adSettings) {
        long publisherId = Long.parseLong(serverExtras.get("publisherId"));
        long adSpaceId = Long.parseLong(serverExtras.get("adSpaceId"));
        adSettings.setPublisherId(publisherId);
        adSettings.setAdspaceId(adSpaceId);
    }

    private void printDebugLogs(String str, DebugCategory debugCategory) {
        Debugger.showLog(new LogMessage(TAG, str, Debugger.Level_1, debugCategory));
    }
}