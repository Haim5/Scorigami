abstract public class InfoHandler {
    protected int limit = Integer.MAX_VALUE;
    protected ScoreInfo curr = new ScoreInfo();
    protected ScoreInfo answer = new ScoreInfo();
    protected String description;

    /**
     * reset the values so we can use the handler again.
     */
    private void reset() {
        limit = Integer.MAX_VALUE;
        this.curr.reset();
        this.answer.reset();
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
        Score s = this.answer.score;
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
     * @param temp the current score we use to measure the distance.
     * @param homeMargin home team point margin (points to gain in order to get to temp home score)
     * @param awayMargin away team point margin (points to gain in order to get to temp away score)
     */
    abstract void setValues(Score temp, int homeMargin, int awayMargin);
}
