package hu.mobilalkfejl.clinicalissuelog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 696969;

    final Calendar calendar = Calendar.getInstance();

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

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void doRegistration(View view) {
        String email = emailRegister.getText().toString();
        String password = passwordRegister.getText().toString();
        String passwordAgain = passwordAgainRegister.getText().toString();
        String name = nameRegister.getText().toString();
        String birthDate= birthDateRegister.getText().toString();
        String qualificationCode = qualificationCodeRegister.getText().toString();
        String qualificationIssuer = qualificationIssuerRegister.getText().toString();
        boolean active;
        String gender = genderRegister.getSelectedItem().toString();

        int radioButtonId = activeRegister.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(radioButtonId);

        if(!(selectedRadioButton == null)){
            active = selectedRadioButton.getText().toString().equals("Yes");
        }else{
            Toast.makeText(this,"Unselected active status!", Toast.LENGTH_LONG).show();
        }

        if(email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty() || name.isEmpty()
                || birthDate == null || qualificationCode.isEmpty() || qualificationIssuer.isEmpty() || gender.isEmpty()){

            Toast.makeText(this,"Registration form is incomplete!",Toast.LENGTH_LONG).show();

        }else{
            if(password.equals(passwordAgain)){
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, task ->{
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Practitioner created successfully");
                        afterRegistration();
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

    public void afterRegistration(){
        Intent intent = new Intent(this, IssueListActivity.class);
        startActivity(intent);
    }

    private void updateLabel() {
        String format = "yyyy-mm-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        birthDateRegister.setText(simpleDateFormat.format(calendar.getTime()));
    }
}