package galodamadrugada.onhere;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    final Context context = this;

    EditText  editTextPass, editTextEmail;
    Button    buttonLogin, buttonSignInUp;
    CheckBox  checkBoxRemember;
    ImageView imageViewLogo;
    TextView  textViewForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextEmail      = (EditText)  findViewById(R.id.editTextEmail);
        editTextPass       = (EditText)  findViewById(R.id.editTextPass);
        buttonLogin        = (Button)    findViewById(R.id.buttonLogin);
        buttonSignInUp     = (Button)    findViewById(R.id.buttonSignInUp);
        checkBoxRemember   = (CheckBox)  findViewById(R.id.checkBoxRemember);
        imageViewLogo      = (ImageView) findViewById(R.id.imageViewLogo);
        textViewForgotPass = (TextView)  findViewById(R.id.textViewForgotPass);

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
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        buttonSignInUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEmail.getText().toString().equals("")) {
                    editTextEmail.setError(getResources().getString(R.string.required_field));
                }
                else if (editTextPass.getText().toString().equals("")) {
                    editTextPass.setError(getResources().getString(R.string.required_field));
                }
                else {
                    // Todo - Implementar a lógica
                }
            }
        });
    }
}
