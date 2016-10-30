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
import android.widget.TextView;

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
	private static final String[] estats_text =
	{
		"Status: Forwards",
		"Status: Backwards",
		"Status: Moving Left",
		"Status: Moving Right",
		"Status: Tilting Left",
		"Status: Tilting Right",
		"Status: Stopped"
	};
	private static int estats_index = 6;
	private TextView estats;
	private static boolean enviar_comandes = false;
	private Button stop_acc;

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
		estats = (TextView) findViewById(R.id.estats);
		stop_acc = (Button)findViewById(R.id.stop_acc);

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
				finish();
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

		stop_acc.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				stop();
			}
		});
	}

	public void stop()
	{
		if(enviar_comandes)
		{
			if (Moviment.btSocket != null)
			{
				enviar_comandes = false;
				myMoviment.stop();
				stop_acc.setBackgroundResource(R.drawable.cercle_verd);
				stop_acc.setTextColor(0x000000);
				stop_acc.setText(R.string.start_acc_text);
			}
		}
		else
		{
			if (Moviment.btSocket != null)
			{
				enviar_comandes = true;
				stop_acc.setBackgroundResource(R.drawable.cercle_vermell);
				stop_acc.setTextColor(0xFFFFFF);
				stop_acc.setText(R.string.stop_acc_text);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1)
	{}

	@Override
	public void onSensorChanged(SensorEvent arg0)
	{
		if(enviar_comandes)
		{
			if (arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				System.arraycopy(arg0.values, 0, lecturaAccelerometre, 0, lecturaAccelerometre.length);
			} else if (arg0.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				System.arraycopy(arg0.values, 0, lecturaMagnetometre, 0, lecturaMagnetometre.length);
			}

			SensorManager.getRotationMatrix(matriuRotacio, null, lecturaAccelerometre, lecturaMagnetometre);
			SensorManager.getOrientation(matriuRotacio, angles);
			angles[0] = angles[0] * (180 / 3.141592f);
			angles[1] = angles[1] * (180 / 3.141592f);
			angles[2] = angles[2] * (180 / 3.141592f);

			angles[0] = angles[0] - cz;
			angles[1] = angles[1] - cx;
			angles[2] = angles[2] - cy;

			double ax = angles[1];
			double ay = angles[2];
			double az = angles[0];

			if (Moviment.btSocket != null && (ax > 40 && abs(ay) < 10 && abs(az) < 10) && estat != endavant) {
				myMoviment.up();
				estats_index = 0;
			} else if (Moviment.btSocket != null && (ax < -40 && abs(ay) < 10 && abs(az) < 10) && estat != endarrere) {
				myMoviment.down();
				estats_index = 1;
			} else if (Moviment.btSocket != null && (abs(ax) < 10 && ay < -20 && az < -40) && estat != gira_esquerra) {
				myMoviment.left();
				estats_index = 2;
			} else if (Moviment.btSocket != null && (abs(ax) < 10 && ay > 20 && az > 40) && estat != gira_dreta) {
				myMoviment.right();
				estats_index = 3;
			} else if (Moviment.btSocket != null && (abs(ax) < 10 && ay > 40 && abs(az) < 10) && estat != tilt_dreta) {
				myMoviment.tilt_body_right();
				estats_index = 4;
			} else if (Moviment.btSocket != null && (abs(ax) < 10 && ay < -40 && abs(az) < 10) && estat != tilt_esquerra) {
				myMoviment.tilt_body_left();
				estats_index = 5;
			} else if (Moviment.btSocket != null && (abs(ax) < 10 && abs(ay) < 10 && abs(az) < 10) && estat != parat) {
				myMoviment.stop();
				estats_index = 6;
			}

			estats.setText(estats_text[estats_index]);
		}
		else if (mesurar)
		{
			cz = angles[0];
			cx = angles[1];
			cy = angles[2];
			mesurar = false;
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

	}
}
