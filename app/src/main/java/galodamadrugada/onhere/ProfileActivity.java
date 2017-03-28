package galodamadrugada.onhere;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by UNIVASF on 14/03/2017.
 */

public class ProfileActivity  extends AppCompatActivity{


    TextView        textViewInformation;
    TextView        textViewName;
    TextView        textViewEmail;
    ImageButton     imageButtonEditProfile;

    RecyclerView                mRecyclerView;
    RecyclerView.Adapter        mAdapter;
    RecyclerView.LayoutManager  mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewInformation     = (TextView) findViewById(R.id.textViewInformation);
        textViewName            = (TextView) findViewById(R.id.textViewName);
        textViewEmail           = (TextView) findViewById(R.id.textViewEmail);
        imageButtonEditProfile  = (ImageButton) findViewById(R.id.imageButtonEditProfile);

        mRecyclerView   = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use essa configuração para melhorar o desempenho se souber que as
        // alterações no conteúdo não alteram o tamanho do layout do RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // usar um linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        //mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

}
