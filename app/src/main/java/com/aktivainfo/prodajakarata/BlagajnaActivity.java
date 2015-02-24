package com.aktivainfo.prodajakarata;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.citizen.port.android.BluetoothPort;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BlagajnaActivity extends Activity {
	private ArrayList<Racun> r_parts = new ArrayList<Racun>();
    private ArrayList<Uplata> u_parts = new ArrayList<Uplata>();
    private RacunAdapter r_adapter;
    private UplataAdapter u_adapter;

    private String _printer;
    private String _autoSpoji;
    private String _korisnikID;
    private String _prodajnoMjestoID; 
    private String _naplatniUredjajID;
    private String _jsonString = "";
    
    private BluetoothAdapter _bluetoothAdapter;
	private BluetoothPort _bluetoothPort; 
	private static final int REQUEST_ENABLE_BT = 1;
	Printer printer;
	private String _bixBTAddr;
	
	private Context _context;
	
	private int pYear;
	private int pMonth;
	private int pDay;
	static final int DATE_DIALOG_ID = 0;
	
	
	
	public void onCreate(Bundle savedInstanceState) {
	       _context = this;
	       super.onCreate(savedInstanceState); 
	       setContentView(R.layout.blagajna_layout);
	       _korisnikID = Postavke.getPostavke(0, _context);
	       _printer = Postavke.getPostavke(4, _context);
	       _autoSpoji = Postavke.getPostavke(5, _context);
		   _bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		   _bluetoothPort = BluetoothPort.getInstance();
		   _prodajnoMjestoID = Postavke.getPostavke(6, _context);
		   _naplatniUredjajID = Postavke.getPostavke(7, _context);
		   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	       printer = new BixPrinter();
	       _bixBTAddr = Postavke.getPostavke(13, _context);
		   if(_bluetoothAdapter != null && !_bluetoothAdapter.isEnabled()){
			   Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			   startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

		   }
		   if(_bluetoothAdapter != null &&  _bluetoothAdapter.isEnabled() && !_bluetoothPort.isConnected()  && _bixBTAddr.equals("")){
			   Intent myIntent = new Intent(_context, BluetoothConnectMenu.class);
			   startActivity(myIntent);} 
	   //postaviInicijalnePodatke();
	   if (isNetworkAvailable()){
		  LoadData();
		}
	   else{
		   AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
	   	}
	   
	   Button povratak = (Button) findViewById(R.id.btnPovratak);
	   povratak.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		   				finish();  	   
	           }});
	   
	   Button uplata = (Button) findViewById(R.id.btnUplata);
	   uplata.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		   	 EditText input = (EditText) findViewById(R.id.txtIznos);
		   	 String value = input.getText().toString();
		     double iznos = 0;
		     try{
		    	 if (isNetworkAvailable()){
		    	 iznos = Double.parseDouble(value);
		    	 String executeString = "exec mobile.ins_blagajna " + _prodajnoMjestoID + ", " + _naplatniUredjajID + ", " + Double.toString(iznos) + ", " + "1" + ", "  + _korisnikID;
		    	 accessWebServiceIzleti(executeString, "uplataIsplataPostback", _context);
		    	 }
		    	 else{
					    AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
					 	}
		     }
		     catch(NumberFormatException ex){
		    	 AlertView.showAlert("Iznos nije u ispravnom formatu!\nZa decimalne brojeve koristite toèku!", _context);
		    	 return;
		     }
	           }});
	   
	   Button isplata = (Button) findViewById(R.id.btnIsplata);
	   isplata.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		   	 EditText input = (EditText) findViewById(R.id.txtIznos);
		   	 String value = input.getText().toString();
		     double iznos = 0;
		     try{
		    	 if (isNetworkAvailable()){
			    	 iznos = Double.parseDouble(value);
			    	 String executeString = "exec mobile.ins_blagajna " + _prodajnoMjestoID + ", " + _naplatniUredjajID + ", " + Double.toString(iznos) + ", " + "2" + ", "  + _korisnikID;
			    	 accessWebServiceIzleti(executeString, "uplataIsplataPostback", _context);
		    	 }
		    	 else{
					    AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
					 	}
		     }
		     catch(NumberFormatException ex){
		    	 AlertView.showAlert("Iznos nije u ispravnom formatu!\nZa decimalne brojeve koristite toèku!", _context);
		    	 return;
		     }
	           }});
	   
	   Button zakljucak = (Button) findViewById(R.id.btnZakljucak);
	   zakljucak.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		    	 String executeString = "exec mobile.napravi_zakljucak " + _prodajnoMjestoID + ", " + _naplatniUredjajID + ", " + _korisnikID;		    	 
				 	if (isNetworkAvailable()){
				 		
				 		if(_printer.equals("CMP30")){
							   CMP30Print cp = new CMP30Print(_context);
							   String msg = cp.provjeriVezu();
							   if(msg.equals("")){
								   accessWebServiceIzleti(executeString, "racunPrintPostback", _context);}
							   else{
								   AlertView.showError(msg, _context);
							   }
						   }
						   
						   if(_printer.equals("BIX")){
							   int rpt = 0;
							   while(printer.getStatus() == false && rpt < 5){
								   printer.connectPrinter(_bixBTAddr);
								   rpt ++;
							   }
							   if (printer.getStatus() == true){
								   accessWebServiceIzleti(executeString, "racunPrintPostback", _context);;
							   }
							   else{
								   AlertView.showError("Bluetooth printer nije moguæe spojiti!\nProvjerite da li je printer upaljen.", _context);
							   }
						   }
				    
				 	}
				 	else{
				    AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
				 	}
	           }});
	   
	   Button btnXizvjestaj = (Button) findViewById(R.id.btnXizvjestaj);
	   btnXizvjestaj.setOnClickListener(new View.OnClickListener() {
		   @SuppressWarnings("deprecation")
		@Override
		   public void onClick(View v) {
			   final Calendar cal = Calendar.getInstance();
		       pYear = cal.get(Calendar.YEAR);
		       pMonth = cal.get(Calendar.MONTH);
		       pDay = cal.get(Calendar.DAY_OF_MONTH);
			   showDialog(DATE_DIALOG_ID);
			   
			   
		           }});
	   
	   
	}
	
	
	
	private void LoadData(){
		 String executeString = "exec mobile.get_blagajna " + _prodajnoMjestoID + ", " + _naplatniUredjajID + ", " + _korisnikID;
		if (isNetworkAvailable()){
			   accessWebServiceIzleti(executeString, "racunPostback", _context);
			}
		   else{
			   AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
		   	}
		EditText input = (EditText) findViewById(R.id.txtIznos);
		input.setText("");
	}
	private void fillObject(){
    	// create some objects
    	r_parts.clear();
    	u_parts.clear();
    	
    	 try{
    		   JSONObject jsonResponse = new JSONObject(_jsonString);
    		   JSONObject jsonInnerObjectPodaci = jsonResponse.getJSONObject("podaci");
    		   JSONObject jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("racuni");
    		   JSONArray jsonMainNode = jsonInnerObject.optJSONArray("racun");
    		   
    		  for(int i = 0; i<jsonMainNode.length();i++){
    		   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
    		   String racunId = jsonChildNode.optString("@racun_id");
    		   int id = Integer.parseInt(racunId);
    		   if(id > 0){ 
	    		   racunId = jsonChildNode.optString("@racun_id");
	    		   String racunGuid = jsonChildNode.optString("@racun_guid");
	    		   String datumRacuna = jsonChildNode.optString("@datum_racuna");
	    		   String fiskalniBrojRacuna = jsonChildNode.optString("@fiskalni_broj_racuna");
	    		   String nazivVoznje = jsonChildNode.optString("@naziv_voznje");   		      		   
	    		   String karte = jsonChildNode.optString("@karte");
	    		   String meniji = jsonChildNode.optString("@meniji");
	    		   r_parts.add(new Racun(Integer.parseInt(racunId), racunGuid, datumRacuna, fiskalniBrojRacuna, nazivVoznje, karte, meniji));
    		   }
    		  }
    		  
    		  
    		  jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("uplate");
   		   	  jsonMainNode = jsonInnerObject.optJSONArray("uplata");
    		  
    		  for(int i = 0; i<jsonMainNode.length();i++){
       		   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
       		   String npId = jsonChildNode.optString("@nacin_placanja_id");  
       		   int id = Integer.parseInt(npId);
       		   if(id >= 0){
	       		   String nacinPlacanja = jsonChildNode.optString("@nacin_placanja");
	       		   String uplata = jsonChildNode.optString("@uplata");
	       		   String isplata = jsonChildNode.optString("@isplata");
	       		   String iznos = jsonChildNode.optString("@iznos");
	       		   
	       		   u_parts.add(new Uplata(0, nacinPlacanja.toString(), uplata.toString(), isplata.toString(), iznos.toString()));
       		   }
       		  }
    		  
    		  
    		  jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("blagajne");
   		   	  jsonMainNode = jsonInnerObject.optJSONArray("blagajna");
    		  
    		  for(int i = 0; i<jsonMainNode.length();i++){
       		   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
       		   String bId = jsonChildNode.optString("@blagajna_id");
    		   int id = Integer.parseInt(bId);
    		   if(id >= 0){
	       		   String iznosBlagajne = jsonChildNode.optString("@iznos_blagajne");
	       		   TextView iznos = (TextView) findViewById(R.id.lblIznosBlagajne);
	       		   iznos.setText(iznosBlagajne);
    		   	}
    		   }
    		  
    	
    		  
    		  
    		 }
    	catch(JSONException e){
    			AlertView.showError("Pogreška kod punjenja objekata sa JSON stringom - "+ e.toString(), _context);
    		   //Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
    		  }
    }
    
	 /**Punjenje ListViewa  sa kartama*/
    private void displayRacuni(ArrayList<Racun> r_parts){
    	r_adapter = new RacunAdapter(_context, R.layout.karta_item, r_parts);

        // display the list.
        ListView listView = (ListView) findViewById(R.id.lvListaRacuna);
        // Assign adapter to ListView
        listView.setAdapter(r_adapter);
        listView.setTextFilterEnabled(true);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		         // When clicked, do some work
        		Racun racun = (Racun) parent.getItemAtPosition(position);
        		String racunGuid = racun.getRacunGuid();
        		String executeString = "exec mobile.get_racun_ispis " +"'" + racunGuid + "'";
				 	if (isNetworkAvailable()){
				 		
				 		if(_printer.equals("CMP30")){
							   CMP30Print cp = new CMP30Print(_context);
							   String msg = cp.provjeriVezu();
							   if(msg.equals("")){
								   accessWebServiceIzleti(executeString, "racunPrintPostback", _context);}
							   else{
								   AlertView.showError(msg, _context);
							   }
						   }
						   
						   if(_printer.equals("BIX")){
							   int rpt = 0;
							   while(printer.getStatus() == false && rpt < 5){
								   printer.connectPrinter(_bixBTAddr);
								   rpt ++;
							   }
							   if (printer.getStatus() == true){
								   accessWebServiceIzleti(executeString, "racunPrintPostback", _context);;
							   }
							   else{
								   AlertView.showError("Bluetooth printer nije moguæe spojiti!\nProvjerite da li je printer upaljen.", _context);
							   }
						   }
 				    
				 	}
				 	else{
 				    AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
				 	}
				 	
				 	
        		}
		     });
        
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        }); 
        
    }
    
    private void displayUplate(ArrayList<Uplata> u_parts){
    	u_adapter = new UplataAdapter(_context, R.layout.nacinplacanja_item, u_parts);

        // display the list.
        ListView listView = (ListView) findViewById(R.id.lvUplate);
        // Assign adapter to ListView
        listView.setAdapter(u_adapter);
        listView.setTextFilterEnabled(true);
        
    }
	
	 /**Funkcija koja se poziva iz SoapAccessTaskIzleti kod snimanja racuna*/
    public void racunPrintPostback(String result){
        //if you started progress dialog dismiss it here
         //Nakon dohvata podataka, ako je ukljuèen flag za ispis æe ispisati, 
    	 //u suprotnom æe uèitati nove podatke   
    		try{
        	   JSONObject jsonResponse = new JSONObject(result);
     		   JSONObject jsonInnerObjectPodaci = jsonResponse.getJSONObject("podaci");
     		   
     		   //izvlaèenje statusa iz primljenog JSONa
     		   JSONObject jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("statusi");
     		   JSONArray jsonMainNode = jsonInnerObject.optJSONArray("status");
     		   Integer status_final = 1;
     		   String poruka = "";
	     		  for(int i = 0; i<jsonMainNode.length();i++){
		     		   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		     		   Integer status = Integer.parseInt(jsonChildNode.optString("@status"));
		     		   if(status > 0){
		     			   status_final = status;
		     			   poruka = jsonChildNode.optString("@error_text");
		     		   } 
	     		  }
	     		 if(status_final == 3){
		     		   AlertView.showError(poruka, _context);
		     		   //Toast.makeText(getApplicationContext(), poruka, Toast.LENGTH_LONG).show();
	     		   }
	     		 else{
     		   	jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("stampanje");
     		   	jsonMainNode = jsonInnerObject.optJSONArray("red");
	     		  
     			   if(_printer.equals("CMP30")){
		     			  
     				   CMP30Print cp = new CMP30Print(_context);
     				   String msg = cp.provjeriVezu();
     				   	if(msg.equals("")){
     				   		cp.print(jsonMainNode);}
     				   	else{
     				   		AlertView.showError(msg, _context);}
		     		}
     			   
     			  if(_printer.equals("BIX")){
	     			  if(printer.getStatus()==true){
	     				  printer.printReceipt(jsonMainNode);
	     			  }
	     			  printer.disconnectPrinter();
		     		}
     			   
     			   if(_printer.equals("CitizenUSB")){
						     			
     				  /*CitizenUSBPrint cup = new CitizenUSBPrint(_context);
     				  cup.print(jsonMainNode);*/
     				  CitizenUSBAsyncPrint cup = new CitizenUSBAsyncPrint(_context, jsonMainNode);
     				  cup.execute();
		     		}	
	     		 }
	     	}
    		catch(JSONException e){
    			AlertView.showError("Neuspješno èitanje JSON stringa kod slanja karata - "+e.toString(), _context);
 			}	
    		
    		//Toast.makeText(getApplicationContext(),"Proknjizeno...", Toast.LENGTH_SHORT).show();
    		
      	    
    	
        
    
    }
    
    public void racunPostback(String result){
		//dovat novi podatka i postavljanje dohvaèenih podataka
        if (result != "") {
        	_jsonString=result;
        	fillObject();
        	displayRacuni(r_parts);
        	displayUplate(u_parts);
        	
        }
    
    }
    public void uplataIsplataPostback(String result){
    	 if (result != "") {
         	try{
         	   JSONObject jsonResponse = new JSONObject(result);
      		   JSONObject jsonInnerObjectPodaci = jsonResponse.getJSONObject("podaci");
      		   
      		   //izvlaèenje statusa iz primljenog JSONa
      		   JSONObject jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("statusi");
      		   JSONArray jsonMainNode = jsonInnerObject.optJSONArray("status");
      		   Integer status_final = 1;
      		   String poruka = "";
 	     		  for(int i = 0; i<jsonMainNode.length();i++){
 		     		   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
 		     		   Integer status = Integer.parseInt(jsonChildNode.optString("@status"));
 		     		   if(status > 0){
 		     			   status_final = status;
 		     			   poruka = jsonChildNode.optString("@error_text");
 		     		   } 
 	     		  }
 	     		 if(status_final == 3){
 		     		   AlertView.showError(poruka, _context);
 		     		   //Toast.makeText(getApplicationContext(), poruka, Toast.LENGTH_LONG).show();
 	     		   }
 	     	}
     		catch(JSONException e){
     			AlertView.showError("Neuspješno èitanje JSON stringa kod slanja karata - "+e.toString(), _context);
  			}
         LoadData();
    	 }
    }
	
	   private void accessWebServiceIzleti(String executeString, String imeFunkcije, Context context) {
	        SoapAccessTaskIzleti task = new SoapAccessTaskIzleti(executeString, imeFunkcije, context);
	        //passes values for the urls string array
	        task.execute();
	    }
	   
	   private boolean isNetworkAvailable() {
	        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	    }
	   
	   public void goToCMP30Connect(){
	    	Intent myIntent = new Intent(_context, BluetoothConnectMenu.class);
	 	   	myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	 	   	startActivity(myIntent);
	    }
	   
	   /** Callback received when the user "picks" a date in the dialog */
	    private DatePickerDialog.OnDateSetListener pDateSetListener =
	            new DatePickerDialog.OnDateSetListener() {
	 
	                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	                	String datum = Integer.toString(year) +"-" + Integer.toString(monthOfYear + 1) + "-" + Integer.toString(dayOfMonth);
		                	 String executeString = "exec mobile.get_x_izvjestaj_ispis" +"'" + datum + "', " + _prodajnoMjestoID + ", " + _naplatniUredjajID + ", " + _korisnikID;		    	 
		 				 	if (isNetworkAvailable()){
		 				 		
		 				 		if(_printer.equals("CMP30")){
		 							   CMP30Print cp = new CMP30Print(_context);
		 							   String msg = cp.provjeriVezu();
		 							   if(msg.equals("")){
		 								   accessWebServiceIzleti(executeString, "racunPrintPostback", _context);}
		 							   else{
		 								   AlertView.showError(msg, _context);
		 							   }
		 						   }
		 						   
		 						   if(_printer.equals("BIX")){
		 							   int rpt = 0;
		 							   while(printer.getStatus() == false && rpt < 5){
		 								   printer.connectPrinter(_bixBTAddr);
		 								   rpt ++;
		 							   }
		 							   if (printer.getStatus() == true){
		 								   accessWebServiceIzleti(executeString, "racunPrintPostback", _context);;
		 							   }
		 							   else{
		 								   AlertView.showError("Bluetooth printer nije moguæe spojiti!\nProvjerite da li je printer upaljen.", _context);
		 							   }
		 						   }
		 				    
		 				 	}
		 				 	else{
		 				    AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
		 				 	}
		                }
	            };
	            /** Get the current date */
		 @Override
		protected Dialog onCreateDialog(int id) {
			 switch (id) {
			 	case DATE_DIALOG_ID:
			 	return new DatePickerDialog(this, 
			 			pDateSetListener,
			 			pYear, pMonth, pDay);
			 }
			 return null;
		}
	            
	    
}
