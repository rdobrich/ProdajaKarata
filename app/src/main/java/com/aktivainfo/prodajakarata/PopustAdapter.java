package com.aktivainfo.prodajakarata;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PopustAdapter extends ArrayAdapter<Popust> {

    private ArrayList<Popust> objects;

    /* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<Item> objects,
	* because it is the list of objects we want to display.
	*/

    public PopustAdapter(Context context, int textViewResourceId, ArrayList<Popust> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;

    }

    /*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
    
    private class ViewHolder {
    	   TextView iznosPopusta;
    	   //TextView idMjesta;
    	  }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	 
    	   ViewHolder holder = null;
    	   Log.v("ConvertView", String.valueOf(position));
    	   if (convertView == null) {
    	 
    	   LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
    	     Context.LAYOUT_INFLATER_SERVICE);
    	   convertView = vi.inflate(R.layout.mjesto_item, null);
    	 
    	   holder = new ViewHolder();
    	   holder.iznosPopusta = (TextView) convertView.findViewById(R.id.lblNazivMjestoItem);
    	   //holder.idMjesta = (TextView) convertView.findViewById(R.id.lblLinijaIdItem);
    	   
    	   convertView.setTag(holder);
    	 
    	   } else {
    	    holder = (ViewHolder) convertView.getTag();
    	   }
    	 
    	   Popust popust = objects.get(position);
    	   holder.iznosPopusta.setText(popust.getIznosPopusta().toString());
    	   //holder.idMjesta.setText(linija.getId().toString());
    	 
    	   return convertView;
    	 
    	  }
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
   	 
	 	   ViewHolder holder = null;
	 	   Log.v("ConvertView", String.valueOf(position));
	 	   if (convertView == null) {
	 	 
	 	   LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
	 	     Context.LAYOUT_INFLATER_SERVICE);
	 	   convertView = vi.inflate(R.layout.mjesto_item, null);
	 	 
	 	   holder = new ViewHolder();
	 	   holder.iznosPopusta = (TextView) convertView.findViewById(R.id.lblNazivMjestoItem);
	 	   //holder.idMjesta = (TextView) convertView.findViewById(R.id.lblLinijaIdItem);
	 	   
	 	   convertView.setTag(holder);
	 	 
	 	   } else {
	 	    holder = (ViewHolder) convertView.getTag();
	 	   }
	 	 
	 	  Popust popust = objects.get(position);
	 	  holder.iznosPopusta.setText(popust.getIznosPopusta().toString());
	 	  //holder.idMjesta.setText(linija.getId().toString());
   	 
	 	  return convertView;
 	 
 	  }
    
}

