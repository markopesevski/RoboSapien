  #include <SoftwareSerial.h>

enum roboCommand {
	turnRight				= 0x80,
	rightArmUp				= 0x81,
	rightArmOut				= 0x82, //obrir ma dreta
	tiltBodyRight			= 0x83,
	rightArmDown			= 0x84,
	rightArmIn				= 0x85, // tancar ma dreta
	walkForward				= 0x86, // caminar endavant
	walkBackward			= 0x87, // caminar enrera
	turnLeft				= 0x88,
	leftArmUp				= 0x89,
	leftArmOut				= 0x8A, //obrir ma esquerra
	tiltBodyLeft			= 0x8B,
	leftArmDown				= 0x8C,
	leftArmIn				= 0x8D, //tancar ma esquerra
	stopMoving				= 0x8E,
	masterCommandProgram	= 0x90,
	programPlay				= 0x91,
	rightSensorProgram		= 0x92,
	leftSensorProgram		= 0x93,
	sonicSensorProgram		= 0x94,
	rightTurnStep			= 0xA0,
	rightHandThump			= 0xA1,
	rightHandThrow			= 0xA2,
	sleep					= 0xA3,
	rightHandPickup			= 0xA4,
	leanBackward			= 0xA5,
	forwardStep				= 0xA6,
	backwardStep			= 0xA7,
	leftTurnStep			= 0xA8,
	leftHandThump			= 0xA9,
	leftHandThrow			= 0xAA,
	listenMicrophone		= 0xAB,
	leftHandPickup			= 0xAC,
	leanForward				= 0xAD,
	reset					= 0xAE,
	execute					= 0xB0,
	wakeup					= 0xB1,
	right					= 0xB2,
	left					= 0xB3,
	sonic					= 0xB4,
	rightHandStrike3		= 0xC0,
	rightHandSweep			= 0xC1,
	burp					= 0xC2,
	rightHandStrike2		= 0xC3,
	high_five				= 0xC4,
	rightHandStrike1		= 0xC5,
	bulldozer				= 0xC6,
	fart					= 0xC7,
	leftHandStrike3			= 0xC8,
	leftHandSweep			= 0xC9,
	whistle					= 0xCA,
	leftHandStrike2			= 0xCB,
	talkback				= 0xCC,
	leftHandStrike1			= 0xCD,
	roar					= 0xCE,
	allDemo					= 0xD0,
	powerOff				= 0xD1,
	demo1					= 0xD2,
	demo2					= 0xD3,
	dance					= 0xD4
};

int garra_esquerra = 1;
int garra_dreta = 1;

float tiempo = 0;
float ultimaTransmissioMillis = 0;
char inbytes = 0;
int i = 0;
int irPin = 13;
int tsDelay = 833;

SoftwareSerial BT(2,3); // 2 RX, 3 TX

void setup()
{
	Serial.begin(115200);
	BT.begin(9600);
	/*
		Serial.println("AT+BAUD4");
		delay(100);
		Serial.println("AT+NAME22ENREPOSO");
	*/
}

void delayTs(unsigned int slices)
{
	delayMicroseconds(tsDelay * slices);
}

void writeCommand(unsigned char cmd)
{
	pinMode(irPin, OUTPUT);
	digitalWrite(irPin, LOW);
	delayTs(8);

	for(char b = 7; b>=0; b--) {
		digitalWrite(irPin, HIGH);
		delayTs( (cmd & 1<<b) ? 4 : 1 );
		digitalWrite(irPin, LOW);
		delayTs(1);
	}

	digitalWrite(irPin, HIGH);
	pinMode(irPin, INPUT);
}

void loop()
{
	tiempo = millis();
	if(Serial.available() > 0)
	{
		BT.write(Serial.read());
	}
	if(BT.available() > 0)
	{
		inbytes = BT.read();
		Serial.print("BT rx: ");
		Serial.println(inbytes);
		if (inbytes == 'a')
		{
			writeCommand(walkForward);
		}
		else if (inbytes == 'b')
		{
			writeCommand(walkBackward);
		}
		else if (inbytes == 'c')
		{
			writeCommand(turnLeft);
		}
		else if (inbytes == 'd')
		{
			writeCommand(turnRight);
		}
		else if (inbytes == 'e')
		{
			writeCommand(stopMoving);
		}
		else if (inbytes == 'f')
		{
		  writeCommand(tiltBodyRight);
		}
		else if (inbytes == 'g')
		{
		  writeCommand(tiltBodyLeft);
		}
		else if (inbytes == 'h')
		{
		  writeCommand(rightArmUp);
		}
		else if (inbytes == 'i')
		{
		  writeCommand(leftArmUp);
		}
		else if (inbytes == 'j')
		{
		  writeCommand(rightArmDown);
		}
		else if (inbytes == 'k')
		{
		  writeCommand(leftArmDown);
		}
		else if (inbytes == 'l') //obrir ma esquerra
		{
		  writeCommand(leftArmOut);
		}
		else if (inbytes == 'm') //tancar ma esquerra
		{
		  writeCommand(leftArmIn);
		}
		else if (inbytes == 'n') //obrir ma dreta
		{
		  writeCommand(rightArmOut);
		}
		else if (inbytes == 'o') // tancar ma dreta
		{
		  writeCommand(rightArmIn);
		}
		else if (inbytes == 'p') // tancar garra esquerra
		{
			if(garra_esquerra == 0)
			{
				writeCommand(leftHandPickup);
				garra_esquerra = 1;
			}
			else if (garra_esquerra == 1)
			{
				writeCommand(leftHandPickup);
				garra_esquerra = 0;
			}
		}
		else if (inbytes == 'q') // tancar garra dreta
		{
			if(garra_dreta == 0)
			{
				writeCommand(rightHandPickup);
				garra_dreta = 1;
			}
			else if (garra_dreta == 1)
			{
				writeCommand(rightHandPickup);
				garra_dreta = 0;
			}

		}
	}
}

