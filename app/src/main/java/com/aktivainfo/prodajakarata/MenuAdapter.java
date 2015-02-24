package com.aktivainfo.prodajakarata;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<Meni> {

    private ArrayList<Meni> objects;

    /* here we must override the constructor for ArrayAdapter
	* the only variable we care about now is ArrayList<Item> objects,
	* because it is the list of objects we want to display.
	*/

    public MenuAdapter(Context context, int textViewResourceId, ArrayList<Meni> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;

    }

    /*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
    
    private class ViewHolder {
    	   TextView nazivMenu;
    	   TextView kolicinaMenu;
    	   TextView cijenaMenu;
    	  }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	 
    	   ViewHolder holder = null;
    	   Log.v("ConvertView", String.valueOf(position));
    	   if (convertView == null) {
    	 
    	   LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
    	     Context.LAYOUT_INFLATER_SERVICE);
    	   convertView = vi.inflate(R.layout.menu_item, null);
    	 
    	   holder = new ViewHolder();
    	   holder.nazivMenu = (TextView) convertView.findViewById(R.id.lblNazivMenija);
    	   holder.kolicinaMenu = (TextView) convertView.findViewById(R.id.lblBrojMenija);
    	   holder.cijenaMenu = (TextView) convertView.findViewById(R.id.lblCijenaMenija);
    	   convertView.setTag(holder);
    	 
    	   } else {
    	    holder = (ViewHolder) convertView.getTag();
    	   }
    	 
    	   Meni menu = objects.get(position);
    	   holder.nazivMenu.setText(menu.getNazivMenija());
    	   holder.kolicinaMenu.setText(menu.getKolicina());
    	   holder.cijenaMenu.setText("(" + menu.getCijena() + " kn)");
    	 
    	   return convertView;
    	 
    	  }
    	 }


