package galodamadrugada.onhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by UNIVASF on 14/03/2017.
 */

public class EventActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView idTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        idTextView   = (TextView) findViewById(R.id.idTextView);

        /*Daniel -- Adicionei este listener so para a tela de infoEvent ficar acessivel no app e verificar o posicionamento das objetos nela*/
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEventInfo = new Intent(EventActivity.this,EventInfoActivity.class);
                startActivity(goToEventInfo);
            }
        });
        /*Daniel - Termina aqui minhas alteracoes*/

        nameTextView.setText(getIntent().getExtras().getString("name"));
        idTextView.setText(getIntent().getExtras().getString("id"));


    }
}
