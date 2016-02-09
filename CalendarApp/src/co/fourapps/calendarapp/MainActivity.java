package co.fourapps.calendarapp;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner mailChooser;
    private ListView eventList;
    private Account[] accounts;
    private Account selectedAccount;
    private EventAdapter adapter;
    private List<CalendarEvent> events;

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID,                           // 0
            CalendarContract.Events.DTSTART,                  // 1
            CalendarContract.Events.DTEND,         // 2
            CalendarContract.Events.OWNER_ACCOUNT,                  // 3
            CalendarContract.Events.TITLE // 4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mailChooser = (Spinner) findViewById(R.id.spinner);
        eventList = (ListView) findViewById(R.id.listView);

        events = new ArrayList<CalendarEvent>();


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
        String tomorrowAsString = dateFormat.format(tomorrow);
        
      
        int duration = Toast.LENGTH_SHORT;
        
        Toast.makeText(context, (CharSequence) todayAsString, duration).show();

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;
        //String selection = "((" + CalendarContract.Events.ACCOUNT_NAME + " = ?))";
        String selection = "((" + CalendarContract.Events.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Events.DTSTART + " >= ?) AND ("+ CalendarContract.Events.DTEND + " <= ?))";
     
        
        String[] selectionArgs = new String[]{selectedAccount.name, sDate.getTime() + "", eDate.getTime() + "" };

        

        
        
        // Submit the query and get a Cursor object back.
       
        
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            long calID = 0;
            String title,owner,strDate,endDate;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();



            title = cur.getString(4);
            owner = cur.getString(3);
            calendar.setTimeInMillis(Long.parseLong(cur.getString(1)));
            strDate = formatter.format(calendar.getTime());
            calendar.setTimeInMillis(Long.parseLong(cur.getString(2)));
            endDate = formatter.format(calendar.getTime());

            events.add(new CalendarEvent(title,owner,strDate,endDate));

        }

        adapter = new EventAdapter(this, -1, events.toArray(new CalendarEvent[events.size()]));
        eventList.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}