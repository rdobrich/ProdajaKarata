package com.aktivainfo.prodajakarata;

import org.json.JSONArray;

public interface Printer {
	  public boolean getStatus();
	  public boolean connectPrinter(String str);
	  public boolean disconnectPrinter();
	  public boolean print(String textToPrint);
	  public boolean printReceipt(JSONArray jsonMainNode);
	}
