package com.example.jiabaotan2012.cw;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;


public class RegisterActivity extends ActionBarActivity {

    EditText emailText, nameText, pwText, repwText, resultText;
    RadioGroup typeGroup;
    RadioButton acctType;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = (EditText)findViewById(R.id.emailText);
        nameText = (EditText)findViewById(R.id.nameText);
        pwText = (EditText)findViewById(R.id.pwText);
        repwText = (EditText)findViewById(R.id.repwText);
        typeGroup = (RadioGroup) findViewById(R.id.typeGroup);
        resultText = (EditText) findViewById(R.id.resultText);

        final Button submitBtn = (Button)findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = typeGroup.getCheckedRadioButtonId();
                acctType = (RadioButton) findViewById(selectedId);
                new HttpAsyncTask().execute("https://clockwork-api.herokuapp.com/users.json");


                //Intent register = new Intent(view.getContext(), RegisterActivity.class);
                //startActivity(register);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            //jsonObject.accumulate("user[email]", account.getEmail());
            //jsonObject.accumulate("user[username]", account.getName());
            //jsonObject.accumulate("user[password]", account.getPassword());
            //jsonObject.accumulate("user[password_confirmation]", account.getRepassword());
            //jsonObject.accumulate("user[account_type]", account.getType());

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("user[email]", account.getEmail()));
            pairs.add(new BasicNameValuePair("user[username]", account.getName()));
            pairs.add(new BasicNameValuePair("user[password]", account.getPassword()));
            pairs.add(new BasicNameValuePair("user[password_confirmation]", account.getRepassword()));
            pairs.add(new BasicNameValuePair("user[account_type]", account.getType()));

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
            HttpResponse httpResponse = httpclient.execute(httpPost);

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
            String accountType = "";

            if (acctType.getText().toString().equals("Employer")) {
                accountType = "employer";
            } else {
                accountType = "job_seeker";
            }

            account = new Account(emailText.getText().toString(), nameText.getText().toString(), pwText.getText().toString(),
                    repwText.getText().toString(), accountType);

            return POST(urls[0], account);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            resultText.setText(result);
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
