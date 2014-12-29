package org.nfctools.examples.hce;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.smartcardio.CardTerminal;





import org.nfctools.examples.TerminalUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class HceDemo {

	

	
	@SuppressWarnings("deprecation")
	public void run() {
		
		
		
		CardTerminal cardTerminal = TerminalUtils.getAvailableTerminal().getCardTerminal();
		HostCardEmulationTagScanner tagScanner = new HostCardEmulationTagScanner(cardTerminal);
		tagScanner.run();
		
		
	}

	//private static SerialCommunicator sc;
	
	

	
	public static void main(String[] args) {
		
		new HceDemo().run();
		
		
	
		
		
	}
}
