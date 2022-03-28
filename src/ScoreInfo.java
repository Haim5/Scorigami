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
     * copy constructor.
     * @param si the copy.
     */
    ScoreInfo(ScoreInfo si) {
        this.possMargin = si.possMargin;
        this.ptsMargin = si.ptsMargin;
        this.score = si.score;
    }

    /**
     * reset to default values.
     */
    public void reset() {
        possMargin = Integer.MAX_VALUE;
        ptsMargin = Integer.MAX_VALUE;
        score = null;
    }

    /**
     * set three values.
     * @param poss possession margin.
     * @param pts points margin.
     * @param s score.
     */
    public void setValues(int poss, int pts, Score s) {
        possMargin = poss;
        ptsMargin = pts;
        score = s;
    }

    /**
     * set 2 values.
     * @param pts points margin.
     * @param s score.
     */
    public void setValues(int pts, Score s) {
        ptsMargin = pts;
        score = s;
    }
}
