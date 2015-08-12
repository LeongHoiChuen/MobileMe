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

public class PublishedAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Post> publishedList = new ArrayList<Post>();
    private static LayoutInflater inflater = null;

    public PublishedAdapter(Activity activity, ArrayList<Post> arrayList) {
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
            view = inflater.inflate(R.layout.published_posts_row, null);
        }

        Post p = publishedList.get(position);

        TextView jobTitle = (TextView) view.findViewById(R.id.jobTitle);
        TextView jobDate = (TextView) view.findViewById(R.id.jobDate);
        TextView salary = (TextView) view.findViewById(R.id.salary);
        TextView numApplicants = (TextView) view.findViewById(R.id.numApplicants);
        Button editButton = (Button) view.findViewById(R.id.editButton);
        // set text
        jobTitle.setText(p.getHeader());
        jobDate.setText("" + p.getJobDate());
        salary.setText("$ " + p.getSalary() + " per hour");
        numApplicants.setText(p.getApplicant_count() + " Applicants");

        if (p.getApplicant_count() > 0) {
            // view only
            editButton.setBackgroundColor(Color.GREEN);
            editButton.setText("View");
        } else {
            // allow to edit
            editButton.setText("Edit");
        }
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
