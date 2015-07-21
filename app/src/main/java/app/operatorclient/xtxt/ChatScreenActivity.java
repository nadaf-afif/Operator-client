package app.operatorclient.xtxt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import app.operatorclient.xtxt.Requestmanager.CircularImageView;
import app.operatorclient.xtxt.Requestmanager.LogoutAsynctask;
import app.operatorclient.xtxt.Requestmanager.RequestManger;
import app.operatorclient.xtxt.Requestmanager.Utils;

/**
 * Created by kiran on 21/7/15.
 */
public class ChatScreenActivity extends Activity implements RequestManger.Constantas {

    ImageButton logout, changePersona, media, send;
    ListView listview;
    CircularImageView customerPic, personaPic;
    TextView customerName, personaName;
    EditText messageEdittext;
    LinearLayout sendLinearlayout;
    SharedPreferences prefs;
    JSONObject dataJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatscreen);

        prefs = getSharedPreferences(RequestManger.PREFERENCES, Context.MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String data = extras.getString(DATA);
            try {
                dataJSON = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        logout = (ImageButton) findViewById(R.id.logout);
        changePersona = (ImageButton) findViewById(R.id.changePersona);
        customerPic = (CircularImageView) findViewById(R.id.customerPic);
        personaPic = (CircularImageView) findViewById(R.id.personaPic);
        customerName = (TextView) findViewById(R.id.customerName);
        personaName = (TextView) findViewById(R.id.personaName);
        listview = (ListView) findViewById(R.id.listview);
        sendLinearlayout = (LinearLayout) findViewById(R.id.sendLinearlayout);

        media = (ImageButton) findViewById(R.id.media);
        send = (ImageButton) findViewById(R.id.send);
        messageEdittext = (EditText) findViewById(R.id.messageEdittext);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatScreenActivity.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new LogoutAsynctask(ChatScreenActivity.this).execute();
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

        try {
            JSONObject customerJSON = dataJSON.getJSONObject(CUSTOMER);
            JSONObject personaJSON = dataJSON.getJSONObject(PERSONA);

            customerName.setText(customerJSON.getString(NAME));
            personaName.setText(personaJSON.getString(NAME));

            Picasso.with(ChatScreenActivity.this)
                    .load(customerJSON.getString(PROFILEPIC))
                    .placeholder(R.drawable.profilepic)
                    .into(customerPic);

            Picasso.with(ChatScreenActivity.this)
                    .load(personaJSON.getString(PROFILEPIC))
                    .placeholder(R.drawable.profilepic)
                    .into(personaPic);

            boolean isSendSMS = dataJSON.getBoolean(SENDSMS);
            if (!isSendSMS) {
                sendLinearlayout.setVisibility(View.GONE);
            }

            String replyop = dataJSON.getString(REPLTOP);
            if (!replyop.equalsIgnoreCase("null")) {
                sendLinearlayout.setVisibility(View.GONE);

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatScreenActivity.this);
                builder.setMessage(replyop + " is already replying.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

            try {
                String customerDOB = customerJSON.getString(DOB);

                Date date = Utils.getDOBDate(customerDOB);
                Date currentDate = new Date();

                Calendar calDOB = Calendar.getInstance();
                calDOB.setTime(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentDate);

                int diff = cal.get(Calendar.YEAR) - calDOB.get(Calendar.YEAR);
                if (diff < 18) {
                    media.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                media.setVisibility(View.GONE);
            }

            String color = customerJSON.getString(COLOR);
            listview.setBackgroundColor(Color.parseColor("#" + color));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
