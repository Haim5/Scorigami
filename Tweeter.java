import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


/**
 * a class that tweets.
 */
public class Tweeter {

    private final String path;

    /**
     * Constructor.
     * @param path path for the keys file.
     */
    public Tweeter(String path) {
        this.path = path;
    }

    /**
     * Tweet a message.
     * @param text the text we want to tweet.
     * @throws TwitterException .
     * @throws IOException .
     */
    public void Tweet(String text) throws TwitterException, IOException {
        List<String> lines = Files.readAllLines(Path.of(this.path), StandardCharsets.UTF_8);
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(lines.get(0))
                .setOAuthConsumerSecret(lines.get(1))
                .setOAuthAccessToken(lines.get(2))
                .setOAuthAccessTokenSecret(lines.get(3));
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        twitter.updateStatus(text);
    }
}
