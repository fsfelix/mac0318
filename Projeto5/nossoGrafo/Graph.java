import java.util.*;
import java.util.ArrayList;

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

    public int indMin(double[] array, ArrayList <Integer> nodeSet) {
        double min = Double.POSITIVE_INFINITY;
        int ind = 0;

        for (int e : nodeSet) {
            if (array[e] < min) {
                min = array[e];
                ind = e;
            }
        }

        return ind;
    }


    // s: source vertex
    public ArrayList<Integer> Dijkstra(int s, int target) {
        double [] distTo = new double[this.V];
        int [] prev = new int[this.V];
        ArrayList <Integer> nodeSet = new ArrayList <Integer> ();

        for (int v = 0; v < this.V; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
            prev[v] = -1;
            nodeSet.add(v);
        }

        distTo[s] = 0;
        nodeSet.remove(Integer.valueOf(8));

        while (!nodeSet.isEmpty()) {
            int u = indMin(distTo, nodeSet);

            nodeSet.remove(Integer.valueOf(u));

            Iterator itr = this.nodes[u].edges.iterator();
            while (itr.hasNext()) {
                Object element = itr.next();
                DirectedEdge edge = (DirectedEdge) element;
                double new_dist = distTo[u] + edge.weight();

                int v = edge.to();
                if (new_dist < distTo[v]) {
                    distTo[v] = new_dist;
                    prev[v] = u;
                }
            }
        }

        ArrayList <Integer> path = new ArrayList <Integer> ();

        while (prev[target] != -1) {
            path.add(0, target);
            target = prev[target];
        }

        path.add(0, target);

        System.out.println("caminho: ");
        for (int e : path) {
            System.out.print(" " + Integer.toString(e + 1));
        }
        System.out.println();

        return path;
    }

}
