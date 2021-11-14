import java.util.Objects;

/**
 * Score class.
 */
public class Score {
    private final int homeScore;
    private final int awayScore;
    private final static int FG = 3, TRY_PLAY = 5, CONVERTED_TRY = 7;

    /**
     * Constructor.
     * @param home home team score.
     * @param away away teams score.
     */
    Score(int home, int away) {
        this.homeScore = home;
        this.awayScore = away;
    }

    /**
     * default constructor. sets the score as 0:0.
     */
    Score() {
        this(0, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return (homeScore == score.homeScore && awayScore == score.awayScore) ||
                (awayScore == score.homeScore && homeScore == score.awayScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeScore, awayScore);
    }

    /**
     * get the score of the home team.
     * @return int.
     */
    public int getHomeScore() {
        return this.homeScore;
    }

    /**
     * get the score of the away team.
     * @return int.
     */
    public int getAwayScore() {
        return this.awayScore;
    }

    /**
     * get the score with the winning score first.
     * @return score.
     */
    public Score getWinningFirst() {
        return new Score(Math.max(this.getAwayScore(), this.getHomeScore()),
                Math.min(this.getAwayScore(), this.getHomeScore()));
    }

    /**
     * returns the total points margin between two scores.
     * @param other the score we compare with.
     * @return int.
     */
    public int distanceByPoints(Score other) {
        return (Math.abs(this.homeScore - other.getHomeScore()) + Math.abs(this.awayScore - other.getAwayScore()));
    }

    /**
     * get the minimal number of possessions between two scores.
     * @param other the score we compare with.
     * @return int
     */
    public int distanceByPoss(Score other) {
        // edge case - null
        if (other == null) {
            return -1;
        }
        // edge case - same score.
        if (this.equals(other)) {
            return 0;
        }
        int homeMargin = Math.abs(this.homeScore - other.getHomeScore());
        int awayMargin = Math.abs(this.awayScore - other.getAwayScore());
        // edge case - invalid margin.
        if (homeMargin == 1 || homeMargin == 2 || homeMargin == 4 ||
                awayMargin == 1 || awayMargin == 2 || awayMargin == 4) {
            return -1;
        }
        return getToScore(homeMargin) + getToScore(awayMargin);
    }

    /**
     * returns the minimal number of possessions needed in order to gain a certain number of points.
     * @param margin the number of points to gain.
     * @return the minimal number of possessions.
     */
    private int getToScore(int margin) {
        // edge case - no points to gain.
        if (margin == 0) {
            return 0;
        }
        // edge case - one possession game.
        if (margin == FG || margin == TRY_PLAY|| margin == CONVERTED_TRY) {
            return 1;
        }
        // edge case - 2 field goals game.
        if (margin == (2 * FG)) {
            return 2;
        }
        // edge case - divide by 7.
        if (margin % CONVERTED_TRY == 0) {
            return margin / CONVERTED_TRY;
        }
        // a dynamic programming algorithm.
        int[] arr = new int[margin + 1];
        for (int i = 0; i < margin + 1; i++) {
            if (i == 1 || i == 2 || i == 4) {
                arr[i] = StatsMaker.MAX * StatsMaker.MAX;
            }
            if (i == 0) {
                arr[i] = 0;
            }
            // one possession game
            if (i == FG || i == TRY_PLAY || i == CONVERTED_TRY) {
                arr[i] = 1;
            }
            // two possessions game.
            if (i == (2 * FG)) {
                arr[i] = 2;
            }
            // more than 7 points to gain.
            if (i > CONVERTED_TRY) {
                arr[i] = 1 + Math.min(Math.min(arr[i - CONVERTED_TRY], arr[i - TRY_PLAY]), arr[i - FG]);
            }
        }
        return arr[margin];
    }

    @Override
    public String toString() {
        return this.homeScore + ":" + this.awayScore;
    }
}
