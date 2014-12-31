package com.ssp.relayapp;




import com.ssp.relayapp.IsoDepTransceiver.OnMessageReceived;

import android.app.Activity;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.ReaderCallback;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity1 extends Activity implements OnMessageReceived, ReaderCallback {

	private NfcAdapter nfcAdapter;
	//private ListView listView;
	private IsoDepAdapter isoDepAdapter;
	private Button chdata;
	private EditText Txt;
	private TextView info;
	public static String key="davut" ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		//listView = (ListView)findViewById(R.id.listView);
		isoDepAdapter = new IsoDepAdapter(getLayoutInflater());
		//listView.setAdapter(isoDepAdapter);
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);


		chdata = (Button) findViewById(R.id.button1);
		Txt= (EditText) findViewById(R.id.editText1);
		info= (TextView) findViewById(R.id.info);
		Txt.setText(key);




		chdata.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {


				try {

					
					
					MyHostApduService.carddata= key;
					
					info.setText("Relay Card is ready!!");
					 info.setTextColor(Color.parseColor("#FF0000"));
					
					//		String CardData = null;
					//	CardData = Txt.getText().toString();
					//	MyHostApduService.carddata= CardData;




				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});









	}

	@Override
	public void onResume() {
		super.onResume();
		// nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
		//		null);
	}

	@Override
	public void onPause() {
		super.onPause();
		nfcAdapter.disableReaderMode(this);
	}

	public void onTagDiscovered(Tag tag) {
		IsoDep isoDep = IsoDep.get(tag);
		IsoDepTransceiver transceiver = new IsoDepTransceiver(isoDep, this);
		Thread thread = new Thread(transceiver);
		thread.start();
	}

	public void onMessage(final byte[] message) {
		runOnUiThread(new Runnable() {

			public void run() {
				isoDepAdapter.addMessage(new String(message));
			}
		});
	}

	public void onError(Exception exception) {
		onMessage(exception.getMessage().getBytes());
	}
}
