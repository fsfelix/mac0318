import java.util.ArrayList;
import java.util.Collections;
/*

  Na matriz -2 indica a posicao inicial da busca, -1 obstaculos, 0 posicoes
  nao visitadas pelo algoritmo de busca, e qualquer outra coisa custo de
  movimentacao da posicao inicial até a meta.

 */

public class widthSearch {

    public widthSearch () {
        
    }

    public static ArrayList <coord> getNeighbours(double[][] matrix, coord current, int connectivity) {
        ArrayList <coord> coordList = new ArrayList <coord> ();
        int M = matrix.length;
        int N = matrix[0].length;
        int i = current.x();
        int j = current.y();

        if (i - 1 >= 0 && matrix[i - 1][j] == 0.0)
            coordList.add(new coord(i - 1, j));
        if (i + 1 < M && matrix[i + 1][j] == 0.0)
            coordList.add(new coord(i + 1, j));
        if (j - 1 >= 0 && matrix[i][j - 1] == 0.0)
            coordList.add(new coord(i, j - 1));
        if (j + 1 < N && matrix[i][j + 1] == 0.0)
            coordList.add(new coord(i, j + 1));
        return coordList;
    }

    public static boolean checkMeta(coord n, coord goal) {
        if (n.x() == goal.x() && n.y() == goal.y())
            return true;
        else
            return false;
    }

