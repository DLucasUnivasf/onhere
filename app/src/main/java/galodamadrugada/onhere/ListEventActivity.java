package galodamadrugada.onhere;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import galodamadrugada.onhere.R;
import galodamadrugada.onhere.domain.Event;
import galodamadrugada.onhere.fragments.EventFragment;


/**
 * Created by UNIVASF on 30/03/2017.
 */


public class ListEventActivity extends AppCompatActivity{

    private static String TAG = "LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);

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
        String[] nome = new String[]{"Gallardo", "Vyron", "Corvette", "Pagani Zonda", "Porsche 911 Carrera", "BMW 720i", "DB77", "Mustang", "Camaro", "CT6"};
        String[] data = new String[]{"Lamborghini", " bugatti", "Chevrolet", "Pagani", "Porsche", "BMW", "Aston Martin", "Ford", "Chevrolet", "Cadillac"};
        List<Event> listAux = new ArrayList<>();

        for(int i = 0; i < qtd; i++){
            Event c = new Event( nome[i % nome.length], data[ i % data.length ] );
            listAux.add(c);
        }
        return(listAux);
    }
}
