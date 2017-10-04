import java.util.ArrayList;
import java.util.Collections;
import lejos.geom.*;
import lejos.robotics.mapping.LineMap;

public class Visibility {
    public static Point init;
    public static Point goal;
    public static Line[] lines;

    public Visibility (Point i, Point g, Line[] l) {
        this.init = i;
        this.goal = g;
        this.lines = l;
    }

    public static ArrayList <Point> getAllPoints() {
        ArrayList <Point> allPoints = new ArrayList <Point> ();
        allPoints.add(init);
        allPoints.add(goal);

        for (Line l : lines) {
            allPoints.add(l.getP1());
            allPoints.add(l.getP2());

        }
        return allPoints;
    }

    public static ArrayList <Point> getLinePoints() {
        ArrayList <Point> linePoints = new ArrayList <Point> ();
        for (Line l : lines) {
            linePoints.add(l.getP1());
            linePoints.add(l.getP2());
        }
        return linePoints;
    }

    public static boolean pointsAreEqual(Point a, Point b) {
        return (a.x == b.x && a.y == b.y);
    }

    public static void addAllLines(ArrayList <Line> map) {
        for (Line l : lines)
            map.add(l);
    }

    public static void dilateLines(ArrayList <Line> map) {
        float RATIO = (float) 1.5;
        for (Line l : lines) {
            Point p1 = l.getP1();
            Point p2 = l.getP2();
            map.add(new Line(p1.x * RATIO, p1.y * RATIO, p2.x * RATIO, p2.y * RATIO));
        }
    }

    public static ArrayList <Line> createMap() {
        ArrayList <Point> allPoints = getAllPoints();
        ArrayList <Point> linePoints = getLinePoints();
        ArrayList <Line>  map = new ArrayList <Line> ();

        Point[] allPointsArray = new Point[allPoints.size()];
        allPointsArray = allPoints.toArray(allPointsArray);
        addAllLines(map);
        // dilateLines(map);

        for (Point p1 : allPoints) {
            System.out.println("all");
            System.out.println(p1);
            for (int i = 0; i < allPointsArray.length; i++) {
                Point p2 = allPointsArray[i];
                // System.out.println(i);
                // System.out.println(allPointsArray.length);
                if (!pointsAreEqual(p1, p2)) {
                    float ratio = (float) 1.01;
                    Line tmp = new Line(p1.x*ratio, p1.y*ratio, p2.x*ratio, p2.y*ratio);
                    boolean inters = false;

                    for (Line l : lines) {
                        if (l.intersectsAt(tmp) != null) {
                            inters = true;
                            break;
                        }
                    }

                    if (!inters) {
                        //System.out.println("Dentro do if");
                        //System.out.println(tmp.x1 + " " + tmp.y1 + " " + tmp.x2 +  " " + tmp.y2);
                        //Line newl = new Line();
                        map.add(tmp);
                    }
                }
            }
        }

        return map;
    }

    public static void main(String[] args) {
        Line[] linesMap = {
            new Line(170,437,60,680),
            new Line(60,680,398,800),
            new Line(398,800,450,677),
            new Line(450,677,235,595),
            new Line(235,595,281,472),
            new Line(281,472,170,437),
            new Line(1070,815,770,602),
            new Line(770,602,1060,516),
            new Line(1070,815,1060,516),
            new Line(335,345,502,155),
            new Line(502,155,700,225),
            new Line(700,225, 725,490),
            new Line(725,490,480,525),
            new Line(480,525,335,345),

        };

        Point[] points = {
            new Point(100,813),    /* P1 */
            new Point(428,873),   /* P2 */
            new Point(1140,885),  /* P3 */
            new Point(1117,432),  /* P4 */
            new Point(830,507),   /* P5 */
            new Point(690,571),   /* P6 */
            new Point(450,593),   /* P7 */
            new Point(263,350),   /* P8 */
            new Point(531,382),   /* P9 */
            new Point(986,166),    /* P10 */
            new Point(490,100)     /* P11 */
        };
        ArrayList <Line>  map = new ArrayList <Line> ();

        Visibility vsl = new Visibility(points[0], points[10], linesMap);
        map = vsl.createMap();

        for (Line l : map) {
            System.out.println(l.x1 + " " + l.y1 + " " + l.x2 +  " " + l.y2);
        }

        Line[] mapRes = new Line[map.size()];
        mapRes = map.toArray(mapRes);

        Rectangle bounds = new Rectangle(0, 0, 1195, 920);
        LineMap mymap = new LineMap(mapRes, bounds);
        try{
            mymap.createSVGFile("mapa.svg");
            mymap.flip().createSVGFile("mapaFlipY.svg"); //creates a fliped version in the Y-axis of the orginal image
        }catch (Exception e){
            System.out.print("Exception caught: ");
            System.out.println(e.getMessage());
        }
    }
}
