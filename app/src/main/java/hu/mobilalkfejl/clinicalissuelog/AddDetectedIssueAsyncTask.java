package hu.mobilalkfejl.clinicalissuelog;

import android.os.AsyncTask;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddDetectedIssueAsyncTask extends AsyncTask<DetectedIssue, Void, Void> {

    FirebaseFirestore firestore;
    String currentPractitionerEmail;

    public AddDetectedIssueAsyncTask(String currentPractitionerEmail){
        firestore = FirebaseFirestore.getInstance();
        this.currentPractitionerEmail = currentPractitionerEmail;
    }

    @Override
    protected Void doInBackground(DetectedIssue... detectedIssues) {
        try{
            DocumentSnapshot documentSnapshot = firestore.collection("Practitioners").document(currentPractitionerEmail).get().getResult();
            detectedIssues[0].setAuthor(documentSnapshot.get("name").toString());
            firestore.collection("DetectedIssues").add(detectedIssues[0]);
            Thread.sleep(100);
        }catch (InterruptedException e){
            e.getStackTrace();
        }

        return null;
    }
}
