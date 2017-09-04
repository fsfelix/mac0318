import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;

// LFR: Left Forward Right 

public class LFR {	
    static LightSensor light;
    static NXTMotor mB;
    static NXTMotor mC;
	
    public static void main(String args[])  
    {
	RConsole.openAny(0);
	light = new LightSensor(SensorPort.S4);
	mB = new NXTMotor(MotorPort.B);
	mC = new NXTMotor(MotorPort.C);
	Button.waitForAnyPress();
		
	while(!Button.ESCAPE.isDown()){
		    
	    while (light.getLightValue () < 40 && !Button.ESCAPE.isDown())
		{
		    mB.setPower(60);
		    mC.setPower(1);
		    RConsole.println("PRIMEIRO: "+light.getLightValue());
		}

	    while (light.getLightValue () >=40 && light.getLightValue () < 45 && !Button.ESCAPE.isDown())
		{
		    mB.setPower(40);
		    mC.setPower(40);
		    RConsole.println("MEIO: "+light.getLightValue());

		}


	    while (light.getLightValue () >= 45 && !Button.ESCAPE.isDown())
		{
		    mB.setPower(1);
		    mC.setPower(60);
		    RConsole.println("ULTIMO: "+light.getLightValue());
		}


	}


	// while (light.getLightValue() < 50){
		    
	//     // mB.setPower(50);
	//     // mC.setPower(50);
		    
	//     //mB.setPower(-50);
	//     //mC.setPower(-50);
	// }
	// mB.stop();
	// mC.stop();
    }	
}

