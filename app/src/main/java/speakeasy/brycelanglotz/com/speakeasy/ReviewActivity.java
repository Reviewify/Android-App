package speakeasy.brycelanglotz.com.speakeasy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

import speakeasy.brycelanglotz.com.model.Meals;
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

        updateListView();

        ParseQuery<ParseObject> formQuery = ParseQuery.getQuery("Form");
        formQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    ArrayList<String> sections = (ArrayList<String>) parseObject.get("sections");
                    mSections = new String[sections.size()];
                    mSections = sections.toArray(mSections);
                    updateListView();
                }
                else {
                    System.out.println(e.getMessage());
                }
            }
        });
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
