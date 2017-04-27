package galodamadrugada.onhere;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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


/**
 * Created by UNIVASF on 14/03/2017.
 */

public class ProfileActivity  extends AppCompatActivity implements EventAdapter.EventAdapterListener {
    TextView            textViewInformation;
    TextView            textViewName;
    RecyclerView        recyclerView;
    EventAdapter        eventAdapter;
    List<Event>         events = new ArrayList<>();
    SharedPreferences   preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        textViewInformation     = (TextView)        findViewById(R.id.textViewInformation);
        textViewName            = (TextView)        findViewById(R.id.textViewName);
        recyclerView            = (RecyclerView)    findViewById(R.id.recyclerView);
        preferences             = getSharedPreferences(Consts.PREFS_FILE_NAME, MODE_PRIVATE);
        eventAdapter            = new EventAdapter(this, events, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(eventAdapter);

        textViewName.setText(preferences.getString("fullname", ""));

        prepareEventData();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile_logout:
                AlertDialog.Builder builder =   new AlertDialog.Builder(ProfileActivity.this);

                builder.setMessage(R.string.confirmation_exit_auccont)
                        .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent logoutIntent = new Intent(ProfileActivity.this, LoginActivity.class);

                                preferences.edit().putString("token", "").apply();

                                startActivity(logoutIntent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();
                break;

        }
        return super.onOptionsItemSelected(item);
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
                                if(jsonArray.getJSONObject(i) != null) {
                                    event.setName(jsonArray.getJSONObject(i).getString("nome"));
                                    event.setId(jsonArray.getJSONObject(i).getString("chave"));
                                    event.setParticipants((jsonArray.getJSONObject(i).getJSONArray("participantes") == null) ? new JSONArray() : jsonArray.getJSONObject(i).getJSONArray("participantes"));
                                    events.add(event);

                                    eventAdapter.notifyDataSetChanged();
                                }
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
                                if(jsonArray.getJSONObject(i) != null) {
                                    event.setName(jsonArray.getJSONObject(i).getString("nome"));
                                    event.setId(jsonArray.getJSONObject(i).getString("chave"));
                                    event.setParticipants((jsonArray.getJSONObject(i).getJSONArray("participantes") == null) ? new JSONArray() : jsonArray.getJSONObject(i).getJSONArray("participantes"));
                                    events.add(event);

                                    eventAdapter.notifyDataSetChanged();
                                }
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

    @Override
    public void onEventAdapterRowClicked(int position) {
        Event event = events.get(position);
        Intent intent = new Intent(ProfileActivity.this, EventActivity.class);

        intent.putExtra("name", event.getName());
        intent.putExtra("id",   event.getId());
        intent.putExtra("participants", event.getParticipants().toString());

        Log.i("ProfileActivity", "Enviando: " + event.getParticipants().toString());

        startActivity(intent);
    }

    @Override
    public void onEventDelete(final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

        builder.setMessage(R.string.confirmation_delete_event)
               .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        HashMap<String, String> headers = new HashMap<>();
                        SharedPreferences preferences = getSharedPreferences(Consts.PREFS_FILE_NAME, MODE_PRIVATE);
                        headers.put("x-access-token", preferences.getString("token", ""));

                        CustomRequest deleteMyEvent = new CustomRequest(Request.Method.GET, Consts.SERVER + Consts.DELETE_EVENT + "?chave=" + events.get(position).getId(),
                                null, headers,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        Log.i("CustomRequest", "response: " + response.toString());

                                        try {
                                            if (response.getString("status").equals("423")){
                                                events.remove(position);

                                                Context context = getApplicationContext();
                                                CharSequence text = "Evento deletado com sucesso!";
                                                int duration = Toast.LENGTH_LONG;

                                                Toast toast = Toast.makeText(context, text, duration);
                                                toast.show();
                                                Log.i("onEventDelete", "toast");

                                                eventAdapter.notifyDataSetChanged();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        NetworkConnection.getInstance().addToRequestQueue(deleteMyEvent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();

    }


}
