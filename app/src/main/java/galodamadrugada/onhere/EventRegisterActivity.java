package galodamadrugada.onhere;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EventRegisterActivity extends AppCompatActivity implements Button.OnClickListener{

    private Calendar calendar;
    private int year, month, day;
    private int hour, minute;
    private TextView textViewHour, textViewDate;
    private Button buttonHour;
    private Button buttonDate;
    static final int DATE_DIALOG_ID = 0;
    static final int HOUR_DIALOG_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);

        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener((View.OnClickListener) this);

        buttonHour = (Button) findViewById(R.id.buttonHour);
        buttonHour.setOnClickListener((View.OnClickListener) this);

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewHour = (TextView) findViewById(R.id.textViewHour);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        showDate(year, month+1, day);
        showTime(hour, minute);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, year, month, day);
            case HOUR_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, hour, minute, DateFormat.is24HourFormat(this));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String data = String.valueOf(dayOfMonth) + " /" + String.valueOf(monthOfYear+1) + " /" + String.valueOf(year);
            Toast.makeText(EventRegisterActivity.this, "DATA = " + data, Toast.LENGTH_SHORT).show();
            showDate(year, monthOfYear+1, dayOfMonth);
        }
    };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String data = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            Toast.makeText(EventRegisterActivity.this, "HORA = " + data + "h", Toast.LENGTH_SHORT).show();
            showTime(hourOfDay, minute);
        }
    };

   private void showDate(int year, int month, int day) {
        textViewDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private void showTime(int hour, int minute) {
        textViewHour.setText(new StringBuilder().append(hour).append(":")
                .append(minute).append("h"));
    }

   @Override
    public void onClick(View v) {
       if (v == buttonDate)
            showDialog(DATE_DIALOG_ID);
       if (v == buttonHour)
            showDialog(HOUR_DIALOG_ID);
    }
}
