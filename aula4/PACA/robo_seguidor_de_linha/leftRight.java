/*
  Felipe Silva Felix - 8941092
  Lucas Helfstein Rocha - 8802426
*/

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;

public class leftRight {
    static LightSensor light;
    static NXTMotor mB;
    static NXTMotor mC;

    public static void main(String args[])
    {
        RConsole.openAny(0);
        light = new LightSensor(SensorPort.S4);
        mB = new NXTMotor(MotorPort.B);
        mC = new NXTMotor(MotorPort.C);
        Button.waitForAnyPress();

        while(!Button.ESCAPE.isDown()){

            while (light.getLightValue () < 40 && !Button.ESCAPE.isDown())
                {
                    mB.setPower(60);
                    mC.setPower(1);
                    RConsole.println("PRIMEIRO: " + light.getLightValue());
                }

            while (light.getLightValue () >= 40 && !Button.ESCAPE.isDown())
                {
                    mC.setPower(60);
                    mB.setPower(1);
                    RConsole.println("SEGUNDO: "+light.getLightValue());

                }

        }

    }
}

