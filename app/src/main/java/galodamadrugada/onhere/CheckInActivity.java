package galodamadrugada.onhere;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private final int EVENT_CODE_LENGHT = 6;
    ProgressDialog progressDialog;


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
                    if(eventCode.length() != EVENT_CODE_LENGHT){
                        checkinEventCode.setError(getResources().getString((R.string.invalid_code)));
                    }else{
                        HashMap<String, String> header = new HashMap<>();
                        header.put("x-access-token", preferences.getString("token",""));
                        Log.i("TokenCheckin","o token eh:  " + header.get("token"));

                        progressDialog.setMessage("Fazendo Checkin...");
                        progressDialog.show();

                        if (NetworkConnection.getInstance().isOnline()) {
                            CustomRequest customRequest = new CustomRequest(Request.Method.GET, Consts.SERVER + Consts.CHECK_IN, null, header,

                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.i("CustomRequestCHECKIN", "SUCCESSO: " + response.toString());
                                            AlertDialog.Builder builder = new AlertDialog.Builder(CheckInActivity.this);
                                            try {
                                                if(response.getString("status").equals(Consts.EVENT_ENTER_ERROR) || response.getString("status").equals(Consts.TOKEN_NOT_FOUND)){
                                                    progressDialog.hide();
                                                    builder.setMessage(R.string.check_in_fail).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            checkinEventCode.setText("");
                                                        }
                                                    });
                                                }else if(response.getString("status").equals(Consts.EVENT_SEARCHING_ERROR)){
                                                    progressDialog.hide();
                                                    builder.setMessage(R.string.event_not_found).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            checkInButton.setText("");
                                                        }
                                                    });
                                                }else if(response.getString("status").equals(Consts.EVENT_ENTER_SUCCESS)){
                                                    progressDialog.hide();
                                                    builder.setMessage(R.string.check_in_sucess).setPositiveButton(R.string.view, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Intent goToPerfil = new Intent(CheckInActivity.this,ProfileActivity.class);
                                                            startActivity(goToPerfil);
                                                            finish();
                                                        }
                                                    });
                                                    AlertDialog dialog = builder.create();
                                                    dialog.show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("CustomRequestCHECKIN", "ERRO NO REQUEST " + error.getMessage());
                                    Toast.makeText(CheckInActivity.this,"Erro na requisicao ao servidor",Toast.LENGTH_SHORT).show();
                                    progressDialog.hide();
                                }
                            }
                            );
                            NetworkConnection.getInstance().addToRequestQueue(customRequest);
                        }else {
                            Toast.makeText(CheckInActivity.this,R.string.network_not_conected,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }});
        }
}
