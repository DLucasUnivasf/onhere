package galodamadrugada.onhere;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import galodamadrugada.onhere.network.CustomRequest;
import galodamadrugada.onhere.network.NetworkConnection;
import galodamadrugada.onhere.util.UrlConst;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPass1, editTextPass2, editTextName;
    Button buttonRegister;
    TextView textViewRegister;
    ProgressDialog progressDialog;
    String dialogTitle, dialogText, dialogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail      = (EditText)  findViewById(R.id.editTextEmail);
        editTextPass1      = (EditText)  findViewById(R.id.editTextPass1);
        editTextPass2      = (EditText)  findViewById(R.id.editTextPass2);
        editTextName       = (EditText)  findViewById(R.id.editTextName);

        buttonRegister     = (Button)    findViewById(R.id.buttonRegister);

        textViewRegister   = (TextView)  findViewById(R.id.textViewRegister);



        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextName.getText().toString().equals("")) {
                    editTextName.requestFocus();
                    editTextName.setError(getResources().getString(R.string.required_field));
                }
                else if (editTextEmail.getText().toString().equals("")) {
                    editTextEmail.requestFocus();
                    editTextEmail.setError(getResources().getString(R.string.required_field));
                }
                else if (!ValidateField.isEmailValid(editTextEmail.getText().toString())){
                    editTextEmail.requestFocus();
                    editTextEmail.setError(getResources().getString(R.string.insert_valid_email));
                }
                else if (editTextPass1.getText().toString().equals("")) {
                    editTextPass1.requestFocus();
                    editTextPass1.setError(getResources().getString(R.string.required_field));
                }
                else if (editTextPass2.getText().toString().equals("")) {
                    editTextPass2.requestFocus();
                    editTextPass2.setError(getResources().getString(R.string.required_field));
                }
                else if (!editTextPass2.getText().toString().equals(editTextPass1.getText().toString())) {
                    editTextPass2.requestFocus();
                    editTextPass2.setError(getResources().getString(R.string.password_not_match));
                }
                else {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("fullname", editTextName.getText().toString());
                    params.put("email", editTextEmail.getText().toString());
                    params.put("password", editTextPass1.getText().toString());

                    progressDialog.setMessage("Carregando...");
                    showProgressDialog();

                    CustomRequest customRequest = new CustomRequest(Request.Method.POST, UrlConst.SERVER + UrlConst.NEW_USER, params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);


                                    if(response.toString() == "408") {
                                        Log.i("CustomRequest", "Erro: " + response.toString());
                                        hideProgressDialog();

                                        dialogTitle = getResources().getString(R.string.register_email_already_exist_title);
                                        dialogButton = getResources().getString(R.string.back);
                                        dialogText = getResources().getString(R.string.register_email_already_exist);

                                        builder.setPositiveButton(dialogButton, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                    }

                                    else{
                                        Log.i("CustomRequest", "Sucesso: " + response.toString());
                                        hideProgressDialog();

                                        dialogTitle = getResources().getString(R.string.register_success_title);
                                        dialogButton = getResources().getString(R.string.ok);
                                        dialogText = getResources().getString(R.string.register_success);

                                        builder.setPositiveButton(dialogButton, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent();
                                                setResult(RESULT_OK, intent);

                                                finish();
                                            }
                                        });
                                    }

                                    LayoutInflater inflater = RegisterActivity.this.getLayoutInflater();
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
            }
        });
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
