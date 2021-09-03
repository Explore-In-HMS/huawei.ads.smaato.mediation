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
import com.smaato.soma.debug.Debugger;
import com.smaato.soma.interstitial.Interstitial;
import com.smaato.soma.interstitial.InterstitialAdListener;

public class InterstitialBannerSample extends AppCompatActivity implements
		InterstitialAdListener, OnClickListener {

	Button loadBanner, showBanner;

	Interstitial interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Debugger.setDebugMode(Debugger.Level_3);
		setContentView(R.layout.activity_interstitial_banner_sample);
		interstitial = new Interstitial(InterstitialBannerSample.this);
		interstitial.setInterstitialAdListener(InterstitialBannerSample.this);
		loadBanner = (Button) findViewById(R.id.load_ad );
		loadBanner.setOnClickListener(InterstitialBannerSample.this);
		showBanner = (Button) findViewById(R.id.show_ad );
		showBanner.setOnClickListener(InterstitialBannerSample.this);
		showBanner.setEnabled(false);
		showBanner.setText("Not Ready");
		setTitleColor(Color.WHITE);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#3498db")));
		SharedPreferences prefs = InterstitialBannerSample.this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		interstitial.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.PUBLISHER_ID, "0")));
		interstitial.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.AD_SPACE_ID, "0")));
		interstitial.setLocationUpdateEnabled(prefs.getBoolean(Constants.COM_SMAATO_DEMOAPP + Constants.GPS, false));
		interstitial.getAdSettings().setHttpsOnly( false );
	}

	@Override
	public void onReadyToShow() {
		showBanner.setText("Ready to show");
		showBanner.setEnabled(true);
		toast("Ready to Show");
	}


	@Override
	public void onWillShow() {
	    toast("on will show");
	    }

	@Override
	public void onWillOpenLandingPage() {
		// TODO Auto-generated method stub
		toast("onWillOpenLandingPage");
	}

	@Override
	public void onWillClose() {
	    toast("on will close");
	    }

	@Override
	public void onFailedToLoadAd() {
	    toast("Failed to load ad");
	    }

	@Override
	public void onClick(View v) {
		if (v == showBanner) {
			interstitial.show();
			showBanner.setEnabled(false);
			showBanner.setText("Not Ready");
		}
		if (v == loadBanner) {
			interstitial.asyncLoadNewBanner();
		}
	}

	@Override
	public void onDestroy() {
		if(interstitial!=null){
			interstitial.destroy();
		}

		super.onDestroy();
	}

	private void toast(final String msg) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(InterstitialBannerSample.this, msg,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}