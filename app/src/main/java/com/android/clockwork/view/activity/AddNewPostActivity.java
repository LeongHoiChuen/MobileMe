package com.android.clockwork.view.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.clockwork.model.Post;
import com.android.clockwork.model.SessionManager;
import com.example.jiabaotan2012.cw.R;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class AddNewPostActivity extends ActionBarActivity implements View.OnClickListener {

    EditText jobTitle, jobLocation, jobDescription, salary, jobDate;
    Button submitBtn;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat sdf;
    Post post;
    SessionManager session;
    HashMap<String, String> user;
    String name, email, authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_post);
        initializeFooter();

        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);
        email = user.get(SessionManager.KEY_EMAIL);
        authToken = user.get(SessionManager.KEY_AUTHENTICATIONTOKEN);

        jobTitle = (EditText) findViewById(R.id.jobTitle);
        jobLocation = (EditText) findViewById(R.id.jobLocation);
        jobDescription = (EditText) findViewById(R.id.jobDescription);
        salary = (EditText) findViewById(R.id.salary);
        jobDate = (EditText) findViewById(R.id.jobDate);
        jobDate.setInputType(InputType.TYPE_NULL);

        sdf = new SimpleDateFormat("dd-MM-yyy");
        setDateTimeField();

        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submit new job post
                new HttpAsyncTask().execute("https://clockwork-api.herokuapp.com/api/v1/posts/new");
                Intent empDashboard = new Intent(view.getContext(), EmployerDashboardActivity.class);
                startActivity(empDashboard);
            }
        });
    }

    private void setDateTimeField() {
        jobDate.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                jobDate.setText(sdf.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_post, menu);
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

    @Override
    public void onClick(View v) {
        datePickerDialog.show();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Successfully created a new job listing!", Toast.LENGTH_LONG).show();
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
            post = new Post(jobTitle.getText().toString(), Integer.parseInt(salary.getText().toString()),
                    jobDescription.getText().toString(), jobLocation.getText().toString(), jobDate.getText().toString());
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("header", post.getHeader()));
            nvps.add(new BasicNameValuePair("company", name));
            nvps.add(new BasicNameValuePair("salary", "" + post.getSalary()));
            nvps.add(new BasicNameValuePair("description", post.getDescription()));
            nvps.add(new BasicNameValuePair("location", post.getLocation()));
            nvps.add(new BasicNameValuePair("job_date", post.getJobDate()));
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
