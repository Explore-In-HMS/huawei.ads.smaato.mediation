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
import android.widget.TextView;
import android.widget.Toast;

import com.smaato.demoapp.R;
import com.smaato.demoapp.utils.Constants;
import com.smaato.soma.video.VASTAdListener;
import com.smaato.soma.video.Video;

public class VASTBannerSample extends AppCompatActivity implements OnClickListener,VASTAdListener {
	Button loadBanner, showBanner;
	TextView messageBoard;
	Video videoAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vastbanner_sample);
		messageBoard = (TextView ) findViewById( R.id.textView1 ) ;
		videoAd = new Video(VASTBannerSample.this);
		videoAd.setVastAdListener(VASTBannerSample.this);
		loadBanner = (Button) findViewById(R.id.load_ad );
		loadBanner.setOnClickListener(VASTBannerSample.this);
		showBanner = (Button) findViewById(R.id.show_ad );
		showBanner.setOnClickListener(VASTBannerSample.this);
		showBanner.setEnabled(false);
		showBanner.setText("Not Ready");
		setTitleColor(Color.WHITE);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#3498db")));
		SharedPreferences prefs = VASTBannerSample.this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		videoAd.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.PUBLISHER_ID, "0")));
		videoAd.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.AD_SPACE_ID, "0")));

		// will be autoclosed after 10 second. Default is 3 seconds
		videoAd.setAutoCloseDuration(10);

		// to completely disable AutoClose feature
/*		videoAd.disableAutoClose(true); */

		// configurable Video Skip Interval. Default is 15 seconds
        videoAd.setVideoSkipInterval(5);
	}

	@Override
	public void onClick(View v) {
		if (v == showBanner) {
			videoAd.show();
			showBanner.setEnabled(false);
			showBanner.setText("Not Ready");
			messageBoard.setText( "Ad shown" );
		}
		if (v == loadBanner) {
			messageBoard.setText("Loading");
			videoAd.asyncLoadNewBanner();
		}
	}

	@Override
	public void onReadyToShow() {
		showBanner.setText("Ready to show");
		showBanner.setEnabled(true);
		toast("Ready to Show");
		messageBoard.setText( "Ad loaded" );
	}

	@Override
	public void onWillShow() {
		toast("on will show");
	}

	@Override
	public void onWillOpenLandingPage() {
		toast("onWillOpenLandingPage");
	}

	@Override
	public void onWillClose() {
		toast("on will close");
	}

	@Override
	public void onFailedToLoadAd() {
		messageBoard.setText( "Ad failed" );
		toast("Failed to load ad");
	}
	
	private void toast(final String msg) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(VASTBannerSample.this, msg,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onDestroy(){
		if(videoAd!=null){
			videoAd.destroy();
		}

		super.onDestroy();
	}
}
