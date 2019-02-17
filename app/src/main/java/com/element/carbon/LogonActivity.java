package com.element.carbon;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

public class LogonActivity extends AppCompatActivity {

    TextView mTextView;
    Button mButton;
    RequestQueue requestQueue;
    TextInputLayout nameField, emailField, phoneField;
    ProgressBar logOnProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        final String url = "http://192.168.43.113:3000/postCustomer";

        mTextView = (TextView) findViewById(R.id.progressStatus);
        mButton = (Button) findViewById(R.id.submit);
        logOnProgress = (ProgressBar) findViewById(R.id.LogOnProgress);
        nameField = (TextInputLayout) findViewById(R.id.nameLayout);
        emailField = (TextInputLayout) findViewById(R.id.emailLayout);
        phoneField = (TextInputLayout) findViewById(R.id.phoneLayout);

        /*try {
            JSONObject data = new JSONObject((String) CacheHandler.getFile("data.cache",getFilesDir()));
            nameField.getEditText().setText(data.getString("name"));
            emailField.getEditText().setText(data.getString("email"));
            phoneField.getEditText().setText(data.getString("phone"));
            mTextView.setText(data.getString("sessionID"));
        } catch (Exception e) {
            mTextView.setText(e.toString());
        }*/
        logOnProgress.setVisibility(View.INVISIBLE);

        requestQueue = Volley.newRequestQueue(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(LogonActivity.this,HomeActivity.class);
                //intent.putExtra("sessionID","Dummy");
                //startActivity(intent);
                try {
                    logOnProgress.setVisibility(View.VISIBLE);
                    final String cName = nameField.getEditText().getText().toString();
                    final String cEmail = emailField.getEditText().getText().toString();
                    final String cPhone = phoneField.getEditText().getText().toString();
                    final String reqBody = "{name: '"+cName+"', email: '"+cEmail+"', phone: '"+cPhone+"'}";
                    JSONObject req = new JSONObject(reqBody);
                    //CacheHandler.saveFile("data.cache",getFilesDir(),reqBody);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url, req, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        logOnProgress.setVisibility(View.INVISIBLE);
                                        mTextView.setText(response.toString());
                                        Intent intent = new Intent(LogonActivity.this,HomeActivity.class);
                                        intent.putExtra("sessionID",response.get("session").toString());
                                        //final String data = "{name: '"+cName+"', email: '"+cEmail+"', phone: '"+cPhone+"', sessionID: '"+ response.get("session") +"'}";
                                        //CacheHandler.saveFile("data.cache",getFilesDir(),data);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    mTextView.setText(error.toString());

                                }
                            });
                    requestQueue.add(jsonObjectRequest);
                } catch (Exception e) {
                    mTextView.setText(e.toString());
                }
            }
        });
    }
}
