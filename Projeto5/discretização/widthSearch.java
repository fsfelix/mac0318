import java.util.ArrayList;

public class widthSearch {

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

    public static void updateNeighbour(double[][] matrix, coord current, coord n, int connectivity) {
        if (current.x() != n.x() && current.y() != n.y())
            matrix[n.x()][n.y()] = matrix[current.x()][current.y()] + Math.sqrt(2);
        else
            matrix[n.x()][n.y()] = matrix[current.x()][current.y()] + 1;
    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

    }

    public static void search(double[][] matrix, coord inicio, coord goal, int connectivity) {
        ArrayList <coord> coordList = new ArrayList <coord> ();
        boolean found = false;
        coordList.add(inicio);

        while (!found) {
            coord current = coordList.remove(0);
            ArrayList <coord> neigh = getNeighbours(matrix, current, connectivity);
            for (coord n : neigh) {
                if (checkMeta(n, goal)) {
                    System.out.println("Encontramos a meta");
                    found = true;
                }
                updateNeighbour(matrix, current, n, connectivity);
                printMatrix(matrix);
                System.out.println();
                coordList.add(n);
            }
        }
    }

    public static void main(String[] args) {
        double [][] mapa = new double[3][3];

        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[0].length; j++) {
                mapa[i][j] = 0;
            }
        }


        // printMatrix(mapa);
        coord init = new coord(0, 0);
        coord goal = new coord(2, 2);

        // mapa[init.x()][init.y()] = -1;

        // exemplos de obstaculos:
        mapa[1][1] = -1;
        mapa[1][2] = -1;

        search(mapa, init, goal, 4);

    }
}
