public class coord {
    private final int x;
    private final int y;

    public coord(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;

        if (! (that instanceof coord)) return false;

        coord thatCoord = (coord) that;

        return this.x() == thatCoord.x() && this.y() == thatCoord.y();
    }
}
