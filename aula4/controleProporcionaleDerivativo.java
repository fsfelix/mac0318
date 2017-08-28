import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;

public class CPD {
	
    static LightSensor light;
    static NXTMotor mB;
    static NXTMotor mC;
	
    public static void main(String args[])  
    {
	int u_linha, turn, KP, KD, erroant, erro;
	RConsole.openAny(0);
	light = new LightSensor(SensorPort.S4);
	mB = new NXTMotor(MotorPort.B);
	mC = new NXTMotor(MotorPort.C);
	Button.waitForAnyPress();
	
	u_linha = 80;
	KP = 50;
	KD = 3;
	erroant = 0;

	while(!Button.ESCAPE.isDown()){
	    erro = 45 - light.getLightValue();
	    turn = KP*erro + KD*(erroant - erro);
	    mB.setPower(u_linha + turn);
	    mC.setPower(u_linha - turn);
	    RConsole.println(""+light.getLightValue());
	    erroant = erro;
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

