import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;


public class bug2 {

    static LightSensor light  = new LightSensor(SensorPort.S4);
    static NXTMotor mB	      = new NXTMotor(MotorPort.B);
    static NXTMotor mC	      = new NXTMotor(MotorPort.C);
    static NXTRegulatedMotor rb	= new NXTRegulatedMotor(MotorPort.B);
    static NXTRegulatedMotor rc = new NXTRegulatedMotor(MotorPort.C);

    public static void unregulatedCPD() {

        // dummy cpd. para ao apertar esc.

        int u_linha, turn, erro, erroant;
        double KP, KD;

        erro 		  = 0;
        erroant 	= 0;
        u_linha 	= 40; 	// 50
        KP 			  = 6;	// 6
        KD 			  = 3;	// 3

        while(!Button.ESCAPE.isDown()) {
            erro = 45 - light.getLightValue();
            turn = (int) (KP * erro + KD * (erroant - erro));
            mB.setPower(u_linha + turn);
            mC.setPower(u_linha - turn);
            erroant = erro;
        }
    }

    public static boolean isOnLine (Position pos) {

        double eps = 2;
        double diff;

        diff = Math.abs(pos.y - pos.x * 0.77); // verifica se pertence a reta

        if (diff < eps) {
	    printPos(pos);
	    RConsole.println("LINHA");

            return true;
	}
        else
            return false;
    }

    public static boolean isCloserToG(Position pos_0, Position pos_f) {
        return pos_f.x > pos_0.x;
    }

    public static double distanceToLine(Position pos) {
        // para estimar quantas verificações devem ser feitas
        double dist = Math.abs((0.77 * pos.x) + (-1.0 * pos.y)) / Math.sqrt((0.77 * 0.77) + 1);
        return dist;
    }

    public static double euclideanDistance(Position pos1, Position pos2) {
        // talvez seja util
        return Math.sqrt(Math.pow((pos1.x - pos2.x), 2) + Math.pow((pos1.y - pos2.y), 2));
    }

    public static void fixHeading(Position pose) {
        // boolean stop = false;
        // double  angle = 0.66;
        // double delta_tachoB, delta_tachoC;
        // double delta_teta, delta_s, tachoB, tachoC;

	
	if (mB.getTachoCount () < mC.getTachoCount()) {
	    mB.setPower(40);
	    mC.setPower(0);
	}
	else {
	    mC.setPower(40);
	    mB.setPower(0);
	
	}


        while(mB.getTachoCount() != mC.getTachoCount()) {
	    RConsole.println("RODANDO ");

        }

        mB.setPower(0);
        mC.setPower(0);

    }

    public static void line_CPD (Position pos_0) {

        int u_linha, turn, erro, erroant;
        double KP, KD;
        boolean stop = false;
        long start_t;
        Position pos_f = new Position(pos_0.x, pos_0.y, pos_0.teta, pos_0.tacho[0], pos_0.tacho[1]);

        erro 		  = 0;
        erroant 	  = 0;
        u_linha 	  = 30; 	// 50
        KP 			  = 2;	// 6
        KD 			  = 1.5;	// 3
        start_t   = System.currentTimeMillis();

        while (!stop) {

            erro = 50 - light.getLightValue();
            turn = (int) (KP * erro + KD * (erroant - erro)/20.0);
            mB.setPower(u_linha + turn);
            mC.setPower(u_linha - turn);
            erroant = erro;

	    Delay.msDelay(20);
	    pos_f.updatePosition(rb, rc);
	    // printPos(pos_f);

	    // if ((System.currentTimeMillis() - start_t) % 20 == 0) {
	    // 	pos_f.updatePosition(rb, rc);
	    // 	printPos(pos_f);
	    // }

            if (isOnLine(pos_f) && isCloserToG(pos_0, pos_f) && (System.currentTimeMillis() - start_t > 5000)) {
                stop = true;
                pos_0 = new Position (pos_f.x, pos_f.y, pos_f.teta, pos_f.tacho[0], pos_f.tacho[1]);
            }

        }

    }

    private static void printPos(Position pos) {
        RConsole.println(" ");
        RConsole.println(" ");
        RConsole.println(" ");
        RConsole.println("x: " + pos.x);
        RConsole.println("y: " + pos.y);
        RConsole.println("teta: " + pos.teta);
        RConsole.println(" ");
        RConsole.println(" ");
        RConsole.println(" ");
        RConsole.println(" ");
    }

    public static void locationTester(Position pos) {

        // Método para testar se Position e updatePosition estão funcionando!

        // print initial position
        printPos(pos);

        // maybe turn to some side
        // rb.rotateTo(180, true);
        // rc.rotate(-180);

        // go forward and stop after 5 seconds
        rb.forward();
        rc.forward();

        Delay.msDelay(3000);
        rb.stop(true);
        rc.stop();

        pos.updatePosition(rb, rc);
        printPos(pos);

        rb.setSpeed(300);
        rc.setSpeed(0);

        rb.forward();
        rc.forward();

        Delay.msDelay(1000);
        rb.stop(true);
        rc.stop();

        pos.updatePosition(rb, rc);
        printPos(pos);

        rb.setSpeed(3000);
        rc.setSpeed(3000);
	
	rb.forward();
        rc.forward();

        Delay.msDelay(100);
        rb.stop(true);
        rc.stop();

        // print final position
        pos.updatePosition(rb, rc);
        printPos(pos);
    }

    public static void main(String args[]) {

        boolean boolb, boolc;
        boolean FINISHED = false;

        RConsole.openAny(0);
        Button.waitForAnyPress();

        // giro inicial
        rb.rotate(260, true);
        rc.rotate(-260);

        rb.resetTachoCount();
        rc.resetTachoCount();

        rb.setSpeed(200);
        rc.setSpeed(200);

        // rb.setSpeed(0);
        // rc.setSpeed(0);

        // set initial position
        Position pose = new Position(0, 0, 0.66, 0, 0);
	
	// rb.flt();
	// rc.flt();
	
	// while (true) {
	//     pose.updatePosition(rb, rc);
	//     System.out.println("x: " + Math.round(pose.x));
	//     System.out.println("y: " + Math.round(pose.y)); 	    
	//     System.out.println("teta: " + Math.round(pose.teta));
	// }
        while (!FINISHED) {

            // regulated
            rb.forward();
            rc.forward();

            // black
            if (light.getLightValue() < 40) {
                rb.stop(true);
                rc.stop();

                pose.updatePosition(rb, rc);
		printPos(pose);

                boolb = rb.suspendRegulation();
                boolc = rc.suspendRegulation();

                line_CPD(pose);
                fixHeading(pose);
            }

	}

	// locationTester(pose);
	// boolb = rb.suspendRegulation();
	// boolc = rc.suspendRegulation();
	// fixHeading(pose);
        // pose.updatePosition(rb, rc);
	// printPos(pose);
    }
}
