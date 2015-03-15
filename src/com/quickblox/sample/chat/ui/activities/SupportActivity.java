package com.quickblox.sample.chat.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.quickblox.sample.chat.R;

public class SupportActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.support_activitty);
		
	}
	public void onBackPressed() {
		Intent intent = new Intent(SupportActivity.this, DialogsActivity.class);
		startActivity(intent);
		finish();
	}
}
