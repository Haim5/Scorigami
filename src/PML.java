import java.util.ArrayList;
import java.util.Arrays;

/**
 * Possession margin list class. Used in order to find possession margin.
 */
public class PML {
    // constant variables.
    public final static int FG = 3, UTRY = 5, CTRY = 7;
    // Possession Margin List. PML[i] = minimal number of possessions needed to gain i points. [0-7] initialised.
    private final ArrayList<Integer> pml = new ArrayList<>(Arrays.asList(0, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, 2, 1));

    /**
     * extend the list if needed.
     * @param index the index we want.
     */
    private void extend(int index) {
        if (index >= pml.size()) {
            for (int i = pml.size(); i <= index; i++) {
                pml.add(i, 1 + Math.min(Math.min(this.pml.get(i - FG), this.pml.get(i - UTRY)), this.pml.get(i - CTRY)));
            }
        }
    }

    /**
     * get the possession margin.
     * @param index the margin.
     * @return pml[index]
     */
    public int get(int index) {
        extend(index);
        return this.pml.get(index);
    }
}
