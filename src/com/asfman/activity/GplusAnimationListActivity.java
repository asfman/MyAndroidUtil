package com.asfman.activity;

import java.util.ArrayList;
import java.util.List;

import com.asfman.adapter.GPlusListAdapter;
import com.asfman.animation.SpeedScrollListener;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GplusAnimationListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView listView = new ListView(this);
		listView.setId(android.R.id.list);
		listView.setDivider(null);
		listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		setContentView(listView);
		ArrayList<String> items = new ArrayList<String>();
		for (int i = 0; i < 38; i++) {
			items.add("");
		}
		setListAdapter(new MyAdapter(new SpeedScrollListener(), items, this));
	}

	class MyAdapter extends GPlusListAdapter {

		public MyAdapter(SpeedScrollListener scrollListener,
				List<? extends Object> items, Context context) {
			super(scrollListener, items, context);
		}

		@Override
		protected View getRowView(int position, View convertView,
				ViewGroup parent) {
			LinearLayout parentLinearLayout = new LinearLayout(getBaseContext());
			parentLinearLayout.setPadding(30, 30, 30, 30);
			TextView textView = new TextView(getBaseContext());
			textView.setText("hello world " + position);
			textView.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			textView.setBackgroundColor(0xff428ad0);
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(0xffffffff);
			textView.setTextSize(18f);
			parentLinearLayout.addView(textView);
			parentLinearLayout
					.setLayoutParams(new android.widget.AbsListView.LayoutParams(
							LayoutParams.MATCH_PARENT, 666));
			return parentLinearLayout;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
