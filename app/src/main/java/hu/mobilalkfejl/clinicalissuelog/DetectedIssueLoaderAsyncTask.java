package hu.mobilalkfejl.clinicalissuelog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DetectedIssueLoaderAsyncTask extends AsyncTaskLoader<ArrayList<DetectedIssue>> {
    private ArrayList<DetectedIssue> detectedIssues;
    private CollectionReference collectionReferenceDetectedIssues;
    private String practitioner;

    public DetectedIssueLoaderAsyncTask(@NonNull Context context,CollectionReference detectedIssues,String currentParctitionerName) {
        super(context);
        this.collectionReferenceDetectedIssues = detectedIssues;
        this.practitioner = currentParctitionerName;
        this.detectedIssues = new ArrayList<>();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<DetectedIssue> loadInBackground() {
        collectionReferenceDetectedIssues.whereEqualTo("author",practitioner).orderBy("identifiedDateTime").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                DetectedIssue detectedIssue = document.toObject(DetectedIssue.class);
                detectedIssues.add(detectedIssue);
            }
        });
        return detectedIssues;
    }
}
