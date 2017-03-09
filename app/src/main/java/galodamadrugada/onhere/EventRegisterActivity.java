package galodamadrugada.onhere;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class EventRegisterActivity extends AppCompatActivity {
    private Button buttonDate;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    static final int DATE_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

//        showDate(year, month+1, day);


        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, year, month,
                        day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String data = String.valueOf(dayOfMonth) + " /"
                    + String.valueOf(monthOfYear+1) + " /" + String.valueOf(year);
            Toast.makeText(EventRegisterActivity.this,
                    "DATA = " + data, Toast.LENGTH_SHORT)
                    .show();
        }
    };

//    private void showDate(int year, int month, int day) {
//        dateView.setText(new StringBuilder().append(day).append("/")
//                .append(month).append("/").append(year));
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == buttonDate)
//            showDialog(DATE_DIALOG_ID);
//    }
}
