public class PtsScoreInfoHandler extends InfoHandler {

    /**
     * constructor, sets the description.
     */
    PtsScoreInfoHandler() {
        this.description = "By points: ";
    }

    @Override
    public void handle() {
        if (curr.ptsMargin < this.minPts) {
            this.ans = this.curr.score;
            this.minPts = this.curr.ptsMargin;
            this.limit = this.minPts;
        }
    }

    @Override
    void setValues(Score temp, int homeMargin, int awayMargin) {
        this.curr.score = temp;
        this.curr.ptsMargin = homeMargin + awayMargin;
    }

    @Override
    public boolean shouldContinue(int n) {
        return (n == 1 || n == 2 || n == 4);
    }
}
