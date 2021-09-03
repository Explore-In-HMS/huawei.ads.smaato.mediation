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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smaato.demoapp.R;
import com.smaato.demoapp.utils.Constants;
import com.smaato.soma.AdDownloaderInterface;
import com.smaato.soma.AdListenerInterface;
import com.smaato.soma.ErrorCode;
import com.smaato.soma.ReceivedBannerInterface;
import com.smaato.soma.nativead.NativeAd;

public class AppNativeAdSample extends Activity implements OnClickListener,
		AdListenerInterface {
	NativeAd nativeAd;
	ImageView iconImage;
	ImageView mainImage;
	TextView nativeText;
	Button ctaButton;
	TextView nativeTitle;
	RelativeLayout nativeRelativeLayout;
	RatingBar ratingBar;
	Button reloadBanner;

	static String TAG = "NativeAdSample";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_native_ad_sample);
		reloadBanner = (Button) findViewById(R.id.load_ad );
		reloadBanner.setOnClickListener(this);
		nativeAd = new NativeAd(getApplicationContext());
		nativeAd.setAdListener(this);
		iconImage = (ImageView) findViewById(R.id.nativeIconImage);
		mainImage = (ImageView) findViewById(R.id.nativeMainImage);
		nativeText = (TextView) findViewById(R.id.nativeText);
		nativeTitle = (TextView) findViewById(R.id.nativeTitle);
		nativeRelativeLayout = (RelativeLayout) findViewById(R.id.nativeAdLayout);
		ratingBar = (RatingBar) findViewById(R.id.ratingBarNative);
		ctaButton = (Button) findViewById(R.id.ctatButton);
		
		iconImage.setVisibility(View.GONE);
		mainImage.setVisibility(View.GONE);
		nativeText.setVisibility(View.GONE);
		ctaButton.setVisibility(View.GONE);
		nativeTitle.setVisibility(View.GONE);
		nativeRelativeLayout.setVisibility(View.GONE);
		ratingBar.setVisibility(View.GONE);
		
		SharedPreferences prefs = this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		nativeAd.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.PUBLISHER_ID, "0")));
		nativeAd.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.AD_SPACE_ID, "0")));
		nativeAd.setClickToActionButton(ctaButton).setIconImageView(iconImage)
				.setMainImageView(mainImage).setTextView(nativeText)
				.setTitleView(nativeTitle).setMainLayout(nativeRelativeLayout).setRatingBar(ratingBar);
		nativeAd.asyncLoadNewBanner();
		/**
		 * nativeAd.asyncLoadPlainNativeAd(true, true, true, true, new NativeAdListener() {

			@Override
			public void onError(ErrorCode errorCode, String errorMessage) {
				Log.e(TAG, ""+errorCode+ " "+ errorMessage);
			}

			@Override
			public void onAdResponse(BannerNativeAd nativeAd) {
				Log.e(TAG, "ad respoinse");
			}
		});
		 */
	}

	@Override
	public void onClick(View v) {
		nativeAd.asyncLoadNewBanner();
	}

	@Override
	public void onDestroy() {
		if(nativeAd!=null){
			nativeAd.destroy();
		}

		super.onDestroy();
	}

	@Override
	public void onReceiveAd(AdDownloaderInterface sender,
			ReceivedBannerInterface receivedBanner) {
		if (receivedBanner.getErrorCode() != ErrorCode.NO_ERROR) {
			Toast.makeText(getApplicationContext(),
					receivedBanner.getErrorMessage(), Toast.LENGTH_LONG).show();
			iconImage.setVisibility(View.GONE);
			mainImage.setVisibility(View.GONE);
			nativeText.setVisibility(View.GONE);
			ctaButton.setVisibility(View.GONE);
			nativeTitle.setVisibility(View.GONE);
			nativeRelativeLayout.setVisibility(View.GONE);
			ratingBar.setVisibility(View.GONE);
		} else{
			iconImage.setVisibility(View.VISIBLE);
			mainImage.setVisibility(View.VISIBLE);
			nativeText.setVisibility(View.VISIBLE);
			ctaButton.setVisibility(View.VISIBLE);
			nativeTitle.setVisibility(View.VISIBLE);
			nativeRelativeLayout.setVisibility(View.VISIBLE);
			ratingBar.setVisibility(View.VISIBLE);
		}
	}
}
