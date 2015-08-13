package com.android.clockwork.view.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.clockwork.model.Post;
import com.android.clockwork.view.activity.EmployerDashboardActivity;
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
import java.util.List;

public class AppliedAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Post> publishedList = new ArrayList<Post>();
    private static LayoutInflater inflater = null;

    public AppliedAdapter(Activity activity, ArrayList<Post> arrayList) {
        this.activity = activity;
        this.publishedList = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            view = inflater.inflate(R.layout.applied_posts_row, null);
        }

        Post p = publishedList.get(position);

        TextView jobTitle = (TextView) view.findViewById(R.id.jobTitle);
        TextView jobDate = (TextView) view.findViewById(R.id.jobDate);
        TextView salary = (TextView) view.findViewById(R.id.salary);
        Button editButton = (Button) view.findViewById(R.id.withdrawButton);
        // set text
        jobTitle.setText(p.getHeader());
        jobDate.setText("" + p.getJobDate());
        salary.setText("$ " + p.getSalary() + " per hour");

        return view;
    }

    @Override
    public int getCount() {
        return publishedList.size();
    }

    @Override
    public Object getItem(int position) {
        return publishedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
