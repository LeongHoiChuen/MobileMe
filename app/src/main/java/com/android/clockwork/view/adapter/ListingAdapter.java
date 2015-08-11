package com.android.clockwork.view.adapter;

/**
 * Created by jiabao.tan.2012 on 2/8/2015.
 */

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.clockwork.model.Post;
import com.example.jiabaotan2012.cw.R;

public class ListingAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Post> postList = new ArrayList<Post>();
    private static LayoutInflater inflater = null;

    public ListingAdapter(Activity activity, ArrayList<Post> arrayList) {
        this.activity = activity;
        this.postList = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_row, null);
        }

        Post p = postList.get(position);

        TextView jobTitle = (TextView) view.findViewById(R.id.jobTitle);
        TextView hiringCo = (TextView) view.findViewById(R.id.hiringCo);
        TextView startDate = (TextView) view.findViewById(R.id.startDate);
        TextView salary = (TextView) view.findViewById(R.id.salary);
        ImageView locationImage = (ImageView) view.findViewById(R.id.locationImage);
        TextView location = (TextView) view.findViewById(R.id.location);

        // set text
        jobTitle.setText(p.getHeader());
        hiringCo.setText(p.getCompany());
        startDate.setText("" + p.getJobDate());
        salary.setText("$ " + p.getSalary() + " per hour");

        location.setText(p.getLocation());

        return view;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
