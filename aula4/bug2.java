import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;


public class bug2 {
	
    static LightSensor light;
    static NXTMotor mB;
    static NXTMotor mC;

    static NXTRegulatedMotor rb;
    static NXTRegulatedMotor rc; 
    
    public static double normalizeAngle(double a, double center) {
		return a - (2 * Math.PI) * Math.floor((a + Math.PI - center) / (2*Math.PI));
    }

    private static double [] newPos(double [] lastPos) {
    	
    	double teta0, x0, y0;

    	x0 = lastPos[0];
    	y0 = lastPos[1];
    	teta0 = lastPos[2];

    	double xf, yf, tetaf, delta_teta, delta_s, raioDaRoda, tachoB, tachoC, sum;
		double [] pos = new double[3];

    	raioDaRoda = 2.8;
	
    	teta0 = Math.toRadians(teta0);
    	tachoB = rb.getTachoCount();
    	tachoC = rc.getTachoCount();
	
    	tachoB = Math.toRadians(tachoB);
    	tachoC = Math.toRadians(tachoC);
	
    	// 11.2 é a distancia entre eixos
    	delta_teta = (tachoB - tachoC)*(raioDaRoda)/(11.2);
	
		sum = teta0 + delta_teta/2;
		sum = normalizeAngle(sum, Math.PI);	

    	delta_s = (tachoB + tachoC)*raioDaRoda/2;
    	// xf = x0 + delta_s*Math.cos(teta0 + delta_teta/2);
    	// yf = y0 + delta_s*Math.sin(teta0 + delta_teta/2);
    	xf = x0 + delta_s*Math.cos(sum);
    	yf = y0 + delta_s*Math.sin(sum);

    	tetaf = teta0 + delta_teta;
		tetaf = normalizeAngle(tetaf, Math.PI);	

		pos[0] = xf;
		pos[1] = yf;
		pos[2] = tetaf;

		return pos;
    }

    private static double [] supremeNewPos(double [] lastPos, double lastDelta) {
    	
    	double teta0, x0, y0;

    	x0 = lastPos[0];
    	y0 = lastPos[1];
    	teta0 = lastPos[2];

    	double xf, yf, tetaf, delta_teta, delta_s, raioDaRoda, tachoB, tachoC, sum;
		double [] pos = new double[3];

		raioDaRoda = 2.8;
    	teta0 = Math.toRadians(teta0);
    	tachoB = rb.getTachoCount();
    	tachoC = rc.getTachoCount();
	
    	tachoB = Math.toRadians(tachoB);
    	tachoC = Math.toRadians(tachoC);
	
    	// 11.2 é a distancia entre eixos
    	delta_teta = (tachoB - tachoC) * (raioDaRoda) / (11.2);
	
		sum = teta0 + delta_teta / 2;
		sum = normalizeAngle(sum, Math.PI);	

    	delta_s = (tachoB + tachoC) * raioDaRoda/2;
    	delta_s = delta_s - lastDelta;

    	xf = x0 + delta_s * Math.cos(sum);
    	yf = y0 + delta_s * Math.sin(sum);

    	tetaf = teta0 + delta_teta;
		tetaf = normalizeAngle(tetaf, Math.PI);	

		pos[0] = xf;
		pos[1] = yf;
		pos[2] = tetaf;

		lastDelta = (tachoB + tachoC) * raioDaRoda / 2;

		return pos;
    }

    private static boolean isOnLine (double [] lastPos){
    	
    	double eps = 5;
    	double diff;
    	double [] pos = newPos(lastPos);
	
    	diff = Math.abs(pos[1] - pos[0] * 0.77);
    
    	if (diff < eps)
		    return true;
    	else 
	    	return false;
    }

    private static double updateDelta() {

    	double delta_teta, tachoB, tachoC;
    	double raioDaRoda = 2.8;
	
    	tachoB = rb.getTachoCount();
    	tachoC = rc.getTachoCount();
	
    	tachoB = Math.toRadians(tachoB);
    	tachoC = Math.toRadians(tachoC);

		delta_teta = (tachoB - tachoC)*(raioDaRoda)/(11.2);

		return delta_teta;
    }

