package uhl.mc.tickchunks;

public class ChunkCoordinate {
    private final int x;
    private final int z;
    public ChunkCoordinate(int x, int z) {
        super();
        this.x = x;
        this.z = z;
    }
    public int getX() {
        return x;
    }
    public int getZ() {
        return z;
    }
    @Override
    public String toString() {
        return "[" + x + "," + z + "]";
    }
}
