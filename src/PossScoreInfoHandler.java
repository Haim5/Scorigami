import java.util.ArrayList;
import java.util.Arrays;

public class PossScoreInfoHandler extends InfoHandler {
    // constant variables.
    private final static int FG = 3, UTRY = 5, CTRY = 7;
    // Possession Margin List. PL[i] = minimal number of possessions needed to gain i points. [0-7] initialised.
    private final ArrayList<Integer> PML = new ArrayList<>(Arrays.asList(0, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, 2, 1));
    private int minPoss = Integer.MAX_VALUE;

    /**
     * constructor, sets the description.
     */
    PossScoreInfoHandler() {
        this.description = "By possessions: ";
    }

    @Override
    public void handle() {
        if (curr.possMargin < this.minPoss) {
            this.ans = this.curr.score;
            this.minPoss = this.curr.possMargin;
            this.minPts = this.curr.ptsMargin;
            this.limit = CTRY * this.curr.possMargin;
        } else if (this.curr.possMargin == this.minPoss && this.curr.ptsMargin < this.minPts) {
            this.ans = this.curr.score;
            this.minPts = this.curr.ptsMargin;
        }
    }

    @Override
    public void setValues(Score og, Score temp) {
        this.curr.score = temp;
        this.curr.ptsMargin = og.distanceByPoints(temp);
        extendPML(index(og, temp));
        this.curr.possMargin = calcMinPoss(og, temp);
    }


    @Override
    protected void reset() {
        super.reset();
        this.minPoss = Integer.MAX_VALUE;
    }

    /**
     * calculate the index.
     * @param og original score.
     * @param temp current score.
     * @return int.
     */
    private int index(Score og, Score temp) {
        return Math.max(Math.abs(og.getHomeScore() - temp.getHomeScore()),
                Math.abs(og.getAwayScore() - temp.getAwayScore()));
    }

    /**
     * extend the PML if needed.
     * @param index the index we need to add.
     */
    private void extendPML(int index) {
        if (index >= PML.size()) {
            // calculate the new value using the previous results (dynamic programming).
            int val = 1 + Math.min(Math.min(PML.get(index - FG), PML.get(index - UTRY)), PML.get(index - CTRY));
            PML.add(index, val);
        }
    }

    /**
     * calculate the minimal possessions distance.
     * @param og original score.
     * @param temp target score.
     * @return int.
     */
    private int calcMinPoss(Score og, Score temp) {
        int v1 = Math.abs(og.getHomeScore() - temp.getHomeScore());
        int v2 = Math.abs(og.getAwayScore() - temp.getAwayScore());
        return PML.get(v1) + PML.get(v2);
    }
}
