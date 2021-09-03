/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.demoapp.utils;

import android.content.res.Resources;

public class Constants {
	public static final String COM_SMAATO_DEMOAPP = "com.smaato.demoapp";

	public static final String PUBLISHER_ID = "PUBLISHER_ID";	
	public static final String AD_SPACE_ID = "AD_SPACE_ID";
	public static final String REFRESH_AD = "REFRESH_AD";
	public static final String REFRESH_INTERVAL = "REFRESH_INTERVAL";
	public static final String GPS = "GPS";
	private Constants (){}


	public static int dpToPx(int dp)
	{
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public static int pxToDp(int px)
	{
		return (int) (px / Resources.getSystem().getDisplayMetrics().density);
	}

}
