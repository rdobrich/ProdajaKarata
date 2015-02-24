package com.aktivainfo.prodajakarata;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;



public class PostavkeActivity extends Activity {
	private Context context;	
	
	public void onCreate(Bundle savedInstanceState)
	{	
		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postavke_layout);
		primjeniPostavke();
		
		Button spremi = (Button) findViewById(R.id.btnSavePrefs);
		spremi.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View v) {
	        	   EditText text = (EditText) findViewById(R.id.txtImeKorisnikaPrefs);
	        	   String vrijednost = text.getText().toString();
	        	   Postavke.spremiPostavke("imeKorisnika",vrijednost, context);
	        	   
	        	   text = (EditText) findViewById(R.id.txtProdajnoMjestoPrefs);
	        	   vrijednost = text.getText().toString();
	        	   Postavke.spremiPostavke("prodajnoMjesto",vrijednost, context);
	        	   
	        	   text = (EditText) findViewById(R.id.txtOznakaTvrtkePrefs);
	        	   vrijednost = text.getText().toString();
	        	   Postavke.spremiPostavke("oznakaTvrtke",vrijednost, context);
	        	   
	        	   text = (EditText) findViewById(R.id.txtSifraProdMjPrefs);
	        	   vrijednost = text.getText().toString();
	        	   Postavke.spremiPostavke("sifraProdajnogMjesta",vrijednost, context);
	        	   
	        	   text = (EditText) findViewById(R.id.txtSifraNapUredjaja);
	        	   vrijednost = text.getText().toString();
	        	   Postavke.spremiPostavke("sifraNaplatnogUredjaja",vrijednost, context);
	        	   
	        	   RadioButton printer = (RadioButton) findViewById(R.id.radioPrinterNA);
	        	   if(printer.isChecked()){
	        		   Postavke.spremiPostavke("printer","NA", context);
	        	   }
	        	   else{
	        		   printer = (RadioButton) findViewById(R.id.radioPrinterCMP30);
		        	   if(printer.isChecked()){
		        		   Postavke.spremiPostavke("printer","CMP30", context);
		        	   }
		        	   else{
		        		   printer = (RadioButton) findViewById(R.id.radioPrinterCitizenUSB);
			        	   if(printer.isChecked()){
			        		   Postavke.spremiPostavke("printer","CitizenUSB", context);
			        	   }
			        	   else{
			        		   printer = (RadioButton) findViewById(R.id.radioSPPR200);
				        	   if(printer.isChecked()){
				        		   Postavke.spremiPostavke("printer","BIX", context);
				        	   }
			        	   }
		        	   }
	        	   }
	        	   
	        	   CheckBox autoSpoji = (CheckBox) findViewById(R.id.chkAutoSpoji);
	        	   if(autoSpoji.isChecked()){
	        		   Postavke.spremiPostavke("autoSpajanje","1", context);
	        	   }
	        	   else{
	        		   Postavke.spremiPostavke("autoSpajanje","0", context);
	        	   }
	        	   
	        	   
	        	   //Intent mainActivityIntent = new Intent(context, MainActivity.class);
	        	   //startActivity(mainActivityIntent);
	        	   Intent LogInIntent = new Intent(context, LogIn.class);
	        	   startActivity(LogInIntent);
	           }});
		
		Button odustani = (Button) findViewById(R.id.btnCancelPrefs);
		odustani.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View v) {
	        	   Intent mainActivityIntent = new Intent(context, MainActivity.class);
	        	   mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        	   startActivity(mainActivityIntent);
	           }});
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
    
    private void primjeniPostavke(){
    	EditText text = (EditText) findViewById(R.id.txtImeKorisnikaPrefs);
        String naziv = Postavke.getPostavke(1, context);
        text.setText(naziv);
        
        text = (EditText) findViewById(R.id.txtProdajnoMjestoPrefs);
        naziv = Postavke.getPostavke(2, context);
        text.setText(naziv);
        
        text = (EditText) findViewById(R.id.txtOznakaTvrtkePrefs);
        naziv = Postavke.getPostavke(3, context);
        text.setText(naziv);
        
        text = (EditText) findViewById(R.id.txtSifraProdMjPrefs);
        naziv = Postavke.getPostavke(8, context);
        text.setText(naziv);
        
        text = (EditText) findViewById(R.id.txtSifraNapUredjaja);
        naziv = Postavke.getPostavke(9, context);
        text.setText(naziv);
        
        naziv=Postavke.getPostavke(4, context);
        
        if(naziv.equals("NA")){
        	RadioButton printer = (RadioButton) findViewById(R.id.radioPrinterNA);
        	printer.setChecked(true);
        }
        
        if(naziv.equals("CMP30")){
        	RadioButton printer = (RadioButton) findViewById(R.id.radioPrinterCMP30);
        	printer.setChecked(true);
        }
        
        if(naziv.equals("CitizenUSB")){
        	RadioButton printer = (RadioButton) findViewById(R.id.radioPrinterCitizenUSB);
        	printer.setChecked(true);
        }
        
        if(naziv.equals("BIX")){
        	RadioButton printer = (RadioButton) findViewById(R.id.radioSPPR200);
        	printer.setChecked(true);
        }
        
        naziv=Postavke.getPostavke(5, context);
        CheckBox autoSpoji = (CheckBox) findViewById(R.id.chkAutoSpoji);
        if(naziv.equals("1")){
        	autoSpoji.setChecked(true);
        }
    }
}
