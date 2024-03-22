package com.example.fydp.ui.medications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fydp.MainActivity;
import com.example.fydp.R;
import com.example.fydp.ui.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Medicine extends AppCompatActivity {
    TextView pillName;
    Button retButton, delButton;
    String name, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        pillName = findViewById(R.id.textViewDisp1);

        Intent intent = getIntent();
        name = "no value";
        if (intent != null) {
            name = intent.getStringExtra("pillName");
            id = intent.getStringExtra("id");
        }
        AccessToken accessToken = (AccessToken) Medicine.this.getApplication();
        String token = accessToken.getGlobalVariable();

        delButton = findViewById(R.id.deleteButton);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(requireActivity(), pos + ", " + quantity + ", " + id, Toast.LENGTH_LONG).show();

                String url = "http://10.0.2.2:4000/medications/" + id;

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(Medicine.this, "Successfully Deleted: " + pillName, Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(Medicine.this, MainActivity.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Wrong user or pass
                                Toast.makeText(Medicine.this, "API Error", Toast.LENGTH_LONG).show();
                                Log.d("My Error 3", error.toString());

                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        return headers;
                    }
                };

                Volley.newRequestQueue(Medicine.this).add(request);


            }
        });

        pillName.setText(name);
        retButton = findViewById(R.id.retButton);
        retButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Medicine.this, MainActivity.class);
                startActivity(intent);

//                FragmentManager fragmentManager = getSupportFragmentManager();
//
//                // Begin FragmentTransaction
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                // Replace current Fragment with Medications Fragment
//                fragmentTransaction.replace(R.id.fragment_container, new Medications());
//
//                // Commit the transaction
//                fragmentTransaction.commit();
            }
        });
    }
}