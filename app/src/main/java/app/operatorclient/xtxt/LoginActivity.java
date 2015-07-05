package app.operatorclient.xtxt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.operatorclient.xtxt.Requestmanager.RequestManger;
import app.operatorclient.xtxt.Requestmanager.Utils;


public class LoginActivity extends Activity {

    TextView loginTextView;
    EditText usernameEdittext, passwordEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEdittext = (EditText) findViewById(R.id.usernameEdittext);
        passwordEdittext = (EditText) findViewById(R.id.passwordEdittext);
        loginTextView = (TextView) findViewById(R.id.loginTextView);

//        usernameEdittext.setText("absop");
//        passwordEdittext.setText("ranium123");

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEdittext.getText().toString();
                String password = passwordEdittext.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginActivity.this, "Please enter Username.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter Password.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (RequestManger.isConnectedToInternet(LoginActivity.this)) {
                    new LoginAsynctask().execute(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Please check Internet Connection.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    class LoginAsynctask extends AsyncTask<String, Void, String> implements RequestManger.Constantas {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String response = "";

            JSONObject object = new JSONObject();
            try {

                object.put("username", params[0]);
                object.put("password", params[1]);
                object.put("app_version", Utils.getAppVer(LoginActivity.this));
                object.put("android_version", Utils.getAndroidVer());

                response = RequestManger.postHttpRequestWithHeader(object, RequestManger.HOST + "login");


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject responseJSON = new JSONObject(result);
                boolean error = responseJSON.getBoolean(ERROR);

                if (!error) {
                    JSONObject dataJSON = responseJSON.getJSONObject(DATA);

                    String uid = dataJSON.getString(USERID);
                    String sessionid = dataJSON.getString(SESSIONID);
                    String name = dataJSON.getString(NAME);
                    String mod = dataJSON.getString(MESSAGEOFTHEDAY);

                    SharedPreferences.Editor editor = getSharedPreferences(RequestManger.PREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putString(USERID, uid);
                    editor.putString(SESSIONID, sessionid);
                    editor.putString(NAME, name);
                    editor.putString(MESSAGEOFTHEDAY, mod);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    JSONObject dataJSON = responseJSON.getJSONObject(DATA);
                    String message = dataJSON.getString("message");
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(LoginActivity.this, "Unable to Login", Toast.LENGTH_LONG).show();
            }
        }

    }

}
