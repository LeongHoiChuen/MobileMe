package com.example.jiabaotan2012.cw;

/**
 * Created by jiabao.tan.2012 on 2/8/2015.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ListingAdapter extends BaseAdapter {

    private Activity activity;
    private String data;
    private static LayoutInflater inflater = null;

    public ListingAdapter(Activity activity, ArrayList<Post> arrayList) {
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = inflater.inflate(R.layout.activity_job_lists, null);
        }

        TextView jobTitle = (TextView) view.findViewById(R.id.jobTitle);
        TextView hiringCo = (TextView) view.findViewById(R.id.hiringCo);
        TextView startDate = (TextView) view.findViewById(R.id.startDate);
        TextView salary = (TextView) view.findViewById(R.id.salary);
        ImageView locationImage = (ImageView) view.findViewById(R.id.locationImage);
        TextView location = (TextView) view.findViewById(R.id.location);
        Button detailsBtn = (Button) view.findViewById(R.id.detailsBtn);

        // set text
        return null;
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
