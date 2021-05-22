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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.sql.Time;
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

public class CreateDetectedIssueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = CreateDetectedIssueActivity.class.getName();
    final Calendar calendar = Calendar.getInstance();
    private static final int SECRET_KEY = 382637;

    String currentPractitionerEmail;
    EditText createDetectedIssuePatientET;
    EditText createDetectedIssueCodeET;
    EditText createDetectedIssueStatusET;
    EditText createDetectedIssueDetailET;
    Spinner createDetectedIssueSeveritySpinner;
    EditText createDetectedIssueIdentifiedDateET;
    EditText createDetectedIssueIdentifiedTimeET;

    NotificationHandler notificationHandler;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_detected_issue);

        if(!this.getIntent().getExtras().get("SECRET_KEY").toString().equals(Integer.toString(SECRET_KEY))){
            this.finish();
        }

        notificationHandler = new NotificationHandler(this);

        createDetectedIssueSeveritySpinner = findViewById(R.id.createDetectedIssueSeverity);
        createDetectedIssuePatientET = findViewById(R.id.createDetectedIssuePatient);
        createDetectedIssueCodeET = findViewById(R.id.createDetectedIssueCode);
        createDetectedIssueStatusET = findViewById(R.id.createDetectedIssueStatus);
        createDetectedIssueDetailET = findViewById(R.id.createDetectedIssueDetail);
        createDetectedIssueIdentifiedDateET = findViewById(R.id.createDetectedIssueIdentifiedDate);
        createDetectedIssueIdentifiedTimeET= findViewById(R.id.createDetectedIssueIdentifiedTime);

        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            createDetectedIssueIdentifiedDateET.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime()));
        };

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                createDetectedIssueIdentifiedTimeET.setText(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendar.getTime()));
            }
        };

        createDetectedIssueIdentifiedDateET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateDetectedIssueActivity.this, onDateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        createDetectedIssueIdentifiedTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(CreateDetectedIssueActivity.this, onTimeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
            }
        });

        currentPractitionerEmail = this.getIntent().getStringExtra("currentPractitionerEmail");

        createDetectedIssueSeveritySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.severity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        createDetectedIssueSeveritySpinner.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        Log.d(LOG_TAG,"onCreate");
    }

    public void createDetectedIssue(View view) {
        String patient = createDetectedIssuePatientET.getText().toString();
        String code = createDetectedIssueCodeET.getText().toString();
        String status = createDetectedIssueStatusET.getText().toString();
        String detail = createDetectedIssueDetailET.getText().toString();
        String severity = createDetectedIssueSeveritySpinner.getSelectedItem().toString();
        String identifiedDate = createDetectedIssueIdentifiedDateET.getText().toString();
        String identifiedTime = createDetectedIssueIdentifiedTimeET.getText().toString();

        if(patient.isEmpty() || code.isEmpty() || status.isEmpty() || detail.isEmpty() || severity.isEmpty() || identifiedDate.isEmpty() || identifiedTime.isEmpty()){
            Toast.makeText(this,"Issue form is incomplete!",Toast.LENGTH_LONG).show();
        }else{
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(identifiedDate),LocalTime.parse(identifiedTime));

            firestore.collection("Practitioners").whereEqualTo("id",currentPractitionerEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    DetectedIssue detectedIssue = new DetectedIssue();
                    detectedIssue.setSeverity(severity);
                    detectedIssue.setDetail(detail);
                    detectedIssue.setCode(code);
                    detectedIssue.setPatient(patient);
                    detectedIssue.setStatus(status);
                    detectedIssue.setIdentifiedDateTime(new Timestamp(Date.from(dateTime.toInstant(OffsetDateTime.now().getOffset()))));
                    detectedIssue.setAuthor(queryDocumentSnapshots.getDocuments().get(0).toObject(Practitioner.class));
                    firestore.collection("DetectedIssues").add(detectedIssue);

                    notificationHandler.sendAfterCreate("New detected issue for " + detectedIssue.getPatient() + " has been successfully created!");

                    backToDetectedIssuesListActivity();
                }
            });
            Toast.makeText(this,"New detected issue has been created!", Toast.LENGTH_SHORT).show();
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