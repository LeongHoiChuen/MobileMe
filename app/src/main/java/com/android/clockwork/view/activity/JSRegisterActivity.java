package com.android.clockwork.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.clockwork.model.Account;
import com.android.clockwork.model.SessionManager;
import com.example.jiabaotan2012.cw.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JSRegisterActivity extends AppCompatActivity {
    EditText emailText, nameText, pwText;
    Account account;
    SessionManager session;
    static HttpResponse httpResponse;
    static int statusCode;
    HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_register);
        initializeFooter();
        session = new SessionManager(getApplicationContext());

        user = session.getUserDetails();

        emailText = (EditText)findViewById(R.id.emailText);
        nameText = (EditText)findViewById(R.id.nameText);
        pwText = (EditText)findViewById(R.id.pwText);

        final Button submitBtn = (Button)findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpAsyncTask().execute("https://clockwork-api.herokuapp.com/users.json");

                Intent editProfile = new Intent(view.getContext(), EditProfileActivity.class);
                startActivity(editProfile);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jsregister, menu);
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

    public static String POST(String url, Account account){
        InputStream inputStream = null;
        String result = "";

        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            // 3. build object to post
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("user[email]", account.getEmail()));
            pairs.add(new BasicNameValuePair("user[username]", account.getName()));
            pairs.add(new BasicNameValuePair("user[password]", account.getPassword()));
            pairs.add(new BasicNameValuePair("user[password_confirmation]", account.getRepassword()));
            pairs.add(new BasicNameValuePair("user[account_type]", account.getType()));

            // 4. set httpPost Entity
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));

            // 5. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");

            // 6. Execute POST request to the given URL
            httpResponse = httpclient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();

            // 7. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 8. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 9. return result
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            account = new Account(emailText.getText().toString(), nameText.getText().toString(), pwText.getText().toString(),
                    pwText.getText().toString(), "job_seeker");

            return POST(urls[0], account);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();

            if (statusCode == 201) {
                Gson gson = new Gson();
                Type hashType = new TypeToken<HashMap<String, Object>>() {}.getType();
                HashMap userHash = gson.fromJson(result, hashType);
                Double idDouble = (Double) userHash.get("id");
                int id = idDouble.intValue();
                String username = (String) userHash.get("username");
                String email = (String) userHash.get("email");
                String accountType = (String) userHash.get("account_type");
                String authenticationToken = (String) userHash.get("authentication_token");
                String passWord = pwText.getText().toString();
                session.createUserLoginSession(id, username, email, accountType, passWord, authenticationToken);
                //finish();
            } else {
                //set your edit text to display the error msg (E.g password too short or email invalid, according to the API response
            }
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public void initializeFooter() {
        final ImageButton jobListing = (ImageButton)findViewById(R.id.jobListing);
        jobListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jobListing = new Intent(view.getContext(), JobListsActivity.class);
                startActivity(jobListing);
            }
        });

        final ImageButton jobDashboard = (ImageButton) findViewById(R.id.jobDashboard);
        jobDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to change and check for employer or JS dashboard
                if (session.checkLogin()) {
                    Intent loginRedirect = new Intent(view.getContext(), MainMenuActivity.class);
                    startActivity(loginRedirect);
                } else {
                    if (user.get(SessionManager.KEY_ACCOUNTYPE).equalsIgnoreCase("employer")) {
                        Intent employerDashboard = new Intent(view.getContext(), EmployerDashboardActivity.class);
                        startActivity(employerDashboard);
                    } else {
                        Intent jsDashboard = new Intent(view.getContext(), JSDashboardActivity.class);
                        startActivity(jsDashboard);
                    }
                }
            }
        });

        final ImageButton accountSettings = (ImageButton) findViewById(R.id.accountSettings);
        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to change the link
                if (session.checkLogin()) {
                    Intent loginRedirect = new Intent(view.getContext(), MainMenuActivity.class);
                    startActivity(loginRedirect);
                } else {
                    if (user.get(SessionManager.KEY_ACCOUNTYPE).equalsIgnoreCase("employer")) {
                        // to confirm and change link
                    } else {
                        Intent editProfile = new Intent(view.getContext(), EditProfileActivity.class);
                        startActivity(editProfile);
                    }
                }
            }
        });

        final ImageButton analytics = (ImageButton) findViewById(R.id.analytics);
        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                if (session.checkLogin()) {
                    Intent loginRedirect = new Intent(view.getContext(), MainMenuActivity.class);
                    startActivity(loginRedirect);
                } else {
                    // analytics link
                }
            }
        });
    }
}
