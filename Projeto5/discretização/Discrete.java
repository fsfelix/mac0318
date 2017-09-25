import lejos.geom.*;
import lejos.robotics.mapping.LineMap;


public class Discrete {
    private double width;
    private double height;
    private int M; // linhas
    private int N; // colunas
    public [][] double map;

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

    public Discrete (int w, int h){
        this.width 	= w;
        this.height = h;
        this.M = Math.ceil(1195/h);
        this.N = Math.ceil(920/w);
        this.map = new double[this.M][this.N];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = 0;
            }
        }

    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

    }

    public static void populateMap() {
        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.N; j++) {
                Line [] currentLines = new Line[4];
                currentLines[0] = new Line(i * this.h, j * this.w, i * this.h, (j + 1) * this.w);
                currentLines[1] = new Line(i * this.h, (j + 1) * this.w, (i + 1) * this.h, (j + 1) * this.w);
                currentLines[2] = new Line((i + 1) * this.h, (j + 1) * this.w, (i + 1) * this.h, j * this.w);
                currentLines[3] = new Line((i + 1) * this.h, j * this.w, i * this.h, j * this.w);
                loop:
                for (int iLine = 0; i < 4; iLine++) {
                    for (int iObj = 0; iObj < 14; iObj++) {
                        if (currentLine[iLine].intersectsAt(lines[iObj]) != null) {
                            this.map[i][j] = -1;
                            break loop;
                        }
                    }

                }
            }
        }
    }


    // Ideia de discretizar todo a matriz e fazer uma linha em cada horizontal e vertical
    //  ver se essa linha corta algum objeto, e onde
    //  essa maneira é a de menor complexidade O(n²)



}
