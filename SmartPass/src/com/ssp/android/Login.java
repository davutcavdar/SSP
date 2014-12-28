package com.ssp.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;





import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends Activity {

	private Button gonder;
	private Button btregfw;
	private TextView sonuc;
	private EditText TxtUser;
	private EditText TxtPass;
	String aLogin=null;
	String bLogin=null;
	String cLogin=null;
	
	
	
	

	String denem = null;
	String result = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		gonder = (Button) findViewById(R.id.bt_register);
		sonuc = (TextView) findViewById(R.id.textView2);
		TxtUser = (EditText) findViewById(R.id.regmail);
		TxtPass = (EditText) findViewById(R.id.regpass);
		sonuc = (TextView) findViewById(R.id.textView2);
		btregfw = (Button) findViewById(R.id.bt_regfw);
		final Intent regfw = new Intent(this, Register.class);
		final Spinner s = (Spinner) findViewById(R.id.spinner1);






		
		

		btregfw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(regfw);

			}
		});

		gonder.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {

					String username = null;
					String password = null;

					username = TxtUser.getText().toString();
					password = TxtPass.getText().toString();
					
					final int selected = s.getSelectedItemPosition();

					final String[] degerler = getResources().getStringArray(R.array.expiretime);

					final String expiretime = degerler[selected];
					
					sendJson(username, password, expiretime);
					//sendJson(username, password);
					
					
					//Toast.makeText(getApplicationContext(), expiretime , Toast.LENGTH_LONG).show();
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}






	public static String convertStreamToString(InputStream is) throws Exception {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		return sb.toString();
	}

	public void sendJson(final String email, final String pwd,final String expire) {

		final Intent requestActivityIntent = new Intent (this, Request.class);

		Thread t = new Thread() {

			public void run() {
				Looper.prepare();
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000);

				JSONObject json = new JSONObject();

				try {
					HttpGet post = new HttpGet(
							MainActivity.SERVER_ADDR + "authentication/login/"+ email + "/" + pwd + "/" + expire);
					// json.put("email", email);
					// json.put("password", pwd);
					StringEntity se = new StringEntity(json.toString());
					se.setContentType("application/json;charset=UTF-8");
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json;charset=UTF-8"));
					// post.setEntity(se);
					HttpResponse httpresponse = client.execute(post);

					/* Checking response */
					if (httpresponse != null) {
						InputStream in = httpresponse.getEntity().getContent(); // Get
						// response

						result = convertStreamToString(in);
						JSONObject res_login = new JSONObject(result);

						aLogin = res_login.getString("status").toString();
						bLogin = res_login.getString("key").toString();
						cLogin = res_login.getString("expireTime").toString();

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								
								if (aLogin.equals("1")){	
									requestActivityIntent.putExtra(Request.KEY, bLogin);
									startActivity(requestActivityIntent);
									//	login basarili

									Toast.makeText(getApplicationContext(), "Login Basarili, Expire Time:" + cLogin , Toast.LENGTH_LONG).show();
									Toast.makeText(getApplicationContext(), "Key:"+ bLogin, Toast.LENGTH_LONG).show();
								}
								else if (aLogin.equals("0")){
									
									//	login basarisiz

									sonuc.setText("");
									Toast.makeText(getApplicationContext(), "Kullanici Adi/Sifre Hatali", Toast.LENGTH_LONG).show();
								}
							}
						});
					}
				}

				catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Baglanti Hatasi Olustu", Toast.LENGTH_LONG).show();
				}
				Looper.loop();
			}
		};

		t.start();

	}
	
	
}
