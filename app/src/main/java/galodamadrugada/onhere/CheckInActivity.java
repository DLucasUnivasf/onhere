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

public class CheckInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);


        Button checkInButton = (Button) findViewById(R.id.checkin_CheckInButton);

        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckInActivity.this);
                builder.setMessage(R.string.check_in_sucess).setPositiveButton("Visualizar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                                //Intent goToPerfil = new Intent(CheckInActivity.this,profile.class)
                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }
}
