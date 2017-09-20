import java.util.*;

public class Graph {
    private int V;
    private Node[] nodes;

    public Graph (int v) {
        this.V = v;
        this.nodes = new Node[v];

        for (int i = 0; i < v; i++) {
            this.nodes[i] = new Node ();
        }
    }

    public void addEdge(int v, int w, double weight) {
        nodes[v].edges.add(new DirectedEdge(v, w, weight));
    }

    public void addBothEdges(int v, int w, double weight) {
        addEdge(v, w, weight);
        addEdge(w, v, weight);
    }

    public void printGraph() {
        for (int i = 0; i < this.V; i++) {
            Iterator itr = this.nodes[i].edges.iterator();
            while (itr.hasNext()) {
                Object element = itr.next();
                DirectedEdge edge = (DirectedEdge) element;
                System.out.println("From: " + edge.from());
                System.out.println("To: " + edge.to());
                System.out.println("Weight: " + edge.weight());
            }
        }
    }
}
