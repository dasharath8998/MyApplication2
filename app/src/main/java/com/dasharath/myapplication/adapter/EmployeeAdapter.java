package com.dasharath.myapplication.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dasharath.myapplication.R;
import com.dasharath.myapplication.database.Employee;
import com.dasharath.myapplication.database.EmployeeDatabase;
import com.dasharath.myapplication.fragment.AddEmployeeFragment;
import com.dasharath.myapplication.utils.Utils;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeHolder> {
    Context context;
    List<Employee> list;
    Activity activity;
    public EmployeeAdapter(Context context, List<Employee> list,Activity activity){
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployeeHolder(LayoutInflater.from(context).inflate(R.layout.item_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImg()).placeholder(R.drawable.ic_account_circle_black_24dp).into(holder.imgProfile);
        holder.tvName.setText(list.get(position).getName());
        holder.tvEmail.setText(list.get(position).getEmail());
        holder.tvBirthday.setText(list.get(position).getBirthday());
        holder.tvGender.setText(list.get(position).getGender());

        onLongClickOnItem(holder, position);

        onClickOnItem(holder, position);
    }

    private void onClickOnItem(@NonNull EmployeeHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong(Utils.EMPLOYEE_ID,list.get(position).getId());
                AddEmployeeFragment fragment = new AddEmployeeFragment();
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.employeeFragment,fragment).addToBackStack(null).commit();
            }
        });
    }

    private void onLongClickOnItem(@NonNull EmployeeHolder holder, final int position) {

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                EmployeeDatabase db = EmployeeDatabase.getInstance(context);
                                db.employeeDao().deleteRecord(list.get(position).getId());
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Deleted..."+position, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).start();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setMessage("Select from below").setTitle("Message").show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filteredList(List<Employee> filterList){
        this.list = filterList;
        notifyDataSetChanged();
    }

}
