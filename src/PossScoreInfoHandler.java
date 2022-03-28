public class PossScoreInfoHandler extends InfoHandler {
    private final PML pml = new PML();

    /**
     * constructor, sets the description.
     */
    PossScoreInfoHandler() {
        this.description = "By possessions: ";
    }

    @Override
    public void handle() {
        if (curr.possMargin < answer.possMargin) {
            this.answer = new ScoreInfo(this.curr);
            this.limit = PML.CTRY * this.answer.possMargin;
        } else if (curr.possMargin == answer.possMargin && curr.ptsMargin < answer.ptsMargin) {
            this.answer.setValues(this.curr.ptsMargin, this.curr.score);
        }
    }

    @Override
    public void setValues(Score temp, int homeMargin, int awayMargin) {
        this.curr.setValues(pml.get(homeMargin) + pml.get(awayMargin), homeMargin + awayMargin, temp);
    }

    @Override
    public boolean shouldContinue(int n) {
        int v = pml.get(n);
        return (v == Integer.MAX_VALUE ||  v > this.answer.possMargin);
    }
}
