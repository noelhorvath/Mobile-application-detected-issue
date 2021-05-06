package hu.mobilalkfejl.clinicalissuelog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ViewDetectedIssueActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String detectedIssueId;

    TextView viewDetectedIssuePatientTW;
    TextView viewDetectedIssueCodeTW;
    TextView viewDetectedIssueStatusTW;
    TextView viewDetectedIssueDetailTW;
    TextView viewDetectedIssueSeverityTW;
    TextView viewDetectedIssueIdentifiedDateTimeTW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detected_issue);
        detectedIssueId = this.getIntent().getCharSequenceExtra("detectedIssueId").toString();

        firestore = FirebaseFirestore.getInstance();

        viewDetectedIssuePatientTW = this.findViewById(R.id.viewDetectedIssuePatient);
        viewDetectedIssueCodeTW = this.findViewById(R.id.viewDetectedIssueCode);
        viewDetectedIssueStatusTW = this.findViewById(R.id.viewDetectedIssueStatus);
        viewDetectedIssueDetailTW = this.findViewById(R.id.viewDetectedIssueDetail);
        viewDetectedIssueSeverityTW = this.findViewById(R.id.viewDetectedIssueSeverity);
        viewDetectedIssueIdentifiedDateTimeTW = this.findViewById(R.id.viewDetectedIssueIdentifiedDateTime);

        initText(detectedIssueId);

    }

    private void initText(String id){
        firestore.collection("DetectedIssues").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            DetectedIssue detectedIssue = new DetectedIssue();
            @Override
            public void onSuccess(DocumentSnapshot document) {
                detectedIssue = document.toObject(DetectedIssue.class);
                viewDetectedIssuePatientTW.setText(detectedIssue.getPatient());
                viewDetectedIssueCodeTW.setText(detectedIssue.getCode());
                viewDetectedIssueStatusTW.setText(detectedIssue.getStatus());
                viewDetectedIssueDetailTW.setText(detectedIssue.getDetail());
                viewDetectedIssueSeverityTW.setText(detectedIssue.getSeverity());
                viewDetectedIssueIdentifiedDateTimeTW.setText(detectedIssue.getIdentifiedDateTime().toDate().toInstant().toString().replace("Z"," ").replace("T", " "));

            }
        });

        try{
            Thread.sleep(240);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void editDetectedIssue(View view) {
    }

    public void deleteDetectedIssue(View view) {
        new DeleteDetectedIssueAsyncTask().execute(detectedIssueId);
        Toast.makeText(this,"Detected issue has been successfully deleted!",Toast.LENGTH_LONG).show();
        finish();
    }

    public void goBackToListActivity(View view) {
        finish();
    }
}