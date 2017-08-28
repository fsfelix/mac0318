 import lejos.nxt.Button;
 import lejos.nxt.Motor;

public class MotorTutor1Remote
 {
      public static void main(String[] args)
      {
           System.out.println("Programa 1");
           Button.waitForAnyPress();
           Motor.A.forward();
           System.out.println("FORWARD");
           Button.waitForAnyPress();
           System.out.println("BACKWARD");
           Motor.A.backward();
           Button.waitForAnyPress();
           Motor.A.stop();     
      }
 }


