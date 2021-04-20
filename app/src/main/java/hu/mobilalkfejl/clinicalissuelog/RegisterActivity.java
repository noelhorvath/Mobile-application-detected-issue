package hu.mobilalkfejl.clinicalissuelog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Date;
import java.time.LocalDateTime;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 696969;

    EditText emailRegister;
    EditText passwordRegister;
    EditText passwordAgainRegister;
    EditText nameRegister;
    EditText birthDateAgainRegister;
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
        birthDateAgainRegister = findViewById(R.id.editTextPractitionerBirthDate);
        qualificationCodeRegister = findViewById(R.id.editTextPractitionerQualificationCode);
        qualificationIssuerRegister = findViewById(R.id.editTextPractitionerQualificationIssuer);
        activeRegister = findViewById(R.id.radioGroupActive);
        genderRegister = findViewById(R.id.spinnerGender);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void doRegistration(View view) {
        String email = emailRegister.getText().toString();
        String password = emailRegister.getText().toString();
        String passwordAgain = emailRegister.getText().toString();
        String name = nameRegister.getText().toString();
        String birthDate= birthDateAgainRegister.getText().toString();
        String qualificationCode = qualificationCodeRegister.getText().toString();
        String qualificationIssuer = qualificationIssuerRegister.getText().toString();

        int radioButtonId = activeRegister.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(radioButtonId);
        String gender = selectedRadioButton.getText().toString();

        if(email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty() || name.isEmpty()
                || birthDate == null || qualificationCode.isEmpty() || qualificationIssuer.isEmpty() || gender.isEmpty()){

            Toast.makeText(this,"Registration form is incomplete!\n",Toast.LENGTH_LONG).show();
        }else{
            if(!name.matches("^[ \\r\\n\\t\\S]+$")){
                Toast.makeText(this,"Bad name format!\n",Toast.LENGTH_LONG).show();
            }else if(!qualificationCode.matches("^[ \\r\\n\\t\\S]+$")){
                Toast.makeText(this,"Bad qualification code format!\n",Toast.LENGTH_LONG).show();
            }else if(!qualificationIssuer.matches("^[ \\r\\n\\t\\S]+$")){
                Toast.makeText(this,"Bad qualification issuer format!\n",Toast.LENGTH_LONG).show();
            }else{
                if(password.equals(passwordAgain)){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, task ->{
                        if (task.isSuccessful()) {
                            Log.d(LOG_TAG, "Practitioner created successfully");
                            afterRegistration();
                        }else{
                            Log.d(LOG_TAG, "Practitioner creation failed!");
                            Toast.makeText(RegisterActivity.this,"Practitioner creation failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }

    public void afterRegistration(){
        Intent intent = new Intent(this, IssueListActivity.class);
        startActivity(intent);
    }
}