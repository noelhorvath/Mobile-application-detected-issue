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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DetectedIssueListActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetectedIssueListActivity.class.getName();

    private static String currentPractitonerEmail;


    private RecyclerView recyclerView;
    private DetectedIssueAdapter detectedIssueAdapter;
    private int gridNumber = 1;

    TextView emptyIssueList;
    ArrayList<DetectedIssue> detectedIssueList;

    FirebaseFirestore firestore;
    CollectionReference detectedIssuesCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected_issue_list);

        currentPractitonerEmail = this.getIntent().getExtras().get("currentPractitionerEmail").toString();

        detectedIssueList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();
        detectedIssuesCollection = firestore.collection("DetectedIssues");

        recyclerView = findViewById(R.id.detectedIssuesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,gridNumber));

        initializeDetectedIssuesForAdapter();

    }

    public void initializeDetectedIssuesForAdapter(){
        detectedIssuesCollection.orderBy("code").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    detectedIssueList.add(document.toObject(DetectedIssue.class));
                }
                detectedIssueAdapter = new DetectedIssueAdapter(DetectedIssueListActivity.this,detectedIssueList);
                recyclerView.setAdapter(detectedIssueAdapter);
                detectedIssueAdapter.notifyDataSetChanged();

                emptyIssueList = findViewById(R.id.detectedIssuesTitleText);

                if(detectedIssueList == null || detectedIssueList.isEmpty()){
                    String title = "You don't have any registered issues!";
                    emptyIssueList.setText(title);
                }else{
                    String title = "Current number of issues: " + detectedIssueAdapter.getItemCount();
                    emptyIssueList.setText(title);
                }
            }
        });
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