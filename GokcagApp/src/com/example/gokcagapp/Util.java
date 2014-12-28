package com.example.gokcagapp;

import java.nio.ByteBuffer;
import java.util.Arrays;

import android.util.Log;

/**
 * An utility class
 *
 */
public final class Util
{
	public static String CARD_HOLDER_NAME 		= "CARD_HOLDER_NAME";
	public static String PHONE_NUMBER 			= "PHONE_NUMBER";
	public static String USABLE_PLAIN_TICKETS 	= "USABLE_PLAIN_TICKETS";
	public static String USED_PLAIN_TICKETS 	= "USED_PLAIN_TICKETS";
	public static String BALANCE_CUTA 			= "BALANCE_CUTA";
	public static String BALANCE_CTTA 			= "BALANCE_CTTA";
	
	/**
	 * Checks if an array is equal to another array
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static boolean equal(byte[] array1, byte[] array2)
	{
		return Arrays.equals(array1, array2);
	}
	
	public static byte[] xorArray(byte[] array1, byte[] array2)
	{
		if (array1.length != array2.length)
			return new byte[0];
		
		return xorArray(array1, 0, array2, 0, array1.length);
	}
	
	public static byte[] xorArray(byte[] array1, int offset1, byte[] array2, int offset2, int length)
	{
		if (array1.length < offset1 + length || array2.length < offset2 + length)
			return new byte[0];
		
		byte[] output = new byte[length];
		int i = 0;
		for (byte b : array1)
			output[i] = (byte) (b ^ array2[i++]);
		return output;
	}

	public static byte[] toBytes(byte value)
	{
		return new byte[]{value};
	}
	
	public static byte[] toBytes(short value)
	{
		return ByteBuffer.allocate(2).putShort(value).array();
	}
	
	public static byte[] toBytes(int value)
	{
		return ByteBuffer.allocate(4).putInt(value).array();
	}
	
	public static byte[] toBytes(long value)
	{
		return ByteBuffer.allocate(8).putLong(value).array();
	}
	
	/**
	 * Convert bytes to a number with a specific byte length
	 * 
	 * Internal only
	 * 
	 * @param offset
	 * @param length
	 * @return
	 */
	public static long getSomething(byte[] buffer, int offset, int length)
	{
		int value = 0;
		for (int i = 0; i < length && buffer.length > i; i++)
		{
			value <<= 8; // Shift left
			value |= buffer[offset+i] & 0xFF; // Grab one byte and OR it
		}
		return value;
	}
	
	/**** START Debugging functions ****/
	public static void d(String tag, String msg)
	{
		Log.i(tag, msg);
	}
	
	public static void d(String tag, String format, Object... args)
	{
		Log.i(tag, String.format(format, (Object[])args));
	}
	
	public static String toHex(byte[] buffer)
	{
		return toHex(buffer, 0, buffer.length);
	}
	
	public static String toUnspacedHex(byte[] buffer)
	{
		return toHex(buffer).replace(" ", "");
	}
		
	public static String toHex(byte[] buffer, int offset, int length)
	{
		String hex = "";
		for(int i = offset; i < offset+length; i++)
			hex = hex.concat(String.format("%02X", buffer[i]));		
		return hex.trim();
	}
	/**** END Debugging functions ****/
	public static byte[] Hex2Bin(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }
	
	final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	public static String Bin2Hex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length*2];
	    int v;
	
	    for(int j=0; j < bytes.length; j++) {
	        v = bytes[j] & 0xFF;
	        hexChars[j*2] = hexArray[v>>>4];
	        hexChars[j*2 + 1] = hexArray[v & 0x0F];
	    }
	
	    return new String(hexChars);
	}
}
