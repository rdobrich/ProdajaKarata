package com.aktivainfo.prodajakarata;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RacunAdapter extends ArrayAdapter<Racun> {

    private ArrayList<Racun> objects;

    /* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<Item> objects,
	* because it is the list of objects we want to display.
	*/

    public RacunAdapter(Context context, int textViewResourceId, ArrayList<Racun> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;

    }

    /*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
    
    private class ViewHolder {
    	   TextView VrijemeRacuna;
    	   TextView BrojRacuna;
    	   TextView Izlet;
    	   TextView Karte;
    	   TextView Meniji;
    	   //TextView idMjesta;
    	  }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	 
    	   ViewHolder holder = null;
    	   Log.v("ConvertView", String.valueOf(position));
    	   if (convertView == null) {
    	 
    	   LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
    	     Context.LAYOUT_INFLATER_SERVICE);
    	   convertView = vi.inflate(R.layout.racuni_item, null);
    	 
    	   holder = new ViewHolder();
    	   holder.VrijemeRacuna = (TextView) convertView.findViewById(R.id.lblDatumRacuna);
    	   holder.BrojRacuna = (TextView) convertView.findViewById(R.id.lblBrojRacuna);
    	   holder.Izlet = (TextView) convertView.findViewById(R.id.lblIzlet);
    	   holder.Karte = (TextView) convertView.findViewById(R.id.lblKarte);
    	   holder.Meniji = (TextView) convertView.findViewById(R.id.lblMeniji);
    	   //holder.idMjesta = (TextView) convertView.findViewById(R.id.lblLinijaIdItem);
    	   
    	   convertView.setTag(holder);
    	 
    	   } else {
    	    holder = (ViewHolder) convertView.getTag();
    	   }
    	 
    	   Racun racun = objects.get(position);
    	   holder.VrijemeRacuna.setText(racun.getVrijemeRacuna().toString());
    	   holder.BrojRacuna.setText(racun.getBrojRacuna().toString());
    	   holder.Izlet.setText(racun.getIzlet().toString());
    	   holder.Karte.setText(racun.getKarte().toString());
    	   holder.Meniji.setText(racun.getMeniji().toString());
    	   //holder.idMjesta.setText(linija.getId().toString());
    	 
    	   return convertView;
    	 
    	  }
    
}
