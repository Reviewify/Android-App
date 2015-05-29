package speakeasy.brycelanglotz.com.speakeasy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import speakeasy.brycelanglotz.com.model.Meals;
import speakeasy.brycelanglotz.com.model.Servers;
import speakeasy.brycelanglotz.com.model.Singleton;


public class MealSummaryActivity extends ActionBarActivity {

    private Meals mMeal;

    private TextView mServerNameTextView;
    private TextView mPotentialRewardTextView;
    private Button mBeginReviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_summary);

        mMeal = (Meals) Singleton.getInstance().getScannedMeal();
        mServerNameTextView = (TextView) findViewById(R.id.serverNameTextView);
        mPotentialRewardTextView = (TextView) findViewById(R.id.potentialRewardTextView);
        mBeginReviewButton = (Button) findViewById(R.id.beginReviewButton);

        ParseQuery<ParseObject> serversQuery = ParseQuery.getQuery("Servers");
        serversQuery.whereEqualTo("objectId", mMeal.getServerObjectId());
        final ProgressDialog dialog = ProgressDialog.show(this, null, "Loading Server Data...");
        serversQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject serverObject, ParseException e) {
                if (e == null) {
                    Servers server = (Servers) serverObject;
                    mPotentialRewardTextView.setText(mMeal.getPotentialReward().toString());
                    mServerNameTextView.setText(server.getFirstName());
                    mBeginReviewButton.setOnClickListener(beginReviewOnClickListener);
                } else {
                    popToMainWithError(e.getMessage());
                }
                dialog.hide();
            }
        });
    }

    private void popToMainWithError(String error) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
        startActivity(intent);
        Toast.makeText(getApplicationContext(), error,
                Toast.LENGTH_LONG).show();
    }

    View.OnClickListener beginReviewOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            presentReviewActivity();
        }
    };

    private void presentReviewActivity() {
        Intent myIntent = new Intent(MealSummaryActivity.this, ReviewActivity.class);
        MealSummaryActivity.this.startActivity(myIntent);
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
