package com.example.testingalz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleAppointment extends AppCompatActivity {

    private EditText editTextPatientName;
    private Spinner spinnerAppointmentType;
    private EditText editTextContactNumber;
    private EditText editTextReasonForVisit;
    private Button buttonPickDate;
    private Button buttonPickTime;
    private Button buttonScheduleAppointment;
    private TextView textViewSelectedDate;
    private TextView textViewSelectedTime;

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);

        editTextPatientName = findViewById(R.id.editTextPatientName);
        spinnerAppointmentType = findViewById(R.id.spinnerAppointmentType);
        editTextContactNumber = findViewById(R.id.editTextContactNumber);
        editTextReasonForVisit = findViewById(R.id.editTextReasonForVisit);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonPickTime = findViewById(R.id.buttonPickTime);
        buttonScheduleAppointment = findViewById(R.id.buttonScheduleAppointment);
        textViewSelectedDate = findViewById(R.id.textViewAppointmentDate);
        textViewSelectedTime = findViewById(R.id.textViewAppointmentTime);

        // Populate Spinner with Appointment Types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.appointment_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAppointmentType.setAdapter(adapter);

        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ScheduleAppointment.this, dateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ScheduleAppointment.this, timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        buttonScheduleAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientName = editTextPatientName.getText().toString();
                String appointmentType = spinnerAppointmentType.getSelectedItem().toString();
                String contactNumber = editTextContactNumber.getText().toString();
                String reasonForVisit = editTextReasonForVisit.getText().toString();
                String appointmentDate = textViewSelectedDate.getText().toString();
                String appointmentTime = textViewSelectedTime.getText().toString();

                // Show a Toast as feedback
                Toast.makeText(ScheduleAppointment.this,
                        "Appointment Scheduled for " + appointmentDate + " at " + appointmentTime,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(year, month, dayOfMonth);
            textViewSelectedDate.setText(dateFormat.format(calendar.getTime()));
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            textViewSelectedTime.setText(timeFormat.format(calendar.getTime()));
        }
    };
}
