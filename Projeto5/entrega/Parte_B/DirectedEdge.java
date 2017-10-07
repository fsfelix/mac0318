public class DirectedEdge {
    // private int v;
    // private int w;
    // private double weight;

    public int v;
    public int w;
    public double weight;

    public DirectedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int from() {
        return this.v;
    }

    public int to() {
        return this.w;
    }

    public double weight() {
        return this.weight;
    }

}
