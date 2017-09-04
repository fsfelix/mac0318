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

    private static double [] newPos(double x0, double y0, double teta0) {
    	double xf, yf, tetaf, delta_teta, delta_s, raioDaRoda, tachoB, tachoC;
	double [] pos = new double[3];

    	raioDaRoda = 2.8;
	
    	teta0 = Math.toRadians(teta0);

    	// tachoB = Motor.B.getTachoCount();
    	// tachoC = Motor.C.getTachoCount();
    	tachoB = rb.getTachoCount();
    	tachoC = rc.getTachoCount();
	
    	tachoB = Math.toRadians(tachoB);
    	tachoC = Math.toRadians(tachoC);
	
    	// 11.2 é a distancia entre eixos

    	delta_teta = (tachoB - tachoC)*(raioDaRoda)/(11.2);

    	delta_s = (tachoB + tachoC)*raioDaRoda/2;

    	xf = x0 + delta_s*Math.cos(teta0 + delta_teta/2);

    	yf = y0 + delta_s*Math.sin(teta0 + delta_teta/2);

    	tetaf = teta0 + delta_teta;

	pos[0] = xf;
	pos[1] = yf;
	pos[2] = tetaf;

	return pos;

    	// LCD.clear();
    	// LCD.drawString(Double.toString(xf),0,0);
    	// LCD.drawString(Double.toString(yf),0,1);
    	// LCD.drawString(Double.toString(tetaf),0,2);
    	// Button.waitForAnyPress();
    }


    public static void main(String args[])  
    {
	int u_linha, turn, erroant, erro;
	double KP, KD, y;
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

	pos[0] = 0;
	pos[1] = 0;
	pos[2] = 0;

	u_linha = 50; // 50
	KP = 3.5; //6
	KD = 1.0; //3

	erroant = 0;
	
	rb.setSpeed(150);
	rc.setSpeed(150);
	y = 0;

	while (!FINISHED)
	    { 
		
		// regulated
		rb.forward();
		rc.forward();

		pos = newPos(pos[0], pos[1], pos[2]);
		y = light.getLightValue();

		RConsole.println("x: "+pos[0]);
		RConsole.println("y: "+pos[1]);
		RConsole.println("teta: "+pos[2]);
		RConsole.println("light: "+y);		

		// black
		if (y < 40)
		{
		    rb.stop(true);
		    rc.stop();

			// regulated to unregulated
			boolb = rb.suspendRegulation(); 
			boolc = rc.suspendRegulation();

			// should it turn right or left ?? 
			// follow the thin black line...
			
			// follow the thin black line using unregulated: CPD
				while(!Button.ESCAPE.isDown())
								
				{
					erro = 45 - light.getLightValue();
					turn = (int) (KP*erro + KD*(erroant - erro));
					mB.setPower(u_linha - turn);
					mC.setPower(u_linha + turn);
					pos = newPos(pos[0], pos[1], pos[2]);
					
					/*if (pos[0] < 200 && CURVE){
						while (true) {
							mB.setPower(10);
							mC.setPower(10);						
						}
					}

					Helfs, tem que ver se ele voltou pra linha, ai ele vai reto. só que 
					quando roda a primeira vez ele não pode chegar esse if. mas provavelmente ele ira
					pq ainda não entrou muito na curva. entao temos que ter um timer pra liberar 
					a entrada nesse if.
					*/

					RConsole.println(""+light.getLightValue());
					RConsole.println("x: "+pos[0]);
					RConsole.println("y: "+pos[1]);
					RConsole.println("teta: "+pos[2]);
					RConsole.println("light: "+y);		
					
					erroant = erro;
				}

			FINISHED = true;
		
		}

		// if (pos[0] > 122*100)
		// {
		//     rb.stop(true);
		//     rc.stop();
		//     FINISHED = true;
		// }
		
	    }

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

