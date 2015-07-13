package app.operatorclient.xtxt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.operatorclient.xtxt.Requestmanager.Customer;
import app.operatorclient.xtxt.Requestmanager.LogoutAsynctask;
import app.operatorclient.xtxt.Requestmanager.RequestManger;
import app.operatorclient.xtxt.Requestmanager.Utils;

/**
 * Created by kiran on 11/7/15.
 */
public class WaitingqueueActivity extends Activity {

    ImageButton logout;
    ListView listview;
    CustomAdapter adapter;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingqueue);

        prefs = getSharedPreferences(RequestManger.PREFERENCES, Context.MODE_PRIVATE);

        logout = (ImageButton) findViewById(R.id.logout);
        listview = (ListView) findViewById(R.id.listview);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WaitingqueueActivity.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new LogoutAsynctask(WaitingqueueActivity.this).execute();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

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
                    String currenttime = dataJSON.getString(CURRENTTIME);

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Customer>>() {
                    }.getType();

                    List<Customer> customers = gson.fromJson(customerArray.toString(), listType);

                    adapter = new CustomAdapter(customers, currenttime);
                    listview.setAdapter(adapter);


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

        private List<Customer> data;
        private LayoutInflater inflater = null;
        String currenttime;

        public CustomAdapter(List<Customer> data, String currenttime) {

            this.data = data;
            this.currenttime = currenttime;
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public int getCount() {

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
                holder.nameTextView = (TextView) vi.findViewById(R.id.name);
                holder.timeTextView = (TextView) vi.findViewById(R.id.time);

                vi.setTag(holder);
            } else
                holder = (ViewHolder) vi.getTag();


            Customer customer = data.get(position);

            holder.nameTextView.setText(customer.getCustomer_name());
            holder.timeTextView.setText(Utils.dateDiff(currenttime, customer.getCreated()));

            return vi;
        }

    }


    static class ViewHolder {

        public TextView nameTextView, timeTextView;

    }


}
