package com.ssp.android;






import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

//import android.widget.Toast;




//apdu servisi manifest e yazılıp, xml altına eklenecek.



public class MainActivity extends Activity {


	String aLogin=null;
	String bLogin=null;
	String cLogin=null;
	private Button logfw;
	
	public static final String SERVER_ADDR = "http://144.122.59.11:8181/smartpassApplication-2.3/rest/";
	
	

	String denem = null;
	String result = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		logfw = (Button) findViewById(R.id.firstlogin);
		final Intent fw = new Intent(this, Login.class);
		
		final Context context = this;
		






		AlertDialog.Builder adaptorkapali = new AlertDialog.Builder(
				context);

		// set title
		adaptorkapali.setTitle("NFC Status");

		// set dialog message
		adaptorkapali.setMessage("NFC Adaptoru Kapalı, Lutfen Aciniz").setCancelable(true)


		 .setPositiveButton("OK",new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog,int id) {
		 // if this button is clicked, close
		 // current activity
		 MainActivity.this.finish();
		 }
		 })
//		.setNegativeButton("Close",
//				new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				// if this button is clicked, just close
//				// the dialog box and do nothing
//				dialog.cancel();
//			}
//		})
		
		;

		
		
		AlertDialog.Builder adaptoracik = new AlertDialog.Builder(
				context);

		// set title
		adaptoracik.setTitle("NFC Status");

		// set dialog message
		adaptoracik.setMessage("NFC Adaptoru Acik").setCancelable(true)


//		 .setPositiveButton("OK",new DialogInterface.OnClickListener() {
//		 public void onClick(DialogInterface dialog,int id) {
//		 // if this button is clicked, close
//		 // current activity
//		 MainActivity.this.finish();
//		 }
//		 })
		.setNegativeButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		})
		
		;
		
	
		// create alert dialog
		AlertDialog alertkapali = adaptorkapali.create();
		AlertDialog alertacik = adaptoracik.create();
		
		
		NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
		NfcAdapter adapter = manager.getDefaultAdapter();
		if (adapter != null && adapter.isEnabled()) {
		    // adapter exists and is enabled.
			
			alertacik.show();
			
		}
		
		
		else {
			
			alertkapali.show();
			
		}
		

		
		
		logfw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(fw);

			}
		});

		
		

	}
	
	
}
