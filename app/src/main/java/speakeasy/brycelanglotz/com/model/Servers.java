package speakeasy.brycelanglotz.com.model;

/**
 * Created by Wes on 5/20/2015.
 */
import com.parse.*;

@ParseClassName("Servers")
public class Servers extends ParseObject {
    public Servers() {
        // A default constructor is required.
    }

    public Servers (String firstName, String lastName, String email, String restaurantObjectId) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setRestaurantObjectId(restaurantObjectId);
    }

    public String getFirstName() {
        return (String) this.get("first_name");
    }

    public void setFirstName(String first) {
        this.put("first_name", first);
    }

    public String getLastName() {
        return (String) this.get("last_name");
    }

    public void setLastName(String last) {
        this.put("last_name", last);
    }

    public String getEmail() {
        return (String) this.get("email");
    }

    public void setEmail(String email) {
        this.put("email", email);
    }

    public String getRestaurantObjectId() {
        return (String) this.get("restaurant_objectId");
    }

    public void setRestaurantObjectId(String restaurant) {
        this.put("restaurant_objectId", restaurant);
    }
}
