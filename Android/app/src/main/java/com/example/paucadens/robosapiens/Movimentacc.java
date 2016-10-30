package com.example.paucadens.robosapiens;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.endarrere;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.endavant;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.gira_dreta;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.gira_esquerra;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.parat;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.tilt_dreta;
import static com.example.paucadens.robosapiens.Movimentacc.t_moviments.tilt_esquerra;
import static java.lang.Math.abs;

public class Movimentacc extends AppCompatActivity implements SensorEventListener
{
	private float cx, cy, cz;
	private static boolean mesurar = false;
	private static boolean calibrat = false;
	private static final float [] lecturaAccelerometre = new float[3];
	private static final float [] lecturaMagnetometre = new float[3];
	private static final float [] matriuRotacio = new float[9];
	private static final float [] angles = new float[3];
	enum t_moviments
	{
		endavant,
		parat,
		gira_esquerra,
		gira_dreta,
		tilt_esquerra,
		tilt_dreta,
		endarrere
	}
	private static final t_moviments estat = parat;
	private final Moviment myMoviment = new Moviment();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		SensorManager sensors;
		Button tornarenradere;
		Button calibrar;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movimentacc);

		calibrar = (Button)findViewById(R.id.calibrar);
		tornarenradere = (Button)findViewById(R.id.tornarenradere);
		//sx = (TextView) findViewById(R.id.sx);

		sensors = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensors.registerListener(this, sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
		sensors.registerListener(this, sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);

	   tornarenradere.setOnClickListener(new View.OnClickListener()
	   {
			@Override
			public void onClick(View v)
			{
				Intent i=new Intent(Movimentacc.this,Moviment.class);
				startActivity(i);
			}
		});

		calibrar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mesurar = true;
			}
		});
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1)
	{}

	@Override
	public void onSensorChanged(SensorEvent arg0)
	{
		if (arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		{
			System.arraycopy(arg0.values, 0, lecturaAccelerometre, 0, lecturaAccelerometre.length);
		}
		else if (arg0.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		{
			System.arraycopy(arg0.values, 0, lecturaMagnetometre, 0, lecturaMagnetometre.length);
		}

		SensorManager.getRotationMatrix(matriuRotacio, null, lecturaAccelerometre, lecturaMagnetometre);
		SensorManager.getOrientation(matriuRotacio, angles);
		angles[0]=angles[0]*(180/3.141592f);
		angles[1]=angles[1]*(180/3.141592f);
		angles[2]=angles[2]*(180/3.141592f);

		if (mesurar)
		{
			cz = angles[0];
			cx = angles[1];
			cy = angles[2];
			mesurar = false;
			calibrat = true;
		}

		if (calibrat)
		{
			angles[0]=angles[0]-cz;
			angles[1]=angles[1]-cx;
			angles[2]=angles[2]-cy;
		}

		//sz.setText("Angle z: "+Float.toString(angles[0]));

		double ax = angles[1];
		double ay = angles[2];
		double az = angles[0];

		if (Moviment.btSocket != null && (ax >40 && abs(ay)<10 && abs(az)<10) && estat != endavant)
		{
			myMoviment.up();
		}
		else if (Moviment.btSocket != null && (ax <-40 && abs(ay)<10 && abs(az)<10) && estat != endarrere)
		{
			myMoviment.down();
		}
		else if (Moviment.btSocket != null && (abs(ax)<10 && ay <-20 && az <-40) && estat != gira_esquerra)
		{
			myMoviment.left();
		}
		else if (Moviment.btSocket != null && (abs(ax)<10 && ay >20 && az >40) && estat != gira_dreta)
		{
			myMoviment.right();
		}
		else if (Moviment.btSocket != null && (abs(ax)<10 && abs(ay)<10 && abs(az)<10) && estat != parat)
		{
			myMoviment.stop();
		}
		else if (Moviment.btSocket != null && (abs(ax)<10 && ay >40 && abs(az)<10) && estat != tilt_dreta)
		{
			myMoviment.tilt_body_right();
		}
		else if (Moviment.btSocket != null && (abs(ax)<10 && ay <-40 && abs(az)<10) && estat != tilt_esquerra)
		{
			myMoviment.tilt_body_left();
		}
	}
}
