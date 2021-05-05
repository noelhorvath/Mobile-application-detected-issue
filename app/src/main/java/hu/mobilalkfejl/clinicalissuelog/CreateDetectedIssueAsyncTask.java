package hu.mobilalkfejl.clinicalissuelog;

import android.os.AsyncTask;

import com.google.firebase.firestore.FirebaseFirestore;

public class CreateDetectedIssueAsyncTask extends AsyncTask<DetectedIssue,Void,Void> {

    private FirebaseFirestore firestore;

    public CreateDetectedIssueAsyncTask(FirebaseFirestore firestore){
        this.firestore = firestore;
    }

    @Override
    protected Void doInBackground(DetectedIssue... detectedIssues) {
        firestore.collection("DetectedIssues").add(detectedIssues[0]);
        return null;
    }
}
