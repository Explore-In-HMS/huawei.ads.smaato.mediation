// this sets the background color of the master UIView (when there are no windows/tab groups on it)
Titanium.UI.setBackgroundColor('#000');

var win = Ti.UI.createWindow({
	backgroundColor : 'black'
});
var label = Ti.UI.createLabel();
win.add(label);

win.open();

// TODO: write your module tests here
var soma_module = require('com.smaato.titaniumplugin');

if (Ti.Platform.name == "android") {
	soma_module.initBannerView();
	soma_module.asyncLoadNewBanner();
}
