import java.time.LocalDate;
import java.util.Objects;

/**
 * Match class.
 */
public class Match {
    private final Team home;
    private final Team away;
    private final Score score;
    private final LocalDate date;
    private final String round;
    private final int idEvent;

    /**
     * Constructor.
     * @param date the date of the match.
     * @param s the round.
     * @param home home team.
     * @param homeScore home team score.
     * @param away away team.
     * @param awayScore away team score.
     * @param id the match id.
     */
    Match(LocalDate date, String s, Team home, int homeScore, Team away, int awayScore, int id) {
        this.date = date;
        this.round = s;
        this.home = home;
        this.away = away;
        this.score = new Score(homeScore, awayScore);
        this.idEvent = id;
    }

    /**
     * get the match's date.
     * @return date.
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * the the match's score.
     * @return Score.
     */
    public Score getScore() {
        return this.score;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.idEvent == ((Match) o).getIdEvent();
    }

    @Override
    public int hashCode() {
        return Objects.hash(home, away, score, date, round);
    }

    @Override
    public String toString() {
        return home + ":" + away + " " + score + " " + date;
    }

    /**
     * get text for tweet.
     * @return string.
     */
    public String toTweet() {
        return home + " " + score.getHomeScore() + ":" + score.getAwayScore() + " " + away;
    }

    /**
     * get text for output file.
     * @return string.
     */
    public String toOutputFile() {
        return this.idEvent + "," +this.date + ",Round " + this.round + "," + this.home + ","
                + this.score.getHomeScore() + "," + this.away + "," + this.score.getAwayScore();
    }

    /**
     * get the match's id.
     * @return ID.
     */
    public int getIdEvent() {
        return this.idEvent;
    }
}
