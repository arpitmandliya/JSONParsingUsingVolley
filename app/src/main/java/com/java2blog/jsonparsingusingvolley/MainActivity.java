package com.java2blog.jsonparsingusingvolley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private Button btnSubmit;
    String responseText;
    Activity activity;
    ArrayList<Country> countries=new ArrayList<Country>();
    private ProgressDialog progressDialog;
    ListView listView;

   // In case if you deploy rest web service, then use below link and replace below ip address with yours
    //http://192.168.2.22:8080/JAXRSJsonExample/rest/countries
    
    //Direct Web services URL
    private String path = "https://cdn.rawgit.com/arpitmandliya/AndroidRestJSONExample/master/countries.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        listView = (ListView) findViewById(android.R.id.list);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countries.clear();

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Fetching conntry data");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //Call WebService
                getWebServiceResponseData();
            }
        });
    }

    protected Void getWebServiceResponseData() {
            JsonArrayRequest req = new JsonArrayRequest(jsonURl,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            // Parsing json response and iterate over each JSON object
                            Log.d(TAG, "data:" + responseText);
                            try {

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonobject = response.getJSONObject(i);
                                    int id = jsonobject.getInt("id");
                                    String country = jsonobject.getString("countryName");
                                    Log.d(TAG, "id:" + id);
                                    Log.d(TAG, "country:" + country);
                                    Country countryObj = new Country(id, country);
                                    countries.add(countryObj);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }

                            CustomCountryList customCountryList = new CustomCountryList(activity, countries);
                            listView.setAdapter(customCountryList);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    Toast.makeText(getApplicationContext(),"You Selected "+countries.get(position).getCountryName()+ " as Country",Toast.LENGTH_SHORT).show();        }
                            });
                         }
                        },   new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                            }

                        });
                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                        requestQueue.add(req);
            return null;
        }
    }
