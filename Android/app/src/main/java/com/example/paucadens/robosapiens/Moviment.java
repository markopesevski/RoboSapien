package com.example.paucadens.robosapiens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Moviment extends AppCompatActivity
{
	private static BTHelper myBTHelper;

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

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		myBTHelper = new BTHelper(Moviment.this);

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

		desconnectar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myBTHelper.disconnect(Moviment.this);
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
		myBTHelper.sendString(Moviment.this, "a", "Up");
	}

	public void down()
	{
		myBTHelper.sendString(Moviment.this, "b", "Down");
	}

	public void left()
	{
		myBTHelper.sendString(Moviment.this, "c", "Left");
	}

	public void right()
	{
		myBTHelper.sendString(Moviment.this, "d", "Right");
	}

	public void stop()
	{
		myBTHelper.sendString(Moviment.this, "e", "Stop");
	}

	private void up_right()
	{
		myBTHelper.sendString(Moviment.this, "f", "Right Arm Up");
	}

	private void up_left()
	{
		myBTHelper.sendString(Moviment.this, "g", "Left Arm Up");
	}

	private void down_right()
	{
		myBTHelper.sendString(Moviment.this, "h", "Right Arm Down");
	}

	private void down_left()
	{
		myBTHelper.sendString(Moviment.this, "i", "Left Arm Down");
	}

	private void open_left_arm()
	{
		myBTHelper.sendString(Moviment.this, "j", "Left Arm Open");
	}

	private void close_left_arm()
	{
		myBTHelper.sendString(Moviment.this, "k", "Left Arm Close");
	}

	private void open_right_arm()
	{
		myBTHelper.sendString(Moviment.this, "l", "Right Arm Open");
	}

	private void close_right_arm()
	{
		myBTHelper.sendString(Moviment.this, "m", "Right Arm Close");
	}

	public void tilt_body_left()
	{
		myBTHelper.sendString(Moviment.this, "n", "Tilt Body Left");
	}

	public void tilt_body_right()
	{
		myBTHelper.sendString(Moviment.this, "o", "Tilt Body Right");
	}

	private void grip_left()
	{
		myBTHelper.sendString(Moviment.this, "p", "Left Grip Action");
	}

	private void grip_right()
	{
		myBTHelper.sendString(Moviment.this, "q", "Right Grip Action");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
