package hu.mobilalkfejl.clinicalissuelog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

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
                    Log.d(LOG_TAG, "Practitioner logged in successfully!");
                    afterLogin(email);
                }else{
                    Log.d(LOG_TAG, "Practitioner login failed!");
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

    public void afterLogin(String email){
        Intent intent = new Intent(this,IssueListActivity.class);
        intent.putExtra("currentPractitionerEmail",email);
        startActivity(intent);
    }
}