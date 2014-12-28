package com.ssp.android;



import android.app.Activity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;





import android.os.Looper;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class Register extends Activity {

	String resultpre_reg=null;
	String resultreg=null;
	private TextView pre_regsonuc; 
	private Button bt_reg;
	private Button bt_comp_reg;
	String aJsonString = null;
	String bJsonString = null;
	String cJsonString = null;
	String DeviceImei;
	
	private TextView imeitxt;
	private EditText reg_tel;
	private EditText reg_user;
	private EditText reg_pass;
	String reg_username = null;
	String reg_password = null;
	String reg_telefon = null;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		bt_reg = (Button) findViewById(R.id.bt_register);
		bt_comp_reg = (Button) findViewById(R.id.bt_comp_register);
		pre_regsonuc = (TextView) findViewById(R.id.pre_regresult);
		imeitxt = (TextView) findViewById(R.id.txt_imei);
		reg_tel = (EditText) findViewById(R.id.Telno_edit);
		reg_user = (EditText) findViewById(R.id.regmail);
		reg_pass = (EditText) findViewById(R.id.regpass);
		
	
		bt_comp_reg.setVisibility(View.INVISIBLE);
		
		TelephonyManager telefon = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		DeviceImei = telefon.getDeviceId();
		imeitxt.setText(DeviceImei); //imei bas
		
		

		
		
		
		
		
		bt_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				reg_username = reg_user.getText().toString();
				reg_password = reg_pass.getText().toString();
				reg_telefon = reg_tel.getText().toString();
			

				PreRegister(reg_username,reg_password,reg_telefon, DeviceImei); 

			}
		});


		bt_comp_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


		
				Register_Complete(reg_username,reg_password,reg_telefon, DeviceImei,bJsonString); 

				
				
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



	
	public void Register_Complete (final String email, final String pwd, final String tel,final String imei,final String key) {
		Thread t = new Thread() {


			public void run() {
				Looper.prepare(); 
				DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());
				
				JSONObject json = new JSONObject();

				try {
					HttpGet post = new HttpGet(MainActivity.SERVER_ADDR + "registration/sendUserActivationRequest/"+email+"/"+pwd+"/"+tel+"/"+imei+ "/"+key);
			
					post.setHeader("Content-type", "application/json");
					StringEntity se = new StringEntity( json.toString());
					se.setContentType("application/json;charset=UTF-8");
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
				

					HttpResponse httpresponse = client.execute(post);
		
					/*Checking response */
					if(httpresponse!=null){
					
						HttpEntity resultentity = httpresponse.getEntity(); //Get response
						InputStream inputstream = resultentity.getContent();
						

						resultreg = convertStreamToString(inputstream);

						JSONObject res_reg = new JSONObject(resultreg);


						cJsonString = res_reg.getString("status").toString();
						


						runOnUiThread(new Runnable() {
							@Override
							public void run() {

								if (aJsonString.equals("1")){
									
									pre_regsonuc.setText("");
									Toast.makeText(getApplicationContext(), "Kullanici Kaydi Yapildi", Toast.LENGTH_LONG).show();
									bt_reg.setVisibility(View.INVISIBLE); }
									
									else if (aJsonString.equals("0")){
										
										pre_regsonuc.setText("");
										Toast.makeText(getApplicationContext(), "Hata Olustu", Toast.LENGTH_LONG).show();
										bt_reg.setVisibility(View.INVISIBLE);
										
									}
							}
						});
						
					}

				}
				catch(Exception e) {
					e.printStackTrace();
					showDialog("Error", "Cannot Estabilish Connection");
				}
				Looper.loop(); 
			}

			private void showDialog(String string, String string2) {
			
			}
		};

		t.start();

	}
	

	public void PreRegister (final String email, final String pwd, final String tel,final String imei) {
		Thread t = new Thread() {


			public void run() {
				Looper.prepare(); 
				DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());
				
				JSONObject json = new JSONObject();

				try {
					HttpGet post = new HttpGet(MainActivity.SERVER_ADDR + "registration/sendRegistrationRequest/"+email+"/"+pwd+"/"+tel+"/"+imei);
			
					post.setHeader("Content-type", "application/json");
					StringEntity se = new StringEntity( json.toString());
					se.setContentType("application/json;charset=UTF-8");
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
				

					HttpResponse httpresponse = client.execute(post);
		
					/*Checking response */
					if(httpresponse!=null){
					
						HttpEntity resultentity = httpresponse.getEntity(); //Get response
						InputStream inputstream = resultentity.getContent();
						

						resultpre_reg = convertStreamToString(inputstream);

						JSONObject res_prereg = new JSONObject(resultpre_reg);


						aJsonString = res_prereg.getString("status").toString();
						bJsonString = res_prereg.getString("key").toString();


						runOnUiThread(new Runnable() {
							@Override
							public void run() {

								if (aJsonString.equals("1")){
									
									pre_regsonuc.setText("Kullanici Adi Uygun" );
									
									Toast.makeText(getApplicationContext(), "Auth Key:" + bJsonString, Toast.LENGTH_LONG).show();
								
									bt_comp_reg.setVisibility(View.VISIBLE); }
									
									else if (aJsonString.equals("0")){
										pre_regsonuc.setText("");
										Toast.makeText(getApplicationContext(), "Kullanici Adi Mevcut", Toast.LENGTH_LONG).show();
										bt_comp_reg.setVisibility(View.INVISIBLE);
										
									}
							}
						});
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					showDialog("Error", "Cannot Estabilish Connection");
				}
				Looper.loop(); 
			}

			private void showDialog(String string, String string2) {
			
			}
		};
		t.start();
	}
}
