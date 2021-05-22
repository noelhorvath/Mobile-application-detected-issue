package hu.mobilalkfejl.clinicalissuelog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ViewDetectedIssueActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String detectedIssueId;
    private boolean reloadNeeded;

    TextView viewDetectedIssuePatientTW;
    TextView viewDetectedIssueCodeTW;
    TextView viewDetectedIssueStatusTW;
    TextView viewDetectedIssueDetailTW;
    TextView viewDetectedIssueSeverityTW;
    TextView viewDetectedIssueIdentifiedDateTimeTW;
    NotificationHandler notificationHandler;

    private static final int INTENT_CODE = 2;
    private static final int SECRET_KEY = 456842;
    private static final int SECRET_KEY_EDIT = 938613;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detected_issue);

        if(!this.getIntent().getExtras().get("SECRET_KEY").toString().equals(Integer.toString(SECRET_KEY))){
            this.finish();
        }

        detectedIssueId = this.getIntent().getCharSequenceExtra("detectedIssueId").toString();

        notificationHandler = new NotificationHandler(this);

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
                Instant instant = detectedIssue.getIdentifiedDateTime().toDate().toInstant();
                viewDetectedIssueIdentifiedDateTimeTW.setText(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toString().replace("T"," "));

            }
        });
    }

    public void editDetectedIssue(View view) {
        Intent intent = new Intent(this,EditDetectedIssueActivity.class);
        intent.putExtra("detectedIssueId",detectedIssueId);
        intent.putExtra("SECRET_KEY", SECRET_KEY_EDIT);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(this,R.anim.slide_up,0);
        this.startActivityForResult(intent,INTENT_CODE,options.toBundle());
    }

    public void deleteDetectedIssue(View view) {
        firestore.collection("DetectedIssues").document(detectedIssueId).delete();
        notificationHandler.sendAfterDelete("A detected issue has been successfully deleted!");
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                reloadNeeded = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(reloadNeeded){
            reloadNeeded = false;
            initText(detectedIssueId);
        }
    }

    public void goBackToListActivity(View view) {
        finish();
    }
}