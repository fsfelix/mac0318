import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;

public class controleProporcional {
	
    static LightSensor light;
    static NXTMotor mB;
    static NXTMotor mC;
	
    public static void main(String args[])  
    {
	int u_linha, turn, KP, erro;
	RConsole.openAny(0);
	light = new LightSensor(SensorPort.S4);
	mB = new NXTMotor(MotorPort.B);
	mC = new NXTMotor(MotorPort.C);
	Button.waitForAnyPress();
	
	u_linha = 80;
	KP = 243;
	while(!Button.ESCAPE.isDown()){
	    erro = 45 - light.getLightValue();
	    turn = KP*erro;
	    mB.setPower(u_linha + turn);
	    mC.setPower(u_linha - turn);
	    RConsole.println(""+light.getLightValue());
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

