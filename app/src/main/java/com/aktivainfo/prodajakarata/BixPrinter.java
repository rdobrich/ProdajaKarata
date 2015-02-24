package com.aktivainfo.prodajakarata;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bixolon.android.library.BxlService;
import com.citizen.jpos.command.ESCPOSConst;
import com.citizen.jpos.printer.ESCPOSPrinter;
	public class BixPrinter implements Printer {
	  BxlService mBxlService;
	  boolean conn;
	  
	  @Override
	    public boolean getStatus() {
	      return conn;
	    }

	  @Override
	    public boolean connectPrinter(String str) {
	      if (conn != true) {
	        CheckGC("Connect_Start");
	        mBxlService = new BxlService();
	        int cn;
	        try{
	        	cn = mBxlService.Connect(str);
	        	}
	        catch(Exception e){
	        	cn= 99;
	        }	        
	        if (cn == 0) {
	          conn = true;
	        } else {
	          conn = false;
	        }
	        CheckGC("Connect_End");
	      }
	      return conn;
	    }

	  @Override
	    public boolean disconnectPrinter() {
	      if (mBxlService.Disconnect() == 0) {
	        conn = false;
	      }
	      else
	      {
	        conn = true;
	      }
	      return !conn;
	    }

	  @Override
	    public boolean print(String textToPrint) {
	      boolean result = false;
	      CheckGC("PrintText_Start");
	      int returnValue;
	      returnValue = mBxlService.PrintText(textToPrint,
	          BxlService.BXL_ALIGNMENT_CENTER, BxlService.BXL_FT_DEFAULT,
	          BxlService.BXL_TS_0WIDTH | BxlService.BXL_TS_0HEIGHT);
	      if (returnValue == BxlService.BXL_SUCCESS) {
	        returnValue = mBxlService.LineFeed(2);
	        result = true;
	      }
	      else {
	        result = false;
	      }
	      CheckGC("PrintText_End");
	      return result;
	    }
	  
	  @Override
	  public boolean printReceipt(JSONArray jsonMainNode){
			//String izmjenjeniESC = "";
			//String izmjenjeniNL = "";
			int a=mBxlService.SetCharacterSet(18);
		    mBxlService.CheckSetCharacterSet();
		    CheckGC("PrintText_Start");
		    int returnValue = 0;
		    boolean result = false;
					try{
		     		    for(int i = 0; i<jsonMainNode.length();i++){
		     		      JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		     			  String izvorni = jsonChildNode.optString("@tekst");
		     			  
		     			  /*izmjenjeniESC = izvorni.replace("#ESC#|",ESC +"|" );
		     		      izmjenjeniNL = izmjenjeniESC.replace("#NL#","\r\n" );*/
		     		      String[] parts = izvorni.split("#");
		     		      for(String str : parts){
		     		    	  byte[] dataBytes= convertAscii852(str);
			     		      returnValue = mBxlService.write(dataBytes);
			     		      mBxlService.LineFeed(1);
		     		      }
		     		    }	     		      
		     		      
		     		      if (returnValue == BxlService.BXL_SUCCESS) {
		     		        returnValue = mBxlService.LineFeed(2);
		     		        result = true;
		     		      }
		     		      else {
		     		        result = false;
		     		      }
		     		      return result;
					}
					catch(JSONException e){
		    			return false;
		 			  //Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_LONG).show();
		   		  	}
			     			
		}
	


	  void CheckGC(String FunctionName) {
	    Runtime.getRuntime().maxMemory();
	    System.runFinalization();
	    System.gc();
	    Runtime.getRuntime().maxMemory();
	  }
	  
	  public byte[] convertAscii852(String input)
	  {
	          int length = input.length();
	          byte[] retVal = new byte[length];
	         
	          for(int i=0; i<length; i++)
	          {
	                    char c = input.charAt(i);
	                   
	                    if (c =='È')
	                    {
	                            retVal[i] = (byte)172;
	                    }
	                    else if (c =='è')
	                    {
	                            retVal[i] = (byte)159;
	                    }
	                    else if (c =='Ž')
	                    {
	                            retVal[i] = (byte)166;
	                    }
	                    else if (c =='ž')
	                    {
	                            retVal[i] = (byte)167;
	                    }
	                    else if (c =='Æ')
	                    {
	                            retVal[i] = (byte)143;
	                    }
	                    else if (c =='æ')
	                    {
	                            retVal[i] = (byte)134;
	                    }
	                    else if (c =='Ð')
	                    {
	                            retVal[i] = (byte)209;
	                    }
	                    else if (c =='ð')
	                    {
	                            retVal[i] = (byte)208;
	                    }
	                    else if (c =='Š')
	                    {
	                            retVal[i] = (byte)230;
	                    }
	                    else if (c =='š')
	                    {
	                            retVal[i] = (byte)231;
	                    }
	                    else {
	                    	retVal[i] = (byte)c;
	                    }
	          }
	         
	          return  retVal ;
	  }

	  

}
