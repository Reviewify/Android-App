package speakeasy.brycelanglotz.com.model;

/**
 * Created by Wes on 5/20/2015.
 */
import com.parse.*;

import java.util.ArrayList;

@ParseClassName("Review")
public class Review extends ParseObject {
    public Review() {
        // A default constructor is required.
    }

    public Review(String restaurantObjectId, String uploaderUsername, Integer reward,
                  String mealObjectId, ArrayList<String> textReviews,
                  ArrayList<Float> starRatings) {
        setRestaurantObjectId(restaurantObjectId);
        setUploaderUsername(uploaderUsername);
        setReward(reward);
        setMealObjectId(mealObjectId);
        setTextReviews(textReviews);
        setStarRatings(starRatings);
    }

    public String getRestaurantObjectId() {
        return (String) this.get("restaurant_objectId");
    }

    public void setRestaurantObjectId(String restaurant) {
        this.put("restaurant_objectId", restaurant);
    }

    public String getUploaderUsername() {
        return (String) this.get("uploader_username");
    }

    public void setUploaderUsername(String user) {
        this.put("uploader_username", user);
    }

    public Integer getReward() {
        return (Integer) this.get("reward");
    }

    public void setReward(Integer reward) {
        this.put("reward", reward);
    }

    public String getMealObjectId() {
        return (String) this.get("meal_objectId");
    }

    public void setMealObjectId(String meal) {
        this.put("meal_objectId", meal);
    }

    public ArrayList<String> getTextReviews() {
        return (ArrayList<String>) this.get("text_reviews");
    }

    public void setTextReviews(ArrayList<String> reviews) {
        this.put("text_reviews", reviews);
    }

    public ArrayList<Float> getStarRatings() {
        return (ArrayList<Float>) this.get("star_ratings");
    }

    public void setStarRatings(ArrayList<Float> stars) {
        this.put("star_ratings", stars);
    }
}