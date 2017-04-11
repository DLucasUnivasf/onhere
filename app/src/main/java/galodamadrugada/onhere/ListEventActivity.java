package galodamadrugada.onhere;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import galodamadrugada.onhere.R;
import galodamadrugada.onhere.domain.Event;
import galodamadrugada.onhere.fragments.EventFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import galodamadrugada.onhere.network.CustomRequest;
import galodamadrugada.onhere.network.NetworkConnection;
import galodamadrugada.onhere.util.Consts;

/**
 * Created by UNIVASF on 30/03/2017.
 */


public class ListEventActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);



        // FRAGMENT
        EventFragment frag = (EventFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        if (frag == null) {
            frag = new EventFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "mainFrag");
            ft.commit();
        }


    }
        public List<Event> getSetEventList(final int qtd){
            final String[] nomeEvento = new String[]{};
            final String[] dataEvento = new String[]{};
            final List<Event> listAux = new ArrayList<>();
            HashMap<String, String> headers = new HashMap<>();
            final SharedPreferences preferences = getSharedPreferences(Consts.PREFS_FILE_NAME, MODE_PRIVATE);

            headers.put("x-access-token", preferences.getString("token", ""));


            CustomRequest customRequest = new CustomRequest(
                    Request.Method.GET,
                    Consts.SERVER + Consts.LIST_EVENTS + Consts.CHECK_TOKEN,
                    null,
                    headers,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("CustomRequest", "SUCESSO:" + response.toString());
                            try {
                                for(int i = 0; i < response.length(); i++){
                                    nomeEvento[i]   =   response.getString("nome");
                                    dataEvento[i]   =   response.getString("data");
                                }

                                for(int i = 0; i < qtd; i++){
                                    Event event = new Event( nomeEvento[i % nomeEvento.length], dataEvento[ i % dataEvento.length ] );
                                    listAux.add(event);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                }}},
                    new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("CustomRequest", "ERRO NO REQUEST " + error.getMessage());
                            }
                    });

         return(listAux);
        }

}

