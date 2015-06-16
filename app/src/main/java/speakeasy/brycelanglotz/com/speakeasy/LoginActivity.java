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
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Arrays;


public class LoginActivity extends ActionBarActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mReenterPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = (EditText) findViewById(R.id.emailEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mReenterPasswordEditText = (EditText) findViewById(R.id.reenterPasswordEditText);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginOnClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void popToRootActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
        startActivity(intent);
    }

    private void login() {
        String lowercaseEmail = mEmailEditText.getText().toString().toLowerCase();
        String password = mPasswordEditText.getText().toString();
        String secondPassword = mReenterPasswordEditText.getText().toString();
        if (secondPassword.equals("")) {
            final ProgressDialog dialog = ProgressDialog.show(this, null, "Logging In...");
            ParseUser.logInInBackground(lowercaseEmail, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e != null) {
                        if (e.getCode() == 101) {
                            Toast.makeText(getApplicationContext(), "● Invalid login credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        boolean emailVerified = (boolean) parseUser.get("emailVerified");
                        if (emailVerified) {
                            popToRootActivity();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "● Verify E-Mail and try again",
                                    Toast.LENGTH_LONG).show();
                            ParseUser.logOut();
                        }
                    }
                    dialog.hide();
                }
            });
        }
        else if (!secondPassword.equals("") && secondPassword.equals(password)) {
            if (password.length() < 5 || password.length() > 16) {
                Toast.makeText(getApplicationContext(), "● Password must be 5-16 characters",
                        Toast.LENGTH_LONG).show();
                ParseUser.logOut();
            }
            else {
                ParseUser user = new ParseUser();
                user.setEmail(lowercaseEmail);
                user.setPassword(password);
                user.setUsername(lowercaseEmail);

                final ProgressDialog dialog = ProgressDialog.show(this, null, "Creating User...");

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            if (e.getCode() == 202 || e.getCode() == 203) {
                                Toast.makeText(getApplicationContext(), "● E-Mail is already registered",
                                        Toast.LENGTH_LONG).show();
                            }
                            if (e.getCode() == 125) {
                                Toast.makeText(getApplicationContext(), "● Invalid E-Mail",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            mReenterPasswordEditText.setText("");
                            Toast.makeText(getApplicationContext(), "● Verify E-Mail before logging in",
                                    Toast.LENGTH_LONG).show();
                            ParseUser.logOut();
                        }
                        dialog.hide();
                    }
                });
            }
        }
        if (!secondPassword.equals("") && !password.equals(secondPassword)) {
            Toast.makeText(getApplicationContext(), "● Passwords don't match",
                    Toast.LENGTH_LONG).show();
        }
    }

    View.OnClickListener loginOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            login();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
