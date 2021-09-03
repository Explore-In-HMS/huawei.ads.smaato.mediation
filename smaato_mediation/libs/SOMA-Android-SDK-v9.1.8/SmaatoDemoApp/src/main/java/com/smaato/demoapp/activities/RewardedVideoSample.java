/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.demoapp.activities;

import com.smaato.demoapp.R;
import com.smaato.demoapp.utils.Constants;
import com.smaato.soma.video.RewardedVideo;
import com.smaato.soma.video.RewardedVideoListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class RewardedVideoSample extends AppCompatActivity implements OnClickListener {

	Button loadBanner, showBanner;
	RewardedVideo rewardedVideo;

	public static String TAG = "RewardedVideoSample";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rewardedvideo_sample);

		rewardedVideo = new RewardedVideo(RewardedVideoSample.this);


		loadBanner = (Button) findViewById(R.id.load_ad );
		loadBanner.setOnClickListener(RewardedVideoSample.this);
		showBanner = (Button) findViewById(R.id.show_ad );
		showBanner.setOnClickListener(RewardedVideoSample.this);
		showBanner.setEnabled(false);
		showBanner.setText("Not Ready");
		setTitleColor(Color.WHITE);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#3498db")));
		SharedPreferences prefs = RewardedVideoSample.this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		rewardedVideo.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.PUBLISHER_ID, "0")));
		rewardedVideo.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.AD_SPACE_ID, "0")));

		// RewardedVideo specific method compared to VASTAd
		rewardedVideo.setAutoCloseDuration(10);
		rewardedVideo.setRewardedVideoListener(rewardedVideoListener);

		// disabling AutoClose will ignore the duration
		//rewardedVideo.disableAutoClose(true);


	}

	@Override
	public void onClick(View v) {
		if (v == showBanner) {
			rewardedVideo.show();
			showBanner.setEnabled(false);
			showBanner.setText("Not Ready");
		}
		if (v == loadBanner) {
			rewardedVideo.asyncLoadNewBanner();
		}
	}

	private void toast(final String msg) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(RewardedVideoSample.this, msg,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	RewardedVideoListener rewardedVideoListener = new RewardedVideoListener() {
		@Override
		public void onRewardedVideoStarted() {
			Log.d(TAG,TAG + "onRewardedVideoStarted");
			toast("onRewardedVideoStarted");
		}

		@Override
		public void onFirstQuartileCompleted() {
			Log.d(TAG,TAG + "onFirstQuartileCompleted");
			toast("onFirstQ Completed");
		}

		@Override
		public void onSecondQuartileCompleted() {
			Log.d(TAG,TAG + "onSecondQuartileCompleted");
			toast("onSecondQ Completed");
		}

		@Override
		public void onThirdQuartileCompleted() {
			Log.d(TAG,TAG + "onThirdQuartileCompleted");
			toast("onThirdQ Completed");
		}

		@Override
		public void onRewardedVideoCompleted() {
			Log.d(TAG,TAG + "onRewardedVideoCompleted");
			toast("onRewardedVideoCompleted");
		}

		@Override
		public void onReadyToShow() {
			showBanner.setText("Ready to show");
			showBanner.setEnabled(true);
			Log.d(TAG,TAG + "onRewardedVideoCompleted");
			toast("Ready to Show");
		}

		@Override
		public void onWillShow() {
			Log.d(TAG,TAG + "onWillShow");
			toast("on will show");
		}

		@Override
		public void onWillOpenLandingPage() {
			Log.d(TAG,TAG + "onWillOpenLandingPage");
			toast("onWillOpenLandingPage");
		}

		@Override
		public void onWillClose() {
			Log.d(TAG,TAG + "onWillClose");
			toast("on will close");
		}

		@Override
		public void onFailedToLoadAd() {
			Log.d(TAG,TAG + "onFailedToLoadAd");
			toast("Failed to load ad");
		}


	};

	@Override
	public void onDestroy(){
		if(rewardedVideo!=null){
			rewardedVideo.destroy();
		}
		super.onDestroy();
	}
}
