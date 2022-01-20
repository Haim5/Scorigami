import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MatchList {
    private final LinkedList<Match> list = new LinkedList<>();

    /**
     * check if the data structure is empty.
     * @return if empty - true, else false.
     */
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    /**
     * get the last match of the list.
     * @return Match.
     */
    public Match getLast() {
        if (isEmpty()) {
            return null;
        }
        return this.list.getLast();
    }

    /**
     * get the first match of the list.
     * @return Match.
     */
    public Match getFirst() {
        return this.list.getFirst();
    }

    /**
     * get the size of the data structure.
     * @return int size.
     */
    public int getSize() {
        return this.list.size();
    }

    /**
     * add a match to the data structure chronologically.
     * @param m the match we want to add.
     */
    public void add(Match m) {
        if (m == null) {
            return;
        }
        if (this.isEmpty()) {
            this.list.add(m);
            return;
        }
        LocalDate ld = m.getDate();
        Iterator<Match> it = list.descendingIterator();
        Match temp = this.getLast();
        int i = list.size() - 1;
        while (i >= 0) {
            if (ld.isEqual(temp.getDate()) || ld.isAfter(temp.getDate())) {
                list.add(i + 1, m);
                return;
            }
            temp = it.next();
            i -= 1;
        }
        this.list.addFirst(m);
    }

    /**
     * get the date of the last match in the data structure.
     * @return date.
     */
    public LocalDate getLastDate() {
        return this.getLast().getDate();
    }

    /**
     * remove the first match in the data structure.
     */
    public void removeFirst() {
        this.list.removeFirst();
    }

    /**
     * check if the match exists in the data structure.
     * @param m the match we are searching for.
     * @return if found - true, else - false.
     */
    public boolean doesMatchExist(Match m) {
        for (Match other : this.list) {
            if (m.equals(other)) {
                return true;
            }
        }
        return false;
    }

    /**
     * return output for output file.
     * @return list.
     */
    public List<String> toOutputFile() {
        List<String> str = new LinkedList<>();
        for (Match m : this.list) {
            str.add(m.toOutputFile());
        }
        return str;
    }
}
