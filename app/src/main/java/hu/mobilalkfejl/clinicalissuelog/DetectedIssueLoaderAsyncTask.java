package hu.mobilalkfejl.clinicalissuelog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DetectedIssueLoaderAsyncTask extends AsyncTaskLoader<ArrayList<DetectedIssue>> {
    private ArrayList<DetectedIssue> detectedIssues;
    private String currentPractitionerEmail;
    private FirebaseFirestore firestore;

    public DetectedIssueLoaderAsyncTask(@NonNull Context context,String currentPractitionerEmail) {
        super(context);
        this.currentPractitionerEmail = currentPractitionerEmail;
        this.detectedIssues = new ArrayList<>();
        this.firestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<DetectedIssue> loadInBackground() {

        try {
            firestore.collection("Practitioners").document(currentPractitionerEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    firestore.collection("DetectedIssues").whereEqualTo("author", documentSnapshot.getString("name")).orderBy("identifiedDateTime").get().addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            DetectedIssue detectedIssue = document.toObject(DetectedIssue.class);
                            detectedIssues.add(detectedIssue);
                        }
                    });
                }
            });
            Thread.sleep(300);
        }catch (InterruptedException e){
            e.getStackTrace();
        }
        return detectedIssues;
    }
}
