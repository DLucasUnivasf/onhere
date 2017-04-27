package galodamadrugada.onhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by UNIVASF on 14/03/2017.
 */

public class EventActivity extends AppCompatActivity {
    private TextView            eventTextView;
    private TextView            descriptionTextView;
    private TextView            idTextView;
    private TextView            dateBeginTextView;
    private TextView            dateEndTextView;
    private TextView            hourBeginTextView;
    private TextView            hourEndTextView;
    private ListView            listPresence;
    private JSONArray           jsonArray;


    private ArrayList<String>   participants = new ArrayList<>();
    private ArrayAdapter<String>    adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        eventTextView       = (TextView) findViewById(R.id.eventTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        idTextView          = (TextView) findViewById(R.id.idTextView);
        dateBeginTextView   = (TextView) findViewById(R.id.dateBeginTextView);
        dateEndTextView     = (TextView) findViewById(R.id.dateEndTextView);
        hourBeginTextView   = (TextView) findViewById(R.id.hourBeginTextView);
        hourEndTextView     = (TextView) findViewById(R.id.hourEndTextView);
        listPresence        = (ListView) findViewById(R.id.list_presence);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("participants");
        eventTextView.setText(getIntent().getExtras().getString("name"));
        idTextView.setText(getIntent().getExtras().getString("id"));
        descriptionTextView.setText(getIntent().getExtras().getString("description"));

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'", Locale.US);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat hourFormatter = new SimpleDateFormat("hh:mm", Locale.US);

        try {
            Date initDate = parser.parse(getIntent().getExtras().getString("initDate"));
            dateBeginTextView.setText(dateFormatter.format(initDate));
            hourBeginTextView.setText(hourFormatter.format(initDate));
        }catch(ParseException e) {
            e.printStackTrace();
        }

        try {
            Date endDate = parser.parse(getIntent().getExtras().getString("endDate"));
            dateEndTextView.setText(dateFormatter.format(endDate));
            hourEndTextView.setText(hourFormatter.format(endDate));
        }catch(ParseException e) {
            e.printStackTrace();
        }


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
