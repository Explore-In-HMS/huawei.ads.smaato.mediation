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
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.banner.BannerView
import com.smaato.soma.ErrorCode
import com.smaato.soma.bannerutilities.constant.Values
import com.smaato.soma.debug.DebugCategory
import com.smaato.soma.debug.Debugger
import com.smaato.soma.debug.LogMessage
import com.smaato.soma.mediation.MediationEventBanner
import com.smaato.soma.mediation.Views

class CustomMediationBanner : MediationEventBanner() {
    private var mBannerListener: MediationEventBannerListener? = null

    // consider to have static and single instance based on the Adapter requirement
    private var mHuaweiAdView: BannerView? = null
    var width = 0
    var height = 0

    /*
     * The Method name could be changed as per the name given in the Smaato SPX portal, but the params should be fixed.
     */
    fun loadCustomBanner(
        context: Context?,
        mediationEventBannerListener: MediationEventBannerListener?,
        serverBundle: Map<String, String?>
    ) {
        mBannerListener = mediationEventBannerListener
        if (!mediationInputsAreValid(serverBundle)) {
            mBannerListener!!.onBannerFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR)
            return
        }
        try {
            mHuaweiAdView = BannerView(context)
            mHuaweiAdView!!.adListener = AdViewListener()
            mHuaweiAdView!!.adId = serverBundle[ID_KEY]
            var adSize = BannerAdSize.BANNER_SIZE_320_50
            if (adSize == null) {
                // Wrong dimension will get defaulted to BANNER
                adSize = BannerAdSize.BANNER_SIZE_320_50
            }
            mHuaweiAdView!!.bannerAdSize = adSize
            val adParam = AdParam.Builder().build()
            mHuaweiAdView!!.loadAd(adParam)
        } catch (e: NoClassDefFoundError) {
            notifyMediationConfigIssues()
            return
        } catch (e: Exception) {
            notifyMediationException()
            return
        }
    }

    override fun onInvalidate() {
        try {
            Views.removeFromParent(mHuaweiAdView)
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
            if (mHuaweiAdView != null) {
                mHuaweiAdView!!.destroy()
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
            if (serverBundle != null && !serverBundle[ID_KEY]!!.isEmpty()) return true
        } catch (e: Exception) {
            return false
        }
        return false
    }

    inner class AdViewListener : AdListener() {
        /*
         * Google Play Services AdListener implementation
         */
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
                if (mBannerListener != null) {
                    mBannerListener!!.onBannerFailed(ErrorCode.NETWORK_NO_FILL)
                }
                if (mHuaweiAdView != null) {
                    mHuaweiAdView!!.pause()
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
                        "Google Play banner ad loaded successfully",
                        Debugger.Level_1,
                        DebugCategory.DEBUG
                    )
                )
                if (mBannerListener != null) {
                    mBannerListener!!.onReceiveAd(mHuaweiAdView)
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
                    "Google Play Services banner ad clicked.",
                    Debugger.Level_1,
                    DebugCategory.DEBUG
                )
            )
            if (mBannerListener != null) {
                mBannerListener!!.onBannerClicked()
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
        mBannerListener!!.onBannerFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR)
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
        mBannerListener!!.onBannerFailed(ErrorCode.GENERAL_ERROR)
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