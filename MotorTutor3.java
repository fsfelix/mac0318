 import lejos.nxt.Button;
 import lejos.nxt.LCD;
 import lejos.nxt.Motor;
 import lejos.util.Delay;
 
 public class MotorTutor3 {
       public static void main(String[] args) 
      {
           LCD.drawString("Program 3", 0, 0);
           Button.waitForAnyPress();
           LCD.clear();   
           Motor.B.rotate(100);
           LCD.drawInt(Motor.B.getTachoCount(),0,0);
           Motor.B.rotateTo(0);
           LCD.drawInt(Motor.B.getTachoCount(),0,1);
           Button.waitForAnyPress();
      }
 }

