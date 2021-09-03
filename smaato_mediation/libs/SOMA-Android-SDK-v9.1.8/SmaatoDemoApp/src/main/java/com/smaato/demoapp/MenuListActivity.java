/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.demoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.smaato.demoapp.activities.AboutActivity;
import com.smaato.demoapp.activities.BannerFromXmlSample;
import com.smaato.demoapp.activities.InterstitialBannerSample;
import com.smaato.demoapp.activities.LeaderSample;
import com.smaato.demoapp.activities.MediumRectangle;
import com.smaato.demoapp.activities.NativeAdAppWallSample;
import com.smaato.demoapp.activities.NativeAdCarouselSample;
import com.smaato.demoapp.activities.NativeAdChatListSample;
import com.smaato.demoapp.activities.NativeAdContentWallSample;
import com.smaato.demoapp.activities.NativeAdContentStreamSample;
import com.smaato.demoapp.activities.NativeAdNewsFeedSample;
import com.smaato.demoapp.activities.NativeAdSample;
import com.smaato.demoapp.activities.PhoneBannerSample;
import com.smaato.demoapp.activities.RewardedVideoSample;
import com.smaato.demoapp.activities.SkySample;
import com.smaato.demoapp.activities.ToasterBannerSample;
import com.smaato.demoapp.activities.VASTBannerSample;
import com.smaato.demoapp.utils.Constants;
import com.smaato.soma.debug.Debugger;

import java.util.Arrays;


