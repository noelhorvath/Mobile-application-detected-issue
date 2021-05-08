package hu.mobilalkfejl.clinicalissuelog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class DetectedIssueListActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetectedIssueListActivity.class.getName();

    private static String currentPractitionerEmail;


    private RecyclerView recyclerView;
    private DetectedIssueAdapter detectedIssueAdapter;
    private int gridNumber = 1;
    private static final int CREATE_ACTIVITY_CODE = 1;
    private boolean reloadNeeded;

    TextView emptyIssueList;
    ArrayList<DetectedIssue> detectedIssueList;

    FirebaseFirestore firestore;
    CollectionReference detectedIssuesCollection;

    NotificationHandler notificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected_issue_list);

        notificationHandler = new NotificationHandler(this);

        firestore = FirebaseFirestore.getInstance();
        detectedIssuesCollection = firestore.collection("DetectedIssues");

        currentPractitionerEmail = this.getIntent().getExtras().get("currentPractitionerEmail").toString();

        detectedIssueList = new ArrayList<>();

        detectedIssueAdapter = new DetectedIssueAdapter(DetectedIssueListActivity.this,detectedIssueList);
        recyclerView = findViewById(R.id.detectedIssuesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,gridNumber));
        recyclerView.setAdapter(detectedIssueAdapter);

        emptyIssueList = findViewById(R.id.detectedIssuesTitleText);

        initializeDetectedIssuesForAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            Thread.sleep(120);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void initializeDetectedIssuesForAdapter(){
        firestore.collection("Practitioners").whereEqualTo("id",currentPractitionerEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Practitioner practitioner = queryDocumentSnapshots.getDocuments().get(0).toObject(Practitioner.class);
                firestore.collection("DetectedIssues").whereEqualTo("author",practitioner).orderBy("identifiedDateTime", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            DetectedIssue detectedIssue = document.toObject(DetectedIssue.class);
                            detectedIssue.setId(document.getId());
                            detectedIssueList.add(detectedIssue);
                        }
                        detectedIssueAdapter.notifyDataSetChanged();
                        setDetectedIssueCounter();
                    }
                });
            }
        });
    }

    public void setDetectedIssueCounter(){
        String title;
        if(detectedIssueAdapter.getItemCount() == 0){
            title = "You don't have any registered issues!";
            emptyIssueList.setText(title);
        }else{
            title = "Current number of issues: " + detectedIssueAdapter.getItemCount();
        }
        emptyIssueList.setText(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detected_issue_list_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchValue) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchValue) {
                Filter.FilterListener filterListener = new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        String text = "Current number of issues: " + count;
                        emptyIssueList.setText(text);
                    }
                };
                detectedIssueAdapter.getFilter().filter(searchValue,filterListener);
                return false;
            }

        });

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
                intent.putExtra("currentPractitionerEmail",currentPractitionerEmail);
                startActivityForResult(intent, CREATE_ACTIVITY_CODE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                reloadNeeded = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(reloadNeeded){
            Log.d(LOG_TAG, String.valueOf(detectedIssueList.isEmpty()));
            detectedIssueList.clear();
            initializeDetectedIssuesForAdapter();
            reloadNeeded = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationHandler.cancelCreateNotification();
        notificationHandler.cancelDeleteNotification();
        notificationHandler.cancelUpdateNotification();
    }
}