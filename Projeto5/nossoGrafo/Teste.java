// import lejos.pc.comm.*;
// import lejos.geom.*;
// import lejos.robotics.mapping.LineMap;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
// import lejos.util.Delay;

public class Teste {

    public static class Point {
        double x, y;
        public Point (double x, double y){
            this.x = x;
            this.y = y;
        }
    }

    private static Point[] points = {
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


    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    public static void main(String[] args) {
        ArrayList <Integer> path = new ArrayList <Integer> ();

        Graph graph = new Graph (11);
        graph.addBothEdges(0, 1, distance(points[0].x, points[0].y, points[1].x, points[1].y));
        graph.addBothEdges(1, 2, distance(points[1].x, points[1].y, points[2].x, points[2].y));
        graph.addBothEdges(2, 3, distance(points[2].x, points[2].y, points[3].x, points[3].y));
        graph.addBothEdges(1, 5, distance(points[1].x, points[1].y, points[5].x, points[5].y));
        graph.addBothEdges(5, 6, distance(points[5].x, points[5].y, points[6].x, points[6].y));
        graph.addBothEdges(4, 5, distance(points[4].x, points[4].y, points[5].x, points[5].y));
        graph.addBothEdges(3, 4, distance(points[3].x, points[3].y, points[4].x, points[4].y));
        graph.addBothEdges(3, 9, distance(points[3].x, points[3].y, points[9].x, points[9].y));
        graph.addBothEdges(4, 9, distance(points[4].x, points[4].y, points[9].x, points[9].y));
        graph.addBothEdges(7, 10, distance(points[7].x, points[7].y, points[10].x, points[10].y));
        graph.addBothEdges(10, 9, distance(points[10].x, points[10].y, points[9].x, points[9].y));

        path = graph.Dijkstra(0, 3);

        int[] intArray = new int[path.size()];
    
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = path.get(i) + 1;
            System.out.println(intArray[i]);      
        }        
    

    }

}
