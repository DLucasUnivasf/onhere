package galodamadrugada.onhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button checkInButton = (Button) findViewById(R.id.main_CheckInButton);
        Button newEventButton = (Button) findViewById(R.id.main_NewEventButton);

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
                //Intent goToNewEvent = new Intent (MainActivity.this,newEvent.class);
                Toast.makeText(MainActivity.this,"Ir para tela de novo evento",Toast.LENGTH_SHORT).show();
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
                //Intent goToProfile = new Intent(this,ProfileActivity.class);
                Toast.makeText(this,"Ir para tela de perfil",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
