/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.demoapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.smaato.demoapp.R;
import com.smaato.demoapp.utils.Constants;
import com.smaato.soma.AdDimension;
import com.smaato.soma.AdDownloaderInterface;
import com.smaato.soma.AdListenerInterface;
import com.smaato.soma.BannerView;
import com.smaato.soma.ErrorCode;
import com.smaato.soma.ReceivedBannerInterface;
import com.smaato.soma.debug.Debugger;

public class LeaderSample extends AppCompatActivity implements OnClickListener,AdListenerInterface {

	BannerView mBannerView;
	Button loadBanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_sample);
		Debugger.setDebugMode(Debugger.Level_3);
		loadBanner = (Button) findViewById(R.id.load_ad );
		loadBanner.setOnClickListener(this);
		mBannerView = (BannerView) findViewById(R.id.bannerView);
		mBannerView.addAdListener(LeaderSample.this);

		// skyscraper 120 by 600   , Leaderboard  728 by 90  // large 216*36


		mBannerView.getAdSettings().setAdDimension(AdDimension.LEADERBOARD);
		SharedPreferences prefs = LeaderSample.this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		mBannerView.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID, "0")));
		mBannerView.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID, "0")));

		mBannerView.setAutoReloadEnabled(prefs.getBoolean(Constants.COM_SMAATO_DEMOAPP + Constants.REFRESH_AD, false));
		mBannerView.setAutoReloadFrequency(prefs.getInt(Constants.COM_SMAATO_DEMOAPP + Constants.REFRESH_INTERVAL, 60));
		mBannerView.setLocationUpdateEnabled(prefs.getBoolean(Constants.COM_SMAATO_DEMOAPP + Constants.GPS, false));
		mBannerView.setScalingEnabled(false);
		mBannerView.asyncLoadNewBanner();
		setTitleColor(Color.WHITE);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3498db")));
	}

	@Override
	public void onClick(View v) {
		if (v == loadBanner) {
			mBannerView.asyncLoadNewBanner();
		}
	}
	@Override
	public void onReceiveAd(AdDownloaderInterface sender,
							ReceivedBannerInterface receivedBanner) {
		if(receivedBanner.getErrorCode() != ErrorCode.NO_ERROR){
			Toast.makeText(getBaseContext(), receivedBanner.getErrorMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroy(){
		if(mBannerView != null) {
			mBannerView.destroy();
		}
		super.onDestroy();
	}
}