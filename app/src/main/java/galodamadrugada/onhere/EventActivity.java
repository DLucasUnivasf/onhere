package galodamadrugada.onhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by UNIVASF on 14/03/2017.
 */

public class EventActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView idTextView;
    private ListView listPresence;
    private JSONArray participants;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        nameTextView    = (TextView) findViewById(R.id.nameTextView);
        idTextView      = (TextView) findViewById(R.id.idTextView);
        listPresence    = (ListView) findViewById(R.id.list_presence);


        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("participants");
        nameTextView.setText(getIntent().getExtras().getString("name"));
        idTextView.setText(getIntent().getExtras().getString("id"));

        try {
            participants = new JSONArray(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
