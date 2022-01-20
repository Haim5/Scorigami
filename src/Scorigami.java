import twitter4j.TwitterException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 * @author Haim.
 */
public class Scorigami {
    private static final String MLR = "5070";
    private static final String MLR_DATA_PATH = "***";
    private static final String MLR_KEYS_PATH = "***";
    private static final String SIX_NATIONS = "4714";
    private static final String SIX_NATIONS_DATA_PATH = "***";
    private static final String SIX_NATIONS_KEYS_PATH = "***";
    private static final String API_KEY_PATH = "***";

    /**
     * main method.
     * @param args .
     * @throws IOException .
     * @throws TwitterException .
     */
    public static void main(String[] args) throws IOException, TwitterException {

        String k = Files.readAllLines(Path.of(API_KEY_PATH), StandardCharsets.UTF_8).get(0);

        // make output manager for each league and update.
        OutputManager mlrOutputManager = new OutputManager(MLR_DATA_PATH, MLR, "MLR", MLR_KEYS_PATH, k);
        mlrOutputManager.update(false);

       OutputManager sixNationsOutputManager = new OutputManager(SIX_NATIONS_DATA_PATH, SIX_NATIONS,
               "Six Nations", SIX_NATIONS_KEYS_PATH, k);
       sixNationsOutputManager.update(false);
     }
}
