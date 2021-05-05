package hu.mobilalkfejl.clinicalissuelog;

import androidx.appcompat.app.AppCompatActivity;

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


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateDetectedIssueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = CreateDetectedIssueActivity.class.getName();
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    final Calendar calendar = Calendar.getInstance();

    String currentPractitionerEmail;
    EditText createDetectedIssuePatientET;
    EditText createDetectedIssueCodeET;
    EditText createDetectedIssueStatusET;
    EditText createDetectedIssueDetailET;
    Spinner createDetectedIssueSeveritySpinner;
    EditText createDetectedIssueIdentifiedDateET;
    EditText createDetectedIssueIdentifiedTimeET;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_detected_issue);

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

            DetectedIssue detectedIssue = new DetectedIssue();
            detectedIssue.setSeverity(severity);
            detectedIssue.setDetail(detail);
            detectedIssue.setCode(code);
            detectedIssue.setPatient(patient);
            detectedIssue.setStatus(status);
            detectedIssue.setIdentifiedDateTime(new Timestamp(Date.from(dateTime.toInstant(ZoneOffset.UTC))));

            new CreateDetectedIssueAsyncTask(firestore).execute(detectedIssue);
            backToDetectedIssuesListActivity();
        }
    }

    public void backToDetectedIssuesListActivity(){
        Intent intent = new Intent(this,DetectedIssueListActivity.class);
        startActivity(intent);
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