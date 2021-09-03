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
import android.widget.RelativeLayout;

import com.smaato.demoapp.R;
import com.smaato.demoapp.utils.Constants;
import com.smaato.soma.AdDownloaderInterface;
import com.smaato.soma.AdListenerInterface;
import com.smaato.soma.BannerStateListener;
import com.smaato.soma.BaseView;
import com.smaato.soma.ReceivedBannerInterface;
import com.smaato.soma.ToasterBanner;

public class AppToasterBannerSample extends AppCompatActivity implements AdListenerInterface, BannerStateListener {

	ToasterBanner toaster;
	  private static final String TAG = "Toaster";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toaster_banner_sample);
		setTitleColor(Color.WHITE);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3498db")));
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.toasterlayout);
		toaster = new ToasterBanner(getApplicationContext(), rl);
	    toaster.addAdListener(this);
	    toaster.setBannerStateListener(this);
	    SharedPreferences prefs = this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		toaster.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.PUBLISHER_ID, "0")));
		toaster.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP+Constants.AD_SPACE_ID, "0")));
	    toaster.asyncLoadNewBanner();
	    Button b = (Button) findViewById(R.id.load_ad );
	    b.setOnClickListener(new OnClickListener() {
	      @Override
		public void onClick(View v) {
	        if(toaster != null)
	        toaster.asyncLoadNewBanner();
	      }
	    });
	}
	@Override
	public void onWillOpenLandingPage(BaseView sender) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onWillCloseLandingPage(BaseView sender) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReceiveAd(AdDownloaderInterface sender,
			ReceivedBannerInterface receivedBanner) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();

		if(toaster!=null){
			toaster.disappearAndDestroy();
		}
	}
}
