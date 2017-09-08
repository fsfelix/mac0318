import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;


public class bug2 {

    static light  = new LightSensor(SensorPort.S4);
    static mB 		= new NXTMotor(MotorPort.B);
    static mC 		= new NXTMotor(MotorPort.C);
    static rb 		= new NXTRegulatedMotor(MotorPort.B);
    static rc 		= new NXTRegulatedMotor(MotorPort.C);

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

    public boolean isOnLine (Position pos) {

        double eps = 5;
        double diff;

        diff = Math.abs(pos.y - pos.x * 0.77); // verifica se pertence a reta

        if (diff < eps)
            return true;
        else
            return false;
    }

    public static void line_CPD (Position pos_0) {

        int u_linha, turn, erro, erroant;
        double KP, KD;
        boolean stop = false;
        Position pos_f = new Position(pos_0.x, pos_0.y, pos_0.teta);

        erro 		  = 0;
        erroant 	= 0;
        u_linha 	= 40; 	// 50
        KP 			  = 6;	// 6
        KD 			  = 3;	// 3

        while (!stop) {

            erro = 45 - light.getLightValue();
            turn = (int) (KP * erro + KD * (erroant - erro));
            mB.setPower(u_linha + turn);
            mC.setPower(u_linha - turn);
            erroant = erro;

            pos_f.updatePosition(rb, rc);

            if (isOnLine(pos_f) && isCloserToG(pos_0, pos_f)) {
                stop = true;
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
        Delay.msDelay(5000);
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

        // set initial position
        Position pose = new Position(0, 0, 37);

        while (!FINISHED) {

            // regulated
            rb.forward();
            rc.forward();

            // black
            if (light.getLightValue() < 40) {
                pose.updatePosition(rb, rc);

                rb.stop(true);
                rc.stop();

                boolb = rb.suspendRegulation();
                boolc = rc.suspendRegulation();

                line_CPD(pose);

            }


        }
    }
}
