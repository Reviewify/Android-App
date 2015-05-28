package speakeasy.brycelanglotz.com.speakeasy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;

import com.google.zxing.integration.android.IntentResult;
import com.parse.FindCallback;

import com.google.zxing.qrcode.QRCodeWriter;

import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import speakeasy.brycelanglotz.com.model.Meals;
import speakeasy.brycelanglotz.com.model.Reviews;
import speakeasy.brycelanglotz.com.model.Rewards;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            presentLoginActivity();
        }
    }

    private void presentLoginActivity() {
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ReviewFragment.newInstance(position + 1))
                        .commit();
                IntentIntegrator.initiateScan(this, "QR_CODE", "Scan QR Code Here");
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, RedeemFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MyAccountFragment.newInstance(position + 1))
                        .commit();
                break;
            case 3:
                ParseUser.logOut();
                presentLoginActivity();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String results = scanResult.getContents();
            String[] components = results.split(" ");
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("restaurantCode", components[0]);
            parameters.put("mealCode", components[1]);
            ParseCloud.callFunctionInBackground("VerifyMeal", parameters, new FunctionCallback<Object>() {
                public void done(Object object, ParseException e) {
                    if (e == null) {
                        Meals meal = (Meals) object;
                        Toast.makeText(getApplicationContext(), meal.getClaimedString(),
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ReviewFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private Button mGenerateQRCodeButton;
        private EditText mCostEditText;
        private ImageView mQRCodeImageView;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ReviewFragment newInstance(int sectionNumber) {
            ReviewFragment fragment = new ReviewFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ReviewFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_review, container, false);
            // mGenerateQRCodeButton = (Button) rootView.findViewById(R.id.generateQRCodeButton);
            // mGenerateQRCodeButton.setOnClickListener(generateQRCodeOnClickListener);
            return rootView;
        }

        View.OnClickListener generateQRCodeOnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                setQRCodeWithString(ParseUser.getCurrentUser().getUsername() + mCostEditText.getText());
            }
        };

        private void setQRCodeWithString(String data) {
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix matrix = writer.encode(
                        data, BarcodeFormat.QR_CODE, 400, 400
                );
                Bitmap bmp = toBitmap(matrix);
                mQRCodeImageView.setImageBitmap(bmp);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        private static Bitmap toBitmap(BitMatrix matrix){
            int height = matrix.getHeight();
            int width = matrix.getWidth();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++){
                for (int y = 0; y < height; y++){
                    bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class RedeemFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private Button mGenerateQRCodeButton;
        private EditText mCostEditText;
        private ImageView mQRCodeImageView;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static RedeemFragment newInstance(int sectionNumber) {
            RedeemFragment fragment = new RedeemFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public RedeemFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_redeem, container, false);
            mCostEditText = (EditText) rootView.findViewById(R.id.costEditText);
            mGenerateQRCodeButton = (Button) rootView.findViewById(R.id.generateQRCodeButton);
            mGenerateQRCodeButton.setOnClickListener(generateQRCodeOnClickListener);
            mQRCodeImageView = (ImageView) rootView.findViewById(R.id.qrCodeImageView);
            return rootView;
        }

        View.OnClickListener generateQRCodeOnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                setQRCodeWithString(ParseUser.getCurrentUser().getUsername() + mCostEditText.getText());
            }
        };

        private void setQRCodeWithString(String data) {
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix matrix = writer.encode(
                        data, BarcodeFormat.QR_CODE, 400, 400
                );
                Bitmap bmp = toBitmap(matrix);
                mQRCodeImageView.setImageBitmap(bmp);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        private static Bitmap toBitmap(BitMatrix matrix){
            int height = matrix.getHeight();
            int width = matrix.getWidth();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++){
                for (int y = 0; y < height; y++){
                    bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public static class MyAccountFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private TextView mTotalRewardsTextView;
        private ListView mMealsListView;
        private ArrayList<Meals> mealsList = new ArrayList<Meals>();
        private ArrayList<String> mealsClaimedList = new ArrayList<String>();

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MyAccountFragment newInstance(int sectionNumber) {
            MyAccountFragment fragment = new MyAccountFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public MyAccountFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);
            mTotalRewardsTextView = (TextView) rootView.findViewById(R.id.totalRewardsTextView);
            mMealsListView = (ListView) rootView.findViewById(R.id.mealsListView);

            final ProgressDialog dialog = ProgressDialog.show(getActivity(), null, "Loading Account Data...");
            ParseQuery<ParseObject> pointsQuery = ParseQuery.getQuery("Points");
            pointsQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            pointsQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject rewardsObject, ParseException e) {
                    if (e == null) {
                        Rewards rewards = (Rewards) rewardsObject;
                        mTotalRewardsTextView.setText("Total Rewards: " + rewards.getTotalRewards().toString());
                    } else {
                        Log.d("Rewards", "Error: " + e.getMessage());
                    }
                    ParseQuery<ParseObject> mealsQuery = ParseQuery.getQuery("Meals");
                    mealsQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> results, ParseException e) {
                            if (e == null) {
                                mealsList = new ArrayList<Meals>();
                                Meals meal;
                                for (ParseObject mealObject : results) {
                                    meal = (Meals) mealObject;
                                    mealsList.add(meal);
                                    mealsClaimedList.add(meal.getClaimedString());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, mealsClaimedList);
                                mMealsListView.setAdapter(adapter);
                            }
                            dialog.hide();
                        }
                    });
                }
            });

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
