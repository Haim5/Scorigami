abstract public class InfoHandler {
    protected int limit = Integer.MAX_VALUE;
    protected int minPts = Integer.MAX_VALUE;
    protected Score ans = null;

    /**
     * reset the values so we can use the handler again.
     */
    public void reset() {
        limit = Integer.MAX_VALUE;
        minPts = Integer.MAX_VALUE;
        ans = null;
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
    public boolean shouldContinue(int n) {
        return (n == 1 || n == 2 || n == 4);
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
     * change the values as needed.
     * @param curr current data to compare.
     */
    abstract void handle(ScoreInfo curr);

    /**
     * set the values of curr.
     * @param curr scoreInfo obj we need to set its values.
     * @param og the original score.
     * @param temp the current score we compare with.
     */
    abstract void setValues(ScoreInfo curr, Score og, Score temp);
}
