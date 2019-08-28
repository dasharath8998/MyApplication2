package com.dasharath.myapplication.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.bumptech.glide.Glide;
import com.dasharath.myapplication.R;
import com.dasharath.myapplication.activity.MainActivity;
import com.dasharath.myapplication.database.Employee;
import com.dasharath.myapplication.database.EmployeeDatabase;
import com.dasharath.myapplication.listeners.DateSetListener;
import com.dasharath.myapplication.utils.Utils;

import java.text.ParseException;
import java.util.Calendar;


public class AddEmployeeFragment extends Fragment {

    private int cYear = 0;
    private int cMonth = 0;
    private int cDay = 0;
    private String uri = "";
    private Long eId = -1L;

    private ImageView imgEdit, imageProfile;
    private Button btnCalendar, submit, reset;
    private EditText etName;
    private EditText etEmail;
    private TextView etBirthDay;
    private TextView tvYears;
    private RadioButton rMale, rFemale;
    private AwesomeValidation awesomeValidation;

    /*
     * Inflating view
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        cYear = c.get(Calendar.YEAR);
        cMonth = c.get(Calendar.MONTH);
        cDay = c.get(Calendar.DAY_OF_MONTH);
        MainActivity.hideKeyboardFrom(getActivity());
        if (getArguments() != null) {
            eId = getArguments().getLong(Utils.EMPLOYEE_ID);
        }

        if (eId != -1) {
            setDataForm();
        }

        Log.d("Employeeadd", "Eid: " + eId.toString());
        return inflater.inflate(R.layout.fragment_add_employee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgEdit = (ImageView) getActivity().findViewById(R.id.imgEdit);
        btnCalendar = (Button) getActivity().findViewById(R.id.btnCalender);
        submit = (Button) getActivity().findViewById(R.id.btnSubmit);
        reset = (Button) getActivity().findViewById(R.id.btnReset);
        etName = (EditText) getActivity().findViewById(R.id.etName);
        etEmail = (EditText) getActivity().findViewById(R.id.etEmail);
        etBirthDay = (TextView) getActivity().findViewById(R.id.etBirthday);
        tvYears = (TextView) getActivity().findViewById(R.id.tvYear);
        imageProfile = (ImageView) getActivity().findViewById(R.id.imgProfile);
        rMale = (RadioButton) getActivity().findViewById(R.id.rBMale);
        rFemale = (RadioButton) getActivity().findViewById(R.id.rBFemale);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        listeners();
    }

    private void listeners() {

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (status == PackageManager.PERMISSION_GRANTED) {
                    readFiles();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                }
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendar();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eId == -1) {
                    insertDataToDb();
                } else {
                    updateData();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetForm();
            }
        });
    }

    private void resetForm() {
        Glide.with(getContext()).load(R.drawable.ic_account_circle_black_24dp).into(imageProfile);
        etName.setText("");
        etEmail.setText("");
        etBirthDay.setText("Birth date");
        tvYears.setText("Years:");
        rMale.setChecked(true);
    }

    private void openCalendar() {
        Utils.openCalendar(getContext(), cYear, cMonth, cDay, new DateSetListener() {
            @Override
            public void onDateSelected(int year, int month, int day) {
                cYear = year;
                cMonth = month + 1;
                cDay = day;
                etBirthDay.setText(cDay + "," + cMonth + "," + cYear);
                tvYears.setText(Utils.getCalculatedAge(cYear, cMonth, cDay));
            }
        });
    }

    private void insertDataToDb() {

        if (checkValidation()) {

            if (etBirthDay.getText().toString().equals("Birth date")) {
                openCalendar();
            } else {
                RadioGroup radioGroup = (RadioGroup) getActivity().findViewById(R.id.rGroup);
                int selected = radioGroup.getCheckedRadioButtonId();
                final RadioButton checkedButton = getActivity().findViewById(selected);
                final Employee emp = new Employee((Long) null, uri, etName.getText().toString(), etEmail.getText().toString(), etBirthDay.getText().toString(), checkedButton.getText().toString(), (Long) System.currentTimeMillis());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EmployeeDatabase db = EmployeeDatabase.getInstance(getContext());
                        db.employeeDao().insertData(emp);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        });
                    }
                }).start();
            }
        }

    }

    private void updateData() {
        if (checkValidation()) {

            if (etBirthDay.getText().toString().equals("Birth date")) {
                openCalendar();
            } else {
                RadioGroup radioGroup = (RadioGroup) getActivity().findViewById(R.id.rGroup);
                int selected = radioGroup.getCheckedRadioButtonId();
                final RadioButton checkedButton = getActivity().findViewById(selected);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EmployeeDatabase db = EmployeeDatabase.getInstance(getContext());
                        db.employeeDao().updateEmployee(eId, uri, etName.getText().toString(), etEmail.getText().toString(), etBirthDay.getText().toString(), checkedButton.getText().toString(), (Long) System.currentTimeMillis());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        });
                    }
                }).start();
            }
        }
    }

    private void setDataForm() {

        lockNavigationDrawer();

        new Thread(new Runnable() {
            @Override
            public void run() {
                EmployeeDatabase db = EmployeeDatabase.getInstance(getContext());
                final Employee emp = db.employeeDao().getOneRecord(eId);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getContext()).load(emp.getImg()).placeholder(R.drawable.ic_account_circle_black_24dp).into(imageProfile);
                        etName.setText(emp.getName());
                        etEmail.setText(emp.getEmail());
                        etBirthDay.setText(emp.getBirthday());
                        if (emp.getGender().equals("Male")) {
                            rMale.setChecked(true);
                        } else {
                            rFemale.setChecked(true);
                        }
                        try {
                            tvYears.setText(Utils.stringToDate(emp.getBirthday(), "dd,mm,yyyy"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        submit.setText("Update");
                    }
                });
            }
        }).start();

    }

    private void lockNavigationDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private boolean checkValidation() {
        if (etName.getText().toString().equals("")) {
            etName.setError("Enter Name");
            return false;
        }else if(etEmail.getText().toString().equals("")){
            etEmail.setError("Enter Email");
            return false;
        }else {
            awesomeValidation.addValidation(getActivity(), R.id.etName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_error);
            awesomeValidation.addValidation(getActivity(), R.id.etEmail, Patterns.EMAIL_ADDRESS, R.string.email_error);
            return awesomeValidation.validate();
        }
    }

    /*
     * If permission granted than load selected image in circle image view
     * */
    private void readFiles() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, 124);
    }

    /*
     * if image is selected than set image to image view
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 124 && data != null) {
            ImageView imageProfile = (ImageView) getActivity().findViewById(R.id.imgProfile);
            uri = data.getData().toString();
            Glide.with(getContext()).load(uri).into(imageProfile);
        }
    }


    /*
     * if user clicks to allow button on permission dialog than open gallery
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readFiles();
        }
    }
}
