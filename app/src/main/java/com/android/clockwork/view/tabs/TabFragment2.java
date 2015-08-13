package com.android.clockwork.view.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.clockwork.view.activity.EmployerRegisterActivity;
import com.android.clockwork.view.activity.JSRegisterActivity;
import com.example.jiabaotan2012.cw.R;

public class TabFragment2 extends Fragment {
    ImageView empRegister, jsRegister;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onActivityCreated(savedInstanceState);
        view = inflater.inflate(R.layout.activity_register_type, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        empRegister = (ImageView) view.findViewById(R.id.empRegister);
        jsRegister = (ImageView) view.findViewById(R.id.jsRegister);

        empRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent empRegPage = new Intent(view.getContext(), EmployerRegisterActivity.class);
                startActivity(empRegPage);
            }
        });

        jsRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jsRegPage = new Intent(view.getContext(), JSRegisterActivity.class);
                startActivity(jsRegPage);
            }
        });
    }
}