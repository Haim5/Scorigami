import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * a class that generates stats from the database.
 */
public class StatsMaker {
    private final ScorigamiMap sm;
    private int games;
    private int diffScores;
    // Possession Margin List. PL[i] = minimal number of possessions needed to gain i points.
    private final ArrayList<Integer> PML;
    // FG: field goal (3 points), UTRY: unconverted try (5 points), CTRY: converted try (7 points).
    private final static int FG = 3, UTRY = 5, CTRY = 7;

    /**
     * Constructor.
     * @param sm the data saved as ScorigamiMap object.
     */
    StatsMaker(ScorigamiMap sm) {
        this.sm = sm;
        this.games = this.numberOfGames();
        this.diffScores = numberOfScoresHelper();
        // initialise base values [0-7]
        PML = new ArrayList<>(Arrays.asList(0, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, 2, 1));
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
     * get the closest scorigami score, closest by total points.
     * @param s the score we start from.
     * @return the closest scorigami by points.
     */
    public Score closestScorigamiByPoints(Score s) {
        // edge case - the score is a scorigami - return the score.
        if (isScorigami(s)) {
            return s;
        }
        // set minimalMargin to max value.
        int minimalMargin = Integer.MAX_VALUE;
        // initialise constant variables.
        final int home = s.getHomeScore(), away = s.getAwayScore(), sum = home + away;
        Score answer = null;
        for (int i = away, h = 0; h <= minimalMargin; i++, h++) {
            for (int j = home, k = h; k <= minimalMargin; j++, k++) {
                Score temp = new Score(j, i);
                int currMargin = i + j - sum;
                if (currMargin < minimalMargin && s.isValidDistance(temp) && isScorigami(temp)) {
                    answer = temp;
                    minimalMargin = currMargin;
                }
            }
        }
        return answer;
    }


    /**
     * get the closest scorigami score, closest by possessions.
     * @param s the score we start from.
     * @return the closest scorigami by possessions.
     */
    public Score closestScorigamiByPossessions(Score s) {
        // edge case - the score is scorigami - return the score.
        if (isScorigami(s)) {
            return s;
        }
        // initialise minimalPoss, minimalMargin and maxMargin to the max value.
        int minimalPoss = Integer.MAX_VALUE, minimalMargin = Integer.MAX_VALUE, maxMargin = Integer.MAX_VALUE;
        // initialise constant variables.
        final int home = s.getHomeScore(), away = s.getAwayScore(), sum = home + away;
        Score answer = null;
        // i - away team score, h - margin from original away score (i - away).
        for (int i = away, h = 0; h <= maxMargin; i++, h++) {
            // check if h is a valid margin by comparing (h:0) with (0:0).
            if (!s.isValidDistance(new Score(home, i))) {
                continue;
            }
            int awayPM = PML.get(h);
            // j - home team score, k - total margin (i + j - home - away).
            for (int j = home, k = h; k <= maxMargin; j++, k++) {
                Score temp = new Score(j, i);
                // update PML.
                possessionMarginListUpdate(k - h);
                // check score validity and if the score is a scorigami.
                if (s.isValidDistance(temp) && isScorigami(temp)) {
                    // set values
                    int currentPossMargin = awayPM + PML.get(k - h);
                    int currentPointMargin = i + j - sum;
                    if (currentPossMargin < minimalPoss) {
                        answer = temp;
                        minimalMargin = currentPointMargin;
                        minimalPoss = currentPossMargin;
                        maxMargin = CTRY * minimalPoss;
                    } else if (currentPossMargin == minimalPoss && currentPointMargin < minimalMargin) {
                        answer = temp;
                        minimalMargin = currentPointMargin;
                    }
                }
            }
        }
        return answer;
    }

    /**
     * update PML if needed.
     * @param m the current index.
     */
    private void possessionMarginListUpdate(int m) {
        // if index m is not in the array, add it.
        if (m >= PML.size()) {
            // dynamic programming - get the distance using previous solutions.
            PML.add(m, 1 + Math.min(Math.min(PML.get(m - FG), PML.get(m - UTRY)), PML.get(m - CTRY)));
        }
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

    /**
     * helper method, gets the number of unique scores that have ever happened. used to reduce complexity.
     * @return number of unique scores.
     */
    private int numberOfScoresHelper() {
        return this.sm.getScoreSet().size();
    }
}
