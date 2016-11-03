package com.example.paucadens.robosapiens;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Connexio extends AppCompatActivity
{
	private static BTHelper myBTHelper;
	private final ArrayList<String> llista = new ArrayList<>();
	private ArrayAdapter<String> adaptat;
	private ProgressDialog progres;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Button emparellats_bttn, buscardisp_bttn;
		ListView llistadisp;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myBTHelper = new BTHelper(Connexio.this);
		myBTHelper.checkBTEnabled();

		adaptat = new ArrayAdapter<String>(Connexio.this, R.layout.custom_textview, llista);
		progres = new ProgressDialog(Connexio.this);

		emparellats_bttn = (Button) findViewById(R.id.emparellats);
		buscardisp_bttn = (Button) findViewById(R.id.buscardisp);

		llistadisp = (ListView)findViewById(R.id.llistadisp);
		llistadisp.setAdapter(adaptat);
		llistadisp.setOnItemClickListener(listenerllista);

		emparellats_bttn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myBTHelper.showPaired(adaptat, progres);
			}
		});
		buscardisp_bttn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myBTHelper.searchDevices(myReceiver, adaptat, progres);
			}
		});

	}

	private BroadcastReceiver myReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action))
			{
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				myBTHelper.myArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
			else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
			{
				myBTHelper.myProgress = ProgressDialog.show(myBTHelper.myContext, "Buscant...", "Espera");
			}
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
			{
				myBTHelper.myProgress.dismiss();
			}
		}
	};

	private final AdapterView.OnItemClickListener listenerllista = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			String infoBT = ((TextView)view).getText().toString();
			String direccioMACBT = infoBT.substring(infoBT.length()-17);

			myBTHelper.cancelDiscovery();
			myBTHelper.setSelectedBTDevice(direccioMACBT);
			myBTHelper.openSocket();

			Intent i = new Intent(Connexio.this, Moviment.class);
			startActivity(i);
			//finish();
		}
	};

	@Override
	public void onDestroy()
	{
		unregisterReceiver(myReceiver);
		myBTHelper.onDestroy();
		super.onDestroy();
	}
}
