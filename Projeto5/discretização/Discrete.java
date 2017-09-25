import lejos.geom.*;
import lejos.robotics.mapping.LineMap;


public class Discrete {
	private int width;
	private int height;	

    Line[] lines = {
      
      /* L-shape polygon */
      new Line(170,437,60,680),
      new Line(60,680,398,800),
      new Line(398,800,450,677),
      new Line(450,677,235,595),
      new Line(235,595,281,472),
      new Line(281,472,170,437),
      
      /* Triangle */
      new Line(1070,815,770,602),
      new Line(770,602,1060,516),
      new Line(1070,815,1060,516),
      
      /* Pentagon */
      new Line(335,345,502,155),
      new Line(502,155,700,225),
      new Line(700,225, 725,490),
      new Line(725,490,480,525),
      new Line(480,525,335,345),


		new Line(0,0,100,0),
    };

    Rectangle bounds = new Rectangle(0, 0, 1195, 920); 
    LineMap mymap = new LineMap(lines, bounds);

	public Discrete (int w, int h){
		this.width 	= w;
		this.height = h;
	}

	public boolean isObstacle(Point p) {
		
		for (int i = 0; i < lines.length; i++) {

        	System.out.println(" P1: " + lines[i].getP1() + " P2: " + lines[i].getP2());
        	System.out.println(" X: " + (double) p.x + " Y: " + (double) p.y);
        	System.out.println(lines[i].contains((double) p.x, (double) p.y));
        	System.out.println();

			if (lines[i].contains((double) p.x, (double) p.y)) 
				return true;
		}

		return false;
	}


  // Ideia de discretizar todo a matriz e fazer uma linha em cada horizontal e vertical
  //  ver se essa linha corta algum objeto, e onde
  //  essa maneira é a de menor complexidade O(n²)



}
