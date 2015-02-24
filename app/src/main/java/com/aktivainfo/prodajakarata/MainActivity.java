package com.aktivainfo.prodajakarata;

/**
 * Created by vjekoslav.mezdic on 20.09.13..
 */

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import android.widget.ListView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.bixolon.printer.BixolonPrinter;
import com.citizen.port.android.BluetoothPort;

public class MainActivity extends Activity {

	// declare class variables
    private ArrayList<Voznja> m_parts = new ArrayList<Voznja>();
    private ArrayList<Karta> k_parts = new ArrayList<Karta>();
    private ArrayList<Meni> menu_parts = new ArrayList<Meni>();
    private ArrayList<Mjesto> mjesto_parts = new ArrayList<Mjesto>();
    private ArrayList<Popust> popust_parts = new ArrayList<Popust>();
    private VoznjaAdapter m_adapter;
    private KartaAdapter k_adapter;
    private MenuAdapter menu_adapter;
    private MjestoAdapter mjesto_adapter;
    private PopustAdapter popust_adapter;
    private String _jsonString = "";
    private Integer _odabranaLinijaID = 0;
    private Integer _odabranaVoznjaID = 0;
    private Integer _tipObracunaCijena;//ne koristi se
    private Integer _maxKarata;
    private Integer _tipRadaRacuna = 0;
    private Double _maxIznosAkontacije;
    private Integer _isAkontacijaUzKartu;
    private String _printer;
    private String _autoSpoji;
    private String _korisnikID;
    private String _prodajnoMjestoID; 
    private String _naplatniUredjajID;
    private Integer _iznosPopusta = 0;
    private Integer _polazisteId = 0;
    private String _vrijemePolaska = "";
    
    private BluetoothAdapter _bluetoothAdapter;
	private BluetoothPort _bluetoothPort;
	private Context _context;
	private String _bixBTAddr;
	
	private static final int REQUEST_ENABLE_BT = 1;
	/*static BixolonPrinter mBixolonPrinter;
	private String mConnectedDeviceName = null;
	public static final String TAG = "BixolonPrinterSample";
	static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;
	static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;
	*/
	Printer printer;
	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
       _context = this;
       super.onCreate(savedInstanceState); 
       setContentView(R.layout.activity_main);
       _korisnikID = Postavke.getPostavke(0, _context);
       _printer = Postavke.getPostavke(4, _context);
       printer = new BixPrinter();
       _autoSpoji = Postavke.getPostavke(5, _context);
	   _bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	   _bluetoothPort = BluetoothPort.getInstance();
	   _prodajnoMjestoID = Postavke.getPostavke(6, _context);
	   _naplatniUredjajID = Postavke.getPostavke(7, _context);
	   _bixBTAddr = Postavke.getPostavke(13, _context);
	   if(_bluetoothAdapter != null && !_bluetoothAdapter.isEnabled()){
		   Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		   startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

	   }
	   if(_bluetoothAdapter != null &&  _bluetoothAdapter.isEnabled() && !_bluetoothPort.isConnected()  && _bixBTAddr.equals("")){
		   Intent myIntent = new Intent(MainActivity.this, BluetoothConnectMenu.class);
		   startActivity(myIntent);} 
	   
	  
	   
	   
	   //postavi execute string
	   //za dohvat podataka o voznji: "exec mobile.get_izleti " + _odabranaLinijaID +  ", " + _odabranaVoznjaID + ", " + _korisnikID;
	   //za knjizenje  _executeString = "mobile.snimi_racun " + XML;
	   
	   String executeString = "exec mobile.get_izleti " + _odabranaLinijaID +  ", " + _odabranaVoznjaID + ", " + _prodajnoMjestoID + ", " + _korisnikID;
	       
	   //postaviInicijalnePodatke();
	   if (isNetworkAvailable()){
		   accessWebServiceIzleti(executeString, "dataPostback", _context);
		}
	   else{
		   AlertView.showAlert("Ureðaj nije spojen na mrežu!", MainActivity.this);
	   	}
	       
