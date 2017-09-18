public class EdgeWeightedDigraph {
    private int V;
    private int E;
    private Bag<DirectedEdge>[] adj;
    private int[] indegree;

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public Iterable<DirectedEdge> adj(int v) {
        validateVertex(v); 
        return adj[v];
    }

    public void addEdge(int v, int w, double weight) {
        DirectedEdge e = new DirectedEdge(v, w, weight);
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        E++;
    }

    public EdgeWeightedDigraph(int numOfVert, int numOfEdges) {

        this.V = numOfVert;
        this.E = numOfEdges;

        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            validateVertex(v);
            validateVertex(w);
            double weight = in.readDouble();
            addEdge(v, w, weight);
        }
    }

}
