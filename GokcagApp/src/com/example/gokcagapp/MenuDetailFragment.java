package com.example.gokcagapp;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A fragment representing a single Menu detail screen. This fragment is either
 * contained in a {@link MenuListActivity} in two-pane mode (on tablets) or a
 * {@link MenuDetailActivity} on handsets.
 */
public class MenuDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MenuDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {

		}
		
		
	}

	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_menu_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		//if (mItem != null) {
		//	((TextView) rootView.findViewById(R.id.menu_detail))
		//			.setText(mItem.content);
		//}
		Button button = (Button) rootView.findViewById(R.id.btnRefresh);
		
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		try
        		{
        			((EditText)getActivity().findViewById(R.id.txtCardHolderName)).setText(getData(Util.CARD_HOLDER_NAME, "NULL"));
        			((EditText)getActivity().findViewById(R.id.txtPhoneNumber)).setText(getData(Util.PHONE_NUMBER, "NULL"));
        			
        			Set<String> usedTickets = getDataSet(Util.USED_PLAIN_TICKETS);
        			String sUsedTickets = "";
        			for (String str : usedTickets) 
        				sUsedTickets += str;
        			((EditText)getActivity().findViewById(R.id.txtUsedPlainTickets)).setText(new String(Util.Hex2Bin(sUsedTickets)));

        			Set<String> usableTickets = getDataSet(Util.USABLE_PLAIN_TICKETS);
        			String sUsableTickets = "";
        			for (String str : usableTickets) 
        				sUsableTickets += str;
        			((EditText)getActivity().findViewById(R.id.txtUsablePainTickets)).setText(new String(Util.Hex2Bin(sUsableTickets)));
        			
        			String sCUTA = getData(Util.BALANCE_CUTA, "NULL");
        			long lCUTA = Long.parseLong(sCUTA, 16);
        			
        			((EditText)getActivity().findViewById(R.id.txtBalanceCUTA)).setText("" + lCUTA);

        			String sCTTA = getData(Util.BALANCE_CTTA, "NULL");
        			long lCTTA = Long.parseLong(sCTTA, 16);
        			
        			((EditText)getActivity().findViewById(R.id.txtBalanceCTTA)).setText("" + lCTTA);
        		}
        		catch (Exception ex)
        		{
        		
        		}
            }
        });

        button = (Button) rootView.findViewById(R.id.btnUpdate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		try
        		{
        			setData(Util.CARD_HOLDER_NAME,((EditText)getActivity().findViewById(R.id.txtCardHolderName)).getText().toString());
        			setData(Util.PHONE_NUMBER,((EditText)getActivity().findViewById(R.id.txtPhoneNumber)).getText().toString());
        			        			
        			
					Set<String> usedTickets = new HashSet<String>();
        			String sUsedTickets = ((EditText)getActivity().findViewById(R.id.txtUsedPlainTickets)).getText().toString();
        			while (sUsedTickets.length() != 0 && sUsedTickets.length()%10 == 0)
        			{
        				usedTickets.add(Util.toHex(sUsedTickets.substring(0, 10).getBytes()));
        				sUsedTickets = sUsedTickets.substring(10);
        			}
        			setDataSet(Util.USED_PLAIN_TICKETS, usedTickets);

					Set<String> usableTickets = new HashSet<String>();
        			String sUsableTickets = ((EditText)getActivity().findViewById(R.id.txtUsablePainTickets)).getText().toString();
        			while (sUsableTickets.length() != 0 && sUsableTickets.length()%10 == 0)
        			{
        				usableTickets.add(Util.toHex(sUsableTickets.substring(0, 10).getBytes()));
        				sUsableTickets = sUsableTickets.substring(10);
        			}
        			setDataSet(Util.USABLE_PLAIN_TICKETS, usableTickets);
        			        			
        			
        			long lCUTA = Long.parseLong(((EditText)getActivity().findViewById(R.id.txtBalanceCUTA)).getText().toString());
        			setData(Util.BALANCE_CUTA, Util.Bin2Hex(Util.toBytes(lCUTA)));

        			long lCTTA = Long.parseLong(((EditText)getActivity().findViewById(R.id.txtBalanceCTTA)).getText().toString());
        			setData(Util.BALANCE_CTTA, Util.Bin2Hex(Util.toBytes(lCTTA)));
        			
        			((EditText)getActivity().findViewById(R.id.txtBalanceCTTA)).setText("" + lCTTA);
        		}
        		catch (Exception ex)
        		{
        		
        		}
            }
        });

		return rootView;
	}
	

	
	private String getData(String key, String defaultValue) throws Exception
	{
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
		if (sharedPref == null)
			throw new Exception();
		return sharedPref.getString(key, defaultValue);
	}

	private Set<String> getDataSet(String key) throws Exception
	{
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
		if (sharedPref == null)
			throw new Exception();
		return sharedPref.getStringSet(key, null);
	}
	
	private void setData(String key, String value) throws Exception
	{
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
		if (sharedPref == null)
			throw new Exception();
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	private void setDataSet(String key, Set<String> value) throws Exception
	{
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
		if (sharedPref == null)
			throw new Exception();
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putStringSet(key, value);
		editor.commit();
	}
}
