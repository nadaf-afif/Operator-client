package app.operatorclient.xtxt;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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


                new LoginAsynctask().execute(username, password);

//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

    }

    class LoginAsynctask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String response;

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", params[0]));
            pairs.add(new BasicNameValuePair("password", params[1]));
            pairs.add(new BasicNameValuePair("app_version", Utils.getAppVer(LoginActivity.this)));
            pairs.add(new BasicNameValuePair("android_version", Utils.getAndroidVer()));

            response = RequestManger.postHttpRequestWithHeader(pairs, RequestManger.HOST + "login");

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }

}
