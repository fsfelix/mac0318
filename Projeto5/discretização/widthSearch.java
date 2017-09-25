import java.util.ArrayList;

public class widthSearch {

    public ArrayList <coord> getNeighbours(coord current, int connectivity) {

    }

    public boolean checkMeta(coord n, coord meta) {
        
    }

    public void updateNeighbour(int matrix[][], coord current, coord n) {
        
    }

    public void search(int matrix[][], coord inicio, coord meta, int connectivity) {
        ArrayList <coord> coordList = new ArrayList <coord> ();
        boolean found = false;

        while (!found) {
            coord current = coordList.remove(0);
            ArrayList <coord> neigh = getNeighbours(current, connectivity);
            for (coord n : neigh) {
                if (checkMeta(n, meta)) {
                    break;
                }
                updateNeighbour(matrix, current, n);
                coordList.add(n);
            }
        }
    }

    public static void main(String[] args) {
        
    }
}
