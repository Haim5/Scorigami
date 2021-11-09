import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
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

//    example:
//    public void fetchGame() throws IOException {
//        String url ="https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php";
//
//        String query = String.format("id=%s",
//                URLEncoder.encode("5070", StandardCharsets.UTF_8));
//
//        URLConnection connection = new URL(url + "?" + query).openConnection();
//        connection.setRequestProperty("Accept-Charset", "UTF-8");
//        InputStream response = connection.getInputStream();
//
//        try (Scanner scanner = new Scanner(response)) {
//            String responseBody = scanner.useDelimiter("\\A").next();
//            System.out.println(responseBody);
//        }
//
//    }

    /**
     * fetch the last 15 games of the league.
     * @param leagueCode the code of the league we want to check.
     * @return a matchList object.
     * @throws IOException .
     */
    public MatchList last15(String leagueCode) throws IOException {
        MatchList ml = new MatchList();
        String url = "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php";

        String query = String.format("id=%s",
                URLEncoder.encode(leagueCode, StandardCharsets.UTF_8));

        URLConnection connection = new URL(url + "?" + query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        InputStream response = connection.getInputStream();

        splitData(ml, response);
        return ml;
    }

    /**
     * helper method, splits the string to valid data.
     * @param ml list of matches.
     * @param response the data fetched from the internet.
     */
    private void splitData(MatchList ml, InputStream response) {
        try (Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.useDelimiter("\\A").next();

            String[] parts = responseBody.split("\\{");
            Map<String, String> map = new HashMap<>();
            for (int i = 2; i < parts.length; i++) {
                String[] subParts = parts[i].split(",");
                for (String s : subParts) {
                    String[] subSubParts = s.split(":");
                    if (subSubParts.length >= 2) {
                        String key = subSubParts[0];
                        String value = subSubParts[1];
                        map.put(key, value);
                    }
                }
                if (map.get(POSTPONED) != null && map.get(POSTPONED).equalsIgnoreCase("\"no\"")) {
                    Team home = Team.makeIfAbsent(map.get(HOME_TEAM).substring(1, map.get(HOME_TEAM).length() -1));
                    Team away = Team.makeIfAbsent(map.get(AWAY_TEAM).substring(1, map.get(AWAY_TEAM).length() -1));
                    int homeScore = Integer.parseInt(map.get(HOME_SCORE).substring(1, map.get(HOME_SCORE).length() -1));
                    int awayScore = Integer.parseInt(map.get(AWAY_SCORE).substring(1, map.get(AWAY_SCORE).length() -1));
                    String r = map.get(ROUND).substring(1, map.get(ROUND).length() - 1);
                    LocalDate ld = LocalDate.parse(map.get(DATE).substring(1, map.get(DATE).length() - 1));
                    int id = Integer.parseInt(map.get(ID).substring(1, map.get(ID).length() - 1));
                    ml.add(new Match(ld, r, home, homeScore, away, awayScore, id));
                }
                map.clear();
            }
        }
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

    /**
     * fetch all the games of the league.
     * @param inauguralSeason the first season.
     * @param leagueCode the league's code.
     * @return MatchList.
     * @throws IOException .
     */
    public MatchList fetchAllGames(Season inauguralSeason, String leagueCode) throws IOException {
        MatchList ml = new MatchList();
        String url ="https://www.thesportsdb.com/api/v1/json/1/eventsseason.php";


        Season last = new Season(Calendar.getInstance().get(Calendar.YEAR) + 2);
        for (Season s = inauguralSeason; !s.equals(last); s = s.nextSeason()) {
            String query = String.format("id=%s",
                    URLEncoder.encode(leagueCode, StandardCharsets.UTF_8));
            String query2 = String.format("s=%s",
                    URLEncoder.encode(s.toString(), StandardCharsets.UTF_8));

            URLConnection connection = new URL(url + "?" + query + "&" + query2).openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            InputStream response = connection.getInputStream();
            splitData(ml, response);
        }
        return ml;
    }
}
