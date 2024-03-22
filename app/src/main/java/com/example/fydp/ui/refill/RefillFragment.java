package com.example.fydp.ui.refill;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fydp.MainActivity;
import com.example.fydp.R;
import com.example.fydp.ui.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class RefillFragment extends Fragment {
    EditText quantityInput;
    Spinner spinner;
    Button submitButton;
    int quantity, pos;
    String selectedItem;

    public RefillFragment() {}

    public static RefillFragment newInstance() {
        RefillFragment fragment = new RefillFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refill, container, false);

        spinner = view.findViewById(R.id.spinner);


        String url = "http://10.0.2.2:4000/medications";
        AccessToken accessToken = (AccessToken) requireActivity().getApplication();
        String token = accessToken.getGlobalVariable();
        List<String> medNames = new ArrayList<>();
        List<String> medIds = new ArrayList<>();
        List<Integer> medBucket = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Extract data from each object
                                String name = jsonObject.getString("name");
                                int bucket = jsonObject.getInt("bucket");
                                String id = jsonObject.getString("id");

                                medNames.add(name);
                                medIds.add(id);
                                medBucket.add(bucket);

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                                        android.R.layout.simple_spinner_item, medNames);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
                                spinner.requestFocus();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // Add headers here
                    headers.put("Authorization", "Bearer " + token);
                    // Add other headers as needed
                    return headers;
                }
            };
            Volley.newRequestQueue(requireActivity()).add(request);

        // Find the Spinner within the inflated view


        quantityInput = view.findViewById(R.id.quantityInput);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected item from the adapter
                pos = position;
                selectedItem = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = quantityInput.getText().toString();
                if (!temp.isEmpty()){
                    quantity = Integer.parseInt(temp);
                }
                String id = medIds.get(pos);

//                Toast.makeText(requireActivity(), pos + ", " + quantity + ", " + id, Toast.LENGTH_LONG).show();


                AccessToken accessToken = (AccessToken) requireActivity().getApplication();
                String token = accessToken.getGlobalVariable();

                String url = "http://10.0.2.2:4000/medications/" + id;
//                Toast.makeText(requireActivity(), url, Toast.LENGTH_LONG).show();
                JSONObject postData = new JSONObject();
                try {
                    postData.put("quantityAdded", quantity);
                    // Add other key-value pairs as needed
                } catch (JSONException e) {
                    Toast.makeText(requireActivity(), "error1: " + e, Toast.LENGTH_LONG).show();
                }
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, postData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String message = response.getString("bucket");
                                    Toast.makeText(requireActivity(), "Place in bucket: " + message, Toast.LENGTH_LONG).show();

//                                    Toast.makeText(getActivity(), "Quantity: " + quantity +
//                                            ", Selected item: " + selectedItem, Toast.LENGTH_LONG).show();

                                    Intent intent=new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                } catch (JSONException e){
                                    Toast.makeText(requireActivity(), "error2: " + e, Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Wrong user or pass
                                Toast.makeText(requireActivity(), "API Error", Toast.LENGTH_LONG).show();
                                Log.d("My Error 3", error.toString());

                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        return headers;
                    }
                };

                Volley.newRequestQueue(requireActivity()).add(request);


            }
        });
        return view;
    }
}