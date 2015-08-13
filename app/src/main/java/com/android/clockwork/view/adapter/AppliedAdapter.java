package com.android.clockwork.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.clockwork.model.Post;
import com.example.jiabaotan2012.cw.R;

import java.util.ArrayList;

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
        Button editButton = (Button) view.findViewById(R.id.editButton);
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
