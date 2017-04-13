package galodamadrugada.onhere;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionService;

import galodamadrugada.onhere.adapter.EventAdapter;
import galodamadrugada.onhere.domain.Event;
import galodamadrugada.onhere.network.CustomRequest;
import galodamadrugada.onhere.network.NetworkConnection;
import galodamadrugada.onhere.util.Consts;

/**
 * Created by UNIVASF on 14/03/2017.
 */

public class ProfileActivity  extends AppCompatActivity {
    TextView        textViewInformation;
    TextView        textViewName;
    TextView        textViewEmail;
    ImageButton     imageButtonEditProfile;
    RecyclerView    recyclerView;
    EventAdapter    eventAdapter;
    List<Event>     events = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewInformation     = (TextView)     findViewById(R.id.textViewInformation);
        textViewName            = (TextView)     findViewById(R.id.textViewName);
        textViewEmail           = (TextView)     findViewById(R.id.textViewEmail);
        imageButtonEditProfile  = (ImageButton)  findViewById(R.id.imageButtonEditProfile);
        recyclerView            = (RecyclerView) findViewById(R.id.recyclreView);
        eventAdapter            = new EventAdapter(events);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(eventAdapter);

        prepareEventData();
    }

    private void prepareEventData() {
        HashMap<String, String> headers = new HashMap<>();
        SharedPreferences preferences = getSharedPreferences(Consts.PREFS_FILE_NAME, MODE_PRIVATE);
        headers.put("x-access-token", preferences.getString("token", ""));

        CustomRequest request = new CustomRequest(Request.Method.GET, Consts.SERVER + Consts.LIST_MY_EVENTS,
                null, headers,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = new JSONArray();

                        Log.i("CustomRequest", "response: " + response.toString());

                        try {
                            jsonArray = response.getJSONArray("eventos");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for(int i = 0; i < jsonArray.length(); i++) {
                            Event event = new Event();

                            try {
                                event.setName(jsonArray.getJSONObject(i).getString("nome"));
                                event.setId(jsonArray.getJSONObject(i).getString("chave"));
                                event.setDescription(jsonArray.getJSONObject(i).getString("descricao"));

                                events.add(event);

                                eventAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        NetworkConnection.getInstance().addToRequestQueue(request);

    }
}
