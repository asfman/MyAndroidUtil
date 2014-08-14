package com.asfman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		View widgetEdit = findViewById(R.id.widget_edit);
		widgetEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.asfman.action.EDIT_TEXT"));
			}
		});
		View chooseCityView = findViewById(R.id.choose_city);
		chooseCityView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.asfman.action.CHOOSE_CITY"));
			}
		});
		
		View plusView = findViewById(R.id.gplus_list);
		plusView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.asfman.action.GPlus_List"));
			}
		});
	}

}
