package com.aktivainfo.prodajakarata;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**Korisnièke postavke*/
public class Postavke {

/**Funkcija koja sprema postavke pod odreðenim nazivom i danom vrijednosti*/
public static void spremiPostavke(String naziv, String vrijednost, Context ctx){
	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
	SharedPreferences.Editor editor = settings.edit();
	//editor.clear();
	editor.putString(naziv, vrijednost);
	editor.commit();	
}

/**Funkcija za dohvaæanje podataka iz postavki
 *0 - id korinika, 1 - ime korisnika, 2 - prodajno mjesto, 3 - oznaka tvrtke, 4- tip printera (NA - nema printera, CMP30), 5 - autospajanje, 6 - idProdajnogMjesta
 *7 - id nap. uredjaja, 8 - sifra prod mjesta, 9 - sifra nap uredjaja, 10 - dozvoljen racun, 11 - dozvoljena rezervacija
 *12 - dozvoljen storno, 13 - printer mac adresa*/
public static String getPostavke(int postavka, Context ctx){
	String naziv;
	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
	switch(postavka){
		case 0:
			naziv = settings.getString("idKorisnika", "");
			break;
    	case 1:
    	    naziv = settings.getString("imeKorisnika", "");
    	    break;
	    case 2:
		    naziv = settings.getString("prodajnoMjesto", "");
		    break;
	    case 3:
		    naziv = settings.getString("oznakaTvrtke", "");
		    break;
	    case 4:
		    naziv = settings.getString("printer", "BIX");
		    break;
	    case 5:
		    naziv = settings.getString("autoSpajanje", "0");
		    break;
	    case 6:
		    naziv = settings.getString("idProdajnogMjesta", "0");
		    break;
	    case 7:
		    naziv = settings.getString("idNaplatnogUredjaja", "0");
		    break;
	    case 8:
		    naziv = settings.getString("sifraProdajnogMjesta", "");
		    break;
	    case 9:
		    naziv = settings.getString("sifraNaplatnogUredjaja", "");
		    break;
	    case 10:
		    naziv = settings.getString("isDozvoljenRacun", "0");
		    break;
	    case 11:
		    naziv = settings.getString("isDozvoljenaRezervacija", "0");
		    break;
	    case 12:
		    naziv = settings.getString("isDozvoljenStorno", "0");
		    break;
	    case 13:
		    naziv = settings.getString("btAddr", "");
		    break;
		default:
			naziv = "";
			break;
	}
	return naziv;
}
}
