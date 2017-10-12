import java.util.ArrayList;
import lejos.pc.comm.*;
import lejos.geom.*;
import lejos.robotics.mapping.LineMap;
import java.util.Scanner;
import java.io.*;
import lejos.util.Delay;

public class Discrete {
    private double width;
    private double height;
    private int M; // linhas
    private int N; // colunas
    public static double [][] map;

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

    };

    Rectangle bounds = new Rectangle(0, 0, 1195, 920);
    LineMap mymap = new LineMap(lines, bounds);

    public Discrete (double w, double h){
        this.width 	= w;
        this.height = h;
        this.M = (int) Math.ceil(1195.0/h);
        this.N = (int) Math.ceil(920.0/w);
        this.map = new double[this.M][this.N];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                this.map[i][j] = 0;
            }
        }
        populateMap();
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
                    for (int iObj = 0; iObj < 14; iObj++) {
                        if (currentLines[iLine].intersectsAt(lines[iObj]) != null) {
                            this.map[i][j] = 1;
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
            sum += 0.1*this.map[i - 1][j];
        if (i + 1 < M)
            sum += 0.1*this.map[i + 1][j];
        if (j - 1 >= 0)
            sum += 0.1*this.map[i][j - 1];
        if (j + 1 < N)
            sum += 0.1*this.map[i][j + 1];
        if (i - 1 >= 0 && j - 1 >= 0)
            sum += 0.05*this.map[i - 1][j - 1];
        if (i - 1 >= 0 && j + 1 < N)
            sum += 0.05*this.map[i - 1][j + 1];
        if (i + 1 < M && j - 1 >= 0)
            sum += 0.05*this.map[i + 1][j - 1];
        if (i + 1 < M && j + 1 < N)
            sum += 0.05*this.map[i + 1][j + 1];

        return sum;
    }

    public void probabilistic() {
        ArrayList <coord> neigh = new ArrayList <coord> ();
        double [][] newMap = new double[this.M][this.N];
        for (int i = 1; i < this.map.length - 1; i++) {
            for (int j = 1; j < this.map[0].length - 1; j++) {
                newMap[i][j] = 0.4*this.map[i][j] + sumNeighbours(i, j);
            }
        }

        for (int i = 0; i < this.M; i++) {
            for(int j = 0; j < this.N; j++) {
                this.map[i][j] = newMap[i][j];
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

    public static void drawMatrix() {
        int M = map.length;
        int N = map[0].length;
        double RADIUS = (double) (M*N)/25000;
        StdDraw.setCanvasSize(M * 15, N * 15);
        StdDraw.setPenRadius(RADIUS);
        StdDraw.clear(StdDraw.WHITE);

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                System.out.println(map[i][j]);
                System.out.println("COR: " + Math.round(map[i][j]*127.0));
                // int tom = (int)Math.round((float)(1.0/(float)map[i][j])*127.0);
                int tom = (int) (255 - map[i][j]*255);
                if (tom > 255)
                    tom = 255;
                if (tom < 0)
                    tom = 0;

                StdDraw.setPenColor(tom, tom, tom);

                //StdDraw.point((double)i/(double)M + RADIUS/2, (double)j/N + RADIUS/2);
                StdDraw.filledSquare((double)i/(double)M + RADIUS/2, (double)j/N + RADIUS/2, RADIUS);

            }
        }
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

    public static void main(String[] args) {
        Discrete dsc = new Discrete (40, 40);
        drawMatrix();
    }
}
