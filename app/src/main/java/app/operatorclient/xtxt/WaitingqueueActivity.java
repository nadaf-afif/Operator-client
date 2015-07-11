package app.operatorclient.xtxt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.operatorclient.xtxt.Requestmanager.Customer;
import app.operatorclient.xtxt.Requestmanager.RequestManger;

/**
 * Created by kiran on 11/7/15.
 */
public class WaitingqueueActivity extends Activity {

    ImageButton logout;
    ListView listview;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingqueue);

        prefs = getSharedPreferences(RequestManger.PREFERENCES, Context.MODE_PRIVATE);

        logout = (ImageButton) findViewById(R.id.logout);
        listview = (ListView) findViewById(R.id.listview);


        new GetWaitingQueueAsynctask().execute();

    }

    class GetWaitingQueueAsynctask extends AsyncTask<String, Void, String> implements RequestManger.Constantas {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(WaitingqueueActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String response = "";

            try {

                Map<String, String> map = new HashMap<String, String>();
                String authkey = prefs.getString(AUTHKEY, "");
                map.put(RequestManger.APIKEY, authkey);
                map.put(RequestManger.REQUESTERKEY, RequestManger.REQUESTEROPERATOR);

                response = RequestManger.getHttpRequestWithHeader(map, RequestManger.HOST + "waiting_queue");


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }


            try {
                JSONObject responseJSON = new JSONObject(result);
                boolean error = responseJSON.getBoolean(ERROR);

                if (!error) {
                    JSONObject dataJSON = responseJSON.getJSONObject(DATA);
                    JSONArray customerArray = dataJSON.getJSONArray(MESSAGE);

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Customer>>() {
                    }.getType();

                    List<Customer> customers = gson.fromJson(customerArray.toString(), listType);


                } else {
                    JSONObject dataJSON = responseJSON.getJSONObject(DATA);
                    String message = dataJSON.getString(MESSAGE);
                    Toast.makeText(WaitingqueueActivity.this, message, Toast.LENGTH_LONG).show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(WaitingqueueActivity.this, "Unable to get data.", Toast.LENGTH_LONG).show();
            }

        }

    }

    public class CustomAdapter extends BaseAdapter {

        private ArrayList data;
        private LayoutInflater inflater = null;
        public Resources res;
        int i = 0;


        public CustomAdapter(ArrayList d, Resources resLocal) {

            data = d;
            res = resLocal;

            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public int getCount() {

            if (data.size() <= 0)
                return 1;
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(int position, View convertView, ViewGroup parent) {

            View vi = convertView;
            ViewHolder holder;

            if (convertView == null) {

                vi = inflater.inflate(R.layout.listitem, null);

                holder = new ViewHolder();
                holder.text = (TextView) vi.findViewById(R.id.text);
                vi.setTag(holder);
            } else
                holder = (ViewHolder) vi.getTag();

            if (data.size() <= 0) {
                holder.text.setText("No Data");

            } else {
                tempValues = null;
                tempValues = (ListModel) data.get(position);

                holder.text.setText(tempValues.getCompanyName());

            }
            return vi;
        }

    }


    static class ViewHolder {

        public TextView text;
        public TextView text1;
        public TextView textWide;
        public ImageView image;

    }


}
