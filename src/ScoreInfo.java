/**
 * a class used for calculating distances, used as a C Struct.
 */
public class ScoreInfo {
    public int possMargin;
    public int ptsMargin;
    public Score score;

    /**
     * set default values.
     */
    ScoreInfo() {
        possMargin = Integer.MAX_VALUE;
        ptsMargin = Integer.MAX_VALUE;
        score = null;
    }

    /**
     * reset to default values.
     */
    public void reset() {
        possMargin = Integer.MAX_VALUE;
        ptsMargin = Integer.MAX_VALUE;
        score = null;
    }
}