    private static void printPos(double [] myPos){
		RConsole.println(" ");
		RConsole.println(" ");
		RConsole.println(" ");
		RConsole.println("x: "+ myPos[0]);
		RConsole.println("y: "+ myPos[1]);
		RConsole.println("teta: "+ myPos[2]);
		RConsole.println(" "+ myPos[2]);
		RConsole.println(" ");
		RConsole.println(" ");
		RConsole.println(" ");
    }

    public static void main(String args[]) {
		
		int u_linha, turn, erroant, erro;
		double KP, KD, light_v, actualDelta;
		boolean boolb, boolc;
		boolean FINISHED = false;

		double [] pos = new double[3];
		double [] actualPos = new double[3];

		RConsole.openAny(0);

		light = new LightSensor(SensorPort.S4);
		mB = new NXTMotor(MotorPort.B);
		mC = new NXTMotor(MotorPort.C);
		rb = new NXTRegulatedMotor(MotorPort.B);
		rc = new NXTRegulatedMotor(MotorPort.C);

		Button.waitForAnyPress();

		rb.rotate(260, true);
		rc.rotate(-260);
		
		rb.resetTachoCount();
		rc.resetTachoCount();

		u_linha = 40; 	// 50
		KP = 6; 		// 6
		KD = 3; 		// 3

		erroant = 0;
		light_v = 0;
			
		rb.setSpeed(200);
		rc.setSpeed(200);
		
		// position 
		actualPos[0] = 0;
		actualPos[1] = 0;
		actualPos[2] = 37;
		actualDelta = 0.0;

		while (!FINISHED) { 

			// regulated
			rb.forward();
			rc.forward();
			
			// get light
			light_v = light.getLightValue();

			// black
			if (light_v < 40) {

				rb.stop(true);
				rc.stop();

				// first black position
				actualDelta = updateDelta();
				actualPos = supremeNewPos(actualPos, actualDelta);

				printPos(actualPos);

				// turn to some side
				rb.rotateTo(180, true);
				rc.rotate(-180);

				// go forward and stop
				rb.forward();
				rc.forward();
				Delay.msDelay(5000);
				rb.stop(true);
				rc.stop();

				// change from regulated to unregulated
				boolb = rb.suspendRegulation(); 
				boolc = rc.suspendRegulation();

				//		 first black position
				actualDelta = updateDelta();
				actualPos = supremeNewPos(actualPos, actualDelta);

				printPos(actualPos);

				FINISHED = true;

				// follow the thin black line using unregulated: CPD
				// while(!Button.ESCAPE.isDown()) {
				
				// 	erro = 45 - light.getLightValue();
				// 	turn = (int) (KP * erro + KD * (erroant - erro));
				// 	mB.setPower(u_linha + turn);
				// 	mC.setPower(u_linha - turn);

				// 	// if (isOnLine(actualPos)) {
				// 	// 	RConsole.println("Tamo na linha!");
				// 	//	pos = newPos(pos);				
				// 	// 	actualDelta = updateDelta();
				// 	// 	actualPos = supremeNewPos(actualPos, actualDelta);
				// 	// }	

				// 	//  pos = updatePos(pos);

		  //  			// Delay.msDelay(200);
				// 	// actualDelta = updateDelta();
				// 	// actualPos = supremeNewPos(actualPos, actualDelta);
				// 	// printPos(actualPos);

				// 	erroant = erro;
			 //    }				
		    }

			// if (isOnLine()){
			//     RConsole.println("Tamo na linha!");
			// }

			// else {
			//     RConsole.println("Não estamos!");
			// }

			// if (Button.ESCAPE.isDown())
			//     {
			// 	FINISHED = true;
				
			//     }
			//}

	    }		
    }	
}

