package org.nfctools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.nfctools.llcp.LlcpOverNfcip;
import org.nfctools.ndef.Record;
import org.nfctools.ndef.mime.BinaryMimeRecord;
import org.nfctools.scio.TerminalMode;
import org.nfctools.snep.SnepConstants;
import org.nfctools.snep.SnepServer;
import org.nfctools.snep.Sneplet;
import org.nfctools.spi.acs.AcsTerminal;
import org.nfctools.utils.CardTerminalUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;







@SuppressWarnings("deprecation")
public class MainNFC {
	
	  static String response=null;
	
	
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
	
	public static void authenticate(final String doorid, final String key) throws Exception {
		
		
		String sURL = "http://144.122.59.11:8181/smartpassApplication-2.3/rest/doorOperation/unlockDoor/"+ doorid + "/" + key; //just a string
		 
	    // Connect to the URL using java's native library
	    URL url = null;
		try {
			url = new URL(sURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    HttpURLConnection request = null;
		try {
			request = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			request.connect();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	    // Convert to a JSON object to print data
	    JsonParser jp = new JsonParser(); //from gson
	    JsonElement root = null;
		try {
			root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //convert the input stream to a json element
	    JsonObject rootobj = root.getAsJsonObject(); //may be an array, may be an object. 
	    String response = rootobj.get("response").getAsString();
	
	System.out.println(response);
	
	
		
		
		
		
		
		
			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	private static SerialCommunicator sc;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		AcsTerminal acs = new AcsTerminal();
		
		acs.setCardTerminal(CardTerminalUtils.getTerminalByName("ACR122"));
		
		NfcAdapter adapter = new NfcAdapter(acs, TerminalMode.INITIATOR);
		
		LlcpOverNfcip initiator = new LlcpOverNfcip();

		adapter.setNfcipConnectionListener(initiator);
		
		
//		SnepClient snepClient = new SnepClient();
//		
//		snepClient.setSnepAgentListener(new SnepAgentListener() {
//			
//			boolean dataSent = false;
//			
//			@Override
//			public void onSnepConnection(SnepAgent snepAgent) {
//				List<Record> records = new ArrayList<Record>();
//				
//				//AndroidApplicationRecord aar = new AndroidApplicationRecord("com.example.android.beam");
//				
//				TextMimeRecord mimeRecord = new TextMimeRecord("application/com.mekya.nfc.NfcP2PActivity", "Hello".getBytes());
//				records.add(mimeRecord); //createMimeRecord("application/com.example.android.beam", "Hello".getBytes()));
//				
//				snepAgent.doPut(records, new PutResponseListener() {
//					
//					@Override
//					public void onSuccess() {
//						System.out
//								.println("MainNFC.main(...).new SnepAgentListener() {...}.onSnepConnection(...).new PutResponseListener() {...}.onSuccess()");
//						dataSent = true;
//					}
//					
//					@Override
//					public void onFailed() {
//						System.out
//								.println("MainNFC.main(...).new SnepAgentListener() {...}.onSnepConnection(...).new PutResponseListener() {...}.onFailed()");
//					}
//				});
//			}
//			
//			@Override
//			public boolean hasDataToSend() {
//				return !dataSent;
//			}
//		});
//		
//		
//		adapter.registerTagListener(new NfcTagListener() {
//			
//			@Override
//			public void handleTag(Tag tag) {
//				System.out.println("Handle tag");	
//			}
//			
//			@Override
//			public boolean canHandle(Tag tag) {
//				System.out
//						.println("MainNFC.main(...).new NfcTagListener() {...}.canHandle()");
//				return false;
//			}
//		});
//		
//		adapter.registerUnknownTagListerner(new UnknownTagListener() {
//			
//			@Override
//			public void unsupportedTag(Tag tag) {
//				System.out
//						.println("MainNFC.main(...).new UnknownTagListener() {...}.unsupportedTag()");
//			}
//		});
//		
//		initiator.getConnectionManager().registerServiceAccessPoint(snepClient);

		 sc = new SerialCommunicator();
		
		SnepServer snepServer = new SnepServer(new Sneplet() {
			
			@Override
			public void doPut(Collection<Record> requestRecords) {
				System.out
						.println("MainNFC.main(...).new Sneplet() {...}.doPut()");
				
				Iterator<Record> recordIterator = requestRecords.iterator();
				
				do {
					BinaryMimeRecord rec = (BinaryMimeRecord) recordIterator.next();
					
					String id = new String(rec.getId());
					String key = new String(rec.getKey());
					
					String content = new String(rec.getContent());
					
					System.out.println(content);
					

					String sURL = "http://144.122.59.11:8181/smartpassApplication-2.3/rest/doorOperation/unlockDoor/"+ content + "/" + "1"; //just a string
					 
				    // Connect to the URL using java's native library
				    URL url = null;
					try {
						url = new URL(sURL);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    HttpURLConnection request = null;
					try {
						request = (HttpURLConnection) url.openConnection();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    try {
						request.connect();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				    // Convert to a JSON object to print data
				    JsonParser jp = new JsonParser(); //from gson
				    JsonElement root = null;
					try {
						root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
					} catch (JsonIOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonSyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //convert the input stream to a json element
				    JsonObject rootobj = root.getAsJsonObject(); //may be an array, may be an object. 
				    String response = rootobj.get("response").getAsString();
				
				System.out.println(response);
					
					
					
					
					
					if (response.equals("1")) {
						byte[] data = new byte[1];
						data[0] = '1';
						sc.writetoport(data);
						System.out.println("Kapý acýlýyor");
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						data[0] = '0';
						sc.writetoport(data);
						
					}
					else {
						byte[] data = new byte[1];
						data[0] = '0';
						sc.writetoport(data);
						System.out.println("Yetkiniz yok");
						
					}
				
					System.out.println("key: "+ content);
					
					
					
				}while(recordIterator.hasNext());
				
			}
			
			
			
			
			
			@Override
			public Collection<Record> doGet(Collection<Record> requestRecords) {
				System.out
						.println("MainNFC.main(...).new Sneplet() {...}.doGet()");
				return null;
			}
			
			
		});
		
//		initiator.getConnectionManager().registerServiceAccessPoint(snepServer);
		initiator.getConnectionManager().registerWellKnownServiceAccessPoint(SnepConstants.SNEP_SERVICE_NAME, snepServer);
		initiator.getConnectionManager().registerServiceAccessPoint(SnepConstants.SNEP_SERVICE_ADDRESS, snepServer);
		
		adapter.startListening();
	}
	
	
	
	
	

}
