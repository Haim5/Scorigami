import java.util.ArrayList;
import java.util.Arrays;

public class PossScoreInfoHandler extends InfoHandler {
    // constant variables.
    private final static int FG = 3, UTRY = 5, CTRY = 7;
    // Possession Margin List. PL[i] = minimal number of possessions needed to gain i points. [0-7] initialised.
    private final ArrayList<Integer> PML = new ArrayList<>(Arrays.asList(0, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, 2, 1));
    private int minPoss = Integer.MAX_VALUE;

    @Override
    public void handle(ScoreInfo curr) {
        if (curr.possMargin < this.minPoss) {
            this.ans = curr.score;
            this.minPoss = curr.possMargin;
            this.minPts = curr.ptsMargin;
            this.limit = CTRY * curr.possMargin;
        } else if (curr.possMargin == this.minPoss && curr.ptsMargin < this.minPts) {
            this.ans = curr.score;
            this.minPts = curr.ptsMargin;
        }
    }

    @Override
    public void setValues(ScoreInfo curr, Score og, Score temp) {
        curr.score = temp;
        curr.ptsMargin = og.distanceByPoints(temp);
        extendPML(index(og, temp));
        curr.possMargin = calcMinPoss(og, temp);
    }


    @Override
    public void reset() {
        limit = Integer.MAX_VALUE;
        minPts = Integer.MAX_VALUE;
        ans = null;
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

    @Override
    public Score answer() {
        Score s = this.ans;
        this.reset();
        return s;
    }
}
