package hu.mobilalkfejl.clinicalissuelog;

import android.os.AsyncTask;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddDetectedIssueAsyncTask extends AsyncTask<DetectedIssue, Void, Void> {

    FirebaseFirestore firestore;

    public AddDetectedIssueAsyncTask(){
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected Void doInBackground(DetectedIssue... detectedIssues) {
        try{
            firestore.collection("DetectedIssues").add(detectedIssues[0]);
            Thread.sleep(100);
        }catch (InterruptedException e){
            e.getStackTrace();
        }

        return null;
    }
}
