using UnityEngine;
using System.Collections.Generic;


public class InterfaceSoma : MonoBehaviour
{
	bool ran = false;
	void OnGUI()
	{
		if(!ran){
			try {
				var unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
				var activity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
				activity.Call("runOnUiThread", new AndroidJavaRunnable(runOnUiThread));
			} catch (System.Exception e) {
				print (e);
			}
			ran = true;
		}

	}

	void runOnUiThread() {
		SomaUnityPlugin.initBannerView(0, 0, 0, 0);
		SomaUnityPlugin.setLocationUpdateEnabled (true);
		SomaUnityPlugin.setAdType(1);
		SomaUnityPlugin.setAutoReloadFrequency(60);
		SomaUnityPlugin.setAutoReloadEnabled(true);
		SomaUnityPlugin.asyncLoadNewBanner();

		/* If using interstitial ads, uncomment the code above and use the code below:
		 * 
		 * 		SomaUnityPlugin.initInterstitialAd(0,0);
		 * 		float latitude = Input.location.lastData.latitude;
		 *		float longitude = Input.location.lastData.longitude;
		 *
		 * 		SomaUnityPlugin.setInterstitialLocation (latitude, longitude);
		 * 		SomaUnityPlugin.setInterstitialLocationUpdateEnabled (true);
		 *		SomaUnityPlugin.asyncLoadNewInterstitial ();
		 */

	}
}