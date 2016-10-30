package com.example.paucadens.robosapiens;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class Connexio extends AppCompatActivity
{
	private static BTHelper myBTHelper;
	private final ArrayList<String> llista = new ArrayList<>();
	private ArrayAdapter<String> adaptat;
	private ProgressDialog progres;

	public static final String DIRECCIO_EXTRA = "Direccio BT";
	public static final String NOMBT_EXTRA = "Nom BT";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Button emparellats_bttn,buscardisp_bttn;
		ListView llistadisp;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		emparellats_bttn = (Button) findViewById(R.id.emparellats);
		buscardisp_bttn = (Button) findViewById(R.id.buscardisp);
		llistadisp = (ListView)findViewById(R.id.llistadisp);
		adaptat = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, llista);
		llistadisp.setAdapter(adaptat);
		llistadisp.setOnItemClickListener(listenerllista);

		emparellats_bttn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myBTHelper.showPaired(adaptat);
			}
		});


		buscardisp_bttn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myBTHelper.searchDevices(adaptat, progres);
			}
		});
	}

	private final AdapterView.OnItemClickListener listenerllista = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			String infoBT = ((TextView)view).getText().toString();
			String direccioMACBT = infoBT.substring(infoBT.length()-17);
			String nomBT = infoBT.substring(0, infoBT.length()-18);

			myBTHelper.cancelDiscovery();
			Intent i = new Intent(Connexio.this, Moviment.class);
			i.putExtra(DIRECCIO_EXTRA, direccioMACBT);
			i.putExtra(NOMBT_EXTRA, nomBT);
			startActivity(i);
			finish();
		}
	};

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
}
