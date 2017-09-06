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
	
    public static void main(String args[]) {

		int u_linha, turn, erroant, erro;
		double KP, KD;

		RConsole.openAny(0);
		
		light = new LightSensor(SensorPort.S4);
		mB = new NXTMotor(MotorPort.B);
		mC = new NXTMotor(MotorPort.C);
		
		Button.waitForAnyPress();
		
		u_linha = 50;
		
		KP = 3.5;
		KD = 1.0;

		erroant = 0;

		while(!Button.ESCAPE.isDown()){
		    erro = 45 - light.getLightValue();
		    turn = (int) (KP*erro + KD*(erroant - erro));
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

