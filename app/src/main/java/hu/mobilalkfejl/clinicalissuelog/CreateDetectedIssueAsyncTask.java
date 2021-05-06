package hu.mobilalkfejl.clinicalissuelog;

import android.os.AsyncTask;

import com.google.firebase.firestore.FirebaseFirestore;

public class CreateDetectedIssueAsyncTask extends AsyncTask<DetectedIssue,Void,Void> {

    @Override
    protected Void doInBackground(DetectedIssue... detectedIssues) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("DetectedIssues").add(detectedIssues[0]);
        return null;
    }
}
