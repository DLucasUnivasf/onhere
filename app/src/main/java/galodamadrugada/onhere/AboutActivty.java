package galodamadrugada.onhere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivty extends AppCompatActivity {
    TextView textViewRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_activty);

        textViewRepository = (TextView) findViewById(R.id.textViewRepository);
        textViewRepository.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
