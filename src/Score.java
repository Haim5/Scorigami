import java.util.Objects;

/**
 * Score class.
 */
public class Score {
    private final int homeScore;
    private final int awayScore;

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
     * returns the total points margin between two scores.
     * @param other the score we compare with.
     * @return int.
     */
    public int distanceByPoints(Score other) {
        return (Math.abs(this.homeScore - other.getHomeScore()) + Math.abs(this.awayScore - other.getAwayScore()));
    }

    /**
     * check if the margin between the two scores is valid in rugby.
     * @param other the second score.
     * @return if the margin is valid - true, else - false.
     */
    public boolean isValidDistance(Score other) {
        int hm = Math.abs(other.getHomeScore() - this.homeScore);
        int am = Math.abs(other.getAwayScore() - this.awayScore);
        return !(hm == 1 || hm == 2 || hm == 4 || am == 1 || am == 2 || am == 4);
    }

    @Override
    public String toString() {
        return this.homeScore + ":" + this.awayScore;
    }

    /**
     * get the score with the winning score first.
     * @return score.
     */
    public Score getWinningFirst() {
        if (this.homeScore >= this.awayScore) {
            return this;
        }
        return new Score(this.awayScore, this.homeScore);
    }
}

