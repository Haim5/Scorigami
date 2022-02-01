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
    public void setValues(Score og, Score temp) {
        this.curr.score = temp;
        this.curr.ptsMargin = og.distanceByPoints(temp);
    }

    @Override
    public boolean shouldContinue(int n) {
        return (n == 1 || n == 2 || n == 4);
    }
}
