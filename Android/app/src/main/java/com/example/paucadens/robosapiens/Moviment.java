package com.example.paucadens.robosapiens;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Moviment extends AppCompatActivity {
    private Button up;
    private Button down;
    private ImageView stop;
    private Button left;
    private Button right;
    private Button upLeft;
    private Button upRight;
    private Button downRight;
    private Button downLeft;
    private Button openLeftArm;
    private Button closeLeftArm;
    private Button openRightArm;
    private Button closeRightArm;
    private Button tiltBodyLeft;
    private Button tiltBodyRight;
    private Button gripLeft;
    private Button gripRight;
    private String nomBT = null;
    private String dirBT = null;
    private ImageView cararobot;
    private Button desconnectar;
    public static BluetoothSocket btSocket = null;
    public static BluetoothAdapter miBT = null;
    static final UUID UUIDserie = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final ConnectarBT connexioBT = new ConnectarBT();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
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

        Intent intentanterior = getIntent();
        dirBT = intentanterior.getStringExtra(Connexio.DIRECCIO_EXTRA);
        nomBT = intentanterior.getStringExtra(Connexio.NOMBT_EXTRA);

        connexioBT.execute();

        desconnectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Moviment.this,Connexio.class);
                if (btSocket != null) {
                    try {
                        connexioBT.setShaconnectat(false);
                        btSocket.close();
                        miBT.disable();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Error closing", Toast.LENGTH_SHORT).show();
                    }
                }
                startActivity(i);
                finish();
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up();
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right();
            }
        });
        upRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_right();
            }
        });
        upLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_left();
            }
        });
        downRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down_right();
            }
        });
        downLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down_left();
            }
        });
        openLeftArm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_left_arm();
            }
        });
        closeLeftArm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_left_arm();
            }
        });
        openRightArm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_right_arm();
            }
        });
        closeRightArm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_right_arm();
            }
        });
        gripLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grip_left();
            }
        });
        gripRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grip_right();
            }
        });
        tiltBodyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilt_body_left();
            }
        });
        tiltBodyRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilt_body_right();
            }
        });


        cararobot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent(Moviment.this,Movimentacc.class);
                startActivity(i);
            }
        });

    }
    @Override
    public void onResume()
    {
        super.onResume();
        new ConnectarBT().execute();
    }

    private void up() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("a".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error up", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void down() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("b".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error down", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void left() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("c".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error left", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void right() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("d".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error right", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stop() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("e".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error stop", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void up_right() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("f".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error up right", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void up_left() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("g".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error up left", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void down_right() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("h".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error down right", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void down_left() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("i".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error down left", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void open_left_arm() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("j".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error open left arm", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void close_left_arm() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("k".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error close left arm", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void open_right_arm() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("l".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error open right arm", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void close_right_arm() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("m".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error close right arm", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void tilt_body_left() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("n".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error tilt body left", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void tilt_body_right() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("o".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error tilt body right", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void grip_left() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("p".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error close right arm", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void grip_right() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("q".getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error close right arm", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class ConnectarBT extends AsyncTask<Void, Void, Void> {
        private boolean shaconnectat = true;
        private ProgressDialog progres;

        @Override
        protected void onPreExecute() {
            progres = ProgressDialog.show(Moviment.this, "Connectant...", "Espera");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (btSocket == null || !shaconnectat) {
                    miBT = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice disp = miBT.getRemoteDevice(dirBT);
                    btSocket = disp.createInsecureRfcommSocketToServiceRecord(UUIDserie);
                    //btSocket = disp.createRfcommSocketToServiceRecord(UUIDserie);
                    btSocket.connect();
                }
            } catch (IOException e) {
                shaconnectat = false;
                Toast.makeText(getApplicationContext(), "Excepcio: " + e.getMessage() + "!", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!shaconnectat) {
                Toast.makeText(getApplicationContext(), "Error de connexió!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Connectat", Toast.LENGTH_LONG).show();
            }
            progres.dismiss();
        }

        public void setShaconnectat(boolean estat)
        {
            shaconnectat = estat;
        }
    }
}



