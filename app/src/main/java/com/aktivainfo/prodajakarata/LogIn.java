package com.aktivainfo.prodajakarata;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends Activity {
	private Context _context; 
	
	public void onCreate(Bundle savedInstanceState)
	{	
		_context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_in_layout);
		
		Button logIn = (Button) findViewById(R.id.btnLogInOk);
		logIn.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View v) {
	        	   if(validateInput()){
		        	   String username;
		        	   String password;
		        	   EditText Text = (EditText) findViewById(R.id.txtUsername);
		        	   username = Text.getText().toString(); 
		        	   Text = (EditText) findViewById(R.id.txtPassword);
		        	   password = Text.getText().toString();
		        	   String sifMjesta = Postavke.getPostavke(8, _context).toString();
		        	   String sifUredjaja = Postavke.getPostavke(9, _context).toString();
		        	   String klijentId = Postavke.getPostavke(3, _context);
		        	   if(username.equals("aiadmin") && password.equals("postavke")){
		        		   Intent PostavkeIntent = new Intent(_context, PostavkeActivity.class);
		            	   startActivity(PostavkeIntent);
		        	   }
		        	   else{
				        	   String executeString = "mobile.make_login " + "'" + username + "'" +"," + "'" + password + "'" +"," + "'" + klijentId + "'" +"," + "'" + sifMjesta + "'" +"," + "'" + sifUredjaja + "'";
				        	   if(isNetworkAvailable()){
				        		   accessWebServiceIzleti(executeString, "logInPostback", _context);
				        	   }
				        	   else{
				        		   AlertView.showAlert("Ureðaj nije spojen na mrežu!", _context);
				        	   }
		        	   }
	        	   }
	       }});
		Button exit = (Button) findViewById(R.id.btnLogInCancel);
		exit.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        Intent i = new Intent(Intent.ACTION_MAIN);
		        i.addCategory(Intent.CATEGORY_HOME);
		        //finish();
		        startActivity(i);
		    }});
	}
	public void onBackPressed() {
		Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        //finish();
        startActivity(i);
    }
	
	
	/**Funkcija za validaciju unosa kod logiranja*/
	private boolean validateInput(){
	   String username;
 	   String password;
 	   EditText Text = (EditText) findViewById(R.id.txtUsername);
 	   username = Text.getText().toString();
 	   Text = (EditText) findViewById(R.id.txtPassword);
 	   password = Text.getText().toString();
 	   if(username.trim().equals("")){
 		   AlertView.showError("Upišite korisnièko ime", _context);
 		   return false;
 	   }
 	  if(password.trim().equals("")){
		   AlertView.showError("Upišite lozinku", _context);
		   return false;
	   }
 	   return true;
	}
	
	/**Provjera da li je ureðaj spojen na mrežu*/
	private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
	
    
    /**Funkcija za dohvaæanje i slanje poataka na server
     * imeFunkcije : "loginPostback" za dohvaèanje login podataka*/
    private void accessWebServiceIzleti(String executeString, String imeFunkcije, Context context) {
        SoapAccessTaskIzleti task = new SoapAccessTaskIzleti(executeString, imeFunkcije, context);
        //passes values for the urls string array
        task.execute();
    }
    
    /**Funkcija koja se poziva iz SoapAccessTaskIzleti kod logiranja*/
    public void logInPostback(String result){
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
	     			  jsonInnerObject = jsonInnerObjectPodaci.getJSONObject("korisnici");
	     			  jsonMainNode = jsonInnerObject.optJSONArray("korisnik");
	     			  for(int i = 0; i<jsonMainNode.length();i++){
	     				 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
			     		 Integer id = Integer.parseInt(jsonChildNode.optString("@korisnik_id"));
	     				 if(id > 0){
	     					//Toast.makeText(context, Integer.toString(id), Toast.LENGTH_LONG).show();
	     					// do sometning with user data
	     					 Postavke.spremiPostavke("idKorisnika", id.toString(), _context);
	     					 
	     					 String imeKorisnika = jsonChildNode.optString("@naziv");
	     					 Postavke.spremiPostavke("imeKorisnika", imeKorisnika, _context);
	     					 
	     					 String idProdajnogMjesta = jsonChildNode.optString("@prodajno_mjesto_id");
	     					 Postavke.spremiPostavke("idProdajnogMjesta", idProdajnogMjesta, _context);
	     					 
	     					 String idNaplatnogUredjaja = jsonChildNode.optString("@naplatni_uredjaj_id");
	     					 Postavke.spremiPostavke("idNaplatnogUredjaja", idNaplatnogUredjaja, _context);
	     					 
	     					 String nazivProdajnogMjesta = jsonChildNode.optString("@naziv_prodajnog_mjesta");
	     					 Postavke.spremiPostavke("prodajnoMjesto", nazivProdajnogMjesta, _context);
	     					 
	     					 String isDozvoljenRacun = jsonChildNode.optString("@dozvoljen_racun");
	     					 Postavke.spremiPostavke("isDozvoljenRacun", isDozvoljenRacun, _context);
	     					 
	     					 String isDozvoljenaRezervacija = jsonChildNode.optString("@dozvoljena_rezervacija");
	     					 Postavke.spremiPostavke("isDozvoljenaRezervacija", isDozvoljenaRezervacija, _context);
	     					 
	     					 String isDozvoljenStorno = jsonChildNode.optString("@dozvoljen_storno");
	     					 Postavke.spremiPostavke("isDozvoljenStorno", isDozvoljenStorno, _context);
	     					 
	     					 Integer printer = Integer.parseInt(jsonChildNode.optString("@printanje"));
	     					 if (printer == 0){
	     						 Postavke.spremiPostavke("printer", "NA", _context);
	     					 }
	     					 /*if (printer == 1){
	     						 Postavke.spremiPostavke("printer", "CMP30", context);
	     					 }*/
	     					Intent mainActivityIntent = new Intent(_context, MainActivity.class);
	     		        	startActivity(mainActivityIntent);
	     					 
			     		   }
	     			  }
		     		  
	     		   }
  		   }
 		catch(JSONException e){
			  Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_LONG).show();
		  		}	
 		
 		//Toast.makeText(getApplicationContext(),"Ureðaj spojen na server...", Toast.LENGTH_SHORT).show();
    }
}
