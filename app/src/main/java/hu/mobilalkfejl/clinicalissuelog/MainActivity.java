package hu.mobilalkfejl.clinicalissuelog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int SECRET_KEY = 268983;

    EditText emailET;
    EditText passwordET;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view){
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    afterLogin(email, view);
                }else{
                    Toast.makeText(MainActivity.this,"Practitioner login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{
            if(email.isEmpty() && password.isEmpty()){
                Toast.makeText(MainActivity.this,"Empty email and password fields!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(MainActivity.this,"Missing email or password for login!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void afterLogin(String email, View view) {
        Intent intent = new Intent(this, DetectedIssueListActivity.class);
        intent.putExtra("currentPractitionerEmail",email);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view,0,0,view.getWidth(), view.getHeight());
        startActivity(intent, options.toBundle());
    }
}