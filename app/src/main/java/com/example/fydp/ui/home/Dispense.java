package com.example.fydp.ui.home;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fydp.MainActivity;
import com.example.fydp.R;
import com.example.fydp.ui.AccessToken;
import com.example.fydp.ui.medications.Medicine;
import com.example.fydp.ui.refill.RefillFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Dispense extends AppCompatActivity {

    TextView pillName;
    Button retButton, button_circular;

    private void showPopupDialog(String pillName, int quantity, int dosage, final Dispense.OnDialogDismissedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dispense.this);
        if (quantity < (5 * dosage)){
            builder.setMessage("Your " + pillName + " will be dispensed shortly.\n" +
                            "You are running low on " + pillName + ". Please refill soon.")
                    .setCancelable(false) // prevents user from cancelling dialog by clicking outside
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Close the dialog
                            dialog.dismiss();
                            listener.onDismissed();
                        }
                    });
        }
        else {
            builder.setMessage("Your " + pillName + " will be dispensed shortly.\n")
                    .setCancelable(false) // prevents user from cancelling dialog by clicking outside
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Close the dialog
                            dialog.dismiss();
                            listener.onDismissed();
                        }
                    });
        }

        // Create the AlertDialog object and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    interface OnDialogDismissedListener {
        void onDismissed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dispense);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String pillDosId = intent.getStringExtra("pillDosId");
        String time = intent.getStringExtra("pillTime");
        String pillMedId = intent.getStringExtra("pillMedId");
        pillName = findViewById(R.id.textViewDisp1);

        AccessToken accessToken = (AccessToken) Dispense.this.getApplication();
        String token = accessToken.getGlobalVariable();
        String url2 = "http://10.0.2.2:4000/medications/" + pillMedId;

        String medNames;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String name = jsonObject.getString("name");
                            pillName.setText(time + " - " + name);
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
        Volley.newRequestQueue(Dispense.this).add(request);

        button_circular = findViewById(R.id.button_circular);
        button_circular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(requireActivity(), pos + ", " + quantity + ", " + id, Toast.LENGTH_LONG).show();

                String url = "http://10.0.2.2:4000/medications/dispense/dose/" + pillDosId;

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String medNames;
                                    int dosage = response.getInt("dosage");
                                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2, null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject jsonObject) {
                                                    try {
                                                        String name = jsonObject.getString("name");
                                                        int quan = jsonObject.getInt("quantity");
                                                        showPopupDialog(name, quan, dosage, new Dispense.OnDialogDismissedListener() {
                                                            @Override
                                                            public void onDismissed() {
                                                                Intent intent = new Intent(Dispense.this, MainActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        });
                                                    } catch (Exception e) {
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
                                    Volley.newRequestQueue(Dispense.this).add(request);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Wrong user or pass
                                Toast.makeText(Dispense.this, "API Error", Toast.LENGTH_LONG).show();
                                Log.d("My Error 3", error.toString());

                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        return headers;
                    }
                };

                Volley.newRequestQueue(Dispense.this).add(request);


            }
        });

        retButton = findViewById(R.id.retButton);
        retButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dispense.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}