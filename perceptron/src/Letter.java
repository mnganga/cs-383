/**
 * Created by hduser on 2/21/15.
 */
public class Letter {
    private static final int NUM_BITS_PER_ROW = 5;
    private static final int NUM_BITS_PER_COL = 5;

    private char alpha;
    private int[] vector;

    // Constructor
    public Letter(char alpha, int[] vector) {
        this.alpha = alpha;
        this.vector = vector;
    }

    // Get methods
    public char getAlpha() {
        return alpha;
    }

    public int[] getVector() {
        return vector;
    }

    // Set methods
    public void setAlpha(char alpha) {
        this.alpha = alpha;
    }

    public void setVector(int[] vector) {
        this.vector = vector;
    }
}
