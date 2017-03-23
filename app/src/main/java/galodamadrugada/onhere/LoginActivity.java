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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import galodamadrugada.onhere.network.CustomRequest;
import galodamadrugada.onhere.network.NetworkConnection;
import galodamadrugada.onhere.util.Consts;

public class LoginActivity extends AppCompatActivity {
    final Context context = this;
    private static final int REQUEST_CODE = 1;

    EditText       editTextPass, editTextEmail;
    Button         buttonLogin;
    ImageView      imageViewLogo;
    TextView       textViewForgotPass, textViewSignInUp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail      = (EditText)  findViewById(R.id.editTextEmail);
        editTextPass       = (EditText)  findViewById(R.id.editTextPass);
        buttonLogin        = (Button)    findViewById(R.id.buttonLogin);
        textViewSignInUp     = (TextView)    findViewById(R.id.textViewSignInUp);
        imageViewLogo      = (ImageView) findViewById(R.id.imageViewLogo);
        textViewForgotPass = (TextView)  findViewById(R.id.textViewForgotPass);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        final String UNIQUE_ID       = "UNIQUE_ID";
        String uniqueId;

        SharedPreferences preferences = getSharedPreferences(Consts.PREFS_FILE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean("first_time", true)) {
            Log.d("SharedPreferences", "Primeiro boot");

            uniqueId = UUID.randomUUID().toString();

            preferences.edit().putString(UNIQUE_ID, uniqueId).apply();

            if(!uniqueId.equals(""))
                preferences.edit().putBoolean("first_time", false).apply();
        }

        uniqueId = preferences.getString(UNIQUE_ID, "");

        Log.i("UNIQUE_ID", uniqueId);

        textViewForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.dialog_send_reset_password_email, null);

                final EditText editTextResetEmail = (EditText)  view.findViewById(R.id.editTextResetEmail);

                builder.setView(view)
                        .setTitle(R.string.send_reset_password_email_title)
                        .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Não a nada aqui pois o método está sendo sobreescrito abaixo
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editTextResetEmail.getText().toString().equals("")) {
                            editTextResetEmail.setError(getResources().getString(R.string.required_field));
                        }
                        else {
                            if(ValidateField.isEmailValid(editTextResetEmail.getText().toString()))
                                dialog.dismiss();
                            else {
                                editTextResetEmail.requestFocus();
                                editTextResetEmail.setError(getResources().getString(R.string.insert_valid_email));
                            }
                        }
                    }
                });
            }
        });

        textViewSignInUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEmail.getText().toString().equals("")) {
                    editTextEmail.requestFocus();
                    editTextEmail.setError(getResources().getString(R.string.required_field));
                }
                else if (editTextPass.getText().toString().equals("")) {
                    editTextPass.requestFocus();
                    editTextPass.setError(getResources().getString(R.string.required_field));
                }
                else
                    if (ValidateField.isEmailValid(editTextEmail.getText().toString())) {
                        Log.i("LoginButton", "Email validado");
                        HashMap<String, String> params = new HashMap<>();
                        params.put("email", editTextEmail.getText().toString());
                        params.put("password", editTextPass.getText().toString());

                        progressDialog.setMessage(getResources().getString(R.string.loading));
                        showProgressDialog();

                        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Consts.SERVER + Consts.LOGIN, params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("CustomRequest", "SUCCESSO: " + response.toString());
                                    hideProgressDialog();
                                    Intent goToMain = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(goToMain);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("CustomRequest", "ERRO NO REQUEST " + error.getMessage());
                                    hideProgressDialog();
                                }
                            }
                        );
                        NetworkConnection.getInstance().addToRequestQueue(customRequest);
                    }
                    else {
                        editTextEmail.requestFocus();
                        editTextEmail.setError(getResources().getString(R.string.insert_valid_email));
                    }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1) {
            if(resultCode==RESULT_OK) {
                editTextEmail.setText(data.getData().toString());
            }
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
}
