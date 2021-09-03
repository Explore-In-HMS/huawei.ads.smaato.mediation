using UnityEngine;
using System.Collections;

public class SomaUnityPlugin : MonoBehaviour {

	private static AndroidJavaObject _plugin;
	static SomaUnityPlugin (){
		using (var pluginClass = new AndroidJavaClass("com.smaato.soma.SomaUnityPlugin"))
			_plugin = pluginClass.CallStatic<AndroidJavaObject>("getInstance");
	}
	public static void initBannerView( int publisherId, int adSpaceId, int adDimension, int position)
	{
		if( Application.platform != RuntimePlatform.Android )
			return;
		
		_plugin.Call( "initBannerView", publisherId, adSpaceId,adDimension,position);
	}

	public static void initInterstitialAd(int publisherId, int adSpaceId){
		if( Application.platform != RuntimePlatform.Android )
			return;
		
		_plugin.Call( "initInterstitialAd", publisherId, adSpaceId);
	}

	public static void asyncLoadNewInterstitial() {
		if (Application.platform != RuntimePlatform.Android) {
			return;
		}

		_plugin.Call ("asyncLoadNewInterstitial");
	}
	
	public static void setInterstitialLocationUpdateEnabled(bool value) {
		if (Application.platform != RuntimePlatform.Android) {
			return;
		}

		_plugin.Call ("setInterstitialLocationUpdateEnabled", value);
	}

	public static void setInterstitialLocation(float latitude, float longitude) {
		if (Application.platform != RuntimePlatform.Android) {
			return;
		}

		_plugin.Call ("setInterstitialLocation", latitude, longitude);
	}

	/**
	 * request a new banner.
	 * */
	public static void asyncLoadNewBanner(){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call( "asyncLoadNewBanner");
	}
	/**
	 * true to enable, false otherwise.
	 * */
	public static void setLocationUpdateEnabled(bool locationUpdate){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("setLocationUpdateEnabled",locationUpdate);
	}
	
	/**
	 * 0 All
	 * 1 Image Banner
	 * 2 RichMedia Banner
	 * 3 Text Banner
	 * */
	public static void setAdType(int adType){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("setAdType",adType);
	}
	
	/**
	 * 0 All
	 * 1 Image Banner
	 * 2 RichMedia Banner
	 * 3 Text Banner
	 * */
	public static void setAdDimension(int adDimension){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("setAdDimension",adDimension);
	}
	
	public static void setAge(int age){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("setAge",age);	
	}
	
	public static void setCity(string city){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("setCity",city);
	}
	
	public static void setKeywordList(string keywordList){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("setKeywordList",keywordList);
	}
	
	public static void setSearchQuery(string searchQuery){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("setSearchQuery",searchQuery);
	}
	
	/**
	 * 0 = Male
	 * 1 = Female
	 * other = Unset.
	 * */
	public static void setUserGender(int gender){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("setUserGender",gender);
	}
	
	public static void setRegion(string region){
		if( Application.platform != RuntimePlatform.Android )
			return;
	 _plugin.Call("setRegion",region);
	}
	
	public static void setCOPPA(bool coppa){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("setCOPPA",coppa);
	}
	
	public static void setAutoReloadFrequency(int frequency){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("setAutoReloadFrequency",frequency);
	}
	
	public static void setAutoReloadEnabled(bool autoReloadEnabled){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("setAutoReloadEnabled",autoReloadEnabled);
	}

	public static void hideView(){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("hideView");
	}

	public static void showView(){
		if( Application.platform != RuntimePlatform.Android )
			return;
		_plugin.Call("showView");
	}
}
