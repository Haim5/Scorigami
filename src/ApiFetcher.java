import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Scanner;

/**
 * API Fetcher class.
 */
public class ApiFetcher {
    private static final String HOME_TEAM = "\"strHomeTeam\"";
    private static final String AWAY_TEAM = "\"strAwayTeam\"";
    private static final String HOME_SCORE = "\"intHomeScore\"";
    private static final String AWAY_SCORE = "\"intAwayScore\"";
    private static final String ROUND = "\"intRound\"";
    private static final String DATE = "\"dateEvent\"";
    private static final String POSTPONED =  "\"strPostponed\"";
    private static final String ID = "\"idEvent\"";
    private static final String TIME = "\"strTimestamp\"";
    private static final String PREFIX = "https://www.thesportsdb.com/api/v1/json/";
    private final String key;

    /**
     * Constructor.
     * @param apiKey key to the satabase.
     */
    ApiFetcher(String apiKey) {
        this.key = apiKey;
    }

    /**
     * fetch the last 15 games of the league.
     * @param leagueCode the code of the league we want to check.
     * @return a matchList object.
     * @throws IOException .
     */
    public MatchList last15(String leagueCode) throws IOException {
        MatchList ml = new MatchList();
        String url = PREFIX + key + "/eventspastleague.php";

        String query = String.format("id=%s",
                URLEncoder.encode(leagueCode, StandardCharsets.UTF_8));

        URLConnection connection = new URL(url + "?" + query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        InputStream response = connection.getInputStream();

        try (Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.useDelimiter("\\A").next();
            String[] parts = responseBody.split("\\{");
            HashMap<String, String> hm;
            int size = parts.length;
            for (int i = 2; i < size; i++) {
                hm = splitMatchData(parts[i].split(","));
                ml.add(makeMatch(hm));
                hm.clear();
            }
        }
        return ml;
    }

    private Match makeMatch(HashMap<String, String> map) {
        if (map.get(POSTPONED) != null && map.get(POSTPONED).equalsIgnoreCase("\"no\"")) {
            Team home = Team.makeIfAbsent(map.get(HOME_TEAM).substring(1, map.get(HOME_TEAM).length() -1));
            Team away = Team.makeIfAbsent(map.get(AWAY_TEAM).substring(1, map.get(AWAY_TEAM).length() -1));
            int homeScore = Integer.parseInt(map.get(HOME_SCORE).substring(1, map.get(HOME_SCORE).length() -1));
            int awayScore = Integer.parseInt(map.get(AWAY_SCORE).substring(1, map.get(AWAY_SCORE).length() -1));
            String r = map.get(ROUND).substring(1, map.get(ROUND).length() - 1);
            LocalDate ld = LocalDate.parse(map.get(DATE).substring(1, map.get(DATE).length() - 1));
            int id = Integer.parseInt(map.get(ID).substring(1, map.get(ID).length() - 1));
            return new Match(ld, r, home, homeScore, away, awayScore, id);
        }
        return null;
    }

    private HashMap<String, String> splitMatchData(String[] data) {
        HashMap<String, String> map = new HashMap<>();
        for (String s: data) {
            String[] subParts = s.split(":");
            if (subParts.length >= 2) {
                map.put(subParts[0], subParts[1]);
            }
        }
        return map;
    }

    /**
     * get the last game of the league.
     * @param leagueCode the league's code.
     * @return Match.
     * @throws IOException .
     */
    public Match getLast(String leagueCode) throws IOException {
        return last15(leagueCode).getLast();
    }
