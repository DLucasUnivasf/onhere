package galodamadrugada.onhere;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import galodamadrugada.onhere.network.CustomRequest;
import galodamadrugada.onhere.network.NetworkConnection;
import galodamadrugada.onhere.util.Consts;

public class EventRegisterActivity extends AppCompatActivity implements Button.OnClickListener{

/////////////////////////////// VARIÁVEIS /////////////////////////////////
    private Calendar calendar;
    private int year, month, day, hour, minute, selector;
    private EditText editTextEventName, editTextDelay, editTextDescription;
    private TextView textViewHour, textViewDate, textViewDateEnd, textViewHourEnd, textViewAddress;
    private Button buttonHour, buttonDate, buttonRegisterEvent, buttonEndHour, buttonEndDate, buttonAddress;
    static final int DATE_DIALOG_ID = 0;
    static final int HOUR_DIALOG_ID = 1;
    private static final int REQUEST_CODE = 1;
    private Bundle bundle;



    String dateStart, hourStart, dateEnd, hourEnd;

    ProgressDialog progressDialog;

    String dialogTitle, dialogText, dialogButton;

    HashMap<String, String> params = new HashMap<>();
    HashMap<String, String> headers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);

/////////////////////////////// INICIALIZAÇÃO /////////////////////////////////
        final SharedPreferences preferences = getSharedPreferences(Consts.PREFS_FILE_NAME, MODE_PRIVATE);

        editTextEventName = (EditText) findViewById(R.id.editTextEventName);
        editTextDelay = (EditText) findViewById(R.id.editTextDelay);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener((View.OnClickListener) this);

        buttonHour = (Button) findViewById(R.id.buttonHour);
        buttonHour.setOnClickListener((View.OnClickListener) this);

        buttonEndDate = (Button) findViewById(R.id.buttonDateEnd);
        buttonEndDate.setOnClickListener((View.OnClickListener) this);

        buttonEndHour = (Button) findViewById(R.id.buttonHourEnd);
        buttonEndHour.setOnClickListener((View.OnClickListener) this);

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewHour = (TextView) findViewById(R.id.textViewHour);
        textViewDateEnd = (TextView) findViewById(R.id.textViewDateEnd);
        textViewHourEnd = (TextView) findViewById(R.id.textViewHourEnd);

        buttonAddress = (Button) findViewById(R.id.buttonAddress);

        textViewAddress = (TextView) findViewById(R.id.textViewAddress);

        buttonRegisterEvent = (Button) findViewById(R.id.buttonRegisterEvent);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        selector = 3;
        showDate(year, month+1, day);
        showTime(hour, minute);

        buttonAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPlacePicker = new Intent(getApplicationContext(), PlacePickerActivity.class);
                startActivityForResult(goToPlacePicker, REQUEST_CODE);
                //finish();
            }
        });

        buttonRegisterEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                Date inTime = null, outTime = null, inDate = null, outDate = null;

                try {
                    inDate = sdf2.parse(textViewDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    outDate = sdf2.parse(textViewDateEnd.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    inTime = sdf.parse(textViewHour.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    outTime = sdf.parse(textViewHourEnd.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert inTime != null;
                assert outTime != null;
                assert outDate != null;
                assert inDate != null;

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
                else if (inDate.after(outDate)) {
                        Context context = getApplicationContext();
                        CharSequence text = getResources().getString(R.string.difference_date);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                }
                else if ((outDate.equals(inDate)) && (inTime.compareTo(outTime) != -1)) {
                        Context context = getApplicationContext();
                        CharSequence text = getResources().getString(R.string.difference_hour);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                }
                else if (textViewAddress.getText().toString().equals("")) {
                    textViewAddress.requestFocus();

                    Context context = getApplicationContext();
                    CharSequence text = getResources().getString(R.string.required_address);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else{
                    if (NetworkConnection.getInstance().isOnline()) {
                        params.put("desc", editTextDescription.getText().toString());
                        params.put("nome", editTextEventName.getText().toString());
                        params.put("dtin", dateStart + hourStart);
                        params.put("tolerancia", editTextDelay.getText().toString());
                        params.put("dtfim", dateEnd + hourEnd);
                        params.put("latitude", bundle.getString("latitude"));
                        params.put("longitude", bundle.getString("longitude"));


                        headers.put("x-access-token", preferences.getString("token", ""));

                        progressDialog.setMessage("Carregando...");
                        showProgressDialog();

                        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Consts.SERVER + Consts.NEW_EVENT, params, headers,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(EventRegisterActivity.this);


                                        try {

                                            if (response.getString("status").equals("418")) {
                                                Log.i("CustomRequest", "Erro: " + response.toString());
                                                hideProgressDialog();

                                                dialogTitle = getResources().getString(R.string.register_email_already_exist_title);
                                                dialogButton = getResources().getString(R.string.back);
                                                dialogText = getResources().getString(R.string.register_email_already_exist);

                                                builder.setPositiveButton(dialogButton, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                            } else {
                                                Log.i("CustomRequest", "Sucesso: " + response.toString());
                                                hideProgressDialog();

                                                dialogTitle = getResources().getString(R.string.event_register_success_title);
                                                dialogButton = getResources().getString(R.string.go_to_profile);
                                                dialogText = getResources().getString(R.string.event_register_success);

                                                builder.setPositiveButton(dialogButton, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Intent goToProfile = new Intent(EventRegisterActivity.this, ProfileActivity.class);
                                                        startActivity(goToProfile);
                                                        finish();
                                                    }
                                                });
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        LayoutInflater inflater = EventRegisterActivity.this.getLayoutInflater();
                                        builder.setMessage(dialogText)
                                                .setTitle(dialogTitle);

                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i("CustomRequest", "ERRO NO REQUEST " + error.getMessage());
                                        hideProgressDialog();

                                        Context context = getApplicationContext();
                                        CharSequence text = getResources().getString(R.string.http_error);
                                        int duration = Toast.LENGTH_SHORT;

                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    }
                                }
                        );
                        NetworkConnection.getInstance().addToRequestQueue(customRequest);

                    }
                    else {
                        Context context = getApplicationContext();
                        CharSequence text = getResources().getString(R.string.network_not_conected);
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
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
            if(selector == 1)
                dateStart = String.format("%04d-%02d-%02dT", year, month+1, day);
            if(selector == 2)
                dateEnd = String.format("%04d-%02d-%02dT", year, month+1, day);
            if(selector == 3) {
                dateStart = String.format("%04d-%02d-%02dT", year, month + 1, day);
                dateEnd = String.format("%04d-%02d-%02dT", year, month + 1, day);
            }

            showDate(year, month+1, day);
        }
    };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(selector == 1)
                hourStart = String.format("%02d:%02d.00Z", hourOfDay, minute);
            if(selector == 2)
                hourEnd = String.format("%02d:%02d.00Z", hourOfDay, minute);
            if(selector == 3) {
                hourStart = String.format("%02d:%02d.00Z", hourOfDay, minute);
                hourEnd = String.format("%02d:%02d.00Z", hourOfDay, minute);
            }
            showTime(hourOfDay, minute);
        }
    };

   private void showDate(int year, int month, int day) {
       if(selector == 1)
           textViewDate.setText(String.format("%02d/%02d/%04d", day, month, year));

       if(selector == 2)
           textViewDateEnd.setText(String.format("%02d/%02d/%04d", day, month, year));

       if(selector == 3){
           textViewDate.setText(String.format("%02d/%02d/%04d", day, month, year));
           textViewDateEnd.setText(String.format("%02d/%02d/%04d", day, month, year));
           }
    }

    private void showTime(int hour, int minute) {
        if(selector == 1)
            textViewHour.setText(String.format("%02d:%02d", hour, minute));
        if(selector == 2)
            textViewHourEnd.setText(String.format("%02d:%02d", hour, minute));
        if(selector == 3){
            textViewHour.setText(String.format("%02d:%02d", hour, minute));
            textViewHourEnd.setText(String.format("%02d:%02d", hour, minute));
        }
    }

   @Override
    public void onClick(View v) {
       if (v == buttonDate) {
           selector = 1;
           showDialog(DATE_DIALOG_ID);
       }
       if (v == buttonHour) {
           selector = 1;
           showDialog(HOUR_DIALOG_ID);
       }
       if (v == buttonEndHour) {
           selector = 2;
           showDialog(HOUR_DIALOG_ID);
       }
       if (v == buttonEndDate) {
           selector = 2;
           showDialog(DATE_DIALOG_ID);
       }
    }

    private void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentPicker) {
        if(requestCode==1) {
            if(resultCode==RESULT_OK) {
                bundle = intentPicker.getExtras();

                textViewAddress.setText(bundle.getString("name") + " - " + bundle.getString("address"));
            }
        }
    }
}
