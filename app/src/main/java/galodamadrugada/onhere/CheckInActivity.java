package galodamadrugada.onhere;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import galodamadrugada.onhere.network.CustomRequest;
import galodamadrugada.onhere.network.NetworkConnection;
import galodamadrugada.onhere.util.Consts;

public class CheckInActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    String dialogTitle,dialogMessage;
    final String dialogButtonText = "Ok";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);


        final Button checkInButton = (Button) findViewById(R.id.checkInCheckInButton);
        final EditText checkinEventCode = (EditText) findViewById(R.id.checkinEventCode);

        final SharedPreferences preferences = getSharedPreferences(Consts.PREFS_FILE_NAME, MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkinEventCode.getText().toString().equals("")){
                    checkinEventCode.requestFocus();
                    checkinEventCode.setError(getResources().getString((R.string.required_field)));
                }else{
                    String eventCode = checkinEventCode.getText().toString();
                    if(eventCode.length() != Consts.EVENT_CODE_LENGHT){
                        checkinEventCode.setError(getResources().getString((R.string.invalid_code_length)));
                    }else{
                        final AlertDialog.Builder builder = new AlertDialog.Builder(CheckInActivity.this);

                        if (NetworkConnection.getInstance().isOnline()) {
                            HashMap<String, String> header = new HashMap<>();
                            header.put("x-access-token", preferences.getString("token", ""));

                            progressDialog.setMessage("Fazendo Checkin...");
                            progressDialog.show();

                            CustomRequest customRequest = new CustomRequest(Request.Method.GET, Consts.SERVER + Consts.CHECK_IN + "?chave=" + eventCode, null, header,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.i("CustomRequestCHECKIN", "SUCCESSO: " + response.toString());

                                            try {
                                                switch (response.getString("status")) {
                                                    case Consts.TOKEN_NOT_FOUND:
                                                        progressDialog.hide();
                                                        dialogTitle = getResources().getString(R.string.id_error);
                                                        dialogMessage = getResources().getString(R.string.token_error);
                                                        builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                            }
                                                        });
                                                        break;
                                                    case Consts.INVALID_TOKEN:
                                                        progressDialog.hide();
                                                        dialogTitle = getResources().getString(R.string.id_error);
                                                        dialogMessage = getResources().getString(R.string.token_error);
                                                        builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                            }
                                                        });
                                                        break;
                                                    case Consts.TOKEN_USER_NOT_FOUND:
                                                        progressDialog.hide();
                                                        dialogTitle = getResources().getString(R.string.id_error);
                                                        dialogMessage = getResources().getString(R.string.token_error);
                                                        builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                            }
                                                        });
                                                        break;
                                                    case Consts.EVENT_SEARCHING_ERROR:
                                                        progressDialog.hide();
                                                        dialogTitle = getResources().getString(R.string.event_not_found);
                                                        dialogMessage = getResources().getString(R.string.event_not_found_message);
                                                        ;
                                                        builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                            }
                                                        });
                                                        break;
                                                    case Consts.EVENT_ENTER_ERROR:
                                                        progressDialog.hide();
                                                        dialogTitle = getResources().getString(R.string.check_in_error);
                                                        dialogMessage = getResources().getString(R.string.server_error);
                                                        builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                            }
                                                        });
                                                        break;

                                                    case Consts.EVENT_ENTER_SUCCESS:
                                                        progressDialog.hide();
                                                        dialogTitle = getResources().getString(R.string.check_in_sucess);
                                                        ;
                                                        dialogMessage = getResources().getString(R.string.check_in_sucess_message);
                                                        builder.setPositiveButton(R.string.view, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                Intent goToPerfil = new Intent(CheckInActivity.this, ProfileActivity.class);
                                                                startActivity(goToPerfil);
                                                                finish();
                                                            }
                                                        });
                                                        break;

                                                    default:
                                                        progressDialog.hide();
                                                        dialogMessage = getResources().getString(R.string.server_error);
                                                        builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                            }
                                                        });
                                                        break;
                                                }
                                                builder.setTitle(dialogTitle).setMessage(dialogMessage);
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("CustomRequestCHECKIN", "ERRO NO REQUEST " + error.getMessage());
                                    progressDialog.hide();
                                    builder.setMessage(R.string.request_error).setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                            NetworkConnection.getInstance().addToRequestQueue(customRequest);
                        }else {
                            builder.setMessage(R.string.network_not_conected).setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                }
            }});
        }
}
