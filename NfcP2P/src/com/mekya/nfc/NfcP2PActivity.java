package com.mekya.nfc;

import java.nio.charset.Charset;

import android.app.Activity;
import android.app.Service;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.widget.EditText;
import android.widget.Toast;

public class NfcP2PActivity extends Activity implements CreateNdefMessageCallback, OnNdefPushCompleteCallback{
    
	private static final int MESSAGE_SENT = 1;
	private NfcAdapter mAdapter;
	private EditText dataEditText;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        NfcManager manager = (NfcManager)getSystemService(Service.NFC_SERVICE);
        
        mAdapter = manager.getDefaultAdapter();
        
        mAdapter.setNdefPushMessageCallback(this, this);
        
        mAdapter.setOnNdefPushCompleteCallback(this, this);

        
        dataEditText = (EditText) findViewById(R.id.dataEditText);
        
        
    }
    
    
    
    public void onResume() {
        super.onResume();
    }

    
    public void onPause() {
        super.onPause();
    }


	public NdefMessage createNdefMessage(NfcEvent event) {
		Time time = new Time();
        time.setToNow();
        
        String text = ("Beam me up!\n\n" +
    			"Beam Time: " + time.format("%H:%M:%S"));
        
        String dataText = dataEditText.getText().toString();
        if (dataText.equals("") == false) {
        	text = dataText;
        }
        
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMimeRecord(
                        "application/com.motes.controlpanel", text.getBytes())
         /**
          * The Android Application Record (AAR) is commented out. When a device
          * receives a push with an AAR in it, the application specified in the AAR
          * is guaranteed to run. The AAR overrides the tag dispatch system.
          * You can add it back in to guarantee that this
          * activity starts when receiving a beamed message. For now, this code
          * uses the tag dispatch system.
          */
          //,NdefRecord.createApplicationRecord("com.example.android.beam")
        });
        return msg;

	}
	
	 public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
	        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
	        NdefRecord mimeRecord = new NdefRecord(
	                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
	        return mimeRecord;
	    }
	 
	 
	 private final Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case MESSAGE_SENT:
	                Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_LONG).show();
	                break;
	            }
	        }
	    };

	    public void onNdefPushComplete(NfcEvent event) {
	    	mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
	    }
	

}