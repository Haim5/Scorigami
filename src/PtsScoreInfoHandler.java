public class PtsScoreInfoHandler extends InfoHandler {

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
}
