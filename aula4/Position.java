public class Position {

    public int x = 0;
    public int y = 0;
    public double teta = 0.0;
    public double accum_delta_s = 0;

    public double raioDaRoda = 2.8;
	public double dist = 11.2;  // distancia entre eixos

    //constructor
    public Position (int a, int b, double c) {
        x = a;
        y = b;
        teta = c;
    }

    public void updatePosition() {

		double teta0, x0, y0, xf, yf, tetaf;
		double delta_teta, delta_s, tachoB, tachoC, sum;

    	x0 = this.x;
    	y0 = this.y;
    	teta0 = this.teta;

    	teta0 = Math.toRadians(teta0);

    	tachoB = rb.getTachoCount();
    	tachoC = rc.getTachoCount();

    	tachoB = Math.toRadians(tachoB);
    	tachoC = Math.toRadians(tachoC);

    	delta_teta = (tachoB - tachoC) * (raioDaRoda) / dist;

		sum = teta0 + delta_teta / 2;
		sum = normalizeAngle(sum, Math.PI);

    	delta_s = (tachoB + tachoC) * raioDaRoda / 2;
    	delta_s = delta_s - this.accum_delta_s;                                 // delta_s do ultimo periodo que o robo andou!

    	xf = x0 + delta_s * Math.cos(sum);
    	yf = y0 + delta_s * Math.sin(sum);

    	tetaf = teta0 + delta_teta;
		tetaf = normalizeAngle(tetaf, Math.PI);

        // atualizar posição
		this.x = xf;
		this.y = yf;
		this.teta = tetaf;
        this.accum_delta_s = (tachoB + tachoC) * raioDaRoda / 2;                // atualizar o delta_s acumulado

		return pos;
    }

}
