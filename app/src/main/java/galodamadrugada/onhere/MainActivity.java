package galodamadrugada.onhere;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import galodamadrugada.onhere.network.CustomRequest;
import galodamadrugada.onhere.network.NetworkConnection;
import galodamadrugada.onhere.util.Consts;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    ProgressDialog progressDialog;
    String dialogTitle, dialogMessage;
    final String dialogButtonText = "Ok";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button checkInButton = (Button) findViewById(R.id.mainCheckInButton);
        FloatingActionButton newEventButton = (FloatingActionButton) findViewById(R.id.newEventButton);
        WebView descriptionText = (WebView) findViewById(R.id.mainDescription);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }

        /*Aparentemente textView não justifica texto, por isso o webView ... :-P */

        String text = "<html><body>"
                + "<p align=\"center\">"
                + "Com <b>OnHere</b> voc&ecirc; controla seus eventos!</br>"
                + "</p>"
                + "<p align=\"justify\">"
                + "Clique no bot&atilde;o <strong>CHECK IN</strong> para registrar "
                + "sua presen&ccedil;a em algum evento.</br>"
                + "</p> "
                + "</body></html>";
        descriptionText.loadData(text, "text/html", "ISO-8859-1");


        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,R.string.required_location_permission,Toast.LENGTH_SHORT).show();
                }else {

                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        View view = inflater.inflate(R.layout.dialog_check_in_code, null);

                        final EditText checkinEventCode = (EditText) view.findViewById(R.id.textCheckInCode);
                        final SharedPreferences preferences = getSharedPreferences(Consts.PREFS_FILE_NAME, MODE_PRIVATE);

                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setCancelable(false);

               /* TextView title = new TextView(MainActivity.this);
                title.setText("CHECK IN no evento");
                title.setBackgroundColor(Color.parseColor("#3f51b5"));
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(20);*/

                builder.setView(view).setTitle(getResources().getString(R.string.check_in))
                            .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });

                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (checkinEventCode.getText().toString().equals("")) {
                                    checkinEventCode.requestFocus();
                                    checkinEventCode.setError(getResources().getString((R.string.required_field)));
                                } else {
                                    String eventCode = checkinEventCode.getText().toString();
                                    if (eventCode.length() != Consts.EVENT_CODE_LENGHT) {
                                        checkinEventCode.setError(getResources().getString((R.string.invalid_code_length)));
                                    } else {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                        if (NetworkConnection.getInstance().isOnline()) {
                                            if (mLastLocation != null) {
                                                HashMap<String, String> header = new HashMap<>();
                                                header.put("x-access-token", preferences.getString("token", ""));

                                                Log.i("Token --> :", preferences.getString("token", ""));
                                                Log.i("Latitude --> :", String.valueOf(mLastLocation.getLatitude()));
                                                Log.i("Longitude --> :", String.valueOf(mLastLocation.getLongitude()));
                                                Log.i("EvenCode --> :", eventCode);


                                                progressDialog.setMessage("Fazendo Checkin...");
                                                progressDialog.show();

                                                String urlRequest = Consts.SERVER + Consts.CHECK_IN + "?chave=" + eventCode + "&latitude=" + String.valueOf(mLastLocation.getLatitude()) + "&longitude=" + String.valueOf(mLastLocation.getLongitude());

                                                CustomRequest customRequest = new CustomRequest(Request.Method.GET, urlRequest , null, header,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response) {
                                                                Log.i("CustomRequestCHECKIN", "SUCCESSO: " + response.toString());

                                                                try {
                                                                    switch (response.getString("status")) {
                                                                        case Consts.TOKEN_NOT_FOUND:
                                                                            progressDialog.hide();
                                                                            dialogTitle = getResources().getString(R.string.id_error);
                                                                            dialogMessage = getResources().getString(R.string.token_error);
                                                                            builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                }
                                                                            });
                                                                            break;
                                                                        case Consts.INVALID_TOKEN:
                                                                            progressDialog.hide();
                                                                            dialogTitle = getResources().getString(R.string.id_error);
                                                                            dialogMessage = getResources().getString(R.string.token_error);
                                                                            builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                }
                                                                            });
                                                                            break;
                                                                        case Consts.TOKEN_USER_NOT_FOUND:
                                                                            progressDialog.hide();
                                                                            dialogTitle = getResources().getString(R.string.id_error);
                                                                            dialogMessage = getResources().getString(R.string.token_error);
                                                                            builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                }
                                                                            });
                                                                            break;
                                                                        case Consts.EVENT_SEARCHING_ERROR:
                                                                            progressDialog.hide();
                                                                            dialogTitle = getResources().getString(R.string.event_not_found);
                                                                            dialogMessage = getResources().getString(R.string.event_not_found_message);
                                                                            builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                }
                                                                            });
                                                                            break;
                                                                        case Consts.EVENT_ENTER_ERROR:
                                                                            progressDialog.hide();
                                                                            dialogTitle = getResources().getString(R.string.check_in_error);
                                                                            dialogMessage = getResources().getString(R.string.server_error);
                                                                            builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                }
                                                                            });
                                                                            break;

                                                                        case Consts.EVENT_TIME_TOLERANCE_ERROR:
                                                                            progressDialog.hide();
                                                                            dialogTitle = getResources().getString(R.string.event_time_tolerance_error);
                                                                            dialogMessage = getResources().getString(R.string.event_time_tolerance);
                                                                            builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                }
                                                                            });
                                                                            break;

                                                                        case Consts.EVENT_MAX_DISTANCE_ERROR:
                                                                            progressDialog.hide();
                                                                            dialogTitle = getResources().getString(R.string.event_max_distance_error);
                                                                            dialogMessage = getResources().getString(R.string.event_max_distance);
                                                                            builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                }
                                                                            });
                                                                            break;


                                                                        case Consts.EVENT_ENTER_SUCCESS:
                                                                            progressDialog.hide();
                                                                            dialogTitle = getResources().getString(R.string.check_in_sucess);
                                                                            dialogMessage = getResources().getString(R.string.check_in_sucess_message);
                                                                            builder.setPositiveButton(R.string.view, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    Intent goToPerfil = new Intent(MainActivity.this, ProfileActivity.class);
                                                                                    startActivity(goToPerfil);
                                                                                    finish();
                                                                                }
                                                                            });
                                                                            break;

                                                                        default:
                                                                            progressDialog.hide();
                                                                            dialogMessage = getResources().getString(R.string.server_error);
                                                                            builder.setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                }
                                                                            });
                                                                            break;
                                                                    }
                                                                    builder.setTitle(dialogTitle).setMessage(dialogMessage);
                                                                    AlertDialog dialog = builder.create();
                                                                    dialog.show();
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.i("CustomRequestCHECKIN", "ERRO NO REQUEST " + error.getMessage());
                                                        progressDialog.hide();
                                                        builder.setMessage(R.string.request_error).setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                            }
                                                        });
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                });
                                                NetworkConnection.getInstance().addToRequestQueue(customRequest);
                                            } else {

                                                builder.setMessage(R.string.checkin_localization).setTitle(R.string.checkin_localization_error)
                                                .setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                    }
                                                });
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }
                                        } else {
                                            builder.setMessage(R.string.network_not_conected).setPositiveButton(dialogButtonText, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            });
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                    }
                                }
                            }
                        });
                   }
                }
            });
        newEventButton.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                Intent goToNewEvent = new Intent(MainActivity.this, EventRegisterActivity.class);
                startActivity(goToNewEvent);
            }
            });
        }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_goToProfile:
                Intent goToProfile = new Intent(this, ProfileActivity.class);
                startActivity(goToProfile);
                break;

            case R.id.main_menu_goToAbout:
                Intent goToAbout = new Intent(this, AboutActivty.class);
                startActivity(goToAbout);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }}}



