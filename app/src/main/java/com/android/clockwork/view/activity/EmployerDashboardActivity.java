package com.android.clockwork.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.clockwork.model.Post;
import com.android.clockwork.model.SessionManager;
import com.android.clockwork.view.adapter.AppliedAdapter;
import com.android.clockwork.view.adapter.PublishedAdapter;
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

public class EmployerDashboardActivity extends AppCompatActivity {
    ProgressDialog dialog;
    ListView publishedList;
    PublishedAdapter publishedAdapter;
    ArrayList<Post> postList = new ArrayList<Post>();
    SessionManager session;
    HashMap<String, String> user;
    String email, authToken;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_dashboard);
        initializeFooter();

        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        authToken = user.get(SessionManager.KEY_AUTHENTICATIONTOKEN);


        new HttpAsyncTask().execute("https://clockwork-api.herokuapp.com/api/v1/users/get_jobs");

        publishedList = (ListView) findViewById(R.id.publishedList);
        publishedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adptView, View view, int position, long arg3) {
                // edit post link
                publishedAdapter = (PublishedAdapter) publishedList.getAdapter();
                post = (Post) publishedAdapter.getItem(position);
                new HttpAsyncTask().execute("https://clockwork-api.herokuapp.com/api/v1/posts/delete");

                Intent empDashboard = new Intent(view.getContext(), EmployerDashboardActivity.class);
                startActivity(empDashboard);
            }
        });

        final Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
                Intent jobListing = new Intent(view.getContext(), JobListsActivity.class);
                startActivity(jobListing);
            }
        });

        final Button addPostButton = (Button) findViewById(R.id.addPostButton);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewPost = new Intent(view.getContext(), AddNewPostActivity.class);
                startActivity(addNewPost);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_employer_dashboard, menu);
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EmployerDashboardActivity.this);
            dialog.setTitle("Retrieving your published jobs");
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (post == null) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Post>>() {
                }.getType();
                postList = gson.fromJson(result, listType);

                publishedAdapter = new PublishedAdapter(EmployerDashboardActivity.this, postList);

                publishedList.setAdapter(publishedAdapter);
                dialog.dismiss();
            } else {
                post = null;
                dialog.dismiss();
            }
        }
    }

    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            // 3. build NVP
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (post != null) {
                nvps.add(new BasicNameValuePair("job_id", String.valueOf(post.getId())));
            }
            nvps.add(new BasicNameValuePair("email", email));

            // 4. set httpPost Entity
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            // 5. Set some headers to inform server about the type of the content
            httpPost.setHeader("Authentication-Token", authToken);

            // 6. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

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
                        Intent employerEditProfile = new Intent(view.getContext(), EditEmployerProfileActivity.class);
                        startActivity(employerEditProfile);
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
