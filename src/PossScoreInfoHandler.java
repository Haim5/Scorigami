import java.util.ArrayList;
import java.util.Arrays;

public class PossScoreInfoHandler extends InfoHandler {
    // constant variables.
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
    public void setValues(Score temp, int homeMargin, int awayMargin) {
        this.curr.score = temp;
        this.curr.ptsMargin = homeMargin + awayMargin;
        extendPML(Math.max(homeMargin, awayMargin));
        this.curr.possMargin = PML.get(homeMargin) + PML.get(awayMargin);
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
        if (index < PML.size()) {
            return;
        }
        for (int i = PML.size(); i <= index; i++) {
            // calculate the new value using the previous results (dynamic programming).
            PML.add(i, 1 + Math.min(Math.min(PML.get(i - FG), PML.get(i - UTRY)), PML.get(i - CTRY)));
        }
    }

    @Override
    public boolean shouldContinue(int n) {
        int v = PML.get(n);
        return v == Integer.MAX_VALUE ||  v > this.minPoss;
    }
}

