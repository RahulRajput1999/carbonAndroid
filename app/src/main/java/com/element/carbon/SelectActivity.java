package com.element.carbon;

import android.content.Intent;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class SelectActivity extends AppCompatActivity {
    private BottomAppBar bottomAppBar;
    private RequestQueue requestQueue;
    private TextView productName, productBrand, productPrice, productDesc;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        bottomAppBar = (BottomAppBar) findViewById(R.id.bottomAppBarSelect);
        setSupportActionBar(bottomAppBar);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        final String sessionID = (String) bundle.get("sessionID");
        final String barcodeData = (String) bundle.get("barcodeData");
        requestQueue = Volley.newRequestQueue(this);
        productName = (TextView) findViewById(R.id.productName);
        productBrand = (TextView) findViewById(R.id.productBrand);
        productPrice = (TextView) findViewById(R.id.productPrice);
        productDesc = (TextView) findViewById(R.id.productDesc);
        getProduct(barcodeData);
        fab = (FloatingActionButton) findViewById(R.id.addCart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(sessionID,barcodeData);
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getProduct(String barcode) {
        String url = "http://192.168.43.113:3000/getProduct";
        String reqBody = "{barcode: " + barcode + "}";

        try {
            requestQueue = Volley.newRequestQueue(this);
            final JSONObject request = new JSONObject(reqBody);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getBoolean("status")) {
                                    JSONObject data = (JSONObject) response.get("data");
                                    productName.setText(data.getString("Name"));
                                    productBrand.setText(data.getString("Brand"));
                                    productPrice.setText(data.getString("Price"));
                                    productDesc.setText(data.getString("Details"));
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToCart(String sessionID, String barcode){
        try{
            String url = "http://192.168.43.113:3000/addToCart";
            requestQueue = Volley.newRequestQueue(this);
            final String reqBody = "{customerID:"+ sessionID +" ,barcode:"+ barcode +"}";
            JSONObject request = new JSONObject(reqBody);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {


                        }
                    });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {

        }
    }

}
