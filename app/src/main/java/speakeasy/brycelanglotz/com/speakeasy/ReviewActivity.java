package speakeasy.brycelanglotz.com.speakeasy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import speakeasy.brycelanglotz.com.model.Meals;
import speakeasy.brycelanglotz.com.model.Reviews;
import speakeasy.brycelanglotz.com.model.Singleton;


public class ReviewActivity extends ActionBarActivity {

    ListView listview;

    ReviewFormBaseAdapter adapter;

    private Meals mMeal;
    private String[] mSections = new String[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mMeal = Singleton.getInstance().getScannedMeal();

        listview = (ListView) findViewById(R.id.list_view_review_form);

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("restaurantCode", mMeal.getRestaurantObjectId());
                parameters.put("mealCode", mMeal.getObjectId());
                parameters.put("username", ParseUser.getCurrentUser().getUsername());
                parameters.put("potentialReward", mMeal.getPotentialReward());

                List<Float> ratings = new ArrayList<Float>(adapter.ratings.length);
                for (float f : adapter.ratings) {
                    ratings.add(Float.valueOf(f));
                }
                if (ratings.contains(0.00f)) {
                    Toast.makeText(getApplicationContext(), "All star ratings are required.", Toast.LENGTH_LONG).show();
                }
                else {
                    ParseCloud.callFunctionInBackground("ReviewMeal", parameters, new FunctionCallback<Object>() {
                        public void done(Object object, ParseException e) {
                            if (e == null) {
                                List<Float> ratings = new ArrayList<Float>(adapter.ratings.length);
                                for (float f : adapter.ratings) {
                                    ratings.add(Float.valueOf(f));
                                }
                                Reviews review = new Reviews(mMeal.getRestaurantObjectId(),
                                        ParseUser.getCurrentUser().getUsername(),
                                        mMeal.getPotentialReward(),
                                        mMeal.getObjectId(),
                                        new ArrayList<String>(Arrays.asList(adapter.reviews)),
                                        new ArrayList<Float>(ratings));
                                popToRootActivity();
                                int totalRewards = (int) object;
                                String message = "You've been rewarded " +
                                        NumberFormat.getNumberInstance(Locale.US).format(mMeal.getPotentialReward()) + "! You now have " +
                                        NumberFormat.getNumberInstance(Locale.US).format(totalRewards) + " total points!";
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        updateListView();

        final ProgressDialog dialog = ProgressDialog.show(this, null, "Loading Review Form...");
        ParseQuery<ParseObject> formQuery = ParseQuery.getQuery("Form");
        formQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    ArrayList<String> sections = (ArrayList<String>) parseObject.get("sections");
                    mSections = new String[sections.size()];
                    mSections = sections.toArray(mSections);
                    updateListView();
                } else {
                    System.out.println(e.getMessage());
                }
                dialog.hide();
            }
        });
    }

    private void popToRootActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
        startActivity(intent);
    }

    private void updateListView() {
        adapter = new ReviewFormBaseAdapter(this, mSections);
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
