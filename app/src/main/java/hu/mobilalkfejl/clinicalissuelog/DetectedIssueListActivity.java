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

public class DetectedIssueListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<DetectedIssue>> {
    private static final String LOG_TAG = DetectedIssueListActivity.class.getName();

    private FirebaseFirestore firestore;
    private static String currentPractitonerEmail;
    private ArrayList<DetectedIssue> detectedIssues;

    private ArrayList<DetectedIssue> detectedIssueList;
    private RecyclerView recyclerView;
    private DetectedIssueAdapter detectedIssueAdapter;
    private int gridNumber = 1;

    TextView emptyIssueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected_issue_list);

        firestore = FirebaseFirestore.getInstance();

        currentPractitonerEmail = this.getIntent().getExtras().get("currentPractitionerEmail").toString();

        Bundle bundle = new Bundle();
        bundle.putString("email",currentPractitonerEmail);
        getSupportLoaderManager().restartLoader(0, bundle, this);
        Log.d(LOG_TAG,"OnCreate");

        if(detectedIssues == null){
            emptyIssueList = findViewById(R.id.emptyIssuesText);
            CharSequence charSequence = "You don't have any registered issues!";
            emptyIssueList.setText(charSequence);
        }else{
            recyclerView = findViewById(R.id.detectedIssuesRecyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this,gridNumber));
            detectedIssueAdapter = new DetectedIssueAdapter(this,detectedIssueList);
            recyclerView.setAdapter(detectedIssueAdapter);
        }


        initializeDate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"OnStart");
    }

    public void initializeDate(){

        //detectedIssueAdapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Loader<ArrayList<DetectedIssue>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(LOG_TAG,"Task created");
        return new DetectedIssueLoaderAsyncTask(this,args.getString("email"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<DetectedIssue>> loader, ArrayList<DetectedIssue> data) {
        if(data.isEmpty()){
            Log.d(LOG_TAG,"List is empty");
        }else{
            detectedIssues = data;
            for(DetectedIssue detectedIssue : data){
                Log.d(LOG_TAG,"Issue: " + detectedIssue.getPatient());
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<DetectedIssue>> loader) {

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