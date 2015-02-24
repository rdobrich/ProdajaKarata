package com.aktivainfo.prodajakarata;

/**
 * Created by vjekoslav.mezdic on 20.09.13..
 */
import java.util.ArrayList;

import com.aktivainfo.prodajakarata.R.drawable;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VoznjaAdapter extends ArrayAdapter<Voznja> {

    private ArrayList<Voznja> objects;
    private int selectedIndex;

    /* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<Item> objects,
	* because it is the list of objects we want to display.
	*/

    public VoznjaAdapter(Context context, int textViewResourceId, ArrayList<Voznja> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;

    }
    public void setSelectedIndex(int ind)
    {
        selectedIndex = ind;
        notifyDataSetChanged();
    }

    /*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
    
    private class ViewHolder {
    	   TextView nazivVoznje;
    	   TextView datumVoznje;
    	   TextView vrijemeVoznje;
    	   TextView slobodnaMjesta;
    	   LinearLayout layout;
    	  }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	 
    	   ViewHolder holder = null;
    	   Log.v("ConvertView", String.valueOf(position));
    	   if (convertView == null) {
    	 
    	   LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
    	     Context.LAYOUT_INFLATER_SERVICE);
    	   convertView = vi.inflate(R.layout.voznja_item, null);
    	 
    	   holder = new ViewHolder();
    	   holder.nazivVoznje = (TextView) convertView.findViewById(R.id.lblNazivVoznje);
    	   holder.datumVoznje = (TextView) convertView.findViewById(R.id.lblDatumVoznje);
    	   holder.vrijemeVoznje = (TextView) convertView.findViewById(R.id.lblVrijemeVoznje);
    	   holder.slobodnaMjesta = (TextView) convertView.findViewById(R.id.lblSlobodnaMjesta);
    	   holder.layout = (LinearLayout) convertView.findViewById(R.id.voznjeLayout);
    	   convertView.setTag(holder);
    	 
    	   } else {
    	    holder = (ViewHolder) convertView.getTag();
    	   }
    	   
    	   if(selectedIndex!= -1 && position == selectedIndex)
           {
               holder.layout.setBackgroundResource(drawable.row_shape_pressed);
           }
           else
           {
               holder.layout.setBackgroundResource(drawable.row_shape);
           }
    	 
    	   Voznja voznja = objects.get(position);
    	   holder.nazivVoznje.setText(voznja.getImeVoznje());
    	   holder.datumVoznje.setText(voznja.getDatumVoznje());
    	   holder.vrijemeVoznje.setText(voznja.getVrijemeVoznje());
    	   holder.slobodnaMjesta.setText(voznja.getSlobodnaMjesta());
    	 
    	   return convertView;
    	 
    	  }
    	 }
