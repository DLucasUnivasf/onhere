package galodamadrugada.onhere;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button checkInButton = (Button) findViewById(R.id.mainCheckInButton);
        Button newEventButton = (Button) findViewById(R.id.newEventButton);
        EditText descriptionText = (EditText) findViewById(R.id.mainDescription);

        descriptionText.setText("   OnHere gerencia seus eventos! \n\n"+
                "Descrição: blablablablablablablablalblalblalbla, Texto estatico");

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
        }
        return super.onOptionsItemSelected(item);
    }

}
