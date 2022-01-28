import java.time.LocalDate;
import java.util.List;

/**
 * a class that generates stats from the database.
 */
public class StatsMaker {
    private final ScorigamiMap sm;
    private int games;
    private int diffScores;

    /**
     * Constructor.
     * @param sm the data saved as ScorigamiMap object.
     */
    StatsMaker(ScorigamiMap sm) {
        this.sm = sm;
        this.games = this.numberOfGames();
        this.diffScores = numberOfScoresHelper();
    }

    /**
     * checks if the score is a scorigami.
     * @param s the score we want to check.
     * @return if it is a scorigami - true, else - false.
     */
    public boolean isScorigami(Score s) {
        MatchList ml = this.sm.getMatchListByScore(s);
        return (ml == null || ml.isEmpty());
    }
    
    /**
     * get the closest scorigami score by the infoHandler definition of "Closest".
     * @param s the original score.
     * @param sih InfoHandler.
     * @return score.
     */
    public Score getClose(Score s, InfoHandler sih) {
        // edge case - the score is a scorigami (distance = 0)
        if (isScorigami(s)) {
            return s;
        }
        ScoreInfo curr = new ScoreInfo();
        int home = s.getHomeScore(), away = s.getAwayScore();
        for (int i = away, h = 0; sih.shouldRun(h); i++, h++) {
            if (sih.shouldContinue(h)) {
                continue;
            }
            for (int j = home, cpm = h; sih.shouldRun(cpm); j++, cpm++) {
                sih.setValues(curr, s, new Score(j, i));
                if (s.isValidDistance(curr.score) && isScorigami(curr.score)) {
                    sih.handle(curr);
                }
            }
        }
        return sih.answer();
    }

    /**
     * get the number of games the has been played in the league.
     * @return number of games.
     */
    public int getNumberOfGames() {
        return this.games;
    }

    /**
     * add 1 to the number of games and to diffScores if needed.
     * @param isScorigami is the score a scorigami.
     */
    public void add(boolean isScorigami) {
        this.games += 1;
        if (isScorigami) {
            this.diffScores += 1;
        }
    }

    /**
     * set this.games field using the database.
     */
    private int numberOfGames() {
        List<Score> keyList = this.sm.getScoreList();
        int count = 0;
        for (Score s : keyList) {
            MatchList ml = this.sm.getMatchListByScore(s);
            if (ml != null) {
                count += ml.getSize();
            }
        }
        return count;
    }

    /**
     * how many times has the score happened.
     * @param s the score we need to check.
     * @return the number of occasions.
     */
    public int numberOfOccasionsByScore(Score s) {
        if (s == null || isScorigami(s) || this.sm.getMatchListByScore(s) == null) {
            return 0;
        }
        return this.sm.getMatchListByScore(s).getSize();
    }

    /**
     * get the date of the last match that ended with the score.
     * @param s the score we need to check.
     * @return date.
     */
    public LocalDate getLastTime(Score s) {
        if (this.sm.getMatchListByScore(s) != null) {
            return this.sm.getMatchListByScore(s).getLastDate();
        }
        return null;
    }

    /**
     * get the number of unique scores that have ever happened.
     * @return number of unique scores.
     */
    public int numberOfScores() {
        return diffScores;
    }
}
