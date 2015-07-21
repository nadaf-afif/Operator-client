package app.operatorclient.xtxt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import app.operatorclient.xtxt.Requestmanager.CircularImageView;
import app.operatorclient.xtxt.Requestmanager.LogoutAsynctask;
import app.operatorclient.xtxt.Requestmanager.RequestManger;

/**
 * Created by kiran on 21/7/15.
 */
public class ChatScreenActivity extends Activity {

    ImageButton logout, changePersona, media, send;
    ListView listview;
    CircularImageView customerPic, personaPic;
    TextView customerName, personaName;
    EditText messageEdittext;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatscreen);

        prefs = getSharedPreferences(RequestManger.PREFERENCES, Context.MODE_PRIVATE);

        logout = (ImageButton) findViewById(R.id.logout);
        changePersona = (ImageButton) findViewById(R.id.changePersona);
        customerPic = (CircularImageView) findViewById(R.id.customerPic);
        personaPic = (CircularImageView) findViewById(R.id.personaPic);
        customerName = (TextView) findViewById(R.id.customerName);
        personaName = (TextView) findViewById(R.id.personaName);
        listview = (ListView) findViewById(R.id.listview);

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

    }
}
