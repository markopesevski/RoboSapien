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
	private float cx = 0, cy = 0, cz = 0;
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
	private static t_moviments estat = parat;
	private static Moviment myMoviment;
	private static final String[] estats_text =
	{
		"Estat: Endavant",
		"Estat: Endarrere",
		"Estat: Esquerra",
		"Estat: Dreta",
		"Estat: Inclinant dreta",
		"Estat: Inclinant esquerra",
		"Estat: Parat",
		"Estat: No s'envien comandes"
	};
	private static int estats_index = 6;
	private TextView estats;
	private static boolean enviar_comandes = false;
	private static Button stop_acc;
	private BTHelper myBTHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		SensorManager sensors;
		Button tornarenradere;
		Button calibrar;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movimentacc);

		myBTHelper = new BTHelper(Movimentacc.this);
		myMoviment = new Moviment();

		calibrar = (Button)findViewById(R.id.calibrar);
		tornarenradere = (Button)findViewById(R.id.tornarenradere);
		estats = (TextView) findViewById(R.id.estats);
		stop_acc = (Button) findViewById(R.id.stop_acc);

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

	private void stop()
	{
		// TODO revisar si els colors son correctes o que
		if(enviar_comandes)
		{
			enviar_comandes = false;
			myMoviment.stop();
			stop_acc.setText(R.string.start_acc_text);
			stop_acc.setTextColor(0x000000);
			stop_acc.setBackgroundResource(R.drawable.button_green);
			estats.setText(estats_text[7]);
		}
		else
		{
			enviar_comandes = true;
			stop_acc.setText(R.string.stop_acc_text);
			stop_acc.setTextColor(0xFFFFFF);
			stop_acc.setBackgroundResource(R.drawable.button_red);
		}
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
		angles[0] = angles[0] * (180 / 3.141592f);
		angles[1] = angles[1] * (180 / 3.141592f);
		angles[2] = angles[2] * (180 / 3.141592f);

		if (mesurar)
		{
			cz = angles[0];
			cx = angles[1];
			cy = angles[2];
			mesurar = false;
			enviar_comandes = true;
		}

		angles[0] = angles[0] - cz;
		angles[1] = angles[1] - cx;
		angles[2] = angles[2] - cy;

		double ax = angles[1];
		double ay = angles[2];
		double az = angles[0];

		if(enviar_comandes)
		{
			if ((ax > 40 && abs(ay) < 10 && abs(az) < 10) && estat != endavant)
			{
				estat = endavant;
				myMoviment.up();
				estats_index = 0;
			}
			else if ((ax < -40 && abs(ay) < 10 && abs(az) < 10) && estat != endarrere)
			{
				estat = endarrere;
				myMoviment.down();
				estats_index = 1;
			}
			else if ((abs(ax) < 10 && ay < -20 && az < -40) && estat != gira_esquerra)
			{
				estat = gira_esquerra;
				myMoviment.left();
				estats_index = 2;
			}
			else if ((abs(ax) < 10 && ay > 20 && az > 40) && estat != gira_dreta)
			{
				estat = gira_dreta;
				myMoviment.right();
				estats_index = 3;
			}
			else if ((abs(ax) < 10 && ay > 40 && abs(az) < 10) && estat != tilt_dreta)
			{
				estat = tilt_dreta;
				myMoviment.tilt_body_right();
				estats_index = 4;
			}
			else if ((abs(ax) < 10 && ay < -40 && abs(az) < 10) && estat != tilt_esquerra)
			{
				estat = tilt_esquerra;
				myMoviment.tilt_body_left();
				estats_index = 5;
			}
			else if ((abs(ax) < 10 && abs(ay) < 10 && abs(az) < 10) && estat != parat)
			{
				estat = parat;
				myMoviment.stop();
				estats_index = 6;
			}

			estats.setText(estats_text[estats_index]);
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
