package com.dasharath.myapplication.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dasharath.myapplication.R;

public class EmployeeHolder extends RecyclerView.ViewHolder {

    ImageView imgProfile = null;
    TextView tvName = null;
    TextView tvEmail = null;
    TextView tvBirthday = null;
    TextView tvGender = null;

    EmployeeHolder(View v){
        super(v);
        this.imgProfile = v.findViewById(R.id.imgListProfile);
        this.tvName = v.findViewById(R.id.tvListName);
        this.tvEmail = v.findViewById(R.id.tvListEmail);
        this.tvBirthday = v.findViewById(R.id.tvListBirthday);
        this.tvGender = v.findViewById(R.id.tvListGender);
    }
}
