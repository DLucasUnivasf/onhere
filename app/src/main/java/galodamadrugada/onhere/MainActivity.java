package galodamadrugada.onhere;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button checkInButton = (Button) findViewById(R.id.mainCheckInButton);
        FloatingActionButton newEventButton = (FloatingActionButton) findViewById(R.id.newEventButton);
        WebView descriptionText = (WebView) findViewById(R.id.mainDescription);

        /*Aparentemente textView não justifica texto, por isso o webView ... :-P */

        String text = "<html><body>"
                         + "<p align=\"justify\">"
                         + "No <b>OnHere</b> voce pode controlar seus eventos!</br></br>"
                         + "Clique no botao <strong>CHECK IN</strong> para registrar "
                         + "sua presenca em algum evento ou crie seu proprio evento</br></br>"
                         + "Voce tambem pode consultar seus eventos ou eventos onde registrou presenca"
                         +  "</br></br> <strong><i>Texto Exemplo</i></strong>"
                         + "</p> "
                         + "</body></html>";
                      descriptionText.loadData(text, "text/html", "ISO-8859-1");


        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCheckIn = new Intent(MainActivity.this,CheckInActivity.class);
                startActivity(goToCheckIn);
            }
        });

        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNewEvent = new Intent (MainActivity.this,EventRegisterActivity.class);
                startActivity(goToNewEvent);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu_goToProfile:
                Intent goToProfile = new Intent(this,ProfileActivity.class);
                startActivity(goToProfile);
                break;

            case R.id.main_menu_goToAbout:
                Intent goToAbout = new Intent(this,AboutActivty.class);
                startActivity(goToAbout);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
