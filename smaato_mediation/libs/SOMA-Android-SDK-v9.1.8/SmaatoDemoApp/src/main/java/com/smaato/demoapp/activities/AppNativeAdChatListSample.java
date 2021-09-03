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

public class AppNativeAdChatListSample extends Activity implements OnClickListener,
		AdListenerInterface {
	NativeAd nativeAd;
	RelativeLayout nativeRelativeLayout;
	RelativeLayout nativeRelativeLayout2;
	NativeAd nativeAd2;

	Button reloadBanner;

	static String TAG= AppNativeAdChatListSample.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_native_ad_chat_list_sample);
		reloadBanner = (Button) findViewById(R.id.load_ad );
		reloadBanner.setOnClickListener(this);
		nativeAd = new NativeAd(getApplicationContext());
		nativeAd.setAdListener(this);
		nativeRelativeLayout = (RelativeLayout) findViewById(R.id.nativeAdLayout);


		SharedPreferences prefs = this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		nativeAd.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID, "0")));
		nativeAd.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID, "0")));
		nativeAd.setMainLayout(nativeRelativeLayout);

		nativeAd2 = new NativeAd(getApplicationContext());
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
		nativeAd.asyncLoadNativeType(NativeAd.NativeType.CHAT_LIST, nativeAdTypeListener);
	}



	NativeAd.NativeAdTypeListener nativeAdTypeListener = new NativeAd.NativeAdTypeListener() {
		@Override
		public void onError(ErrorCode errorCode, String errorMessage) {
			Log.e(TAG, "" + errorCode + " " + errorMessage);
		}

		@Override
		public void onAdResponse(ViewGroup nativeView) {
			if (nativeView != null) {
				Log.d(TAG, "Layout view received & Added for NativeAd_1");
				// IMPORTANT step to be invoked after attaching ad response to Screen.
				// IMPORTANT FOR IMPRESSIONS COUNTS
				// To customize based on your need

				if(nativeAd.getTextView()!=null){
					nativeAd.getTextView().setTextColor(Color.BLUE);
					nativeAd.getTextView().setTextSize(10);
				}

				if(nativeAd.getMainImageView()!=null){
					nativeAd.getMainImageView().setMaxWidth(300);
				}

				if(nativeAd.getIconImageView()!=null){
					nativeAd.getIconImageView().setMaxHeight(30);
					nativeAd.getIconImageView().setMaxWidth(30);
				}

				if(nativeAd.getRatingBarView()!=null){
					nativeAd.getRatingBarView().setRating(2);
				}

				if(nativeAd.getTitleView()!=null){
					nativeAd.getTitleView().setTextColor(Color.BLUE);
				}

				if(nativeAd.getClickToActionButton()!=null){
					nativeAd.getClickToActionButton().setTextColor(Color.BLUE);
				}

				nativeAd.bindAdResponse(nativeView);
			}
		}
	};

	public void requestNewSecondAd() {

		nativeAd2.asyncLoadNativeType(NativeAd.NativeType.CHAT_LIST, new NativeAd.NativeAdTypeListener() {
			@Override
			public void onError(ErrorCode errorCode, String errorMessage) {
				Log.e(TAG, "" + errorCode + " " + errorMessage);
			}

			@Override
			public void onAdResponse(ViewGroup nativeView) {
				if (nativeView != null) {

					if(nativeAd2.getTextView()!=null){
						nativeAd2.getTextView().setTextColor(Color.BLUE);
						nativeAd2.getTextView().setTextSize(10);
					}

					if(nativeAd2.getTitleView()!=null){
						nativeAd2.getTitleView().setTextColor(Color.BLUE);
					}

					// IMPORTANT step to be invoked after attaching ad response to Screen.
					// IMPORTANT FOR IMPRESSIONS COUNTS
					// To customize based on your need
					nativeAd2.bindAdResponse(nativeView);
				}
			}
		});

	}

	// only for first NativeAd
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