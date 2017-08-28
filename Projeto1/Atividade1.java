/*
  Felipe Silva Felix nUSP 8941092
  Lucas Helfstein Rocha nUSP 8802426
  Projeto 1 - MAC0318
*/

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;
import lejos.nxt.Button;

public class Atividade1 {

    private static void Movimento1(int deltaT) {
	Motor.B.setSpeed(300);
	Motor.C.setSpeed(300);
	
	Motor.B.forward();
	Motor.C.forward();

	Delay.msDelay(deltaT);
	Motor.B.stop(true);
	Motor.C.stop();

    }

    private static void Movimento2(int deltaT) {
	Motor.B.setSpeed(300);
	Motor.C.setSpeed(400);
	
	Motor.B.forward();
	Motor.C.forward();

	Delay.msDelay(deltaT);

	Motor.B.stop(true);
	Motor.C.stop();
    }

    private static void Movimento3(int deltaT) {
	Motor.B.setSpeed(300);
	Motor.C.setSpeed(300);

	Motor.C.forward();
	Motor.B.backward();
	
	Delay.msDelay(deltaT);

	Motor.C.stop(true);
	Motor.B.stop();
    }
    // Calcula a nova posicao do robo dados (x,y,teta) 
    private static void newPos(double x0, double y0, double teta0) {
	double xf, yf, tetaf, delta_teta, delta_s, raioDaRoda, tachoB, tachoC;

	raioDaRoda = 2.8;
	
	teta0 = Math.toRadians(teta0);

	tachoB = Motor.B.getTachoCount();
	tachoC = Motor.C.getTachoCount();
	
	tachoB = Math.toRadians(tachoB);
	tachoC = Math.toRadians(tachoC);
	
	// 11.2 é a distancia entre eixos

	delta_teta = (tachoB - tachoC)*(raioDaRoda)/(11.2);

	delta_s = (tachoB + tachoC)*raioDaRoda/2;

	xf = x0 + delta_s*Math.cos(teta0 + delta_teta/2);

	yf = y0 + delta_s*Math.sin(teta0 + delta_teta/2);

	tetaf = teta0 + delta_teta;

	LCD.clear();
	LCD.drawString(Double.toString(xf),0,0);
	LCD.drawString(Double.toString(yf),0,1);
	LCD.drawString(Double.toString(tetaf),0,2);

	Button.waitForAnyPress();
    }

    public static void main(String[] args) {
	// Escolha o tipo de movimento descomentando alguma linha.

	// Movimento1(2500);
	// Movimento2(2500);
	Movimento3(1500);
	newPos(0,0,0);
    } 

}

