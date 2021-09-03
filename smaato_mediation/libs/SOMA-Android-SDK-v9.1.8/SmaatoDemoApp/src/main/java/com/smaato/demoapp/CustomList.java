/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.demoapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<CustomList.Item> {
	private final boolean isMainMenu;
	
	public CustomList(Activity context, CustomList.Item[] samples, boolean isMainMenu) {
		super(context, R.layout.list_single, samples);
		this.isMainMenu = isMainMenu;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolderItem viewHolderItem = null;
		Context context = getContext();

		if(view == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			if(!isMainMenu){
				view = inflater.inflate(R.layout.config_list, null, true);
			}
			else {
				view = inflater.inflate(R.layout.list_single, null, true);
			}

			viewHolderItem = new ViewHolderItem();

			viewHolderItem.txtTitle = (TextView) view.findViewById(R.id.txt);
			viewHolderItem.imageView = (ImageView) view.findViewById(R.id.img);

			view.setTag( viewHolderItem );
		} else {
			viewHolderItem = (ViewHolderItem) view.getTag();
		}

		Item item = getItem(position);
		if(item.title != null) {
			viewHolderItem.txtTitle.setText(item.title);
		} else {
			viewHolderItem.txtTitle.setText(item.titleId);
		}
		if(!isMainMenu){
			viewHolderItem.txtTitle.setTextColor(context.getResources().getColor( R.color.trans ));
			viewHolderItem.txtTitle.setBackgroundColor(context.getResources().getColor( R.color.black ));
			viewHolderItem.imageView.setBackgroundColor(  context.getResources().getColor( R.color.black_dark));
			((RelativeLayout)viewHolderItem.imageView.getParent()).setBackgroundColor(context.getResources().getColor( R.color.black_dark ));
		} else{
			viewHolderItem.txtTitle.setTextColor(context.getResources().getColor( R.color.grey ));
			viewHolderItem.txtTitle.setBackgroundColor(context.getResources().getColor( R.color.grey_medium ));
			viewHolderItem.imageView.setBackgroundColor( context.getResources().getColor( R.color.grey_light ));
			((RelativeLayout)viewHolderItem.imageView.getParent()).setBackgroundColor(  context.getResources().getColor( R.color.grey_light ));
		}
		viewHolderItem.imageView.setImageResource(item.imageId);
		return view;
	}

	static class ViewHolderItem {
		TextView txtTitle;
		ImageView imageView;
	}

	public static class Item {
		public final int imageId;
		public final int titleId;
		public final String title;

		public Item(int imageId, String title) {
			this.imageId = imageId;
			this.title = title;
			this.titleId = -1;
		}

		public Item(int imageId, int titleId) {
			this.imageId = imageId;
			this.titleId = titleId;
			this.title = null;
		}
	}

}
