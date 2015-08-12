package com.android.clockwork.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.android.clockwork.view.activity.MainMenuActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Hoi Chuen on 10/8/2015.
 */
public class SessionManager {
    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "SessionUser";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // Session name (make variable public to access from outside)
    public static final String KEY_NAME = "userName";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_ACCOUNTYPE = "accountType";

    public static final String KEY_AUTHENTICATIONTOKEN = "authenticationToken" ;

    public static final String KEY_PASSWORD = "passWord" ;

    public static final String KEY_ID = "id" ;

    String status = "";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(int id, String userName, String email, String accountType, String passWord, String authenticationToken){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        //Storing ID in pref
        editor.putInt(KEY_ID, id);

        //Storing accountType in pref
        editor.putString(KEY_ACCOUNTYPE, accountType);

        //Storing passWord in pref
        editor.putString(KEY_PASSWORD, passWord);

        //Storing authenticationToken in pref
        editor.putString(KEY_AUTHENTICATIONTOKEN, authenticationToken);

        // Storing name in pref
        editor.putString(KEY_NAME, userName);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainMenuActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_ACCOUNTYPE, pref.getString(KEY_ACCOUNTYPE, null));

        user.put(KEY_AUTHENTICATIONTOKEN, pref.getString(KEY_AUTHENTICATIONTOKEN, null));

        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // return user
        return user;
    }

    public HashMap<String, Integer> getUserID(){

        //Use hashmap to store user credentials
        HashMap<String, Integer> user = new HashMap<String, Integer>();

        // user name
        user.put(KEY_ID, pref.getInt(KEY_ID, 0));

        // return user
        return user;
    }

    public String getLogoutStatus () {
        return status;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, MainMenuActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);


        //new HttpAsyncTask().execute("https://clockwork-api.herokuapp.com/users/sign_out.json");
    }

    // Check for login
    public boolean isUserLoggedIn(){

        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public static String DELETE(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make Delete request to the given URL
            HttpDelete httpDelete = new HttpDelete(url);

            // 3. build jsonObject

            httpDelete.setHeader("Accept", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpDelete);

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

            return DELETE(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Gson gson = new Gson();
            //Type hashType = new TypeToken<HashMap<String, Object>>(){}.getType();
            //HashMap userHash = gson.fromJson(result, hashType);
            //String successfulLogout = (String)userHash.get("message");
            status = result;

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
