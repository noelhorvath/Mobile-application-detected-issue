package hu.mobilalkfejl.clinicalissuelog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DetectedIssueListActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetectedIssueListActivity.class.getName();

    private FirebaseFirestore mFirestore;
    private CollectionReference mPractitioners;
    private static String CurrentPractitonerEmail;

    private RecyclerView mRecyclerView;
    private ArrayList<DetectedIssue> mIssueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected_issue_list);

        mFirestore = FirebaseFirestore.getInstance();
        mPractitioners = mFirestore.collection("practitioners");

        CurrentPractitonerEmail = this.getIntent().getExtras().get("currentPractitionerEmail").toString();
    }

}