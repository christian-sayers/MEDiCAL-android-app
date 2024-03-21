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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class RefillFragment extends Fragment {
    EditText quantityInput;
    Spinner spinner;
    Button submitButton;
    int quantity;

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
        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, medNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.requestFocus();

        quantityInput = view.findViewById(R.id.quantityInput);
        submitButton = view.findViewById(R.id.submitButton);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected item from the adapter

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String temp = quantityInput.getText().toString();
                        if (!temp.isEmpty()){
                            quantity = Integer.parseInt(temp);
                        }
                        String selectedItem = (String) parent.getItemAtPosition(position);
                        Toast.makeText(getActivity(), "Quantity: " + quantity +
                                ", Selected item: " + selectedItem, Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }

                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }
}