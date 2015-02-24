package com.aktivainfo.prodajakarata;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertView
{	
	public static void showError(String message, Context ctx)
	{
		showAlert("Pogreška!", message, ctx);
	}
	
	public static void showAlert(String message, Context ctx)
	{
		showAlert("Upozorenje!", message, ctx);
	}
	
	public static void showAlert(String title, String message, Context ctx)
	{
		//Create a builder	
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(message);
		//add buttons and listener
		EmptyListener pl = new EmptyListener();
		builder.setPositiveButton("U redu", pl);
		//Create the dialog
		AlertDialog ad = builder.create();
		//show
		ad.show();	
	}
	
	public static void LogOutQuestion(String title, String message, Context ctx)
	{
		//Create a builder	
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title); 
		builder.setMessage(message);
		//add buttons and listener
		EmptyListener pl = new EmptyListener();
		builder.setNeutralButton("Odustani", pl);
		LogOutPositive lop = new LogOutPositive(ctx);
		builder.setPositiveButton("U redu", lop);
		//Create the dialog
		AlertDialog ad = builder.create();
		//show
		ad.show();
	}
}

class EmptyListener implements android.content.DialogInterface.OnClickListener 
{
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
	}	
}

class LogOutPositive implements android.content.DialogInterface.OnClickListener 
{
	private Context context;
	LogOutPositive(Context context){
		this.context = context;
	}
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		 MainActivity ma = (MainActivity) context;
		 ma.goToLogIn();
	}	
}
