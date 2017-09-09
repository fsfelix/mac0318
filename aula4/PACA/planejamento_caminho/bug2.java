/*
  Felipe Silva Felix - 8941092
  Lucas Helfstein Rocha - 8802426
*/

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


    public static boolean isOnLine (Position pos) {

        double eps = 2;
        double diff;

        diff = Math.abs(pos.y - pos.x * 0.77); // verifica se pertence a reta

        if (diff < eps)
            return true;
        else
            return false;
    }

    public static boolean isCloserToG(Position pos_0, Position pos_f) {
        return pos_f.x > pos_0.x;
    }

    public static void fixHeading(Position pose) {


        if (mB.getTachoCount () < mC.getTachoCount()) {
            mB.setPower(40);
            mC.setPower(0);
        }

        else {
            mC.setPower(40);
            mB.setPower(0);
        }

        while(mB.getTachoCount() != mC.getTachoCount()) {
        }

        pose.updateTeta(rb, rc);

        mB.setPower(0);
        mC.setPower(0);

    }

    public static Position line_CPD (Position pos_0) {

        int u_linha, turn, erro, erroant;
        double KP, KD;
        boolean stop = false;
        long start_t;
        Position pos_f = new Position(pos_0.x, pos_0.y, pos_0.teta, pos_0.tacho[0], pos_0.tacho[1]);
	
        erro 		  = 0;
        erroant 	  = 0;
        u_linha 	  = 30; 
        KP 			  = 3;	
        KD 			  = 1.5;
        start_t       = System.currentTimeMillis();

        while (!stop) {

            erro = 50 - light.getLightValue();
            turn = (int) (KP * erro + KD * (erroant - erro)/20.0);
            mB.setPower(u_linha + turn);
            mC.setPower(u_linha - turn);
            erroant = erro;

            Delay.msDelay(20);
            pos_f.updatePosition(rb, rc);
            printPos(pos_f);

            if (isOnLine(pos_f) && isCloserToG(pos_0, pos_f) && (System.currentTimeMillis() - start_t > 2000)) {
                stop = true;
                pos_0 = new Position (pos_f.x, pos_f.y, pos_f.teta, pos_f.tacho[0], pos_f.tacho[1]);
            }

        }

        return pos_0;
    }

    private static void printPos(Position pos) {
        RConsole.println(pos.x + " " + pos.y + " " + pos.teta * 180 / Math.PI );
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

        // set initial position
        Position pose = new Position(0, 0, 0.66, 0, 0);
        printPos(pose);

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

                pose = line_CPD(pose);
                fixHeading(pose);
            }

            pose.updatePosition(rb, rc);
            printPos(pose);

            if (pose.y > 70.0 && pose.x > 90.0)
                FINISHED = true;

        }
    }
}
