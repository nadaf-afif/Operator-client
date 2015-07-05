package app.operatorclient.xtxt.Requestmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * Created by mac on 04/07/15.
 */
public class RequestManger {

    public static final String HOST = "http://stage.operator-api.xtxt.co/api/v1";

    public static final String APIKEY = "Authorization";
    public static final String APIKEYVALUE = "xTxt123456";
    public static final String REQUESTERKEY = "requester";
    public static final String REQUESTERVALUE = "xtxt-app-global";

    public static String postHttpRequestWithHeader(List<NameValuePair> pairs, String url) {

        String responseString = null;

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(pairs, "UTF8"));
            post.setHeader(APIKEY, APIKEYVALUE);
            post.setHeader(REQUESTERKEY, REQUESTERVALUE);
            post.setHeader("Content-type", "application/json");

            HttpResponse response = client.execute(post);
            int statuscode = response.getStatusLine().getStatusCode();

            responseString = EntityUtils.toString(response.getEntity());


        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("RESPONSE GET", responseString + " ");
        return responseString;

    }


    public static boolean isConnectedToInternet(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable() && ni.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
