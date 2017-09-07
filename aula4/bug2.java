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

    	delta_teta = (tachoB - tachoC)*(raioDaRoda)/(11.2);

		sum = teta0 + delta_teta/2;
		sum = normalizeAngle(sum, Math.PI);

    	delta_s = (tachoB + tachoC)*raioDaRoda/2;
    	xf = x0 + delta_s*Math.cos(sum);
    	yf = y0 + delta_s*Math.sin(sum);

    	tetaf = teta0 + delta_teta;
		tetaf = normalizeAngle(tetaf, Math.PI);

		pos[0] = xf;
		pos[1] = yf;
		pos[2] = tetaf;

		return pos;
  	}

    private static double [] supremeNewPos(Position lastPos, double lastDelta) {

		double dist = 11.2;  // distancia entre eixos
		double teta0, x0, y0, xf, yf, tetaf;
		double delta_teta, delta_s, raioDaRoda, tachoB, tachoC, sum;
		double [] pos = new double[3];

    	x0 = lastPos.x;
    	y0 = lastPos.y;
    	teta0 = lastPos.teta;
		raioDaRoda = 2.8;

    	teta0 = Math.toRadians(teta0);
    	tachoB = rb.getTachoCount();
    	tachoC = rc.getTachoCount();

    	tachoB = Math.toRadians(tachoB);
    	tachoC = Math.toRadians(tachoC);

    	delta_teta = (tachoB - tachoC) * (raioDaRoda) / dist;

		sum = teta0 + delta_teta / 2;
		sum = normalizeAngle(sum, Math.PI);

    	delta_s = (tachoB + tachoC) * raioDaRoda / 2;
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

    private static double updateDeltaS() {

		// gets the FULL DELTA_S

		double delta_s, tachoB, tachoC;
    	double raioDaRoda = 2.8;

    	tachoB = rb.getTachoCount();
    	tachoC = rc.getTachoCount();

    	tachoB = Math.toRadians(tachoB);
    	tachoC = Math.toRadians(tachoC);

    	delta_s = (tachoB + tachoC)*raioDaRoda/2;

		return delta_s;
    }

	public static void unregulatedCPD() {

		int u_linha, turn, erro, erroant;
		double KP, KD;

		while(!Button.ESCAPE.isDown()) {
			erro = 45 - light.getLightValue();
			turn = (int) (KP * erro + KD * (erroant - erro));
			mB.setPower(u_linha + turn);
			mC.setPower(u_linha - turn);
			erroant = erro;
		}
	}

	private static boolean isOnLine (Position pos) {

		double eps = 5;
		double diff;

		diff = Math.abs(pos.y - pos.x * 0.77); // verifica se pertence a reta

		if (diff < eps)
			return true;
		else
			return false;
	}

    private static void printPos(double [] myPos) {
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

	public static void locationTester(double [] actualPos, double actualDelta) {

		// initial position, print it
		actualPos = supremeNewPos(actualPos, actualDelta);
		actualDelta = updateDeltaS();
		printPos(actualPos);

		// turn to some side
		rb.rotateTo(180, true);
		rc.rotate(-180);

		// go forward and stop after 5 seconds
		rb.forward();
		rc.forward();
		Delay.msDelay(5000);
		rb.stop(true);
		rc.stop();

		// final position print it
		actualPos = supremeNewPos(actualPos, actualDelta);
		actualDelta = updateDeltaS();
		printPos(actualPos);
	}

    public static void main(String args[]) {

		int u_linha, turn, erroant, erro;
		double KP, KD, light_v, lastUsedDelta;
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

		// giro inicial
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
		actualPos[0]	= 0;
		actualPos[1] 	= 0;
		actualPos[2] 	= 37;
		lastUsedDelta 	= 0.0;

		Position ponto = new Position(0, 0, 37);

		while (!FINISHED) {

			// regulated
			rb.forward();
			rc.forward();

			// get light
			light_v = light.getLightValue();

			// black
			if (light_v < 40) {

				// change from regulated to unregulated
				rb.stop(true);
				rc.stop();

				boolb = rb.suspendRegulation();
				boolc = rc.suspendRegulation();

				unregulatedCPD();
		    }
	    }
    }
}
