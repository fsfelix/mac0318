import lejos.geom.*;

public class Teste {

	public static void main(String[] args) {
        
        Discrete d = new Discrete(1, 1);
        Point p = new Point(50, 0);
        
        if (d.isObstacle(p))
        	System.out.println("Obstáculo!");
        else
        	System.out.println("Não é obstáculo!");
    }

}
