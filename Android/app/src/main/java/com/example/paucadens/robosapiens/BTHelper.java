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
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;

import static com.example.paucadens.robosapiens.Moviment.btSocket;

public class BTHelper extends AppCompatActivity
{
	private static BluetoothAdapter myBluetoothAdapter = null;
	private static ArrayAdapter<String> myAdapter;
	private static ProgressDialog myProgress;
	private static BluetoothDevice myBluetoothDevice = null;
	public static BluetoothSocket myBluetoothSocket = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (myBluetoothAdapter == null)
		{
			Toast.makeText(getApplicationContext(),"No hi ha Bluetooth en aquest dispositiu", Toast.LENGTH_LONG).show();
		}
		else
		{
			if (!myBluetoothAdapter.isEnabled())
			{
				Intent ActivaBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(ActivaBT,1);
			}
		}
	}

	public void searchDevices(ArrayAdapter<String> adaptadorLlista, ProgressDialog progres)
	{
		myAdapter = adaptadorLlista;
		myProgress = progres;

		// Register the BroadcastReceiver
		IntentFilter myFilter = new IntentFilter();
		myFilter.addAction(BluetoothDevice.ACTION_FOUND);
		myFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		myFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(myReceiver, myFilter); // Don't forget to unregister during onDestroy
		myBluetoothAdapter.startDiscovery();
	}

	public void showPaired(ArrayAdapter<String> adaptadorLlista)
	{
		myAdapter = adaptadorLlista;
		Set<BluetoothDevice> dispemparellats;
		myAdapter.clear();
		if (myBluetoothAdapter.getBondedDevices().size()>0)
		{
			int tamany = myBluetoothAdapter.getBondedDevices().size();
			int i = 0;
			for (i = 0; i < tamany; i++)
			{
				adaptadorLlista.add(myBluetoothDevice.getName() + "\n" + myBluetoothDevice.getAddress());
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "No hi ha dispositius emparellats", Toast.LENGTH_LONG).show();
		}
	}

	public void cancelDiscovery()
	{
		myBluetoothAdapter.cancelDiscovery();
	}

	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver myReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action))
			{
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a ListView
				myAdapter.add(device.getName() + "\n" + device.getAddress());
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

	public void openSocket(ProgressDialog progress)
	{
		new connectToSocket(progress).execute;
	}

	private class connectToSocket extends AsyncTask<ProgressDialog, Void, ProgressDialog>
	{
		private boolean isConnected = true;

		@Override
		protected void onPreExecute(ProgressDialog... progress)
		{
			progress.show(getApplicationContext(), "Connectant...", "Espera");
		}

		@Override
		protected Void doInBackground(ProgressDialog... progress)
		{
			try
			{
				if (myBluetoothSocket == null || !isConnected)
				{
					BluetoothDevice disp = miBT.getRemoteDevice(dirBT);
					btSocket = disp.createInsecureRfcommSocketToServiceRecord(UUIDserie);
					btSocket.connect();
				}
			}
			catch (IOException e)
			{
				isConnected = false;
				finish();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ProgressDialog... progress)
		{
			progress.dismiss();
			if (!isConnected)
			{
				showToast("Error de connexió!");
			}
			else
			{
				showToast("Connectat");
			}
		}

		@Override
		protected ProgressDialog doInBackground(ProgressDialog... params) {
			return null;
		}
	}

	public void sendOnSocket(BluetoothSocket socket, String str)
	{
		if (socket != null)
		{
			try
			{
				socket.getOutputStream().write(str.getBytes());
			}
			catch (IOException e)
			{
				showToast("Error writing" + str);
			}
		}
	}

	public void showToast(String msg)
	{
		final String str = msg;
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
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

