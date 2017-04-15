package galodamadrugada.onhere;

//########## IMPORTS ########################################################################
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


//########## INÍCIO DA CLASSE ###############################################################
public class EventRegisterActivity extends AppCompatActivity implements Button.OnClickListener{

//########## DECLARAÇÃO VARIÁVEIS ###########################################################
    private Calendar calendar;
    private int year, month, day, hour, minute, selector;
    private EditText editTextEventName, editTextDelay, editTextDescription;
    private TextView textViewHour, textViewDate, textViewDateEnd, textViewHourEnd, textViewAddress;
    private Button buttonHour, buttonDate, buttonRegisterEvent, buttonEndHour, buttonEndDate, buttonAddress;
    static final int DATE_DIALOG_ID = 0;
    static final int HOUR_DIALOG_ID = 1;
    private static final int REQUEST_CODE = 1;
    private Bundle bundle;

    String dateStart, hourStart, dateEnd, hourEnd, dialogTitle, dialogText, dialogButton;

    ProgressDialog progressDialog;

    HashMap<String, String> params = new HashMap<>();
    HashMap<String, String> headers = new HashMap<>();

//########## INÍCIO DOS MÉTODOS ###############################################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);

//########## INICIALIZAÇÃO DAS VARIÁVEIS #######################################################
        final SharedPreferences preferences = getSharedPreferences(Consts.PREFS_FILE_NAME, MODE_PRIVATE); //TOKEN

        //EXIBIÇÃO DOS DADO DO EVENTO
        editTextEventName = (EditText) findViewById(R.id.editTextEventName);
        editTextDelay = (EditText) findViewById(R.id.editTextDelay);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        //DATE PICKER'S
        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener((View.OnClickListener) this);

        buttonEndDate = (Button) findViewById(R.id.buttonDateEnd);
        buttonEndDate.setOnClickListener((View.OnClickListener) this);

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewDateEnd = (TextView) findViewById(R.id.textViewDateEnd);

        //TIME PICKER'S
        buttonHour = (Button) findViewById(R.id.buttonHour);
        buttonHour.setOnClickListener((View.OnClickListener) this);

        buttonEndHour = (Button) findViewById(R.id.buttonHourEnd);
        buttonEndHour.setOnClickListener((View.OnClickListener) this);

        textViewHour = (TextView) findViewById(R.id.textViewHour);
        textViewHourEnd = (TextView) findViewById(R.id.textViewHourEnd);

