package speakeasy.brycelanglotz.com.model;

/**
 * Created by Wes on 5/20/2015.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.*;

@ParseClassName("Meals")
public class Meals extends ParseObject {

    public Meals() {
        // A default constructor is required.
    }

    public Meals(Boolean claimed, String claimedBy, Integer potentialRewards,
                 String restaurantObjectId, String serverObjectId) {
        setClaimed(claimed);
        setClaimedBy(claimedBy);
        setPotentialReward(potentialRewards);
        setRestaurantObjectId(restaurantObjectId);
        setServerObjectId(serverObjectId);
    }

    public Boolean getClaimed() {
        return (Boolean) this.get("claimed");
    }

    public void setClaimed(Boolean claimed) {
        this.put("claimed", claimed);
    }

    public String getClaimedBy() {
        return (String) this.get("claimed_by");
    }

    public void setClaimedBy(String claimedBy) {
        this.put("claimed_by", claimedBy);
    }

    public Integer getPotentialReward() {
        return (Integer) this.get("potential_reward");
    }

    public void setPotentialReward(Integer potentialReward) {
        this.put("potential_reward", potentialReward);
    }

    public String getRestaurantObjectId() {
        return (String) this.get("restaurant_objectId");
    }

    public void setRestaurantObjectId(String restaurant) {
        this.put("restaurant_objectId", restaurant);
    }

    public String getServerObjectId() {
        return (String) this.get("server_objectId");
    }

    public void setServerObjectId(String server) {
        this.put("server_objectId", server);
    }

    public String getClaimedString() {
        String claimedIndicator = "Available";
        if (this.getClaimed() == true) {
            claimedIndicator = "Claimed";
        }
        return claimedIndicator;
    }

    public String getRewardsString() {
        return getPotentialReward().toString();
    }
}
