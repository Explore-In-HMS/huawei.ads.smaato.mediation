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
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.InterstitialAd
import com.smaato.soma.ErrorCode
import com.smaato.soma.bannerutilities.constant.Values
import com.smaato.soma.debug.DebugCategory
import com.smaato.soma.debug.Debugger
import com.smaato.soma.debug.LogMessage
import com.smaato.soma.mediation.MediationEventInterstitial

class CustomMediationIntersitial : MediationEventInterstitial() {
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
        mediationEventBannerListener: MediationEventInterstitialListener?,
        serverBundle: Map<String, String?>
    ) {
        mIntersitialListener = mediationEventBannerListener
        if (!mediationInputsAreValid(serverBundle)) {
            mIntersitialListener!!.onInterstitialFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR)
            return
        }
        try {
            mGoogleAdView = InterstitialAd(context)
            mGoogleAdView!!.adId = serverBundle[ID_KEY]
            mGoogleAdView!!.adListener = AdViewListener()
            val adParam = AdParam.Builder().build()
            mGoogleAdView!!.loadAd(adParam)
        } catch (e: NoClassDefFoundError) {
            notifyMediationConfigIssues()
            return
        } catch (e: Exception) {
            notifyMediationException()
            return
        }
    }

    override fun showInterstitial() {
        if (mGoogleAdView!!.isLoaded) {
            mGoogleAdView!!.show()
        }
    }

    override fun onInvalidate() {
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
            notifyMediationConfigIssues()
            return
        } catch (e: Exception) {
            notifyMediationException()
            return
        }
    }

    fun destroy() {
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
            return
        } catch (e: Exception) {
            return
        }
    }

    private fun mediationInputsAreValid(serverBundle: Map<String, String?>?): Boolean {
        try {
            if (serverBundle == null) return false
            try {
                // Check and update whether widht and Height are needed for your custom Adapter
                if (serverBundle[Values.MEDIATION_WIDTH] != null && serverBundle[Values.MEDIATION_HEIGHT] != null) {
                    width = Integer.valueOf(serverBundle[Values.MEDIATION_WIDTH])
                    height = Integer.valueOf(serverBundle[Values.MEDIATION_HEIGHT])
                }
            } catch (ex: Exception) { // check if width ht params are mandatory return false;
            }

            // ### Needs to be updated as per Custom Network Mandatory Fields
            if (serverBundle != null && !serverBundle[ID_KEY]!!
                    .isEmpty()
            ) return true
        } catch (e: Exception) {
            return false
        }
        return false
    }

    inner class AdViewListener : AdListener() {
        override fun onAdClosed() {}
        override fun onAdFailed(errorCode: Int) {
            try {
                Debugger.showLog(
                    LogMessage(
                        TAG,
                        "Google Play Services banner ad failed to load.",
                        Debugger.Level_1,
                        DebugCategory.DEBUG
                    )
                )
                if (mIntersitialListener != null) {
                    mIntersitialListener!!.onInterstitialFailed(ErrorCode.NETWORK_NO_FILL)
                }
                if (mGoogleAdView != null) {
                }
                onInvalidate()
            } catch (e: NoClassDefFoundError) {
                notifyMediationConfigIssues()
                return
            } catch (e: Exception) {
                notifyMediationException()
                return
            }
        }

        override fun onAdLeave() {

            // cleanup
            onInvalidate()
        }

        override fun onAdLoaded() {
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
                }
            } catch (e: NoClassDefFoundError) {
                notifyMediationConfigIssues()
                return
            } catch (e: Exception) {
                notifyMediationException()
                return
            }
        }

        override fun onAdOpened() {
            Debugger.showLog(
                LogMessage(
                    TAG,
                    " banner ad clicked.",
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
        private const val TAG = "CustomMediationBannerAdapterSample"
    }
}