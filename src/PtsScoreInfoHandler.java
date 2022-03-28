public class PtsScoreInfoHandler extends InfoHandler {

    /**
     * constructor, sets the description.
     */
    PtsScoreInfoHandler() {
        this.description = "By points: ";
    }

    @Override
    public void handle() {
        if (curr.ptsMargin < answer.ptsMargin) {
            this.answer = new ScoreInfo(this.curr);
            this.limit = this.answer.ptsMargin;
        }
    }

    @Override
    void setValues(Score temp, int homeMargin, int awayMargin) {
        this.curr.setValues(homeMargin + awayMargin, temp);
    }

    @Override
    public boolean shouldContinue(int n) {
        return (n == 1 || n == 2 || n == 4);
    }
}
