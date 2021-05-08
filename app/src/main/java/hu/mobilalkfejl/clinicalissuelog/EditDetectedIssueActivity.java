package hu.mobilalkfejl.clinicalissuelog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EditDetectedIssueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = CreateDetectedIssueActivity.class.getName();
    final Calendar calendar = Calendar.getInstance();

    EditText editDetectedIssuePatientET;
    EditText editDetectedIssueCodeET;
    EditText editDetectedIssueStatusET;
    EditText editDetectedIssueDetailET;
    Spinner editDetectedIssueSeveritySpinner;
    EditText editDetectedIssueIdentifiedDateET;
    EditText editDetectedIssueIdentifiedTimeET;

    String currentDetectedIssueId;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detected_issue);

        editDetectedIssueSeveritySpinner = findViewById(R.id.editDetectedIssueSeverity);
        editDetectedIssuePatientET = findViewById(R.id.editDetectedIssuePatient);
        editDetectedIssueCodeET = findViewById(R.id.editDetectedIssueCode);
        editDetectedIssueStatusET = findViewById(R.id.editDetectedIssueStatus);
        editDetectedIssueDetailET = findViewById(R.id.editDetectedIssueDetail);
        editDetectedIssueIdentifiedDateET = findViewById(R.id.editDetectedIssueIdentifiedDate);
        editDetectedIssueIdentifiedTimeET= findViewById(R.id.editDetectedIssueIdentifiedTime);

        currentDetectedIssueId = this.getIntent().getCharSequenceExtra("detectedIssueId").toString();

        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));

        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editDetectedIssueIdentifiedDateET.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime()));
        };

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                editDetectedIssueIdentifiedTimeET.setText(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendar.getTime()));
            }
        };

        editDetectedIssueIdentifiedDateET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditDetectedIssueActivity.this, onDateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editDetectedIssueIdentifiedTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(EditDetectedIssueActivity.this, onTimeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
            }
        });

        editDetectedIssueSeveritySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.severity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editDetectedIssueSeveritySpinner.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        initEditTextsWithDetectedIssueData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Thread.sleep(240);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void initEditTextsWithDetectedIssueData(){
        firestore.collection("DetectedIssues").document(currentDetectedIssueId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            DetectedIssue detectedIssue = new DetectedIssue();
            @Override
            public void onSuccess(DocumentSnapshot document) {
                detectedIssue = document.toObject(DetectedIssue.class);
                editDetectedIssuePatientET.setText(detectedIssue.getPatient());
                editDetectedIssueCodeET.setText(detectedIssue.getCode());
                editDetectedIssueStatusET.setText(detectedIssue.getStatus());
                editDetectedIssueDetailET.setText(detectedIssue.getDetail());
                editDetectedIssueSeveritySpinner.setSelection(1);
                String[] severity_list = getBaseContext().getResources().getStringArray(R.array.severity);
                for(int i = 0; i < severity_list.length; i++){
                    if(severity_list[i].equals(detectedIssue.getSeverity())){
                        editDetectedIssueSeveritySpinner.setSelection(i);
                    }
                }
                calendar.setTime(detectedIssue.getIdentifiedDateTime().toDate());
                editDetectedIssueIdentifiedDateET.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime()));
                editDetectedIssueIdentifiedTimeET.setText(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendar.getTime()));
            }
        });
    }

    public void editDetectedIssue(View view) {
        String patient = editDetectedIssuePatientET.getText().toString();
        String code = editDetectedIssueCodeET.getText().toString();
        String status = editDetectedIssueStatusET.getText().toString();
        String detail = editDetectedIssueDetailET.getText().toString();
        String severity = editDetectedIssueSeveritySpinner.getSelectedItem().toString();
        String identifiedDate = editDetectedIssueIdentifiedDateET.getText().toString();
        String identifiedTime = editDetectedIssueIdentifiedTimeET.getText().toString();

        if(patient.isEmpty() || code.isEmpty() || status.isEmpty() || detail.isEmpty() || severity.isEmpty() || identifiedDate.isEmpty() || identifiedTime.isEmpty()){
            Toast.makeText(this,"Issue form has empty empty field(s)!",Toast.LENGTH_LONG).show();
        }else{
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(identifiedDate),LocalTime.parse(identifiedTime));
            DetectedIssue detectedIssue = new DetectedIssue();
            detectedIssue.setSeverity(severity);
            detectedIssue.setDetail(detail);
            detectedIssue.setCode(code);
            detectedIssue.setPatient(patient);
            detectedIssue.setStatus(status);
            detectedIssue.setIdentifiedDateTime(new Timestamp(Date.from(dateTime.toInstant(OffsetDateTime.now().getOffset()))));
            new UpdateDetectedIssueAsyncTask().execute(detectedIssue);
            backToDetectedIssuesListActivity();

            Toast.makeText(this,"Detected issue has been successfully updated!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backToDetectedIssuesListActivity(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void cancel(View view) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}