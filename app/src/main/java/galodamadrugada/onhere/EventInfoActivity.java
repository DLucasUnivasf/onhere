package galodamadrugada.onhere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

public class EventInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_event);


        TextView infoEventName = (TextView) findViewById(R.id.infoEventName);
        TextView infoEventDate = (TextView) findViewById(R.id.infoEventDate);
        TextView infoStartTime = (TextView) findViewById(R.id.infoStartTime);
        TextView infoDelayAllowed = (TextView) findViewById(R.id.infoDelayAllowed);
        TextView infoDescricao = (TextView) findViewById(R.id.infoDescricao);
        MapView  infoMap = (MapView) findViewById(R.id.infoMap);






    }
}
