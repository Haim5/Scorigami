abstract public class InfoHandler {
    protected int limit = Integer.MAX_VALUE;
    protected int minPts = Integer.MAX_VALUE;
    protected Score ans = null;
    protected ScoreInfo curr = new ScoreInfo();
    protected String description;

    /**
     * reset the values so we can use the handler again.
     */
    protected void reset() {
        limit = Integer.MAX_VALUE;
        minPts = Integer.MAX_VALUE;
        ans = null;
        this.curr.reset();
    }

    /**
     * get the handler's description
     * @return string
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * get the current score. (the score we need to check)
     * @return Score.
     */
    public Score getCurrentScore() {
        return curr.score;
    }

    /**
     * return the score and reset.
     * @return Score.
     */
    public Score answer() {
        Score s = this.ans;
        this.reset();
        return s;
    }

    /**
     * check if the loop should keep running.
     * @param n the current iteration (margin).
     * @return boolean.
     */
    public boolean shouldRun(int n) {
        return n <= this.limit;
    }

    /**
     * check if the loop should continue (skip iteration).
     * @param n the current iteration (margin)
     * @return boolean
     */
    abstract boolean shouldContinue(int n);

    /**
     * change the values as needed.
     */
    abstract void handle();

    /**
     * set the values of curr.
     * @param og the original score.
     * @param temp the current score we compare with.
     */
    abstract void setValues(Score og, Score temp);
}
