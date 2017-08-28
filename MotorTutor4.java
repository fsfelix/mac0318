 import lejos.nxt.Button;
 import lejos.nxt.LCD;
 import lejos.nxt.Motor;
 import lejos.util.Delay;
 public class MotorTutor4 {

      public static void main(String[] args) {
           LCD.drawString("Programa 4", 0, 0);
           Button.waitForAnyPress();
           while(Button.readButtons()>0);
           LCD.clear();
           Motor.B.rotateTo(1440, true);
           while(Motor.B.isMoving())
           {
             Delay.msDelay(200);
             LCD.drawInt(Motor.B.getTachoCount(),0,0);
              if(Button.readButtons()>0) Motor.B.stop(true);
           }
           LCD.drawInt(Motor.B.getTachoCount(),0,1);
 
           Button.waitForAnyPress();     
      }
 }
