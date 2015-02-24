package com.aktivainfo.prodajakarata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.citizen.jpos.command.ESCPOS;
import com.citizen.sdk.ESCPOSConst;
import com.citizen.sdk.ESCPOSPrinter;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;

public class CitizenUSBAsyncPrint extends AsyncTask<String, Void, String> {
	
	private Context _context;
	final String PREFERENCES_NAME = "Citizen_PSample_for_PosPrinterLibraly";
    private final char ESC = ESCPOS.ESC;
    private ESCPOSPrinter _posPtr;
    private Integer result;
    private UsbDevice usbDevice;
    private JSONArray jsonMainNode;
    
    
	public CitizenUSBAsyncPrint(Context ctx, JSONArray array){
		
		this._context = ctx;
		this.jsonMainNode = array;
		
		_posPtr = new ESCPOSPrinter();
		//
		// Set context
		_posPtr.setContext( _context );
		//
		// Get Address
		usbDevice = null;												// null (Automatic detection)
		//
		// Connect
		result = _posPtr.connect( ESCPOSConst.CMP_PORT_USB, usbDevice );
		
	}
	
	 @Override
     protected void onPreExecute() {
         //if you want, start progress dialog here
     }

	@Override
	protected String doInBackground(String... arg0) {

		if ( ESCPOSConst.CMP_SUCCESS == result )
		{
				_posPtr.transactionPrint( ESCPOSConst.CMP_TP_TRANSACTION );
				
				String izmjenjeniESC;
				String izmjenjeniNL;
				String izmjenjenilA;
				String izmjenjenicA;
					try{
		     		    for(int i = 0; i<jsonMainNode.length();i++){
		     		      JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		     			  String izvorni = jsonChildNode.optString("@tekst");
		     			  
		     			  izmjenjenicA = izvorni.replace("#ESC#|cA","" );
		     			  izmjenjenilA = izmjenjenicA.replace("#ESC#|lA","" );
		     			  izmjenjeniESC = izmjenjenilA.replace("#ESC#|",ESC +"|" );
		     		      izmjenjeniNL = izmjenjeniESC.replace("#NL#","\r\n" );
		     		      
		     		      _posPtr.printNormal(izmjenjeniNL);}
					}
	     		    catch(JSONException e){
	     		    	AlertView.showError("Neuspješno èitanje JSON stringa kod slanja karata - "+e.toString(), _context);
	    			  	}			
				//_posPtr.cutPaper( ESCPOSConst.CMP_CUT_PARTIAL_PREFEED );
				_posPtr.transactionPrint( ESCPOSConst.CMP_TP_NORMAL );
			}
		result = _posPtr.disconnect();

		return null;
	}
	

}
