package galodamadrugada.onhere;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.service.voice.VoiceInteractionSession;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import galodamadrugada.onhere.R;
import galodamadrugada.onhere.domain.Event;
import galodamadrugada.onhere.fragments.EventFragment;
import galodamadrugada.onhere.network.CustomRequest;
import galodamadrugada.onhere.util.Consts;


/**
 * Created by UNIVASF on 30/03/2017.
 */


public class ListEventActivity extends AppCompatActivity{


    /**
    public class Message extends android.os.Message{
        public String       nome;
        public String       descricao;
        public String       dtin;
        public String       dtfim;
        public String       tolerancia;
        public Localizacao  localizacao;

        Message(String novoNome, String novoDescricao, String novoDtin, String novoDtfim, String novoTolerancia, Localizacao novoLocalizacao){
            this.nome           = novoNome;
            this.descricao      = novoDescricao;
            this.dtin           = novoDtin;
            this.dtfim          = novoDtfim;
            this.tolerancia     = novoTolerancia;
            this.localizacao    = novoLocalizacao;
        }
    }

    public class Localizacao{
        public String lat;
        public String lon;

        Localizacao(String novoLat, String novoLon){
            this.lat    = novoLat;
            this.lon    = novoLon;
        }
    }
     **/

    private Map<String, String> params;
    private RequestQueue event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);

        params  =   null;
        event   =   Volley.newRequestQueue(ListEventActivity.this);

        // FRAGMENT
        EventFragment frag = (EventFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        if(frag == null) {
            frag = new EventFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "mainFrag");
            ft.commit();
        }
    }

    public List<Event> getSetEventList(int qtd){

        final String[]    nomeEvento = new String[]{};
        String[]    dataEvento = new String[]{};
        List<Event> listAux = new ArrayList<>();


        CustomRequest customRequest = new CustomRequest( Request.Method.GET,
                Consts.SERVER + Consts.LIST_EVENTS,
                params,
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse( JSONObject response){
                        Log.i("CustomRequest", "SUCESSO" + response.toString());

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(ListEventActivity.this, "Erro" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } );

        customRequest.setTag("tag");
        event.add(customRequest);



        for(int i = 0; i < qtd; i++){
            Event c = new Event( nomeEvento[i % nomeEvento.length], dataEvento[ i % dataEvento.length ] );
            listAux.add(c);
        }
        return(listAux);
    }
    /**
    public List<Message> readMessagesArray(JsonReader reader) throws IOException {
        List<Message> messages = new ArrayList<Message>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }


    public Message readMessage( JsonReader reader ) throws IOException {
        String          nome        =   null;
        String          descricao   =   null;
        String          dtin        =   null;
        String          dtfim       =   null;
        String          tolerancia  =   null;
        Localizacao     localizacao =   null;

        reader.beginObject();

        while(reader.hasNext()){
            String name =   reader.nextName();
            if (name.equals("nome")){
                nome        = reader.nextString();
            }
            else if (name.equals("descricao")){
                descricao   = reader.nextString();
            }
            else if (name.equals("dtin")){
                dtin        = reader.nextString();
            }
            else if (name.equals("dtfim")){
                dtfim       = reader.nextString();
            }
            else if(name.equals("tolerancia")){
                tolerancia  = reader.nextString();
            }
            else if(name.equals("localizacao")){
                localizacao = readLocalizacao(reader);
            }else{
                reader.skipValue();
            }
        }

        reader.endObject();
        return new Message(nome, descricao, dtin, dtfim, tolerancia, localizacao);
    }

    public Localizacao readLocalizacao(JsonReader reader) throws IOException {
        String lat = null;
        String lon = null;

        reader.beginObject();

        while (reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("lat")){
                lat = reader.nextString();
            }
            else if(name.equals("long")){
                lon = reader.nextString();
            }else {
                reader.skipValue();
            }
        }

        reader.endObject();
        return new Localizacao(lat, lon);
    }
     **/
}


