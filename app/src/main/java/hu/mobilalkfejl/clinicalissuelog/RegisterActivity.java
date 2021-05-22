package hu.mobilalkfejl.clinicalissuelog;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final int SECRET_KEY = 268983;
    private static final String DATE_FORMAT = "yyyy-MM-dd";


    final Calendar calendar = Calendar.getInstance();

    private FirebaseFirestore mFirestore;

    EditText emailRegister;
    EditText passwordRegister;
    EditText passwordAgainRegister;
    EditText nameRegister;
    EditText birthDateRegister;
    EditText qualificationCodeRegister;
    EditText qualificationIssuerRegister;
    Spinner genderRegister;
    RadioGroup activeRegister;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailRegister = findViewById(R.id.editTextPractitionerEmail);
        passwordRegister = findViewById(R.id.editTextPractitionerPassword);
        passwordAgainRegister = findViewById(R.id.editTextPractitionerPasswordAgain);
        nameRegister = findViewById(R.id.editTextPractitionerName);
        birthDateRegister = findViewById(R.id.editTextPractitionerBirthDate);
        qualificationCodeRegister = findViewById(R.id.editTextPractitionerQualificationCode);
        qualificationIssuerRegister = findViewById(R.id.editTextPractitionerQualificationIssuer);
        activeRegister = findViewById(R.id.radioGroupActive);
        genderRegister = findViewById(R.id.spinnerGender);

        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        birthDateRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterActivity.this, onDateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        genderRegister.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderRegister.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void doRegistration(View view) {
        String email = emailRegister.getText().toString();
        String password = passwordRegister.getText().toString();
        String passwordAgain = passwordAgainRegister.getText().toString();
        String name = nameRegister.getText().toString();
        String birthDate = birthDateRegister.getText().toString();
        String qualificationCode = qualificationCodeRegister.getText().toString();
        String qualificationIssuer = qualificationIssuerRegister.getText().toString();
        String gender = genderRegister.getSelectedItem().toString();

        int radioButtonId = activeRegister.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(radioButtonId);

        if(selectedRadioButton == null){
            Toast.makeText(this,"Unselected active status!", Toast.LENGTH_LONG).show();
        }

        if(email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty() || name.isEmpty() || selectedRadioButton == null || qualificationCode.isEmpty() || qualificationIssuer.isEmpty() || gender.isEmpty()){

            Toast.makeText(this,"Registration form is incomplete!",Toast.LENGTH_LONG).show();

        }else{
            boolean active = selectedRadioButton.getText().toString().equals("Yes");
            if(password.equals(passwordAgain)){
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, task ->{
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Practitioner created successfully");
                        Qualification qualification = new Qualification(qualificationCode,qualificationIssuer);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                        Practitioner practitioner = null;
                        try {
                            practitioner = new Practitioner(email, name, active, gender, dateFormat.parse(birthDate), qualification);
                        } catch (ParseException e) {
                            Toast.makeText(RegisterActivity.this,"Wrong date format!",Toast.LENGTH_LONG).show();
                        }
                        mFirestore.collection("Practitioners").add(practitioner);
                        afterRegistration(email);
                    }else{
                             Log.d(LOG_TAG, "Practitioner creation failed!");
                            Toast.makeText(RegisterActivity.this,"Practitioner creation failed: \n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                System.out.println("not same");
                Toast.makeText(RegisterActivity.this,"Passwords don't match!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void afterRegistration(String email){
        Intent intent = new Intent(this, DetectedIssueListActivity.class);
        intent.putExtra("currentPractitionerEmail",email);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    private void updateLabel() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        birthDateRegister.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public void cancel(View view) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}