package com.dasharath.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dasharath.myapplication.R;
import com.dasharath.myapplication.adapter.EmployeeAdapter;
import com.dasharath.myapplication.database.Employee;
import com.dasharath.myapplication.database.EmployeeDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class EmployeeListFragment extends Fragment {

    List<Employee> arrayList = null;
    EmployeeAdapter employeeAdapter;
    List<Employee> filterdNames = new ArrayList();

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        final DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);

        if(drawerLayout.getDrawerLockMode(Gravity.NO_GRAVITY) == 0){
            toolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        }
        return inflater.inflate(R.layout.fragment_employee_list,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvNDF = getActivity().findViewById(R.id.tvNDF);

        new Thread(new Runnable() {
            @Override
            public void run() {
                EmployeeDatabase db = EmployeeDatabase.getInstance(getContext());
                arrayList = db.employeeDao().getAll();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(arrayList.size() == 0){
                            tvNDF.setVisibility(View.VISIBLE);
                        }else {
                            if(tvNDF.getVisibility() == View.VISIBLE){
                                tvNDF.setVisibility(View.GONE);
                            }
                            RecyclerView empRView = (RecyclerView) getActivity().findViewById(R.id.empRView);
                            empRView.setLayoutManager(new LinearLayoutManager(getContext()));
                            employeeAdapter = new EmployeeAdapter(getContext(),arrayList,getActivity());
                            empRView.setAdapter(employeeAdapter);
                        }
                    }
                });
            }
        }).start();

        EditText etSearch = getActivity().findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    filterdNames.clear();
                    displayData();
                }else {
                    filter(editable+"");
                }
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void displayData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EmployeeDatabase db = EmployeeDatabase.getInstance(getContext());
                arrayList = db.employeeDao().getAll();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        employeeAdapter.filteredList(arrayList);
                    }
                });
            }
        }).start();
    }

    private void filter(String text) {
        filterdNames.clear();
        for (Employee s : arrayList){
            if(s.getName().toLowerCase().contains(text.toLowerCase()) || s.getEmail().toLowerCase().contains(text.toLowerCase())){
                filterdNames.add(s);
            }
        }
        employeeAdapter.filteredList(filterdNames);
    }


    private void sortByEmail(){

        Collections.sort(arrayList, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o1.getEmail().compareToIgnoreCase(o2.getEmail());
            }
        });
        employeeAdapter.notifyDataSetChanged();
    }

    private void sortByLastAdded(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            arrayList.sort(Comparator.comparing(Employee::getTimestamp).reversed());
        }
        employeeAdapter.notifyDataSetChanged();
    }

    private void sortByName(){
        Collections.sort(arrayList, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        employeeAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sortingEmail) {
            sortByEmail();
        }else if (id == R.id.sortingLastAdded) {
            sortByLastAdded();
        }else if (id == R.id.sortingName) {
            sortByName();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
}
