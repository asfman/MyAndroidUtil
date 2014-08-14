package com.asfman.activity;

import com.asfman.R;
import com.asfman.widget.ClearEditText;

import android.app.Activity;
import android.os.Bundle;

public class ClearEditTextActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_edit);
		ClearEditText clearEditText = (ClearEditText) findViewById(R.id.widget_edit);
		clearEditText.setAnimation(ClearEditText.shakeAnimation(10));
		clearEditText.getAnimation().start();
	}

}
