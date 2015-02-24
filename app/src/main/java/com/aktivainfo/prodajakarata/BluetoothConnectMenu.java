package com.aktivainfo.prodajakarata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

//import com.citizen.app.assist.AlertView;
import com.citizen.port.android.BluetoothPort;
import com.citizen.request.android.RequestHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BluetoothConnectMenu  extends Activity
{
	private static final String TAG = "BluetoothConnectMenu";
	// Intent request codes
	//private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 1;

	ArrayAdapter<String> adapter;
	private BluetoothAdapter mBluetoothAdapter;
	private Vector<BluetoothDevice> remoteDevices;
	private BroadcastReceiver searchFinish;
	private BroadcastReceiver searchStart;
	private BroadcastReceiver discoveryResult;
	private Thread hThread;
	private Context context;
	private String _printer;
	// UI
	private EditText btAddrBox;
	private Button connectButton;
	private Button searchButton;
	private ListView list;
	// BT
	private BluetoothPort bluetoothPort;
	
	/**Set up Bluetooth.*/
	private void bluetoothSetup()
	{
		// Initialize 
		clearBtDevData();
		bluetoothPort = BluetoothPort.getInstance();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) 
		{
		    // Device does not support Bluetooth
			return;
		}
		if (!mBluetoothAdapter.isEnabled()) 
		{
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); 
		}	
	}
	
	private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "//temp";
	private static final String fileName = dir + "//BTPrinter";
	private String lastConnAddr = "";
	private void loadSettingFile()
	{
		int rin = 0;
		char [] buf = new char[128];
		try
		{	
			FileReader fReader = new FileReader(fileName);
			rin = fReader.read(buf);
			if(rin > 0)
			{
				lastConnAddr = new String(buf,0,rin);
				btAddrBox.setText(lastConnAddr);
				btConn(mBluetoothAdapter.getRemoteDevice(btAddrBox.getText().toString()));
				
			}
			fReader.close();
		}
		catch (FileNotFoundException e)
		{
			Log.i(TAG, "Connection history not exists.");
		}
		catch (IOException e)
		{
			Log.e(TAG, e.getMessage(), e);
		}	
	}
	
	private void saveSettingFile()
	{
		try
		{
			File tempDir = new File(dir);
			if(!tempDir.exists())
			{
				tempDir.mkdir();
			}
			FileWriter fWriter = new FileWriter(fileName);
			if(lastConnAddr != null)
				fWriter.write(lastConnAddr);
			fWriter.close();
			Postavke.spremiPostavke("btAddr", lastConnAddr, context);
		}
		catch (FileNotFoundException e)
		{
			Log.e(TAG, e.getMessage(), e);
		}
		catch (IOException e)
		{
			Log.e(TAG, e.getMessage(), e);
		}	
	}
	
	/** clear device data used list.*/
	private void clearBtDevData()
	{
		remoteDevices = new Vector<BluetoothDevice>();
	}	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_menu);
		context = this;
		// Setting
		btAddrBox = (EditText) findViewById(R.id.EditTextAddressBT);
		connectButton = (Button) findViewById(R.id.ButtonConnectBT);
		searchButton = (Button) findViewById(R.id.ButtonSearchBT);
		_printer = Postavke.getPostavke(4, context);
		list = (ListView) findViewById(R.id.ListView01);	
		bluetoothSetup();
		// Setting
		loadSettingFile();

		
		// Connect, Disconnect -- Button
		connectButton.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				if(!bluetoothPort.isConnected()) // Connect routine.
				{
					try
					{
						btConn(mBluetoothAdapter.getRemoteDevice(btAddrBox.getText().toString()));
					}
					catch(IllegalArgumentException e)
					{
						// Bluetooth Address Format [OO:OO:OO:OO:OO:OO]
						Log.e(TAG,e.getMessage(),e);
						AlertView.showAlert(e.getMessage(), context);
						return;	
					}
					catch (IOException e)
					{
						Log.e(TAG,e.getMessage(),e);
						AlertView.showAlert(e.getMessage(), context);
						return;
					}
				}
				else // Disconnect routine.
				{
					// Always run. 
					btDisconn();
				}
			}
		});	
		
		Button back = (Button) findViewById(R.id.btnReturnToMain);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{	if(_printer.equals("BIX")){
					btDisconn();
					Intent myIntent = new Intent(context, MainActivity.class);
		        	startActivity(myIntent);
				}
				else{
					Intent myIntent = new Intent(context, MainActivity.class);
					myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		        	   //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        	startActivity(myIntent);
				}
			}});		
		// Search Button
		searchButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!mBluetoothAdapter.isDiscovering())
				{	
					clearBtDevData();
					adapter.clear();
					mBluetoothAdapter.startDiscovery();	
				}
				else
				{	
					mBluetoothAdapter.cancelDiscovery();
				}
			}
		});				
		// Bluetooth Device List
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		list.setAdapter(adapter);
		//addPairedDevices();
		// Connect - click the List item.
		list.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				BluetoothDevice btDev = remoteDevices.elementAt(arg2);
				try
				{
					if(mBluetoothAdapter.isDiscovering())
					{
						mBluetoothAdapter.cancelDiscovery();
					}
					btAddrBox.setText(btDev.getAddress());
					btConn(btDev);
				}
				catch (Exception e)
				{
					AlertView.showAlert(e.getMessage(), context);
					return;
				}
			}
		});
		
		// UI - Event Handler.
		// Search device, then add List.
		discoveryResult = new BroadcastReceiver() 
		{
			@Override
			public void onReceive(Context context, Intent intent) 
			{
				String key;
				BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if(remoteDevice != null)
				{
					if(remoteDevice.getBondState() != BluetoothDevice.BOND_BONDED)
					{
						key = remoteDevice.getName() +"\n["+remoteDevice.getAddress()+"]";
					}
					else
					{
						key = remoteDevice.getName() +"\n["+remoteDevice.getAddress()+"] [Paired]";
					}
					remoteDevices.add(remoteDevice);
					adapter.add(key);
				}
			}
		};
		registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		searchStart = new BroadcastReceiver() 
		{
			@Override
			public void onReceive(Context context, Intent intent) 
			{
				connectButton.setEnabled(false);
				btAddrBox.setEnabled(false);
				searchButton.setText("Trazi");
			}
		};
		registerReceiver(searchStart, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
		searchFinish = new BroadcastReceiver() 
		{
			@Override
			public void onReceive(Context context, Intent intent) 
			{
				connectButton.setEnabled(true);
				btAddrBox.setEnabled(true);
				searchButton.setText("Trazi");				
			}
		};
		registerReceiver(searchFinish, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
	}
	
	
	@Override
	protected void onResume()
    {
       super.onResume();
       bluetoothSetup();
       
    }
	
	@Override
	protected void onDestroy()
	{
		try
		{
			saveSettingFile();
			bluetoothPort.disconnect();
		}
		catch (IOException e)
		{
			Log.e(TAG, e.getMessage(), e);
		}
		catch (InterruptedException e)
		{
			Log.e(TAG, e.getMessage(), e);
		}
		if((hThread != null) && (hThread.isAlive()))
		{
			hThread.interrupt();
			hThread = null;
		}	
		unregisterReceiver(searchFinish);
		unregisterReceiver(searchStart);
		unregisterReceiver(discoveryResult);
		super.onDestroy();
	}
	
	private connTask connectionTask;
	
	/**Bluetooth Connection method.*/
	private void btConn(final BluetoothDevice btDev) throws IOException
	{
		if((connectionTask != null) && (connectionTask.getStatus() == AsyncTask.Status.RUNNING))
		{
			connectionTask.cancel(true);
			if(!connectionTask.isCancelled())
				connectionTask.cancel(true);
			connectionTask = null;
		}
		connectionTask = new connTask();
		connectionTask.execute(btDev);
	}
	/** Bluetooth Disconnection method.*/
	private void btDisconn()
	{
		try
		{
			bluetoothPort.disconnect();
		}
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage(), e);
		}
		if((hThread != null) && (hThread.isAlive()))
			hThread.interrupt();
		// UI
		connectButton.setText("Spajanje");
		list.setEnabled(true);
		btAddrBox.setEnabled(true);
		searchButton.setEnabled(true);
		Toast toast = Toast.makeText(context, "Bluetooth veza prekinuta", Toast.LENGTH_SHORT);
		toast.show();
	}
	
	/**Bluetooth Connection Task.*/
	class connTask extends AsyncTask<BluetoothDevice, Void, Integer>
	{
		private final ProgressDialog dialog = new ProgressDialog(BluetoothConnectMenu.this);
		
		@Override
		protected void onPreExecute()
		{
			dialog.setTitle("Bluetooth");
			dialog.setMessage("Spajanje...");
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(BluetoothDevice... params)
		{
			Integer retVal = null;
			try
			{
				bluetoothPort.connect(params[0]);
				lastConnAddr = params[0].getAddress();
				retVal = Integer.valueOf(0);
			}
			catch (IOException e)
			{
				retVal = Integer.valueOf(-1);
			}
			return retVal;
		}
		
		@Override
		protected void onPostExecute(Integer result)
		{
			if(result.intValue() == 0)	// Connection success.
			{
				RequestHandler rh = new RequestHandler();				
				hThread = new Thread(rh);
				hThread.start();
				// UI
				connectButton.setText("Prekini vezu");
				list.setEnabled(false);
				btAddrBox.setEnabled(false);
				searchButton.setEnabled(false);
				if(dialog.isShowing())
					dialog.dismiss();				
				Toast toast = Toast.makeText(context, "Bluetooth spojen", Toast.LENGTH_SHORT);
				toast.show();
				saveSettingFile();
				if(_printer.equals("BIX")){
					btDisconn();
					Intent myIntent = new Intent(context, MainActivity.class);
		        	startActivity(myIntent);
				}
				else{
					Intent myIntent = new Intent(context, MainActivity.class);
					myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		        	   //myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        	startActivity(myIntent);
				}
			}
			else	// Connection failed.
			{
				if(dialog.isShowing())
					dialog.dismiss();				
				AlertView.showAlert("Neuspje�no spajanje preko bluetootha",
									"Provjerite status ure�aja i bluetooth postavke", context);
			}
			super.onPostExecute(result);
		}
	}

	
}
	 


