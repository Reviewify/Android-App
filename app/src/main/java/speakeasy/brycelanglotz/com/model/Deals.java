package speakeasy.brycelanglotz.com.model;

/**
 * Created by Wes on 5/20/2015.
 */

import com.parse.*;

@ParseClassName("Deals")
public class Deals extends ParseObject {
    public Deals() {
        // A default constructor is required.
    }

    public Deals(Integer cost, String information, String restaurantObjectId) {
        setCost(cost);
        setInformation(information);
        setRestaurantObjectID(restaurantObjectId);
    }

    public Integer getCost() {
        return (Integer) this.get("cost");
    }

    public void setCost(Integer cost) {
        this.put("cost", cost);
    }

    public String getInformation() {
        return (String) this.get("information");
    }

    public void setInformation(String information) {
        this.put("information", information);
    }

    public String getRestaurantObjectId() {
        return (String) this.get("restaurant_objectId");
    }

    public void setRestaurantObjectID(String restaurant) {
        this.put("restaurant_objectId", restaurant);
    }

}
