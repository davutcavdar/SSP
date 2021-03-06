package co.fourapps.calendarapp;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.net.Uri.Builder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Instances;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

	//David
	private Spinner mailChooser;
	private ListView eventList;
	private ListView insList;
	private Account[] accounts;
	private Account selectedAccount;
	private EventAdapter adapter;
	private EventAdapter adapter_ins;
	private List<CalendarEvent> events;
	private List<CalendarEvent> ins;
	public TextView datatext;
	public TextView netinfo;
	private int volume_level=0;


	public static final String[] EVENT_PROJECTION = new String[]{
			CalendarContract.Events._ID,                           // 0
			CalendarContract.Events.DTSTART,                  // 1

			CalendarContract.Events.DTEND,         // 2
			CalendarContract.Events.OWNER_ACCOUNT,                  // 3        
			CalendarContract.Events.TITLE, // 4
			CalendarContract.Events.ACCESS_LEVEL, //5
			CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,//6
			CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, //7


	};

	public static final String[] EVENT_PROJECTION_INS = new String[]{
			CalendarContract.Events._ID,                           // 0
			CalendarContract.Instances.BEGIN,                  // 1
			CalendarContract.Instances.END,         // 2
			CalendarContract.Events.OWNER_ACCOUNT,                  // 3        
			CalendarContract.Events.TITLE, // 4
			CalendarContract.Events.ACCESS_LEVEL, //5
			CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,//6
			CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,//7
			CalendarContract.Events.RRULE,//8
			CalendarContract.Events.DESCRIPTION, //9
			CalendarContract.Events.ALL_DAY,//10
			CalendarContract.Events.HAS_ATTENDEE_DATA //11



	};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



		mailChooser = (Spinner) findViewById(R.id.spinner);
		//  eventList = (ListView) findViewById(R.id.listView);
		insList = (ListView) findViewById(R.id.insView);
		datatext = (TextView) findViewById(R.id.VolumeTxt);
		netinfo=(TextView) findViewById(R.id.NetInfo);

		events = new ArrayList<CalendarEvent>();
		ins=new ArrayList<CalendarEvent>();








		try {
			accounts = AccountManager.get(this).getAccountsByType("com.google");
			selectedAccount = accounts[0];
		} catch (Exception e) {
			Log.i("Exception", "Exception:" + e);
		}


		mailChooser.setOnItemSelectedListener(this);

		List<String> categories = new ArrayList<String>();

		for (Account acc : accounts) {
			categories.add(acc.name);
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mailChooser.setAdapter(dataAdapter);

	}



	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		String selectedTitle = parent.getItemAtPosition(position).toString();

		for (Account acc : accounts) {
			if (acc.name.equals(selectedTitle))
				selectedAccount = acc;
		}


		GetringerInfo();
		GetNetworkInfo();
		makeQuery();



	}


	public void GetringerInfo() {

		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
		volume_level= am.getRingerMode();


		if (volume_level==0)
		{
			datatext.setText("Telefonun ses seviyesi: sessiz"); }

		if (volume_level==2){
			datatext.setText("Telefonun ses seviyesi: sesli");}

		if (volume_level==1){
			datatext.setText("Telefonun ses seviyesi: titresim");}




	}



	public void GetNetworkInfo(){


		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo connectionInfo = wifiManager.getConnectionInfo();

		String strWifiInfo = "";

		int ipAddress = connectionInfo.getIpAddress();

		String ipString = String.format("%d.%d.%d.%d",
				(ipAddress & 0xff),
				(ipAddress >> 8 & 0xff),
				(ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff));

		String[] NetInfo = new String[7];

		NetInfo[0]=connectionInfo.getSSID() ;
		NetInfo[1]=connectionInfo.getBSSID();
		NetInfo[2]=ipString;
		NetInfo[3]=connectionInfo.getMacAddress();
		NetInfo[4]=Integer.toString(connectionInfo.getLinkSpeed());
		NetInfo[5]=Integer.toString(connectionInfo.getRssi());
		NetInfo[6]=Integer.toString(WifiManager.calculateSignalLevel(connectionInfo.getRssi(),5)); 





		strWifiInfo += 
				"SSID: " + NetInfo[0] + "\n" +
						"BSSID: " + NetInfo[1]  + "\n" +
						"IP Address: " + NetInfo[2]  + "\n" +
						"MAC Address: " + NetInfo[3]  + "\n" +
						"LinkSpeed: " + NetInfo[4]  + WifiInfo.LINK_SPEED_UNITS + "\n" +
						"Rssi: " + NetInfo[5]  + "dBm" + "\n" +
						"Rssi Level: " + NetInfo[6] ;



		netinfo.setText(strWifiInfo);




	}






	@SuppressWarnings("deprecation")
	public void makeQuery() {
		Date sDate = new Date();
		Date eDate = new Date();

		sDate.setHours(0);
		sDate.setMinutes(0);
		sDate.setSeconds(0);


		eDate.setDate(sDate.getDate()+1);
		eDate.setHours(2);
		eDate.setMinutes(0);
		eDate.setSeconds(0);


		Calendar calendar1 = Calendar.getInstance();
		Date today = calendar1.getTime();
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.DAY_OF_YEAR, 1);
		calendar2.set(Calendar.HOUR_OF_DAY, 2);
		calendar2.set(Calendar.MINUTE, 0);
		Date tomorrow = calendar2.getTime();

		Context context = getApplicationContext();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String todayAsString = dateFormat.format(eDate);




		Toast.makeText(context, "Reading Events" +(CharSequence) todayAsString, Toast.LENGTH_SHORT).show();


		Cursor cur = null;
		Cursor cur2=null; //for instances

		ContentResolver cr = getContentResolver();
		ContentResolver cr2 = getContentResolver(); //for instances

		Uri uri = CalendarContract.Events.CONTENT_URI;
		Builder uri2 = Uri.parse("content://com.android.calendar/instances/when").buildUpon(); // for instances

		ContentUris.appendId(uri2, Long.MIN_VALUE);
		ContentUris.appendId(uri2, Long.MAX_VALUE); 


		String selection = "((" + CalendarContract.Events.ACCOUNT_NAME + " = ?) AND ("
				+ CalendarContract.Events.DTSTART + " >= ?) AND ("+ CalendarContract.Events.DTEND + " <= ?))";

		String selection2 = "((" + CalendarContract.Events.ACCOUNT_NAME + " = ?) AND ("
				+ CalendarContract.Instances.BEGIN + " >= ?) AND ("+ CalendarContract.Instances.END + " <= ?))";

		String[] selectionArgs = new String[]{selectedAccount.name, sDate.getTime() + "", eDate.getTime() + "" };





		// Submit the query and get a Cursor object back.


		cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
		cur2 = cr2.query(uri2.build(), EVENT_PROJECTION_INS, selection2, selectionArgs, null);




		while (cur2.moveToNext()) {
			long calID = 0;
			String title,owner,strDate,endDate,eventaccess,calendarname,calendaraccess;
			String rrule,desc,allday,attdata;

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			// Create a calendar object that will convert the date and time value in milliseconds to date.
			Calendar calendar = Calendar.getInstance();



			title = cur2.getString(4);
			owner = cur2.getString(3);
			calendar.setTimeInMillis(Long.parseLong(cur2.getString(1)));
			strDate = formatter.format(calendar.getTime());
			calendar.setTimeInMillis(Long.parseLong(cur2.getString(2)));
			endDate = formatter.format(calendar.getTime());
			eventaccess= cur2.getString(5);
			calendarname=cur2.getString(6);
			calendaraccess=cur2.getString(7);
			rrule=cur2.getString(8);
			desc=cur2.getString(9);
			allday=cur2.getString(10);
			attdata=cur2.getString(11);




			ins.add(new CalendarEvent(title,owner,strDate,endDate,eventaccess,calendarname,calendaraccess,rrule,desc,allday,attdata));


		};
		adapter_ins = new EventAdapter(this, -1, ins.toArray(new CalendarEvent[ins.size()]));
		insList.setAdapter(adapter_ins);






	}





	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
