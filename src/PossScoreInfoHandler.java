import java.util.ArrayList;
import java.util.Arrays;

public class PossScoreInfoHandler extends InfoHandler {
    // constant variables. FG - field goal (3 pts), UTRY - unconverted try (5 pts), CTRY - converted try (7 pts).
    private final static int FG = 3, UTRY = 5, CTRY = 7;
    // Possession Margin List. PML[i] = minimal number of possessions needed to gain i points. [0-7] initialised.
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
        int v1 = Math.abs(og.getHomeScore() - temp.getHomeScore());
        int v2 = Math.abs(og.getAwayScore() - temp.getAwayScore());
        extendPML(Math.max(v1, v2));
        this.curr.possMargin = PML.get(v1) + PML.get(v2);
    }


    @Override
    protected void reset() {
        super.reset();
        this.minPoss = Integer.MAX_VALUE;
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
}
