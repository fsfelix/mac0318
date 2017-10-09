 import lejos.nxt.Button;
 import lejos.nxt.LCD;
import lejos.nxt.Motor;
 import lejos.util.Delay;
 public class MotorTutor2 {
     public static void main(String[] args) 
     {
          LCD.drawString("Programa 2", 0, 0);
          Button.waitForAnyPress();
          Motor.B.setSpeed(4000);
          Motor.B.forward();
          LCD.clear();
          Delay.msDelay(2000);
          LCD.drawInt(Motor.B.getTachoCount(),0,0);
          Motor.B.stop();
          LCD.drawInt(Motor.B.getTachoCount(),0,1);
          Motor.B.backward();
          while (Motor.B.getTachoCount()>0); 
          LCD.drawInt(Motor.B.getTachoCount(),0,2);
          Motor.B.stop();
          LCD.drawInt(Motor.B.getTachoCount(),0,3);
          Button.waitForAnyPress();
     }
}

