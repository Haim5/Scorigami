import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Team class.
 */
public class Team {
    private final String name;
    private static final Map<String, Team> teams = new HashMap<>();

    /**
     * Constructor.
     * @param name the team's name.
     */
    private Team(String name) {
        this.name = name;
    }

    /**
     * static method, used in order to avoid making several team objects for the same team.
     * @param name team's name.
     * @return Team.
     */
    public static Team makeIfAbsent(String name) {
        // case - if the team exists already.
        if (teams.containsKey(name)) {
            return teams.get(name);
        }
        // if the teams does not exist.
        Team t = new Team(name);
        teams.put(name, t);
        return t;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(name.toLowerCase(Locale.ROOT), team.name.toLowerCase(Locale.ROOT));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return this.name;
    }

}
