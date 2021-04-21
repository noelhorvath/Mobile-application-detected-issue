package hu.mobilalkfejl.clinicalissuelog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;

public class CreateDetectedIssueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = CreateDetectedIssueActivity.class.getName();
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    final Calendar calendar = Calendar.getInstance();

    String currentPractitionerEmail;
    EditText createDetectedIssuePatientET;
    EditText createDetectedIssueCodeET;
    EditText createDetectedIssueStatusET;
    EditText createDetectedIssueDetailET;
    Spinner createDetectedIssueSeveritySpinner;
    EditText createDetectedIssueIdentifiedDateET;
    EditText createDetectedIssueIdentifiedTimeET;

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
                createDetectedIssueIdentifiedTimeET.setText(new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH).format(calendar.getTime()));
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
    }

    public void addDetectedIssue(View view) {
        String patient = createDetectedIssuePatientET.getText().toString();
        String code = createDetectedIssueCodeET.getText().toString();
        String status = createDetectedIssueStatusET.getText().toString();
        String detail = createDetectedIssueDetailET.getText().toString();
        String severity = createDetectedIssueSeveritySpinner.getSelectedItem().toString();
        String identifiedDate = createDetectedIssueIdentifiedDateET.toString();
        String identifiedTime = createDetectedIssueIdentifiedTimeET.toString();

        if(patient.isEmpty() || code.isEmpty() || status.isEmpty() || detail.isEmpty() || severity.isEmpty() || identifiedDate.isEmpty() || identifiedTime.isEmpty()){
            Toast.makeText(this,"Issue form is incomplete!",Toast.LENGTH_LONG).show();
        }else{
            LocalDate date = LocalDate.parse(identifiedDate);
            LocalTime time = LocalTime.parse(identifiedTime);
            LocalDateTime identifiedDateTime = LocalDateTime.of(date, time);

            Log.d(LOG_TAG,"datetime: "+ identifiedDateTime.toString());

            DetectedIssue detectedIssue = new DetectedIssue();
            detectedIssue.setSeverity(severity);
            detectedIssue.setDetail(detail);
            detectedIssue.setCode(code);
            detectedIssue.setPatient(patient);
            detectedIssue.setIdentifiedDateTime(identifiedDateTime);
            detectedIssue.setStatus(status);
            new AddDetectedIssueAsyncTask(currentPractitionerEmail).execute();
        }
    }

    private void updateLabelDate(String format, EditText editText, String mode) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        editText.setText(simpleDateFormat.format(calendar.getTime()));
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