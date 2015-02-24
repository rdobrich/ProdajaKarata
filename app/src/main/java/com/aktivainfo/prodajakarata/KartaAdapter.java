package com.aktivainfo.prodajakarata;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class KartaAdapter extends ArrayAdapter<Karta> {

    private ArrayList<Karta> objects;

    /* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<Item> objects,
	* because it is the list of objects we want to display.
	*/

    public KartaAdapter(Context context, int textViewResourceId, ArrayList<Karta> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;

    }

    /*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
    
    private class ViewHolder {
    	   TextView tipKarte;
    	   TextView cijenaKarte;
    	   TextView brojKarata;
    	  }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	 
    	   ViewHolder holder = null;
    	   Log.v("ConvertView", String.valueOf(position));
    	   if (convertView == null) {
    	 
    	   LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
    	     Context.LAYOUT_INFLATER_SERVICE);
    	   convertView = vi.inflate(R.layout.karta_item, null);
    	 
    	   holder = new ViewHolder();
    	   holder.tipKarte = (TextView) convertView.findViewById(R.id.lblTipKarte);
    	   holder.cijenaKarte = (TextView) convertView.findViewById(R.id.lblCijenaKarte);
    	   holder.brojKarata = (TextView) convertView.findViewById(R.id.lblBrojKarata);
    	   
    	   convertView.setTag(holder);
    	 
    	   } else {
    	    holder = (ViewHolder) convertView.getTag();
    	   }
    	 
    	   Karta karta = objects.get(position);
    	   holder.tipKarte.setText(karta.getTipKarte());
    	   holder.cijenaKarte.setText("(" + karta.getCijenaKarte() + " kn)");
    	   holder.brojKarata.setText(karta.getBrojKarata());
    	 
    	   return convertView;
    	 
    	  }
    	 }


