import lejos.nxt.NXTRegulatedMotor;

public class Position {

    public double x                 = 0.0;
    public double y                 = 0.0;
    public double teta              = 0.0;
    public double accum_delta_s     = 0.0;
    public double raioDaRoda        = 2.8;
	public double dist_entre_eixos  = 11.2;

    // construtor (x, y, pose). pose em graus.
    public Position (int a, int b, double c) {
        x = a;
        y = b;
        teta = c;
    }

    // normalizadora de angulos
    public static double normalizeAngle(double a, double center) {
        return a - (2 * Math.PI) * Math.floor((a + Math.PI - center) / (2 * Math.PI));
    }

    // atualizar posicao, passando motores como parametro
    public void updatePosition(NXTRegulatedMotor rb, NXTRegulatedMotor rc) {

		double x0, y0, xf, yf, teta0, tetaf;
		double delta_teta, delta_s, tachoB, tachoC, sum;

        // posicao atual
    	x0 = this.x;
    	y0 = this.y;
    	teta0 = this.teta;

        // obter os tacometros que indicam todo o delta_s
    	tachoB = rb.getTachoCount();
    	tachoC = rc.getTachoCount();

        // tudo em radianos
    	tachoB = Math.toRadians(tachoB);
    	tachoC = Math.toRadians(tachoC);
        teta0 = Math.toRadians(teta0);

        // angulo = pose
    	delta_teta = (tachoB - tachoC) * (raioDaRoda) / dist_entre_eixos;

		sum = teta0 + delta_teta / 2;
		sum = normalizeAngle(sum, Math.PI);

    	delta_s = (tachoB + tachoC) * raioDaRoda / 2;

        // delta_s do ultimo periodo que o robo andou!
    	delta_s = delta_s - this.accum_delta_s;

        // nova posicao, considerando apenas o delta_s do ultimo movimento
    	xf = x0 + delta_s * Math.cos(sum);
    	yf = y0 + delta_s * Math.sin(sum);

    	tetaf = teta0 + delta_teta;
		tetaf = normalizeAngle(tetaf, Math.PI);

        // atualizar posição
		this.x = xf;
		this.y = yf;
		this.teta = tetaf;

        // atualizar o delta_s acumulado
        this.accum_delta_s = (tachoB + tachoC) * raioDaRoda / 2;
    }

    // verifica se pertence a reta. epsilon e coeficiente angular hardcoded.
    public boolean belongsToLine () {

		double eps = 5;
		double diff;

		diff = Math.abs(this.y - this.x * 0.77); // verifica se pertence a reta

		if (diff < eps)
			return true;
		else
			return false;
	}
}
