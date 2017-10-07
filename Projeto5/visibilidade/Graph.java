import java.util.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

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
                System.out.println();
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
        PriorityQueue <Integer> pq = new PriorityQueue <Integer> (this.V,
                                                                  new Comparator<Integer> () {
                                                                      public int compare(Integer a, Integer b) {
                                                                          if (distTo[a] > distTo[b])
                                                                              return 1;
                                                                          if (distTo[a] < distTo[b])
                                                                              return -1;
                                                                          return 0;
                                                                      }
                                                                  });

        for (int v = 0; v < this.V; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
            prev[v] = -1;
            pq.add(v);
        }

        distTo[s] = 0;

        while (pq.size() > 0) {
            int u = pq.poll();

            for (DirectedEdge edge : this.nodes[u].edges) {
                double new_dist = distTo[u] + edge.weight();
                int v = edge.to();
                if (new_dist < distTo[v]) {
                    distTo[v] = new_dist;
                    prev[v] = u;
                    pq.remove(v);
                    pq.add(v);
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
