package com.example.dataentryformipnx;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class form_list extends AppCompatActivity {

    ListView listView;
    ListAdapter adapter;
    ProgressDialog progressDialog;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_list);

        listView = findViewById(R.id.list_view);
        getItems();

        imageView = findViewById(R.id.imageView);






    }

    private void getItems(){

        progressDialog = progressDialog.show(this, "Loading", "Please wait", false, true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://script.google.com/macros/s/AKfycbwZDvjcxVbZ8uDP9XEbGy0ST87QyiUk0uQ4dWpSKlw3juO6Um2C/exec?action=getItems",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(form_list.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        int socketTimeout = 30000;

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void parseItems(String jsonResponse){

        ArrayList<HashMap <String, String >> list = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for(int i =0; i < jsonArray.length(); i++){

                JSONObject object = jsonArray.getJSONObject(i);

                String sID = object.getString("sID");
                String sAffectedRegion = object.getString("sAffectedRegion");
                String sType = object.getString("sType");
                String sAffected = object.getString("sAffected");
                String sLocation = object.getString("sLocation");
                String sRFO = object.getString("sRFO");
                String sUserImage = object.getString("sUserImage");

                HashMap<String, String > item = new HashMap<>();
                item.put("sID", sID);
                item.put("sAffectedRegion", sAffectedRegion);
                item.put("sType", sType);
                item.put("sAffected", sAffected);
                item.put("sLocation", sLocation);
                item.put("sRFO", sRFO);
                item.put("sUserImage", sUserImage);

                list.add(item);
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }




        adapter = new List_Adapter(this, list,  R.layout.activity_form_row,
                new String[]{"sID", "sAffectedRegion", "sType", "sAffected", "sLocation", "sRFO"}, new int[]{R.id.textView_id,
                R.id.textView_region, R.id.textView_type, R.id.textView_pon, R.id.textView_location, R.id.textView_rfo});

        listView.setAdapter(adapter);

        progressDialog.dismiss();





    }
}
