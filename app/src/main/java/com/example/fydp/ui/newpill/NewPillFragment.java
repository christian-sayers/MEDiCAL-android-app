package com.example.fydp.ui.newpill;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fydp.MainActivity;
import com.example.fydp.R;
import com.example.fydp.ui.AccessToken;
import com.example.fydp.ui.login.LoginPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewPillFragment extends Fragment {

    String medName, medDose, medQuan, timeHour, timeMin;
    Button submitPillButton, timeButton;
    EditText medNameInput, medDoseInput, medFreqInput, medQuanInput;
    TextView currentTime;
    JSONArray timesArray = new JSONArray();

    int curHr, curMin;
    Calendar calendar;

    public NewPillFragment() {
        // Required empty public constructor
    }

    public static NewPillFragment newInstance(String param1, String param2) {
        NewPillFragment fragment = new NewPillFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void showPopupDialog(String pillName, String quantity, String bucket, final OnDialogDismissedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Thank you for adding " + pillName + "!\n" +
                        "Please add " + quantity + " of " + pillName + " to bucket number " + bucket + "!")
                .setCancelable(false) // prevents user from cancelling dialog by clicking outside
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the dialog
                        dialog.dismiss();
                        listener.onDismissed();
                    }
                });

        // Create the AlertDialog object and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    interface OnDialogDismissedListener {
        void onDismissed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_pill, container, false);

        medNameInput = rootView.findViewById(R.id.medNameInput);
        medDoseInput = rootView.findViewById(R.id.medDoseInput);
        medFreqInput = rootView.findViewById(R.id.medFreqInput);
        medQuanInput = rootView.findViewById(R.id.medQuanInput);

        currentTime = rootView.findViewById(R.id.currentTime);
        timeButton = rootView.findViewById(R.id.timeButton);
        calendar = Calendar.getInstance();
        curHr = calendar.get(Calendar.HOUR);
        curMin = calendar.get(Calendar.MINUTE);

        //TO DO
        // - implement a way to only click submit if all values are filled out

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timesArray = new JSONArray();
                String editTextValue = medFreqInput.getText().toString();
                int numberOfDialogs;
                try {
                    numberOfDialogs = Integer.parseInt(editTextValue);

                    // Create and show TimePickerDialogs
                    for (int i = 0; i < numberOfDialogs; i++) {
                        Calendar calendar = Calendar.getInstance();
                        int curHr = calendar.get(Calendar.HOUR_OF_DAY);
                        int curMin = calendar.get(Calendar.MINUTE);

                        TimePickerDialog dialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                currentTime.setText(hourOfDay + ":" + minute);
                                if (hourOfDay < 10) {
                                    timeHour = "0" + (hourOfDay + 5);
                                } else if (hourOfDay > 18) {
                                    timeHour = String.valueOf(hourOfDay - 19);
                                } else {
                                    timeHour = String.valueOf(hourOfDay + 5);
                                }

                                if (minute < 10) {
                                    timeMin = "0" + minute;
                                } else {
                                    timeMin = String.valueOf(minute);
                                }
                                timesArray.put("2024-03-16T" + timeHour + ':' + timeMin + ":00.000Z");
                            }
                        }, curHr, curMin, false);
                        dialog.show();
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where the string cannot be parsed to an integer
                    // For example, show a toast message or prompt the user to enter a valid number
                }
            }
        });


        submitPillButton = rootView.findViewById(R.id.submitPillButton);
        submitPillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medName = medNameInput.getText().toString();
                medDose = medDoseInput.getText().toString();
                medQuan = medQuanInput.getText().toString();

                AccessToken accessToken = (AccessToken) requireActivity().getApplication();

                String token = accessToken.getGlobalVariable();

                String url = "http://10.0.2.2:4000/medications";
                JSONObject postData = new JSONObject();
                try {
//                    JSONArray timesArray = new JSONArray();
//                    timesArray.put("2024-03-16T" + timeHour + ':' + timeMin + ":00.000Z");
                    postData.put("name", medName);
                    postData.put("quantity", Integer.parseInt(medQuan));
                    postData.put("dosage", Integer.parseInt(medDose));
                    postData.put("times", timesArray);
                    // Add other key-value pairs as needed
                } catch (JSONException e) {
                    Toast.makeText(requireActivity(), "error1: " + e, Toast.LENGTH_LONG).show();
                }
//                Toast.makeText(requireActivity(), medName + " " + medQuan + " " + medDose + " " + timesArray, Toast.LENGTH_LONG).show();
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                        new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String message = response.getString("bucket");
                                        showPopupDialog(medName, medQuan, message, new OnDialogDismissedListener() {
                                            @Override
                                            public void onDismissed() {
                                                Intent intent=new Intent(getActivity(), MainActivity.class);
                                                startActivity(intent);
                                            }
                                        });
//                                        Toast.makeText(requireActivity(), "Place in bucket: " + message, Toast.LENGTH_LONG).show();
//                                        Intent intent=new Intent(getActivity(), MainActivity.class);
//                                        startActivity(intent);
                                    } catch (JSONException e){
                                        Toast.makeText(requireActivity(), "error2: " + e, Toast.LENGTH_LONG).show();
                                    }
                                }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Wrong user or pass
                                Toast.makeText(requireActivity(), "Please try again.", Toast.LENGTH_LONG).show();
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

        return rootView;
    }
}