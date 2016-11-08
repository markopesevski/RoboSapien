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
import java.util.UUID;

public class BTHelper extends AppCompatActivity
{
	private static BluetoothAdapter myBluetoothAdapter = null;
	private static BluetoothDevice myBluetoothDevice = null;
	private static BluetoothSocket myBluetoothSocket = null;
	private static boolean isDeviceSelected = false;
	private static boolean isConnected;
	public ArrayAdapter<String> myArrayAdapter = null;
	public ProgressDialog myProgress = null;
	public Context myContext = null;

	public BTHelper(Context context)
	{
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		myContext = context;
		isDeviceSelected = false;
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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	public void showPaired(ArrayAdapter<String> adaptadorLlista, ProgressDialog progress, BroadcastReceiver receiver)
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
			showToast("No hi ha dispositius emparellats");
		}
	}

	public void cancelDiscovery()
	{
		myBluetoothAdapter.cancelDiscovery();
	}

	// TODO perque peta aqui
	public void startSearching(BroadcastReceiver receiver, ArrayAdapter<String> listAdapter, ProgressDialog progress)
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
		if(isDeviceSelected)
		{
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
			// TODO veure perque no s'espera fins que s'ha connectat per amagar el ProgressDialog
			try
			{
				if (myBluetoothSocket == null || !isConnected)
				{
					myBluetoothSocket = myBluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUIDserie);
					publishProgress();
					myBluetoothSocket.connect();
					publishProgress();
					isConnected = true;
					publishProgress();
				}
			}
			catch (IOException e)
			{
				isConnected = false;
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... params)
		{
			myProgress.incrementProgressBy(10);
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
			myBluetoothSocket.close();
			myBluetoothSocket = null;
			isConnected = false;
			myBluetoothDevice = null;
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

	private void myShowDialog(String title, String msg)
	{
		final String tit = title;
		final String str = msg;
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					myProgress = ProgressDialog.show(myContext, tit, str);
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
		try
		{
			myBluetoothSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

