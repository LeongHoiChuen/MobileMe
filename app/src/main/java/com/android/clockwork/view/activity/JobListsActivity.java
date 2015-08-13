package com.android.clockwork.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.android.clockwork.controller.MainController;
import com.android.clockwork.view.adapter.ListingAdapter;
import com.android.clockwork.model.Post;
import com.android.clockwork.model.SessionManager;
import com.example.jiabaotan2012.cw.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class JobListsActivity extends AppCompatActivity {

    public final static String PAR_KEY = "KEY";
    ListView listView;
    ListingAdapter listingAdapter;
    ArrayList<Post> postList;
    ProgressDialog dialog;
    SessionManager session;
    HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_lists);
        initializeFooter();
        new HttpAsyncTask().execute("https://clockwork-api.herokuapp.com/api/v1/posts/all.json");

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adptView, View view, int position, long arg3) {
                Bundle bundle = new Bundle();
                listingAdapter = (ListingAdapter) listView.getAdapter();
                bundle.putParcelable(PAR_KEY, (Post) listingAdapter.getItem(position));

                Intent viewJobActivity = new Intent(view.getContext(), ViewJobActivity.class);
                viewJobActivity.putExtras(bundle);
                startActivity(viewJobActivity);
            }
        });
        // Session class instance
        session = new SessionManager(getApplicationContext());

/*        // Button logout
        Button btnLogout = (Button) findViewById(R.id.logoutButton);;
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Clear the User session data
                // and redirect user to MainMenuActivity
                session.logoutUser();
            }
        });*/

        // Check user login (this is the important point)
        // If Session is not logged in , This will redirect user to MainMenuActivity
        // and finish current activity from activity stack.
 /*       if(session.checkLogin()) {

            finish();
        }*/

        // get user data from session
        user = session.getUserDetails();

        // get name
        String name = user.get(SessionManager.KEY_NAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_job_lists, menu);
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

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(JobListsActivity.this);
            dialog.setTitle("Retrieving job listings");
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Post>>(){}.getType();
            postList = gson.fromJson(result, listType);

            listingAdapter = new ListingAdapter(JobListsActivity.this, postList);

            listView.setAdapter(listingAdapter);
            dialog.dismiss();
        }
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
