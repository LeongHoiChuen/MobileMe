package com.android.clockwork.view.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.android.clockwork.controller.MainController;
import com.android.clockwork.model.Session;
import com.android.clockwork.model.SessionManager;
import com.example.jiabaotan2012.cw.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainMenuActivity extends AppCompatActivity {
    final static MainController mainController = new MainController();
    static int statusCode;
    EditText ed1,ed2;
    TextView tx1;
    Session loginSession;
    SessionManager session;
    static HttpResponse httpResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ed1 = (EditText)findViewById(R.id.editText);
        ed2 = (EditText)findViewById(R.id.editText2);
        tx1 = (TextView)findViewById(R.id.resultText);

        //Session Session Manager'
        session = new SessionManager(getApplicationContext());
        //if(!session.isUserLoggedIn()) {
            //Intent descriptionIntent = getIntent();
        String status= session.getLogoutStatus();
                    //descriptionIntent.getExtras().getString("status");
        if (status.equals("user has been successfully signed out")) {
            tx1.setText("You have successfully logged out!");
        }
        //}

        final Button button = (Button)findViewById(R.id.LoginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpAsyncTask().execute("https://clockwork-api.herokuapp.com/users/sign_in.json");
            }
        });

        final Button button2 = (Button)findViewById(R.id.ListingButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jobListing = new Intent(view.getContext(), JobListsActivity.class);
                startActivity(jobListing);
            }
        });

        final Button button3 = (Button)findViewById(R.id.RegisterButton);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(view.getContext(), RegisterTypeActivity.class);
                startActivity(register);
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

        final Button mainButton = (Button) findViewById(R.id.tabbedButton);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainMenu = new Intent(view.getContext(), MainActivity.class);
                startActivity(mainMenu);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
    public static String POST(String url, Session loginSession){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();

            pairs.add(new BasicNameValuePair("user[email]", loginSession.getEmail()));
            pairs.add(new BasicNameValuePair("user[password]", loginSession.getPassword()));

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            //StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");

            // 8. Execute POST request to the given URL
            httpResponse = httpclient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();


            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            loginSession = new Session(ed1.getText().toString(), ed2.getText().toString());

            return POST(urls[0], loginSession);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(statusCode == 401) {
                tx1.setText("The email / password is invalid, please try again.");
            }else {
                Gson gson = new Gson();
                Type hashType = new TypeToken<HashMap<String, Object>>(){}.getType();
                HashMap userHash = gson.fromJson(result, hashType);
                Double idDouble = (Double)userHash.get("id");
                int id = idDouble.intValue();
                String username = (String)userHash.get("username");
                String email = (String)userHash.get("email");
                String accountType = (String)userHash.get("account_type");
                String authenticationToken = (String)userHash.get("authentication_token");
                String passWord = ed2.getText().toString();

                if(accountType.equals("job_seeker")) {
                    session.createUserLoginSession(id, username, email, accountType, passWord, authenticationToken);
                    // Starting MainActivity
                    Intent i = new Intent(getApplicationContext(), JobListsActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                    //finish();
                }else {
                    session.createUserLoginSession(id, username, email, accountType, passWord, authenticationToken);
                    // Starting MainActivity
                    Intent i = new Intent(getApplicationContext(), JobListsActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    //finish();

                }
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
}
