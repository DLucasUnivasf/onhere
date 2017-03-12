package galodamadrugada.onhere;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPass1, editTextPass2, editTextCPF, editTextName;
    Button buttonRegister;
    TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail      = (EditText)  findViewById(R.id.editTextEmail);
        editTextPass1      = (EditText)  findViewById(R.id.editTextPass1);
        editTextPass2      = (EditText)  findViewById(R.id.editTextPass2);
        editTextCPF        = (EditText)  findViewById(R.id.editTextCPF);
        editTextName       = (EditText)  findViewById(R.id.editTextName);

        buttonRegister     = (Button)    findViewById(R.id.buttonRegister);

        textViewRegister   = (TextView)  findViewById(R.id.textViewRegister);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
    }
}
