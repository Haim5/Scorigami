import twitter4j.TwitterException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

/**
 * output manager class.
 */
public class OutputManager {
    private final ScorigamiMap sm = new ScorigamiMap();
    private final StatsMaker stat;
    private final ApiFetcher api = new ApiFetcher();
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
     * @throws IOException .
     */
    OutputManager(String path, String leagueCode, String leagueName, String keysPath) throws IOException {
        this.sm.add(Path.of(path));
        this.leagueCode = leagueCode;
        this.stat = new StatsMaker(this.sm);
        this.path = path;
        this.name = leagueName;
        this.tweeter = new Tweeter(keysPath);
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
     * update the database and tweet.
     * @throws IOException .
     * @throws TwitterException .
     */
    public void update() throws IOException, TwitterException {
        MatchList newMatches = new MatchList();
        if (!this.isUpdated()) {
            MatchList ml = this.api.last15(this.leagueCode);
            while (!ml.isEmpty()) {
                Match m = ml.getFirst();
                if (!this.sm.isMatchInMap(m)) {
                    String end;
                    if (this.stat.isScorigami(m.getScore())) {
                        end = this.scorigamiMessage();
                    } else {
                        end = this.noScorigamiMessage(m.getScore());
                    }
                    this.sm.add(m);
                    newMatches.add(m);
                    this.stat.add();
                    this.tweeter.Tweet("#" + this.stat.getNumberOfGames()
                            + "\n\nFinal Score:\n" + m.toTweet() + ".\n\n" + end);
                }
                ml.removeFirst();
            }
            this.addNewDataToFile(newMatches);
        }
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
        String output = "";
        for(String s : lines) {
            if (!s.equals("\n")) {
                output = output.concat(s);
                output = output.concat("\n");
            }
        }
        fw.write(output);
        fw.close();
    }

    /**
     * helper method, makes the tweet's text.
     * @param s the score.
     * @return string.
     */
    private String closestScorigamiText(Score s) {
        Score byPoints = this.stat.closestScorigamiByPoints(s);
        Score byPoss = this.stat.closestScorigamiByPossessions(s);
        if (byPoints.equals(byPoss)) {
            return "Closest Scorigami: " + byPoints + ".";
        }
        return "Closest Scorigami:\n\nBy Points: " + byPoints + ".\n\n" + "By Possessions: " + byPoss + ".";
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
        if ((num >= 11 && num <= 13) || (num%100 >= 11 && num% 100 <= 13)) {
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
    private String noScorigamiMessage(Score s) {
        int num = this.stat.numberOfOccasionsByScore(s);
        String times = " times before, most recently on ";
        if (num == 1) {
            times = " time before, on ";
        }
        LocalDate ld = this.stat.getLastTime(s);
        String suffix = this.getSuffix(ld.getDayOfMonth());
        return  "No Scorigami, this score has happened " + num + times + ld.getMonth() + " " + ld.getDayOfMonth()
                + suffix + ", " + ld.getYear() + ".\n\n" + closestScorigamiText(s);
    }

}
