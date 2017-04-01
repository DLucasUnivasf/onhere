package galodamadrugada.onhere;


//########## IMPORTS ########################################################################
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import galodamadrugada.onhere.network.CustomRequest;
import galodamadrugada.onhere.network.NetworkConnection;
import galodamadrugada.onhere.util.Consts;

//########## INÍCIO DA CLASSE ###############################################################
public class RegisterActivity extends AppCompatActivity {

//########## DECLARAÇÃO VARIÁVEIS ###########################################################
    EditText editTextEmail, editTextPass1, editTextPass2, editTextName;
    Button buttonRegister;
    TextView textViewRegister;
    ProgressDialog progressDialog;
    String dialogTitle, dialogText, dialogButton;

//########## INÍCIO DOS MÉTODOS ###############################################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//########## INICIALIZAÇÃO DAS VARIÁVEIS #######################################################

        //EXIBIÇÃO DOS DADOS DE CADASTRO
        textViewRegister   = (TextView)  findViewById(R.id.textViewRegister);
        editTextEmail      = (EditText)  findViewById(R.id.editTextEmail);
        editTextPass1      = (EditText)  findViewById(R.id.editTextPass1);
        editTextPass2      = (EditText)  findViewById(R.id.editTextPass2);
        editTextName       = (EditText)  findViewById(R.id.editTextName);

        //CONFIGURA O PROGRESS DIALOG
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        //BOTÃO DE CADASTRO
        buttonRegister     = (Button)    findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //VERIFICA SE O NOME DO USUÁRIO FOI PREENCHIDO
                if (editTextName.getText().toString().equals("")) {
                    editTextName.requestFocus();
                    editTextName.setError(getResources().getString(R.string.required_field));
                }

                //VERIFICA SE O EMAIL DO USUÁRIO FOI PREENCHIDO
                else if (editTextEmail.getText().toString().equals("")) {
                    editTextEmail.requestFocus();
                    editTextEmail.setError(getResources().getString(R.string.required_field));
                }

                //VERIFICA SE O EMAIL DO USUÁRIO POSSUI UM FORMATO VÁLIDO
                else if (!ValidateField.isEmailValid(editTextEmail.getText().toString())){
                    editTextEmail.requestFocus();
                    editTextEmail.setError(getResources().getString(R.string.insert_valid_email));
                }

                //VERIFICA SE O PRIMEIRO CAMPO SENHA FOI PREENCHIDO
                else if (editTextPass1.getText().toString().equals("")) {
                    editTextPass1.requestFocus();
                    editTextPass1.setError(getResources().getString(R.string.required_field));
                }

                //VERIFICA SE O SEGUNDO CAMPO SENHA FOI PREENCHIDO
                else if (editTextPass2.getText().toString().equals("")) {
                    editTextPass2.requestFocus();
                    editTextPass2.setError(getResources().getString(R.string.required_field));
                }

                //VERIFICA SE O PRIMEIRO E O SEGUNDO CAMPO SENHA SÃO IGUAIS
                else if (!editTextPass2.getText().toString().equals(editTextPass1.getText().toString())) {
                    editTextPass2.requestFocus();
                    editTextPass2.setError(getResources().getString(R.string.password_not_match));
                }

                //INICIA A REQUISIÇÃO AO SERVIDOR SE TODOS OS TESTES ACIMA FOREM SATISFEITOS
                else {
                    if (NetworkConnection.getInstance().isOnline()) {

                        //INFORMA OS PARÂMETROS DA REQUISIÇÃO
                        HashMap<String, String> params = new HashMap<>();
                        params.put("fullname", editTextName.getText().toString());
                        params.put("email", editTextEmail.getText().toString());
                        params.put("password", editTextPass1.getText().toString());

                        //MOSTRA O PROGRESS DIALOG
                        progressDialog.setMessage("Carregando...");
                        showProgressDialog();

                        //EXECUTA A REQUISIÇÃO DE CADASTRO DE USUÁRIO
                        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Consts.SERVER + Consts.NEW_USER, params, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        //CRIA UM NOVO ALERT DIALOG COM INFORMAÇÕES QUE DEPENDEM DA RESPOSTA DO SERVIDOR
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                                        try {
                                            //TRATA O ERRO DE CÓDIGO 408: Email já cadastrado.
                                            if (response.getString("status").equals("408")) {
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
                                            //SE O SERVIDOR NÃO RETORNAR ERRO, CHAMA A TELA DE LOGIN.
                                            else {
                                                Log.i("CustomRequest", "Sucesso: " + response.toString());

                                                //ESCONDE O PROGRESS DIALOG
                                                hideProgressDialog();

                                                dialogTitle = getResources().getString(R.string.register_success_title);
                                                dialogButton = getResources().getString(R.string.ok);
                                                dialogText = getResources().getString(R.string.register_success);

                                                builder.setPositiveButton(dialogButton, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Intent intent = new Intent();
                                                        intent.setData(Uri.parse(editTextEmail.getText().toString()));

                                                        setResult(RESULT_OK, intent);

                                                        finish();
                                                    }
                                                });
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        //MOSTRA O DIALOG CRIADO ANTERIORMENTE
                                        LayoutInflater inflater = RegisterActivity.this.getLayoutInflater();
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

    //DEFINIÇÃO DAS FUNÇÕES DO PROGRESS DIALOG
    private void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }
}
