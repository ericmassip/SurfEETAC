package com.example.sharedataandroid;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity2 extends Activity {

	private GlobalArea appState;
	private TextView textresponse;
	private TextView textresponse2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_activity2);
		appState = ((GlobalArea)getApplicationContext());
		
		 textresponse = (TextView)findViewById(R.id.display1);
		 textresponse2 = (TextView)findViewById(R.id.display2);
		 
		 textresponse.setText("Singleton Pattern: "+appState.getUserName());
		 textresponse2.setText("Static: "+Activity1.MyStaticData);
	}

	
}
