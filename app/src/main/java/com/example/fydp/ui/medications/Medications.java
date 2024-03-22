package com.example.fydp.ui.medications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fydp.R;
import com.example.fydp.ui.AccessToken;
import com.example.fydp.ui.home.Dispense;
import com.example.fydp.ui.login.LoginPage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Medications extends Fragment {
    Button pillButton1, pillButton2;

    public Medications() {
        // Required empty public constructor

    }

    public static Medications newInstance() {
        Medications fragment = new Medications();
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medications, container, false);

        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }

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
                            LinearLayout layout = rootView.findViewById(R.id.buttonLayout);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, // width
                                    LinearLayout.LayoutParams.WRAP_CONTENT // height
                            );

                            int backC = getResources().getColor(R.color.blue_500);
                            int textC = getResources().getColor(R.color.white);

                            for (int j = 0; j < medNames.size(); j++) {
                                // Create a new button
                                Button button = new Button(requireActivity());
                                // Set button text
                                String curName = medNames.get(j);
//            Toast.makeText(requireActivity(), name, Toast.LENGTH_LONG).show();
                                button.setText(curName);

                                if (j > 0) {
                                    layoutParams.setMargins(0, 16, 0, 0); // Top margin between buttons
                                }
                                button.setLayoutParams(layoutParams);
                                button.setBackgroundColor(backC);
                                button.setTextColor(textC);
                                button.setPadding(16, 16, 16, 16);

                                String pill = medNames.get(j);
                                String pillId = medIds.get(j);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), Medicine.class);
                                        intent.putExtra("pillName", pill);
                                        intent.putExtra("id", pillId);
//                                            intent.putExtra("token", token);
                                        startActivity(intent);
                                    }
                                });

                                layout.addView(button);
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

        return rootView;
    }
}