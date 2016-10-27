package com.example.paucadens.robosapiens;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.endarrere;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.endavant;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.gira_dreta;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.gira_esquerra;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.parat;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.tilt_dreta;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.tilt_esquerra;
import static java.lang.Math.abs;

public class Movimentacc extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensors;
    private Button tornarenradere;
    private Button calibrar;
    double ax, ay, az;
    float cx, cy, cz;
    private boolean mesurar = false;
    private boolean calibrat = false;
    TextView sx, sy, sz;
    private final float [] lecturaAccelerometre = new float[3];
    private final float [] lecturaMagnetometre = new float[3];
    private final float [] matriuRotacio = new float[9];
    private final float [] angles = new float[3];
    enum t_moviments{
        endavant,
        parat,
        gira_esquerra,
        gira_dreta,
        tilt_esquerra,
        tilt_dreta,
        endarrere
    };
    t_moviments estat = parat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentacc);

        calibrar = (Button)findViewById(R.id.calibrar);
        tornarenradere = (Button)findViewById(R.id.tornarenradere);
        sx = (TextView) findViewById(R.id.sx);
        sy = (TextView) findViewById(R.id.sy);
        sz = (TextView) findViewById(R.id.sz);

        sensors = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensors.registerListener(this, sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
        sensors.registerListener(this, sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);

       tornarenradere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Movimentacc.this,Moviment.class);
                startActivity(i);
            }
        });

        calibrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesurar = true;
            }
        });
    }
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1){}
    @Override
    public void onSensorChanged(SensorEvent arg0){

        if (arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            System.arraycopy(arg0.values, 0, lecturaAccelerometre, 0, lecturaAccelerometre.length);
        }
        else if (arg0.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            System.arraycopy(arg0.values, 0, lecturaMagnetometre, 0, lecturaMagnetometre.length);
        }
        sensors.getRotationMatrix(matriuRotacio, null, lecturaAccelerometre, lecturaMagnetometre);
        sensors.getOrientation(matriuRotacio, angles);
        angles[0]=angles[0]*(180/3.141592f);
        angles[1]=angles[1]*(180/3.141592f);
        angles[2]=angles[2]*(180/3.141592f);
        if (mesurar == true){
            cz = angles[0];
            cx = angles[1];
            cy = angles[2];
            mesurar = false;
            calibrat = true;
        }
        if (calibrat == true){
            angles[0]=angles[0]-cz;
            angles[1]=angles[1]-cx;
            angles[2]=angles[2]-cy;
        }
        sz.setText("Angle z: "+Float.toString(angles[0]));
        sx.setText("Angle x: "+Float.toString(angles[1]));
        sy.setText("Angle y: "+Float.toString(angles[2]));

        ax = angles[1];
        ay = angles[2];
        az = angles[0];

        if (Moviment.btSocket != null && (ax>40 && abs(ay)<10 && abs(az)<10) && estat != endavant) {
            try {
                Moviment.btSocket.getOutputStream().write("u".getBytes());
                estat = endavant;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error Up", Toast.LENGTH_SHORT).show();
            }
        }
        else if (Moviment.btSocket != null && (ax<-40 && abs(ay)<10 && abs(az)<10) && estat != endarrere) {
            try {
                Moviment.btSocket.getOutputStream().write("d".getBytes());
                estat = endarrere;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error Down", Toast.LENGTH_SHORT).show();
            }
        }
        else if (Moviment.btSocket != null && (abs(ax)<10 && ay<-20 && az<-40) && estat != gira_esquerra) {
            try {
                Moviment.btSocket.getOutputStream().write("l".getBytes());
                estat = gira_esquerra;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error Left", Toast.LENGTH_SHORT).show();
            }
        }
        else if (Moviment.btSocket != null && (abs(ax)<10 && ay>20 && az>40) && estat != gira_dreta) {
            try {
                Moviment.btSocket.getOutputStream().write("r".getBytes());
                estat = gira_dreta;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error Right", Toast.LENGTH_SHORT).show();
            }
        }
        else if (Moviment.btSocket != null && (abs(ax)<10 && abs(ay)<10 && abs(az)<10) && estat != parat) {
            try {
                Moviment.btSocket.getOutputStream().write("s".getBytes());
                estat = parat;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error Stop", Toast.LENGTH_SHORT).show();
            }
        }
        else if (Moviment.btSocket != null && (abs(ax)<10 && ay>40 && abs(az)<10) && estat != tilt_dreta) {
            try {
                Moviment.btSocket.getOutputStream().write("a".getBytes());
                estat = tilt_dreta;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error tilt Right", Toast.LENGTH_SHORT).show();
            }
        }
        else if (Moviment.btSocket != null && (abs(ax)<10 && ay<-40 && abs(az)<10) && estat != tilt_esquerra) {
            try {
                Moviment.btSocket.getOutputStream().write("b".getBytes());
                estat = tilt_esquerra;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error tilt Left", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
