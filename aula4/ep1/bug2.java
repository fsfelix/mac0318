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
        Delay.msDelay(5000);
        rb.stop(true);
        rc.stop();

        // print final position
        pos.updatePosition(rb, rc);
        printPos(pos);
    }

    public static void main(String args[]) {

        double light_v;
        boolean boolb, boolc;
        boolean FINISHED = false;

        light 	= new LightSensor(SensorPort.S4);
        mB 		= new NXTMotor(MotorPort.B);
        mC 		= new NXTMotor(MotorPort.C);
        rb 		= new NXTRegulatedMotor(MotorPort.B);
        rc 		= new NXTRegulatedMotor(MotorPort.C);

        RConsole.openAny(0);
        Button.waitForAnyPress();

        // giro inicial
        rb.rotate(260, true);
        rc.rotate(-260);

        rb.resetTachoCount();
        rc.resetTachoCount();

        light_v = 0;
        rb.setSpeed(200);
        rc.setSpeed(200);

        // set initial position
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
