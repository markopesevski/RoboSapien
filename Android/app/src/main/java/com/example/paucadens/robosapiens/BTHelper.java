package com.example.paucadens.robosapiens;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BTHelper extends AppCompatActivity
{
	private static BluetoothAdapter myBluetoothAdapter = null;
	private static BluetoothDevice myBluetoothDevice = null;
	private static BluetoothSocket myBluetoothSocket = null;
	private static boolean isDeviceSelected = false;
	private static boolean isConnected;
	private ProgressDialog myProgress = null;
	private Context myContext = null;
	private static final BroadcastReceiver myReceiver = null;

	public BTHelper()
	{
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		checkBTEnabled();
	}

	public BTHelper(Context context)
	{
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		checkBTEnabled();
		myContext = context;
		isDeviceSelected = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	public void checkBTEnabled()
	{
		if (myBluetoothAdapter == null)
		{
			showToast("No hi ha Bluetooth en aquest dispositiu");
		}
		else
		{
			if (!myBluetoothAdapter.isEnabled())
			{
				myBluetoothAdapter.enable();
			}
		}
	}

	public void disableBT()
	{
		myBluetoothAdapter.disable();
	}

	public void showPaired(ArrayAdapter<String> adaptadorLlista, ProgressDialog progress)
	{
		adaptadorLlista.clear();
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
			showToast("No hi ha dispositius emparellats");
		}
	}

	public void cancelDiscovery()
	{
		myBluetoothAdapter.cancelDiscovery();
	}

	public void startSearching()
	{
		myBluetoothAdapter.startDiscovery();
	}

	public void setSelectedBTDevice(String deviceMAC)
	{
		myBluetoothDevice = myBluetoothAdapter.getRemoteDevice(deviceMAC);
		isDeviceSelected = true;
	}

	public void openSocket()
	{
		/* sanity check */
		if(!isConnected &&
			isDeviceSelected &&
			myBluetoothAdapter != null &&
			myBluetoothDevice != null &&
			myBluetoothSocket == null)
		{
			TimerTask task = new TimerTask()
			{
				@Override
				public void run()
				{
					if(myProgress != null)
					{
						myProgress.dismiss();
					}
				}
			};

			Timer timer = new Timer();
			timer.schedule(task, 500);

			new connectToSocket().execute();

		}
		else
		{
			showToast("Device not selected!");
		}
	}

	private class connectToSocket extends AsyncTask<Void, Void, Void>
	{
		private final UUID UUIDserie = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

		@Override
		protected void onPreExecute()
		{
			myProgress = ProgressDialog.show(myContext, "Connectant...", "Espera");
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			try
			{
				if (myBluetoothSocket == null || !isConnected)
				{
					myBluetoothSocket = myBluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUIDserie);
					myBluetoothSocket.connect();
					isConnected = true;
				}
			}
			catch (IOException e)
			{
				isConnected = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void params) {
			if(isConnected)
			{
				myProgress.dismiss();
			}
			else
			{
				showToast("Error al connectar!");
			}
		}
	}

	public void sendString(String str, String errMsg)
	{
		if (myBluetoothSocket != null && isConnected)
		{
			try
			{
				myBluetoothSocket.getOutputStream().write(str.getBytes());
			}
			catch (IOException e)
			{
				showToast("Error sending: " + errMsg);
			}
		}
	}

	public void disconnect()
	{
		try
		{
			if(myBluetoothSocket != null)
			{
				myBluetoothSocket.close();
				myBluetoothSocket = null;
			}
			if(myBluetoothDevice != null)
			{
				myBluetoothDevice = null;
			}
			if(myProgress != null)
			{
				myProgress.dismiss();
				myProgress = null;
			}
			isConnected = false;
			isDeviceSelected = false;
			showToast("Disconnected");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			showToast("Error closing socket!");
		}
	}

	private void showToast(String msg)
	{
		final String str = msg;
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Toast.makeText(myContext, str, Toast.LENGTH_LONG).show();
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
		try
		{
			unregisterReceiver(myReceiver);
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		disconnect();
		super.onDestroy();
	}
}

