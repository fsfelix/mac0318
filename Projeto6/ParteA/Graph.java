import java.util.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import lejos.geom.*;

public class Graph {
    private int V;
    private Node[] nodes;

    private static Point[] points = {
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

    public Graph (int v) {
        this.V = v;
        this.nodes = new Node[v];

        for (int i = 0; i < v; i++) {
            this.nodes[i] = new Node (points[i].x, points[i].y);
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

    public double heuristic(int icurrent, int igoal, int sel) {
        if (sel == 0) {
            Node current = this.nodes[icurrent];
            Node goal = this.nodes[igoal];
            // System.out.println(Math.sqrt( Math.pow(current.x - goal.x, 2) + Math.pow(current.y - goal.y, 2)));
            return Math.sqrt( Math.pow(current.x - goal.x, 2) + Math.pow(current.y - goal.y, 2));
        }
        return 0;
    }

    // s: source vertex
    public ArrayList<Integer> A_Star(int s, int target) {
        double [] distTo = new double[this.V]; // gScore
        double [] distToHeurist = new double[this.V]; // fScore
        int [] prev = new int[this.V];
        PriorityQueue <Integer> pq = new PriorityQueue <Integer> (this.V,
                                                                  new Comparator<Integer> () {
                                                                      public int compare(Integer a, Integer b) {
                                                                          if (distToHeurist[a] > distToHeurist[b])
                                                                              return 1;
                                                                          if (distToHeurist[a] < distToHeurist[b])
                                                                              return -1;
                                                                          return 0;
                                                                      }
                                                                  }); // openSet
        PriorityQueue <Integer> closedSet = new PriorityQueue <Integer> (this.V);

        for (int v = 0; v < this.V; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
            distToHeurist[v] = Double.POSITIVE_INFINITY;
            prev[v] = -1;
        }

        distTo[s] = 0;
        distToHeurist[s] = heuristic(s, target, 0);

        pq.add(s);

        while (pq.size() > 0) {
            int u = pq.poll();

            if (u == target) {
                break;
            }

            closedSet.add(u);

            for (DirectedEdge edge : this.nodes[u].edges) {

                int v = edge.to();
                if (!closedSet.contains(v)) {
                    if (!pq.contains(v)) {
                        pq.add(v);
                    }
                    double new_dist = distTo[u] + edge.weight();
                    if (new_dist < distTo[v]) {
                        distTo[v] = new_dist;
                        distToHeurist[v] = new_dist + heuristic(v, target, 0);
                        prev[v] = u;
                        pq.remove(v);
                        pq.add(v);
                    }
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

    // s: source vertex
    public ArrayList<Integer> BestChoice(int s, int target) {
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
