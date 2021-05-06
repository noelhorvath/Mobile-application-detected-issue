package hu.mobilalkfejl.clinicalissuelog;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteDetectedIssueAsyncTask extends AsyncTask<String,Void,Void> {

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected Void doInBackground(String... strings) {
        firestore.collection("DetectedIssues").document(strings[0]).delete();
        return null;
    }
}
