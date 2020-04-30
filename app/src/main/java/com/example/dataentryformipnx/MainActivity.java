package com.example.dataentryformipnx;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    Spinner spinner1;
    Button save;
    Button modify;
    Button newSheet;
    Button delete;
    EditText affectedPort, location, rFO;
    String text_affected_region = "";
    String text_type = "";
    TextView textView ;
    Button addImage;
    private int PICK_IMAGE_REQUEST = 1;


    String sUserImage ;
    Bitmap rBitMap;
//    String sRFO;
//    String sLocation;
//    String sAffected;
//    String sType;
//    String sAffectedRegion;

//    ProgressDialog loading1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.affectedRegion);
        spinner1 = findViewById(R.id.type);

        save = findViewById(R.id.save);
        modify = findViewById(R.id.modify);
        newSheet = findViewById(R.id.newSheet);
        delete = findViewById(R.id.delete);

        affectedPort = findViewById(R.id.affectedPONPort);
        location = findViewById(R.id.location);
        rFO = findViewById(R.id.rFO);
        addImage = findViewById(R.id.addImage);


//        textView = findViewById(R.id.textView);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Region_Arrays, R.layout.activity_spinner_color_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Type, R.layout.activity_spinner_color_layout);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                textView.setText(sUserImage);
                    addItemToSheet();
//                    Toast.makeText(MainActivity.this, "Clicked Save Data", Toast.LENGTH_SHORT).show();
                }




            });


        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),form_list.class);
                startActivity(intent);
//                Toast.makeText(MainActivity.this, "Clicked Modify Data", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Delete_Class.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Clicked Delete Data", Toast.LENGTH_SHORT).show();
            }
        });

        newSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked New Sheet", Toast.LENGTH_SHORT).show();

            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text_affected_region = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text_affected_region, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text_type = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text_type, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    public void addItemToSheet() {

        final String sRFO = rFO.getText().toString().trim();
        final String sLocation = location.getText().toString().trim();
        final String sAffected = affectedPort.getText().toString().trim();
        final String sType = text_type;
        final String sAffectedRegion = text_affected_region;

        Log.e("null", "values" + sUserImage);

        if (sUserImage == null) {
            sUserImage = "";

        }


        if ("" != sUserImage || !sRFO.isEmpty() || !sLocation.isEmpty() || !sAffected.isEmpty() || !sType.isEmpty() || !sAffectedRegion.isEmpty()) {

                final ProgressDialog loading = ProgressDialog.show(this, "Adding Data", "Please Wait");


                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        "https://script.google.com/macros/s/AKfycbwZDvjcxVbZ8uDP9XEbGy0ST87QyiUk0uQ4dWpSKlw3juO6Um2C/exec",

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                loading.dismiss();
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            }


                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                Toast.makeText(MainActivity.this, "Poor or No Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();


                        params.put("action", "addItem");
                        params.put("sAffectedRegion", sAffectedRegion);
                        params.put("sType", sType);
                        params.put("sAffected", sAffected);
                        params.put("sLocation", sLocation);
                        params.put("sRFO", sRFO);
                        params.put("sUserImage", sUserImage);


                        return params;
                    }
                };

                int socketTimeOut = 30000;

                RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);

                RequestQueue queue = Volley.newRequestQueue(this);

                queue.add(stringRequest);

        }
        else
            Toast.makeText(MainActivity.this, "No data input", Toast.LENGTH_LONG).show();
    }


    private void fileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select the Image"), PICK_IMAGE_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            Uri filePath = data.getData();
            Toast.makeText(this, "Image added Successfully", Toast.LENGTH_LONG).show();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                rBitMap = getResizedBitmap(bitmap,250);
                sUserImage = getStringImage(rBitMap);
            } catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();

        float bitMapRatio = (float) width / (float) height;

        if (bitMapRatio > 1){
            width = maxSize;
            height = (int) (width/bitMapRatio);

        }
        else {
            height = maxSize;
            width = (int) (height * bitMapRatio);

        }
        return Bitmap.createScaledBitmap(image,width,height,true);

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}

