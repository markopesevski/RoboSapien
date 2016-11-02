package com.example.paucadens.robosapiens;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class BTHelper extends AppCompatActivity
{
	private static BluetoothAdapter myBluetoothAdapter = null;
	private static ArrayAdapter<String> myArrayAdapter = null;
	private static ProgressDialog myProgress = null;
	private static BluetoothDevice myBluetoothDevice = null;
	private static BluetoothSocket myBluetoothSocket = null;
	private static boolean isDeviceSelected = false;
	private static boolean isConnected = false;

	public BTHelper(Context context)
	{
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (myBluetoothAdapter == null)
		{
			showToast(context, "No hi ha Bluetooth en aquest dispositiu");
		}
		else
		{
			if (!myBluetoothAdapter.isEnabled())
			{
				// TODO perque peta aqui
				Intent activateBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(activateBT, 1);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	public void showPaired(Context context, ArrayAdapter<String> adaptadorLlista, ProgressDialog progress)
	{
		myArrayAdapter = adaptadorLlista;
		myArrayAdapter.clear();
		myProgress = progress;

		if (myBluetoothAdapter.getBondedDevices().size() > 0)
		{
			for(BluetoothDevice device : myBluetoothAdapter.getBondedDevices())
			{
				adaptadorLlista.add(device.getName() + "\n" + device.getAddress());
			}
		}
		else
		{
			showToast(context, "No hi ha dispositius emparellats");
		}
	}

	public void cancelDiscovery()
	{
		myBluetoothAdapter.cancelDiscovery();
	}

	// TODO perque peta aqui
	public void searchDevices(ArrayAdapter<String> listAdapter, ProgressDialog progress)
	{
		myArrayAdapter = listAdapter;
		myProgress = progress;

		IntentFilter myFilter = new IntentFilter();
		myFilter.addAction(BluetoothDevice.ACTION_FOUND);
		myFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		myFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(myReceiver, myFilter);
		myBluetoothAdapter.startDiscovery();
	}

	private final BroadcastReceiver myReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action))
			{
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				myArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
			else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
			{
				myProgress = ProgressDialog.show(getApplicationContext(), "Buscant...", "Espera");
			}
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
			{
				myProgress.dismiss();
			}
		}
	};

	public void setSelectedBTDevice(String deviceMAC)
	{
		myBluetoothDevice = myBluetoothAdapter.getRemoteDevice(deviceMAC);
		isDeviceSelected = true;
	}

	public void openSocket(Context context)
	{
		if(isDeviceSelected)
		{
			new connectToSocket().execute(context);
		}
		else
		{
			showToast(context, "Device not selected!");
		}
	}

	private class connectToSocket extends AsyncTask<Context, Void, Void>
	{
		private final UUID UUIDserie = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

		@Override
		protected Void doInBackground(Context... context)
		{
			// TODO veure perque no s'espera fins que s'ha connectat per amagar el ProgressDialog
			myShowDialog(context[0], "Connectant...", "Espera");
			try
			{
				if (myBluetoothSocket == null || !isConnected)
				{
					myBluetoothSocket = myBluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUIDserie);
					myBluetoothSocket.connect();
				}
				isConnected = true;
			}
			catch (IOException e)
			{
				isConnected = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void params) {
			while(!isConnected)
			{
			}
			if(isConnected)
			{
				dismissDialog();
			}
		}
	}

	public void sendString(Context context, String str, String errMsg)
	{
		if (myBluetoothSocket != null && isConnected)
		{
			try
			{
				myBluetoothSocket.getOutputStream().write(str.getBytes());
			}
			catch (IOException e)
			{
				showToast(context, "Error sending: " + errMsg);
			}
		}
	}

	public void disconnect(Context context)
	{
		try
		{
			myBluetoothSocket.close();
			myBluetoothSocket = null;
			isConnected = false;
			myBluetoothDevice = null;
			isDeviceSelected = false;
			showToast(context, "Disconnected");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			showToast(context, "Error closing socket!");
		}
	}

	private void showToast(Context context, String msg)
	{
		final String str = msg;
		final Context where = context;
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Toast.makeText(where, str, Toast.LENGTH_LONG).show();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void myShowDialog(Context context, String title, String msg)
	{
		final Context where = context;
		final String tit = title;
		final String str = msg;
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					myProgress = ProgressDialog.show(where, tit, str);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void dismissDialog()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					myProgress.dismiss();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(myReceiver);
	}
}