public class MenuListActivity extends AppCompatActivity
		implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private static NavigationDrawerFragment mNavigationDrawerFragment;
	private SharedPreferences prefs;

	private static String TABLET = "tablet";
	private static String PHONE = "phone";

	private static String TAG = "MenuListActivity";

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	ListView list;

	private class Sample extends CustomList.Item {
		public final Class activity;
		public Sample(Class activity, int imageId, int titleId) {
			super(imageId, titleId);
			this.activity = activity;
		}
	}

	private final Sample[] SAMPLES = {
			new Sample(PhoneBannerSample.class,				R.drawable.imagead,		R.string.title_activity_phone_banner_sample),
			new Sample(MediumRectangle.class,				R.drawable.rectangle,	R.string.title_activity_medium_rectangle),
			new Sample(InterstitialBannerSample.class,		R.drawable.interstitial,R.string.title_activity_interstitial_banner_sample),
			new Sample(ToasterBannerSample.class,			R.drawable.toaster,		R.string.title_activity_toaster_banner_sample),
			new Sample(BannerFromXmlSample.class,			R.drawable.imagead,		R.string.title_activity_banner_from_xml_sample),
			new Sample(RewardedVideoSample.class,			R.drawable.interstitial,R.string.title_activity_rewardedvideo_sample),
			new Sample(VASTBannerSample.class,				R.drawable.interstitial,R.string.title_activity_vastbanner_sample),
			new Sample(NativeAdSample.class,				R.drawable.imagead,		R.string.title_activity_native_ad_sample),
			new Sample(NativeAdAppWallSample.class,			R.drawable.imagead,		R.string.title_activity_native_app_wall_ad_sample),
			new Sample(NativeAdContentWallSample.class,		R.drawable.imagead,		R.string.title_activity_native_content_wall_ad_sample),
			new Sample(NativeAdNewsFeedSample.class,		R.drawable.imagead,		R.string.title_activity_native_news_feed_ad_sample),
			new Sample(NativeAdChatListSample.class,		R.drawable.imagead,		R.string.title_activity_native_chat_list_ad_sample),
			new Sample(NativeAdCarouselSample.class,		R.drawable.imagead,		R.string.title_activity_native_carousel_ad_sample),
			new Sample(NativeAdContentStreamSample.class,	R.drawable.imagead,		R.string.title_activity_native_content_stream_ad_sample),
			new Sample(SkySample.class,						R.drawable.sky,			R.string.title_activity_sky_ad_sample),
			new Sample(LeaderSample.class,					R.drawable.leader,		R.string.title_activity_leaderboard_ad_sample),
			// TODO enable App Context
			// tzwick: don't ask me what this is), I just included this commented out section in this small refactor
/*
			new Sample(AppPhoneBannerSample.class,			R.drawable.imagead,		R.string.title_activity_phone_banner_sample),
			new Sample(AppMediumRectangle.class,			R.drawable.rectangle,	R.string.title_activity_medium_rectangle),
			new Sample(AppInterstitialBannerSample.class,	R.drawable.interstitial,R.string.title_activity_interstitial_banner_sample),
			new Sample(AppToasterBannerSample.class,		R.drawable.toaster,		R.string.title_activity_toaster_banner_sample),
			new Sample(AppBannerFromXmlSample.class,		R.drawable.imagead,		R.string.title_activity_banner_from_xml_sample),
			new Sample(AppVASTBannerSample.class,			R.drawable.interstitial,R.string.title_activity_vastbanner_sample),
			new Sample(AppNativeAdSample.class,				R.drawable.imagead,		R.string.title_activity_native_ad_sample),
			new Sample(AppNativeAdAppWallSample.class,		R.drawable.imagead,		R.string.title_activity_native_app_wall_ad_sample),
			new Sample(AppNativeAdContentWallSample.class,	R.drawable.imagead,		R.string.title_activity_native_content_wall_ad_sample),
			new Sample(AppNativeAdNewsFeedSample.class,		R.drawable.imagead,		R.string.title_activity_native_news_feed_ad_sample),
			new Sample(AppNativeAdChatListSample.class,		R.drawable.imagead,		R.string.title_activity_native_chat_list_ad_sample),
			new Sample(AppNativeAdCarouselSample.class,		R.drawable.imagead,		R.string.title_activity_native_carousel_ad_sample),
			new Sample(AppNativeAdContentStreamSample.class,R.drawable.imagead,		R.string.title_activity_native_content_stream_ad_sample),
			new Sample(AppSkySample.class,					R.drawable.sky,			R.string.title_activity_sky_ad_sample),
			new Sample(AppLeaderSample.class,				R.drawable.leader,		R.string.title_activity_leaderboard_ad_sample),
			new Sample(ProgrammaticPhoneBannerSample.class,	R.drawable.imagead,		R.string.title_activity_programmatic_phone_banner_sample),
*/
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Debugger.setDebugMode(Debugger.Level_3);

		Debugger.setDebugMode(Debugger.Level_0);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_list);


		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = "Smaato Demo App";
		setTitle(mTitle);

		if(getSupportActionBar()!=null)
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		CustomList adapter = null;

		if(findDeviceType(this).equals(TABLET)){
			adapter = new CustomList(MenuListActivity.this, SAMPLES, true);
		} else {
			adapter = new CustomList(MenuListActivity.this, Arrays.copyOfRange(SAMPLES,0,SAMPLES.length-2), true);
		}

		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(MenuListActivity.this, SAMPLES[position].activity));
			}
		});
		setTitleColor(Color.WHITE);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3498db")));

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
				.commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
			case 1:
				showAccountInformation();
				break;
			case 2:
				showRefreshInterval();
				break;
			case 3:
				showAutoRefreshAd();
				break;
			case 4:
				showGPS();
				break;
		}
	}

	private void showGPS() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MenuListActivity.this);
		// Get the layout inflater
		LayoutInflater inflater = MenuListActivity.this.getLayoutInflater();
		prefs = MenuListActivity.this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		boolean state = prefs.getBoolean(Constants.COM_SMAATO_DEMOAPP + Constants.GPS, false);
		final View v = inflater.inflate(R.layout.autorefresh, null);
		((Switch) v.findViewById(R.id.switch1)).setChecked(state);
		((Switch) v.findViewById(R.id.switch1)).setText("GPS Status");
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		try{
			builder.setView(v)
                    // Add action buttons
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            prefs.edit()
                                    .putBoolean(Constants.COM_SMAATO_DEMOAPP + Constants.GPS,
                                            ((Switch) v.findViewById(R.id.switch1)).isChecked()).apply();
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
			builder.create().show();
		} catch( Exception e ){
			Log.d( TAG,  "builder failed in showGPS");
		}
	}

	private void showRefreshInterval() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MenuListActivity.this);
		// Get the layout inflater
		LayoutInflater inflater = MenuListActivity.this.getLayoutInflater();
		prefs = MenuListActivity.this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		int value = prefs.getInt(Constants.COM_SMAATO_DEMOAPP + Constants.REFRESH_INTERVAL, 0);
		final View v = inflater.inflate(R.layout.autorefreshinterval, null);
		((SeekBar) v.findViewById(R.id.seekBar1)).setProgress(value);
		((TextView) v.findViewById(R.id.textViewInterval)).setText(value + " seconds");
		((SeekBar) v.findViewById(R.id.seekBar1)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
										  boolean fromUser) {
				((TextView) v.findViewById(R.id.textViewInterval)).setText(progress + " seconds");
			}
		});

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		try{
			builder.setView(v)
                    // Add action buttons
			.setPositiveButton("Save", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					prefs.edit()
							.putInt(Constants.COM_SMAATO_DEMOAPP + Constants.REFRESH_INTERVAL,
									((SeekBar) v.findViewById(R.id.seekBar1)).getProgress()).apply();
				}
			})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int id) {
								}
							});
			builder.create().show();
		} catch( Exception e ){
			// todo rearchitect to avoid window leak
			// leaked window com.android.internal.policy.PhoneWindow$DecorView
		}
	}

	private void showAutoRefreshAd() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MenuListActivity.this);
		// Get the layout inflater
		LayoutInflater inflater = MenuListActivity.this.getLayoutInflater();
		prefs = MenuListActivity.this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		boolean state = prefs.getBoolean(Constants.COM_SMAATO_DEMOAPP + Constants.REFRESH_AD, false);
		final View v = inflater.inflate(R.layout.autorefresh, null);
		((Switch) v.findViewById(R.id.switch1)).setChecked(state);
		((Switch) v.findViewById(R.id.switch1)).setText("Ad Refresh status");
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		try{
			builder.setView(v)
                    // Add action buttons
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            prefs.edit()
                                    .putBoolean(Constants.COM_SMAATO_DEMOAPP + Constants.REFRESH_AD,
                                            ((Switch) v.findViewById(R.id.switch1)).isChecked()).apply();
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
			builder.create().show();
		} catch( Exception e ){
			Log.d( TAG,  "builder failed in showAutoRefreshAd");
		}
	}

	private void showAccountInformation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MenuListActivity.this);
		// Get the layout inflater
		LayoutInflater inflater = MenuListActivity.this.getLayoutInflater();
		final View v = inflater.inflate(R.layout.accountinformation, null);
		prefs = MenuListActivity.this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		((EditText)v.findViewById(R.id.publisherId)).setText(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID, "0"));
		((EditText)v.findViewById(R.id.adSpace)).setText(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID, "0"));
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		try{
			builder.setView(v)
                    // Add action buttons
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    String publisherId = ((EditText)v.findViewById(R.id.publisherId)).getText().toString();
                                    String adSpaceId = ((EditText)v.findViewById(R.id.adSpace)).getText().toString();
                                    try {
                                        prefs.edit()
                                                .putString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID,
                                                        ""+Integer.parseInt(publisherId)).apply();
                                        prefs.edit()
                                                .putString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID,
                                                        ""+Integer.parseInt(adSpaceId)).apply();
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(MenuListActivity.this, "Publisher or AdSpace Id too long !! using default settings (0:0)", Toast.LENGTH_SHORT).show();
                                        prefs.edit()
                                                .putString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID,
                                                        ""+0).apply();
                                        prefs.edit()
                                                .putString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID,
                                                        ""+0).apply();
                                    }

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //LoginDialogFragment.this.getDialog().cancel();
                                }
                            });
			builder.create().show();
		} catch( Exception e ){
			Log.d( TAG,  "builder failed");
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3498db")));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.menu_list, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(MenuListActivity.this, AboutActivity.class);

			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static String findDeviceType(Context context) {

		DisplayMetrics dm =  context.getResources().getDisplayMetrics();
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		int dens = dm.densityDpi;
		double wi = (double) width / (double) dens;
		double hi = (double) height / (double) dens;
		double x = Math.pow(wi, 2);
		double y = Math.pow(hi, 2);
		double screenInches = Math.sqrt(x + y);
		Log.d(TAG,  "ScreenInches" + screenInches);
		String devTyp;
		if (screenInches >= 10) {
			//Device is a 10" tablet
			devTyp = TABLET;
		} else if (screenInches >= 6.25) {
			//Device is a 7" tablet
			devTyp = TABLET;
		} else if (screenInches >= 4.5) {
			//Device is a Smart Phone
			devTyp = PHONE;
		} else {
			//Device is a mini Phone
			devTyp = PHONE;
		}
		return devTyp;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_menu_list, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);

			if(mNavigationDrawerFragment!=null && mNavigationDrawerFragment.isDrawerOpen()){
				((MenuListActivity) activity).onSectionAttached(
						getArguments().getInt(ARG_SECTION_NUMBER));
			}
		}
	}

}
