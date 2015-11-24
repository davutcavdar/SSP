package com.ubdroid.calendar;



import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	
	public TextView datatext;
	private int volume_level=0;

	



	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		datatext = (TextView) findViewById(R.id.VolumeTxt);
		
		
		
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
		volume_level= am.getRingerMode();
		
		
		if (volume_level==0)
		{
			datatext.setText("Telefonun modu sessiz"); }
			
			if (volume_level==2){
				datatext.setText("Telefonun modu sesli");}
				
			if (volume_level==1){
				datatext.setText("Telefonun modu titresim");}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