    public static void updateNeighbour(double[][] matrix, coord current, coord n) {
        if (current.x() != n.x() && current.y() != n.y())
            matrix[n.x()][n.y()] = matrix[current.x()][current.y()] + Math.sqrt(2);
        else
            matrix[n.x()][n.y()] = matrix[current.x()][current.y()] + 1;
    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == -1.0)
                    System.out.print("1 ");
                else if (matrix[i][j] != 0.0)
                    System.out.print(matrix[i][j] + " ");
                else
                    System.out.print("  ");

            }
            System.out.println();
        }

    }

    public static void search(double[][] matrix, coord init, coord goal, int connectivity) {
        ArrayList <coord> coordList = new ArrayList <coord> ();
        boolean found = false;
        coordList.add(init);

        while (!found) {
            coord current = coordList.remove(0);
            ArrayList <coord> neigh = getNeighbours(matrix, current, connectivity);
            for (coord n : neigh) {
                if (checkMeta(n, goal)) {
                    System.out.println("Encontramos a meta");
                    found = true;
                }
                updateNeighbour(matrix, current, n);
                coordList.add(n);
            }
        }
        matrix[init.x()][init.y()] = -2.0;
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

    public static coord updatePosWithMinValue(double [][] map, coord tmp, int connectivity) {
        int M = map.length;
        int N = map[0].length;
        int i = tmp.x();
        int j = tmp.y();
        int argmin = -1;
        double [] costs = new double[connectivity];
        coord updated = new coord(-1, -1);

        for (int ii = 0; ii < connectivity; ii++)
            costs[ii] = Double.POSITIVE_INFINITY;

        if (i - 1 >= 0 && map[i - 1][j] != -1.0 && map[i - 1][j] != 0.0)
            costs[0] = map[i - 1][j];
        if (i + 1 < M && map[i + 1][j] != -1.0 && map[i + 1][j] != 0.0)
            costs[1] = map[i + 1][j];
        if (j - 1 >= 0 && map[i][j - 1] != -1.0 && map[i][j - 1] != 0.0)
            costs[2] = map[i][j - 1];
        if (j + 1 < N && map[i][j + 1] != -1.0 && map[i][j + 1] != 0.0)
            costs[3] = map[i][j + 1];

        argmin = argmin(costs);
        if (argmin == -1) {
            // for (int ii = 0; ii < costs.length; i++)
            //     System.out.println(costs[ii]);
            System.out.println(map[i - 1][j]);
            System.out.println(map[i + 1][j]);
            System.out.println(map[i][j - 1]);
            System.out.println(map[i][j + 1]);
        }
        if (argmin == 0) {
            // map[i - 1][j] = -1;
            updated = new coord(i - 1, j);
        }

        if (argmin == 1) {
            // map[i + 1][j] = -1;
            updated = new coord(i + 1, j);
        }

        if (argmin == 2) {
            // map[i][j - 1] = -1;
            updated = new coord(i, j - 1);
        }

        if (argmin == 3) {
            // map[i][j + 1] = -1;
            updated = new coord(i, j + 1);
        }

        return updated;
    }

    public static void printMatrixOriginal(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static ArrayList <coord> getPath(double [][] map, coord init, coord goal, int connectivity) {
        int i = goal.x();
        int j = goal.y();
        int i_init = init.x();
        int j_init = init.y();
        coord tmp = new coord(goal.x(), goal.y());
        ArrayList <coord> path = new ArrayList <coord> ();

        path.add(goal);
        while (tmp.x() != i_init || tmp.y() != j_init) {
            tmp = updatePosWithMinValue(map, tmp, connectivity);
            if (tmp.x() == -1 && tmp.y() == -1)
                System.out.println("Deu merda");

            // for (coord p : path)
            //     System.out.println(p.x() + " " + p.y());

            path.add(0, tmp);
        }
        return path;
    }

    public static void drawMatrix(Discrete dsc) {
        int M = dsc.map.length;
        int N = dsc.map[0].length;
        double RADIUS = (double) (M*N)/75000;
        StdDraw.setCanvasSize(M * 15, N * 15);
        StdDraw.setPenRadius(RADIUS);
        StdDraw.clear(StdDraw.LIGHT_GRAY);

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (dsc.map[i][j] == 0) {
                    StdDraw.setPenColor(StdDraw.GRAY);
                    StdDraw.point((double)i/(double)M + RADIUS/2, (double)j/N + RADIUS/2);
                }
                else if (dsc.map[i][j] == -1) {
                    StdDraw.setPenColor(StdDraw.PINK);
                    StdDraw.point((double)i/(double)M + RADIUS/2, (double)j/N + RADIUS/2);

                }

            }
        }
    }


    public static void drawPath(Discrete dsc, ArrayList <coord> path) {
        int M = dsc.map.length;
        int N = dsc.map[0].length;
        double RADIUS = (double) (M*N)/75000;
        StdDraw.setPenRadius(RADIUS);

        for (coord p : path) {
            StdDraw.setPenColor(StdDraw.ORANGE);
            StdDraw.point((double)p.x()/(double)M + RADIUS/2, (double)p.y()/N + RADIUS/2);
        }
    }

    public static void drawObjective(Discrete dsc, coord init, coord goal) {
        int M = dsc.map.length;
        int N = dsc.map[0].length;
        double RADIUS = (double) (M*N)/75000;
        StdDraw.setPenRadius(RADIUS*2);

        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point((double)init.x()/(double)M + RADIUS/2, (double)init.y()/N + RADIUS/2);

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.point((double)goal.x()/(double)M + RADIUS/2, (double)goal.y()/N + RADIUS/2);

    }


    public static void main(String[] args) {
        ArrayList <coord> path = new ArrayList <coord> ();
        ArrayList <coord> newPath = new ArrayList <coord> ();
        Discrete dsc = new Discrete (40, 40);

        // System.out.println(dsc.map.length + " " + dsc.map[0].length);
        // printMatrix(dsc.map);

        // dsc.thicken();

        // // printMatrix(dsc.map);

        drawMatrix(dsc);

        coord init = new coord(0, 0);
        coord goal = new coord(20, 12);

        drawObjective(dsc, init, goal);

        // search(dsc.map, init, goal, 4);
        // path = getPath(dsc.map, init, goal, 4);

        /* FRENTE DE ONDA */

        search(dsc.map, goal, init, 4);
        path = getPath(dsc.map, goal, init, 4);
        Collections.reverse(path);

        for (coord p : path)
           System.out.println(p.x() + " " + p.y());

        // newPath = dsc.linearizePath(path, 20, 20);

        drawPath(dsc, path);
        // for (coord p : newPath)
        //     System.out.println(p.x() + " " + p.y());

    }
}
