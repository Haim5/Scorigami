import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * a class that generates stats from the database.
 */
public class StatsMaker {
    private final ScorigamiMap sm;
    private int games;

    /**
     * Constructor.
     * @param sm the data saved as ScorigamiMap object.
     */
    StatsMaker(ScorigamiMap sm) {
        this.sm = sm;
        this.numberOfGames();
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
//    public Score closestScorigamiByPoints(Score s) {
//        // edge case - the score is a scorigami - return the score.
//        if (isScorigami(s)) {
//            return s;
//        }
//        Score answer = null;
//        int minimalMargin = Integer.MAX_VALUE, home = s.getHomeScore(), away = s.getAwayScore(), i, j;
//        for (i = away; i - away <= minimalMargin; i++) {
//            // for (i = awa, int k = i - away; k <= minimalMargin; i++, k++)
//            // j - home <= minimalMargin - i + away???
//            for (j = home; j - home <= minimalMargin; j++) {
//                Score temp = new Score(j, i);
//                int currMargin = s.distanceByPoints(temp);
//                if (currMargin < minimalMargin && isScorigami(temp) && isValidRugbyDistance(s, temp)) {
//                    answer = temp;
//                    minimalMargin = currMargin;
//                }
//            }
//        }
//        return answer;
//    }

    public Score closestScorigamiByPoints(Score s) {
        // edge case - the score is a scorigami - return the score.
        if (isScorigami(s)) {
            return s;
        }
        Score answer = null;
        int minimalMargin = Integer.MAX_VALUE, home = s.getHomeScore(), away = s.getAwayScore(), h, i, j, k;
        int l = home + away;
        for (i = away, h = 0; h <= minimalMargin; i++, h++) {
            for (j = home, k = h; k <= minimalMargin; j++, k++) {
                Score temp = new Score(j, i);
                int currMargin = i + j - l;
                if (currMargin < minimalMargin && isScorigami(temp) && isValidRugbyDistance(s, temp)) {
                    answer = temp;
                    minimalMargin = currMargin;
                }
            }
        }
        return answer;
    }

//            for (int i = Math.max(s.getHomeScore(), s.getAwayScore()); i < MAX; i++) {
//        for (int j = Math.min(s.getHomeScore(), s.getAwayScore()); j < MAX; j++) {
//            Score temp = new Score(i, j);
//            if (isScorigami(temp) && isValidRugbyDistance(s, temp)) {
//                if (scores[s.distanceByPoints(temp)] == null) {
//                    scores[s.distanceByPoints(temp)] = temp;
//                } else {
//                    if (s.distanceByPoints(temp) <= s.distanceByPoints(scores[s.distanceByPoints(temp)])) {
//                        scores[s.distanceByPoints(temp)] = temp;
//                    }
//                }
//            }
//        }
//    }

    /**
     * check if the margin between the two scores is valid in rugby.
     * @param og the original score.
     * @param target the target score.
     * @return if the margin is valid - true, else - false.
     */
    private boolean isValidRugbyDistance(Score og, Score target) {
        int homeMargin = Math.abs(og.getHomeScore() - target.getHomeScore());
        int awayMargin = Math.abs(og.getAwayScore() - target.getAwayScore());
        return  !(homeMargin == 1 || homeMargin == 2 || homeMargin == 4 ||
                awayMargin == 1 || awayMargin == 2 || awayMargin == 4);
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
        int minimalPoss = Integer.MAX_VALUE, minimalMargin = Integer.MAX_VALUE, maxMargin = Integer.MAX_VALUE;
        int home = s.getHomeScore(), away = s.getAwayScore(), h, i ,j, k, l = home + away;
        Score answer = null;
        for (i = away, h = 0; h <= maxMargin; i++, h++) {
            for (j = home, k = h; k <= maxMargin; j++, k++) {
                Score temp = new Score(j, i);
                if (isValidRugbyDistance(s, temp) && isScorigami(temp)) {
                    int currentPossMargin = s.distanceByPoss(temp);
                    int currentPointMargin = i + j - l;
                    if (currentPossMargin == minimalPoss && currentPointMargin < minimalMargin) {
                        answer = temp;
                        minimalMargin = currentPointMargin;
                    } else if (currentPossMargin < minimalPoss) {
                        answer = temp;
                        minimalMargin = currentPointMargin;
                        minimalPoss = currentPossMargin;
                        maxMargin = 7 * minimalPoss;
                    }
                }
            }
        }
        return answer;
    }

    /**
     * get a list of the most common scores.
     * @return a list of the most common scores.
     */
    private LinkedList<Score> getCommonList() {
        int max = 0;
        LinkedList<Score> commonList = new LinkedList<>();
        LinkedList<Score> keyList = (LinkedList<Score>) this.sm.getScoreList();
        for (Score s : keyList) {
            MatchList ml = this.sm.getMatchListByScore(s);
            int times = 0;
            if (ml != null) {
                times = ml.getSize();
            }
            if (times > max) {
                commonList.clear();
                commonList.add(s.getWinningFirst());
                max = times;
            } else if (times == max) {
                commonList.add(s.getWinningFirst());
            }
        }
        return commonList;
    }

    /**
     * get the number of games the has been played in the league.
     * @return number of games.
     */
    public int getNumberOfGames() {
        return this.games;
    }

    /**
     * add 1 to the number of games.
     */
    public void add() {
        this.games += 1;
    }

    /**
     * set this.games field using the database.
     */
    private void numberOfGames() {
        List<Score> keyList = this.sm.getScoreList();
        int count = 0;
        for (Score s : keyList) {
            MatchList ml = this.sm.getMatchListByScore(s);
            if (ml != null) {
                count += ml.getSize();
            }
        }
        this.games = count;
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
        return this.sm.getScoreSet().size();
    }

    public int mostPointsByTeam() {
        List<Score> l = sm.getScoreList();
        int max = 0;
        for(Score s: l) {
            max = Math.max(Math.max(s.getHomeScore(), s.getAwayScore()), max);
        }
        return max;
    }
}