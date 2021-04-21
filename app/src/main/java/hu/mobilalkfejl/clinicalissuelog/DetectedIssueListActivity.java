package hu.mobilalkfejl.clinicalissuelog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DetectedIssueListActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetectedIssueListActivity.class.getName();

    private static String currentPractitonerEmail;

    private ArrayList<DetectedIssue> detectedIssueList;
    private RecyclerView recyclerView;
    private DetectedIssueAdapter detectedIssueAdapter;
    private int gridNumber = 1;

    TextView emptyIssueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected_issue_list);

        currentPractitonerEmail = this.getIntent().getExtras().get("currentPractitionerEmail").toString();

        try {
            detectedIssueList = new LoadDetectedIssueAsyncTask().execute(currentPractitonerEmail).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG,"OnCreate");

        if(detectedIssueList == null){
            emptyIssueList = findViewById(R.id.emptyIssuesText);
            CharSequence charSequence = "You don't have any registered issues!\n" +
                    "You can add a new issue in the top left corner!";
            emptyIssueList.setText(charSequence);
        }else{
            recyclerView = findViewById(R.id.detectedIssuesRecyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this,gridNumber));
            detectedIssueAdapter = new DetectedIssueAdapter(this,detectedIssueList);
            recyclerView.setAdapter(detectedIssueAdapter);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"OnStart");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detected_issue_list_menu,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;

            case R.id.createDetectedIssue:
                Intent intent = new Intent(this,CreateDetectedIssueActivity.class);
                intent.putExtra("currentPractitionerEmail",currentPractitonerEmail);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}