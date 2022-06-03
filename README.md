 <h1 align="center">Huawei-Smaato Mediation Github Documentation</h3>
 
 ![Latest Version](https://img.shields.io/badge/latestVersion-1.0.1-yellow) ![Kotlin](https://img.shields.io/badge/language-kotlin-blue)
<br>
![Supported Platforms](https://img.shields.io/badge/Supported_Platforms:-Native_Android-orange)

# Introduction

In this documentation we explained how to use Huawei-Smaato mediation with in the different platforms.

# Compatibility

|   | Banner Ad | Interstitial Ad |
| --- | --- | --- |
| Native (Java/Kotlin) | ✅ | ✅ |

# How to start?
  
## Create an ad unit on Huawei Publisher Service

1. Sign in to [Huawei Developer Console] (https://developer.huawei.com/consumer/en/console) and create an AdUnit

## Create a custom event on Smaato

1. Sign in to [Smaato console] ([https://accounts.smaato.com/](https://accounts.smaato.com/))
2. Go to "**Inventory -> Create a new App" 
3. Under the "**Apps**" section, click "**New Adspace**" Give it a label (eg: Huawei Banner)  click "**Save**"
4. Go to "**Networks -> Create a new Network"  and choose Custom SDK Network
5. Click New Line Item for add mediation
6. Enter the **Class Name and Method Name ** and **Custom Data** according to the type of your Ad. Refer to the section below.

## Custom event class
| Ad Type        | Custom event class           |
| ------------- |:-------------:|
| Banner Ad      | com.hmscl.huawei.smaato_mediation.CustomMediationBanner |
| Interstitial Ad      | com.hmscl.huawei.smaato_mediation.CustomMediationIntersitial     |

## Custom Event Parameters
```
{"AD_UNIT_ID":"testw6vs28auh3"}
```
## Method Names
```
loadCustomBanner
loadCustomIntersitial
```

# Integrate the Huawei Mediation SDK

**Note** : A device with Huawei Mobile Services (HMS) installed is required

In the **project-level** build.gradle, include Huawei's Maven repository.

```groovy
repositories {
    google()
    jcenter() // Also, make sure jcenter() is included
    maven { url 'https://developer.huawei.com/repo/' } // Add this line
}

...

allprojects {
    repositories {
        google()
        jcenter() // Also, make sure jcenter() is included
        maven { url 'https://developer.huawei.com/repo/' } //Add this line
    }
}
```

First, download the "smaato_mediation-v1.0.1.aar" file in the project. Then in Android Studio, after saying File->New->New Module, select Import AAR&JAR. Then select the downloaded aar file

**Note** : Older versions could be found under the "_releases" folder

```groovy
dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar"])
    //Adapter SDK
    implementation project(":smaato_mediation-v1.0.1")
    //Huawei Ads Prime
    implementation 'com.huawei.hms:ads-prime:<latest_version>'
}
```

> **_NOTE:_**  If your app can run only on Huawei mobile phones, you can integrate the Huawei Ads Lite SDK instead of Huawei Ads SDK (Optional)
```groovy
dependencies {
    ...
    //Huawei Ads Lite
    implementation 'com.huawei.hms:ads-lite:<latest_version>'
    ...
}
```

<h3>Latest version of SDKs</h3>
<ul>
  <li><a href="https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/publisher-service-version-change-history-0000001050066909">Check the Huawei Ads SDKs here</a></li>
  <li><a href="#version-change-history">Check the version of adapter here</a></li>
</ul>



**Important:** _To add Huawei Ads Kit SDK and Mediation adapter, the native project should be opened with Android Studio._

## **Permissions**
The HUAWEI Ads SDK (com.huawei.hms:ads) has integrated the required permissions. Therefore, you do not need to apply for these permissions. <br />

**android.permission.ACCESS_NETWORK_STATE:** Checks whether the current network is available.   <br/>

**android.permission.ACCESS_WIFI_STATE:** Obtains the current Wi-Fi connection status and the information about WLAN hotspots. <br />

**android.permission.BLUETOOTH:** Obtains the statuses of paired Bluetooth devices. (The permission can be removed if not necessary.) <br />

**android.permission.CAMERA:** Displays AR ads in the Camera app. (The permission can be removed if not necessary.) <br />

**android.permission.READ_CALENDAR:** Reads calendar events and their subscription statuses. (The permission can be removed if not necessary.) <br />

**android.permission.WRITE_CALENDAR:** Creates a calendar event when a user clicks the subscription button in an ad. (The permission can be removed if not necessary.) <br />

# Version Change History

# 1.0.1

<ul>
  <li>Integration methods of Huawei Ads SDK in the plugin have been changed to <a href="https://docs.gradle.org/2.12/release-notes.html#support-for-declaring-compile-time-only-dependencies-with-java-plugin"><i>compileOnly</i></a>.</li>
  <li>Huawei Ads SDK (lite or prime) has to be added externally to the app anymore.</li>
</ul>

# Platforms

## Native

This section demonstrates how to use Smaato mediation feature with Huawei Ads Kit on Native android app.

Firstly, integrate the Smaato SDK for Android

[Smaato Android SDK](https://developers.smaato.com/publishers-legacy/android-sdk-getting-started) can be used for all ad types.

**Note** : Developers can find app level build.gradle in their project from __**"app-folder/app/build.gradle"**__

### **Banner Ad**

To use _Banner_ ads in Native android apps, please check the Smaato SDK. Click [here](https://developers.smaato.com/publishers-legacy/android-sdk-adformat-banners) to get more information about Smaato SDKs _Banner_ Ad development.

### **Interstitial Ad**

To use Interstitial ads in Native android apps, please check the Smaato SDK. Click [here](https://developers.smaato.com/publishers-legacy/android-sdk-adformat-interstitial) to get more information about Smaato SDKs Interstitial Ad development.

## **Sample Codes Based on Ad Types**

### **Banner Ad**

```jsx
<SmaatoBanner
adSize="fullBanner"
BannerView mBanner = new BannerView (context);
mBanner.getAdSettings().setPublisherId(publisherId);
mBanner.getAdSettings().setAdspaceId(adspaceId);} />
```

### **Interstitial Ad**

```jsx
interstitial = new Interstitial(this); //'this' is your Context
interstitial.setInterstitialAdListener(interstitialAdListener);
interstitial.getAdSettings().setPublisherId(your_publisher_id);
interstitial.getAdSettings().setAdspaceId(your_adspace_d);
```


# Screenshots

## Smaato
<table>
<tr>
<td>
<img src="https://github.com/umitkose1/mediationadaptersmaato/blob/master/Screenshot_20210504_083309_com.example.huaweitestapplica.jpg" width="200">

Banner Ad
</td>

</table>

## Huawei
<table>
<tr>
<td>
<img src="https://user-images.githubusercontent.com/41696219/109942123-ee758900-7ce4-11eb-96a3-11cce5454c51.png" width="200">

Banner Ad
</td>

<td>
<img src="https://user-images.githubusercontent.com/41696219/109939330-01d32500-7ce2-11eb-9e39-6a9237ca8c54.JPG" width="200">


Interstitial Ad
</td>

</table>




