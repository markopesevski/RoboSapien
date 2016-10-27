package com.example.paucadens.robosapiens;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Set;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class Connexio extends AppCompatActivity {

    Button emparellats_bttn,buscardisp_bttn;
    ListView llistadisp;

    private ArrayList llista = new ArrayList();
    private ArrayAdapter adaptat;

    private BluetoothAdapter miBluetooth = null;

    private Set<BluetoothDevice> dispemparellats;
    private ProgressDialog progres;

    public static String DIRECCIO_EXTRA = "Direccio BT";
    public static String NOMBT_EXTRA = "Nom BT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emparellats_bttn = (Button) findViewById(R.id.emparellats);
        buscardisp_bttn = (Button) findViewById(R.id.buscardisp);
        llistadisp = (ListView)findViewById(R.id.llistadisp);
        miBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (miBluetooth == null){
            Toast.makeText(getApplicationContext(),"Bluetooth no existeix", Toast.LENGTH_LONG).show();
        }
        else{
            if (!miBluetooth.isEnabled()){
                Intent ActivaBT = new
                        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(ActivaBT,1);
            }
        }
        emparellats_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraemparellats();
            }
        });
        adaptat = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,llista);
        llistadisp.setAdapter(adaptat);
        llistadisp.setOnItemClickListener(listenerllista);

        buscardisp_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscardisp();
            }
        });
    }

    private void buscardisp(){
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver((BroadcastReceiver) mReceiver, filter); // Don't forget to unregister during onDestroy
        miBluetooth.startDiscovery();
    }

    private void mostraemparellats(){
        dispemparellats = miBluetooth.getBondedDevices();
        adaptat.clear();
        if (dispemparellats.size()>0){
            for (BluetoothDevice bt:dispemparellats){
                llista.add(bt.getName()+"\n"+bt.getAddress());
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"No hi ha dispositius emparellats",Toast.LENGTH_LONG).show();
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                adaptat.add(device.getName() + "\n" + device.getAddress());
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                progres = ProgressDialog.show(Connexio.this,"Buscant...","Espera");
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                progres.dismiss();
            }
        }
    };
        @Override
        public void onDestroy(){
            unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private AdapterView.OnItemClickListener listenerllista = new
            AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    miBluetooth.cancelDiscovery();
                    String infoBT = ((TextView)view).getText().toString();
                    String direccioMACBT=infoBT.substring(infoBT.length()-17);
                    String nomBT=infoBT.substring(0,infoBT.length()-18);
                    Intent i=new Intent(Connexio.this,Moviment.class);
                    i.putExtra(DIRECCIO_EXTRA,direccioMACBT);
                    i.putExtra(NOMBT_EXTRA,nomBT);
                    startActivity(i);
                }
            };
}