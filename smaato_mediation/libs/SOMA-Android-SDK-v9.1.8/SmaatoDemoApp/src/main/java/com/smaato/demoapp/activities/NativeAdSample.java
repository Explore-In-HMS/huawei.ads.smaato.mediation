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
import com.smaato.soma.debug.DebugCategory;
import com.smaato.soma.debug.Debugger;
import com.smaato.soma.debug.LogMessage;
import com.smaato.soma.nativead.NativeAd;

public class NativeAdSample extends Activity implements OnClickListener,
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


	NativeAd nativeAdSec;
	ImageView iconImageSec;
	ImageView mainImageSec;
	TextView nativeTextSec;
	Button ctaButtonSec;
	TextView nativeTitleSec;
	RelativeLayout nativeRelativeLayoutSec;
	RatingBar ratingBarSec;

	static String TAG = "NativeAdSample";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native_ad_sample);
		reloadBanner = (Button) findViewById(R.id.load_ad );
		reloadBanner.setOnClickListener(NativeAdSample.this);
		nativeAd = new NativeAd(this);
		nativeAd.setAdListener(NativeAdSample.this);
		iconImage = (ImageView) findViewById(R.id.nativeIconImage);
		mainImage = (ImageView) findViewById(R.id.nativeMainImage);
		nativeText = (TextView) findViewById(R.id.nativeText);
		nativeTitle = (TextView) findViewById(R.id.nativeTitle);
		nativeRelativeLayout = (RelativeLayout) findViewById(R.id.nativeAdLayout);
		ratingBar = (RatingBar) findViewById(R.id.ratingBarNative);
		ctaButton = (Button) findViewById(R.id.ctatButton);



		nativeAdSec = new NativeAd(this);
		nativeAdSec.setAdListener( new AdListenerInterface() {

				@Override
				public void onReceiveAd(AdDownloaderInterface sender, ReceivedBannerInterface receivedBanner) {
					if (receivedBanner.getErrorCode() != ErrorCode.NO_ERROR) {
						Toast.makeText(getApplicationContext(),
								receivedBanner.getErrorMessage(), Toast.LENGTH_LONG).show();

						Debugger.showLog(new LogMessage(TAG,
								"Ad 2 NOT available",
								Debugger.Level_1,
								DebugCategory.DEBUG));

					} else{

						Debugger.showLog(new LogMessage(TAG,
								"Ad 2 available",
								Debugger.Level_1,
								DebugCategory.DEBUG));

						iconImageSec.setVisibility(View.VISIBLE);
						mainImageSec.setVisibility(View.VISIBLE);
						nativeTextSec.setVisibility(View.VISIBLE);
						ctaButtonSec.setVisibility(View.VISIBLE);
						nativeTitleSec.setVisibility(View.VISIBLE);
						nativeRelativeLayoutSec.setVisibility(View.VISIBLE);
						ratingBarSec.setVisibility(View.VISIBLE);

						// IMPORTANT step to be invoked after attaching the ad response and making it visible.
						// IMPORTANT FOR IMPRESSIONS COUNTS & REVENUE
						nativeAdSec.registerImpression();

					}
				}

		} );



		iconImageSec = (ImageView) findViewById(R.id.nativeIconImageSec);
		mainImageSec = (ImageView) findViewById(R.id.nativeMainImageSec);
		nativeTextSec = (TextView) findViewById(R.id.nativeTextSec);
		nativeTitleSec = (TextView) findViewById(R.id.nativeTitleSec);
		nativeRelativeLayoutSec = (RelativeLayout) findViewById(R.id.nativeAdLayoutSec);
		ratingBarSec = (RatingBar) findViewById(R.id.ratingBarNativeSec);
		ctaButtonSec = (Button) findViewById(R.id.ctatButtonSec);



		iconImage.setVisibility(View.GONE);
		mainImage.setVisibility(View.GONE);
		nativeText.setVisibility(View.GONE);
		ctaButton.setVisibility(View.GONE);
		nativeTitle.setVisibility(View.GONE);
		nativeRelativeLayout.setVisibility(View.GONE);
		ratingBar.setVisibility(View.GONE);
		
		SharedPreferences prefs = NativeAdSample.this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		nativeAd.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.PUBLISHER_ID, "0")));
		nativeAd.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.AD_SPACE_ID, "0")));
		nativeAd.setClickToActionButton(ctaButton).setIconImageView(iconImage)
				.setMainImageView(mainImage).setTextView(nativeText)
				.setTitleView(nativeTitle).setMainLayout(nativeRelativeLayout).setRatingBar(ratingBar);
		nativeAd.asyncLoadNewBanner();


		nativeAdSec.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.PUBLISHER_ID, "0")));
		nativeAdSec.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.AD_SPACE_ID, "0")));
		nativeAdSec.setClickToActionButton(ctaButtonSec).setIconImageView(iconImageSec)
				.setMainImageView(mainImageSec).setTextView(nativeTextSec)
				.setTitleView(nativeTitleSec).setMainLayout(nativeRelativeLayoutSec).setRatingBar(ratingBarSec);
		nativeAdSec.asyncLoadNewBanner();
	}

	@Override
	public void onClick(View v) {
		nativeAd.asyncLoadNewBanner();
		nativeAdSec.asyncLoadNewBanner();
	}

	@Override
	public void onReceiveAd(AdDownloaderInterface sender,
			ReceivedBannerInterface receivedBanner) {
		if (receivedBanner.getErrorCode() != ErrorCode.NO_ERROR) {
			Toast.makeText(getApplicationContext(),
					receivedBanner.getErrorMessage(), Toast.LENGTH_LONG).show();

			Debugger.showLog(new LogMessage(TAG,
					"Ad 1 NOT available",
					Debugger.Level_1,
					DebugCategory.DEBUG));
		} else{

			Debugger.showLog(new LogMessage(TAG,
					"Ad 1 available",
					Debugger.Level_1,
					DebugCategory.DEBUG));

			iconImage.setVisibility(View.VISIBLE);
			mainImage.setVisibility(View.VISIBLE);
			nativeText.setVisibility(View.VISIBLE);
			ctaButton.setVisibility(View.VISIBLE);
			nativeTitle.setVisibility(View.VISIBLE);
			nativeRelativeLayout.setVisibility(View.VISIBLE);
			ratingBar.setVisibility(View.VISIBLE);

			// IMPORTANT step to be invoked after attaching the ad response and making it visible.
			// IMPORTANT FOR IMPRESSIONS COUNTS & REVENUE
			nativeAd.registerImpression();

		}
	}

	@Override
	public void onDestroy(){
		if(nativeAd!=null) {
			nativeAd.destroy();
		}
		super.onDestroy();
	}

}
