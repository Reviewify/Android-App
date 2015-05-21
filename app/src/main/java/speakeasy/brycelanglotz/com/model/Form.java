package speakeasy.brycelanglotz.com.model;

/**
 * Created by Wes on 5/20/2015.
 */
import com.parse.*;

import java.util.ArrayList;

@ParseClassName("Form")
public class Form extends ParseObject {
    public Form() {
        // A default constructor is required.
    }

    public ArrayList<String> getSections() {
        return (ArrayList<String>) this.get("sections");
    }
}