	   //button za postavljanje podataka u pocetno stanje
	   ImageButton refresh = (ImageButton) findViewById(R.id.btnRefresh);
	   refresh.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
	        	   //postavi podatke na pocetno stanje
	        	   _odabranaLinijaID = 0;
	        	   _odabranaVoznjaID = 0;
	        	   //postavi execute string 
	        	   String executeString = "exec mobile.get_izleti " + _odabranaLinijaID +  ", " + _odabranaVoznjaID + ", " + _prodajnoMjestoID + ", " + _korisnikID;
	        	   if (isNetworkAvailable()){
            		   accessWebServiceIzleti(executeString, "dataPostback", _context);
	        	   }
            	   else{
            		   AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
            	   }
	        	   //provjeriPrinter();
	           }});
	       
	       //button za knjizenje i ispis
	   ImageButton print = (ImageButton) findViewById(R.id.btnPrint);
	   print.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   _tipRadaRacuna = 0;
			   if(_printer.equals("CMP30")){
				   CMP30Print cp = new CMP30Print(_context);
				   String msg = cp.provjeriVezu();
				   if(msg.equals("")){
					   posaljiKarte();}
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
					   posaljiKarte();
				   }
				   else{
					   AlertView.showError("Bluetooth printer nije moguæe spojiti!\nProvjerite da li je printer upaljen.", _context);
				   }
			   }
			   
			   if(_printer.equals("CitizenUSB")){
				   CitizenUSBPrint cup = new CitizenUSBPrint(_context);
				   if(cup.provjeriVezu()){
					   posaljiKarte();
				   }
				   else{
						AlertView.showError("Spajanje sa USB printerom nije uspjelo", _context);

				   }
			   }
			   
			   if(_printer.equals("NA")){
				   posaljiKarte();
			   }
	           }});
	   
	   ImageButton rezervacija = (ImageButton) findViewById(R.id.btnRezervacija);
	   rezervacija.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   _tipRadaRacuna = 2;
			   if(_printer.equals("CMP30")){
				   CMP30Print cp = new CMP30Print(_context);
				   String msg = cp.provjeriVezu();
				   if(msg.equals("")){
					   posaljiKarte();}
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
					   posaljiKarte();
				   }
				   else{
					   AlertView.showError("Bluetooth printer nije moguæe spojiti!\nProvjerite da li je printer upaljen.", _context);
				   }
			   }
			   
			   if(_printer.equals("CitizenUSB")){
				   CitizenUSBPrint cup = new CitizenUSBPrint(_context);
				   if(cup.provjeriVezu()){
					   posaljiKarte();
				   }
				   else{
						AlertView.showError("Spajanje sa USB printerom nije uspjelo", _context);

				   }
			   }
			   
			   if(_printer.equals("NA")){
				   posaljiKarte();
			   }
	           }});
	       
	       //button za otvaranje Intenta sa obrascem za spajanje na printer
	   ImageButton connect = (ImageButton) findViewById(R.id.btnConnect);
	   connect.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View v) {
	        	   AlertDialog.Builder alert = new AlertDialog.Builder(_context);

	    		   alert.setTitle("Storno");
	    		   alert.setMessage("Unesite broj raèuna koji želite stornirati");
	    		   // Set an EditText view to get user input 
	    		   final EditText input = new EditText(_context);
	    		   alert.setView(input);

	    		   alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    		   public void onClick(DialogInterface dialog, int whichButton) {
			    		     String value = input.getText().toString();
			    		     if(_printer.equals("CMP30")){
			  				   CMP30Print cp = new CMP30Print(_context);
			  				   String msg = cp.provjeriVezu();
			  				   if(msg.equals("")){
			  					 String executeString = "mobile.racun_storno '" + value +"'," + _prodajnoMjestoID +"," + _naplatniUredjajID +"," + _korisnikID;
				            	   if (isNetworkAvailable()){
				            		   accessWebServiceIzleti(executeString, "printPostback", _context);
				            		   }
				            	   else{
				            		   AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
				            	   }}
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
			  					 String executeString = "mobile.racun_storno '" + value +"'," + _prodajnoMjestoID +"," + _naplatniUredjajID +"," + _korisnikID;
				            	   if (isNetworkAvailable()){
				            		   accessWebServiceIzleti(executeString, "printPostback", _context);
				            		   }
				            	   else{
				            		   AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
				            	   }
			  				   }
			  				   else{
			  					   AlertView.showError("Bluetooth printer nije moguæe spojiti!\nProvjerite da li je printer upaljen.", _context);
			  				   }
			  			   }
			  			   
			  			   if(_printer.equals("CitizenUSB")){
			  				   CitizenUSBPrint cup = new CitizenUSBPrint(_context);
			  				   if(cup.provjeriVezu()){
			  					 String executeString = "mobile.racun_storno '" + value +"'," + _prodajnoMjestoID +"," + _naplatniUredjajID +"," + _korisnikID;
				            	   if (isNetworkAvailable()){
				            		   accessWebServiceIzleti(executeString, "printPostback", _context);
				            		   }
				            	   else{
				            		   AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
				            	   }
			  				   }
			  				   else{
			  						AlertView.showError("Spajanje sa USB printerom nije uspjelo", _context);
		
			  				   }
			  			   }
			  			   
			  			   if(_printer.equals("NA")){
			  				 String executeString = "mobile.racun_storno '" + value +"'," + _prodajnoMjestoID +"," + _naplatniUredjajID +"," + _korisnikID;
			            	   if (isNetworkAvailable()){
			            		   accessWebServiceIzleti(executeString, "printPostback", _context);
			            		   }
			            	   else{
			            		   AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
			            	   }
			  			   }
	    		     //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
	    		     }
	    		   });

	    		   alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    		     public void onClick(DialogInterface dialog, int whichButton) {
	    		       // Canceled.
	    		     }
	    		   });

	    		   alert.show();
	        	   /*if(_bluetoothAdapter != null && (_printer.equals("CMP30") || _printer.equals("BIX"))){
	        		   goToCMP30Connect();
	        	   }
	        	   else{
	        		   AlertView.showError("Ureðaj nema Bluetooth i nije moguæe spojiti printer!", MainActivity.this);
	        	   }*/
	           }});
	   
	   TextView akontacija = (TextView) findViewById(R.id.lblSumAkontacija);
	   akontacija.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
	        	   //postavi podatke na pocetno stanje
		   AlertDialog.Builder alert = new AlertDialog.Builder(_context);

		   alert.setTitle("Akontacija");
		   alert.setMessage("Unesite iznos akontacije");
		   // Set an EditText view to get user input 
		   final EditText input = new EditText(_context);
		   alert.setView(input);

		   alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int whichButton) {
		     String value = input.getText().toString();
		     double iznosAkontacije = 0;
		     try{
		    	 iznosAkontacije = Double.parseDouble(value);
		    	 TextView akontacijaUkupna = (TextView) findViewById(R.id.lblSumAkontacija);
		    	 DecimalFormat df = new DecimalFormat("##0.00");
		    	 akontacijaUkupna.setText(df.format(iznosAkontacije));
		     }
		     catch(NumberFormatException ex){
		    	 AlertView.showAlert("Format akontacije nije ispravan!\nZa decimalne brojeve koristite toèku!", MainActivity.this);
		    	 return;
		     }
		     //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
		     }
		   });

		   alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog, int whichButton) {
		       // Canceled.
		     }
		   });

		   alert.show();
	           }});
	       
	   /*ImageButton exit = (ImageButton) findViewById(R.id.btnLogOut);
	   exit.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View v) {
	        	   //Intent myIntent = new Intent(MainActivity.this, LogIn.class);
	        	   //myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        	   //startActivity(myIntent);
	        	   AlertView.LogOutQuestion("Odjava", "Jeste li sigurni da se želite odjaviti?", _context);
	           }});*/
	       
	   //provjeriPrinter();
	   
	   postaviDanasnjiDatum();
	   
	   /*primjena postavki*/
	   primjeniPostavke();
       }
    
    //Ucitavanje menija sa Postavkama
    @Override 
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        super.onOptionsItemSelected(item);
 
        switch(item.getItemId()){
            /*case R.id.postavke:
         	   Intent PostavkeIntent = new Intent(_context, PostavkeActivity.class);
        	   startActivity(PostavkeIntent);
                break;*/
            case R.id.blagajna:
          	   Intent BlagajnaIntent = new Intent(_context, BlagajnaActivity.class);
         	   startActivity(BlagajnaIntent);
                 break;
            case R.id.printer:
 	        	   if(_bluetoothAdapter != null && (_printer.equals("CMP30") || _printer.equals("BIX"))){
 	        		   goToCMP30Connect();
 	        	   }
 	        	   else{
 	        		   AlertView.showError("Ureðaj nema Bluetooth i nije moguæe spojiti printer!", MainActivity.this);
 	        	   }
 	        	 break;
            case R.id.odjava:
            	AlertView.LogOutQuestion("Odjava", "Jeste li sigurni da se želite odjaviti?", _context);

                 break;
        }
        return true;
 
    }
    
    
    //Izvodi se u trenutku povratka na ovaj MainActivity sa nekog drugog Intenta
    /*@Override
    protected void onResume()
    {
       super.onResume();
       provjeriPrinter();
    }*/

    protected void onDestroy(){
    	//Code when app is destroyed
    }
    @Override
    public void onBackPressed() {
        // do nothing.
    }
    /**Puni voznje, karte i menije sa podacima iz _JSONStringa
     * _JSONString se puni u pozivu SOAPa*/
    private void fillObject(){
    	// create some objects
    	m_parts.clear();
    	k_parts.clear();
    	menu_parts.clear();
    	mjesto_parts.clear();
    	popust_parts.clear();
    	
    	 try{ 
    		   JSONObject jsonResponse = new JSONObject(_jsonString);
    		   JSONObject jsonInnerObjectPodaci = jsonResponse.getJSONObject("podaci");
    		   JSONObject jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("linije");
    		   JSONArray jsonMainNode = jsonInnerObject.optJSONArray("linija");
    		   
    		  for(int i = 0; i<jsonMainNode.length();i++){ 
    		   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
    		   String izletId = jsonChildNode.optString("@izlet_id");
    		   int id = Integer.parseInt(izletId);
    		   if(id > 0){ 
    			   _tipObracunaCijena = Integer.parseInt(jsonChildNode.optString("@tip_obracuna_cijene"));
    			   _maxKarata = Integer.parseInt(jsonChildNode.optString("@maximum_karata"));
    			   _maxIznosAkontacije = Double.parseDouble(jsonChildNode.optString("@iznos_akontacije"));
    			   _isAkontacijaUzKartu = Integer.parseInt(jsonChildNode.optString("@is_akontacija_uz_kartu"));
    			   
	    		   String voznjaId = jsonChildNode.optString("@voznja_id");
	    		   String naziv = jsonChildNode.optString("@naziv_izleta");
	    		   String datum = jsonChildNode.optString("@datum");
	    		   String vrijeme = jsonChildNode.optString("@vrijeme");
	    		   String slobodno = jsonChildNode.optString("@slobodno");    		      		   
	    		   String prodajaDozvoljena = jsonChildNode.optString("@prodaja_dozvoljena");
	    		   String prikazTeksta = jsonChildNode.optString("@prikaz_teksta");
	    		   String tipObracunaCijene = jsonChildNode.optString("@tip_obracuna_cijene");
	    		   String maximumKarata = jsonChildNode.optString("@maximum_karata");
	    		   String iznosAkontacije = jsonChildNode.optString("@iznos_akontacije");
	    		   m_parts.add(new Voznja(Integer.parseInt(izletId), Integer.parseInt(voznjaId), naziv, datum, vrijeme, slobodno, Integer.parseInt(prodajaDozvoljena), prikazTeksta, Integer.parseInt(tipObracunaCijene), Integer.parseInt(maximumKarata), iznosAkontacije));
    		   }
    		  }
    		  
    		  
    		  jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("karte");
   		   	  jsonMainNode = jsonInnerObject.optJSONArray("karta");
    		  
    		  for(int i = 0; i<jsonMainNode.length();i++){
       		   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
       		   String kartaId = jsonChildNode.optString("@vrsta_karte_id");
       		   int id = Integer.parseInt(kartaId);
       		   if(id > 0){
	       		   String linijaId = jsonChildNode.optString("@linija_id");
	       		   String vrstaKarteId = jsonChildNode.optString("@vrsta_karte_id");
	       		   String naziv = jsonChildNode.optString("@naziv");
	       		   String cijena = jsonChildNode.optString("@cijena");
	       		   String akontacija = jsonChildNode.optString("@akontacija");
	       		   k_parts.add(new Karta(Integer.parseInt(linijaId), Integer.parseInt(vrstaKarteId), naziv, cijena, akontacija, "0"));
       		   }
       		  }
    		  
    		  
    		  jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("meniji");
   		   	  jsonMainNode = jsonInnerObject.optJSONArray("meni");
    		  
    		  for(int i = 0; i<jsonMainNode.length();i++){
       		   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
       		   String meniId = jsonChildNode.optString("@meni_id");
    		   int id = Integer.parseInt(meniId);
    		   if(id > 0){
	       		   String menuId = jsonChildNode.optString("@meni_id");
	       		   String nazivMenija = jsonChildNode.optString("@naziv_menija");
	       		   String cijena = jsonChildNode.optString("@cijena");
	       		   String akontacija = jsonChildNode.optString("@akontacija");
	       		   String linijaId = jsonChildNode.optString("@linija_id");
	       		   String redoslijed= jsonChildNode.optString("@redoslijed");
	       		   String vrstaKarteId = jsonChildNode.optString("@vrsta_karte_id");
	       		   menu_parts.add(new Meni(Integer.parseInt(menuId), nazivMenija, cijena, akontacija, Integer.parseInt(linijaId), Integer.parseInt(redoslijed), Integer.parseInt(vrstaKarteId), "0"));
    		   	}
    		   }
    		  
    		  jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("mjesta");
   		   	  jsonMainNode = jsonInnerObject.optJSONArray("mjesto");
   		   	  mjesto_parts.add(new Mjesto(0, "Luka",0, ""));
    		  for(int i = 0; i<jsonMainNode.length();i++){
       		   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
       		   String meniId = jsonChildNode.optString("@ukrcajno_mjesto_id");
    		   int id = Integer.parseInt(meniId);
    		   if(id > 0){
	       		   String mjestoId = jsonChildNode.optString("@ukrcajno_mjesto_id");
	       		   String nazivMjesta = jsonChildNode.optString("@ukrcajno_mjesto");
	       		   String vremenskiPomak = jsonChildNode.optString("@vremenski_pomak");
	       		   String vrijemePolaska = jsonChildNode.optString("@prikaz_vremena_ukrcaja");
	       		   mjesto_parts.add(new Mjesto(Integer.parseInt(mjestoId), nazivMjesta,Integer.parseInt(vremenskiPomak), vrijemePolaska));
    		   	}
    		   }
    		  
    		  jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("popusti");
   		   	  jsonMainNode = jsonInnerObject.optJSONArray("popust");
    		  for(int i = 0; i<jsonMainNode.length();i++){
       		   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
       		   String meniId = jsonChildNode.optString("@popust_id");
    		   int id = Integer.parseInt(meniId);
    		   if(id > 0){
	       		   String popustId = jsonChildNode.optString("@popust_id");
	       		   String iznosPopusta = jsonChildNode.optString("@iznos_popusta");
	       		   popust_parts.add(new Popust(Integer.parseInt(popustId),Integer.parseInt(iznosPopusta)));
    		   	}
    		   }
    		  
    		  
    		 }
    	catch(JSONException e){
    			AlertView.showError("Pogreška kod punjenja objekata sa JSON stringom - "+ e.toString(), _context);
    		   //Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
    		  }	
    }
    
    
    

    /**Punjenje ListViewa  sa voznjama*/
    private void displayVoznje(ArrayList<Voznja> m_parts){

    		m_adapter = new VoznjaAdapter(_context, R.layout.voznja_item, m_parts);

            // display the list.
            ListView listView = (ListView) findViewById(R.id.lvVoznje);
            Parcelable state = listView.onSaveInstanceState();
            // Assign adapter to ListView
            listView.setAdapter(m_adapter);
            
            listView.onRestoreInstanceState(state);
          //enables filtering for the contents of the given ListView
            listView.setTextFilterEnabled(true);
            listView.setOnItemClickListener(new OnItemClickListener() {
             public void onItemClick(AdapterView<?> parent, View view,
		               int position, long id) {
		             // When clicked, do some work
		             Voznja voznja = (Voznja) parent.getItemAtPosition(position);
		             Integer odabranaLinijaId = voznja.getIzletId();
		             _odabranaLinijaID = odabranaLinijaId;
		             
		             Integer odabranaVoznjaId = voznja.getVoznjaId();
		             _odabranaVoznjaID = odabranaVoznjaId;
		             
		             String vrijemePolaska = voznja.getVrijemeVoznje();
		             _vrijemePolaska = vrijemePolaska;
		             
		             String executeString = "exec mobile.get_izleti " + _odabranaLinijaID +  ", " + _odabranaVoznjaID + ", " + _prodajnoMjestoID + ", " + _korisnikID;
		             
		             accessWebServiceIzleti(executeString, "dataPostback", _context);
		             
		             m_adapter.setSelectedIndex(position);
		             
		             racunajCijenu();
		             
		             }
		            });
           
        }
    
    
    
    
    /**Punjenje ListViewa  sa kartama*/
    private void displayKarte(ArrayList<Karta> k_parts){
    	k_adapter = new KartaAdapter(_context, R.layout.karta_item, k_parts);

        // display the list.
        ListView listView = (ListView) findViewById(R.id.lvKarte);
        // Assign adapter to ListView
        listView.setAdapter(k_adapter);
        listView.setTextFilterEnabled(true);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		         // When clicked, do some work
		         Karta karta = (Karta) parent.getItemAtPosition(position);
		         karta.setBrojKarata(Integer.toString(Integer.parseInt(karta.getBrojKarata())+1)); 
		         racunajCijenu();
		         reloadKarte();
		         
		         }
		        });
        
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            	Karta karta = (Karta) parent.getItemAtPosition(position);
                karta.setBrojKarata("0"); 
                reloadKarte();
                racunajCijenu();
                return true;
            }
        }); 
        
    }
    
    
    
    /**Punjenje ListViewa  sa menijima*/
    private void displayMenu(ArrayList<Meni> menu_parts){
    	menu_adapter = new MenuAdapter(_context, R.layout.menu_item, menu_parts);

        // display the list.
        ListView listView = (ListView) findViewById(R.id.lvMenus);
        // Assign adapter to ListView
        listView.setAdapter(menu_adapter);
        listView.setTextFilterEnabled(true);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
	         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	         // When clicked, do some work
			         Meni menu = (Meni) parent.getItemAtPosition(position);
			         menu.setKolicina(Integer.toString(Integer.parseInt(menu.getKolicina())+1)); 
			         racunajCijenu();
			         reloadMenus();
			         }
         });
        
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        	public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
        		 Meni menu = (Meni) parent.getItemAtPosition(position);
    	         menu.setKolicina("0");
    	         reloadMenus();
    	         racunajCijenu();
                return true;
            } 
        }); 
        
    }
    
    /**Punjenje Spinnera sa mjestima*/
    private void displayPolaziste(ArrayList<Mjesto> mjestaArray){
    	mjesto_adapter = new MjestoAdapter(_context, R.layout.mjesto_item, mjestaArray);
        // display the list.
        Spinner popMjesta = (Spinner) findViewById(R.id.ukrcaj_spinner);
        // Assign adapter to Spinner
        popMjesta.setAdapter(mjesto_adapter);
        
        //enables filtering for the contents of the given ListView
        popMjesta.setOnItemSelectedListener( new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View view,
	               int position, long id) {
	             // When clicked, do some work
	             Mjesto mjesto = (Mjesto) parent.getItemAtPosition(position);
	             _polazisteId = mjesto.getMjestoId();
	             String vrijemePolaska = mjesto.getVrijemePolaska();
	             if (!vrijemePolaska.equals("")){
	            	TextView vp = (TextView)findViewById(R.id.lblSumPolazak);
	     	        vp.setText(vrijemePolaska);
	             }
	             else if(!_vrijemePolaska.equals("") && vrijemePolaska.equals("")){
	            	 TextView vp = (TextView)findViewById(R.id.lblSumPolazak);
		     	     vp.setText(_vrijemePolaska);
	             }
	              
	             
	            
	 	         //vrijemePolaska.setText(m_parts.get(0).getVrijemeVoznje());
	             //Toast.makeText(_context, odabranoMjestoId.toString() , Toast.LENGTH_SHORT).show();
	             
        	}
        	public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            } 
	     });
       
    }
    
    /**Punjenje Spinnera sa popustima*/  
    private void displayPopusti(ArrayList<Popust> popustiArray){
    	popust_adapter = new PopustAdapter(_context, R.layout.mjesto_item, popustiArray);
        // display the list.
        Spinner popPopust = (Spinner) findViewById(R.id.popust_spinner);
        // Assign adapter to Spinner
        popPopust.setAdapter(popust_adapter);
        
        //enables filtering for the contents of the given ListView
        popPopust.setOnItemSelectedListener( new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View view,
	               int position, long id) {
	             // When clicked, do some work
	            Popust popust= (Popust) parent.getItemAtPosition(position);
	             //Integer popustId = popust.getPopustId();
	             
	            _iznosPopusta = popust.getIznosPopusta();
	            racunajCijenu();
	            //Toast.makeText(_context, _iznosPopusta.toString() , Toast.LENGTH_SHORT).show();
	             
        	}
        	public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            } 
	     });
       
    }
    
    
    /**Funkcije za refresh karata*/    
    private void reloadKarte(){
    	k_adapter.notifyDataSetChanged();  
    } 
    
    /**Funkcije za refresh menija*/
    private void reloadMenus(){
    	menu_adapter.notifyDataSetChanged();
    }
    
    /**Funkcije za refresh ukrcajnih mjesta*/
    private void reloadPolazista(){
    	mjesto_adapter.notifyDataSetChanged();
    }
    
    /**Funkcija za raèunanje ukupnog iznosa voznje*/
    private void racunajCijenu(){
    	Double ukupnaCijena = 0.00;
    	for(int i=0;i<k_parts.size();i++){
    		Double cijena = Double.parseDouble(k_parts.get(i).getCijenaKarte().toString());
    		Double kolicina = Double.parseDouble(k_parts.get(i).getBrojKarata().toString());
    		ukupnaCijena = ukupnaCijena + (cijena*kolicina);
    	}
    	for(int i=0;i<menu_parts.size();i++){
    		Double cijena = Double.parseDouble(menu_parts.get(i).getCijena().toString());
    		Double kolicina = Double.parseDouble(menu_parts.get(i).getKolicina().toString());
    		ukupnaCijena = ukupnaCijena + (cijena*kolicina);
    	}
    	DecimalFormat df = new DecimalFormat("##0.00");
    	TextView cijenaUkupna = (TextView) findViewById(R.id.lblSumCijena);
    	ukupnaCijena = ukupnaCijena * (1 - Double.parseDouble(_iznosPopusta.toString())/100);
    	cijenaUkupna.setText(df.format(ukupnaCijena));
    	//cijenaUkupna.setText(ukupnaCijena.toString());
    	
    	Double ukupnaAkontacija = 0.00;
    	Double kolicinaMenu = 0.00; 
    	for(int i=0;i<k_parts.size();i++){
    		Double cijena = Double.parseDouble(k_parts.get(i).getAkontacija().toString());
    		kolicinaMenu = Double.parseDouble(k_parts.get(i).getBrojKarata().toString());
    		ukupnaAkontacija = ukupnaAkontacija + (cijena*kolicinaMenu);
    	}
    	
    	for(int i=0;i<menu_parts.size();i++){
    		Double cijena = Double.parseDouble(menu_parts.get(i).getAkontacija().toString());
    		kolicinaMenu = Double.parseDouble(menu_parts.get(i).getKolicina().toString());
    		ukupnaAkontacija = ukupnaAkontacija + (cijena*kolicinaMenu);
    	}

    	TextView akontacijaUkupna = (TextView) findViewById(R.id.lblSumAkontacija);
    	akontacijaUkupna.setText(df.format(ukupnaAkontacija));
    	if (_tipObracunaCijena == 0){
    		akontacijaUkupna.setText(df.format(0));
    	}
    	else {
    		if (_tipObracunaCijena == 2 && _isAkontacijaUzKartu == 1){
    			akontacijaUkupna.setText(df.format(ukupnaAkontacija));
    		}
    		else{
    			akontacijaUkupna.setText(df.format(_maxIznosAkontacije));
    		}
    	}
    	
    } 
    
    
    
    /**Postavlja danasnji datum*/
    private void postaviDanasnjiDatum(){
    	 //Set danasnji datum
        TextView tvDisplayDate = (TextView) findViewById(R.id.lblDatum);
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
        // Month is 0 based, just add 1
                .append(dd).append(".").append(mm + 1).append(".")
                .append(yy).append("."));
    	
    }
    
    /** Funkcija koja skuplja potrebne podatke generira XML za slanje na server*/
    private String generateXML(){
    	String ukupnaCijena;
    	String prodajnoMjestoId = Postavke.getPostavke(6, _context);
    	String naplatniUredjajId = Postavke.getPostavke(7, _context);
    	String korisnikId = Postavke.getPostavke(0, _context);
    	Integer racunId = 0;
    	String ukupnaAkontacija;
    	
    	TextView tekst = (TextView) findViewById(R.id.lblSumCijena);
    	ukupnaCijena = (String) tekst.getText();
    	ukupnaCijena = ukupnaCijena.replace(",",".");
    	
    	tekst = (TextView) findViewById(R.id.lblSumAkontacija);
    	ukupnaAkontacija = (String) tekst.getText();
    	ukupnaAkontacija = ukupnaAkontacija.replace(",",".");
    	
    	String XML = "";
    	XML = "'<racun voznja_id =\"" + _odabranaVoznjaID +  "\" racun_id=\""+ racunId + "\" ukupna_cijena=\"" + ukupnaCijena + "\" prodajno_mjesto_id =\"" + prodajnoMjestoId + "\" naplatni_uredjaj_id =\"" + naplatniUredjajId + "\" tip_rada_racuna =\"" + _tipRadaRacuna.toString() + "\" ukrcajno_mjesto_id =\"" + _polazisteId.toString() + "\" popust =\"" + _iznosPopusta.toString() + "\" ukupna_akontacija=\"" + ukupnaAkontacija + "\" korisnik_id=\"" + korisnikId + "\" ><karte>"; 
    	
    	for (int i = 0; i < k_parts.size(); i++){
    		XML = XML + "<karta vrsta_karte_id=\"" + k_parts.get(i).getTipKarteId() + "\" kolicina=\"" + k_parts.get(i).getBrojKarata() + "\" oznaka =\"" + k_parts.get(i).getTipKarte() + "\" cijena=\"" + k_parts.get(i).getCijenaKarte() + "\" akontacija=\"" + k_parts.get(i).getAkontacija() + "\"/> ";
    		}  
    		      
    	XML = XML +  "</karte><meniji>";
    	
    	for (int i = 0; i < menu_parts.size(); i++){
    	XML = XML +	 " <menu meni_id=\"" + menu_parts.get(i).getId() + "\" kolicina=\"" + menu_parts.get(i).getKolicina() + "\" cijena=\"" + menu_parts.get(i).getCijena() + "\" akontacija=\"" + menu_parts.get(i).getAkontacija() + "\"/>";
    		}
    	
    	XML = XML +	"</meniji></racun>'";
    	
    	return XML;
    }
    
    /**Funkcija za postavljanje inicijalnih podataka u formu*/
    private void postaviInicijalnePodatke(){
    	try{
	    	//Napuni Voznje objekt i listu Voznji 
	        fillObject();
	               
	      //Postavi podatke za zaglavlje voznje
	        TextView odabranaVoznja = (TextView)findViewById(R.id.lblLinija);
	        odabranaVoznja.setText(m_parts.get(0).getImeVoznje());
	        TextView odabranaVoznjaDatum = (TextView)findViewById(R.id.lblProdajaDatum);
	        odabranaVoznjaDatum.setText(m_parts.get(0).getDatumVoznje());        
	        
	        //Postavi podatke za sumarno
	        TextView odabranoOdrediste = (TextView)findViewById(R.id.lblSumOdrediste);
	        odabranoOdrediste.setText(m_parts.get(0).getImeVoznje() + " " + m_parts.get(0).getDatumVoznje());
	        
	        TextView vrijemePolaska = (TextView)findViewById(R.id.lblSumPolazak);
	        vrijemePolaska.setText(m_parts.get(0).getVrijemeVoznje());
	        
	        TextView akontacija = (TextView)findViewById(R.id.lblSumAkontacija);
	        akontacija.setText(m_parts.get(0).getIznosAkontacije());
	        
	        _vrijemePolaska = m_parts.get(0).getVrijemeVoznje();
	        
	        //Napuni Karte objekt, listu Karata i Menu objekt, listu menua
	        displayVoznje(m_parts);
	        displayKarte(k_parts);
	        displayMenu(menu_parts);
	        displayPolaziste(mjesto_parts);
	        displayPopusti(popust_parts);
	        
	        _odabranaVoznjaID = m_parts.get(0).getVoznjaId();
	        _odabranaLinijaID = m_parts.get(0).getIzletId();
	        Spinner spn = (Spinner)findViewById(R.id.popust_spinner);
	        spn.setSelection(0);
	        _iznosPopusta = 0;
	        spn = (Spinner)findViewById(R.id.ukrcaj_spinner);
	        spn.setSelection(0);
	        _polazisteId = 0;
	        
	        racunajCijenu();}
    	catch(Exception e){
    		AlertView.showError("Nemogu postaviti inicijalne podatke "+e.toString(), _context);
            //Toast.makeText(MainActivity.this,"Nemogu postaviti inicijalne podatke "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**Funkcija za postavljanje dohvaèenih podataka u formu*/
    private void postaviNovePodatke(){
    	try{
    	//Napuni Voznje objekt i listu Voznji 
	        fillObject();   
	      //Postavi podatke za zaglavlje voznje
	        for(int i=0; i<m_parts.size(); i++){
	        	int voznjaId = m_parts.get(i).getVoznjaId();
	        	if (voznjaId == _odabranaVoznjaID){
	        		 TextView odabranaVoznja = (TextView)findViewById(R.id.lblLinija);
	        	        odabranaVoznja.setText(m_parts.get(i).getImeVoznje());
	        	        TextView odabranaVoznjaDatum = (TextView)findViewById(R.id.lblProdajaDatum);
	        	        odabranaVoznjaDatum.setText(m_parts.get(i).getDatumVoznje());        
	        	        
	        	        //POstavi podatke za sumarno
	        	        TextView odabranoOdrediste = (TextView)findViewById(R.id.lblSumOdrediste);
	        	        odabranoOdrediste.setText(m_parts.get(i).getImeVoznje() + " " + m_parts.get(i).getDatumVoznje());
	        	        
	        	        TextView vrijemePolaska = (TextView)findViewById(R.id.lblSumPolazak);
	        	        vrijemePolaska.setText(m_parts.get(i).getVrijemeVoznje());
	        	        
	        	        TextView akontacija = (TextView)findViewById(R.id.lblSumAkontacija);
	        	        akontacija.setText(m_parts.get(i).getIznosAkontacije());
	        	        
	        	        _vrijemePolaska = m_parts.get(i).getVrijemeVoznje();
	       
	        	        //Napuni Karte objekt, listu Karata i Menu objekt, listu menua
	        	        m_adapter.notifyDataSetChanged();
	        	        k_adapter.notifyDataSetChanged();
	        	        menu_adapter.notifyDataSetChanged();
	        	        mjesto_adapter.notifyDataSetChanged();
	        	        popust_adapter.notifyDataSetChanged();
	        	        Spinner spn = (Spinner)findViewById(R.id.popust_spinner);
	        	        spn.setSelection(0);
	        	        _iznosPopusta = 0;
	        	        spn = (Spinner)findViewById(R.id.ukrcaj_spinner);
	        	        spn.setSelection(0);
	        	        _polazisteId = 0;
	        	        racunajCijenu();	
	        	}
	        }
       }
    	catch(Exception e){
    		AlertView.showError("Nemogu postaviti nove podatke "+e.toString(), _context);
            //Toast.makeText(MainActivity.this,"Nemogu postaviti inicijalne podatke "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    
    
    /**Funkcija za dohvaæanje i slanje poataka na server
     * imeFunkcije : "printPostback" ako se salje racun, "dataPostback ako se dohvacaju podaci za liste"*/
    private void accessWebServiceIzleti(String executeString, String imeFunkcije, Context context) {
        SoapAccessTaskIzleti task = new SoapAccessTaskIzleti(executeString, imeFunkcije, context);
        //passes values for the urls string array
        task.execute();
    }
    
    /**Funkcija koja se poziva iz SoapAccessTaskIzleti kod snimanja racuna*/
    public void printPostback(String result){
        //if you started progress dialog dismiss it here
         //Nakon dohvata podataka, ako je ukljuèen flag za knjiženje proknjiži, 
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
     				   cp.print(jsonMainNode);
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
    		 String executeString = "exec mobile.get_izleti " + _odabranaLinijaID +  ", " + _odabranaVoznjaID + ", " + _prodajnoMjestoID + ", " + _korisnikID;
      	    accessWebServiceIzleti(executeString, "dataPostback", _context);
      	    //Toast.makeText(getApplicationContext(),"Ureðaj spojen na server...", Toast.LENGTH_SHORT).show();
      	    
    	
        
    
    }
    
    /**Funkcija koja se poziva iz SoapAccessTaskIzleti kod preuzimanja podataka za liste*/
    public void dataPostback(String result){
    		//dovat novi podatka i postavljanje dohvaèenih podataka
            if (result != "") {
            	_jsonString=result;
            	}
            if(_odabranaVoznjaID == 0){
            	postaviInicijalnePodatke();
            	}
            else{
            	postaviNovePodatke();
            	}
    }

	/**Provjerava kolicinu karata, ako nijedna karta nije odabrana prekida slanje i 
     * javlja poruku da se odabere karta za prodaju*/
    private boolean provjeriKolicinuKarata(){
    	Integer ukupnaKolicina = 0;
    	for(int i=0; i<k_parts.size(); i++){
    		Integer kolicina = Integer.parseInt(k_parts.get(i).getBrojKarata());
    		ukupnaKolicina = ukupnaKolicina + kolicina;
    	}
    	if(ukupnaKolicina == 0) return false;
    	else return true;
    }
    
    
    private void primjeniPostavke(){
    	TextView text = (TextView) findViewById(R.id.lblBlagajnik);
        String naziv = Postavke.getPostavke(1, _context);
        text.setText(naziv);
        text = (TextView) findViewById(R.id.lblProdMjesto);
        naziv = Postavke.getPostavke(2, _context);
        text.setText(naziv);   	
        
        if (Postavke.getPostavke(10, _context).equals("0")){
	        ImageButton racun = (ImageButton) findViewById(R.id.btnPrint);
	        racun.setVisibility(View.GONE);
	        }
        
        if (Postavke.getPostavke(11, _context).equals("0")){
            ImageButton rezervacija = (ImageButton) findViewById(R.id.btnRezervacija);
            rezervacija.setVisibility(View.GONE);
            }  
        
    }
    
    /**Provjera da li je ureðaj spojen na mrežu*/
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    /**Posalji karte na server*/
    private void posaljiKarte(){
    	if(provjeriKolicinuKarata()){
     	   String XML = generateXML();
     	   String executeString = "mobile.snimi_racun " + XML;
	            	   if (isNetworkAvailable()){
	            		   accessWebServiceIzleti(executeString, "printPostback", _context);
	            		   }
	            	   else{
	            		   AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
	            	   }
 	   }
 	   else{
 		   AlertView.showAlert("Nema odabranih karata!\nOdaberite karte i pokušajte ponovo.", _context);
 	   }
	   
    }  
    
    public void goToLogIn(){
    	Intent myIntent = new Intent(_context, LogIn.class);
 	   	startActivity(myIntent);
    }
    
    public void goToCMP30Connect(){
    	Intent myIntent = new Intent(_context, BluetoothConnectMenu.class);
 	   	myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
 	   	startActivity(myIntent);
    }
    
    private void provjeriPrinter(){
    	_bluetoothPort = BluetoothPort.getInstance();
  	   	ImageButton connect = (ImageButton) findViewById(R.id.btnConnect);
        if(_bluetoothPort.isConnected() && _printer.equals("CMP30")){
      	   //connect.setVisibility(View.GONE);
      	   connect.setImageResource(R.drawable.printer_settings_connected);
         		}
        else{//connect.setVisibility(View.VISIBLE);
      	   connect.setImageResource(R.drawable.printer_settings_disconnected);
         		}
        if (_printer.equals("NA")){
  		   connect.setVisibility(View.GONE);
        }
        if (_printer.equals("CitizenUSB")){
  		   connect.setEnabled(false);
  		   CitizenUSBPrint cup = new CitizenUSBPrint(_context);
  		   if(cup.provjeriVezu()){
  			   connect.setImageResource(R.drawable.printer_settings_connected);
  		   }
        }
    }
    

};