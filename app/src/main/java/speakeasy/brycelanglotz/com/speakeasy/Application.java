package speakeasy.brycelanglotz.com.speakeasy;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Wes on 5/19/2015.
 */
public class Application extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "aS5JfOEzT7lLooye3NQCzkFQagbUXaQVhKX24wnE", "5cUQj1EYf3azLeyvCOtMlyxkpqHYexewg8qBqZMh");
        ParseFacebookUtils.initialize(getApplicationContext());
    }
}