import java.util.*;
import java.util.ArrayList;
import lejos.pc.comm.*;
import lejos.geom.*;
import lejos.robotics.mapping.LineMap;
import java.util.Scanner;
import java.io.*;
import lejos.util.Delay;
import java.util.PriorityQueue;



/*
                       1
                       ^
                       |
                       |
                       |
             8 <----- ROBO ----> 3
                       |
                       |
                       |
                       v
                       5


                      As diagonais são 2, 4, 6, 7.
                      Por definição, o robo inicia com
                      pose 3, como se ele estivesse apontando
                      para a direita.
 */

public class Discrete {
    private double width;
    private double height;
    private int M;                                          // linhas
    private int N;                                          // colunas
    public static double [][] map;
    public static double [][] costs;
    public static double [][] probMap;

    public static double custo0 = 0; // custo de manter-se na mesma direção
    public static double custo1 = 0.25; // custo de girar 45 graus
    public static double custo2 = 0.5; // custo de girar 90 graus
    public static double custo3 = 1; // custo de girar 180 graus

    public static Point[] points = {
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
        new Line(502,155,700,225),   // base do pentágono!
        new Line(700,225, 725,490),
        new Line(725,490,480,525),
        new Line(480,525,335,345),
    };

    Rectangle bounds = new Rectangle(0, 0, 1195, 920);
    LineMap mymap = new LineMap(lines, bounds);

