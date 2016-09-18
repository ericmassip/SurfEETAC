package com.example.sharedataandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Activity1 extends Activity {

	private GlobalArea mAppState;
	private EditText mUsername;
	private EditText mPassword;
	public static String MyStaticData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity1);
		
		mAppState = ((GlobalArea)getApplicationContext());
    	mUsername = (EditText)findViewById(R.id.txtUsername);
    	mPassword = (EditText)findViewById(R.id.txtPassword);
    	

	}


	
	public void log_in(View view) {

		mAppState.setUserName(mUsername.getText().toString());
		MyStaticData=mPassword.getText().toString();
		Intent intent = new Intent(this, Activity2.class);
		startActivity(intent);

	}
	
}
