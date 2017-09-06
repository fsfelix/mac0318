import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;

//public class unregulatedMotor {
public class leftForwardRight {	
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
		    
	    while (light.getLightValue () < 40)
		{
		    mB.setPower(15);
		    mC.setPower(1);
		    RConsole.println("PRIMEIRO"+light.getLightValue());
		}

	    while (light.getLightValue () >=40 && light.getLightValue () < 43)
		{
		    mB.setPower(40);
		    mC.setPower(40);
		    RConsole.println("NOVO"+light.getLightValue());

		}


	    while (light.getLightValue() >= 43 && light.getLightValue() <= 47)
		{
		    mB.setPower(30);
		    mC.setPower(30);
		    RConsole.println("MEIO"+light.getLightValue());
			    
		}

	    while (light.getLightValue () > 47 && light.getLightValue () < 55)
		{
		    mB.setPower(1);
		    mC.setPower(15);
		    RConsole.println("ULTIMO"+light.getLightValue());
		}

	    while (light.getLightValue () >= 55)
		{
		    mC.setPower(40);
		    mB.setPower(1);
		    RConsole.println("NOVO"+light.getLightValue());

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
