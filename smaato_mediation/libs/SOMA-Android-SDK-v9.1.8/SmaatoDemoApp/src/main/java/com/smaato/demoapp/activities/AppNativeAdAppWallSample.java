/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.demoapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

public class AppNativeAdAppWallSample extends Activity implements OnClickListener,
		AdListenerInterface {
	NativeAd nativeAd;
	RelativeLayout nativeRelativeLayout;
	Button reloadBanner;

	static String TAG= AppNativeAdAppWallSample.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_native_ad_app_wall_sample);
		reloadBanner = (Button) findViewById(R.id.load_ad );
		reloadBanner.setOnClickListener(this);
		nativeAd = new NativeAd(getApplicationContext());
		nativeAd.setAdListener(this);
		nativeRelativeLayout = (RelativeLayout) findViewById(R.id.nativeAdLayout);

		SharedPreferences prefs = this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		nativeAd.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID, "0")));
		nativeAd.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID, "0")));


		// Non Mandatory
		nativeAd.setTitleTextSize(15);
		//nativeAd.setWidthWithoutDensity(80);  // density calculation happens inside SDK for icons width
		//nativeAd.setHeightWithoutDensity(80); // default value is 70*density

		nativeAd.setMainLayout(nativeRelativeLayout);
		requestNewAd();

	}

	@Override
	public void onClick(View v) {
		requestNewAd();
	}

	public void requestNewAd() {

		nativeAd.asyncLoadNativeType(NativeAd.NativeType.APP_WALL, new NativeAd.NativeAdTypeListener() {
			@Override
			public void onError(ErrorCode errorCode, String errorMessage) {
				// No Ad Response is fine. Check for errorCode & errorMessage.
				Log.e("OnErrorResponse", "" + errorCode + " " + errorMessage);
			}

			@Override
			public void onAdResponse(ViewGroup nativeView) {
				if (nativeView != null) {
					Log.d(TAG, "Generated RelativeLayout (APP_WALL) view received");
					// Configure & Format as required on the generated Layout elements before binding.
					nativeAd.getIconImageView().setBackgroundColor(Color.BLUE);
					nativeAd.getTitleView().setTextColor(Color.BLUE);
					// IMPORTANT step to be invoked after attaching ad response to Screen.
					// IMPORTANT FOR IMPRESSIONS COUNTS
					nativeAd.bindAdResponse(nativeView);
				}
			}
		});

	}

	@Override
	public void onReceiveAd(AdDownloaderInterface sender,
			ReceivedBannerInterface receivedBanner) {
		if (receivedBanner.getErrorCode() != ErrorCode.NO_ERROR) {
			Toast.makeText(getApplicationContext(),
					receivedBanner.getErrorMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onDestroy() {
		if(nativeAd!=null){
			nativeAd.destroy();
		}

		super.onDestroy();
	}
}
