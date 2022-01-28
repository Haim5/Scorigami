public class PtsScoreInfoHandler extends InfoHandler {
    @Override
    public void handle(ScoreInfo curr) {
        if (curr.ptsMargin < this.minPts) {
            this.ans = curr.score;
            this.minPts = curr.ptsMargin;
            this.limit = this.minPts;
        }
    }

    @Override
    public void setValues(ScoreInfo curr, Score og, Score temp) {
        curr.score = temp;
        curr.ptsMargin = og.distanceByPoints(temp);
    }
}
