package com.example.testingalz;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleAppointment extends AppCompatActivity {

    private EditText editTextPatientName;
    private Spinner spinnerAppointmentType;
    private EditText editTextContactNumber;
    private EditText editTextEmail;
    private EditText editTextReasonForVisit;
    private Button buttonPickDate;
    private Button buttonPickTime;
    private Button buttonScheduleAppointment;
    private Button buttonGenerateReport;
    private TextView textViewSelectedDate;
    private TextView textViewSelectedTime;

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_SEND_EMAIL = 113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);

        editTextPatientName = findViewById(R.id.editTextPatientName);
        spinnerAppointmentType = findViewById(R.id.spinnerAppointmentType);
        editTextContactNumber = findViewById(R.id.editTextContactNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextReasonForVisit = findViewById(R.id.editTextReasonForVisit);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonPickTime = findViewById(R.id.buttonPickTime);
        buttonScheduleAppointment = findViewById(R.id.buttonScheduleAppointment);
        buttonGenerateReport = findViewById(R.id.buttonGenerateReport);
        textViewSelectedDate = findViewById(R.id.textViewAppointmentDate);
        textViewSelectedTime = findViewById(R.id.textViewAppointmentTime);

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
                String email = editTextEmail.getText().toString();
                String reasonForVisit = editTextReasonForVisit.getText().toString();
                String appointmentDate = textViewSelectedDate.getText().toString();
                String appointmentTime = textViewSelectedTime.getText().toString();

                // Show a Toast as feedback
                Toast.makeText(ScheduleAppointment.this,
                        "Appointment Scheduled for " + appointmentDate + " at " + appointmentTime,
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScheduleAppointment.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ScheduleAppointment.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                } else {
                    generateReport();
                }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
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

    private void generateReport() {
        String patientName = editTextPatientName.getText().toString();
        String appointmentType = spinnerAppointmentType.getSelectedItem().toString();
        String contactNumber = editTextContactNumber.getText().toString();
        String email = editTextEmail.getText().toString();
        String reasonForVisit = editTextReasonForVisit.getText().toString();
        String appointmentDate = textViewSelectedDate.getText().toString();
        String appointmentTime = textViewSelectedTime.getText().toString();

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        android.graphics.Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(16); // Set a font size
        canvas.drawText("Patient Name: " + patientName, 10, 20, paint);
        canvas.drawText("Appointment Type: " + appointmentType, 10, 40, paint);
        canvas.drawText("Contact Number: " + contactNumber, 10, 60, paint);
        canvas.drawText("Reason for Visit: " + reasonForVisit, 10, 80, paint);
        canvas.drawText("Appointment Date: " + appointmentDate, 10, 100, paint);
        canvas.drawText("Appointment Time: " + appointmentTime, 10, 120, paint);

        pdfDocument.finishPage(page);

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "AppointmentReport.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Report generated successfully", Toast.LENGTH_LONG).show();

            // Send the report via email
            sendEmailWithReport(file, email); // Pass email address

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating report", Toast.LENGTH_LONG).show();
        } finally {
            pdfDocument.close();
        }
    }

    private void sendEmailWithReport(File file, String email) {
        String subject = "Appointment Report";
        String body = "Please find your appointment report attached.";

        Uri fileUri;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(this, "com.example.testingalz.fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email}); // Use email address
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(Intent.createChooser(intent, "Send email..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No email client installed", Toast.LENGTH_SHORT).show();
        }
    }
}
