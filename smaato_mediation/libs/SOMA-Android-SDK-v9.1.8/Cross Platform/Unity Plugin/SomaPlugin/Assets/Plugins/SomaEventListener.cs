using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;


public class SomaEventListener : MonoBehaviour
{
	
	void OnEnable()
	{
		SomaEventManager.onReceiveBannerEvent += onReceiveBannerEvent;
		SomaEventManager.noAdAvailableEvent += noAdAvailableEvent;
		SomaEventManager.onWillOpenLandingPageEvent += onWillOpenLandingPageEvent;
		SomaEventManager.onWillCloseLandingPageEvent += onWillCloseLandingPageEvent;
	}


	void OnDisable()
	{
		SomaEventManager.onReceiveBannerEvent -= onReceiveBannerEvent;
		SomaEventManager.noAdAvailableEvent -= noAdAvailableEvent;
		SomaEventManager.onWillOpenLandingPageEvent -= onWillOpenLandingPageEvent;
		SomaEventManager.onWillCloseLandingPageEvent -= onWillCloseLandingPageEvent;
	}
	
	void onReceiveBannerEvent(){
		Debug.Log("Banner Received");
	}
	
	void noAdAvailableEvent(){
		Debug.Log("no Ad Available");
	}
	
	void onWillOpenLandingPageEvent(){
		Debug.Log("Banner clicked, opening landing page");
	}
	void onWillCloseLandingPageEvent(){
		Debug.Log("Closing banner");
	}
}
