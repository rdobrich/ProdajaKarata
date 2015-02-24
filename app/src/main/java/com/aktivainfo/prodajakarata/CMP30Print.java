package com.aktivainfo.prodajakarata;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray; 
import org.json.JSONException;
import org.json.JSONObject;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import com.citizen.jpos.command.ESCPOS;
import com.citizen.jpos.command.ESCPOSConst;
import com.citizen.jpos.printer.ESCPOSPrinter;
import com.citizen.port.android.BluetoothPort;

public class CMP30Print {
	
	private Context _context;
	private ESCPOSPrinter _posPtr;
	private final char ESC = ESCPOS.ESC;
	
    private BluetoothAdapter _bluetoothAdapter;
	private BluetoothPort _bluetoothPort;
	
	public CMP30Print(Context context){
		this._context = context;
	}
	
	/**Funkcija za ispis, prima JSONArray*/
	public void print(JSONArray jsonMainNode){
			String izmjenjeniESC = "";
			String izmjenjeniNL = "";
			
			_posPtr = new ESCPOSPrinter();
			if(_posPtr.printerCheck() == ESCPOSConst.CMP_FAIL)
			{
				Log.i("Sample2","printerCheck() Failed");
				return;
				}
			if(_posPtr.status() == ESCPOSConst.CMP_STS_NORMAL){	
				try{
					try{
		     		    for(int i = 0; i<jsonMainNode.length();i++){
		     		      JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		     			  String izvorni = jsonChildNode.optString("@tekst");
		     			  
		     			  /*izmjenjeniESC = izvorni.replace("#ESC#|",ESC +"|" );
		     		      izmjenjeniNL = izmjenjeniESC.replace("#NL#","\r\n" );*/
		     		      String[] parts = izvorni.split("#");
		     		      for(String str : parts){
		     		    	 _posPtr.printString(str + "\r\n");
		     		      }
		     		    }
					}
					catch(JSONException e){
		    			AlertView.showError("Neuspješno èitanje JSON stringa kod slanja karata - "+e.toString(), _context);
		 			  //Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_LONG).show();
		   		  		}
			     			
		     		   }
				catch (UnsupportedEncodingException e) {
					AlertView.showError("Neuspješno slanje raèuna na ispis - "+e.toString(), _context);
					}
				}	
	
	}
	
	/**Funkcija za provjeru dostupnosti printera*/
	public String provjeriVezu(){
		String msg = "";
    	_bluetoothPort = BluetoothPort.getInstance();
    	_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
 	   	if(_bluetoothAdapter != null){
	               if(!_bluetoothPort.isConnected()){
	            	   	msg = "Spojite bluetooth pisaè prije nastavka!";
	      			 	return msg;
	      			 	}
	               else{
	            	   _posPtr = new ESCPOSPrinter();
	     			   if(_posPtr.printerCheck() == ESCPOSConst.CMP_FAIL)
		     			{
		     				Log.i("Print","printerCheck() Failed");
		     				msg = "Printer nije povezan ispravno!\n " +
		     									"1. Provjerite da li je printer ukljuèen.\n " +
		     									"2. Provjerite da li je bluetooth ukljuèen\n " +
		     									"3. Prekinite vezu i ponovo spojite tablet sa bluetooth ureðajem";
		     				return msg;
		     				}
	     			   else{
	     				  return msg;
	     				  }
	               }
        		}
 	   else{
 		   msg = "Ureðaj nema Bluetooth i nije moguæe spojiti printer!";
 		   return msg;
 	   }
    
	}

}
