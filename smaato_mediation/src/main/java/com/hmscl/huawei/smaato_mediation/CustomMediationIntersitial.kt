/*
 * Copyright 2021. Explore in HMS. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * ou may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hmscl.huawei.smaato_mediation

import android.content.Context
import android.util.Log
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.InterstitialAd
import com.smaato.soma.ErrorCode
import com.smaato.soma.bannerutilities.constant.Values
import com.smaato.soma.debug.DebugCategory
import com.smaato.soma.debug.Debugger
import com.smaato.soma.debug.LogMessage
import com.smaato.soma.mediation.MediationEventInterstitial
import java.io.PrintWriter
import java.io.StringWriter

class CustomMediationIntersitial : MediationEventInterstitial() {
    val ADAPTER_NAME = CustomMediationIntersitial::class.java.simpleName

    private var mIntersitialListener: MediationEventInterstitialListener? = null

    // consider to have static and single instance based on the Adapter requirement
    private var mGoogleAdView: InterstitialAd? = null
    var width = 0
    var height = 0

    /*
     * The Method name could be changed as per the name given in the Smaato SPX portal, but the params should be fixed.
     */
    fun loadCustomIntersitial(
            context: Context?,
            mediationEventInterstitialListener: MediationEventInterstitialListener?,
            serverBundle: Map<String, String?>
    ) {
        Log.d(ADAPTER_NAME, "CustomMediationInterstitial - loadCustomInterstitial()")

        mIntersitialListener = mediationEventInterstitialListener
        if (!mediationInputsAreValid(serverBundle)) {
            Log.d(ADAPTER_NAME, "CustomMediationInterstitial - loadCustomInterstitial() - serverBundle not valid")
            mIntersitialListener!!.onInterstitialFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR)
            return
        }
        try {
            mGoogleAdView = InterstitialAd(context)

            if (!serverBundle.containsKey(ID_KEY)) {
                Log.e(ADAPTER_NAME, "CustomMediationInterstitial - loadCustomInterstitial() - serverBundle is not contain AD_UNIT_ID")
            }
            mGoogleAdView!!.adId = serverBundle[ID_KEY]
            mGoogleAdView!!.adListener = AdViewListener()
            val adParam = AdParam.Builder().build()
            mGoogleAdView!!.loadAd(adParam)
            Log.d(ADAPTER_NAME, "CustomMediationInterstitial - loadCustomInterstitial() - adapter attempting to load ad")
        } catch (e: NoClassDefFoundError) {
            val stacktrace =
                    StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
            Log.e(ADAPTER_NAME, "CustomMediationInterstitial - loadCustomInterstitial() - Request Interstitial Ad Failed: $stacktrace")
            notifyMediationConfigIssues()
            return
        } catch (e: Exception) {
            val stacktrace =
                    StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
            Log.e(ADAPTER_NAME, "CustomMediationInterstitial - loadCustomInterstitial() - Request Interstitial Ad Failed: $stacktrace")
            notifyMediationException()
            return
        }
    }

    override fun showInterstitial() {
        Log.d(ADAPTER_NAME, "CustomMediationInterstitial - showInterstitial()")
        if (mGoogleAdView!!.isLoaded) {
            mGoogleAdView!!.show()
        }
    }

    override fun onInvalidate() {
        Log.d(ADAPTER_NAME, "CustomMediationInterstitial - onInvalidate()")
        try {
            Debugger.showLog(
                    LogMessage(
                            TAG,
                            "hata invalidate",
                            Debugger.Level_1,
                            DebugCategory.DEBUG
                    )
            )
            destroy()
        } catch (e: NoClassDefFoundError) {
            val stacktrace =
                    StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
            Log.e(ADAPTER_NAME, "CustomMediationInterstitial - onInvalidate() - OnInvalidate call failed: $stacktrace")
            notifyMediationConfigIssues()
            return
        } catch (e: Exception) {
            notifyMediationException()
            return
        }
    }

    fun destroy() {
        Log.d(ADAPTER_NAME, "CustomMediationInterstitial - destroy()")
        try {
            if (mGoogleAdView != null) {
                Debugger.showLog(
                        LogMessage(
                                TAG,
                                "hata destroy",
                                Debugger.Level_1,
                                DebugCategory.DEBUG
                        )
                )
            }
        } catch (e: NoClassDefFoundError) {
            val stacktrace =
                    StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
            Log.e(ADAPTER_NAME, "CustomMediationInterstitial - onInvalidate() - OnDestroy call failed: $stacktrace")
            return
        } catch (e: Exception) {
            val stacktrace =
                    StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
            Log.e(ADAPTER_NAME, "CustomMediationInterstitial - onInvalidate() - OnDestroy call failed: $stacktrace")
            return
        }
    }

    private fun mediationInputsAreValid(serverBundle: Map<String, String?>?): Boolean {
        try {
            Log.d(ADAPTER_NAME, "CustomMediationInterstitial - mediationInputsAreValid()")
            if (serverBundle == null) {
                Log.e(ADAPTER_NAME, "CustomMediationInterstitial - mediationInputsAreValid() - serverBundle is null")
                return false
            }
            try {
                // Check and update whether widht and Height are needed for your custom Adapter
                if (serverBundle[Values.MEDIATION_WIDTH] != null && serverBundle[Values.MEDIATION_HEIGHT] != null) {
                    width = Integer.valueOf(serverBundle[Values.MEDIATION_WIDTH])
                    height = Integer.valueOf(serverBundle[Values.MEDIATION_HEIGHT])
                } else {
                    Log.e(ADAPTER_NAME, "CustomMediationInterstitial - mediationInputsAreValid() - serverBundle[Values.MEDIATION_WIDTH] or serverBundle[Values.MEDIATION_HEIGHT] is null")
                }
            } catch (e: Exception) { // check if width ht params are mandatory return false;
                val stacktrace =
                        StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
                Log.e(ADAPTER_NAME, "CustomMediationInterstitial - mediationInputsAreValid() - Failed: $stacktrace")
            }

            // ### Needs to be updated as per Custom Network Mandatory Fields
            if (serverBundle != null && !serverBundle[ID_KEY]!!
                            .isEmpty()
            ) {
                Log.d(ADAPTER_NAME, "CustomMediationInterstitial - mediationInputsAreValid() - serverBundle { AD_UNIT_ID } = $serverBundle[ID_KEY]")
                return true
            }
        } catch (e: Exception) {
            val stacktrace =
                    StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
            Log.e(ADAPTER_NAME, "CustomMediationInterstitial - mediationInputsAreValid() - Failed: $stacktrace")
            return false
        }
        return false
    }

    inner class AdViewListener : AdListener() {
        override fun onAdClosed() {
            Log.d(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - onAdClosed()")

        }
        override fun onAdFailed(errorCode: Int) {
            Log.d(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - onAdClosed()")
            try {
                Debugger.showLog(
                        LogMessage(
                                TAG,
                                "Google Play Services interstitial ad failed to load.",
                                Debugger.Level_1,
                                DebugCategory.DEBUG
                        )
                )
                if (mIntersitialListener != null) {

                    Log.e(
                            ADAPTER_NAME,
                            "CustomMediationInterstitial - AdViewListener - onAdFailed() - InterstitialListener is not null, no network fill"
                    )
                    mIntersitialListener!!.onInterstitialFailed(ErrorCode.NETWORK_NO_FILL)
                }
                else{
                    Log.e(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - onAdFailed() - InterstitialListener is null")
                }
                if (mGoogleAdView != null) {
                }
                onInvalidate()
            } catch (e: NoClassDefFoundError) {
                val stacktrace =
                        StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
                Log.e(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - onAdFailed() : OnFailed call failed : $stacktrace")
                notifyMediationConfigIssues()
                return
            } catch (e: Exception) {
                val stacktrace =
                        StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
                Log.e(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - onAdFailed() : OnFailed call failed : $stacktrace")
                notifyMediationException()
                return
            }
        }

        override fun onAdLeave() {
            Log.d(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - onAdLeave()")
            // cleanup
            onInvalidate()
        }

        override fun onAdLoaded() {
            Log.d(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - onAdLoaded()")
            try {
                Debugger.showLog(
                        LogMessage(
                                TAG,
                                " ad loaded successfully",
                                Debugger.Level_1,
                                DebugCategory.DEBUG
                        )
                )
                if (mIntersitialListener != null) {
                    mIntersitialListener!!.onInterstitialLoaded()
                }else{
                    Log.d(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - onAdLoaded() - InterstitialListener is null")
                }
            } catch (e: NoClassDefFoundError) {
                val stacktrace =
                        StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
                Log.e(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - onAdLoaded() : OnAdLoaded call failed : $stacktrace")
                notifyMediationConfigIssues()
                return
            } catch (e: Exception) {
                val stacktrace =
                        StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
                Log.e(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - onAdLoaded() : OnAdLoaded call failed : $stacktrace")
                notifyMediationException()
                return
            }
        }

        override fun onAdOpened() {
            Log.d(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - onAdOpened()")

            Debugger.showLog(
                    LogMessage(
                            TAG,
                            " Interstitial ad clicked.",
                            Debugger.Level_1,
                            DebugCategory.DEBUG
                    )
            )
            if (mIntersitialListener != null) {
                mIntersitialListener!!.onInterstitialClicked()
            }
        }
    }

    private fun notifyMediationConfigIssues() {
        Log.d(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - notifyMediationConfigIssues()")

        Debugger.showLog(
                LogMessage(
                        TAG,
                        "Dependencies missing. Check configurations of " + TAG,
                        Debugger.Level_1,
                        DebugCategory.ERROR
                )
        )
        mIntersitialListener!!.onInterstitialFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR)
        onInvalidate()
    }

    private fun notifyMediationException() {
        Log.d(ADAPTER_NAME, "CustomMediationInterstitial - AdViewListener - notifyMediationException()")

        Debugger.showLog(
                LogMessage(
                        TAG,
                        "Exception happened with Mediation inputs. Check in " + TAG,
                        Debugger.Level_1,
                        DebugCategory.ERROR
                )
        )
        mIntersitialListener!!.onInterstitialFailed(ErrorCode.GENERAL_ERROR)
        onInvalidate()
    }

    companion object {
        /**
         * KEY_STRING needs to be updated and to be matched with SPX PORTAL inputs
         */
        private const val ID_KEY = "AD_UNIT_ID"

        /**
         * TAG used for Log.
         */
        private const val TAG = "CustomMediationInterstitialAdapterSample"
    }
}