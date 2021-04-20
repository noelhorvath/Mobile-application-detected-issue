package hu.mobilalkfejl.clinicalissuelog;

import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DetectedIssueAdapter extends RecyclerView.Adapter<DetectedIssueAdapter.ViewHolder> implements Filterable {
    private ArrayList<DetectedIssue> detectedIssueData;
    private ArrayList<DetectedIssue> detectedIssueDataAll;
    private Context context;

    DetectedIssueAdapter(Context context, ArrayList<DetectedIssue> issuesData){
        this.context = context;
        detectedIssueData = issuesData;
        detectedIssueDataAll = issuesData;
    }

    @NonNull
    @Override
    public DetectedIssueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.detected_issue_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetectedIssueAdapter.ViewHolder holder, int position) {
        DetectedIssue currentDetectedIssue = detectedIssueData.get(position);
        holder.bindTo(currentDetectedIssue);
    }

    @Override
    public int getItemCount() {
        return detectedIssueData.size();
    }

    @Override
    public Filter getFilter() {
        return detectedIssueFilter;
    }

    private Filter detectedIssueFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<DetectedIssue> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                results.count = detectedIssueDataAll.size();
                results.values = detectedIssueDataAll;
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(DetectedIssue issue : detectedIssueDataAll){
                    if(issue.getPatient().toLowerCase().contains(filterPattern)){
                        filteredList.add(issue);
                    }
                    results.count = filteredList.size();
                    results.values = filteredList;
                }
            }

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            detectedIssueData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView patientNameText;
        private TextView codeText;
        private TextView statusText;
        private TextView severityText;
        private TextView identifiedDateTimeText;

        public ViewHolder(View itemView) {
            super(itemView);

            patientNameText = itemView.findViewById(R.id.textIssuePatientName);
            codeText = itemView.findViewById(R.id.textIssueCode);
            statusText = itemView.findViewById(R.id.textIssueStatus);
            severityText = itemView.findViewById(R.id.textIssueSeverity);
            identifiedDateTimeText = itemView.findViewById(R.id.textIssueIdentifiedDateTime);
        }

        public void bindTo(DetectedIssue currentDetectedIssue){
            patientNameText.setText(currentDetectedIssue.getPatient());
            codeText.setText(currentDetectedIssue.getCode());
            statusText.setText(currentDetectedIssue.getStatus());
            severityText.setText(currentDetectedIssue.getSeverity());
            identifiedDateTimeText.setText(currentDetectedIssue.getIdentifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-ddThh:mm:ss+zz:zz")));
        }
    };

}