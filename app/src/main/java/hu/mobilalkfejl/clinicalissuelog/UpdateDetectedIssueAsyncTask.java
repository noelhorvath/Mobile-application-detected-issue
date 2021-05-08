package hu.mobilalkfejl.clinicalissuelog;

import android.os.AsyncTask;

import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateDetectedIssueAsyncTask extends AsyncTask<DetectedIssue,Void,Void> {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected Void doInBackground(DetectedIssue... detectedIssues) {
        firestore.collection("DetectedIssues").document(detectedIssues[0]
                ._getId())
                .update("status",detectedIssues[0].getStatus(),
                        "severity",detectedIssues[0].getSeverity(),
                        "identifiedDateTime",detectedIssues[0].getIdentifiedDateTime(),
                        "code",detectedIssues[0].getCode(),
                        "detail",detectedIssues[0].getDetail(),
                        "patient",detectedIssues[0].getPatient());
        return null;
    }
}
