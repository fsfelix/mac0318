import java.util.ArrayList;
import java.util.Collections;
import lejos.geom.*;
import lejos.robotics.mapping.LineMap;

public class Visibility {
    public static Point init;
    public static Point goal;
    public static Line[] lines;
    public static Graph graph;
    public static ArrayList <Line> map;
    public static ArrayList <Line> mapOriginal;
    public static ArrayList <Line> mapDilated;
    public static ArrayList <Point> pointsFinal;
    public static Line[] linesMap = {
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

    // public static void dilateLines(ArrayList <Line> map) {
    //     float RATIO = (float) 1.5;
    //     for (Line l : lines) {
    //         Point p1 = l.getP1();
    //         Point p1New = new Point(p1.x * RATIO, p1.y *RATIO);
    //         Point p2 = l.getP2();
    //         Point p2New = new Point(p2.x * RATIO, p2.y *RATIO);
    //         float dist1 = (float) Math.sqrt( Math.pow(p1.x-p1New.x, 2) + Math.pow(p1.y - p1New.y, 2) );
    //         float dist2 = (float) Math.sqrt( Math.pow(p2.x-p2New.x, 2) + Math.pow(p2.y - p2New.y, 2) );
    //         double R = 20;

    //         for (double y = p1.y - R; y <= p1.y + R; y += R)
    //             for (double x = p1.x - R; x <= p1.x + R; x += R)
    //                 for (double y2 = p2.y - R; y2 <= p2.y + R; y2 += R)
    //                     for (double x2 = p2.x - R; x2 <= p2.x + R; x2 += R) {
    //                         //System.out.println(y + " " + x + " " + y2 + " " + x2);
    //                         Line tmp = new Line((float)x, (float)y, (float)x2, (float)y2);
    //                         if (tmp.length() > l.length() && tmp.intersectsAt(l) == null)
    //                             map.add(tmp);
    //                     }
    //         //map.add(new Line(p1.x * RATIO - dist1, p1.y * RATIO -dist1, p2.x * RATIO - dist2, p2.y * RATIO - dist2));
    //     }
    // }

    public static float distance(Point a, Point b){
        return (float) Math.sqrt(Math.pow( a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public static int numberOfDistinctPoints(ArrayList <Line> lines) {
        ArrayList <Point> points = new ArrayList <Point>();
        for (Line l : lines) {
            Point p1 = l.getP1();
            Point p2 = l.getP2();
            boolean p1Add = true;
            boolean p2Add = true;
            for (Point p : points) {
                if (pointsAreEqual(p, p1))
                    p1Add = false;
                if (pointsAreEqual(p, p2))
                    p2Add = false;
            }
            if (p1Add)
                points.add(p1);
            if (p2Add)
                points.add(p2);
        }
        return points.size();
    }

    public static void addWithCondition(Point a, ArrayList <Point> dilatedPoints, float eps) {
        boolean add = true;
        for (Point p : dilatedPoints) {
            if (distance(p, a) < 5*eps) {
                add = false;
                break;
            }
        }
        if (add)
            dilatedPoints.add(a);
    }

    public static ArrayList <Point> dilatedPoints() {
        ArrayList <Point> dilatedPoints = new ArrayList <Point>();
        pointsFinal = new ArrayList <Point>();
        mapDilated = new ArrayList <Line>();
        float eps = 10.0f;

        for (Line l : lines) {
            l.lengthen(eps);
            Point p1 = l.getP1();
            Point p2 = l.getP2();
            float m = (p1.y - p2.y)/(p1.x - p2.x);

            float b1 = p1.y + (1/m)*p1.x;
            float b2 = p2.y + (1/m)*p2.x;

            Line tmp1 = new Line(p1.x, p1.y, p1.x + eps/10, (p1.x + eps/10)*(-1/m) + b1);
            tmp1.lengthen(eps);

            Line tmp2 = new Line(p2.x, p2.y, p2.x + eps/10, (p2.x + eps/10)*(-1/m) + b2);
            tmp2.lengthen(eps);

            Point p3 = tmp1.getP1();
            Point p4 = tmp1.getP2();

            Point p5 = tmp2.getP1();
            Point p6 = tmp2.getP2();

            addWithCondition(p3, dilatedPoints, eps);
            addWithCondition(p4, dilatedPoints, eps);
            addWithCondition(p5, dilatedPoints, eps);
            addWithCondition(p6, dilatedPoints, eps);

            Line tmp3 = new Line(p3.x, p3.y, p5.x, p5.y);
            Line tmp4 = new Line(p4.x, p4.y, p6.x, p6.y);

            l.lengthen(-eps);
            mapDilated.add(l);
            mapDilated.add(tmp1);
            mapDilated.add(tmp2);
            mapDilated.add(tmp3);
            mapDilated.add(tmp4);

        }

        return dilatedPoints;
    }

    public static void addToGraph(Point [] Points, int i, int j) {
        graph.addBothEdges(i, j, (double) distance(Points[i], Points[j]));
    }

    public static void createMap() {
        ArrayList <Point> allPoints = getAllPoints();
        ArrayList <Point> linePoints = getLinePoints();
        ArrayList <Point> dilatedPoints = getLinePoints();
        map = new ArrayList <Line> ();

        Point[] allPointsArray = new Point[allPoints.size()];
        allPointsArray = allPoints.toArray(allPointsArray);
        // addAllLines(map);

        dilatedPoints = dilatedPoints();
        dilatedPoints.add(init);
        dilatedPoints.add(goal);

        for (Point p : dilatedPoints)
            pointsFinal.add(p);


        Point[] dilatedPointsArray = new Point[dilatedPoints.size()];
        dilatedPointsArray = dilatedPoints.toArray(dilatedPointsArray);
        graph = new Graph(dilatedPoints.size());


        for (int i = 0; i < dilatedPoints.size(); i++) {
            Point p1 = dilatedPointsArray[i];
            for (int j = 0; j < dilatedPoints.size(); j++) {
                if (i != j) {
                    Point p2 = dilatedPointsArray[j];
                    boolean inters = false;
                    Line tmp = new Line(p1.x, p1.y, p2.x, p2.y);
                    for (Line l : lines) {
                        if (l.intersectsAt(tmp) != null) {
                            inters = true;
                            break;
                        }
                    }
                    if (!inters) {
                        map.add(tmp);
                        addToGraph(dilatedPointsArray, i, j);
                    }
                }
            }
        }

        //        return map;
    }

    public static ArrayList <Point> findPath() {
        Visibility vsl = new Visibility(init, goal, linesMap);
        ArrayList <Integer> path = new ArrayList <Integer> ();
        ArrayList <Point> pathPoints = new ArrayList <Point> ();

        vsl.createMap();

        int indInit = pointsFinal.size() - 2;
        int indFinal = pointsFinal.size() - 1;

        path = graph.Dijkstra(indInit, indFinal);

        for (int i : path)
            pathPoints.add(pointsFinal.get(i));

        // Point[] pathArray = new Point[pathPoints.size()];
        // pathArray = pathPoints.toArray(pathArray);

        return pathPoints;
    }

    public static void main(String[] args) {

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
        //ArrayList <Line>  map = new ArrayList <Line> ();

        Visibility vsl = new Visibility(points[0], points[9], linesMap);
        //map = vsl.createMap();
        vsl.createMap();
        // for (Line l : map) {
        //     System.out.println(l.x1 + " " + l.y1 + " " + l.x2 +  " " + l.y2);
        // }

        graph.printGraph();

        int indInit = pointsFinal.size() - 2;
        int indFinal = pointsFinal.size() - 1;

        graph.Dijkstra(indInit, indFinal);

        Line[] mapRes = new Line[map.size()];
        mapRes = map.toArray(mapRes);

        Rectangle bounds = new Rectangle(0, 0, 1195, 920);
        LineMap mymap = new LineMap(mapRes, bounds);

        Line[] mapDilatedArray = new Line[mapDilated.size()];
        mapDilatedArray = mapDilated.toArray(mapDilatedArray);

        LineMap mapDilatedLineMap = new LineMap(mapDilatedArray, bounds);

        try{
            mymap.createSVGFile("mapa.svg");
            mymap.flip().createSVGFile("mapaFlipY.svg"); //creates a fliped version in the Y-axis of the orginal image
            mapDilatedLineMap.createSVGFile("mapaDilatado.svg");
            mapDilatedLineMap.flip().createSVGFile("mapaDilatadoFlip.svg"); //creates a fliped version in the Y-axis of the orginal image

        }catch (Exception e){
            System.out.print("Exception caught: ");
            System.out.println(e.getMessage());
        }
    }
}
