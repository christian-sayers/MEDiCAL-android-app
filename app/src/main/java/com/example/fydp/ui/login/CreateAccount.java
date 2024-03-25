package com.example.fydp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fydp.MainActivity;
import com.example.fydp.R;
import com.example.fydp.ui.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {
    EditText nameInput, emailInput, passInput, phoneInput, emergNameInput, emergPhoneInput;
    TextView haveAccountText;
    Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);

//        FirebaseApp.initializeApp(this);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);
        phoneInput = findViewById(R.id.phoneInput);
        emergNameInput = findViewById(R.id.emergNameInput);
        emergPhoneInput = findViewById(R.id.emergPhoneInput);

        createAccountButton = findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String pass = passInput.getText().toString();
                String name = nameInput.getText().toString();
                String phone = phoneInput.getText().toString();
                String eName = emergNameInput.getText().toString();
                String ePhone = emergPhoneInput.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty() && !name.isEmpty() && !phone.isEmpty()
                && !eName.isEmpty() && !ePhone.isEmpty()) {
                    // Log in
                    String url = "http://10.0.2.2:4000/auth/register";
                    JSONObject postData = new JSONObject();
                    try {
                        JSONObject contactInformation = new JSONObject();
                        contactInformation.put("name", name);
                        contactInformation.put("phoneNumber", phone);

                        JSONObject emergencyContactInformation = new JSONObject();
                        emergencyContactInformation.put("name", eName);
                        emergencyContactInformation.put("phoneNumber", ePhone);

                        postData.put("email", email);
                        postData.put("password", pass);
                        postData.put("contactInformation", contactInformation);
                        postData.put("emergencyContactInformation", emergencyContactInformation);
                        // Add other key-value pairs as needed
                    } catch (JSONException e) {
                        Toast.makeText(CreateAccount.this, "error1: " + e, Toast.LENGTH_LONG).show();
                    }

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = response.getString("access_token");
//                                Toast.makeText(CreateAccount.this, message, Toast.LENGTH_LONG).show();
                                generateFirebaseToken(message);
                                Intent intent = new Intent(CreateAccount.this, LoginPage.class);
                                startActivity(intent);
                            } catch (JSONException e){
                                Toast.makeText(CreateAccount.this, "error2: " + e, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Wrong user or pass
                            //String message = error.getString("message");
                            Toast.makeText(CreateAccount.this, error.toString(), Toast.LENGTH_LONG).show();
                            Log.d("My Error 3", error.toString());
                        }
                    });

                    Volley.newRequestQueue(CreateAccount.this).add(request);
                    Intent intent = new Intent(CreateAccount.this, LoginPage.class);
                    startActivity(intent);
                }
            }
        });

        haveAccountText = findViewById(R.id.haveAccountText);
        haveAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccount.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }

    private void generateFirebaseToken(String accessToken) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isComplete()){
                    String token = task.getResult();
                    String url = "http://10.0.2.2:4000/auth/device-token";
                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("deviceToken", token);
                    }
                    catch (JSONException e) {

                    }
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + accessToken);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("My Error 3", error.toString());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            return headers;
                        }
                    };
                    Volley.newRequestQueue(CreateAccount.this).add(request);
                }
            }
        });
    }
}
