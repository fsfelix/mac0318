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

    private static double [] newPos(double x0, double y0, double teta0) {
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

    private static boolean isOnLine (){
    	
    	double eps = 5;
    	double diff;
    	double [] pos = newPos(0, 0, 37);
	
	// RConsole.println("x: "+pos[0]);
	// RConsole.println("y: "+pos[1]);
	// RConsole.println("teta: "+pos[2]);
	
    	diff = Math.abs(pos[1] - pos[0] * 0.77);
	// RConsole.println("diff: " + diff); 
    	if (diff < eps)
	    return true;
    	else 
	    return false;
    }


    public static void main(String args[])  
    {
	int u_linha, turn, erroant, erro;
	double KP, KD, light_v;
	boolean boolb, boolc;
	boolean FINISHED = false;
	double [] pos = new double[3];
		
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

	pos[0] = 0;
	pos[1] = 0;
	pos[2] = 0;

	u_linha = 40; 	// 50
	KP = 6; 		// 6
	KD = 3; 		// 3

	erroant = 0;
		
	rb.setSpeed(400);
	rc.setSpeed(400);
	light_v = 0;

	while (!FINISHED)
	    { 
		// regulated
		rb.forward();
		rc.forward();
			
		light_v = light.getLightValue();

		// black
		if (light_v < 40)
		    {
			rb.stop(true);
			rc.stop();
			// // regulated to unregulated
			boolb = rb.suspendRegulation(); 
			boolc = rc.suspendRegulation();

			// // should it turn right or left ?? 
			// // follow the thin black line...
				
			// // follow the thin black line using unregulated: CPD
			while(!Button.ESCAPE.isDown())
			    {
				erro = 45 - light.getLightValue();
				turn = (int) (KP*erro + KD*(erroant - erro));
				mB.setPower(u_linha + turn);
				mC.setPower(u_linha - turn);
				
				if (isOnLine())
				    {
					RConsole.println("Tamo na linha!");
					pos = newPos(0, 0, 37);
					RConsole.println("x: "+pos[0]);
					RConsole.println("y: "+pos[1]);
					RConsole.println("teta: "+pos[2]);

				    }
				
			    }

			// 	/* Helfs, tem que ver se ele voltou pra linha, ai ele vai reto. só que 
			// 	quando roda a primeira vez ele não pode chegar esse if. mas provavelmente ele ira
			// 	pq ainda não entrou muito na curva. entao temos que ter um timer pra liberar 
			// 	a entrada nesse if. */

			// 	RConsole.println("x: "+pos[0]);
			// 	RConsole.println("y: "+pos[1]);
			// 	RConsole.println("teta: "+pos[2]);
			// 	RConsole.println("light: "+light_v);		
			// 	RConsole.println("");
			// 	erroant = erro;
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
	pos = newPos(0, 0, 37);
	RConsole.println("x: "+pos[0]);
	RConsole.println("y: "+pos[1]);
	RConsole.println("teta: "+pos[2]);
		
	// while(!Button.ESCAPE.isDown()){
	//     erro = 45 - light.getLightValue();
	//     turn = (int) (KP*erro + KD*(erroant - erro));
	//     mB.setPower(u_linha + turn);
	//     mC.setPower(u_linha - turn);
	//     RConsole.println(""+light.getLightValue());
	//     erroant = erro;
	// }

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

