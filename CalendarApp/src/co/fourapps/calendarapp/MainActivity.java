package co.fourapps.calendarapp;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;

import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



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
            CalendarContract.Events.DTSTART,                  // 1
            CalendarContract.Events.DTEND,         // 2
            CalendarContract.Events.OWNER_ACCOUNT,                  // 3        
            CalendarContract.Events.TITLE, // 4
            CalendarContract.Events.ACCESS_LEVEL, //5
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,//6
            CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, //7
            CalendarContract.Instances.BEGIN //8
            
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        
        mailChooser = (Spinner) findViewById(R.id.spinner);
        eventList = (ListView) findViewById(R.id.listView);
        insList = (ListView) findViewById(R.id.insView);
        datatext = (TextView) findViewById(R.id.VolumeTxt);

        events = new ArrayList<CalendarEvent>();


		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
		volume_level= am.getRingerMode();
		
		
		if (volume_level==0)
		{
			datatext.setText("Telefonun ses seviyesi: sessiz"); }
			
			if (volume_level==2){
				datatext.setText("Telefonun ses seviyesi: sesli");}
				
			if (volume_level==1){
				datatext.setText("Telefonun ses seviyesi: titresim");}
		
		
	
        
        
        
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
        makeQuery();

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
        Uri uri2 = CalendarContract.Instances.CONTENT_URI; // for instances
        
        String selection = "((" + CalendarContract.Events.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Events.DTSTART + " >= ?) AND ("+ CalendarContract.Events.DTEND + " <= ?))";
     
        
        String[] selectionArgs = new String[]{selectedAccount.name, sDate.getTime() + "", eDate.getTime() + "" };

        

        
        
        // Submit the query and get a Cursor object back.
       
        
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        cur2 = cr2.query(uri2, EVENT_PROJECTION_INS, selection, selectionArgs, null);

        
        while (cur.moveToNext()) {
            long calID = 0;
            String title,owner,strDate,endDate,eventaccess,calendarname,calendaraccess;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();



            title = cur.getString(4);
            owner = cur.getString(3);
            calendar.setTimeInMillis(Long.parseLong(cur.getString(1)));
            strDate = formatter.format(calendar.getTime());
            calendar.setTimeInMillis(Long.parseLong(cur.getString(2)));
            endDate = formatter.format(calendar.getTime());
            eventaccess= cur.getString(5);
            calendarname=cur.getString(6);
            calendaraccess=cur.getString(7);

            events.add(new CalendarEvent(title,owner,strDate,endDate,eventaccess,calendarname,calendaraccess));

           
        };
        adapter = new EventAdapter(this, -1, events.toArray(new CalendarEvent[events.size()]));
        eventList.setAdapter(adapter);
  
    
        while (cur2.moveToNext()) {
            long calID = 0;
            String title,owner,strDate,endDate,eventaccess,calendarname,calendaraccess;

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

            ins.add(new CalendarEvent(title,owner,strDate,endDate,eventaccess,calendarname,calendaraccess));

           
        };
        adapter_ins = new EventAdapter(this, -1, ins.toArray(new CalendarEvent[ins.size()]));
        insList.setAdapter(adapter_ins);
    
        
        
        
        
        
        
        
        
        
    
    
    
    
    
    
    
    
    }

    
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
