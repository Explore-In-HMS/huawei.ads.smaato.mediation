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

public class AppNativeAdContentWallSample extends Activity implements OnClickListener,
		AdListenerInterface {
	NativeAd nativeAd;
	RelativeLayout nativeRelativeLayout;
	Button reloadBanner;


	NativeAd nativeAd2;
	RelativeLayout nativeRelativeLayout2;


	static String TAG = "NativeAdContentWall";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_native_ad_content_wall_sample);
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
		nativeAd2.setAdListener(this);
		nativeRelativeLayout2 = (RelativeLayout) findViewById(R.id.nativeAdLayout2);
		nativeAd2.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID, "0")));
		nativeAd2.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID, "0")));
		nativeAd2.setMainLayout(nativeRelativeLayout2);

		// calling Native_ContentWall_Req
		invokeNativeAd();

	}

	/*
	 * This common method to load and Reload nativeAdType with specified layout
	 */
	public void invokeNativeAd() {

		if(nativeAd!=null && nativeRelativeLayout!=null)
		nativeAd.asyncLoadNativeType(NativeAd.NativeType.CONTENT_WALL, new NativeAd.NativeAdTypeListener() {

			@Override
			public void onError(ErrorCode errorCode, String errorMessage) {
				Log.e("", "" + errorCode + " " + errorMessage);
			}

			@Override
			public void onAdResponse(ViewGroup nativeView) {
				if (nativeView != null) {
					Log.d(TAG, "Layout view received & Added");
					nativeAd.bindAdResponse(nativeView);
				}
			}
		});


		if(nativeAd2!=null && nativeRelativeLayout2!=null)
			nativeAd2.asyncLoadNativeType(NativeAd.NativeType.CONTENT_WALL, new NativeAd.NativeAdTypeListener() {

				@Override
				public void onError(ErrorCode errorCode, String errorMessage) {
					Log.e("", "" + errorCode + " " + errorMessage);
				}

				@Override
				public void onAdResponse(ViewGroup nativeView) {
					if (nativeView != null) {
						Log.d(TAG, "Layout view received & Added");
						// Invoke this method only when your MainLayout is visible and before attaching AdLayout
						nativeAd2.bindAdResponse(nativeView);
					}
				}
			});

	}

	@Override
	public void onClick(View v) {
		invokeNativeAd();
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
