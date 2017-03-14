package galodamadrugada.onhere;

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

public class RegisterActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPass1, editTextPass2, editTextName;
    Button buttonRegister;
    TextView textViewRegister;

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    LayoutInflater inflater = RegisterActivity.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.dialog_register_success, null))
                            .setTitle(R.string.register_success_title)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);

                                    finish();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
