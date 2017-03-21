package galodamadrugada.onhere;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EventRegisterActivity extends AppCompatActivity implements Button.OnClickListener{

    private Calendar calendar;
    private int year, month, day;
    private int hour, minute;
    private EditText editTextEventName, editTextDelay, editTextDescription;
    private TextView textViewHour, textViewDate;
    private Button buttonHour, buttonDate, buttonRegisterEvent;
    static final int DATE_DIALOG_ID = 0;
    static final int HOUR_DIALOG_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);

        editTextEventName = (EditText) findViewById(R.id.editTextEventName);
        editTextDelay = (EditText) findViewById(R.id.editTextDelay);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener((View.OnClickListener) this);

        buttonHour = (Button) findViewById(R.id.buttonHour);
        buttonHour.setOnClickListener((View.OnClickListener) this);

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewHour = (TextView) findViewById(R.id.textViewHour);

        buttonRegisterEvent = (Button) findViewById(R.id.buttonRegisterEvent);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        showDate(year, month+1, day);
        showTime(hour, minute);

        buttonRegisterEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEventName.getText().toString().equals("")) {
                    editTextEventName.requestFocus();
                    editTextEventName.setError(getResources().getString(R.string.required_field));
                }
                else if (editTextDelay.getText().toString().equals("")) {
                    editTextDelay.requestFocus();
                    editTextDelay.setError(getResources().getString(R.string.required_field));
                }
                else if (editTextDescription.getText().toString().equals("")) {
                    editTextDescription.requestFocus();
                    editTextDescription.setError(getResources().getString(R.string.required_field));
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventRegisterActivity.this);
                    LayoutInflater inflater = EventRegisterActivity.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.dialog_event_register_success, null))
                            .setTitle(R.string.event_register_success_title)
                            .setPositiveButton(R.string.go_to_profile, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent goToProfile = new Intent(EventRegisterActivity.this, ProfileActivity.class);
                                    startActivity(goToProfile);
                                    finish();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, mDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return datePickerDialog;
            case HOUR_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, hour, minute, DateFormat.is24HourFormat(this));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int day) {
            showDate(year, month+1, day);
        }
    };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showTime(hourOfDay, minute);
        }
    };

   private void showDate(int year, int month, int day) {
        textViewDate.setText(String.format("%02d/%02d/%04d", day, month, year));
    }

    private void showTime(int hour, int minute) {
        textViewHour.setText(String.format("%02d:%02dh", hour, minute));
    }

   @Override
    public void onClick(View v) {
       if (v == buttonDate)
            showDialog(DATE_DIALOG_ID);
       if (v == buttonHour)
            showDialog(HOUR_DIALOG_ID);
    }
}
