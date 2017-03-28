package galodamadrugada.onhere.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.android.gms.common.stats.StatsEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.io.UnsupportedEncodingException;

/**
 * Created by ProjetoMoscamed on 22/03/2017.
 */

public class CustomRequest extends Request<JSONObject> {

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;
    private Map<String, String> headers;

    public CustomRequest(int method, String url, Map<String, String> params, Map<String, String> headers,
                         Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        HashMap<String, String> aux = new HashMap<>();
        this.listener = responseListener;
        if(params == null)
            this.params = aux;
        else
            this.params = params;
        if(headers == null)
            this.headers  = aux;
        else
            this.headers = headers;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
}
