package com.example.fydp.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fydp.R;
import com.example.fydp.databinding.FragmentHomeBinding;
import com.example.fydp.ui.AccessToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    Button pillButton1, pillButton2;

    public HomeFragment() {
        // Required empty public constructor

    }

    public static com.example.fydp.ui.home.HomeFragment newInstance() {
        com.example.fydp.ui.home.HomeFragment fragment = new com.example.fydp.ui.home.HomeFragment();
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        String url = "http://10.0.2.2:4000/doses";
        AccessToken accessToken = (AccessToken) requireActivity().getApplication();
        String token = accessToken.getGlobalVariable();
        List<String> medTimes = new ArrayList<>();
        List<String> medIds = new ArrayList<>();
        List<String> medMedicationIds = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String medicationId = jsonObject.getString("medicationId");
                                String time = jsonObject.getString("time");

                                medTimes.add(time);
                                medIds.add(id);
                                medMedicationIds.add(medicationId);
                            }
                            LinearLayout layout = rootView.findViewById(R.id.buttonLayout);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, // width
                                    LinearLayout.LayoutParams.WRAP_CONTENT // height
                            );

                            int backC = getResources().getColor(R.color.blue_500);
                            int textC = getResources().getColor(R.color.white);

                            for (int i = 0; i < medTimes.size(); i++) {
                                // Create a new button
                                Button button = new Button(requireActivity());
                                // Set button text
                                String time = medTimes.get(i);
                                String id = medIds.get(i);
                                String pillDosId = medIds.get(i);
                                String curMedId = medMedicationIds.get(i);
                                int indexOfT = time.indexOf('T');

                                String url2 = "http://10.0.2.2:4000/medications/" + curMedId;

                                String medNames;
                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject jsonObject) {
                                                try {
                                                    String name = jsonObject.getString("name");
                                                    String Ftime = time.substring(indexOfT + 1, indexOfT + 6);
                                                    button.setText(Ftime + " - " + name);
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
                                        Map<String, String> headers2 = new HashMap<>();
                                        // Add headers here
                                        headers2.put("Authorization", "Bearer " + token);
                                        // Add other headers as needed
                                        return headers2;
                                    }
                                };
                                Volley.newRequestQueue(requireActivity()).add(request);


                                if (i > 0) {
                                    layoutParams.setMargins(0, 16, 0, 0); // Top margin between buttons
                                }
                                button.setLayoutParams(layoutParams);
                                button.setBackgroundColor(backC);
                                button.setTextColor(textC);
//            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // Set text size in scaled pixels
                                button.setPadding(16, 16, 16, 16);


                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), Dispense.class);
                                        String Ftime = time.substring(indexOfT + 1, indexOfT + 6);
                                        intent.putExtra("pillTime", Ftime);
                                        intent.putExtra("pillDosId", pillDosId);
                                        intent.putExtra("pillMedId", curMedId);
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