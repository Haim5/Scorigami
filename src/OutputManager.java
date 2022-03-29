import twitter4j.TwitterException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * output manager class.
 */
public class OutputManager {
    private final ScorigamiMap sm = new ScorigamiMap();
    private final StatsMaker stat;
    private final ApiFetcher api;
    private final String leagueCode;
    private final String path;
    private final Tweeter tweeter;
    private final String name;

    /**
     * constructor.
     * @param path path to the data file.
     * @param leagueCode the league's code.
     * @param leagueName the name of the league.
     * @param keysPath the path to the twitter account keys.
     * @param api the key for the apiFetcher constructor.
     * @throws IOException .
     */
    OutputManager(String path, String leagueCode, String leagueName, String keysPath, String api) throws IOException {
        this(path, leagueCode, leagueName, keysPath, new ApiFetcher(api));
    }

    /**
     * Constructor with apiFetcher.
     * @param path path to the data file.
     * @param leagueCode the league's code.
     * @param leagueName the name of the league.
     * @param keysPath the path to the twitter account keys.
     * @param a an apiFetcher object
     * @throws IOException .
     */
    OutputManager(String path, String leagueCode, String leagueName, String keysPath, ApiFetcher a) throws IOException {
        this.sm.add(Path.of(path));
        this.leagueCode = leagueCode;
        this.stat = new StatsMaker(this.sm);
        this.path = path;
        this.name = leagueName;
        this.tweeter = new Tweeter(keysPath);
        this.api = a;
    }

    /**
     * check if the database is updated.
     * @return if up to date - true, else - false.
     * @throws IOException .
     */
    private boolean isUpdated() throws IOException {
        return this.sm.isMatchInMap(this.api.getLast(this.leagueCode));
    }

    /**
     * updates the database and tweets.
     * @param testMode true - work in test mode. false - tweet.
     * @throws IOException .
     * @throws TwitterException .
     */
    public void update(boolean testMode) throws IOException, TwitterException {
        // check if the database is updated.
        if (this.isUpdated()) {
            return;
        }
        MatchList ml = this.api.last15(this.leagueCode);
        MatchList newMatches = new MatchList();
        List<InfoHandler> list = getHandlers();
        while (!ml.isEmpty()) {
            Match m = ml.getFirst();
            // check if the match is already in the database.
            if (!this.sm.isMatchInMap(m)) {
                String end;
                boolean isScorigami = this.stat.isScorigami(m.getScore());
                if (isScorigami) {
                    end = this.scorigamiMessage();
                } else {
                    end = this.noScorigamiMessage(m.getScore(), list);
                }
                // add the match to the databases.
                this.sm.add(m);
                newMatches.add(m);
                this.stat.add(isScorigami);
                if (testMode) {
                    System.out.print("#" + this.stat.getNumberOfGames()
                            + "\n\nFinal Score:\n" + m.toTweet() + ".\n\n" + end);
                } else {
                    this.tweeter.Tweet("#" + this.stat.getNumberOfGames()
                            + "\n\nFinal Score:\n" + m.toTweet() + ".\n\n" + end);
                }
            }
            ml.removeFirst();
        }
        this.addNewDataToFile(newMatches);
    }

    /**
     * make handlers list
     * @return list.
     */
    private List<InfoHandler> getHandlers() {
        ArrayList<InfoHandler> m = new ArrayList<>();
        m.add(new PtsScoreInfoHandler());
        m.add(new PossScoreInfoHandler());
        return m;
    }

    /**
     * add the new matches to the data file.
     * @param ml a MatchList of the new matches.
     * @throws IOException .
     */
    private void addNewDataToFile(MatchList ml) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(this.path), StandardCharsets.UTF_8);
        lines.addAll(ml.toOutputFile());
        FileWriter fw = new FileWriter(this.path);
        StringBuilder sb = new StringBuilder();
        for(String s : lines) {
            if (!s.equals("\n")) {
                sb.append(s).append("\n");
            }
        }
        fw.write(sb.toString());
        fw.close();
    }

    /**
     * helper method, makes the tweet's text.
     * @param s the score.
     * @return string.
     */
    private String closestScorigamiText(Score s, List<InfoHandler> list) {
        int size = list.size();
        Score[] scoreArr = new Score[size];
        int i = 0;
        for (InfoHandler ih : list) {
            this.stat.getClose(s, ih);
            scoreArr[i] = ih.answer();
            i++;
        }
        if (areAllScoresEqual(scoreArr, size)) {
            return "Closest Scorigami: " + scoreArr[0] + ".";
        }
        i = 0;
        StringBuilder sb = new StringBuilder("Closest Scorigami:\n\n");
        for (InfoHandler ih : list) {
            sb.append(ih.getDescription()).append(scoreArr[i]).append(".");
            if (i != size - 1) {
                sb.append("\n\n");
            }
            i++;
        }
        return sb.toString();
    }

    /**
     * compares an array of scores.
     * @param arr the array of scores
     * @param size the array size.
     * @return if all scores are equal - true, otherwise - false.
     */
    private boolean areAllScoresEqual(Score[] arr, int size) {
        Score t = arr[0];
        for (int i = 1; i < size; i++) {
            if (!t.equals(arr[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * helper method, makes tweet text in case of scorigami.
     * @return string.
     */
    private String scorigamiMessage() {
        String suffix = this.getSuffix(this.stat.numberOfScores() + 1);
        return "Scorigami! \n\nThis is the " + (this.stat.numberOfScores() + 1) + suffix + " unique score in "
                + this.name + " history.";
    }

    /**
     * get the right suffix to the number.
     * @param num number.
     * @return string.
     */
    private String getSuffix(int num) {
        if ((num >= 11 && num <= 13) || (num % 100 >= 11 && num % 100 <= 13)) {
            return "th";
        }
        return switch (num % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    /**
     * helper method, makes tweet text in case of no scorigami.
     * @param s the score.
     * @return string.
     */
    private String noScorigamiMessage(Score s, List<InfoHandler> list) {
        int num = this.stat.numberOfOccasionsByScore(s);
        String times;
        if (num == 1) {
            times = " time before, on ";
        } else {
            times = " times before, most recently on ";
        }
        LocalDate ld = this.stat.getLastTime(s);
        String suffix = this.getSuffix(ld.getDayOfMonth());
        return  "No Scorigami, this score has happened " + num + times + ld.getMonth().toString().charAt(0)
                + ld.getMonth().toString().substring(1).toLowerCase() + " " + ld.getDayOfMonth() + suffix + ", "
                + ld.getYear() + ".\n\n" + closestScorigamiText(s, list);
    }
}
