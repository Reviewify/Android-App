package speakeasy.brycelanglotz.com.model;

/**
 * Created by Wes on 5/20/2015.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.*;

@ParseClassName("Meals")
public class Meals extends ParseObject implements Parcelable {

    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Meals> CREATOR = new Parcelable.Creator<Meals>() {
        public Meals createFromParcel(Parcel in) {
            return new Meals(in);
        }

        public Meals[] newArray(int size) {
            return new Meals[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Meals(Parcel in) {

    }

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
        return (String) this.get("server");
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