    public Discrete (double w, double h){
        this.width 	= w;
        this.height = h;
        this.M = (int) Math.ceil(1195.0/h);
        this.N = (int) Math.ceil(920.0/w);
        this.map = new double[this.M][this.N];
        this.costs = new double[this.M][this.N];
        this.probMap = new double[this.M][this.N];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                this.map[i][j] = Double.POSITIVE_INFINITY;
                this.probMap[i][j] = 0;
                this.costs[i][j]  = Double.POSITIVE_INFINITY;
            }
        }

        populateMap();

        // Três convoluções
        probabilistic();
        probabilistic();
        probabilistic();
    }

    public double get_w (){
        return this.width;
    }

    public double get_h (){
        return this.height;
    }

    public void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void populateMap() {
        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.N; j++) {
                Line [] currentLines = new Line[4];
                currentLines[0] = new Line((int) Math.ceil(i * this.height), (int) Math.ceil(j * this.width), (int) Math.ceil(i * this.height), (int) Math.ceil((j + 1) * this.width));
                currentLines[1] = new Line((int) Math.ceil(i * this.height), (int) Math.ceil((j + 1) * this.width), (int) Math.ceil((i + 1) * this.height), (int) Math.ceil((j + 1) * this.width));
                currentLines[2] = new Line((int) Math.ceil((i + 1) * this.height), (int) Math.ceil((j + 1) * this.width), (int) Math.ceil((i + 1) * this.height), (int) Math.ceil(j * this.width));
                currentLines[3] = new Line((int) Math.ceil((i + 1) * this.height), (int) Math.ceil(j * this.width), (int) Math.ceil(i * this.height), (int) Math.ceil(j * this.width));
                loop:
                for (int iLine = 0; iLine < 4; iLine++) {
                    for (int iObj = 0; iObj < lines.length; iObj++) {                                                     
                        if (currentLines[iLine].intersectsAt(lines[iObj]) != null) {
                            this.map[i][j] = -1;
                            this.probMap[i][j] = 1;
                            break loop;
                        }
                    }
                }
            }
        }
    }

    public float sumNeighbours(int i, int j) {
        ArrayList <coord> coordList = new ArrayList <coord> ();
        int M = this.map.length;
        int N = this.map[0].length;
        float sum = 0;

        if (i - 1 >= 0)
            sum += 0.1*this.probMap[i - 1][j];
        if (i + 1 < M)
            sum += 0.1*this.probMap[i + 1][j];
        if (j - 1 >= 0)
            sum += 0.1*this.probMap[i][j - 1];
        if (j + 1 < N)
            sum += 0.1*this.probMap[i][j + 1];
        if (i - 1 >= 0 && j - 1 >= 0)
            sum += 0.05*this.probMap[i - 1][j - 1];
        if (i - 1 >= 0 && j + 1 < N)
            sum += 0.05*this.probMap[i - 1][j + 1];
        if (i + 1 < M && j - 1 >= 0)
            sum += 0.05*this.probMap[i + 1][j - 1];
        if (i + 1 < M && j + 1 < N)
            sum += 0.05*this.probMap[i + 1][j + 1];

        return sum;
    }

    public void probabilistic() {
        ArrayList <coord> neigh = new ArrayList <coord> ();
        double [][] newMap = new double[this.M][this.N];
        for (int i = 1; i < this.map.length - 1; i++) {
            for (int j = 1; j < this.map[0].length - 1; j++) {
                newMap[i][j] = 0.4*this.probMap[i][j] + sumNeighbours(i, j);
            }
        }

        for (int i = 0; i < this.M; i++) {
            for(int j = 0; j < this.N; j++) {
                this.probMap[i][j] = newMap[i][j];
            }
        }
    }

    public void thicken() {
        double [][] newMap = new double[this.M][this.N];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (this.map[i][j] == -1)
                    newMap[i][j] = -1;
                else
                    newMap[i][j] = 0;
            }
        }


        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.map[i][j] == -1.0) {

                    if (i - 1 >= 0 && this.map[i - 1][j] != -1.0)
                        newMap[i - 1][j] = -1.0;

                    if (i + 1 < this.M && this.map[i + 1][j] != -1.0)
                        newMap[i + 1][j] = -1.0;

                    if (j - 1 >= 0 && this.map[i][j - 1] != -1.0)
                        newMap[i][j - 1] = -1.0;

                    if (j + 1 < this.N && this.map[i][j + 1] != -1.0)
                        newMap[i][j + 1] = -1.0;

                    /*8 vizinhanca*/

                    if (i - 1 >= 0 && j - 1 >= 0 && this.map[i - 1][j - 1] != -1.0)
                        newMap[i - 1][j - 1] = -1.0;
                    if (i - 1 >= 0 && j + 1 < this.N && this.map[i - 1][j + 1] != -1.0)
                        newMap[i - 1][j + 1] = -1.0;
                    if (i + 1 < this.M && j - 1 >= 0  && this.map[i + 1][j - 1] != -1.0)
                        newMap[i + 1][j - 1] = -1.0;
                    if (i + 1 < this.M && j + 1 < this.N && this.map[i + 1][j + 1] != -1.0)
                        newMap[i + 1][j + 1] = -1.0;
                }

            }
        }
        this.map = newMap;
    }

    public static ArrayList <coord> getNeighbours (coord cur) {
        ArrayList <coord> neigh = new ArrayList <coord> ();
        int i = cur.x();
        int j = cur.y();
        int M = map.length;
        int N = map[0].length;

        if (i - 1 >= 0)
            neigh.add(new coord(i - 1, j));
        if (i + 1 < M)
            neigh.add(new coord(i + 1, j));
        if (j - 1 >= 0)
            neigh.add(new coord(i, j - 1));
        if (j + 1 < N)
            neigh.add(new coord(i, j + 1));
        if (i - 1 >= 0 && j - 1 >= 0)
            neigh.add(new coord(i - 1, j - 1));
        if (i - 1 >= 0 && j + 1 < N)
            neigh.add(new coord(i - 1, j + 1));
        if (i + 1 < M && j - 1 >= 0)
            neigh.add(new coord(i + 1, j - 1));
        if (i + 1 < M && j + 1 < N)
            neigh.add(new coord(i + 1, j + 1));
        return neigh;
    }


    public static int relativePose(coord current, coord n) {
        int c_i = current.x();
        int c_j = current.y();
        int n_i = n.x();
        int n_j = n.y();

        if (c_i - n_i == 1 && c_j - n_j == 1) {
            return 8;
        }
        if (c_i - n_i == 1 && c_j - n_j == 0) {
            return 1;
        }
        if (c_i - n_i == 1 && c_j - n_j == -1) {
            return 2;
        }
        if (c_i - n_i == 0 && c_j - n_j == -1) {
            return 3;
        }
        if (c_i - n_i == -1 && c_j - n_j == -1) {
            return 4;
        }
        if (c_i - n_i == -1 && c_j - n_j == 0) {
            return 5;
        }
        if (c_i - n_i == -1 && c_j - n_j == 1) {
            return 6;
        }
        if (c_i - n_i == 0 && c_j - n_j == 1) {
            return 7;
        }
        return -1;
    }

    public static void updateNeighbour(coord current, coord n, int poseAtual) {
        int c_i = current.x();
        int c_j = current.y();
        int n_i = n.x();
        int n_j = n.y();

        int relativePose = relativePose(current, n);
        int k = Math.abs(poseAtual - relativePose)%4;
        System.out.println(k);
        if (k == 0) {
            map[n_i][n_j] = map[c_i][c_j] + custo0;
            costs[n_i][n_j] = costs[c_i][c_j] + custo0;
        }

        if (k == 1) {
            map[n_i][n_j] = map[c_i][c_j] + custo1;
            costs[n_i][n_j] = costs[c_i][c_j] + custo1;
        }

        if (k == 2) {
            map[n_i][n_j] = map[c_i][c_j] + custo2;
            costs[n_i][n_j] = costs[c_i][c_j] + custo2;
        }

        if (k == 3) {
            map[n_i][n_j] = map[c_i][c_j] + custo3;
            costs[n_i][n_j] = costs[c_i][c_j] + custo3;
        }

        // if (current.x() != n.x() && current.y() != n.y()) {
        //     map[n.x()][n.y()] = map[current.x()][current.y()] + Math.sqrt(2);
        //     costs[n.x()][n.y()] = costs[current.x()][current.y()] + Math.sqrt(2);
        // }
        // else {
        //     //map[n.x()][n.y()] += map[current.x()][current.y()] + 1;
        //     map[n.x()][n.y()] = map[current.x()][current.y()] + 1;
        //     costs[n.x()][n.y()] = costs[current.x()][current.y()] + 1;
        // }

    }

    public static boolean checkIfCostDecreases(coord current, coord n, int poseAtual) {
        int c_i = current.x();
        int c_j = current.y();
        int n_i = n.x();
        int n_j = n.y();

        int relativePose = relativePose(current, n);
        int k = Math.abs(poseAtual - relativePose)%4;

        if (k == 0) {
            if (map[n_i][n_j] > map[c_i][c_j] + custo0)
                return true;
            else
                return false;
        }

        if (k == 1) {
            if (map[n_i][n_j] > map[c_i][c_j] + custo1)
                return true;
            else
                return false;
        }

        if (k == 2) {
            if (map[n_i][n_j] > map[c_i][c_j] + custo2)
                return true;
            else
                return false;
        }

        if (k == 3) {
            if (map[n_i][n_j] > map[c_i][c_j] + custo3)
                return true;
            else
                return false;
        }
        return false;
        // if (c_i != n_i && c_j != n_j) {
        //     if (map[n_i][n_j] > map[c_i][c_j] + Math.sqrt(2))
        //         return true;
        //     else
        //         return false;
        // }

        // else {
        //     if (map[n_i][n_j] > map[c_i][c_j] + 1)
        //         return true;
        //     else
        //         return false;
        // }

    }

    public static double distance(coord c1, coord c2) {
        return Math.sqrt(Math.pow(c1.x() - c2.x(), 2) + Math.pow(c1.y() - c2.y(), 2));
    }

    public static double evaluateFunction(coord n, coord goal) {
        int M = map.length;
        int N = map[0].length;
        double h = distance(n, goal)/distance(new coord(0,0), new coord(M - 1, N - 1));
        double p = probMap[n.x()][n.y()];
        double c = costs[n.x()][n.y()]/(M * N);
        double alpha = 1;
        // System.out.println("h " + h);
        // System.out.println("p " + p);
        // System.out.println("c " + c);
        // System.out.println("aqui dentro dando infinito? " + (c*p+h));
        //return c*p + h;
        return alpha*c + (1-alpha)*p + h;
    }

    public static int argmin(double [] array) {
        double min = Double.POSITIVE_INFINITY;
        int argmin = -1;
        for (int ii = 0; ii < array.length; ii++) {
            if (array[ii] < min) {
                min = array[ii];
                argmin = ii;
            }
        }
        return argmin;
    }

    public static void aStar(coord init, coord goal) {
        int M = map.length;
        int N = map[0].length;

        Comparator cmp = new Comparator <coord> () {
                public int compare(coord a, coord b) {
                    if (evaluateFunction(a, goal) > evaluateFunction(b, goal))
                        return 1;
                    if (evaluateFunction(a, goal) < evaluateFunction(b, goal))
                        return -1;
                    return 0;
                }
            };

        Comparator cmp_e = new Comparator <coord> () {
                public int compare(coord a, coord b) {
                    if (a.x() > b.x() && a.y() > b.y())
                        return 1;
                    if (a.x() < b.x() && a.y() < b.y())
                        return -1;
                    return 0;
                }
            };

        coord[][] prev = new coord[M][N];
        PriorityQueue <coord> pq = new PriorityQueue <coord> (M*N, cmp);
        PriorityQueue <coord> explored = new PriorityQueue <coord> (M*N, cmp_e);
        boolean found = false;
        pq.add(init);

        costs[init.x()][init.y()] = 0;
        map[init.x()][init.y()] = 0;
        int pose = 3;
        coord cur = pq.poll();
        while (!found) {
            explored.add(cur);
            for (coord n : getNeighbours(cur)) {
                if (!explored.contains(n)) {
                    //if (map[n.x()][n.y()] != -1 && map[n.x()][n.y()] > map[cur.x()][cur.y()] + 1) {
                    if (map[n.x()][n.y()] != -1 && checkIfCostDecreases(cur, n, pose)) {
                        updateNeighbour(cur, n, pose);

                        //System.out.println("pose antiga: " + pose + " pose nova " + nextPose(cur, n));
                        // pose = nextPose(cur, n);
                        pq.add(n);
                        // System.out.println(n.x() + " neigh " +n.y());
                        // System.out.println(cur.x() + " current " +cur.y());
                        prev[n.x()][n.y()] = cur;
                        //   }
                        if (n.equals(goal)) {
                            found = true;
                            explored.add(goal);
                            System.out.println("encontrei");
                            System.out.println("Goal: " + goal.x() + " " + goal.y());
                            System.out.println("Goal encontrado: " + n.x() + " " + n.y());
                            break;
                        }
                    }
                }
            }
            coord ant = new coord(cur.x(), cur.y());
            cur = pq.poll();
            pose = relativePose(ant, cur);
        }

        ArrayList <coord> path = new ArrayList <coord> ();
        coord tmp = goal;
        path.add(goal);
        while (tmp.x() != init.x() || tmp.y() != init.y()) {
            //System.out.println(tmp.x() + " " + tmp.y());
            tmp = prev[tmp.x()][tmp.y()];
            //System.out.println(tmp.x() + " " + tmp.y());
            path.add(0, tmp);
        }

        // ArrayList <coord> path = getPath(explored, init, goal);
        for (coord c : path)
            System.out.println(c.x() + " " + c.y());
        drawPath(path);
    }

    public static void drawPath(ArrayList <coord> path) {
        int M = map.length;
        int N = map[0].length;
        double RADIUS = (double) (M*N)/25000;

        for (coord c : path) {
            StdDraw.setPenColor(StdDraw.RED);
            int i = c.x();
            int j = c.y();
            StdDraw.point((double)i/(double)M + RADIUS/2, (double)j/N + RADIUS/2);
            //StdDraw.filledSquare((double)i/(double)M + RADIUS/2, (double)j/N + RADIUS/2, RADIUS);

        }
    }

    public static void drawMatrix() {
        int M = map.length;
        int N = map[0].length;
        double RADIUS = (double) (M*N)/25000;
        StdDraw.setCanvasSize(M * 30, N * 30);
        StdDraw.setPenRadius(RADIUS);
        StdDraw.clear(StdDraw.WHITE);

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                // System.out.println(map[i][j]);
                // System.out.println("COR: " + Math.round(map[i][j]*127.0));
                // int tom = (int)Math.round((float)(1.0/(float)map[i][j])*127.0);
                int tom = (int) (255 - probMap[i][j]*255);
                if (tom > 255)
                    tom = 255;
                if (tom < 0)
                    tom = 0;

                StdDraw.setPenColor(tom, tom, tom);

                StdDraw.point((double)i/(double)M + RADIUS/2, (double)j/N + RADIUS/2);
                //StdDraw.filledSquare((double)i/(double)M + RADIUS/2, (double)j/N + RADIUS/2, RADIUS);

            }
        }
    }

    public static void drawPoint(coord c) {
        int M = map.length;
        int N = map[0].length;
        int i = c.x();
        int j = c.y();
        double RADIUS = (double) (M*N)/25000;
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.point((double)i/(double)M + RADIUS/2, (double)j/N + RADIUS/2);
    }

    public static ArrayList <Point> xyToMap(ArrayList <coord> path, int w, int h) {
        ArrayList <Point> points = new ArrayList <Point> ();
        for (coord p : path) {
            Point tmp = new Point((int) Math.round(p.x() * w), (int) Math.round(p.y() * h));
            points.add(tmp);
        }
        return points;
    }

    public ArrayList <coord> linearizePath(ArrayList <coord> path, int w, int h) {
        coord[] pathArray = new coord[path.size()];
        pathArray = path.toArray(pathArray);

        int init, current;
        ArrayList <coord> newPath = new ArrayList <coord> ();

        for (init = 0; init < pathArray.length; ) {
            boolean found = false;
            for (current = init + 1; current < pathArray.length; current++) {
                found = false;
                Line tmp = new Line((int) (pathArray[init].x()*this.width), (int)(pathArray[init].y()*this.height), (int)(pathArray[current].x()*this.width), (int)(pathArray[current].y()*this.height) );

                for (int l = 0; l < lines.length && !found; l++) {
                    if (tmp.intersectsAt(lines[l]) != null) {
                        found = true;
                    }
                }

                if (found) {
                    newPath.add(pathArray[init]);
                    newPath.add(pathArray[current - 1]);
                    init = current - 1;
                    current = init + 1;
                }
                System.out.println("init: " + init + " current: " + current);
            }

            if (!found) {
                newPath.add(pathArray[init]);
                newPath.add(pathArray[current - 1]);
                break;
            }

        }

        return newPath;
    }

    public static coord pointAsCoord(int i, int size) {
        coord c = new coord((int) points[i-1].x / size, (int) points[i-1].y / size);
        return c;
    }

    public static void main(String[] args) {

        int size = 50;

        Discrete dsc = new Discrete (size, size);
        
        // coord init = pointAsCoord(1, size);
        // coord goal = pointAsCoord(10, size);

        coord init = pointAsCoord(1, size);
        coord goal = pointAsCoord(10, size);

        drawMatrix();
        drawPoint(init);
        drawPoint(goal);
        aStar(init, goal);
    }
}
