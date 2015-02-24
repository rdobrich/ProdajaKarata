package com.aktivainfo.prodajakarata;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class UplataAdapter extends ArrayAdapter<Uplata> {

    private ArrayList<Uplata> objects;

    /* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<Item> objects,
	* because it is the list of objects we want to display.
	*/

    public UplataAdapter(Context context, int textViewResourceId, ArrayList<Uplata> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;

    }

    /*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
    
    private class ViewHolder {
    	   TextView nacinPlacanja;
    	   TextView uplata;
    	   TextView isplata;
    	   TextView iznos;
    	  }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	 
    	   ViewHolder holder = null;
    	   Log.v("ConvertView", String.valueOf(position));
    	   if (convertView == null) {
    	 
    	   LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
    	     Context.LAYOUT_INFLATER_SERVICE);
    	   convertView = vi.inflate(R.layout.nacinplacanja_item, null);
    	 
    	   holder = new ViewHolder();
    	   holder.nacinPlacanja = (TextView) convertView.findViewById(R.id.lblNacinPlacanja);
    	   holder.uplata = (TextView) convertView.findViewById(R.id.lblUplata);
    	   holder.isplata = (TextView) convertView.findViewById(R.id.lblIsplata);
    	   holder.iznos = (TextView) convertView.findViewById(R.id.lblUplaceniIznos);
    	   
    	   convertView.setTag(holder);
    	 
    	   } else {
    	    holder = (ViewHolder) convertView.getTag();
    	   }
    	 
    	   Uplata uplata = objects.get(position);
    	   holder.nacinPlacanja.setText(uplata.getNacinPlacanja());
    	   holder.uplata.setText(uplata.getUplata().toString());
    	   holder.isplata.setText(uplata.getIsplata().toString());
    	   holder.iznos.setText(uplata.getIznos().toString());
    	 
    	   return convertView;
    	 
    	  }
    	 }