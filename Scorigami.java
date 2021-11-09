import twitter4j.TwitterException;
import java.io.IOException;

/**
 * @author Haim.
 */
public class Scorigami {
    private static final String MLR = "5070";
    private static final String MLR_DATA_PATH = "/home/haim/Desktop/Scorigami/lib/mlr_data";
    private static final String MLR_KEYS_PATH = "/home/haim/Desktop/Scorigami/lib/mlr_keys";
    private static final String SIX_NATIONS = "4714";
    private static final String SIX_NATIONS_DATA_PATH = "/home/haim/Desktop/Scorigami/lib/6n_data";
    private static final String SIX_NATIONS_KEYS_PATH = "/home/haim/Desktop/Scorigami/lib/six_nations_keys";

    /**
     * main method.
     * @param args .
     * @throws IOException .
     * @throws TwitterException .
     */
    public static void main(String[] args) throws IOException, TwitterException {

        // make output manager for each league and update.
        OutputManager mlrOutputManager = new OutputManager(MLR_DATA_PATH, MLR, "MLR", MLR_KEYS_PATH);
        mlrOutputManager.update();

        OutputManager sixNationsOutputManager = new OutputManager(SIX_NATIONS_DATA_PATH, SIX_NATIONS,
                "Six Nations", SIX_NATIONS_KEYS_PATH);
        sixNationsOutputManager.update();

    }
}
