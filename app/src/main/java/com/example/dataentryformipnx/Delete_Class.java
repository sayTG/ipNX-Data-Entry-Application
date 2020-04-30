package com.example.dataentryformipnx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Delete_Class extends AppCompatActivity {

    Button deleteButton;
    EditText editText;

    int deleteID;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_activity);

        deleteButton = findViewById(R.id.deleteButton);
        editText = findViewById(R.id.serialNumber);


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }

        });

    }

    private void deleteItem() {
        deleteID = Integer.parseInt(editText.getText().toString());

        final ProgressDialog loading = ProgressDialog.show(this, "Deleting Data", "Please Wait");


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://script.google.com/macros/s/AKfycbwZDvjcxVbZ8uDP9XEbGy0ST87QyiUk0uQ4dWpSKlw3juO6Um2C/exec",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        loading.dismiss();
                        Toast.makeText(Delete_Class.this, response, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }


                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(Delete_Class.this, "Poor or No Internet Connection", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put("action", "addItem");
                params.put("deleteID", Integer.toString(deleteID));
                return params;
            }
        };

        int socketTimeOut = 30000;

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);


    }


}







