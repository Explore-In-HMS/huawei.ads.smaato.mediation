/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.demoapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.smaato.demoapp.R;
import com.smaato.demoapp.utils.Constants;
import com.smaato.soma.AdDownloaderInterface;
import com.smaato.soma.AdListenerInterface;
import com.smaato.soma.ErrorCode;
import com.smaato.soma.ReceivedBannerInterface;
import com.smaato.soma.nativead.NativeAd;

public class NativeAdCarouselSample extends Activity implements OnClickListener
		 {
	NativeAd nativeAd;
	RelativeLayout nativeRelativeLayout;
	RelativeLayout nativeRelativeLayout2;
	NativeAd nativeAd2;

	Button reloadBanner;

	static String TAG= NativeAdCarouselSample.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native_ad_carousel_sample);
		reloadBanner = (Button) findViewById(R.id.load_ad );
		reloadBanner.setOnClickListener(NativeAdCarouselSample.this);
		nativeAd = new NativeAd(this);
		nativeRelativeLayout = (RelativeLayout) findViewById(R.id.nativeAdLayout);

		SharedPreferences prefs = NativeAdCarouselSample.this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		nativeAd.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID, "0")));
		nativeAd.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID, "0")));
		nativeAd.setMainLayout(nativeRelativeLayout);

		nativeAd2 = new NativeAd(this);
		nativeRelativeLayout2 = (RelativeLayout) findViewById(R.id.nativeAdLayout2);
		nativeAd2.setAdListener(new AdListenerInterface() {
			@Override
			public void onReceiveAd(AdDownloaderInterface sender, ReceivedBannerInterface receivedBanner) {
				if (receivedBanner.getErrorCode() != ErrorCode.NO_ERROR) {
					Toast.makeText(getApplicationContext(), "2nd Req failed" + receivedBanner.getErrorMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});


		nativeAd2.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID, "0")));
		nativeAd2.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID, "0")));
		nativeAd2.setMainLayout(nativeRelativeLayout2);

		requestNewAd();
		requestNewSecondAd();

	}

	@Override
	public void onClick(View v) {
		requestNewAd();
	}

	public void requestNewAd() {
		nativeAd.asyncLoadNativeType(NativeAd.NativeType.CAROUSEL, nativeAdTypeListener);
	}


	NativeAd.NativeAdTypeListener nativeAdTypeListener = new NativeAd.NativeAdTypeListener() {
		@Override
		public void onError(ErrorCode errorCode, String errorMessage) {
			Log.e(TAG, "" + errorCode + " " + errorMessage);
		}

		@Override
		public void onAdResponse(ViewGroup nativeView) {
			if (nativeView != null) {
				// IMPORTANT step to be invoked after attaching ad response to Screen.
				// IMPORTANT FOR IMPRESSIONS COUNTS
				nativeAd.bindAdResponse(nativeView);
			}
		}
	};

	public void requestNewSecondAd() {

		nativeAd2.asyncLoadNativeType(NativeAd.NativeType.CAROUSEL, new NativeAd.NativeAdTypeListener() {
			@Override
			public void onError(ErrorCode errorCode, String errorMessage) {
				Log.e(TAG, "" + errorCode + " " + errorMessage);
			}

			@Override
			public void onAdResponse(ViewGroup nativeView) {
				if (nativeView != null) {
					// IMPORTANT step to be invoked after attaching ad response to Screen.
					// IMPORTANT FOR IMPRESSIONS COUNTS
					nativeAd2.bindAdResponse(nativeView);
				}
			}
		});

	}

	@Override
	public void onDestroy(){
		if(nativeAd != null) {
			nativeAd.destroy();
		}
		super.onDestroy();
	}
}