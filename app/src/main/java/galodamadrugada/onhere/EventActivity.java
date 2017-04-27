package galodamadrugada.onhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by UNIVASF on 14/03/2017.
 */

public class EventActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView idTextView;
    private ListView listPresence;
    private JSONArray jsonArray;

    private ArrayList<String>   participants = new ArrayList<>();
    private ArrayAdapter<String>    adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        nameTextView    = (TextView) findViewById(R.id.nameTextView);
        idTextView      = (TextView) findViewById(R.id.idTextView);
        listPresence    = (ListView) findViewById(R.id.list_presence);


        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("participants");
        nameTextView.setText(getIntent().getExtras().getString("name"));
        idTextView.setText(getIntent().getExtras().getString("id"));

        Log.i("EventActivity", jsonString);

        try {
            jsonArray = new JSONArray(jsonString);
            Log.i("EventActivity", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < jsonArray.length(); i++){
            try {
                participants.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new ArrayAdapter<>(this, R.layout.participants_list_row, R.id.textViewParticipants, participants);

        listPresence.setAdapter(adapter);

    }
}
