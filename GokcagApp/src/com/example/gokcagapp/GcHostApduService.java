package com.example.gokcagapp;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

public class GcHostApduService extends HostApduService {

	private String sCardHolderName = "";
	private String sPhoneNumber = "";
	private Set<String> aUsableTickets;
	private Set<String> aUsedTickets;
	private long lCUTA = -1;
	private long lCTTA = -1;
	
	public GcHostApduService() 
	{
		super();
		aUsableTickets = null;
		aUsedTickets = null;
	}
	
	
	
	@Override
	public void onDeactivated(int reason) {

		this.sCardHolderName = "";
		this.sPhoneNumber = "";
		this.aUsableTickets = null;
		this.aUsedTickets = null;
		this.lCUTA = -1;
		this.lCTTA = -1;
	}

	@Override
	public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) 
	{

		
		if (commandApdu.length >= 5)
		{
			//Select command
			if (commandApdu[0] == (byte)0x00 && commandApdu[1] == (byte)0xa4)
				return Util.Hex2Bin(getString(R.string.SW_SUCCESS_RESPONSE));
			else if ((commandApdu[0] & (byte)0x80) == (byte)0x80)
			{
				//Check security for second phase
				//if ((commandApdu[0] & (byte)0x04) == (byte)0x04)
				//	Check SecurityFunction(commandApdu);
				//Get Data Command
				if (commandApdu[1] == (byte)0xCA)
				{
					if (commandApdu[2] == (byte)0x00 )
					{
						switch(commandApdu[3])
						{
							case (byte)0x00:
								return Util.Hex2Bin(getString(R.string.SW_SUCCESS_RESPONSE) + getString(R.string.SUPPORTED_APPLICATION_MODE));
							case (byte)0x01:
								return getUserNameResponse(commandApdu);
							case (byte)0x02:
								return getPhoneNumberResponse(commandApdu);
							case (byte)0x03:
								return getUsablePlainTicketsResponse(commandApdu);
							case (byte)0x04:
								return getUsedPlainTicketsResponse(commandApdu);
							case (byte)0x05:
								return getCUTAResponse(commandApdu);
							case (byte)0x06:
								return getCTTAResponse(commandApdu);
							default:
								return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
						}
					}
					else
						return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
				}
				//Get Data Command
				else if (commandApdu[1] == (byte)0xE2)
				{
					switch(commandApdu[2])
					{
						case (byte)0x01:
							return storeTicket(commandApdu);
						case (byte)0x02:
							return useTicket(commandApdu);
						case (byte)0x03:
							return deleteTicket(commandApdu);
						case (byte)0x04:
							return useBalance(commandApdu);
						default:
							return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
					}
				}
				else 
					return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
			}
			else
				return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
		}

		return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
	}

	private byte[] getUserNameResponse(byte[] commandApdu)
	{
		try
		{
			if (commandApdu[4] != 0x00)
				return Util.Hex2Bin(getString(R.string.SW_FAILED_RESPONSE));
				
			if (this.sCardHolderName.isEmpty())
			{
				String cardHolderName = getData(Util.CARD_HOLDER_NAME, "NULL");
				
				if (cardHolderName == "NULL")
					return Util.Hex2Bin(getString(R.string.SW_EMPTY_RESPONSE));

				this.sCardHolderName = cardHolderName;
			}
			
			return Util.Hex2Bin( String.format("%s%02X%s", 
												getString(R.string.SW_SUCCESS_RESPONSE), 
												this.sCardHolderName.length(), 
												Util.Bin2Hex(this.sCardHolderName.getBytes())));
		}
		catch ( Exception ex)
		{
			return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
		}
	}
	
	private byte[] getPhoneNumberResponse(byte[] commandApdu)
	{
		try
		{
			if (commandApdu[4] != 0x00)
				return Util.Hex2Bin(getString(R.string.SW_FAILED_RESPONSE));

			if (this.sCardHolderName.isEmpty())
			{
				String phoneNumber = getData(Util.PHONE_NUMBER, "NULL");
				if (phoneNumber == "NULL")
					return Util.Hex2Bin(getString(R.string.SW_EMPTY_RESPONSE));
				
				this.sPhoneNumber = phoneNumber;
			}
			
			return Util.Hex2Bin( String.format("%s%02X%s", 
												getString(R.string.SW_SUCCESS_RESPONSE), 
												this.sPhoneNumber.length(), 
												Util.Bin2Hex(this.sPhoneNumber.getBytes()))
								);
		}
		catch ( Exception ex)
		{
			return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
		}
	}

	private byte[] getUsablePlainTicketsResponse(byte[] commandApdu)
	{
		try
		{
			if (commandApdu[4] != 0x00)
				return Util.Hex2Bin(getString(R.string.SW_FAILED_RESPONSE));
				
			if (this.aUsableTickets == null)
			{
				Set<String> usableTickets = getDataSet(Util.USABLE_PLAIN_TICKETS);
				if (usableTickets == null)
					return Util.Hex2Bin(getString(R.string.SW_EMPTY_RESPONSE));
			}
			
			String allTickets = "";
			for (String str : this.aUsableTickets) 
				allTickets += str;
			
			return Util.Hex2Bin(String.format("%s%02X%s", getString(R.string.SW_SUCCESS_RESPONSE), this.aUsableTickets.size(), allTickets));
		}
		catch ( Exception ex)
		{
			return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
		}
	}

	private byte[] getUsedPlainTicketsResponse(byte[] commandApdu)
	{
		try
		{
			if (commandApdu[4] != 0x00)
				return Util.Hex2Bin(getString(R.string.SW_FAILED_RESPONSE));
				
			if (this.aUsedTickets == null)
			{
				Set<String> usedTickets = getDataSet(Util.USED_PLAIN_TICKETS);
				if (usedTickets == null)
					return Util.Hex2Bin(getString(R.string.SW_EMPTY_RESPONSE));
			}
			
			String usedTickets = "";
			for (String str : this.aUsedTickets) 
				usedTickets += str;
			
			return Util.Hex2Bin(String.format("%s%02X%s", getString(R.string.SW_SUCCESS_RESPONSE), this.aUsedTickets.size(), usedTickets));
		}
		catch ( Exception ex)
		{
			return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
		}
	}

	private byte[] getCUTAResponse(byte[] commandApdu)
	{
		try
		{
			if (commandApdu[4] != 0x00)
				return Util.Hex2Bin(getString(R.string.SW_FAILED_RESPONSE));
				
			if (this.lCUTA == -1)
			{
				String sCUTA = getData(Util.BALANCE_CUTA, "NULL");
				if (sCUTA == "NULL")
					return Util.Hex2Bin(getString(R.string.SW_EMPTY_RESPONSE));
				
				this.lCUTA = Long.parseLong(sCUTA, 16);
			}
			
			return Util.Hex2Bin(String.format("%s%012X", getString(R.string.SW_SUCCESS_RESPONSE), this.lCUTA));
		}
		catch ( Exception ex)
		{
			return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
		}
	}

	private byte[] getCTTAResponse(byte[] commandApdu)
	{
		try
		{
			if (commandApdu[4] != 0x00)
				return Util.Hex2Bin(getString(R.string.SW_FAILED_RESPONSE));
				
			if (this.lCTTA == -1)
			{
				String sCTTA = getData(Util.BALANCE_CTTA, "NULL");
				if (sCTTA == "NULL")
					return Util.Hex2Bin(getString(R.string.SW_EMPTY_RESPONSE));
				
				this.lCTTA = Long.parseLong(sCTTA, 16);
			}
			
			return Util.Hex2Bin(String.format("%s%012X", getString(R.string.SW_SUCCESS_RESPONSE), this.lCTTA));
		}
		catch ( Exception ex)
		{
			return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
		}
	}
	
	private byte[] storeTicket(byte[] commandApdu)
	{
		boolean isInitiallyEmpty = false;
		
		try
		{
			if (commandApdu[3] != 0x00)
				throw new Exception();
			
			if (commandApdu[4] != (byte)0x0A)
				throw new Exception();
			
			if (commandApdu.length != (byte)0x0F)
				throw new Exception();
			
			String ticketNo = Util.toHex(commandApdu, 5, 10);

			if (this.aUsableTickets == null)
			{
				Set<String> usableTickets = getDataSet(Util.USABLE_PLAIN_TICKETS);
				if (usableTickets == null)
					return Util.Hex2Bin(getString(R.string.SW_EMPTY_RESPONSE));
				else 
				{
					this.aUsableTickets = usableTickets;
					isInitiallyEmpty = true;
				}
			}
			
			this.aUsableTickets.add(ticketNo);

			try
			{
				setDataSet(Util.USABLE_PLAIN_TICKETS, this.aUsableTickets);	
			}
			catch(Exception e)
			{
				if (isInitiallyEmpty)
					this.aUsableTickets = null;
				throw e;
			}
			
			return Util.Hex2Bin(getString(R.string.SW_SUCCESS_RESPONSE));
		}
		catch ( Exception ex)
		{
			return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
		}		
	}
	
	private byte[] useTicket(byte[] commandApdu)
	{		
		try
		{
			if (commandApdu[3] != 0x00)
				throw new Exception();
			
			if (commandApdu[4] != (byte)0x0A)
				throw new Exception();
			
			if (commandApdu.length != (byte)0x0F)
				throw new Exception();
			
			String ticketNo = Util.toHex(commandApdu, 5, 10);

			if (this.aUsableTickets == null)
			{
				Set<String> usableTickets = getDataSet(Util.USABLE_PLAIN_TICKETS);
				if (usableTickets == null)
					return Util.Hex2Bin(getString(R.string.SW_NOT_FOUND));
				else 
				{
					this.aUsableTickets = usableTickets;
				}
			}
			
			if (this.aUsedTickets == null)
			{
				Set<String> usedTickets = getDataSet(Util.USED_PLAIN_TICKETS);
				if (usedTickets == null)
					usedTickets = new HashSet<String>();
				else 
					this.aUsedTickets = usedTickets;
			}
			
			
			if (!this.aUsableTickets.contains(ticketNo))
				return Util.Hex2Bin(getString(R.string.SW_NOT_FOUND));

			if (this.aUsedTickets.contains(ticketNo))
				return Util.Hex2Bin(getString(R.string.SW_ALREADY_USED));
			
			this.aUsableTickets.remove(ticketNo);
			this.aUsedTickets.add(ticketNo);
			setDataSet(Util.USABLE_PLAIN_TICKETS, this.aUsableTickets);
			setDataSet(Util.USED_PLAIN_TICKETS, this.aUsedTickets);

			return Util.Hex2Bin(getString(R.string.SW_SUCCESS_RESPONSE));
		}
		catch ( Exception ex)
		{
			return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
		}		
	}
	
	private byte[] deleteTicket(byte[] commandApdu)
	{	
		try
		{
			if (commandApdu[3] != 0x00)
				throw new Exception();
			
			if (commandApdu[4] != (byte)0x0A)
				throw new Exception();
			
			if (commandApdu.length != (byte)0x0F)
				throw new Exception();
			
			String ticketNo = Util.toHex(commandApdu, 5, 10);
			
			if (this.aUsableTickets == null)
			{
				Set<String> usableTickets = getDataSet(Util.USABLE_PLAIN_TICKETS);
				if (usableTickets == null)
					return Util.Hex2Bin(getString(R.string.SW_NOT_FOUND));
				else 
				{
					this.aUsableTickets = usableTickets;
				}
			}
			
			if (!this.aUsableTickets.contains(ticketNo))
				return Util.Hex2Bin(getString(R.string.SW_NOT_FOUND));

			this.aUsableTickets.remove(ticketNo);
			setDataSet(Util.USABLE_PLAIN_TICKETS, this.aUsableTickets);

			return Util.Hex2Bin(getString(R.string.SW_SUCCESS_RESPONSE));
		}
		catch ( Exception ex)
		{
			return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
		}			
	}
	
	private byte[] useBalance(byte[] commandApdu)
	{
		try
		{
			if (commandApdu[3] != 0x00)
				throw new Exception();
			
			if (commandApdu[4] != (byte)0x06)
				throw new Exception();
			
			if (commandApdu.length != (byte)0x0B)
				throw new Exception();
			
			if (this.lCUTA == -1)
			{
				String sCUTA = getData(Util.BALANCE_CUTA, "NULL");
				this.lCUTA = Long.parseLong(sCUTA, 16);
			}

			if (this.lCTTA == -1)
			{
				String sCTTA = getData(Util.BALANCE_CTTA, "NULL");
				this.lCTTA = Long.parseLong(sCTTA, 16);
			}
			
			if (this.lCUTA == -1 || this.lCTTA == -1)
				throw new Exception();
			
			long currentBalance = this.lCUTA - this.lCTTA;
			long operationBalance = Long.parseLong(Util.toHex(commandApdu, 5, 6), 16);
			
			if (operationBalance > currentBalance)
				return	Util.Hex2Bin(String.format("%s%s", getString(R.string.SW_BALANCE_NOT_ENOUGH), Util.toHex(Util.toBytes(currentBalance))));
				
						
			this.lCTTA += operationBalance;
			setData(Util.BALANCE_CTTA, Util.Bin2Hex(Util.toBytes(this.lCTTA)));
			return Util.Hex2Bin(String.format("%s%s", getString(R.string.SW_SUCCESS_RESPONSE), Util.toHex(Util.toBytes(currentBalance - operationBalance))));
		}
		catch ( Exception ex)
		{
			return Util.Hex2Bin(getString(R.string.SW_EXCEPTION_RESPONSE));
		}		
	}
	
	private String getData(String key, String defaultValue) throws Exception
	{
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
		if (sharedPref == null)
			throw new Exception();
		return sharedPref.getString(key, defaultValue);
	}

	private Set<String> getDataSet(String key) throws Exception
	{
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
		if (sharedPref == null)
			throw new Exception();
		return sharedPref.getStringSet(key, null);
	}
	
	private void setData(String key, String value) throws Exception
	{
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
		if (sharedPref == null)
			throw new Exception();
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	private void setDataSet(String key, Set<String> value) throws Exception
	{
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
		if (sharedPref == null)
			throw new Exception();
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putStringSet(key, value);
		editor.commit();
	}
}
