package com.example.paucadens.robosapiens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class Moviment extends AppCompatActivity
{
	private static BTHelper myBTHelper;

	public void Moviment()
	{
		myBTHelper = new BTHelper(Moviment.this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Button up;
		Button down;
		ImageView stop;
		Button left;
		Button right;
		Button upLeft;
		Button upRight;
		Button downRight;
		Button downLeft;
		Button openLeftArm;
		Button closeLeftArm;
		Button openRightArm;
		Button closeRightArm;
		Button tiltBodyLeft;
		Button tiltBodyRight;
		Button gripLeft;
		Button gripRight;
		ImageView cararobot;
		Button desconnectar;
		Spinner spinnerBalls;
		SpinnerActivity gestorBalls;
		ArrayAdapter<String> adapter;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		gestorBalls = new SpinnerActivity();

		up = (Button) findViewById(R.id.up);
		down = (Button) findViewById(R.id.down);
		stop = (ImageView) findViewById(R.id.stop);
		left = (Button) findViewById(R.id.left);
		right = (Button) findViewById(R.id.right);
		cararobot = (ImageView) findViewById(R.id.cararobot);
		desconnectar = (Button) findViewById(R.id.desconnectar);
		upLeft = (Button) findViewById(R.id.armUpLeft);
		upRight = (Button) findViewById(R.id.armUpRight);
		downRight = (Button) findViewById(R.id.armDownRight);
		downLeft = (Button) findViewById(R.id.armDownLeft);
		openLeftArm = (Button) findViewById(R.id.armOutLeft);
		closeLeftArm = (Button) findViewById(R.id.armInLeft);
		openRightArm = (Button) findViewById(R.id.armOutRight);
		closeRightArm = (Button) findViewById(R.id.armInRight);
		tiltBodyLeft = (Button) findViewById(R.id.tiltBodyLeft);
		tiltBodyRight = (Button) findViewById(R.id.tiltBodyRight);
		gripLeft = (Button) findViewById(R.id.gripLeft);
		gripRight = (Button) findViewById(R.id.gripRight);
		spinnerBalls = (Spinner) findViewById(R.id.spinner);

		adapter = new ArrayAdapter<String>(Moviment.this, android.R.layout.simple_spinner_dropdown_item)
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View v = super.getView(position, convertView, parent);
				if (position == getCount())
				{
					((TextView)v.findViewById(android.R.id.text1)).setText("");
					((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
				}

				return v;
			}

			@Override
			public int getCount()
			{
				return super.getCount()-1; // you dont display last item. It is used as hint.
			}
		};
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerBalls.setAdapter(adapter);


		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add("Ball 1");
		adapter.add("Ball 2");
		adapter.add("Ball 3");
		adapter.add("Balls"); //This is the text that will be displayed as hint.
		spinnerBalls.setAdapter(adapter);
		spinnerBalls.setSelection(adapter.getCount()); //set the hint the default selection so it appears on launch.
		spinnerBalls.setOnItemSelectedListener(gestorBalls);

		desconnectar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myBTHelper.disconnect();
				Intent i=new Intent(Moviment.this,Connexio.class);
				startActivity(i);
				finish();
			}
		});

		up.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				up();
			}
		});

		down.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				down();
			}
		});

		stop.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				stop();
			}
		});

		left.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				left();
			}
		});

		right.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				right();
			}
		});

		upRight.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				up_right();
			}
		});

		upLeft.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				up_left();
			}
		});

		downRight.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				down_right();
			}
		});

		downLeft.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				down_left();
			}
		});

		openLeftArm.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				open_left_arm();
			}
		});

		closeLeftArm.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				close_left_arm();
			}
		});

		openRightArm.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				open_right_arm();
			}
		});

		closeRightArm.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				close_right_arm();
			}
		});

		gripLeft.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				grip_left();
			}
		});

		gripRight.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				grip_right();
			}
		});

		tiltBodyLeft.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				tilt_body_left();
			}
		});

		tiltBodyRight.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				tilt_body_right();
			}
		});

		cararobot.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i=new Intent(Moviment.this,Movimentacc.class);
				startActivity(i);
				finish();
			}
		});


	}

	public void up()
	{
		myBTHelper.sendString("a", "Up");
	}

	public void down()
	{
		myBTHelper.sendString("b", "Down");
	}

	public void left()
	{
		myBTHelper.sendString("c", "Left");
	}

	public void right()
	{
		myBTHelper.sendString("d", "Right");
	}

	public void stop()
	{
		myBTHelper.sendString("e", "Stop");
	}

	private void up_right()
	{
		myBTHelper.sendString("f", "Right Arm Up");
	}

	private void up_left()
	{
		myBTHelper.sendString("g", "Left Arm Up");
	}

	private void down_right()
	{
		myBTHelper.sendString("h", "Right Arm Down");
	}

	private void down_left()
	{
		myBTHelper.sendString("i", "Left Arm Down");
	}

	private void open_left_arm()
	{
		myBTHelper.sendString("j", "Left Arm Open");
	}

	private void close_left_arm()
	{
		myBTHelper.sendString("k", "Left Arm Close");
	}

	private void open_right_arm()
	{
		myBTHelper.sendString("l", "Right Arm Open");
	}

	private void close_right_arm()
	{
		myBTHelper.sendString("m", "Right Arm Close");
	}

	public void tilt_body_left()
	{
		myBTHelper.sendString("n", "Tilt Body Left");
	}

	public void tilt_body_right()
	{
		myBTHelper.sendString("o", "Tilt Body Right");
	}

	private void grip_left()
	{
		myBTHelper.sendString("p", "Left Grip Action");
	}

	private void grip_right()
	{
		myBTHelper.sendString("q", "Right Grip Action");
	}

	private void ball_1()
	{
		myBTHelper.sendString("1", "Ball 1");
	}

	private void ball_2()
	{
		myBTHelper.sendString("2", "Ball 2");
	}

	private void ball_3()
	{
		myBTHelper.sendString("3", "Ball 3");
	}

	private class SpinnerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
	{
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
		{
			// An item was selected. You can retrieve the selected item using
			if(pos == 0)
			{
				ball_1();
			}
			else if(pos == 1)
			{
				ball_2();
			}
			else if(pos == 2)
			{
				ball_3();
			}
		}

		public void onNothingSelected(AdapterView<?> parent)
		{
			// Another interface callback
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
