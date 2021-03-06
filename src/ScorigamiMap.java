import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * a class that stores all the data.
 */
public class ScorigamiMap {
    private final Map<Score, MatchList> data = new HashMap<>();

    /**
     * add a match to the database.
     * @param m the match we want to add.
     */
    public void add(Match m) {
        Score temp = m.getScore().getWinningFirst();
        if (this.data.get(temp) == null) {
            MatchList ml = new MatchList();
            ml.add(m);
            this.data.put(temp, ml);
        } else {
            this.data.get(temp).add(m);
        }
    }

    /**
     * add data from a file.
     * @param path path to the file.
     * @throws IOException .
     */
    public void add(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (String s : lines) {
            add(s);
        }
    }

    /**
     * add an entire match list to the database.
     * @param ml a list of matches.
     */
    public void add(MatchList ml) {
        while (!ml.isEmpty()) {
            Match m = ml.getFirst();
            this.add(m);
            ml.removeFirst();
        }
    }
    
    /**
     * converts string to valid data and adds to the database.
     * @param s the data.
     */
    public void add(String s) {
        String[] parts = s.split(",");
        if (parts.length < 6) {
            return;
        }

        Match m = new Match(LocalDate.parse(parts[1]),
                parts[2],
                Team.makeIfAbsent(parts[3]),
                Integer.parseInt(parts[4]),
                Team.makeIfAbsent(parts[5]),
                Integer.parseInt(parts[6]),
                Integer.parseInt(parts[0]));
        this.add(m);
    }

    /**
     * return a list of all scores.
     * @return list.
     */
    public List<Score> getScoreList() {
        return this.data.keySet().stream().toList();
    }

    /**
     * get a set of all scores.
     * @return set.
     */
    public Set<Score> getScoreSet() {
        return this.data.keySet();
    }

    /**
     * get a list of all matches that ended with a certain score.
     * @param s the score we search for.
     * @return matchList.
     */
    public MatchList getMatchListByScore(Score s) {
        return this.data.get(s.getWinningFirst());
    }

    /**
     * check if the match is stored in the database.
     * @param m the match we search for.
     * @return if it is in the database - true, else - false.
     */
    public boolean isMatchInMap(Match m) {
        if (m == null || this.getMatchListByScore(m.getScore()) == null) {
            return false;
        }
        return this.getMatchListByScore(m.getScore()).doesMatchExist(m);
    }

}
