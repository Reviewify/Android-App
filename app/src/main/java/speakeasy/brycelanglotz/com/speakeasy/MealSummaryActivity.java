package speakeasy.brycelanglotz.com.speakeasy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import speakeasy.brycelanglotz.com.model.Meals;
import speakeasy.brycelanglotz.com.model.Servers;


public class MealSummaryActivity extends ActionBarActivity {

    private Meals mMeal;

    private TextView mServerNameTextView;
    private TextView mPotentialRewardTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_summary);
        Intent i = getIntent();
        mMeal = (Meals) i.getParcelableExtra("MEAL");
        mServerNameTextView = (TextView) findViewById(R.id.serverNameTextView);
        mPotentialRewardTextView = (TextView) findViewById(R.id.potentialRewardTextView);
        mPotentialRewardTextView.setText(mMeal.getPotentialReward().toString());
        ParseQuery<ParseObject> pointsQuery = ParseQuery.getQuery("Points");
        pointsQuery.whereEqualTo("objectId", mMeal.getServerObjectId());
        pointsQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject rewardsObject, ParseException e) {
                if (e == null) {
                    Servers server = (Servers) rewardsObject;
                    mServerNameTextView.setText(server.getFirstName());
                } else {
                    Log.d("Servers", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meal_summary, menu);
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