        //PLACE PICKER
        buttonAddress = (Button) findViewById(R.id.buttonAddress);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        buttonAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se não possui permissão
                if (ContextCompat.checkSelfPermission(EventRegisterActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Verifica se já mostramos o alerta e o usuário negou na 1ª vez.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(EventRegisterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Caso o usuário tenha negado a permissão anteriormente, e não tenha marcado o check "nunca mais mostre este alerta"
                        // Podemos mostrar um alerta explicando para o usuário porque a permissão é importante.
                        Context context = getApplicationContext();
                        CharSequence text = getResources().getString(R.string.required_location_permission);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    } else {
                        // Solicita a permissão
                        ActivityCompat.requestPermissions(EventRegisterActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
                    }
                } else {
                    // Tudo OK, podemos prosseguir.


                    Intent goToPlacePicker = new Intent(getApplicationContext(), PlacePickerActivity.class);
                    startActivityForResult(goToPlacePicker, REQUEST_CODE);
                }

            }
        });

        //DADOS DE DATA E HORA
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        //CONFIGURA O PROGRESS DIALOG
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        //SELETOR DA TEXT VIEW QUE SERÁ ALTERADA PELO PICKER
        selector = 3;

        //INICIA AS TEXT VIEWS COM OS DADOS ATUAIS
        showDate(year, month+1, day);
        showTime(hour, minute);

        dateStart = String.format("%04d-%02d-%02dT", year, month+1, day);
        dateEnd = String.format("%04d-%02d-%02dT", year, month+1, day);
        hourStart = String.format("%02d:%02d:00", hour, minute);

        //BOTÃO DE CADASTRO
        buttonRegisterEvent = (Button) findViewById(R.id.buttonRegisterEvent);
        buttonRegisterEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DEFINE OS FORMATOS DE DATA E HORA PARA FIM DE COMPARAÇÃO
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

                //INICIALIZA AS VARIÁVEIS QUE SERÃO COMPARADAS PARA FIM DE CONSISTÊNCIA DE DADOS
                Date inTime = null, outTime = null, inDate = null, outDate = null;

                //COLOCA OS DADOS ATUAIS NAS VARIÁVEIS ACIMA
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

                //VERIFICA SE O NOME DO EVENTO FOI PREENCHIDO
                if (editTextEventName.getText().toString().equals("")) {
                    editTextEventName.requestFocus();
                    editTextEventName.setError(getResources().getString(R.string.required_field));
                }
                //VERIFICA SE O ATRASO MÁXIMO DO EVENTO FOI PREENCHIDO
                else if (editTextDelay.getText().toString().equals("")) {
                    editTextDelay.requestFocus();
                    editTextDelay.setError(getResources().getString(R.string.required_field));
                }
                //VERIFICA SE A DESCRIÇÃO DO EVENTO FOI PREENCHIDA
                else if (editTextDescription.getText().toString().equals("")) {
                    editTextDescription.requestFocus();
                    editTextDescription.setError(getResources().getString(R.string.required_field));
                }
                //VERIFICA SE A DATA INICIAL DO EVENTO É ANTERIOR OU IGUAL À DATA FINAL
                else if (inDate.after(outDate)) {
                        Context context = getApplicationContext();
                        CharSequence text = getResources().getString(R.string.difference_date);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                }
                //VERIFICA SE A HORA INICIAL DO EVENTO É ANTERIOR À HORA FINAL
                else if ((outDate.equals(inDate)) && (inTime.compareTo(outTime) != -1)) {
                        Context context = getApplicationContext();
                        CharSequence text = getResources().getString(R.string.difference_hour);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                }
                //VERIFICA SE O ENDEREÇO DO EVENTO FOI PREENCHIDO
                else if (textViewAddress.getText().toString().equals("")) {
                    textViewAddress.requestFocus();

                    Context context = getApplicationContext();
                    CharSequence text = getResources().getString(R.string.required_address);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                //INICIA A REQUISIÇÃO AO SERVIDOR SE TODOS OS TESTES ACIMA FOREM SATISFEITOS
                else{
                    if (NetworkConnection.getInstance().isOnline()) {
                        //INFORMA OS PARÂMETROS DA REQUISIÇÃO
                        params.put("desc", editTextDescription.getText().toString());
                        params.put("nome", editTextEventName.getText().toString());
                        params.put("dtin", dateStart + hourStart);
                        params.put("tolerancia", editTextDelay.getText().toString());
                        params.put("dtfim", dateEnd + hourEnd);
                        params.put("latitude", bundle.getString("latitude"));
                        params.put("longitude", bundle.getString("longitude"));

                        headers.put("x-access-token", preferences.getString("token", ""));

                        //MOSTRA O PROGRESS DIALOG
                        progressDialog.setMessage("Carregando...");
                        showProgressDialog();

                        //EXECUTA A REQUISIÇÃO DE CADASTRO DE EVENTO
                        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Consts.SERVER + Consts.NEW_EVENT, params, headers,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        //CRIA UM NOVO ALERT DIALOG COM INFORMAÇÕES QUE DEPENDEM DA RESPOSTA DO SERVIDOR
                                        AlertDialog.Builder builder = new AlertDialog.Builder(EventRegisterActivity.this);

                                        try {
                                            //TRATA O ERRO DE CÓDIGO 420: Formato de data inválido.
                                            if (response.getString("status").equals("420")) {
                                                Log.i("CustomRequest", "Erro: " + response.toString());
                                                hideProgressDialog();

                                                dialogTitle = getResources().getString(R.string.register_email_already_exist_title);
                                                dialogButton = getResources().getString(R.string.back);
                                                dialogText = getResources().getString(R.string.invalid_date);

                                                builder.setPositiveButton(dialogButton, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                            }
                                            //TRATA O ERRO DE CÓDIGO 421: Erro ao tentar cadastrar o evento.
                                            else if (response.getString("status").equals("421")) {
                                                Log.i("CustomRequest", "Erro: " + response.toString());
                                                hideProgressDialog();

                                                dialogTitle = getResources().getString(R.string.server_error);
                                                dialogButton = getResources().getString(R.string.back);
                                                dialogText = getResources().getString(R.string.invalid_date);

                                                builder.setPositiveButton(dialogButton, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                            }
                                            //SE O SERVIDOR NÃO RETORNAR ERRO, CHAMA A TELA DE PERFIL
                                            else {
                                                Log.i("CustomRequest", "Sucesso: " + response.toString());

                                                //ESCONDE O PROGRESS DIALOG
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

                                        //MOSTRA O DIALOG CRIADO ANTERIORMENTE
                                        LayoutInflater inflater = EventRegisterActivity.this.getLayoutInflater();
                                        builder.setMessage(dialogText)
                                                .setTitle(dialogTitle);

                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                },

                                //TRATA ERROS HTTP RETORNADOS PELO SERVIDOR
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

    //FUNÇÃO QUE CRIA OS DIALOGS DO PLACE PICKER E TIME PICKER DE ACORDO COM O BOTÃO PRESSIONADO
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

    //FUNÇÕES QUE MOSTRAM NA TELA O VALOR SELECIONADO NO DATE PICKER E FORMATAM O DADO DA REQUISIÇÃO DE ACORDO COM O BOTÃO PRESSIONADO
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int day) {

            //BOTÃO DE DATA INICIAL PRESSIONADO
            if(selector == 1)
                dateStart = String.format("%04d-%02d-%02dT", year, month+1, day);

            //BOTÃO DE DATA FINAL PRESSIONADO
            if(selector == 2)
                dateEnd = String.format("%04d-%02d-%02dT", year, month+1, day);

            //INICIAL
            if(selector == 3) {
                dateStart = String.format("%04d-%02d-%02dT", year, month + 1, day);
                dateEnd = String.format("%04d-%02d-%02dT", year, month + 1, day);
            }

            showDate(year, month+1, day);
        }
    };

    private void showDate(int year, int month, int day) {

        //BOTÃO DATA INICIAL PRESSIONADO
        if(selector == 1)
            textViewDate.setText(String.format("%02d/%02d/%04d", day, month, year));

        //BOTÃO DATA FINAL PRESSIONADO
        if(selector == 2)
            textViewDateEnd.setText(String.format("%02d/%02d/%04d", day, month, year));

        //INICIAL
        if(selector == 3){
            textViewDate.setText(String.format("%02d/%02d/%04d", day, month, year));
            textViewDateEnd.setText(String.format("%02d/%02d/%04d", day, month, year));
        }
    }


    //FUNÇÕES QUE MOSTRAM NA TELA O VALOR SELECIONADO NO TIME PICKER E FORMATAM O DADO DA REQUISIÇÃO DE ACORDO COM O BOTÃO PRESSIONADO
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            //BOTÃO HORA INICIAL PRESSIONADO
            if(selector == 1)
                hourStart = String.format("%02d:%02d:00", hourOfDay, minute);

            //BOTÃO HORA FINAL PRESSIONADO
            if(selector == 2)
                hourEnd = String.format("%02d:%02d:00", hourOfDay, minute);

            //INICIAL
            if(selector == 3) {
                hourStart = String.format("%02d:%02d:00", hourOfDay, minute);
                hourEnd = String.format("%02d:%02d:00", hourOfDay, minute);
            }

            showTime(hourOfDay, minute);
        }
    };


    private void showTime(int hour, int minute) {

        //BOTÃO HORA INICIAL PRESSIONADO
        if(selector == 1)
            textViewHour.setText(String.format("%02d:%02d", hour, minute));

        //BOTÃO HORA FINAL PRESSIONADO
        if(selector == 2)
            textViewHourEnd.setText(String.format("%02d:%02d", hour, minute));

        //INICIAL
        if(selector == 3){
            textViewHour.setText(String.format("%02d:%02d", hour, minute));
            textViewHourEnd.setText(String.format("%02d:%02d", hour, minute));
        }
    }


    //FUNÇÃO QUE EXIBE O PICKER DE ACORDO COM O BOTÃO PRESSIONADO
   @Override
    public void onClick(View v) {

       //BOTÃO DATA INICIAL PRESSIONADO
       if (v == buttonDate) {
           selector = 1;
           showDialog(DATE_DIALOG_ID);
       }

       //BOTÃO DATA FINAL PRESSIONADO
       if (v == buttonEndDate) {
           selector = 2;
           showDialog(DATE_DIALOG_ID);
       }

       //BOTÃO HORA INICIAL PRESSIONADO
       if (v == buttonHour) {
           selector = 1;
           showDialog(HOUR_DIALOG_ID);
       }

       //BOTÃO HORA FINAL PRESSIONADO
       if (v == buttonEndHour) {
           selector = 2;
           showDialog(HOUR_DIALOG_ID);
       }

    }

    //DEFINIÇÃO DAS FUNÇÕES DO PROGRESS DIALOG
    private void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }

    //FUNÇÃO QUE EXIBE OS DADOS DO LOCAL SELECIONADO NO PLACE PICKER
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentPicker) {
        if(requestCode==1) {
            if(resultCode==RESULT_OK) {
                bundle = intentPicker.getExtras();

                textViewAddress.setText(bundle.getString("name") + " - " + bundle.getString("address"));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCodePermission,String permissions[], int[] grantResults) {
        switch (requestCodePermission) {
            case 0: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)      {
                    // Usuário aceitou a permissão!
                    Intent goToPlacePicker = new Intent(getApplicationContext(), PlacePickerActivity.class);
                    startActivityForResult(goToPlacePicker, REQUEST_CODE);
                } else {
                    // Usuário negou a permissão.
                    // Não podemos utilizar esta funcionalidade.
                    Log.i("Erro", "permissão negada");

                    Context context = getApplicationContext();
                    CharSequence text = getResources().getString(R.string.required_location_permission);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                return;
            }
        }
    }
}
