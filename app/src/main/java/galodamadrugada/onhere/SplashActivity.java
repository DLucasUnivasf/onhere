package galodamadrugada.onhere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import galodamadrugada.onhere.network.CustomRequest;
import galodamadrugada.onhere.network.NetworkConnection;
import galodamadrugada.onhere.util.Consts;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String UNIQUE_ID = "UNIQUE_ID";
        String uniqueId;

        final SharedPreferences preferences = getSharedPreferences(Consts.PREFS_FILE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean("first_time", true)) {
            Log.d("SharedPreferences", "Primeiro boot");

            uniqueId = UUID.randomUUID().toString();

            preferences.edit().putString(UNIQUE_ID, uniqueId).apply();

            if(!uniqueId.equals(""))
                preferences.edit().putBoolean("first_time", false).apply();

            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            if(!preferences.getString("token", "").equals("")) {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("x-access-token", preferences.getString("token", ""));

                CustomRequest customRequest = new CustomRequest(Request.Method.GET, Consts.SERVER + Consts.CHECK_TOKEN, null, headers,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.i("SUCESSO", response.toString());
                                    if (response.getString("status").equals("666")) {
                                        goToMain();
                                    }
                                    else {
                                        goToLogin();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("CustomRequest", "ERRO NO REQUEST " + error.getMessage());
                                Context context = getApplicationContext();
                                CharSequence text = getResources().getString(R.string.server_error);
                                int duration = Toast.LENGTH_LONG;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                goToLogin();
                            }
                        }
                );
                if(NetworkConnection.getInstance().isOnline()) {
                    NetworkConnection.getInstance().addToRequestQueue(customRequest);
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = getResources().getString(R.string.network_not_conected);
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    goToLogin();
                }
            }
            else {
                goToLogin();
            }
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
