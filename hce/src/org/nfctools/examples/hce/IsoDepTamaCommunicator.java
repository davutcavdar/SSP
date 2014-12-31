package org.nfctools.examples.hce;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.nfctools.io.ByteArrayReader;
import org.nfctools.io.ByteArrayWriter;
import org.nfctools.spi.tama.AbstractTamaCommunicator;
import org.nfctools.spi.tama.request.DataExchangeReq;
import org.nfctools.spi.tama.request.InListPassiveTargetReq;
import org.nfctools.spi.tama.response.DataExchangeResp;
import org.nfctools.spi.tama.response.InListPassiveTargetResp;
import org.nfctools.utils.NfcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class IsoDepTamaCommunicator extends AbstractTamaCommunicator {
	
	
	public static String sendreq ;
	
	public static SerialCommunicator sc;
	
	
	

	private Logger log = LoggerFactory.getLogger(getClass());
	private int messageCounter = 0;
	private static final byte[] CLA_INS_P1_P2 = { 0x00, (byte)0xA4, 0x04, 0x00 };
	private static final byte[] AID_ANDROID = { (byte)0xF0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };

	public IsoDepTamaCommunicator(ByteArrayReader reader, ByteArrayWriter writer) {
		super(reader, writer);
	}

	private byte[] createSelectAidApdu(byte[] aid) {
		byte[] result = new byte[6 + aid.length];
		System.arraycopy(CLA_INS_P1_P2, 0, result, 0, CLA_INS_P1_P2.length);
		result[4] = (byte)aid.length;
		System.arraycopy(aid, 0, result, 5, aid.length);
		result[result.length - 1] = 0;
		return result;
	}
		
	public void connectAsInitiator() throws IOException {
		
		
		
		while (true) {
			
			

			InListPassiveTargetResp inListPassiveTargetResp = sendMessage(new InListPassiveTargetReq((byte)1, (byte)0,
					new byte[0]));
			if (inListPassiveTargetResp.getNumberOfTargets() > 0) {
				log.info("TargetData: " + NfcUtils.convertBinToASCII(inListPassiveTargetResp.getTargetData()));
				if (inListPassiveTargetResp.isIsoDepSupported()) {
					log.info("IsoDep Supported");
					byte[] selectAidApdu = createSelectAidApdu(AID_ANDROID);
					DataExchangeResp resp = sendMessage(new DataExchangeReq(inListPassiveTargetResp.getTargetId(),
							false, selectAidApdu, 0, selectAidApdu.length));
					String dataIn = new String(resp.getDataOut());
					log.info("Received: " + dataIn);
				
				
				//sendreq=dataIn;
				
			
				Ask(dataIn);
				

	
				
					if (dataIn.startsWith("")) {
						exchangeData(inListPassiveTargetResp);
					}
				}
				else {
					log.info("IsoDep NOT Supported");
				}
				break;
			}
			else {
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	
	public void Ask (String key) {
		
		
		
		
		System.out.println("sorgulanan key sendreq: " + key);
		
	
		
		
		
		String sURL = "http://144.122.59.11:8181/smartpassApplication-2.3/rest/doorOperation/unlockDoor/"+ key + "/" + "1"; //just a string

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

		System.out.println("key: "+ key);
		
		System.out.println("Sonuc Kod : " +response);
		
	
		sc = new SerialCommunicator();


		if (response.equals("1")) {
			byte[] data = new byte[1];
			data[0] = '1';
			
			sc.writetoport(data);
			System.out.println("Sonuc :Kapý acýlýyor");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			data[0] = '0'; // arduino ya sürekli akým vermesin diye
			sc.writetoport(data);

		}
		else {
			
			byte[] data = new byte[1];
			data[0] = '0';
			sc.writetoport(data);
			System.out.println("Sonuc : Yetkiniz yok");

		}
		
		
		
		
		
		
		
		
	}
	
	
	
	private void exchangeData(InListPassiveTargetResp inListPassiveTargetResp) throws IOException {
		DataExchangeResp resp;
		String dataIn;
		while (true) {
			byte[] dataOut = ("Message from desktop: " + messageCounter++).getBytes();
			resp = sendMessage(new DataExchangeReq(inListPassiveTargetResp.getTargetId(), false, dataOut, 0,
					dataOut.length));
			dataIn = new String(resp.getDataOut());
		//	log.info("Received: " + dataIn);
		}
	}
}
