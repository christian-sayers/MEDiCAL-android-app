package com.example.fydp.ui.medications;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    private AlertDialog alertDialog;

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

    private void showConfirmButtonPopup(String pillId, String pillName) {
        // Create a custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.confirm_button_popup_layout, null);

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        // Set up the confirm button
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
        Button denyButton = dialogView.findViewById(R.id.denyButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle confirm button click
                alertDialog.dismiss(); // Dismiss the dialog if needed
                deleteMedication(pillId, pillName);
            }
        });
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle confirm button click
                alertDialog.dismiss(); // Dismiss the dialog if needed
            }
        });
        // Create and show the dialog
        alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteMedication(String pillId, String pillName) {
        String url = "http://10.0.2.2:4000/medications/" + pillId;
        AccessToken accessToken = (AccessToken) requireActivity().getApplication();
        String token = accessToken.getGlobalVariable();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(requireActivity(), "Successfully Deleted: " + pillName, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(requireActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
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

                            RelativeLayout relativeLayout = rootView.findViewById(R.id.buttonLayout);
                            int buttonId = 1000;

                            int backC = getResources().getColor(R.color.blue_500);
                            int textC = getResources().getColor(R.color.white);

                            for (int j = 0; j < medNames.size(); j++) {
                                // Create a new button
                                Button button = new Button(requireActivity());
                                button.setId(buttonId);
                                buttonId++;
                                String curName = medNames.get(j);
                                button.setText(curName);

                                ImageButton trashButton = new ImageButton(requireActivity());
                                trashButton.setImageResource(R.drawable.trashcan_icon);

                                RelativeLayout.LayoutParams layoutParamsButton = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT
                                );
                                if (j > 0) {
                                    layoutParamsButton.addRule(RelativeLayout.BELOW, buttonId - 2);
                                    layoutParamsButton.setMargins(0,16,0,0);
                                }

                                layoutParamsButton.width = 560;
                                RelativeLayout.LayoutParams layoutParamsTrashButton = new RelativeLayout.LayoutParams(
                                        160,
                                        160
                                );
                                if (j > 0) { // Check if it's not the first button
                                    layoutParamsTrashButton.addRule(RelativeLayout.RIGHT_OF, buttonId - 1);
                                }
                                button.setLayoutParams(layoutParamsButton);
                                button.setBackgroundColor(backC);
                                button.setTextColor(textC);
                                button.setPadding(16, 16, 16, 16);

                                layoutParamsTrashButton.addRule(RelativeLayout.ALIGN_TOP, button.getId());
                                layoutParamsTrashButton.addRule(RelativeLayout.RIGHT_OF, button.getId());
                                layoutParamsTrashButton.addRule(RelativeLayout.CENTER_VERTICAL);

                                trashButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                trashButton.setBackgroundTintList(null);
                                layoutParamsTrashButton.setMargins(16, 0, 0, 0);
                                trashButton.setLayoutParams(layoutParamsTrashButton);

                                String pill = medNames.get(j);
                                String pillId = medIds.get(j);
//                                button.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//
//                                    }
//                                });
                                trashButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showConfirmButtonPopup(pillId, pill);
                                    }
                                });

                                relativeLayout.addView(button, layoutParamsButton);
                                relativeLayout.addView(trashButton, layoutParamsTrashButton);
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