package galodamadrugada.onhere;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import galodamadrugada.onhere.adapter.EventAdapter;
import galodamadrugada.onhere.domain.Event;
import galodamadrugada.onhere.network.CustomRequest;
import galodamadrugada.onhere.network.NetworkConnection;
import galodamadrugada.onhere.util.Consts;
import galodamadrugada.onhere.util.RecyclerTouchListener;

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
        recyclerView            = (RecyclerView) findViewById(R.id.recyclerView);
        eventAdapter            = new EventAdapter(events);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(eventAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Event event = events.get(position);
                Intent intent = new Intent(ProfileActivity.this, EventActivity.class);

                intent.putExtra("name", event.getName());
                intent.putExtra("id",   event.getId());

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareEventData();
    }

    private void prepareEventData() {
        HashMap<String, String> headers = new HashMap<>();
        SharedPreferences preferences = getSharedPreferences(Consts.PREFS_FILE_NAME, MODE_PRIVATE);
        headers.put("x-access-token", preferences.getString("token", ""));

        CustomRequest requestMyEvents = new CustomRequest(Request.Method.GET, Consts.SERVER + Consts.LIST_MY_EVENTS,
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

        CustomRequest requestOthersEvents = new CustomRequest(Request.Method.GET, Consts.SERVER + Consts.LIST_EVENTS,
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
        NetworkConnection.getInstance().addToRequestQueue(requestMyEvents);
        NetworkConnection.getInstance().addToRequestQueue(requestOthersEvents);
    }
}
