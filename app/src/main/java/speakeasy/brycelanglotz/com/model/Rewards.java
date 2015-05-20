package speakeasy.brycelanglotz.com.model;

/**
 * Created by brycelanglotz on 5/20/15.
 */
import com.parse.*;

@ParseClassName("Points")
public class Rewards extends ParseObject {
    public Rewards() {
        // A default constructor is required.
    }

    public Integer getTotalRewards() {
        return (Integer) this.get("points");
    }
}
