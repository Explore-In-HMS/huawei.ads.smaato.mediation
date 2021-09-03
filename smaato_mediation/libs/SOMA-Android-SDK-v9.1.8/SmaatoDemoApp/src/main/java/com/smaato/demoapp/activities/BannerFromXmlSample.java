/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.demoapp.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.smaato.demoapp.R;
import com.smaato.soma.BannerView;

public class BannerFromXmlSample extends AppCompatActivity implements OnClickListener {

	BannerView mBannerView;
	Button loadBanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banner_from_xml_sample);
		setTitleColor(Color.WHITE);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3498db")));
		//Debugger.setDebugMode(Debugger.Level_3);
		mBannerView = (BannerView) findViewById(R.id.bannerView);
		loadBanner = (Button) findViewById(R.id.load_ad );
		loadBanner.setOnClickListener(BannerFromXmlSample.this);
	}

	@Override
	public void onClick(View v) {
		if (v == loadBanner) {
			mBannerView.asyncLoadNewBanner();
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
