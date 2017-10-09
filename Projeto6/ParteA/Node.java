import java.util.ArrayList;

public class Node {
    public ArrayList <DirectedEdge> edges = new ArrayList <DirectedEdge> ();
    public float x;
    public float y;

    public Node (float x, float y) {
        this.x = x;
        this.y = y;
    }
}
