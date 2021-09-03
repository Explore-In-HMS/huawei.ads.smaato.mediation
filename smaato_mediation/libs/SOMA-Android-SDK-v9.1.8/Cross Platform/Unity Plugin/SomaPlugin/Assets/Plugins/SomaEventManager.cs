using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;

public class SomaEventManager : MonoBehaviour {

	public static event Action onReceiveBannerEvent;
	public static event Action noAdAvailableEvent;
	public static event Action onWillOpenLandingPageEvent;
	public static event Action onWillCloseLandingPageEvent;
	
	void Awake()
	{
		gameObject.name = this.GetType().ToString();
		DontDestroyOnLoad( this );
	}
	
	public void noAdAvailable(){
		if(noAdAvailableEvent != null){
			noAdAvailableEvent();
		}
	}
	
	public void onReceiveBanner(){
		if(onReceiveBannerEvent != null){
			onReceiveBannerEvent();
		}
	}
	
	public void onWillOpenLandingPage(){
		if(onWillOpenLandingPageEvent != null){
			onWillOpenLandingPageEvent();
		}
	}
	
	public void onWillCloseLandingPage(){
		if(onWillCloseLandingPageEvent != null){
			onWillCloseLandingPageEvent();
		}
	}
}
