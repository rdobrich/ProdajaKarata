package com.aktivainfo.prodajakarata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.citizen.jpos.command.ESCPOS;
import com.citizen.sdk.ESCPOSConst;
import com.citizen.sdk.ESCPOSPrinter;

import android.content.Context;
import android.hardware.usb.UsbDevice;


public class CitizenUSBPrint {
	
	private Context _context;
	final String PREFERENCES_NAME = "Citizen_PSample_for_PosPrinterLibraly";
    private final char ESC = ESCPOS.ESC;
    private ESCPOSPrinter _posPtr;
    private Integer result;
    private UsbDevice usbDevice;
	public CitizenUSBPrint(Context ctx){
		
		this._context = ctx;
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
	
	/**Funkcija za ispis, prima JSONArray*/
	public void print(JSONArray jsonMainNode){
		// Android 3.1 ( API Level 12 ) or later
		if ( ESCPOSConst.CMP_SUCCESS == result )
		{
			// Printer Check
			if ( printerCheck( _posPtr ) )
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
				_posPtr.cutPaper( ESCPOSConst.CMP_CUT_PARTIAL_PREFEED );
				_posPtr.transactionPrint( ESCPOSConst.CMP_TP_NORMAL );
			}
			//result = _posPtr.disconnect();
		}
		else
		{
			// connect() Error
			AlertView.showError("Spajanje sa printerom nije uspjelo\nPogreška : "+Integer.toString( result ), _context);
			
		}
	}
	
	/**Funkcija za provjeru dostupnosti printera*/
	public boolean provjeriVezu(){
		if ( ESCPOSConst.CMP_SUCCESS == result ){
			return true;
		}
		else{
			return false;
		}
		
	}
	
	
	
	private boolean printerCheck( ESCPOSPrinter posPtr )
	{
		int result;
		boolean bRet = false;
		String msg = "";

		//
		if ( null == posPtr )
		{
			msg += "\nESCPOSPrinter() : Instance is not created";
		}
		else
		{
			//
			// Printer Check
			result = posPtr.printerCheck();
			if ( ESCPOSConst.CMP_SUCCESS == result ) {
				msg += "\nprinterCheck() : Success\n";
				//
				// Get Status
				int status = posPtr.status();
				msg += "\nstatus() : ";
				//
				// Status Check
				if ( ESCPOSConst.CMP_STS_NORMAL == status ) {
					//
					// No Error
					bRet = true;
					msg += "\n\t * Normal ( No Error )";
				} else {
					if ( ( ESCPOSConst.CMP_STS_COVER_OPEN & status ) > 0 ) {
						//
						// Cover Open
						msg += "\n\t * Cover Open";
					}
					if ( ( ESCPOSConst.CMP_STS_PAPER_EMPTY & status ) > 0 ) {
						//
						// Paper Empty
						msg += "\n\t * Paper Empty";
					}
					if ( ( ESCPOSConst.CMP_STS_PRINTEROFF & status ) > 0 ) {
						//
						// Printer Offline
						msg += "\n\t * Printer Offline";
					}
				}
			} else {
				//
				// printerCheck() Error
				msg += "\nprinterCheck() Error : " + Integer.toString( result );
			}
		}
		if ( !bRet )
		{
			AlertView.showError(msg, _context);
		}
		return bRet;
	}




}
