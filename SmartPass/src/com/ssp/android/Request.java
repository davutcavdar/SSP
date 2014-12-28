package com.ssp.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;


//public class Request extends Activity implements CreateNdefMessageCallback, OnNdefPushCompleteCallback{

public class Request extends Activity {




	String reqresult=null;
	private ImageButton SentReq;
	private String key;

	

	public final static String KEY = "key";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request);

//		NfcManager manager = (NfcManager)getSystemService(Service.NFC_SERVICE);
//
//		mAdapter = manager.getDefaultAdapter();
//
//		mAdapter.setNdefPushMessageCallback(this, this);
//
//		mAdapter.setOnNdefPushCompleteCallback(this, this);


		Intent i = getIntent();
		if (i.getExtras().containsKey(KEY)) {
			key = (String)i.getExtras().get(KEY);

		}
		
		MyHostApduService.carddata= key;
		SentReq = (ImageButton) findViewById(R.id.imageButton1);

		SentReq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				MyHostApduService.carddata= key;
				Toast.makeText(getApplicationContext(), "Istek Gonderildi", Toast.LENGTH_LONG).show();

			}
		});
	}

	public static String convertStreamToString(InputStream is) throws Exception {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		return sb.toString();
	}

//	@Override
//	public void onNdefPushComplete(NfcEvent arg0) {
//		mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
//
//	}
//
//	private final Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case MESSAGE_SENT:
//				Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_LONG).show();
//				break;
//			}
//		}
//	};

//	public NdefMessage createNdefMessage(NfcEvent event) {
//		Time time = new Time();
//		time.setToNow();
//
//
//
//		NdefMessage msg = new NdefMessage(
//				new NdefRecord[] { createMimeRecord(
//						"application/com.motes.controlpanel", key.getBytes())
//						/**
//						 * The Android Application Record (AAR) is commented out. When a device
//						 * receives a push with an AAR in it, the application specified in the AAR
//						 * is guaranteed to run. The AAR overrides the tag dispatch system.
//						 * You can add it back in to guarantee that this
//						 * activity starts when receiving a beamed message. For now, this code
//						 * uses the tag dispatch system.
//						 */
//						//,NdefRecord.createApplicationRecord("com.example.android.beam")
//				});
//		return msg;
//
//	}
//
//	public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
//		byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
//		NdefRecord mimeRecord = new NdefRecord(
//				NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
//		return mimeRecord;
//	}

}
