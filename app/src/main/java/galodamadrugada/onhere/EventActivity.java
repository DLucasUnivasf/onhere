package galodamadrugada.onhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by UNIVASF on 14/03/2017.
 */

public class EventActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView idTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        idTextView   = (TextView) findViewById(R.id.idTextView);

        nameTextView.setText(getIntent().getExtras().getString("name"));
        idTextView.setText(getIntent().getExtras().getString("id"));
    }
}
