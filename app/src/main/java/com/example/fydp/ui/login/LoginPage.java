package com.example.fydp.ui.login;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fydp.MainActivity;
import com.example.fydp.R;
import com.example.fydp.ui.AccessToken;
import com.example.fydp.ui.login.CreateAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    EditText emailInput, passInput;
    TextView forgotPassText, createText;
    Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);
        signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String pass = passInput.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty()) {
                    // Log in
                    String url = "http://10.0.2.2:4000/auth/login";
                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("email", email);
                        postData.put("password", pass);
                        // Add other key-value pairs as needed
                    } catch (JSONException e) {
                        Toast.makeText(LoginPage.this, "error1: " + e, Toast.LENGTH_LONG).show();
                    }

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = response.getString("access_token");
//                                Toast.makeText(LoginPage.this, message, Toast.LENGTH_LONG).show();
                                AccessToken accessToken = (AccessToken) getApplication();
                                accessToken.setGlobalVariable(message);
                                generateFirebaseToken(message);
                                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e){
                                Toast.makeText(LoginPage.this, "error2: " + e, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Wrong user or pass
                            Toast.makeText(LoginPage.this, "Incorrect Credentials", Toast.LENGTH_LONG).show();
                            Log.d("My Error 3", error.toString());
                        }
                    });
                    Volley.newRequestQueue(LoginPage.this).add(request);
                }
                else{
                    Toast.makeText(LoginPage.this, "Incorrect Credentials", Toast.LENGTH_LONG).show();
                }
            }
        });

        createText = findViewById(R.id.createText);
        createText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(LoginPage.this, CreateAccount.class);
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
                    Volley.newRequestQueue(LoginPage.this).add(request);
                }
            }
        });
    }
}
